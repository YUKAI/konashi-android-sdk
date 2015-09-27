package com.uxxu.konashi.lib.store;

import com.uxxu.konashi.lib.dispatcher.CharacteristicDispatcher;
import com.uxxu.konashi.lib.dispatcher.PwmStoreUpdater;

/**
 * Created by izumin on 9/18/15.
 */
public class PwmStore implements Store {
    private static final int PWM_LENGTH = 8;

    private int mModes = 0;
    private int[] mPeriods = new int[PWM_LENGTH];
    private int[] mDuties = new int[PWM_LENGTH];

    public PwmStore(CharacteristicDispatcher<PwmStore, PwmStoreUpdater> dispatcher) {
        dispatcher.setStore(this);
    }

    public int getModes() {
        return mModes;
    }

    public void setModes(int modes) {
        mModes = modes;
    }

    public int getPeriod(int pin) {
        return mPeriods[pin];
    }

    public void setPeriod(int pin, int period) {
        mPeriods[pin] = period;
    }

    public int getDuty(int pin) {
        return mDuties[pin];
    }

    public void setDuty(int pin, int duty) {
        mDuties[pin] = duty;
    }
}
