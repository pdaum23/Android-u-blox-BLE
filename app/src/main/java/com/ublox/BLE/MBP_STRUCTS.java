package com.ublox.BLE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
//import androidx.annotation.Nullable;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.TimeZone;

public class MBP_STRUCTS {
    static public uDL1Status udl1Status = new uDL1Status();

    final static String MANUAL_NAME = "UDL1-MAN-001-201.pdf";
    final static String UDL1_G1_FW_NAME = "udl1_g1_118.bin";
    final static String UDL1_G2_FW_NAME = "udl1_g2_118.bin";
    // phil final static int UDLBT1_FW_NAME = R.raw.dfu_boot_v102_app_v102_sd_v0x101;
    final static String VINC_FW_NAME = "vincII_100.rom";

    final static int UDLBT_APPVERSION = 102;
    final static int VINC_APPVERSION = 100;
    final static String FOLDER_NAME = "/Mobiltex Data/";
    static final int FIRMWARE_DATALOG_BASE = 0x00040000; /* Datalog block occupies final 256K of flash */
    static final int FIRMWARE_DATALOG_MAXSIZE = 0x00040000; /* Datalog block size */

    static final int UDLBT_FIRMWARE_SIGNATURE_KEY = 0x32424952;
    static final int UDLBT_FIRMWARE_APP_MAXSIZE = 0x00027E00;
    static final int UDLBT_FIRMWARE_FLASH_BASE = 0x08000000;
    static final int UDLBT_FIRMWARE_APPUPDHEADER_BASE = 0x00038000;

    static final int UDL1_FIRMWARE_KILL_KEY = 0xAA55A55A;
    static final int UDL1_FIRMWARE_FLASH_BASE = 0x08000000;
    static final int UDL1_FIRMWARE_APPHEADER_BASE = 0x00008000;
    static final int UDL1_FIRMWARE_APPSTART_BASE = 0x00008200;

    static final int UDL1_FIRMWARE_G1_SIGNATURE_KEY = 0xF0615D85;
    static final int UDL1_FIRMWARE_G1_APP_MAXSIZE = 0x00017E00;
    static final int UDL1_FIRMWARE_G1_FLASH_END = 0x0801FFFF;

    static final int UDL1_FIRMWARE_G2_SIGNATURE_KEY = 0xF1615D85;
    static final int UDL1_FIRMWARE_G2_APP_MAXSIZE = 0x00037E00;
    static final int UDL1_FIRMWARE_G2_FLASH_END = 0x0803FFFF;

    static final int VINC_FIRMWARE_APP_MAXSIZE = 0x0001FFFF;
    static final int VINC_FIRMWARE_FLASH_BASE = 0x00000000;
    static final int VINC_FIRMWARE_PAGESIZE = 128;

    final static DecimalFormat decimalFormat = new DecimalFormat("0.000###");

    final static String EXTRA_MESSAGE =
            "com.mobiltex.EXTRA_MESSAGE";
    final static String EXTRA_TITLE =
            "com.mobiltex.EXTRA_TITLE";
    final static String EXTRA_DISCONNECT =
            "com.mobiltex.EXTRA_DISCONNECT";
    final static String EXTRA_ERROR =
            "com.mobiltex.EXTRA_ERROR";
    final static String AUTO_SET_UDL1_LOCATION =
            "com.mobiltex.AUTO_SET_UDL1_LOCATION";
    final static String PROMPT_UDLBT1_FW_UPDATE =
            "com.mobiltex.PROMPT_UDLBT1_FW_UPDATE";
    final static String PROMPT_UDL1_FW_UPDATE =
            "com.mobiltex.PROMPT_UDL1_FW_UPDATE";
    final static String FEATURE_ENABLE_DEBUG_CONSOLE =
            "com.mobiltex.EnableDebugConsole";
    final static String FEATURE_ENABLE_FACTORY_DEFAULTS_PROGRAMMING =
            "com.mobiltex.EnableFactoryDefaultsProgramming";
    final static String FEATURE_ENABLE_CHANNEL_MAP_EDITOR =
            "com.mobiltex.EnableChannelMapEditor";
    final static String FEATURE_ENABLE_CALIBRATION =
            "com.mobiltex.EnableCalibration";
    final static String FEATURE_ENABLE_SATELLITE_PARAMETER_EDITS =
            "com.mobiltex.EnableSatelliteParameterEdits";
    final static String FEATURE_ENABLE_GEOLOCATION_EDITS =
            "com.mobiltex.EnableGeolocationEdits";
    final static String FEATURE_ENABLE_AC_SENSE_EDITS =
            "com.mobiltex.EnableACSenseEdits";
    final static String FEATURE_ENABLE_4TO20MA_LOOP_EDITS =
            "com.mobiltex.Enable4To20mALoopEdits";
    final static String FEATURE_ENABLE_SCHEDULE_EDITS =
            "com.mobiltex.EnableScheduleEdits";
    final static String FEATURE_ENABLE_DOOR_ALARM_EDITS =
            "com.mobiltex.EnableDoorAlarmEdits";
    final static String FEATURE_ENABLE_ENABLE_LIMIT_EDITS =
            "com.mobiltex.EnableLimitEdits";
    final static String FEATURE_ENABLE_PEAK_HOLD_MODE =
            "com.mobiltex.EnablePeakHoldMode";
    final static String[] PROGRAMMERTYPE_NAMES = {"uDLBT1"};
    static final int PROGRAMMERTYPE_UDLBT1 = 0;
    final static SimpleDateFormat dateFormat = new SimpleDateFormat("E MMM dd, yyyy hh:mm:ss a Z", Locale.ENGLISH);
    //////////////////////////////////////////
    final static String[] MEASUREMENT_DESCRIPTION_IMAGES = {
            "singlecoupon",
            "dualcoupon",
            "singlecouponAcMit",
            "bond",
            "testpoint",
            "separatedcaccoupons",
    };
    static final String[] couponUnits = {
            "square meters",
            "square centimeters",
            "square millimeters",
            "square inches",
            "square feet",
    };
    static final int CALCULATOR_TYPE_NONE = 0;
    static final int CALCULATOR_TYPE_COUPON = 1;
    static final int CALCULATOR_TYPE_SHUNT = 2;
    static final String[] RANGE_STRINGS =
            {
                    "Low    (±150mV Peak)",
                    "Medium (  ±20V Peak)",
                    "High   ( ±150V Peak)",
            };
    static final String[] FAULT_STRINGS =
            {
                    "Configuration fault", //#define FAULT_CONFIG_INVALID       0x00000001
                    "GPS initialization fault", //#define FAULT_GPS_INIT              0x00000002
                    "EEPROM fault", //#define FAULT_EEPROM               0x00000004
                    "Battery fault", //#define FAULT_BATTERY               0x00000008
                    "ADC fault", //#define FAULT_ADC                   0x00000010
                    "Dataflash fault", //#define FAULT_DATAFLASH             0x00000020

                    "Calibration fault", //#define FAULT_CALIBRATION          0x00000040
                    "Dataflash write location not blank", //#define FAULT_DATAFLASH_FF         0x00000080

                    "Dataflash blank locate fault", //#define FAULT_DATAFLASH_FINDSTART 0x00000100
                    "Reserved 0x00000200", //Reserved                0x00000200
                    "Reserved 0x00000400", //Reserved                0x00000400
                    "Reserved 0x00000800", //Reserved                0x00000800

                    "Reserved 0x00001000", //Reserved                0x00001000
                    "Reserved 0x00002000", //Reserved                0x00002000
                    "Reserved 0x00004000", //Reserved                0x00004000
                    "Reserved 0x00008000", //Reserved                0x00008000

                    "Reserved 0x00010000", //Reserved                0x00010000
                    "Reserved 0x00020000", //Reserved                0x00020000
                    "Reserved 0x00040000", //Reserved                0x00040000
                    "Reserved 0x00080000", //Reserved                0x00080000

                    "Reserved 0x00100000", //Reserved                0x00100000
                    "Reserved 0x00200000", //Reserved                0x00200000
                    "Reserved 0x00400000", //Reserved                0x00400000
                    "Reserved 0x00800000", //Reserved                0x00800000

                    "Reserved 0x01000000", //Reserved                0x01000000
                    "Reserved 0x02000000", //Reserved                0x02000000
                    "Reserved 0x04000000", //Reserved                0x04000000
                    "Reserved 0x08000000", //Reserved                0x08000000

                    "Reserved 0x10000000", //Reserved                0x10000000
                    "Reserved 0x20000000", //Reserved                0x20000000
                    "Reserved 0x40000000", //Reserved                0x40000000
                    "Reserved 0x80000000" //Reserved                0x80000000
            };

