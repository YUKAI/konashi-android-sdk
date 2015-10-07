package com.uxxu.konashi.lib.filter;

import android.bluetooth.BluetoothGattCharacteristic;

import com.uxxu.konashi.lib.util.KonashiUtils;

import org.jdeferred.DoneFilter;

/**
 * Created by izumin on 9/23/15.
 */
public class BatteryLevelReadFilter implements DoneFilter<BluetoothGattCharacteristic, Integer>{

    @Override
    public Integer filterDone(BluetoothGattCharacteristic result) {
        return KonashiUtils.getBatteryLevel(result);
    }
}
