package com.ublox.BLE;

import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static com.ublox.BLE.MBP_STRUCTS.*;

public class T_MBP_PACKET {
    private final String TAG = T_MBP_PACKET.class.getSimpleName();
    private int msgLength = 0;
    private ByteBuffer rawBuffer;

    T_MBP_PACKET(int size) {
        rawBuffer = ByteBuffer.allocate(size + 3);
        setAddress(BMM_APP_ADDRESS);
        setProtocolType(BMM_APP_PROTOCOL);
        rawBuffer.order(ByteOrder.LITTLE_ENDIAN);
    }

    byte getAddress() {
        return rawBuffer.get(0);
    }

    void setAddress(byte address) {
        rawBuffer.put(0, address);
    }

    byte getProtocolType() {
        return rawBuffer.get(1);
    }

    void setProtocolType(byte protocolType) {
        rawBuffer.put(1, protocolType);
    }

    byte getMessageType() {
        return rawBuffer.get(2);
    }

    void setMessageType(byte messageType) {
        rawBuffer.put(2, messageType);
    }

    int setPayload(ByteBuffer payload) {
        msgLength = 0;
        if (payload.position() + 3 >= rawBuffer.capacity()) {
            Log.e(TAG, "setPayload() rawBuffer is not large enough for the payload");
            return 0;
        }
        rawBuffer.position(3);
        payload.position(0);
        rawBuffer.put(payload);
        msgLength = payload.capacity();
        return msgLength;
    }

    ByteBuffer getPayload() {
        ByteBuffer ret = rawBuffer.duplicate();
        ret.position(3);
        ret = ret.slice();
        ret.position(rawBuffer.position() - 3);
        return ret.asReadOnlyBuffer();
    }

    int getRawLength() {
        return rawBuffer.position();
    }

    void reset() {
        msgLength = 0;
        rawBuffer.position(3);
        setAddress(BMM_APP_ADDRESS);
        setProtocolType(BMM_APP_PROTOCOL);
    }

    void resetRaw() {
        msgLength = 0;
        rawBuffer.position(0);
        setAddress((byte) 0);
        setProtocolType((byte) 0);
    }

    int appendPayload(byte payloadSrc) {
        if ((rawBuffer.position() + 1) >= rawBuffer.capacity()) {
            Log.e(TAG, "setPayload() rawBuffer is not large enough for the payload");
            return 0;
        }
        rawBuffer.put(payloadSrc);
        msgLength += 1;

        return msgLength;
    }

    ByteBuffer getRawBuffer() {
        ByteBuffer dup = rawBuffer.duplicate();
        return dup.asReadOnlyBuffer();
    }

    void removeCrc() {
        if (rawBuffer.position() > 5) {
            rawBuffer.position(rawBuffer.position() - 2);
            if (msgLength > 2) {
                msgLength -= 2;
            }
        }
    }
}
