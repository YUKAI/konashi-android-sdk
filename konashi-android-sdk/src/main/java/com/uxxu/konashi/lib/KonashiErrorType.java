package com.uxxu.konashi.lib;

import info.izumin.android.bletia.BletiaErrorType;

/**
 * Created by izumin on 9/16/15.
 */
public enum KonashiErrorType implements BletiaErrorType{
    INVALID_PARAMETER,
    NOT_ENABLED_UART,
    NOT_ENABLED_I2C;

    @Override
    public int getCode() {
        return -1;
    }

    @Override
    public String getName() {
        return name();
    }
}
