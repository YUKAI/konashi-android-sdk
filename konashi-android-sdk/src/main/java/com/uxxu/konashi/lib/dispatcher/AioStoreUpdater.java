package com.uxxu.konashi.lib.dispatcher;

import com.uxxu.konashi.lib.Konashi;
import com.uxxu.konashi.lib.KonashiUUID;
import com.uxxu.konashi.lib.store.AioStore;
import com.uxxu.konashi.lib.util.AioUtils;

import java.util.UUID;

/**
 * Created by izumin on 9/19/15.
 */
public enum AioStoreUpdater implements CharacteristicDispatcher.Updater<AioStore> {
    READ_AIO0(KonashiUUID.ANALOG_READ0_UUID) {
        @Override
        public void update(AioStore store, byte[] value) {
            updateAioValue(store, Konashi.AIO0, value);
        }
    },
    READ_AIO1(KonashiUUID.ANALOG_READ1_UUID) {
        @Override
        public void update(AioStore store, byte[] value) {
            updateAioValue(store, Konashi.AIO1, value);
        }
    },
    READ_AIO2(KonashiUUID.ANALOG_READ2_UUID) {
        @Override
        public void update(AioStore store, byte[] value) {
            updateAioValue(store, Konashi.AIO2, value);
        }
    };

    private final UUID mUuid;

    AioStoreUpdater(UUID uuid) {
        mUuid = uuid;
    }

    public UUID getUuid() {
        return mUuid;
    }

    protected void updateAioValue(AioStore store, int pin, byte[] value) {
        store.setValue(pin, AioUtils.getAnalogValue(pin, value));
    }
}
