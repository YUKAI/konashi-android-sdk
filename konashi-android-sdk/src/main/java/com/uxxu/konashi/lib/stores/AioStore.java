package com.uxxu.konashi.lib.stores;

import android.util.SparseArray;

import com.uxxu.konashi.lib.Konashi;
import com.uxxu.konashi.lib.dispatcher.AioStoreUpdater;
import com.uxxu.konashi.lib.dispatcher.CharacteristicDispatcher;

/**
 * Created by izumin on 8/18/15.
 */
public class AioStore implements Store {
    private static final int[] PINS = {Konashi.AIO0, Konashi.AIO1, Konashi.AIO2};
    private SparseArray<Integer> mAioValues;

    public AioStore(CharacteristicDispatcher<AioStore, AioStoreUpdater> dispatcher) {
        dispatcher.setStore(this);
        mAioValues = new SparseArray<>();
        for (int pin : PINS) {
            mAioValues.put(pin, 0);
        }
    }

    public int getAnalogValue(int pin) {
        return mAioValues.get(pin);
    }

    public void setAnalogValue(int pin, int value) {
        mAioValues.put(pin, value);
    }
}
