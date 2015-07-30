package com.uxxu.konashi.lib.entities;

import java.util.UUID;

/**
 * Created by izumin on 7/30/15.
 */
public class KonashiReadMessage extends KonashiMessage {
    public static final String TAG = KonashiReadMessage.class.getSimpleName();

    private UUID mServiceUuid;

    public KonashiReadMessage(UUID serviceUuid, UUID characteristicUuid){
        super(characteristicUuid);
        mServiceUuid = serviceUuid;
    }

    public UUID getServiceUuid() {
        return mServiceUuid;
    }
}
