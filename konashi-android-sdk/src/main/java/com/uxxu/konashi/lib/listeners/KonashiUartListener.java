package com.uxxu.konashi.lib.listeners;

/**
 * Created by izumin on 8/6/15.
 */
public interface KonashiUartListener extends KonashiBaseListener {
    /**
     * UARTの有効/無効が切り替わった時
     */
    void onUpdateUartMode(int mode);
    /**
     * UARTの通信速度が変更された時
     */
    void onUpdateUartBaudrate(int baudrate);
    /**
     * UARTでデータを送信した時
     */
    void onWriteUart(byte[] data);
    /**
     * UARTのRxからデータを受信した時
     */
    void onCompleteUartRx(byte[] data);
}
