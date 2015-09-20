package com.uxxu.konashi.lib.util;

import com.uxxu.konashi.lib.Konashi;

/**
 * Created by izumin on 9/21/15.
 */
public final class I2cUtils {
    private I2cUtils() {
        throw new AssertionError("constructor of the utility class should not be called");
    }

    public static boolean isValidMode(int mode) {
        return (mode == Konashi.I2C_DISABLE) || (mode == Konashi.I2C_ENABLE)
                || (mode == Konashi.I2C_ENABLE_100K) || (mode == Konashi.I2C_ENABLE_400K);
    }
}