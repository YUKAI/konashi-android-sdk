package com.uxxu.konashi.lib.action;

import android.bluetooth.BluetoothGattService;

import com.uxxu.konashi.lib.KonashiUUID;
import com.uxxu.konashi.lib.util.I2cUtils;

import java.util.UUID;

/**
 * Created by izumin on 9/20/15.
 */
public class I2cModeAction extends KonashiWriteCharacteristicAction {

    private static final UUID UUID = KonashiUUID.I2C_CONFIG_UUID;

    private final int mMode;

    public I2cModeAction(BluetoothGattService service, int mode) {
        super(service, UUID);
        mMode = mode;
    }

    @Override
    protected void setValue() {
        getCharacteristic().setValue(new byte[]{(byte) mMode});
    }

    @Override
    protected boolean hasValidParams() {
        return I2cUtils.isValidMode(mMode);
    }
}
