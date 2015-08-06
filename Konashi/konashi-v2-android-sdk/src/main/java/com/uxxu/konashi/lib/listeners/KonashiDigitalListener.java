package com.uxxu.konashi.lib.listeners;

/**
 * Created by izumin on 8/6/15.
 */
public interface KonashiDigitalListener extends KonashiBaseListener {
    /**
     * PIOの入力の状態が変化した時に呼ばれる
     */
    void onUpdatePioInput(byte value);
}
