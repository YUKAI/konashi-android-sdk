package com.uxxu.konashi.lib.dispatcher;

import com.uxxu.konashi.lib.KonashiUUID;
import com.uxxu.konashi.lib.stores.PioStore;

import java.util.UUID;

/**
 * Created by izumin on 9/17/15.
 */
public enum PioStoreUpdater implements CharacteristicDispatcher.Updater<PioStore> {
    MODE(KonashiUUID.PIO_SETTING_UUID) {
        @Override
        public void update(PioStore store, byte[] value) {
            store.setPioModes(value[0]);
        }
    },
    PULLUP(KonashiUUID.PIO_PULLUP_UUID) {
        @Override
        public void update(PioStore store, byte[] value) {
            store.setPioPullups(value[0]);
        }
    },
    OUTPUT(KonashiUUID.PIO_OUTPUT_UUID) {
        @Override
        public void update(PioStore store, byte[] value) {
            store.setPioOutputs(value[0]);
        }
    };

    private final UUID mUuid;

    PioStoreUpdater(UUID uuid) {
        mUuid = uuid;
    }

    public UUID getUuid() {
        return mUuid;
    }
}
