package com.uxxu.konashi.lib.listeners;

/**
 * Created by izumin on 8/6/15.
 */
public interface KonashiUartListener extends KonashiBaseListener {
    /**
     * UARTのRxからデータを受信した時
     */
    void onCompleteUartRx(byte[] data);
}
