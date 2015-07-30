package com.uxxu.konashi.lib.entities;

import java.util.UUID;

/**
 * Created by izumin on 7/30/15.
 */
public class KonashiWriteMessage extends KonashiMessage {
    public static final String TAG = KonashiWriteMessage.class.getSimpleName();

    private byte[] mData;

    public KonashiWriteMessage(UUID characteristicUuid, byte[] data){
        super(characteristicUuid);
        mData = data;
    }

    public byte[] getData() {
        return mData;
    }
}
