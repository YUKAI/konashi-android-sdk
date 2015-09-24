package com.uxxu.konashi.lib.store;

import com.uxxu.konashi.lib.Konashi;
import com.uxxu.konashi.lib.dispatcher.CharacteristicDispatcher;
import com.uxxu.konashi.lib.dispatcher.UartStoreUpdater;

/**
 * Created by e10dokup on 9/20/15.
 */
public class UartStore implements Store {

    private byte mMode;
    private byte[] mBaudrate;
    private byte[] mData;

    public UartStore(CharacteristicDispatcher<UartStore, UartStoreUpdater> dispatcher) {
        dispatcher.setStore(this);
    }

    public byte getMode() {
        return mMode;
    }

    public void setMode(byte mode) {
        mMode = mode;
    }

    public boolean isEnabled() {
        return mMode == Konashi.UART_ENABLE;
    }

    public byte[] getBaudrate() {
        return mBaudrate;
    }

    public void setBaudrate(byte[] baudrate) {
        mBaudrate = baudrate;
    }

    public byte[] getData() {
        return mData;
    }

    public void setData(byte[] data) {
        mData = data;
    }
}