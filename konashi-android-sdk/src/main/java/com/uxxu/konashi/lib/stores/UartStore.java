package com.uxxu.konashi.lib.stores;

import com.uxxu.konashi.lib.dispatcher.CharacteristicDispatcher;
import com.uxxu.konashi.lib.dispatcher.UartStoreUpdater;

/**
 * Created by e10dokup on 9/20/15.
 */
public class UartStore implements Store {

    private byte mUartSetting;
    private byte[] mUartBaudrate;

    public UartStore(CharacteristicDispatcher<UartStore, UartStoreUpdater> dispatcher) {
        dispatcher.setStore(this);
    }

    public byte getUartSetting() {
        return mUartSetting;
    }

    public void setUartSetting(byte uartSetting) {
        mUartSetting = uartSetting;
    }

    public byte[] getUartBaudrate() {
        return mUartBaudrate;
    }

    public void setUartBaudrate(byte[] uartBaudrate) {
        mUartBaudrate = uartBaudrate;
    }
}