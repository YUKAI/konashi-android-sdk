package com.uxxu.konashi.lib.events;

import com.uxxu.konashi.lib.listeners.KonashiBaseListener;
import com.uxxu.konashi.lib.listeners.KonashiPwmListener;

/**
 * Created by izumin on 8/9/15.
 */
public enum KonashiPwmEvent implements KonashiEvent {
    UPDATE_PWM_CONFIG {
        @Override
        protected void notifyPwmEvent(Object param0, Object param1, KonashiPwmListener listener) {
            listener.onUpdatePwmMode(Integer.valueOf(param0.toString()));
        }
    },
    UPDATE_PWM_PARAM {
        @Override
        protected void notifyPwmEvent(Object param0, Object param1, KonashiPwmListener listener) {
            listener.onUpdatePwmPeriod(Integer.valueOf(param0.toString()), Integer.valueOf(param1.toString()));
        }
    },
    UPDATE_PWM_DUTY {
        @Override
        protected void notifyPwmEvent(Object param0, Object param1, KonashiPwmListener listener) {
            listener.onUpdatePwmDuty(Integer.valueOf(param0.toString()), Integer.valueOf(param1.toString()));
        }
    };

    abstract protected void notifyPwmEvent(Object param0, Object param1, KonashiPwmListener listener);

    @Override
    public void notify(Object param0, Object param1, KonashiBaseListener listener) {
        if (listener instanceof KonashiPwmListener) {
            notifyPwmEvent(param0, param1, (KonashiPwmListener) listener);
        }
    }
}
