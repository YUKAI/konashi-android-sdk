package com.uxxu.konashi.lib.listeners;

/**
 * Created by izumin on 8/6/15.
 */
public interface KonashiDigitalListener extends KonashiBaseListener {
    /**
     * PIOのピンの設定が変化した時に呼ばれる
     */
    void onUpdatePioSetting(int modes);
    /**
     * PIOのピンをプルアップするかの設定が変化した時に呼ばれる
     */
    void onUpdatePioPullup(int pullups);
    /**
     * PIOの入力の状態が変化した時に呼ばれる
     */
    void onUpdatePioInput(byte value);
    /**
     * PIOの出力の状態が変化した時に呼ばれる
     */
    void onUpdatePioOutput(byte value);
}
