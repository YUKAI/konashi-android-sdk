package com.uxxu.konashi.lib.action;

import android.bluetooth.BluetoothGattService;

import com.uxxu.konashi.lib.KonashiUUID;
import com.uxxu.konashi.lib.store.I2cStore;
import com.uxxu.konashi.lib.util.I2cUtils;

import java.util.UUID;

/**
 * Created by izumin on 9/21/15.
 */
public class I2cSetReadParamAction extends I2cAction {

    private static final UUID UUID = KonashiUUID.I2C_READ_PARAM_UUID;

    private final int mLength;
    private final byte mAddress;

    public I2cSetReadParamAction(BluetoothGattService service, int length, byte address, I2cStore store) {
        super(service, UUID, store);
        mLength = length;
        mAddress = address;
    }

    @Override
    protected void setValue() {
        getCharacteristic().setValue(new byte[]{(byte)mLength, (byte)((mAddress << 1) | 0x01)});
    }

    @Override
    protected boolean hasValidParams() {
        return I2cUtils.isValidDataLength(mLength);
    }
}
