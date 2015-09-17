package com.uxxu.konashi.lib.dispatcher;

import android.bluetooth.BluetoothGattCharacteristic;

import com.uxxu.konashi.lib.KonashiUUID;
import com.uxxu.konashi.lib.stores.PioStore;

import org.jdeferred.DoneCallback;

import java.util.UUID;

/**
 * Created by izumin on 9/17/15.
 */
public class PioDispatcher implements DoneCallback<BluetoothGattCharacteristic> {
    enum ActionType {
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

        ActionType(UUID uuid) {
            mUuid = uuid;
        }

        public UUID getUuid() {
            return mUuid;
        }

        public abstract void update(PioStore store, byte[] value);

        static ActionType valueOf(UUID uuid) {
            for (ActionType action : values()) {
                if (uuid.equals(action.getUuid())) { return action; }
            }
            throw new IllegalArgumentException();
        }
    }

    private PioStore mStore;

    public void setStore(PioStore store) {
        mStore = store;
    }

    @Override
    public void onDone(BluetoothGattCharacteristic result) {
        ActionType.valueOf(result.getUuid()).update(mStore, result.getValue());
    }
}
