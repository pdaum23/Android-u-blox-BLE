package com.ublox.BLE;

import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.os.SystemClock;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import static com.ublox.BLE.MBP_STRUCTS.*;
import static com.ublox.BLE.services.BluetoothLeService.*;

public class Mbp {
    private final static String TAG = Mbp.class.getSimpleName();
    static boolean updatingFw = false;
    private final T_MBP_PACKET rxBuffer = new T_MBP_PACKET(BIP_RX_BUFFER_SIZE);
    private final T_MBP_PACKET txBuffer = new T_MBP_PACKET(BIP_TX_BUFFER_SIZE);
    private final ByteBuffer dataBuffer = ByteBuffer.allocate(BIP_MAXIMUM_RX_PAYLOAD);
    private final com.ublox.BLE.services.BluetoothLeService mBTService;
    boolean udl1InBootMode = false;
    private byte lastTxAddress = BMM_APP_ADDRESS;
    private boolean lastRxValid = false;
    private short rxBufferCrc = FCS16.FCS16_INIT;
    private RxStates rxState = RxStates.FlagSearch;
    private boolean msgReceived = false;
    //private byte lastTxMsgCode = (byte) (0xff);
    private byte lastRxMsgCode = (byte) (0xff);
    private byte ack = 0;
    private Timer mMbpQueue = new Timer(true);
    private T_FIRMWARE_HEADER udl1G1FwHeader;
    private T_FIRMWARE_HEADER udl1G2FwHeader;
    private ByteBuffer udl1G1FwBuffer;
    private ByteBuffer udl1G2FwBuffer;
    private ByteBuffer vincBuffer;

    public Mbp(com.ublox.BLE.services.BluetoothLeService service) {
        mBTService = service;
        dataBuffer.position(0);
        dataBuffer.mark();
        dataBuffer.order(ByteOrder.LITTLE_ENDIAN);
    }