    /***************
     * DEFINITIONS *
     ***************/

    final static String ACTION_MBP_NEW_VALUES =
            "com.mobiltex.mbp.ACTION_MBP_NEW_VALUES";
    final static String ACTION_MBP_FW_WRITE =
            "com.mobiltex.mbp.ACTION_MBP_FW_WRITE";
    final static String ACTION_MBP_ERROR =
            "com.mobiltex.mbp.ACTION_MBP_ERROR";
    final static String ACTION_MBP_SAVE =
            "com.mobiltex.mbp.ACTION_MBP_SAVE";
    final static String ACTION_MBP_UDL_NOT_RESPONDING =
            "com.mobiltex.mbp.ACTION_MBP_UDL_NOT_RESPONDING";
    final static String[] MbpActionStrings = {
            ACTION_MBP_NEW_VALUES, ACTION_MBP_ERROR, ACTION_MBP_SAVE,
            ACTION_MBP_FW_WRITE, ACTION_MBP_UDL_NOT_RESPONDING
    };
    final static String MBP_INITIAL_READ =
            "com.mobiltex.mbp.MBP_INITIAL_READ";
    final static String MBP_PGMFW_UPDATE_NEEDED =
            "com.mobiltex.mbp.MBP_PGMFW_UPDATE_NEEDED";
    final static String MBP_READ_PARAMS =
            "com.mobiltex.mbp.MBP_READ_PARAMS";
    final static String MBP_WRITE_PARAMS =
            "com.mobiltex.mbp.MBP_WRITE_PARAMS";
    final static String MBP_NEW_READINGS =
            "com.mobiltex.mbp.MBP_NEW_READINGS";
    final static String MBP_RECURRING_TASK =
            "com.mobiltex.mbp.MBP_RECURRING_TASK";
    final static String MBP_REBOOTED =
            "com.mobiltex.mbp.MBP_REBOOTED";
    /*final static String MBPPGM_REBOOTED =
        "com.mobiltex.mbp.MBPPGM_REBOOTED";*/
    final static String MBP_FAULTS_CLEARED =
            "com.mobiltex.mbp.MBP_FAULTS_CLEARED";
    /***************
     * DEFINITIONS *
     ***************/

