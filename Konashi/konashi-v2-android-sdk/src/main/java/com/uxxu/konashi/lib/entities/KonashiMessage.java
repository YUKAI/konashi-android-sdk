package com.uxxu.konashi.lib.entities;

import java.util.UUID;

/**
 * Created by izumin on 7/30/15.
 */
public class KonashiMessage {
    public static final String TAG = KonashiMessage.class.getSimpleName();
    private UUID mCharacteristicUuid;

    protected KonashiMessage(UUID characteristicUuid) {
        mCharacteristicUuid = characteristicUuid;
    }

    public UUID getCharacteristicUuid() {
        return mCharacteristicUuid;
    }
}
