package com.uxxu.konashi.lib.stores;

import com.uxxu.konashi.lib.Konashi;

/**
 * Created by izumin on 9/20/15.
 */
public class I2cStore implements Store {
    private byte mMode = 0;
    private byte[] mReadData = new byte[Konashi.I2C_DATA_MAX_LENGTH];
    private int mReadDataLength = 0;
    private byte mReadAddress = 0;

    public I2cStore() {
        for (int i = 0; i < mReadData.length; i++) { mReadData[i] = 0; }
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
}
