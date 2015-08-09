package com.uxxu.konashi.lib.listeners;

/**
 * Created by izumin on 8/9/15.
 */
public interface KonashiI2cListener {
    /**
     * I2Cの有効/無効が切り替わった時
     */
    void onUpdateI2cMode(int mode);
    /**
     * I2Cのコンディションが発行された時
     */
    void onSendI2cCondition(int condition);
    /**
     * I2Cでデータが書き込まれた時
     */
    void onWriteI2c(byte[] data, byte address);
    /**
     * I2Cでデータが読み込まれた時
     */
    void onReadI2c(byte[] data, byte address);
}