    final static byte BIP_FLAG = 0x7E;
    final static byte BIP_ESCAPE = 0x7D;
    final static byte BIP_ADDRESS = 0;
    final static byte BIP_PROTOCOL = 1;
    final static byte BIP_MSGTYPE = 2;
    final static byte BIP_START_OF_PAYLOAD = 3;
    final static byte BMM_APP_ADDRESS = 0x15; // UDL
    final static byte BMM_APP_PROTOCOL = 0x01;
    final static byte UDL_BOOT_PROTOCOL = 0x00;
    final static byte NINA_APP_ADDRESS = 0x11; //NINA
    final static byte NINA_APP_PROTOCOL = 0x01;
    final static byte VINC_APP_ADDRESS = 0x12; //VINC
    final static byte VINC_APP_PROTOCOL = 0x01;
    final static int BIP_MAXIMUM_TX_PAYLOAD = 2048;
    final static int BIP_MAXIMUM_RX_PAYLOAD = 2048;
    final static int BIP_TX_BUFFER_SIZE = (2 * (BIP_MAXIMUM_TX_PAYLOAD + 5) + 2);
    final static int BIP_RX_BUFFER_SIZE = (BIP_MAXIMUM_RX_PAYLOAD + 5);
    final static int MBP_RX_TIMEOUT = 5000;
    final static int MBP_MAX_PAYLOAD = (BIP_MAXIMUM_TX_PAYLOAD - 8);
    final static int MBP_MAX_PACKET_SIZE = (BIP_MAXIMUM_TX_PAYLOAD + 5);
    final static byte MBP_FLAG = 0x7E;
    final static byte MBP_ESCAPE = 0x7D;
    final static int MBP_MAX_MSG_SIZE = BIP_MAXIMUM_TX_PAYLOAD;
    final static short MBP_MAX_EEPROM_READ = 125;
    final static int MBP_SETSERIALKEY = 0xAA551234;
    final static byte UDLTYPE_NONE = (byte) 255;
    final static byte UDLTYPE_UDL1 = 0;
    final static byte UDLTYPE_UDL2 = 1;
    final static byte UDLTYPE_UDL3 = 2;
    final static byte UDLTYPE_UDL4 = 3;
    final static byte UDLTYPE_UDL5 = 4;
    final static int SPI_FLASH_SECTOR_4K = 0x00;
    final static int SPI_FLASH_SECTOR_32K = 0x01;
    final static int SPI_FLASH_SECTOR_64K = 0x02;
    final static int SPI_FLASH_SECTOR_ALL = 0x03;
    //From VINC
    final static byte VINC_PING_RESP = (byte) 0x80;
    final static byte VINC_SETUDLBAUD_RESP = (byte) 0x81;
    final static byte VINC_RESETUDL_RESP = (byte) 0x82;
    final static byte VINC_CONFIG_RESP = (byte) 0x83;
    //To VINC
    final static byte VINC_PING = 0x00;
    final static byte VINC_SETUDLBAUD = 0x01;
    final static byte VINC_RESETUDL = 0x02;
    final static byte VINC_CONFIG = 0x03;
    //Programmer modes
    final static byte MBP_MODE_RMU3 = 0x00;
    final static byte MBP_MODE_RMU2 = 0x01;
    final static byte MBP_MODE_RMU2FIRM = 0x02;
    final static byte MBP_MODE_GTX2FIRM = 0x03;
    final static byte MBP_MODE_RMU1 = 0x04;
    //To PC From Programmer
    final static byte NINA_VII_LOCKINBOOT_RESP = (byte) 0x81;
    final static byte NINA_VII_WRITE_RESP = (byte) 0x82;
    final static byte NINA_VII_READ_RESP = (byte) 0x83;
    final static byte NINA_VII_UNLOCKBOOT_RESP = (byte) 0x84;
    final static byte NINA_REBOOT_RESP = (byte) 0x85;
    final static byte NINA_LOCKINBOOT_RESP = (byte) 0x86;
    final static byte NINA_SETSERIAL_RESP = (byte) 0x87;
    //From PC To Programmer
    final static byte NINA_VII_LOCKINBOOT = 0x01;
    final static byte NINA_VII_WRITE = 0x02;
    final static byte NINA_VII_READ = 0x03;
    final static byte NINA_VII_UNLOCKBOOT = 0x04;
    final static byte NINA_REBOOT = 0x05;
    final static byte NINA_LOCKINBOOT = 0x06;
    final static byte NINA_SETSERIAL = 0x07;
    //uDL1 Protocol Numbers
//To PC
    final static byte MBP_PING_RESP = (byte) 0x80;
    final static byte MBP_GETVERSION_RESP = (byte) 0x81;
    final static byte MBP_READEEPROM_RESP = (byte) 0x82;
    final static byte MBP_WRITEEEPROM_RESP = (byte) 0x83;
    final static byte MBP_ERASEFLASH_RESP = (byte) 0x84;
    final static byte MBP_READFLASH_RESP = (byte) 0x85;
    final static byte MBP_WRITEFLASH_RESP = (byte) 0x86;
    final static byte MBP_ERASEDF_RESP = (byte) 0x87;
    final static byte MBP_READDF_RESP = (byte) 0x88;
    final static byte MBP_WRITEDF_RESP = (byte) 0x89;
    final static byte MBP_REBOOT_RESP = (byte) 0x8a;
    final static byte MBP_LOCKINBOOT_RESP = (byte) 0x8b;
    final static byte MBP_GETAPPINFO_RESP = (byte) 0x8c;
    final static byte MBP_GETGPSINFO_RESP = (byte) 0x8d;
    final static byte MBP_GETBATTERYINFO_RESP = (byte) 0x8e;
    final static byte MBP_GETFAULTS_RESP = (byte) 0x8f;
    final static byte MBP_POWERDOWN_RESP = (byte) 0x90;
    final static byte MBP_SETTESTMODE_RESP = (byte) 0x91;
    final static byte MBP_GETALLINPUTS_RESP = (byte) 0x92;
    final static byte MBP_SETPORTOUTPUT_RESP = (byte) 0x93;
    final static byte MBP_SETGPSPWR_RESP = (byte) 0x94;
    final static byte MBP_SETRTC_RESP = (byte) 0x95;
    final static byte MBP_GETRTC_RESP = (byte) 0x96;
    final static byte MBP_CLEARSTORAGESTART_RESP = (byte) 0x97;
    final static byte MBP_CLEARSTORAGESTATUS_RESP = (byte) 0x98;
    final static byte MBP_SETSERIAL_RESP = (byte) 0x99;
    final static byte MBP_SETINPUTRANGE_RESP = (byte) 0x9A;
    final static byte MBP_READRAWANALOG_RESP = (byte) 0x9B;
    final static byte MBP_READSCALEDANALOG_RESP = (byte) 0x9C;
    final static byte MBP_CLEARFAULTHISTORY_RESP = (byte) 0x9D;
    //From PC
    final static byte MBP_PING = 0x00;
    final static byte MBP_GETVERSION = 0x01;
    final static byte MBP_READEEPROM = 0x02;
    final static byte MBP_WRITEEEPROM = 0x03;
    final static byte MBP_ERASEFLASH = 0x04;
    final static byte MBP_READFLASH = 0x05;
    final static byte MBP_WRITEFLASH = 0x06;
    final static byte MBP_ERASEDF = 0x07;
    final static byte MBP_READDF = 0x08;
    final static byte MBP_WRITEDF = 0x09;
    final static byte MBP_REBOOT = 0x0a;
    final static byte MBP_LOCKINBOOT = 0x0b;
    final static byte MBP_GETAPPINFO = 0x0c;
    final static byte MBP_GETGPSINFO = 0x0d;
    final static byte MBP_GETBATTERYINFO = 0x0e;
    final static byte MBP_GETFAULTS = 0x0f;
    final static byte MBP_POWERDOWN = 0x10;
    final static byte MBP_SETTESTMODE = 0x11;
    final static byte MBP_GETALLINPUTS = 0x12;
    final static byte MBP_SETPORTOUTPUT = 0x13;
    final static byte MBP_SETGPSPWR = 0x14;
    final static byte MBP_SETRTC = 0x15;
    final static byte MBP_GETRTC = 0x16;
    final static byte MBP_CLEARSTORAGESTART = 0x17;
    final static byte MBP_CLEARSTORAGESTATUS = 0x18;
    final static byte MBP_SETSERIAL = 0x19;
    final static byte MBP_SETINPUTRANGE = 0x1A;
    final static byte MBP_READRAWANALOG = 0x1B;
    final static byte MBP_READSCALEDANALOG = 0x1C;
    final static byte MBP_CLEARFAULTHISTORY = 0x1D;
    /* Dataflash Sector erase sizes */
    static final byte DATAFLASH_SECTOR_4K = (byte) 0;
    static final byte DATAFLASH_SECTOR_32K = (byte) 1;
    static final byte DATAFLASH_SECTOR_64K = (byte) 2;
    static final byte DATAFLASH_SECTOR_ALL = (byte) 3;

