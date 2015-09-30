package com.uxxu.konashi.lib.util;

import com.uxxu.konashi.lib.Konashi;

/**
 * Created by izumin on 9/16/15.
 */
public final class PioUtils {
    private PioUtils() {
        throw new AssertionError("constructor of the utility class should not be called");
    }

    public static boolean isValidPin(int pin) {
        return (pin >= Konashi.PIO0) && (pin <= Konashi.PIO7);
    }

    public static boolean isValidMode(int mode) {
        return (mode == Konashi.OUTPUT) || (mode == Konashi.INPUT);
    }

    public static boolean isValidModes(int modes) {
        return (modes >= 0x00) && (modes <= 0xFF);
    }

    public static boolean isValidPullup(int pullup) {
        return (pullup == Konashi.PULLUP) || (pullup == Konashi.NO_PULLS);
    }

    public static boolean isValidPullups(int pullups) {
        return (pullups >= 0x00) && (pullups <= 0xFF);
    }

    public static boolean isValidOutput(int output) {
        return (output == Konashi.HIGH) || (output == Konashi.LOW);
    }

    public static boolean isValidOutputs(int outputs) {
        return (outputs >= 0x00) && (outputs <= 0xFF);
    }
}