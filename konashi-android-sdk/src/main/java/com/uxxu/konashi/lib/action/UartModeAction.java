package com.uxxu.konashi.lib.action;

import android.bluetooth.BluetoothGattService;

import com.uxxu.konashi.lib.Konashi;
import com.uxxu.konashi.lib.KonashiUUID;

import java.util.UUID;

/**
 * Created by e10dokup on 2015/09/23
 **/
public class UartModeAction extends KonashiWriteCharacteristicAction{
    private static final String TAG = UartModeAction.class.getSimpleName();
    private final UartModeAction self = this;

    private static final UUID UUID = KonashiUUID.UART_CONFIG_UUID;

    private int mMode;

    public UartModeAction(BluetoothGattService service, int mode) {
        super(service, UUID);
        mMode = mode;
    }


    @Override
    protected void setValue() {
        getCharacteristic().setValue(new byte[]{(byte) mMode});
    }

    @Override
    protected boolean hasValidParams() {
        return mMode >= Konashi.UART_DISABLE && mMode <= Konashi.UART_ENABLE;
    }
}