    //EEPROM locations
    static final short EEPROM_SIZE = 0x1000;
    static final short EEPROM_MFG_OFFSET = 0x0800; /*2K offset from start of EEPROM*/
    static final short EEPROM_FAULTQUEUE_OFFSET = 0x0c00; /*3K offset from start of EEPROM*/
    static final short EEPROM_CELL_OFFSET = 0x1000; /*4K offset from start of EEPROM*/
    static final short EEPROM_SERIAL_NUM = (EEPROM_SIZE - 4);
    static final short EEPROM_TEST_COMPLETE = (EEPROM_SIZE - 8);
    static final short EEPROM_TEST_VERSION = (EEPROM_SIZE - 12);
    final static int REQUEST_LOCATION = 199;
    final static int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;
    final static int MY_PERMISSIONS_REQUEST_BLUETOOTH = 2;
    final static int BT_ADDR_SIZE = 6;
    final static int UDL1_PARAM_EEPROM_SIZE = 0x1000;
    final static int UDL1_PARAM_SERIALNUM_OFFSET = (UDL1_PARAM_EEPROM_SIZE - 4);
    final static int UDL1_PARAM_MFGTESTTIME_OFFSET = (UDL1_PARAM_EEPROM_SIZE - 8);
    final static int UDL1_PARAM_MFGTESTDONE_OFFSET = (UDL1_PARAM_EEPROM_SIZE - 12);
    final static int UDL1_PARAM_CAPDETECTPARAM_OFFSET = (UDL1_PARAM_EEPROM_SIZE - 32);
    final static byte PGM_PARAM_VERSION = 0;
    final static int FAULT_HISTORY_SIZE = 8;
    final static byte BOARDTYPE_UNKNOWN = -1;
    final static byte BOARDTYPE_G1 = 0;
    final static byte BOARDTYPE_G2 = 1;
    final static byte BOARDTYPE_G3 = 2;
    final static int UDL1_BOOT_BAUDRATE = 921600;
    final static int UDL1_APP_BAUDRATE = 1000000;
    final static int PARAM_MFG_OFFSET = 0x800; /*2K offset from start of EEPROM*/
    final static int PARAM_OFFSET = 0x00;
    final static int CAL_RANGE_COUNT = 3;
    final static byte PARAM_MFG_VERSION = 0;
    final static int OPTIONFLAGS_GPS = 0x00000001;
    final static int OPTIONFLAGS_EXTMEM = 0x00000002;
    final static int OPTIONFLAGS_NEWCAL = 0x40000000;
    final static int OPTIONFLAGS_PROTOTYPE = 0x80000000;
    final static int PARAM_PARAM_OFFSET = 0x0000;
    final static byte PARAM_VERSION = 0;
    final static short CONFIGFLAGS_DCENABLED = 0x0001;
    final static short CONFIGFLAGS_ACENABLED = 0x0002;
    final static short CONFIGFLAGS_4TO20MA = 0x0004;
    final static short CONFIGFLAGS_LOWLIMITDC_ENABLED = 0x0008;
    final static short CONFIGFLAGS_HIGHLIMITDC_ENABLED = 0x0010;
    final static short CONFIGFLAGS_LOWLIMITAC_ENABLED = 0x0020;
    final static short CONFIGFLAGS_HIGHLIMITAC_ENABLED = 0x0040;
    final static short CONFIGFLAGS_FASTSAMPLING_ENABLED = 0x0080;
    final static short CONFIGFLAGS_INTERRUPTIONTRACKING_ENABLED = 0x0100;
    final static short CONFIGFLAGS_TEMPERATURE_ENABLED = 0x0200;
    final static short CONFIGFLAGS_TEMPERATURE_FAHRENHEIT = 0x0400;
    final static short CONFIGFLAGS_DC_AMPS = 0x0800;
    final static short CONFIGFLAGS_AC_AMPS = 0x1000;
    final static short CONFIGFLAGS_REQUIREGPSLOCK_ENABLED = 0x2000;
    final static short CONFIGFLAGS_FASTSAMPLINGCONTINUOUS_ENABLED = 0x4000;
    final static short CONFIGFLAGS_NOLONGTERMGPSSYNC_ENABLED = (short) 0x8000;
    final static short CONFIGFLAGSEXT_SHOWTIME = 0x0001;
    final static short CONFIGFLAGSEXT_LOCALTIME = 0x0002;
    final static short CONFIGFLAGSEXT_EHPEWAIT = 0x0004;
    final static short CONFIGFLAGSEXT_LOCKFASTSAMPLING_100MS = 0x0008; /*Start fast sampling relative to a 100ms boundary*/
    final static int PARAM_CHANNEL_DC = 0;
    final static int PARAM_CHANNEL_AC = 1;
    final static int PARAM_NOTES_SIZE = 105;
    private final static String TAG = Mbp.class.getSimpleName();

    static int BV(int val) { // bit value
        return (1 << val);
    }
    interface T_STRUCT<T> extends Comparable<T> {
        int size = 0;

        void parse(ByteBuffer buffer);

        ByteBuffer compose();

        int compareTo(@Nullable T that);
    }

    final static class T_FIRMWARE_HEADER implements Comparable<T_FIRMWARE_HEADER> {
        final static int size = 512;
        int signature = 0;
        int version = 0;
        int length = 0;
        int checksum = 0;
        byte notes[] = new byte[128];
        int fill[] = new int[25 + 64];
        int forceUpdate = 0;
        int enabled = 0;
        int headerChecksum = 0;

        T_FIRMWARE_HEADER() {}

        T_FIRMWARE_HEADER(final ByteBuffer buffer) {
            if (buffer.limit() < size) return;
            ByteBuffer headerBuffer = subByteBufferOf(buffer, 0, size);
            signature = headerBuffer.getInt();
            version = headerBuffer.getInt();
            length = headerBuffer.getInt();
            checksum = headerBuffer.getInt();
            headerBuffer.get(notes, 0, 128);
            for (int i = 0; i < 25 + 64; i++)
                fill[i] = headerBuffer.getInt();
            forceUpdate = headerBuffer.getInt();
            enabled = headerBuffer.getInt();
            headerChecksum = headerBuffer.getInt();
        }

        @Override
        public int compareTo(@Nullable T_FIRMWARE_HEADER that) {
            if (that == null) {
                return 1;
            }
            int ret = 0;
            ret += this.signature == that.signature ? 0 : 1;
            ret += this.version == that.version ? 0 : 1;
            ret += this.length == that.length ? 0 : 1;
            ret += this.checksum == that.checksum ? 0 : 1;
            ret += Arrays.equals(this.notes, that.notes) ? 0 : 1;
            ret += Arrays.equals(this.fill, that.fill) ? 0 : 1;
            ret += this.forceUpdate == that.forceUpdate ? 0 : 1;
            ret += this.enabled == that.enabled ? 0 : 1;
            ret += this.headerChecksum == that.headerChecksum ? 0 : 1;
            return ret;
        }
    }

