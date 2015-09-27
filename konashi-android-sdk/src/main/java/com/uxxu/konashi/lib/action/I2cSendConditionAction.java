package com.uxxu.konashi.lib.action;

import android.bluetooth.BluetoothGattService;

import com.uxxu.konashi.lib.KonashiUUID;
import com.uxxu.konashi.lib.store.I2cStore;
import com.uxxu.konashi.lib.util.I2cUtils;

import java.util.UUID;

/**
 * Created by izumin on 9/20/15.
 */
public class I2cSendConditionAction extends I2cAction {

    private static final UUID UUID = KonashiUUID.I2C_START_STOP_UUID;

    private final int mCondition;

    public I2cSendConditionAction(BluetoothGattService service, int condition, I2cStore store) {
        super(service, UUID, store, false);
        mCondition = condition;
    }

    @Override
    protected void setValue() {
        getCharacteristic().setValue(new byte[]{(byte) mCondition});
    }

    @Override
    protected boolean hasValidParams() {
        return I2cUtils.isValidCondition(mCondition);
    }
}
