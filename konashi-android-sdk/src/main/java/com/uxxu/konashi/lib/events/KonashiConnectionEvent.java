package com.uxxu.konashi.lib.events;

import com.uxxu.konashi.lib.listeners.KonashiBaseListener;
import com.uxxu.konashi.lib.listeners.KonashiConnectionListener;

/**
 * Created by izumin on 8/6/15.
 */
public enum KonashiConnectionEvent implements KonashiEvent {
    /**
     * findWithNameで指定した名前のkonashiが見つからなかった時、もしくはまわりにBLEデバイスがなかった時
     */
    PERIPHERAL_NOT_FOUND {
        @Override
        public void notifyConnectionEvent(KonashiConnectionListener listener) {
            listener.onNotFoundPeripheral();
        }
    },
    /**
     * BLEデバイス選択ダイアログをキャンセルした時
     */
    CANCEL_SELECT_KONASHI {
        @Override
        public void notifyConnectionEvent(KonashiConnectionListener listener) {
            listener.onCancelSelectKonashi();
        }
    },
    /**
     * konashiに接続完了した時(この時からkonashiにアクセスできるようになります)
     */
    READY {
        @Override
        public void notifyConnectionEvent(KonashiConnectionListener listener) {
            listener.onReady();
        }
    },
    /**
     * konashiに接続した時(まだこの時はkonashiが使える状態ではありません)
     */
    CONNECTED {
        @Override
        public void notifyConnectionEvent(KonashiConnectionListener listener) {
            listener.onConnected();
        }
    },
    /**
     * konashiとの接続を切断した時
     */
    DISCONNECTED {
        @Override
        public void notifyConnectionEvent(KonashiConnectionListener listener) {
            listener.onDisconnected();
        }
    };

    abstract protected void notifyConnectionEvent(KonashiConnectionListener listener);

    @Override
    public void notify(Object param0, Object param1, KonashiBaseListener listener) {
        if (listener instanceof KonashiConnectionListener) {
            notifyConnectionEvent((KonashiConnectionListener) listener);
        }
    }
}
