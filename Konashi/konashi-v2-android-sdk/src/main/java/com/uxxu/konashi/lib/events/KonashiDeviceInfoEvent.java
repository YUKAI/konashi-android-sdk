package com.uxxu.konashi.lib.events;

import com.uxxu.konashi.lib.KonashiEvent;
import com.uxxu.konashi.lib.listeners.KonashiBaseListener;
import com.uxxu.konashi.lib.listeners.KonashiDeviceInfoListener;

/**
 * Created by izumin on 8/7/15.
 */
public enum KonashiDeviceInfoEvent implements KonashiEvent {
    /**
     * konashiのバッテリーのレベルを取得できた時
     */
    UPDATE_BATTERY_LEVEL {
        @Override
        public void notifyDeviceInfoEvent(Object param, KonashiDeviceInfoListener listener) {
            listener.onUpdateBatteryLevel(Integer.valueOf(param.toString()));
        }
    },
    /**
     * konashiの電波強度を取得できた時
     */
    UPDATE_SIGNAL_STRENGTH {
        @Override
        public void notifyDeviceInfoEvent(Object param, KonashiDeviceInfoListener listener) {
            listener.onUpdateSignalStrength(Integer.valueOf(param.toString()));
        }
    };

    abstract protected void notifyDeviceInfoEvent(Object param0, KonashiDeviceInfoListener listener);

    @Override
    public void notify(Object param0, Object param1, KonashiBaseListener listener) {
        if (listener instanceof KonashiDeviceInfoListener) {
            notifyDeviceInfoEvent(param0, (KonashiDeviceInfoListener) listener);
        }
    }
}
