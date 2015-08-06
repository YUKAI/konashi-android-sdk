package com.uxxu.konashi.lib.events;

import com.uxxu.konashi.lib.listeners.KonashiBaseListener;

/**
 * Created by izumin on 8/7/15.
 */
public enum KonashiI2cEvent implements KonashiEvent {
    /**
     * I2Cからデータを受信した時
     */
    I2C_READ_COMPLETE;

    @Override
    public void notify(Object param0, Object param1, KonashiBaseListener listener) {
        // TODO: Not yet implemented.
    }
}
