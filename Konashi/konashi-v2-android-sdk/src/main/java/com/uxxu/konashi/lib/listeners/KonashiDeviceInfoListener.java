package com.uxxu.konashi.lib.listeners;

/**
 * Created by izumin on 8/6/15.
 */
public interface KonashiDeviceInfoListener extends KonashiBaseListener {
    /**
     * konashiのバッテリーのレベルを取得できた時
     */
    void onUpdateBatteryLevel(int level);
    /**
     * konashiの電波強度を取得できた時
     */
    void onUpdateSignalStrength(int rssi);
}
