package com.uxxu.konashi.lib.events;

import com.uxxu.konashi.lib.listeners.KonashiBaseListener;
import com.uxxu.konashi.lib.listeners.KonashiI2cListener;

/**
 * Created by izumin on 8/7/15.
 */
public enum KonashiI2cEvent implements KonashiEvent {
    UPDATE_I2C_MODE {
        @Override
        protected void notifyI2cEvent(Object param0, Object param1, KonashiI2cListener listener) {
            listener.onUpdateI2cMode(Integer.valueOf(param0.toString()));
        }
    },
    SEND_I2C_CONDITION {
        @Override
        protected void notifyI2cEvent(Object param0, Object param1, KonashiI2cListener listener) {
            listener.onSendI2cCondition(Integer.valueOf(param0.toString()));
        }
    },
    WRITE_I2C {
        @Override
        protected void notifyI2cEvent(Object param0, Object param1, KonashiI2cListener listener) {
            listener.onWriteI2c((byte[]) param0, (byte) param1);
        }
    },
    I2C_READ_COMPLETE {
        @Override
        protected void notifyI2cEvent(Object param0, Object param1, KonashiI2cListener listener) {
            // TODO: Not yet implemented.
        }
    };

    abstract protected void notifyI2cEvent(Object param0, Object param1, KonashiI2cListener listener);

    @Override
    public void notify(Object param0, Object param1, KonashiBaseListener listener) {
        if (listener instanceof KonashiI2cListener) {
            notifyI2cEvent(param0, param1, (KonashiI2cListener) listener);
        }
    }
}
