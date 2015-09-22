package com.uxxu.konashi.lib.action;

import android.bluetooth.BluetoothGattService;

import com.uxxu.konashi.lib.KonashiErrorType;
import com.uxxu.konashi.lib.stores.I2cStore;

import java.util.UUID;

import info.izumin.android.bletia.BletiaException;
import info.izumin.android.bletia.wrapper.BluetoothGattWrapper;

/**
 * Created by izumin on 9/22/15.
 */
public abstract class I2cAction extends KonashiWriteCharacteristicAction {

    private final I2cStore mStore;

    public I2cAction(BluetoothGattService service, UUID uuid, I2cStore store) {
        super(service, uuid);
        mStore = store;
    }

    @Override
    public void execute(BluetoothGattWrapper gattWrapper) {
        if (mStore.isEnabled()) {
            super.execute(gattWrapper);
        } else {
            getDeferred().reject(new BletiaException(KonashiErrorType.NOT_ENABLED_I2C));
        }
    }
}
