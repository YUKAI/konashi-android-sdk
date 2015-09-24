package com.uxxu.konashi.lib.store;

import com.uxxu.konashi.lib.dispatcher.CharacteristicDispatcher;
import com.uxxu.konashi.lib.dispatcher.PioStoreUpdater;

/**
 * Created by izumin on 8/18/15.
 */
public class PioStore implements Store {
    private int mModes = 0;
    private int mPullups = 0;
    private int mInputs = 0;
    private int mOutputs = 0;

    public PioStore(CharacteristicDispatcher<PioStore, PioStoreUpdater> dispatcher) {
        dispatcher.setStore(this);
    }

    public byte getInput(int pin) {
        return (byte) ((mInputs >> pin) & 0x01);
    }

    public int getModes() {
        return mModes;
    }

    public void setModes(int modes) {
        mModes = modes;
    }

    public int getPullups() {
        return mPullups;
    }

    public void setPullups(int pullups) {
        mPullups = pullups;
    }

    public int getInputs() {
        return mInputs;
    }

    public void setInputs(int inputs) {
        mInputs = inputs;
    }

    public int getOutputs() {
        return mOutputs;
    }

    public void setOutputs(int outputs) {
        mOutputs = outputs;
    }
}
