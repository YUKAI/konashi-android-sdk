package com.uxxu.konashi.lib.action;

import android.bluetooth.BluetoothGattService;

import com.uxxu.konashi.lib.KonashiUUID;
import com.uxxu.konashi.lib.KonashiUtils;
import com.uxxu.konashi.lib.store.UartStore;
import com.uxxu.konashi.lib.util.UartUtils;

import java.util.UUID;

/**
 * Created by e10dokup on 2015/09/23
 **/
public class UartModeAction extends UartAction {
    private static final String TAG = UartModeAction.class.getSimpleName();
    private final UartModeAction self = this;

    private static final UUID UUID = KonashiUUID.UART_CONFIG_UUID;

    private int mMode;

    public UartModeAction(BluetoothGattService service, int mode, UartStore store) {
        super(service, UUID, store, true);
        mMode = mode;
    }


    @Override
    protected void setValue() {
        getCharacteristic().setValue(KonashiUtils.int2bytes(mMode));
    }

    @Override
    protected boolean hasValidParams() {
        return UartUtils.isValidMode(mMode);
    }
}