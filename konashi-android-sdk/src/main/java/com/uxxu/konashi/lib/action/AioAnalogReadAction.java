package com.uxxu.konashi.lib.action;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.util.SparseArray;

import com.uxxu.konashi.lib.Konashi;
import com.uxxu.konashi.lib.KonashiErrorType;
import com.uxxu.konashi.lib.KonashiUUID;

import java.util.UUID;

import info.izumin.android.bletia.BleErrorType;
import info.izumin.android.bletia.BletiaException;
import info.izumin.android.bletia.action.ReadCharacteristicAction;
import info.izumin.android.bletia.wrapper.BluetoothGattWrapper;

/**
 * Created by izumin on 9/19/15.
 */
public class AioAnalogReadAction extends ReadCharacteristicAction {

    private static final SparseArray<UUID> sPin2Uuid = new SparseArray<UUID>() {{
        append(Konashi.AIO0, KonashiUUID.ANALOG_READ0_UUID);
        append(Konashi.AIO1, KonashiUUID.ANALOG_READ1_UUID);
        append(Konashi.AIO2, KonashiUUID.ANALOG_READ2_UUID);
    }};

    protected AioAnalogReadAction(BluetoothGattCharacteristic characteristic) {
        super(characteristic);
    }

    public AioAnalogReadAction(BluetoothGattService service, int pin) {
        this(service.getCharacteristic(sPin2Uuid.get(pin, null)));
    }

    @Override
    public void execute(BluetoothGattWrapper gattWrapper) {
        if (getCharacteristic() == null) {
            rejectIfParamsAreInvalid();
        } else {
            super.execute(gattWrapper);
        }
    }

    private void rejectIfParamsAreInvalid() {
        getDeferred().reject(new BletiaException(KonashiErrorType.NULL_CHARACTERISTIC, getCharacteristic()));
    }
}
