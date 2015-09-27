package com.uxxu.konashi.lib.action;

import android.bluetooth.BluetoothGattService;

import com.uxxu.konashi.lib.KonashiUUID;
import com.uxxu.konashi.lib.KonashiUtils;
import com.uxxu.konashi.lib.store.UartStore;
import com.uxxu.konashi.lib.util.UartUtils;

import java.util.UUID;

/**
 * Created by e10dokup on 2015/09/22
 **/
public class UartBaudrateAction extends UartAction {
    private static final UUID UUID= KonashiUUID.UART_BAUDRATE_UUID;

    private int mBaudrate;

    public UartBaudrateAction(BluetoothGattService service, int baudrate, UartStore store) {
        super(service, UUID, store, false);
        mBaudrate = baudrate;
    }

    @Override
    protected void setValue() {
        getCharacteristic().setValue(KonashiUtils.int2bytes(mBaudrate));
    }

    @Override
    protected boolean hasValidParams() {
        return UartUtils.isValidBaudrate(mBaudrate);
    }
}