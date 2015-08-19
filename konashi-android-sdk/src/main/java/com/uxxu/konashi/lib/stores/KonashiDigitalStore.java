package com.uxxu.konashi.lib.stores;

import com.uxxu.konashi.lib.KonashiErrorReason;
import com.uxxu.konashi.lib.listeners.KonashiDigitalListener;

/**
 * Created by izumin on 8/18/15.
 */
public class KonashiDigitalStore implements KonashiDigitalListener {
    private byte mPioModes = 0;
    private byte mPioPullups = 0;
    private byte mPioInputs = 0;
    private byte mPioOutputs = 0;

    public byte getPioModes() {
        return mPioModes;
    }

    public byte getPioPullups() {
        return mPioPullups;
    }

    public byte getPioInput(int pin) {
        return (byte) ((mPioInputs >> pin) & 0x01);
    }

    public byte getPioInputs() {
        return mPioInputs;
    }

    public byte getPioOutputs() {
        return mPioOutputs;
    }

    @Override
    public void onUpdatePioSetting(int modes) {
        mPioModes = (byte) modes;
    }

    @Override
    public void onUpdatePioPullup(int pullups) {
        mPioPullups = (byte) pullups;
    }

    @Override
    public void onUpdatePioInput(byte value) {
        mPioInputs = value;
    }

    @Override
    public void onUpdatePioOutput(byte value) {
        mPioOutputs = value;
    }

    @Override
    public void onError(KonashiErrorReason errorReason, String message) {
        // do nothing...
    }
}