    static IntentFilter MakeMbpUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        for (String action : MbpActionStrings) {
            intentFilter.addAction(action);
        }
        return intentFilter;
    }

    public MbpError RxProcess(ByteArrayInputStream bytesIn) {
        msgReceived = false;
        byte data;
        while (bytesIn.available() > 0) {
            data = (byte) bytesIn.read();
            if (data == MBP_FLAG) {
                if ((rxBuffer.getRawLength()) >= 5) {
                    if ((rxBufferCrc == FCS16.FCS16_GOOD) && (FCS16.fcs16_Verify(rxBuffer.getRawBuffer()))) {
                        rxBuffer.removeCrc(); // remove crc
                        if (DispatchPacket() != MbpError.Success) {
                            Log.e(TAG, "Dispatch packet error");
                        }
                    } else {
                        Log.e(TAG, "CRC Check failed");
                    }
                } /*else {
          Log.e(TAG,"Frame is too short, only "+rxBuffer.getRawBuffer()+" bytes");
        }*/
                rxBufferCrc = FCS16.FCS16_INIT;
                rxState = RxStates.GetData;
                rxBuffer.resetRaw();
            } else {
                switch (rxState) {
                    case FlagSearch:
                        Log.d(TAG, "Searching for flag...");
                        break;
                    case GetData:
                        if (data == MBP_ESCAPE) {
                            rxState = RxStates.GetEscData;
                        } else {
                            if (rxBuffer.getRawLength() < BIP_MAXIMUM_RX_PAYLOAD + 4) {
                                rxBuffer.appendPayload(data);
                                rxBufferCrc = FCS16.fcs16_AddByte(rxBufferCrc, data);
                            } else {
                                Log.d(TAG, "MBP maximum packet size exceeded");
                                rxState = RxStates.FlagSearch;
                            }
                        }
                        break;
                    case GetEscData:
                        if (rxBuffer.getRawLength() < BIP_MAXIMUM_RX_PAYLOAD + 4) {
                            byte tempData = (byte) (data ^ 0x20);
                            rxBuffer.appendPayload(tempData);
                            rxBufferCrc = FCS16.fcs16_AddByte(rxBufferCrc, tempData);
                            rxState = RxStates.GetData;
                            //Log.w(TAG,"Read escaped char");
                        } else {
                            Log.d(TAG, "MBP maximum packet size exceeded");
                            rxState = RxStates.FlagSearch;
                        }
                        break;

                    default:
                        rxState = RxStates.FlagSearch;
                        Log.d(TAG, "Unknown RxState = " + (rxState.rawValue));
                        break;
                }
            }
        }
        return MbpError.Success;
    }

    private MbpError Transmit() {
        //let pckBuf : NSMutableData = NSMutableData(bytes: [MBP_FLAG, txBuffer.address, txBuffer.protocolType, txBuffer.message], length: 4)
        ByteArrayOutputStream pckBuf = new ByteArrayOutputStream();

        lastRxValid = false;
        lastRxMsgCode = (byte) 0xff;
        //lastTxMsgCode = txBuffer.getMessageType();
        lastTxAddress = txBuffer.getAddress();

        pckBuf.write(MBP_FLAG);

        ByteBuffer temp = subByteBufferOf(txBuffer.getRawBuffer(), 0, txBuffer.getRawLength());

        //for byte : UInt8 in txBuffer.payload {
        if (temp == null) {
            return MbpError.Timeout;
        }
        for (byte b : temp.array()) {
            if (b == MBP_FLAG || b == MBP_ESCAPE) {
                pckBuf.write(MBP_ESCAPE);

                // send message if overflowing
                if (pckBuf.size() >= MBP_MAX_MSG_SIZE - 1) {
                    // phil mBTService.sendMessage(ByteBuffer.wrap(pckBuf.toByteArray()));
                    pckBuf.reset();
                }
                pckBuf.write(b ^ 0x20);
                //Log.w(TAG,"Escaping char");
            } else {
                pckBuf.write(b);
            }

            // send message if overflowing
            if (pckBuf.size() >= MBP_MAX_MSG_SIZE - 1) {
                // phil mBTService.sendMessage(ByteBuffer.wrap(pckBuf.toByteArray()));
                pckBuf.reset();
            }
        }

        pckBuf.write(MBP_FLAG);
        // phil mBTService.sendMessage(ByteBuffer.wrap(pckBuf.toByteArray()));
        return MbpError.Success;
    }

    private void ResetLastRxValid() {
        rxBuffer.reset();
        lastRxValid = false;
        msgReceived = false;
        ack = 0;
    }

    private MbpError DispatchPacket() {
        Log.d(TAG, "Dispatching packet: ln=" + rxBuffer.getRawLength() + ", addr=0x" + Integer.toHexString(rxBuffer.getAddress()) + ", prt=0x" + Integer.toHexString(rxBuffer.getProtocolType()) + ", msgType=0x" + Integer.toHexString((0x00ff & rxBuffer.getMessageType())));

        ByteBuffer payload = rxBuffer.getPayload();
        payload.flip();
        payload.order(ByteOrder.LITTLE_ENDIAN);

        // UDL App
        if ((rxBuffer.getAddress() == BMM_APP_ADDRESS) && (rxBuffer.getAddress() == lastTxAddress) && (rxBuffer.getProtocolType() == BMM_APP_PROTOCOL)) {
            lastRxMsgCode = rxBuffer.getMessageType();
            ack = payload.get();

            switch (lastRxMsgCode) {
                case MBP_PING_RESP:
                case MBP_GETVERSION_RESP:
                    udl1Status.appVersion = payload.getInt();
                    udl1Status.bootVersion = payload.getInt();
                    udl1Status.serialNumber = payload.getInt();
                    udl1Status.paramVersion = payload.get();
                    udl1Status.storageUsedBytes = payload.getInt();
                    udl1Status.storageTotalBytes = payload.getInt();
                    if (udl1Status.appVersion < 115) {
                        udl1Status.boardType = BOARDTYPE_G1;
                    } else {
                        udl1Status.boardType = payload.get();
                    }
                    break;

                case MBP_READEEPROM_RESP:
                    dataBuffer.put(payload);
                    break;

                case MBP_WRITEEEPROM_RESP:
                    break;

                case MBP_READFLASH_RESP:
                    dataBuffer.put(payload);
                    break;

                case MBP_WRITEFLASH_RESP:
                    break;

                case MBP_ERASEFLASH_RESP:
                    break;

                case MBP_READDF_RESP:
                    dataBuffer.put(payload);
                    break;

                case MBP_WRITEDF_RESP:
                    break;

                case MBP_ERASEDF_RESP:
                    break;

                case MBP_REBOOT_RESP:
                    // phil SendBroadcast(mBTService.getApplicationContext(), ACTION_MBP_NEW_VALUES, MBP_REBOOTED);
                    break;

                case MBP_LOCKINBOOT_RESP:
                    break;

                case MBP_GETAPPINFO_RESP:
                    udl1Status.appValid = payload.get();
                    udl1Status.appVersion = payload.getInt();
                    udl1Status.appChecksum = payload.getInt();
                    break;

                case MBP_GETGPSINFO_RESP:
                    udl1Status.gpsInfo.parse(payload);
                    break;

                case MBP_GETBATTERYINFO_RESP:
                    udl1Status.batteryVolts = payload.getFloat();
                    udl1Status.batteryTemperature = payload.getFloat();
                    udl1Status.batteryCharging = true;
                    //noinspection SimplifiableIfStatement
                    if(udl1Status.appVersion >= 116) {
                        udl1Status.batteryCharging = payload.get() != 0;
                    } else {
                        udl1Status.batteryCharging = true; // ** BUG IN THE UDL1 CODE v1.15- **
                    }
                    break;

                case MBP_GETFAULTS_RESP:
                    udl1Status.faults = payload.getInt();
                    if(udl1Status.appVersion >= 105) {
                        udl1Status.faultHistory.parse(payload);
                    } else {
                        udl1Status.faultHistory = new T_FAULT_HISTORY();
                    }
                    break;

                case MBP_POWERDOWN_RESP:
                    break;

                case MBP_SETRTC_RESP:
                    break;

                case MBP_GETRTC_RESP:
                    udl1Status.rtcTime = payload.getInt();
                    if(udl1Status.appVersion >= 105) {
                        udl1Status._1ppsCount = payload.getInt();
                        udl1Status._1ppsMsCount = payload.getShort();
                        udl1Status.rtcTimeLock = payload.get() != 0;
                    } else {
                        udl1Status._1ppsCount = 0;
                        udl1Status._1ppsMsCount = 0;
                        udl1Status.rtcTimeLock = false;
                    }
                    break;

                case MBP_CLEARSTORAGESTART_RESP:
                    break;

                case MBP_CLEARSTORAGESTATUS_RESP:
                    udl1Status.storageClearBusy = payload.get();
                    udl1Status.storageClearPercent = payload.get();
                    udl1Status.storageClearFailure = payload.get();
                    break;

                case MBP_SETSERIAL_RESP:
                    break;

                case MBP_SETINPUTRANGE_RESP:
                    break;

                case MBP_READRAWANALOG_RESP:
                    //udl1Status.rawMeasurement = data.readInt(&count)
                    break;

                case MBP_READSCALEDANALOG_RESP:
                    //udl1Status.scaledMeasurement = payload.getFloat();
                    break;

                case MBP_CLEARFAULTHISTORY_RESP:
                    break;

                default:
                    Log.d(TAG, String.format("No dispatch response to command 0x%02x", rxBuffer.getMessageType()));
            }

            // phil SendBroadcast(mBTService.getApplicationContext(), ACTION_MBP_NEW_VALUES, MbpError.Success);
            lastRxValid = true;
            msgReceived = true;

            // uDL BOOT
        } else if ((rxBuffer.getAddress() == BMM_APP_ADDRESS) && (rxBuffer.getAddress() == lastTxAddress) && (rxBuffer.getProtocolType() == UDL_BOOT_PROTOCOL)) {
            lastRxMsgCode = rxBuffer.getMessageType();
            ack = payload.get();

            switch (lastRxMsgCode) {
                case MBP_PING_RESP:
                case MBP_GETVERSION_RESP:
                    udl1Status.bootVersion = payload.getShort();
                    if(udl1Status.bootVersion > 105) {
                        udl1Status.boardType = payload.get();
                        udl1Status.udlType = payload.get();
                    } else {
                        udl1Status.boardType = BOARDTYPE_G1;
                        udl1Status.udlType = UDLTYPE_UDL1;
                    }
                    break;

                case MBP_READEEPROM_RESP:
                    dataBuffer.put(payload);
                    break;
                case MBP_WRITEEEPROM_RESP:
                    break;
                case MBP_READFLASH_RESP:
                    dataBuffer.put(payload);
                    break;
                case MBP_WRITEFLASH_RESP:
                    break;
                case MBP_ERASEFLASH_RESP:
                    break;
                case MBP_READDF_RESP:
                    dataBuffer.put(payload);
                    break;
                case MBP_WRITEDF_RESP:
                    break;
                case MBP_ERASEDF_RESP:
                    break;
                case MBP_REBOOT_RESP:
                    break;
                case MBP_LOCKINBOOT_RESP:
                    break;
                case MBP_GETAPPINFO_RESP:
                    udl1Status.appValid = payload.get();
                    udl1Status.appVersion = payload.getInt();
                    udl1Status.appChecksum = payload.getInt();
                    break;
                default:
                    Log.d(TAG, String.format("No dispatch response to command 0x%02x", rxBuffer.getMessageType()));
            }

            // phil SendBroadcast(mBTService.getApplicationContext(), ACTION_MBP_NEW_VALUES, MbpError.Success);
            lastRxValid = true;
            msgReceived = true;

            // NINA
        } else if ((rxBuffer.getAddress() == NINA_APP_ADDRESS) && (rxBuffer.getAddress() == lastTxAddress) && (rxBuffer.getProtocolType() == NINA_APP_PROTOCOL)) {
            lastRxMsgCode = rxBuffer.getMessageType();
            ack = payload.get();

            switch (lastRxMsgCode) {
                case MBP_PING_RESP:
                    udl1Status.ninaAppVersion = payload.get();
                    udl1Status.ninaSerialNumber = payload.getInt();
                    udl1Status.vincInBootloader = payload.get();
                    udl1Status.ninaBootVersion = payload.get();
                    break;
                case NINA_REBOOT_RESP:
                    // phil BusyScreen.update("Rebooting...", 60);
                    // phil SendBroadcast(mBTService.getApplicationContext(), ACTION_MBP_NEW_VALUES, MBP_REBOOTED);
                    break;
                case NINA_VII_LOCKINBOOT_RESP:
                    break;
                case NINA_VII_WRITE_RESP:
                    break;
                case NINA_VII_READ_RESP:
                    udl1Status.gpsInfo.parse(payload);
                    break;
                case NINA_VII_UNLOCKBOOT_RESP:
                    break;
                case NINA_LOCKINBOOT_RESP:
                    break;
                default:
                    Log.d(TAG, String.format("No dispatch response to command 0x%02x", rxBuffer.getMessageType()));
            }

            lastRxValid = true;
            msgReceived = true;
            // Vinculum II
        } else if ((rxBuffer.getAddress() == VINC_APP_ADDRESS) && (rxBuffer.getAddress() == lastTxAddress) && (rxBuffer.getProtocolType() == VINC_APP_PROTOCOL)) {
            lastRxMsgCode = rxBuffer.getMessageType();
            ack = payload.get();

            switch (rxBuffer.getMessageType()) {
                case MBP_PING_RESP:
                    udl1Status.vincAppVersion = payload.get();
                    udl1Status.vincUdlType = payload.get();
                    udl1Status.vincUdlBaudrate = payload.getInt();
                    break;

                default:
                    break;
            }
            // phil SendBroadcast(mBTService.getApplicationContext(), ACTION_MBP_NEW_VALUES, MbpError.Success);
            lastRxValid = true;
            msgReceived = true;

        }

        return MbpError.Success;
    }

    private MbpError Encapsulate(byte mbpAddress, byte mbpProtocol, byte message, ByteBuffer payload) {
        txBuffer.reset();
        txBuffer.setProtocolType(mbpProtocol);
        txBuffer.setAddress(mbpAddress);
        txBuffer.setMessageType(message);
        if (payload != null) {
            txBuffer.setPayload(payload);
        }
        short crc = FCS16.fcs16_Add(txBuffer.getRawBuffer());
        txBuffer.appendPayload((byte) FCS16.LOWBYTE(crc));
        txBuffer.appendPayload((byte) FCS16.HIGHBYTE(crc));
        return Transmit();
    }

    enum MbpError {
        Success(0),
        Timeout(1),
        Disconnected(2),
        CrcError(3),
        SizeMismatch(4),
        Eeprom(5),
        ParameterVersion(6);
        public int rawValue;

        MbpError(int rawValue) {
            this.rawValue = rawValue;
        }
    }

    private enum RxStates {
        FlagSearch(0),
        GetData(1),
        GetEscData(2);
        //Dispatch(3);
        public int rawValue;

        RxStates(int rawValue) {
            this.rawValue = rawValue;
        }
    }
}