    static ByteBuffer subByteBufferOf(ByteBuffer buffer, int startPoint, int count) {
        ByteBuffer ret = ByteBuffer.allocate(count);
        ret.order(ByteOrder.LITTLE_ENDIAN);
        //if (buffer.capacity() - startPoint < count) return null;
        for (int i = 0; i < count; i++) {
            if ((i + startPoint) < buffer.capacity())
                ret.put(buffer.get(i + startPoint));
            else
                ret.put((byte) 0);
        }
        ret.position(0);
        return ret;
    }

    final static class T_FAULT_HISTORY implements T_STRUCT<T_FAULT_HISTORY> {
        final static int size = 64;

        int time[] = new int[FAULT_HISTORY_SIZE];
        int faultCode[] = new int[FAULT_HISTORY_SIZE];

        @Override
        public int compareTo(@Nullable T_FAULT_HISTORY that) {
            if (that == null) {
                return 1;
            }
            int ret = 0;
            ret += Arrays.equals(this.time, that.time) ? 1 : 0;
            ret += Arrays.equals(this.faultCode, that.faultCode) ? 1 : 0;
            return ret;
        }

        @Override
        public void parse(ByteBuffer buffer) {
            int startPosition = buffer.position();
            for (int i = 0; i < FAULT_HISTORY_SIZE; i++) {
                time[i] = buffer.getInt();
            }
            for (int i = 0; i < FAULT_HISTORY_SIZE; i++) {
                faultCode[i] = buffer.getInt();
            }
            if (buffer.position() - startPosition != size) {
                Log.e(TAG, "Parsing error, mismatched size. Position " + buffer.position() + " != Size " + size);
            }
        }

        @Override
        public ByteBuffer compose() {
            ByteBuffer buffer = ByteBuffer.allocate(size);
            buffer.order(ByteOrder.LITTLE_ENDIAN);

            for (int i = 0; i < FAULT_HISTORY_SIZE; i++) {
                buffer.putInt(time[i]);
            }
            for (int i = 0; i < FAULT_HISTORY_SIZE; i++) {
                buffer.putInt(faultCode[i]);
            }

            if (buffer.position() != size) {
                Log.e(TAG, "compose() size mismatch!");
                return null;
            }
            buffer.position(0);
            return buffer;
        }

        int count() {
            int i = 0;
            for (int faultTime : time) {
                if (faultTime > 0) {
                    i += 1;
                }
            }
            return i;
        }
    }

    final static class T_CHANNEL_CALIBRATION implements T_STRUCT<T_CHANNEL_CALIBRATION> {
        static final int size = 18;

        short zeroCal[] = new short[CAL_RANGE_COUNT];
        float scaleFactor[] = new float[CAL_RANGE_COUNT];

        T_CHANNEL_CALIBRATION() {
        }

        T_CHANNEL_CALIBRATION(T_CHANNEL_CALIBRATION that) {
            System.arraycopy(that.zeroCal, 0, this.zeroCal, 0, CAL_RANGE_COUNT);
            System.arraycopy(that.scaleFactor, 0, this.scaleFactor, 0, CAL_RANGE_COUNT);
        }

        T_CHANNEL_CALIBRATION(short zeroCal[], float scaleFactor[]) {
            System.arraycopy(zeroCal, 0, this.zeroCal, 0, CAL_RANGE_COUNT);
            System.arraycopy(scaleFactor, 0, this.scaleFactor, 0, CAL_RANGE_COUNT);
        }

        @Override
        public void parse(ByteBuffer buffer) {
            int startPosition = buffer.position();
            for (int i = 0; i < zeroCal.length; i++) {
                zeroCal[i] = buffer.getShort();
            }
            for (int i = 0; i < scaleFactor.length; i++) {
                scaleFactor[i] = buffer.getFloat();
            }
            if (buffer.position() - startPosition != size) {
                Log.e(TAG, "Parsing error, mismatched size. Position " + buffer.position() + " != Size " + size);
            }
        }

        @Override
        public ByteBuffer compose() {
            ByteBuffer buffer = ByteBuffer.allocate(size);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            for (short aZeroCal : zeroCal) {
                buffer.putShort(aZeroCal);
            }
            for (float aScaleFactor : scaleFactor) {
                buffer.putFloat(aScaleFactor);
            }
            if (buffer.position() != size) {
                Log.e(TAG, "compose() size mismatch!");
                return null;
            }
            buffer.position(0);
            return buffer;
        }

        @Override
        public int compareTo(@Nullable T_CHANNEL_CALIBRATION that) {
            if (that == null) {
                return 1;
            }
            int ret = 0;
            ret += Arrays.equals(this.zeroCal, that.zeroCal) ? 0 : 1;
            ret += Arrays.equals(this.scaleFactor, that.scaleFactor) ? 0 : 1;
            return ret;
        }
    }

    final static class T_GPSDATA_DEF implements T_STRUCT<T_GPSDATA_DEF> {
        static final int size = 60;

        short utcYear = 0;
        byte utcMonth = 0;
        byte utcDay = 0;
        byte utcHours = 0;
        byte utcMinutes = 0;
        byte utcSeconds = 0;
        short utcMilliSeconds = 0;

        int latitude = 0;
        int longitude = 0;
        int altitude = 0;

        byte satsInFix = 0;
        byte satsTracked = 0;

        short solValid = 0;
        short hpodValid = 0;

        byte timeMarkSeconds = 0;
        byte timeMarkValid = 0;
        byte timeMarkNew = 0;

        short gpsWeek = 0;

        byte[] svid = new byte[12];
        byte[] snr = new byte[12];

        short hpod = 0;
        short ehpe = 0;

        T_GPSDATA_DEF() {
        }

        public ByteBuffer compose() {
            ByteBuffer buffer = ByteBuffer.allocate(size);
            buffer.order(ByteOrder.LITTLE_ENDIAN);

            if (buffer.position() != size) {
                Log.e(TAG, "compose() size mismatch!");
                return null;
            }
            buffer.position(0);
            return buffer;
        }

        public void parse(ByteBuffer buffer) {
            int startPosition = buffer.position();
            utcYear = buffer.getShort();
            utcMonth = buffer.get();
            utcDay = buffer.get();
            utcHours = buffer.get();
            utcMinutes = buffer.get();
            utcSeconds = buffer.get();
            utcMilliSeconds = buffer.getShort();
            latitude = buffer.getInt();
            longitude = buffer.getInt();
            altitude = buffer.getInt();
            satsInFix = buffer.get();
            satsTracked = buffer.get();
            solValid = buffer.getShort();
            hpodValid = buffer.getShort();
            timeMarkSeconds = buffer.get();
            timeMarkValid = buffer.get();
            timeMarkNew = buffer.get();
            gpsWeek = buffer.getShort();

            for (int i = 0; i < 12; i++) {
                svid[i] = buffer.get();
            }
            for (int i = 0; i < 12; i++) {
                snr[i] = buffer.get();
            }
            if(udl1Status.appVersion >= 111) {
                hpod = buffer.getShort();
                ehpe = buffer.getShort();
            } else {
                hpod = 0;
                ehpe = 0;
            }

            if (buffer.position() - startPosition != size) {
                Log.e(TAG, "Parsing error, mismatched size. Position " + buffer.position() + " != Size " + size);
            }
        }

