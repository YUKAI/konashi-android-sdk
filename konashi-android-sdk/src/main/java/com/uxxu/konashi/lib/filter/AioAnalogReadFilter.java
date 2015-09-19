package com.uxxu.konashi.lib.filter;

import android.bluetooth.BluetoothGattCharacteristic;

import com.uxxu.konashi.lib.util.AioUtils;

import org.jdeferred.DoneFilter;

/**
 * Created by izumin on 9/19/15.
 */
public class AioAnalogReadFilter implements DoneFilter<BluetoothGattCharacteristic, Integer> {

    private final int mPin;

    public AioAnalogReadFilter(int pin) {
        mPin = pin;
    }

    @Override
    public Integer filterDone(BluetoothGattCharacteristic result) {
        return AioUtils.getAnalogValue(mPin, result.getValue());
    }
}
