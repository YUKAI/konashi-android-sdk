package com.uxxu.konashi.lib.listeners;

/**
 * Created by izumin on 8/9/15.
 */
public interface KonashiPwmListener extends KonashiBaseListener {
    /**
     * PIOのピンのPWM設定が変化した時に呼ばれる
     */
    void onUpdatePwmMode(int modes);
    /**
     * ピンのPWM周期が変化したときに呼ばれる
     */
    void onUpdatePwmPeriod(int pin, int period);
    /**
     * ピンのPWMのデューティが変化したときに呼ばれる
     */
    void onUpdatePwmDuty(int pin, int duty);
}