        @Override
        public int compareTo(@Nullable T_GPSDATA_DEF that) {
            if (that == null) {
                return 1;
            }
            int ret = 0;
            ret += this.utcYear == that.utcYear ? 0 : 1;
            ret += this.utcMonth == that.utcMonth ? 0 : 1;
            ret += this.utcDay == that.utcDay ? 0 : 1;
            ret += this.utcHours == that.utcHours ? 0 : 1;
            ret += this.utcMinutes == that.utcMinutes ? 0 : 1;
            ret += this.utcSeconds == that.utcSeconds ? 0 : 1;
            ret += this.utcMilliSeconds == that.utcMilliSeconds ? 0 : 1;
            ret += this.latitude == that.latitude ? 0 : 1;
            ret += this.longitude == that.longitude ? 0 : 1;
            ret += this.altitude == that.altitude ? 0 : 1;
            ret += this.satsInFix == that.satsInFix ? 0 : 1;
            ret += this.satsTracked == that.satsTracked ? 0 : 1;
            ret += this.solValid == that.solValid ? 0 : 1;
            ret += this.hpodValid == that.hpodValid ? 0 : 1;
            ret += this.timeMarkSeconds == that.timeMarkSeconds ? 0 : 1;
            ret += this.timeMarkValid == that.timeMarkValid ? 0 : 1;
            ret += this.timeMarkNew == that.timeMarkNew ? 0 : 1;
            ret += this.gpsWeek == that.gpsWeek ? 0 : 1;
            ret += this.svid == that.svid ? 0 : 1;
            ret += this.snr == that.snr ? 0 : 1;
            ret += this.hpod == that.hpod ? 0 : 1;
            ret += this.ehpe == that.ehpe ? 0 : 1;
            return ret;
        }
    }

    final static class T_PARAM_MFGDATA implements T_STRUCT<T_PARAM_MFGDATA> {
        static final int size = 171;

        byte paramVersion = PARAM_MFG_VERSION;
        int optionFlags = 0;

        T_CHANNEL_CALIBRATION[] calibration = {
                new T_CHANNEL_CALIBRATION(new short[]{0, 0, 0}, new float[]{4.77676E-006f, 6.47599E-004f, 5.19762E-003f}), // DC
                new T_CHANNEL_CALIBRATION(new short[]{0, 0, 0}, new float[]{2.73466E-006f, 3.38183E-004f, 3.02280E-03f}), // AC
        };

        short acFloorCal[] = {0, 0, 0};
        short calTemperature = 0;
        int calDate = 0;
        int spare[] = new int[29];
        short crc16 = 0;

        T_PARAM_MFGDATA() {
        }

        T_PARAM_MFGDATA(ByteBuffer buffer) {
            parse(buffer);
        }

        T_PARAM_MFGDATA(T_PARAM_MFGDATA that) {
            this.paramVersion = that.paramVersion;
            this.optionFlags = that.optionFlags;
            System.arraycopy(that.calibration, 0, this.calibration, 0, 2);
            System.arraycopy(that.acFloorCal, 0, this.acFloorCal, 0, 3);
            this.calTemperature = that.calTemperature;
            this.calDate = that.calDate;
            System.arraycopy(that.spare, 0, this.spare, 0, 29);
            this.crc16 = that.crc16;
        }

        @Override
        public void parse(ByteBuffer buffer) {
            int startPosition = buffer.position();

            this.paramVersion = buffer.get();
            this.optionFlags = buffer.getInt();

            for (T_CHANNEL_CALIBRATION aCal : this.calibration) {
                aCal.parse(buffer);
            }
            for (int i = 0; i < 3; i++) {
                this.acFloorCal[i] = buffer.getShort();
            }
            this.calTemperature = buffer.getShort();
            this.calDate = buffer.getInt();
            for (int i = 0; i < 29; i++) {
                this.spare[i] = buffer.getInt();
            }
            this.crc16 = buffer.getShort();

            if (buffer.position() - startPosition != size) {
                Log.e(TAG, "Parsing error, mismatched size. Position " + buffer.position() + " != Size " + size);
            }
        }

        @Override
        public ByteBuffer compose() {
            ByteBuffer buffer = ByteBuffer.allocate(size);
            buffer.order(ByteOrder.LITTLE_ENDIAN);

            buffer.put(paramVersion);
            buffer.putInt(optionFlags);
            for (T_CHANNEL_CALIBRATION aCal : this.calibration) {
                buffer.put(aCal.compose());
            }
            for (int i = 0; i < 3; i++) {
                buffer.putShort(acFloorCal[i]);
            }
            buffer.putShort(calTemperature);
            buffer.putInt(calDate);
            for (int i = 0; i < 29; i++) {
                buffer.putInt(this.spare[i]);
            }
            buffer.putShort(FCS16.fcs16_Add(buffer));

            if (buffer.position() != size) {
                Log.e(TAG, "compose() size mismatch!");
                return null;
            }
            buffer.position(0);
            return buffer;
        }

        @Override
        public int compareTo(@Nullable T_PARAM_MFGDATA that) {
            if (that == null) {
                return 1;
            }
            int ret = 0;
            ret += this.paramVersion != that.paramVersion ? 0 : 1;
            ret += this.optionFlags != that.optionFlags ? 0 : 1;
            ret += Arrays.equals(this.calibration, that.calibration) ? 0 : 1;
            ret += Arrays.equals(this.acFloorCal, that.acFloorCal) ? 0 : 1;
            ret += this.calTemperature != that.calTemperature ? 0 : 1;
            ret += this.calDate != that.calDate ? 0 : 1;
            ret += Arrays.equals(this.spare, that.spare) ? 0 : 1;
            ret += this.crc16 != that.crc16 ? 0 : 1;
            return ret;
        }
    }

    final static class T_PARAM_PARAMETERS implements T_STRUCT<T_PARAM_PARAMETERS> {
        static final int size = 304;

        byte paramVersion = PARAM_VERSION;
        short configFlags = CONFIGFLAGS_DCENABLED;

        int samplingCyclePeriod = 1000; /* Number of milliseconds between readings */
        int samplingCyclePeriodFast = 100; /* Number of milliseconds between fast sampling mode readings, must be 100ms or greater */
        int samplingFastDuration = 60; /* Number of seconds for a fast sampling mode*/
        int samplingFastInterval = 30 * 60; /* Number of seconds between fast sampling groups */


