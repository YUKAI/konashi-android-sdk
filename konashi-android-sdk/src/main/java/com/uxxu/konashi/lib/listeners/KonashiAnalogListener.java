package com.uxxu.konashi.lib.listeners;

/**
 * Created by izumin on 8/6/15.
 */
public interface KonashiAnalogListener extends KonashiBaseListener {
    /**
     * AIOのどれかのピンの電圧が取得できた時
     */
    void onUpdateAnalogValue(int pin, int value);
    /**
     * AIO0の電圧が取得できた時
     */
    void onUpdateAnalogValueAio0(int value);
    /**
     * AIO1の電圧が取得できた時
     */
    void onUpdateAnalogValueAio1(int value);
    /**
     * AIO2の電圧が取得できた時
     */
    void onUpdateAnalogValueAio2(int value);
}
