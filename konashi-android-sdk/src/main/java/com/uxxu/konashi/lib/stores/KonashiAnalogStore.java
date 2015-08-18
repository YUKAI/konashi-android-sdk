package com.uxxu.konashi.lib.stores;

import android.util.SparseArray;

import com.uxxu.konashi.lib.Konashi;
import com.uxxu.konashi.lib.KonashiErrorReason;
import com.uxxu.konashi.lib.listeners.KonashiAnalogListener;

/**
 * Created by izumin on 8/18/15.
 */
public class KonashiAnalogStore implements KonashiAnalogListener{
    private static final int[] PINS = {Konashi.AIO0, Konashi.AIO1, Konashi.AIO2};
    private SparseArray<Integer> mAioValues;

    public KonashiAnalogStore() {
        mAioValues = new SparseArray<>();
        for (int pin : PINS) {
            mAioValues.put(pin, 0);
        }
    }

    public int getAnalogValue(int pin) {
        return mAioValues.get(pin);
    }

    @Override
    public void onUpdateAnalogValue(int pin, int value) {
        mAioValues.put(pin, value);
    }

    @Override
    public void onUpdateAnalogValueAio0(int value) {
        mAioValues.put(Konashi.AIO0, value);
    }

    @Override
    public void onUpdateAnalogValueAio1(int value) {
        mAioValues.put(Konashi.AIO1, value);
    }

    @Override
    public void onUpdateAnalogValueAio2(int value) {
        mAioValues.put(Konashi.AIO2, value);
    }

    @Override
    public void onError(KonashiErrorReason errorReason, String message) {
        // do nothing...
    }
}