        short interruptionCycleTime = 10000; /* Future use for sync to interruption cycles (ms) */
        short interruptionOffTime = 1000; /* Future use for sync to interruption cycles (ms) */
        short interruptionOffToOnOffset = 100; /* Future use for sync to interruption cycles (ms) */
        short interruptionOnToOffOffset = 100; /* Future use for sync to interruption cycles (ms) */
        short interruptionUTCOffset = 0; /* Future use for sync to interruption cycles (ms) */
        byte interruptionOnFirst = 0; /* Future use for sync to interruption cycles (ms) */

        byte rangeSelect = 2; /*0=low, 1=med, 2=high*/
        float lowLimit[] = {-1000.0f, 1000.0f};
        float highLimit[] = {-1000.0f, 1000.0f};
        float scaleFactor[] = {1.0f, 1.0f};
        float offsetFactor[] = {0.0f, 0.0f};

        short batteryLowThreshold = 3150;
        short batteryWarnThreshold = 3300;

        byte notes[] = new byte[PARAM_NOTES_SIZE]; //Notes field

        short samplingCycleOffset = 0; /* Number of milliseconds to delay sampling on normal sampling mode (v1.16 addition)*/

        short configFlagsExtended = 0; /* Extended config flags */

        short timeZoneOffsetMins = 0; /* Time zone offset minutes from UTC */

        short minimumEHPE = 80; //Minimum EHPE for position recording (fixed point with 0.1m resolution), 2.5 to 200.0 range
        short minimumEHPEWaitTimeSec = 300; //Maximum number of seconds to wait for a good EHPE (3600s max, 300s nom)

        byte lockedFastSamplingOffsetMs = 0; //Number of milliseconds to offset from 100ms boundary before starting fast sampling (0-95ms)

        int delayStartSec = 0; //Delay before starting acquisition

        byte spare8[] = new byte[1]; //For future use so we don't have to bump the parameter version number for minor changes
        int spare[] = new int[28]; //For future use so we don't have to bump the parameter version number for minor changes
        short firmwareVerParamWrite = 116; //Used to flag a firmware to update any changed areas in the configuration
        short crc16 = 0;

        T_PARAM_PARAMETERS() {
        }

        T_PARAM_PARAMETERS(ByteBuffer buffer) {
            parse(buffer);
        }

        T_PARAM_PARAMETERS(T_PARAM_PARAMETERS that) {
            this.paramVersion = that.paramVersion;
            this.configFlags = that.configFlags;

            this.samplingCyclePeriod = that.samplingCyclePeriod; /* Number of milliseconds between readings */
            this.samplingCyclePeriodFast = that.samplingCyclePeriodFast; /* Number of milliseconds between fast sampling mode readings, must be 100ms or greater */
            this.samplingFastDuration = that.samplingFastDuration; /* Number of seconds for a fast sampling mode*/
            this.samplingFastInterval = that.samplingFastInterval; /* Number of seconds between fast sampling groups */
            this.interruptionCycleTime = that.interruptionCycleTime; /* Future use for sync to interruption cycles (ms) */
            this.interruptionOffTime = that.interruptionOffTime; /* Future use for sync to interruption cycles (ms) */
            this.interruptionOffToOnOffset = that.interruptionOffToOnOffset; /* Future use for sync to interruption cycles (ms) */
            this.interruptionOnToOffOffset = that.interruptionOnToOffOffset; /* Future use for sync to interruption cycles (ms) */
            this.interruptionUTCOffset = that.interruptionUTCOffset; /* Future use for sync to interruption cycles (ms) */
            this.interruptionOnFirst = that.interruptionOnFirst; /* Future use for sync to interruption cycles (ms) */
            this.rangeSelect = that.rangeSelect; /*0=low, 1=med, 2=high*/
            System.arraycopy(that.lowLimit, 0, this.lowLimit, 0, 2);
            System.arraycopy(that.highLimit, 0, this.highLimit, 0, 2);
            System.arraycopy(that.scaleFactor, 0, this.scaleFactor, 0, 2);
            System.arraycopy(that.offsetFactor, 0, this.offsetFactor, 0, 2);
            this.batteryLowThreshold = that.batteryLowThreshold;
            this.batteryWarnThreshold = that.batteryWarnThreshold;
            System.arraycopy(that.notes, 0, this.notes, 0, PARAM_NOTES_SIZE);
            this.samplingCycleOffset = that.samplingCycleOffset; /* Number of milliseconds to delay sampling on normal sampling mode (v1.16 addition)*/
            this.configFlagsExtended = that.configFlagsExtended; /* Extended config flags */
            this.timeZoneOffsetMins = that.timeZoneOffsetMins; /* Time zone offset minutes from UTC */
            this.minimumEHPE = that.minimumEHPE; //Minimum EHPE for position recording (fixed point with 0.1m resolution), 2.5 to 200.0 range
            this.minimumEHPEWaitTimeSec = that.minimumEHPEWaitTimeSec; //Maximum number of seconds to wait for a good EHPE (3600s max, 300s nom)
            this.lockedFastSamplingOffsetMs = that.lockedFastSamplingOffsetMs; //Number of milliseconds to offset from 100ms boundary before starting fast sampling (0-95ms)
            this.delayStartSec = that.delayStartSec; //Delay before starting acquisition
            System.arraycopy(that.spare8, 0, this.spare8, 0, 1);
            System.arraycopy(that.spare, 0, this.spare, 0, 28);
            this.firmwareVerParamWrite = that.firmwareVerParamWrite;
            this.crc16 = that.crc16;
        }

        @Override
        public void parse(ByteBuffer buffer) {
            int startPosition = buffer.position();

            this.paramVersion = buffer.get();
            this.configFlags = buffer.getShort();

            this.samplingCyclePeriod = buffer.getInt();
            this.samplingCyclePeriodFast = buffer.getInt();
            this.samplingFastDuration = buffer.getInt();
            this.samplingFastInterval = buffer.getInt();

            this.interruptionCycleTime = buffer.getShort();
            this.interruptionOffTime = buffer.getShort();
            this.interruptionOffToOnOffset = buffer.getShort();
            this.interruptionOnToOffOffset = buffer.getShort();
            this.interruptionUTCOffset = buffer.getShort();
            this.interruptionOnFirst = buffer.get();

            this.rangeSelect = buffer.get();
            for (int i = 0; i < 2; i++) {
                this.lowLimit[i] = buffer.getFloat();
            }
            for (int i = 0; i < 2; i++) {
                this.highLimit[i] = buffer.getFloat();
            }
            for (int i = 0; i < 2; i++) {
                this.scaleFactor[i] = buffer.getFloat();
            }
            for (int i = 0; i < 2; i++) {
                this.offsetFactor[i] = buffer.getFloat();
            }
            this.batteryLowThreshold = buffer.getShort();
            this.batteryWarnThreshold = buffer.getShort();

            for (int i = 0; i < PARAM_NOTES_SIZE; i++) {
                this.notes[i] = buffer.get();
            }
            this.samplingCycleOffset = buffer.getShort();
            this.configFlagsExtended = buffer.getShort();
            this.timeZoneOffsetMins = buffer.getShort();
            this.minimumEHPE = buffer.getShort();
            this.minimumEHPEWaitTimeSec = buffer.getShort();
            this.lockedFastSamplingOffsetMs = buffer.get();
            this.delayStartSec = buffer.getInt();
            for (int i = 0; i < 1; i++) {
                this.spare8[i] = buffer.get();
            }
            for (int i = 0; i < 28; i++) {
                this.spare[i] = buffer.getInt();
            }
            this.firmwareVerParamWrite = buffer.getShort();
            this.crc16 = buffer.getShort();

            if (buffer.position() - startPosition != size) {
                Log.e(TAG, "Parsing error, mismatched size. Position " + buffer.position() + " != Size " + size);
            }
        }

