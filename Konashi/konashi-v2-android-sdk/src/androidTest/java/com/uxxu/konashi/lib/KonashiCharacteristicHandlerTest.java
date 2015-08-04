package com.uxxu.konashi.lib;

import android.bluetooth.BluetoothGattCharacteristic;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.UUID;

/**
 * Created by izumin on 8/4/15.
 */
@RunWith(AndroidJUnit4.class)
public class KonashiCharacteristicHandlerTest {
    public static final String TAG = KonashiCharacteristicHandlerTest.class.getSimpleName();

    @Spy private KonashiNotifier mNotifier;
    @Mock private KonashiListener mListener;
    @Mock private BluetoothGattCharacteristic mCharacteristic;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mNotifier.addListener(mListener);
    }

    @Test
    public void testHandleAnalogRead0() {
        Mockito.when(mCharacteristic.getValue()).thenReturn(new byte[] {0x03, 0x48});
        KonashiCharacteristicHandler
                .valueOf(KonashiUUID.ANALOG_READ0_UUID)
                .handle(mCharacteristic, mNotifier);
        Mockito.verify(mListener, Mockito.times(1)).onUpdateAnalogValueAio0(840);
    }

    @Test
    public void testHandleAnalogRead1() {
        Mockito.when(mCharacteristic.getValue()).thenReturn(new byte[] {0x03, 0x48});
        KonashiCharacteristicHandler
                .valueOf(KonashiUUID.ANALOG_READ1_UUID)
                .handle(mCharacteristic, mNotifier);
        Mockito.verify(mListener, Mockito.times(1)).onUpdateAnalogValueAio1(840);
    }

    @Test
    public void testHandleAnalogRead2() {
        Mockito.when(mCharacteristic.getValue()).thenReturn(new byte[] {0x03, 0x48});
        KonashiCharacteristicHandler
                .valueOf(KonashiUUID.ANALOG_READ2_UUID)
                .handle(mCharacteristic, mNotifier);
        Mockito.verify(mListener, Mockito.times(1)).onUpdateAnalogValueAio2(840);
    }

    @Test
    public void testHandleBatteryLevel() {
        Mockito.when(mCharacteristic.getValue()).thenReturn(new byte[] {0x48});
        KonashiCharacteristicHandler
                .valueOf(KonashiUUID.BATTERY_LEVEL_UUID)
                .handle(mCharacteristic, mNotifier);
        Mockito.verify(mListener, Mockito.times(1)).onUpdateBatteryLevel(72);
    }

    @Test
    public void testHandlePioNotification() {
        Mockito.when(mCharacteristic.getValue()).thenReturn(new byte[] {0x13});
        KonashiCharacteristicHandler
                .valueOf(KonashiUUID.PIO_INPUT_NOTIFICATION_UUID)
                .handle(mCharacteristic, mNotifier);
        Mockito.verify(mListener, Mockito.times(1)).onUpdatePioInput((byte) 0x13);
    }

    @Test
    public void testHandleUartRxNotification() {
        Mockito.when(mCharacteristic.getValue()).thenReturn("test".getBytes());
        KonashiCharacteristicHandler
                .valueOf(KonashiUUID.UART_RX_NOTIFICATION_UUID)
                .handle(mCharacteristic, mNotifier);
        Mockito.verify(mListener, Mockito.times(1)).onCompleteUartRx(new byte[] {0x74, 0x65, 0x73, 0x74});
    }

    @Test
    public void testHandleUnknownCharacteristic() {
        KonashiCharacteristicHandler.valueOf(UUID.randomUUID()).handle(mCharacteristic, mNotifier);
        Mockito.verifyNoMoreInteractions(mListener);
    }
}
