package com.uxxu.konashi.lib.action;

import android.bluetooth.BluetoothGattService;

import com.uxxu.konashi.lib.KonashiErrorType;
import com.uxxu.konashi.lib.KonashiUUID;

import java.util.UUID;

/**
 * Created by izumin on 9/25/15.
 */
public class HardwareResetAction extends KonashiWriteCharacteristicAction {

    private static final UUID UUID = KonashiUUID.HARDWARE_RESET_UUID;

    public HardwareResetAction(BluetoothGattService service) {
        super(service.getCharacteristic(UUID));
    }

    @Override
    protected void setValue() {
        getCharacteristic().setValue(new byte[]{0x01});
    }

    @Override
    protected KonashiErrorType validate() {
        return KonashiErrorType.NO_ERROR;
    }
}
