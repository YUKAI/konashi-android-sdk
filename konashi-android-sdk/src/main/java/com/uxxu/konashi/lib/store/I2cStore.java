package com.uxxu.konashi.lib.store;

import com.uxxu.konashi.lib.Konashi;
import com.uxxu.konashi.lib.dispatcher.CharacteristicDispatcher;
import com.uxxu.konashi.lib.dispatcher.I2cStoreUpdater;

/**
 * Created by izumin on 9/20/15.
 */
public class I2cStore implements Store {
    private byte mMode = 0;
    private byte[] mReadData = new byte[Konashi.I2C_DATA_MAX_LENGTH];
    private int mReadDataLength = 0;
    private byte mReadAddress = 0;

    public I2cStore(CharacteristicDispatcher<I2cStore, I2cStoreUpdater> dispatcher) {
        for (int i = 0; i < mReadData.length; i++) { mReadData[i] = 0; }
        dispatcher.setStore(this);
    }

    public byte getMode() {
        return mMode;
    }

    public void setMode(byte mode) {
        mMode = mode;
    }

    public byte[] getReadData() {
        return mReadData;
    }

    public void setReadData(byte[] readData) {
        mReadData = readData;
    }

    public int getReadDataLength() {
        return mReadDataLength;
    }

    public void setReadDataLength(int readDataLength) {
        mReadDataLength = readDataLength;
    }

    public byte getReadAddress() {
        return mReadAddress;
    }

    public void setReadAddress(byte readAddress) {
        mReadAddress = readAddress;
    }

    public boolean isEnabled() {
        return (mMode == Konashi.I2C_ENABLE)
                || (mMode == Konashi.I2C_ENABLE_100K) || (mMode == Konashi.I2C_ENABLE_400K);
    }
}
