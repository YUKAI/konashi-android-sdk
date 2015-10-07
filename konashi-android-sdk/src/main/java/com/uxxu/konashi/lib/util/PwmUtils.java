package com.uxxu.konashi.lib.util;

import com.uxxu.konashi.lib.Konashi;

import java.util.Arrays;

/**
 * Created by izumin on 9/18/15.
 */
public final class PwmUtils {
    private PwmUtils() {
        throw new AssertionError("constructor of the utility class should not be called");
    }

    public static boolean isValidPin(int pin) {
        return (pin >= Konashi.PIO0) && (pin <= Konashi.PIO2);
    }

    public static boolean isValidMode(int mode) {
        return (mode == Konashi.PWM_ENABLE) || (mode == Konashi.PWM_DISABLE)
                || (mode == Konashi.PWM_ENABLE_LED_MODE);
    }

    public static boolean isValidPeriod(int period, int duty) {
        return (period >= 0) && (duty >= 0) && (duty <= period);
    }

    public static boolean isValidDuty(int duty, int period) {
        return isValidPeriod(period, duty);
    }

    public static boolean isValidDutyRatio(float dutyRatio) {
        return dutyRatio >= 0.0 && dutyRatio <= 100.0;
    }

    public static int getPwmDuty(byte[] value) {
        byte[] bytes = Arrays.copyOfRange(value, 1, value.length);
        return KonashiUtils.bytes2int(bytes);
    }

    public static int getPwmPeriod(byte[] value) {
        byte[] bytes = Arrays.copyOfRange(value, 1, value.length);
        return KonashiUtils.bytes2int(bytes);
    }
}