package com.uxxu.konashi.lib.listeners;

/**
 * Created by izumin on 8/5/15.
 */
public interface KonashiConnectionListener extends KonashiBaseListener {
    /**
     * findWithNameで指定した名前のkonashiが見つからなかった時、もしくはまわりにBLEデバイスがなかった時に呼ばれる
     */
    void onNotFoundPeripheral();
    /**
     * konashiに接続した時(まだこの時はkonashiが使える状態ではありません)に呼ばれる
     */
    void onConnected();
    /**
     * konashiとの接続を切断した時に呼ばれる
     */
    void onDisconnected();
    /**
     * konashiに接続完了した時(この時からkonashiにアクセスできるようになります)に呼ばれる
     */
    void onReady();
    /**
     * BLEデバイス選択ダイアログをキャンセルした時に呼ばれる
     */
    void onCancelSelectKonashi();
}
