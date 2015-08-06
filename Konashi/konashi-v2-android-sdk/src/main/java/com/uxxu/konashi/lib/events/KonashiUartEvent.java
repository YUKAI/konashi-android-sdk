package com.uxxu.konashi.lib.events;

import com.uxxu.konashi.lib.KonashiEvent;
import com.uxxu.konashi.lib.listeners.KonashiBaseListener;
import com.uxxu.konashi.lib.listeners.KonashiUartListener;

/**
 * Created by izumin on 8/7/15.
 */
public enum KonashiUartEvent implements KonashiEvent {
    /**
     * UARTのRxからデータを受信した時
     */
    UART_RX_COMPLETE {
        @Override
        protected void notifyUartEvent(Object param, KonashiUartListener listener) {
            listener.onCompleteUartRx((byte[]) param);
        }
    };

    abstract protected void notifyUartEvent(Object param, KonashiUartListener listener);

    @Override
    public void notify(Object param0, Object param1, KonashiBaseListener listener) {
        if (listener instanceof KonashiUartListener) {
            notifyUartEvent(param0, (KonashiUartListener) listener);
        }
    }
}
