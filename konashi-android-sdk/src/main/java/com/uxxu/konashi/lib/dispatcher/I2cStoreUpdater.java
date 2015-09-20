package com.uxxu.konashi.lib.dispatcher;

import com.uxxu.konashi.lib.KonashiUUID;
import com.uxxu.konashi.lib.stores.I2cStore;

import java.util.UUID;

/**
 * Created by izumin on 9/20/15.
 */
public enum I2cStoreUpdater implements CharacteristicDispatcher.Updater<I2cStore> {
    MODE(KonashiUUID.I2C_CONFIG_UUID) {
        @Override
        public void update(I2cStore store, byte[] value) {
            store.setMode(value[0]);
        }
    },
    CONDITION(KonashiUUID.I2C_START_STOP_UUID) {
        @Override
        public void update(I2cStore store, byte[] value) {
            // TODO: Not yet implemented.
        }
    },
    WRITE_DATA(KonashiUUID.I2C_WRITE_UUID) {
        @Override
        public void update(I2cStore store, byte[] value) {
            // TODO: Not yet implemented.
        }
    },
    READ_PARAM(KonashiUUID.I2C_READ_PARAM_UUID) {
        @Override
        public void update(I2cStore store, byte[] value) {
            // TODO: Not yet implemented.
        }
    },
    READ_DATA(KonashiUUID.I2C_READ_UUID) {
        @Override
        public void update(I2cStore store, byte[] value) {
            // TODO: Not yet implemented.
        }
    };

    private final UUID mUuid;

    I2cStoreUpdater(UUID uuid) {
        mUuid = uuid;
    }

    public UUID getUuid() {
        return mUuid;
    }
}
