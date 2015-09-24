package com.uxxu.konashi.lib.filter;

import android.bluetooth.BluetoothGattCharacteristic;

import org.jdeferred.DoneFilter;

/**
 * Created by izumin on 9/21/15.
 */
public class I2cReadFilter implements DoneFilter<BluetoothGattCharacteristic, byte[]> {

    @Override
    public byte[] filterDone(BluetoothGattCharacteristic result) {
        return result.getValue();
    }
}