        @Override
        public ByteBuffer compose() {
            ByteBuffer buffer = ByteBuffer.allocate(size);
            buffer.order(ByteOrder.LITTLE_ENDIAN);

            buffer.put(paramVersion);
            buffer.putShort(configFlags);

            buffer.putInt(samplingCyclePeriod);
            buffer.putInt(samplingCyclePeriodFast);
            buffer.putInt(samplingFastDuration);
            buffer.putInt(samplingFastInterval);

            buffer.putShort(interruptionCycleTime);
            buffer.putShort(interruptionOffTime);
            buffer.putShort(interruptionOffToOnOffset);
            buffer.putShort(interruptionOnToOffOffset);
            buffer.putShort(interruptionUTCOffset);
            buffer.put(interruptionOnFirst);

            buffer.put(rangeSelect);
            for (int i = 0; i < 2; i++) {
                buffer.putFloat(lowLimit[i]);
            }
            for (int i = 0; i < 2; i++) {
                buffer.putFloat(highLimit[i]);
            }
            for (int i = 0; i < 2; i++) {
                buffer.putFloat(scaleFactor[i]);
            }
            for (int i = 0; i < 2; i++) {
                buffer.putFloat(offsetFactor[i]);
            }
            buffer.putShort(batteryLowThreshold);
            buffer.putShort(batteryWarnThreshold);
            for (int i = 0; i < PARAM_NOTES_SIZE; i++) {
                buffer.put(notes[i]);
            }

            buffer.putShort(samplingCycleOffset);
            buffer.putShort(configFlagsExtended);
            // phil timeZoneOffsetMins = (IsFlagSet(configFlagsExtended, CONFIGFLAGSEXT_LOCALTIME) ? (short) ((TimeZone.getDefault().getRawOffset() + TimeZone.getDefault().getDSTSavings()) / 1000 / 60) : 0);
            buffer.putShort(timeZoneOffsetMins);
            buffer.putShort(minimumEHPE);
            buffer.putShort(minimumEHPEWaitTimeSec);
            buffer.put(lockedFastSamplingOffsetMs);
            buffer.putInt(delayStartSec);
            for (int i = 0; i < 1; i++) {
                buffer.put(spare8[i]);
            }
            for (int i = 0; i < 28; i++) {
                buffer.putInt(spare[i]);
            }

            buffer.putShort(firmwareVerParamWrite);
            buffer.putShort(FCS16.fcs16_Add(buffer));

            if (buffer.position() != size) {
                Log.e(TAG, "compose() size mismatch!");
                return null;
            }
            buffer.position(0);
            return buffer;
        }

        @Override
        public int compareTo(@Nullable T_PARAM_PARAMETERS that) {
            if (that == null) {
                return 1;
            }
            int ret = 0;

            ret += this.paramVersion == that.paramVersion ? 0 : 1;
            ret += this.configFlags == that.configFlags ? 0 : 1;
            ret += this.samplingCyclePeriod == that.samplingCyclePeriod ? 0 : 1;
            ret += this.samplingCyclePeriodFast == that.samplingCyclePeriodFast ? 0 : 1;
            ret += this.samplingFastDuration == that.samplingFastDuration ? 0 : 1;
            ret += this.samplingFastInterval == that.samplingFastInterval ? 0 : 1;
            ret += this.interruptionCycleTime == that.interruptionCycleTime ? 0 : 1;
            ret += this.interruptionOffTime == that.interruptionOffTime ? 0 : 1;
            ret += this.interruptionOffToOnOffset == that.interruptionOffToOnOffset ? 0 : 1;
            ret += this.interruptionOnToOffOffset == that.interruptionOnToOffOffset ? 0 : 1;
            ret += this.interruptionUTCOffset == that.interruptionUTCOffset ? 0 : 1;
            ret += this.interruptionOnFirst == that.interruptionOnFirst ? 0 : 1;
            ret += this.rangeSelect == that.rangeSelect ? 0 : 1;
            ret += Arrays.equals(this.lowLimit, that.lowLimit) ? 0 : 1;
            ret += Arrays.equals(this.highLimit, that.highLimit) ? 0 : 1;
            ret += Arrays.equals(this.scaleFactor, that.scaleFactor) ? 0 : 1;
            ret += Arrays.equals(this.offsetFactor, that.offsetFactor) ? 0 : 1;
            ret += this.batteryLowThreshold == that.batteryLowThreshold ? 0 : 1;
            ret += this.batteryWarnThreshold == that.batteryWarnThreshold ? 0 : 1;
           // phil ret += (MBP_STRUCTS.toString(this.notes).compareTo(MBP_STRUCTS.toString(that.notes)) == 0) ? 0 : 1;
            ret += this.samplingCycleOffset == that.samplingCycleOffset ? 0 : 1;
            ret += this.configFlagsExtended == that.configFlagsExtended ? 0 : 1;
            //ret += this.timeZoneOffsetMins == that.timeZoneOffsetMins ? 0 : 1;
            ret += this.minimumEHPE == that.minimumEHPE ? 0 : 1;
            ret += this.minimumEHPEWaitTimeSec == that.minimumEHPEWaitTimeSec ? 0 : 1;
            ret += this.lockedFastSamplingOffsetMs == that.lockedFastSamplingOffsetMs ? 0 : 1;
            ret += this.delayStartSec == that.delayStartSec ? 0 : 1;
            ret += Arrays.equals(this.spare8, that.spare8) ? 0 : 1;
            ret += Arrays.equals(this.spare, that.spare) ? 0 : 1;
            ret += this.firmwareVerParamWrite == that.firmwareVerParamWrite ? 0 : 1;

            return ret;
        }
    }
}
