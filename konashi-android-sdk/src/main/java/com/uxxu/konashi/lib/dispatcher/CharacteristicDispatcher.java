package com.uxxu.konashi.lib.dispatcher;

import android.bluetooth.BluetoothGattCharacteristic;

import com.uxxu.konashi.lib.store.Store;

import org.jdeferred.DoneCallback;

import java.util.UUID;

/**
 * Created by izumin on 9/18/15.
 */
public class CharacteristicDispatcher<S extends Store, T extends Enum<T> & CharacteristicDispatcher.Updater<S>> implements DoneCallback<BluetoothGattCharacteristic> {
    interface Updater<S> {
        void update(S store, byte[] value);
        UUID getUuid();
    }

    private S mStore;
    private final Class<T> mTypeClass;

    public CharacteristicDispatcher(Class<T> typeClass) {
        mTypeClass = typeClass;
    }

    public void setStore(S store) {
        mStore = store;
    }

    private Updater<S> valueOf(UUID uuid) {
        for (Updater<S> type : mTypeClass.getEnumConstants()) {
            if (type.getUuid().equals(uuid)) { return type; }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public void onDone(BluetoothGattCharacteristic result) {
        valueOf(result.getUuid()).update(mStore, result.getValue());
    }
}
