package com.uxxu.konashi.test_all_functions;

import android.view.ViewGroup;
import android.widget.TableRow;

import com.uxxu.konashi.lib.Konashi;

/**
 * Created by kiryu on 7/28/15.
 */
public class Utils {

    public static final int[] PIO_PINS = new int[]{Konashi.PIO0, Konashi.PIO1, Konashi.PIO2, Konashi.PIO3, Konashi.PIO4, Konashi.PIO5};

    public static final int[] PWM_PINS = new int[]{Konashi.PIO0, Konashi.PIO1, Konashi.PIO2};

    public static final int[] AIO_PINS = new int[]{Konashi.AIO0, Konashi.AIO1, Konashi.AIO2};

    public static int uartLabelToValue(String uartLabel) {
        if (uartLabel.equals("2400")) {
            return Konashi.UART_RATE_2K4;
        }
        if (uartLabel.equals("9600")) {
            return Konashi.UART_RATE_9K6;
        }
        return 0;
    }

    public static TableRow.LayoutParams createTableRowLayoutParamwWithWeight(float weight) {
        return new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, weight);
    }

    public static void sleep() {
        sleep(100);
    }

    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
        }
    }
}
