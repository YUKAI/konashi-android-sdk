package com.uxxu.konashi.lib.store;

import android.util.SparseArray;

import com.uxxu.konashi.lib.Konashi;
import com.uxxu.konashi.lib.dispatcher.AioStoreUpdater;
import com.uxxu.konashi.lib.dispatcher.CharacteristicDispatcher;

/**
 * Created by izumin on 8/18/15.
 */
public class AioStore implements Store {
    private static final int[] PINS = {Konashi.AIO0, Konashi.AIO1, Konashi.AIO2};
    private SparseArray<Integer> mValues;

    public AioStore(CharacteristicDispatcher<AioStore, AioStoreUpdater> dispatcher) {
        dispatcher.setStore(this);
        mValues = new SparseArray<>();
        for (int pin : PINS) {
            mValues.put(pin, 0);
        }
    }

    public int getValue(int pin) {
        return mValues.get(pin);
    }

    public void setValue(int pin, int value) {
        mValues.put(pin, value);
    }
}
