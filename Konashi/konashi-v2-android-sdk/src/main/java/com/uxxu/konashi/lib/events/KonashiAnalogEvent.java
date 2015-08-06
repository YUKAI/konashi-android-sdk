package com.uxxu.konashi.lib.events;

import com.uxxu.konashi.lib.KonashiEvent;
import com.uxxu.konashi.lib.listeners.KonashiAnalogListener;
import com.uxxu.konashi.lib.listeners.KonashiBaseListener;

/**
 * Created by izumin on 8/6/15.
 */
public enum KonashiAnalogEvent implements KonashiEvent {
    /**
     * AIOのどれかのピンの電圧が取得できた時
     */
    UPDATE_ANALOG_VALUE {
        @Override
        public void notifyAnalogEvent(Object param0, Object param1, KonashiAnalogListener listener) {
            listener.onUpdateAnalogValue(
                    Integer.valueOf(param0.toString()),
                    Integer.valueOf(param1.toString())
            );
        }
    },
    /**
     * AIO0の電圧が取得できた時
     */
    UPDATE_ANALOG_VALUE_AIO0 {
        @Override
        public void notifyAnalogEvent(Object param0, Object param1, KonashiAnalogListener listener) {
            listener.onUpdateAnalogValueAio0(Integer.valueOf(param0.toString()));
        }
    },
    /**
     * AIO1の電圧が取得できた時
     */
    UPDATE_ANALOG_VALUE_AIO1 {
        @Override
        protected void notifyAnalogEvent(Object param0, Object param1, KonashiAnalogListener listener) {
            listener.onUpdateAnalogValueAio1(Integer.valueOf(param0.toString()));
        }
    },
    /**
     * AIO2の電圧が取得できた時
     */
    UPDATE_ANALOG_VALUE_AIO2 {
        @Override
        protected void notifyAnalogEvent(Object param0, Object param1, KonashiAnalogListener listener) {
            listener.onUpdateAnalogValueAio2(Integer.valueOf(param0.toString()));
        }
    };

    abstract protected void notifyAnalogEvent(Object param0, Object param1, KonashiAnalogListener listener);

    @Override
    public void notify(Object param0, Object param1, KonashiBaseListener listener) {
        if (listener instanceof KonashiAnalogListener) {
            notifyAnalogEvent(param0, param1, (KonashiAnalogListener) listener);
        }
    }
}
