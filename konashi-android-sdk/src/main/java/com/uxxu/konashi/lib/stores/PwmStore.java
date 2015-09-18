package com.uxxu.konashi.lib.stores;

/**
 * Created by izumin on 9/18/15.
 */
public class PwmStore {
    private static final int PWM_LENGTH = 8;

    private int mPwmModes = 0;
    private int[] mPwmPeriods = new int[PWM_LENGTH];
    private int[] mPwmDuties = new int[PWM_LENGTH];

    public int getPwmModes() {
        return mPwmModes;
    }

    public void setPwmModes(int modes) {
        mPwmModes = modes;
    }

    public int getPwmPeriod(int pin) {
        return mPwmPeriods[pin];
    }

    public void setPwmPeriod(int pin, int period) {
        mPwmPeriods[pin] = period;
    }

    public int getPwmDuty(int pin) {
        return mPwmDuties[pin];
    }

    public void setPwmDuty(int pin, int duty) {
        mPwmDuties[pin] = duty;
    }
}
