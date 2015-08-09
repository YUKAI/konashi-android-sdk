package com.uxxu.konashi.lib.events;

import com.uxxu.konashi.lib.listeners.KonashiBaseListener;
import com.uxxu.konashi.lib.listeners.KonashiUartListener;

/**
 * Created by izumin on 8/7/15.
 */
public enum KonashiUartEvent implements KonashiEvent {
    /**
     * UARTの有効/無効が切り替わった時
     */
    UPDATE_UART_MODE {
        @Override
        protected void notifyUartEvent(Object param, KonashiUartListener listener) {
            listener.onUpdateUartMode(Integer.valueOf(param.toString()));
        }
    },
    /**
     * UARTの通信速度が変更された時
     */
    UPDATE_UART_BAUDRATE {
        @Override
        protected void notifyUartEvent(Object param, KonashiUartListener listener) {
            listener.onUpdateUartBaudrate(Integer.valueOf(param.toString()));
        }
    },
    /**
     * UARTでデータを送信した時
     */
    WRITE_UART {
        @Override
        protected void notifyUartEvent(Object param, KonashiUartListener listener) {
            listener.onWriteUart((byte[]) param);
        }
    },
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
