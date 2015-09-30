package com.uxxu.konashi.lib.action;

import android.bluetooth.BluetoothGattService;

import com.uxxu.konashi.lib.KonashiUUID;

import java.util.UUID;

import info.izumin.android.bletia.action.ReadCharacteristicAction;

/**
 * Created by izumin on 9/21/15.
 */
public class I2cReadAction extends ReadCharacteristicAction {

    private static final UUID UUID = KonashiUUID.I2C_READ_UUID;

    public I2cReadAction(BluetoothGattService service) {
        super(service.getCharacteristic(UUID));
    }
}
