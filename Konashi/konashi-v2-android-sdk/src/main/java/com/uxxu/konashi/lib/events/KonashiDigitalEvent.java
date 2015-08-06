package com.uxxu.konashi.lib.events;

import com.uxxu.konashi.lib.KonashiEvent;
import com.uxxu.konashi.lib.listeners.KonashiBaseListener;
import com.uxxu.konashi.lib.listeners.KonashiDigitalListener;

/**
 * Created by izumin on 8/7/15.
 */
public enum KonashiDigitalEvent implements KonashiEvent {
    /**
     * PIOの入力の状態が変化した時
     */
    UPDATE_PIO_INPUT {
        @Override
        protected void notifyDigitalEvent(Object param, KonashiDigitalListener listener) {
            listener.onUpdatePioInput(Byte.valueOf(param.toString()));
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
