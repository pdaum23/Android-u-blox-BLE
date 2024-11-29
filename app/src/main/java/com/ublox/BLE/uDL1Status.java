package com.ublox.BLE;

import static com.ublox.BLE.MBP_STRUCTS.*;
import android.util.Log;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class uDL1Status {
    private final static String TAG = uDL1Status.class.getSimpleName();

    byte ninaAppVersion = 0;
    byte ninaBootVersion = 0;
    int ninaSerialNumber = 0;
    byte vincInBootloader = 0;
    byte vincAppVersion = 0;
    byte vincUdlType = UDLTYPE_NONE;
    int vincUdlBaudrate = 0;

    int appVersion = 0;
    int bootVersion = 0;
    int serialNumber = 0;
    byte paramVersion = 0;
    int storageUsedBytes = 0;
    int storageTotalBytes = 10000;
    byte boardType = BOARDTYPE_G1;
    byte udlType = UDLTYPE_NONE;

    //byte optionTypes = 0;
    byte appValid = 0;
    int appChecksum = 0;
    MBP_STRUCTS.T_GPSDATA_DEF gpsInfo = new MBP_STRUCTS.T_GPSDATA_DEF();
    float batteryVolts = 0;
    float batteryTemperature = 0;
    boolean batteryCharging = false;
    int faults = 0;
    T_FAULT_HISTORY faultHistory = new T_FAULT_HISTORY();
    int rtcTime = 0;
    int _1ppsCount = 0;
    short _1ppsMsCount = 0;
    boolean rtcTimeLock = false;
    byte storageClearBusy = 0;
    byte storageClearPercent = 0;
    byte storageClearFailure = 0;
    //rawMeasurement = 0
    //scaledMeasurement = 0
    //boolean g2Unit = false;
    MBP_STRUCTS.T_PARAM_PARAMETERS paramData = new MBP_STRUCTS.T_PARAM_PARAMETERS();
    MBP_STRUCTS.T_PARAM_PARAMETERS originalParamData = null;
    MBP_STRUCTS.T_PARAM_MFGDATA mfgData = new MBP_STRUCTS.T_PARAM_MFGDATA();
    // program variables
    boolean promptUdl1FwUpdate = true;
    boolean promptUdlbt1FwUpdate = true;


    // returns the bytes wanted for a configuration file
    byte[] composeConfigData() {
        ByteBuffer buffer = ByteBuffer.allocate(MBP_STRUCTS.T_PARAM_PARAMETERS.size+12);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        // phil buffer.putInt(Configurations.MAGIC_KEY_FOR_FILES);
        // phil buffer.putInt(Configurations.VERSION_KEY_FOR_FILES);
        buffer.putInt(serialNumber);
        buffer.put(paramData.compose());
        return buffer.array();
    }

    // 0  = success
    // -1 = parsing error
    // -2 = no param XL only
    enum LoadConfigDataErrors {
        Success, ParsingError, NoParamXlData
    }
    LoadConfigDataErrors loadConfigData(byte[] buffer) {
        try {
            ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
            int magicKey = byteBuffer.asIntBuffer().get();
            byteBuffer.position(byteBuffer.position() + 4);
            int versionKey = byteBuffer.asIntBuffer().get();
            byteBuffer.position(byteBuffer.position() + 4);
            int serial = byteBuffer.asIntBuffer().get();
            byteBuffer.position(byteBuffer.position() + 4);
            Log.d(TAG, "Serial of config data = " + serial);

            if (false) { // phil (magicKey != Configurations.MAGIC_KEY_FOR_FILES) {
                // phil Log.i(TAG, "Magic key mismatch! " + magicKey + " != expected " + Configurations.MAGIC_KEY_FOR_FILES);
                return LoadConfigDataErrors.ParsingError;
            }
            if (false) { // phil (versionKey > Configurations.VERSION_KEY_FOR_FILES) {
                // phil Log.i(TAG, "Version mismatch! " + versionKey + " > expected " + Configurations.VERSION_KEY_FOR_FILES);
                return LoadConfigDataErrors.ParsingError;
            }

            if (byteBuffer.capacity() - byteBuffer.position() == MBP_STRUCTS.T_PARAM_PARAMETERS.size) {
                ByteBuffer paramDataBuffer = subByteBufferOf(byteBuffer, byteBuffer.position(), MBP_STRUCTS.T_PARAM_PARAMETERS.size);
                byteBuffer.position(byteBuffer.position() + MBP_STRUCTS.T_PARAM_PARAMETERS.size);

                paramDataBuffer.position(paramDataBuffer.capacity());
                if (!FCS16.fcs16_Verify(paramDataBuffer)) {
                    Log.d(TAG, "CRC error in parameter data");
                    paramData = new MBP_STRUCTS.T_PARAM_PARAMETERS();
                    return LoadConfigDataErrors.ParsingError;
                }
                paramDataBuffer.position(0);
                paramData = new MBP_STRUCTS.T_PARAM_PARAMETERS(paramDataBuffer);
            } else {
                paramData = new MBP_STRUCTS.T_PARAM_PARAMETERS();
                return LoadConfigDataErrors.ParsingError;
            }
            return LoadConfigDataErrors.Success;
        } catch (RuntimeException e) {
            e.printStackTrace();
            paramData = new MBP_STRUCTS.T_PARAM_PARAMETERS();
            return LoadConfigDataErrors.ParsingError;
        }
    }

    boolean advancedUnit() {
        return false;
        // phil return IsFlagSet(mfgData.optionFlags, OPTIONFLAGS_GPS);
    }

    void clearData() {
        ninaAppVersion = 0;
        ninaSerialNumber = 0;
        vincInBootloader = 0;
        vincAppVersion = 0;
        vincUdlType = UDLTYPE_NONE;
        vincUdlBaudrate = 0;

        appVersion = 0;
        bootVersion = 0;
        serialNumber = 0;
        paramVersion = 0;
        storageUsedBytes = 0;
        storageTotalBytes = 10000;
        boardType = BOARDTYPE_G1;
        udlType = UDLTYPE_NONE;

        //optionTypes = 0;
        appValid = 0;
        appChecksum = 0;
        gpsInfo = new MBP_STRUCTS.T_GPSDATA_DEF();
        batteryVolts = 0;
        batteryTemperature = 0;
        batteryCharging = false;
        faults = 0;
        faultHistory = new T_FAULT_HISTORY();
        rtcTime = 0;
        _1ppsCount = 0;
        _1ppsMsCount = 0;
        rtcTimeLock = false;
        storageClearBusy = 0;
        storageClearPercent = 0;
        storageClearFailure = 0;
        //rawMeasurement = 0;
        //scaledMeasurement = 0;

        paramData = new MBP_STRUCTS.T_PARAM_PARAMETERS();
        originalParamData = null;
        mfgData = new MBP_STRUCTS.T_PARAM_MFGDATA();

        // program variables
        //promptUdl1FwUpdate = true;
        //promptUdlbt1FwUpdate = true;
    }
}
