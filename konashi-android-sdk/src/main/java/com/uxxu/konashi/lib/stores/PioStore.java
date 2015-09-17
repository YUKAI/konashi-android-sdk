package com.uxxu.konashi.lib.stores;

import com.uxxu.konashi.lib.dispatcher.PioDispatcher;

/**
 * Created by izumin on 8/18/15.
 */
public class PioStore {
    private int mPioModes = 0;
    private int mPioPullups = 0;
    private int mPioInputs = 0;
    private int mPioOutputs = 0;

    public PioStore(PioDispatcher dispatcher) {
        dispatcher.setStore(this);
    }

    public byte getPioInput(int pin) {
        return (byte) ((mPioInputs >> pin) & 0x01);
    }

    public int getPioModes() {
        return mPioModes;
    }

    public void setPioModes(int pioModes) {
        mPioModes = pioModes;
    }

    public int getPioPullups() {
        return mPioPullups;
    }

    public void setPioPullups(int pioPullups) {
        mPioPullups = pioPullups;
    }

    public int getPioInputs() {
        return mPioInputs;
    }

    public void setPioInputs(int pioInputs) {
        mPioInputs = pioInputs;
    }

    public int getPioOutputs() {
        return mPioOutputs;
    }

    public void setPioOutputs(int pioOutputs) {
        mPioOutputs = pioOutputs;
    }
}
