package com.uxxu.konashi.lib.action;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

import java.util.UUID;

import info.izumin.android.bletia.BleErrorType;
import info.izumin.android.bletia.BletiaException;
import info.izumin.android.bletia.action.WriteCharacteristicAction;
import info.izumin.android.bletia.wrapper.BluetoothGattWrapper;

/**
 * Created by izumin on 9/17/15.
 */
public abstract class KonashiWriteCharacteristicAction extends WriteCharacteristicAction {

    protected KonashiWriteCharacteristicAction(BluetoothGattCharacteristic characteristic) {
        super(characteristic);
    }

    protected KonashiWriteCharacteristicAction(BluetoothGattService service, UUID uuid) {
        this(service.getCharacteristic(uuid));
    }

    @Override
    public void execute(BluetoothGattWrapper gattWrapper) {
        if (hasValidParams()) {
            setValue();
            super.execute(gattWrapper);
        } else {
            rejectIfParamsAreInvalid();
        }
    }

    protected void rejectIfParamsAreInvalid() {
        getDeferred().reject(new BletiaException(BleErrorType.INVALID_PARAMETER, getCharacteristic()));
    }

    protected abstract void setValue();
    protected abstract boolean hasValidParams();
}
