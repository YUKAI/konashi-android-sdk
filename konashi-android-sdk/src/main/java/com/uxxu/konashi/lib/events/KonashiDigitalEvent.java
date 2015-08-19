package com.uxxu.konashi.lib.events;

import com.uxxu.konashi.lib.listeners.KonashiBaseListener;
import com.uxxu.konashi.lib.listeners.KonashiDigitalListener;

/**
 * Created by izumin on 8/7/15.
 */
public enum KonashiDigitalEvent implements KonashiEvent {
    /**
     * PIOのピンの設定が変化した時
     */
    UPDATE_PIO_SETTING {
        @Override
        protected void notifyDigitalEvent(Object param, KonashiDigitalListener listener) {
            listener.onUpdatePioSetting(Byte.valueOf(param.toString()));
        }
    },
    /**
     * PIOのピンをプルアップするかの設定が変化した時
     */
    UPDATE_PIO_PULLUP {
        @Override
        protected void notifyDigitalEvent(Object param, KonashiDigitalListener listener) {
            listener.onUpdatePioPullup(Byte.valueOf(param.toString()));
        }
    },
    /**
     * PIOの入力の状態が変化した時
     */
    UPDATE_PIO_INPUT {
        @Override
        protected void notifyDigitalEvent(Object param, KonashiDigitalListener listener) {
            listener.onUpdatePioInput(Byte.valueOf(param.toString()));
        }
    },
    /**
     * PIOの出力の状態が変化した時
     */
    UPDATE_PIO_OUTPUT {
        @Override
        protected void notifyDigitalEvent(Object param, KonashiDigitalListener listener) {
            listener.onUpdatePioOutput(Byte.valueOf(param.toString()));
        }
    };

    abstract protected void notifyDigitalEvent(Object param0, KonashiDigitalListener listener);

    @Override
    public void notify(Object param0, Object param1, KonashiBaseListener listener) {
        if (listener instanceof KonashiDigitalListener) {
            notifyDigitalEvent(param0, (KonashiDigitalListener) listener);
        }
    }
}
