package com.uxxu.konashi.lib;

import android.support.test.runner.AndroidJUnit4;

import com.uxxu.konashi.lib.events.KonashiAnalogEvent;
import com.uxxu.konashi.lib.events.KonashiConnectionEvent;
import com.uxxu.konashi.lib.events.KonashiDeviceInfoEvent;
import com.uxxu.konashi.lib.events.KonashiDigitalEvent;
import com.uxxu.konashi.lib.events.KonashiUartEvent;
import com.uxxu.konashi.lib.listeners.KonashiListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

/**
 * Created by izumin on 8/2/15.
 */
@RunWith(Enclosed.class)
public class KonashiNotifierTest {
    public static final String TAG = KonashiNotifierTest.class.getSimpleName();

    abstract private static class BaseTest {
        private KonashiNotifier mNotifier;
        private KonashiListener mListener;

        @Before
        public void setUp() throws Exception {
            mNotifier = Mockito.spy(new KonashiNotifier());
            mListener = Mockito.mock(KonashiListener.class);
            mNotifier.addListener(mListener);
        }

        protected KonashiNotifier getNotifier() {
            return mNotifier;
        }

        protected KonashiListener getListner() {
            return mListener;
        }
    }

    @RunWith(AndroidJUnit4.class)
    public static class NotifyKonashiEventTest extends BaseTest {
        @Test
        public void callOnNotFoundPeripheral() {
            getNotifier().notifyKonashiEvent(KonashiConnectionEvent.PERIPHERAL_NOT_FOUND, null, null);
            Mockito.verify(getListner(), Mockito.times(1)).onNotFoundPeripheral();
        }

        @Test
        public void callOnConnected() {
            getNotifier().notifyKonashiEvent(KonashiConnectionEvent.CONNECTED, null, null);
            Mockito.verify(getListner(), Mockito.times(1)).onConnected();
        }

        @Test
        public void callOnDisconnected() {
            getNotifier().notifyKonashiEvent(KonashiConnectionEvent.DISCONNECTED, null, null);
            Mockito.verify(getListner(), Mockito.times(1)).onDisconnected();
        }

        @Test
        public void callOnReady() {
            getNotifier().notifyKonashiEvent(KonashiConnectionEvent.READY, null, null);
            Mockito.verify(getListner(), Mockito.times(1)).onReady();
        }

        @Test
        public void callOnUpdatePioInput() {
            getNotifier().notifyKonashiEvent(KonashiDigitalEvent.UPDATE_PIO_INPUT, 0x01, null);
            Mockito.verify(getListner(), Mockito.times(1)).onUpdatePioInput((byte) 0x01);
        }

        @Test
        public void callOnUpdateAnalogValue() {
            getNotifier().notifyKonashiEvent(KonashiAnalogEvent.UPDATE_ANALOG_VALUE, 0x01, 0xff);
            Mockito.verify(getListner(), Mockito.times(1)).onUpdateAnalogValue(1, 255);
        }

        @Test
        public void callOnUpdateAnalogValueAio0() {
            getNotifier().notifyKonashiEvent(KonashiAnalogEvent.UPDATE_ANALOG_VALUE_AIO0, 0xff, null);
            Mockito.verify(getListner(), Mockito.times(1)).onUpdateAnalogValueAio0(255);
        }

        @Test
        public void callOnUpdateAnalogValueAio1() {
            getNotifier().notifyKonashiEvent(KonashiAnalogEvent.UPDATE_ANALOG_VALUE_AIO1, 0xff, null);
            Mockito.verify(getListner(), Mockito.times(1)).onUpdateAnalogValueAio1(255);
        }

        @Test
        public void callOnUpdateAnalogValueAio2() {
            getNotifier().notifyKonashiEvent(KonashiAnalogEvent.UPDATE_ANALOG_VALUE_AIO2, 0xff, null);
            Mockito.verify(getListner(), Mockito.times(1)).onUpdateAnalogValueAio2(255);
        }

        @Test
        public void callOnCompleteUartRx() {
            byte[] data = "test".getBytes();
            getNotifier().notifyKonashiEvent(KonashiUartEvent.UART_RX_COMPLETE, data, null);
            Mockito.verify(getListner(), Mockito.times(1)).onCompleteUartRx(data);
        }

        @Test
        public void callOnUpdateBatteryLevel() {
            getNotifier().notifyKonashiEvent(KonashiDeviceInfoEvent.UPDATE_BATTERY_LEVEL, 50, null);
            Mockito.verify(getListner(), Mockito.times(1)).onUpdateBatteryLevel(50);
        }

        @Test
        public void callOnUpdateSignalStrength() {
            getNotifier().notifyKonashiEvent(KonashiDeviceInfoEvent.UPDATE_SIGNAL_STRENGTH, 25, null);
            Mockito.verify(getListner(), Mockito.times(1)).onUpdateSignalStrength(25);
        }

        @Test
        public void callOnCancelSelectKonashi() {
            getNotifier().notifyKonashiEvent(KonashiConnectionEvent.CANCEL_SELECT_KONASHI, null, null);
            Mockito.verify(getListner(), Mockito.times(1)).onCancelSelectKonashi();
        }
    }

    @RunWith(AndroidJUnit4.class)
    public static class NotifyKonashiErrorTest extends BaseTest {
        @Test
        public void callOnError() {
            KonashiErrorReason error = KonashiErrorReason.ALREADY_READY;
            getNotifier().notifyKonashiError(error);
            Mockito.verify(getListner(), Mockito.times(1)).onError(Mockito.eq(error), Mockito.anyString());
        }
    }
}
