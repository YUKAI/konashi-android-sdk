package com.uxxu.konashi.lib.util;

import com.uxxu.konashi.lib.Konashi;

/**
 * Created by e10dokup on 2015/09/22
 **/
public final class UartUtils {
    private UartUtils() {
        throw new AssertionError("constructor of the utility class should not be called");
    }

    public static boolean isValidBaudrate(int baudrate){
        return baudrate == Konashi.UART_RATE_9K6 || baudrate == Konashi.UART_RATE_19K2 ||
                baudrate == Konashi.UART_RATE_38K4 || baudrate == Konashi.UART_RATE_57K6 ||
                baudrate == Konashi.UART_RATE_76K8 || baudrate == Konashi.UART_RATE_115K2;
    }
}