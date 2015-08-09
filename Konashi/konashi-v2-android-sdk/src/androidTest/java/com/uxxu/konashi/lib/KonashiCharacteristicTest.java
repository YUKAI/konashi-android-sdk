package com.uxxu.konashi.lib;

import android.bluetooth.BluetoothGattCharacteristic;
import android.support.test.runner.AndroidJUnit4;

import com.uxxu.konashi.lib.listeners.KonashiListener;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.UUID;

/**
 * Created by izumin on 8/4/15.
 */
@RunWith(Enclosed.class)
public class KonashiCharacteristicTest {
    public static final String TAG = KonashiCharacteristicTest.class.getSimpleName();

    private static class BaseTest {
        @Spy private KonashiNotifier mNotifier;
        @Mock private KonashiListener mListener;
        @Mock private BluetoothGattCharacteristic mCharacteristic;

        @Before
        public void setUp() {
            MockitoAnnotations.initMocks(this);
            mNotifier.addListener(mListener);
        }

        public KonashiNotifier getNotifier() {
            return mNotifier;
        }

        public KonashiListener getListener() {
            return mListener;
        }

        public BluetoothGattCharacteristic getCharacteristic() {
            return mCharacteristic;
        }
    }

    @RunWith(AndroidJUnit4.class)
    public static class AnalogCharacteristicTest extends BaseTest {
        @Test
        public void testHandleAnalogRead0() {
            Mockito.when(getCharacteristic().getValue()).thenReturn(new byte[]{0x03, 0x48});
            KonashiCharacteristic
                    .valueOf(KonashiUUID.ANALOG_READ0_UUID)
                    .handle(getCharacteristic(), getNotifier());
            Mockito.verify(getListener(), Mockito.times(1)).onUpdateAnalogValueAio0(840);
        }

        @Test
        public void testHandleAnalogRead1() {
            Mockito.when(getCharacteristic().getValue()).thenReturn(new byte[]{0x03, 0x48});
            KonashiCharacteristic
                    .valueOf(KonashiUUID.ANALOG_READ1_UUID)
                    .handle(getCharacteristic(), getNotifier());
            Mockito.verify(getListener(), Mockito.times(1)).onUpdateAnalogValueAio1(840);
        }

        @Test
        public void testHandleAnalogRead2() {
            Mockito.when(getCharacteristic().getValue()).thenReturn(new byte[]{0x03, 0x48});
            KonashiCharacteristic
                    .valueOf(KonashiUUID.ANALOG_READ2_UUID)
                    .handle(getCharacteristic(), getNotifier());
            Mockito.verify(getListener(), Mockito.times(1)).onUpdateAnalogValueAio2(840);
        }
    }

    @RunWith(AndroidJUnit4.class)
    public static class PioCharacteristicTest extends BaseTest {
        @Test
        public void testHandlePioSetting() {
            Mockito.when(getCharacteristic().getValue()).thenReturn(new byte[]{0x08});
            KonashiCharacteristic
                    .valueOf(KonashiUUID.PIO_SETTING_UUID)
                    .handle(getCharacteristic(), getNotifier());
            Mockito.verify(getListener(), Mockito.times(1)).onUpdatePioSetting((byte) 0x08);
        }

        @Test
        public void testHandlePioPullup() {
            Mockito.when(getCharacteristic().getValue()).thenReturn(new byte[]{0x08});
            KonashiCharacteristic
                    .valueOf(KonashiUUID.PIO_PULLUP_UUID)
                    .handle(getCharacteristic(), getNotifier());
            Mockito.verify(getListener(), Mockito.times(1)).onUpdatePioPullup((byte) 0x08);
        }

        @Test
        public void testHandlePioNotification() {
            Mockito.when(getCharacteristic().getValue()).thenReturn(new byte[]{0x13});
            KonashiCharacteristic
                    .valueOf(KonashiUUID.PIO_INPUT_NOTIFICATION_UUID)
                    .handle(getCharacteristic(), getNotifier());
            Mockito.verify(getListener(), Mockito.times(1)).onUpdatePioInput((byte) 0x13);
        }
    }

    @RunWith(AndroidJUnit4.class)
    public static class PwmCharacteristicTset extends BaseTest {
        @Test
        public void testHandlePwmMode() {
            Mockito.when(getCharacteristic().getValue()).thenReturn(new byte[]{0x04});
            KonashiCharacteristic
                    .valueOf(KonashiUUID.PWM_CONFIG_UUID)
                    .handle(getCharacteristic(), getNotifier());
            Mockito.verify(getListener(), Mockito.times(1)).onUpdatePwmMode(4);
        }

        @Test
        public void testHandlePwmPeriod() {
            Mockito.when(getCharacteristic().getValue()).thenReturn(new byte[]{0x02, 0x00, 0x00, 0x27, 0x10});
            KonashiCharacteristic
                    .valueOf(KonashiUUID.PWM_PARAM_UUID)
                    .handle(getCharacteristic(), getNotifier());
            Mockito.verify(getListener(), Mockito.times(1)).onUpdatePwmPeriod(2, 10000);
        }

        @Test
        public void testHandlePwmDuty() {
            Mockito.when(getCharacteristic().getValue()).thenReturn(new byte[]{0x02, 0x00, 0x00, 0x27, 0x10});
            KonashiCharacteristic
                    .valueOf(KonashiUUID.PWM_DUTY_UUID)
                    .handle(getCharacteristic(), getNotifier());
            Mockito.verify(getListener(), Mockito.times(1)).onUpdatePwmDuty(2, 10000);
        }
    }

    @RunWith(AndroidJUnit4.class)
    public static class UartCharacteristicTest extends BaseTest {
        @Test
        public void testHandleUartMode() {
            Mockito.when(getCharacteristic().getValue()).thenReturn(new byte[]{0x01});
            KonashiCharacteristic
                    .valueOf(KonashiUUID.UART_CONFIG_UUID)
                    .handle(getCharacteristic(), getNotifier());
            Mockito.verify(getListener(), Mockito.times(1)).onUpdateUartMode(1);
        }

        @Test
        public void testHandleUartBaudrate() {
            Mockito.when(getCharacteristic().getValue()).thenReturn(new byte[]{Konashi.UART_RATE_9K6});
            KonashiCharacteristic
                    .valueOf(KonashiUUID.UART_BAUDRATE_UUID)
                    .handle(getCharacteristic(), getNotifier());
            Mockito.verify(getListener(), Mockito.times(1)).onUpdateUartBaudrate(Konashi.UART_RATE_9K6);
        }

        @Test
        public void testHandleWriteUart() {
            Mockito.when(getCharacteristic().getValue()).thenReturn(new byte[]{0x04, 0x74, 0x65, 0x73, 0x74});
            KonashiCharacteristic
                    .valueOf(KonashiUUID.UART_TX_UUID)
                    .handle(getCharacteristic(), getNotifier());
            Mockito.verify(getListener(), Mockito.times(1)).onWriteUart(new byte[]{0x74, 0x65, 0x73, 0x74});
        }

        @Test
        public void testHandleUartRxNotification() {
            Mockito.when(getCharacteristic().getValue()).thenReturn("test".getBytes());
            KonashiCharacteristic
                    .valueOf(KonashiUUID.UART_RX_NOTIFICATION_UUID)
                    .handle(getCharacteristic(), getNotifier());
            Mockito.verify(getListener(), Mockito.times(1)).onCompleteUartRx(new byte[]{0x74, 0x65, 0x73, 0x74});
        }
    }

    @RunWith(AndroidJUnit4.class)
    public static class I2cCharacteristicTest extends BaseTest {
        @Test
        public void testHandleI2cMode() {
            Mockito.when(getCharacteristic().getValue()).thenReturn(new byte[]{0x02});
            KonashiCharacteristic
                    .valueOf(KonashiUUID.I2C_CONFIG_UUID)
                    .handle(getCharacteristic(), getNotifier());
            Mockito.verify(getListener(), Mockito.times(1)).onUpdateI2cMode(2);
        }

        @Test
        public void testHandleSendI2cCondition() {
            Mockito.when(getCharacteristic().getValue()).thenReturn(new byte[]{0x02});
            KonashiCharacteristic
                    .valueOf(KonashiUUID.I2C_START_STOP_UUID)
                    .handle(getCharacteristic(), getNotifier());
            Mockito.verify(getListener(), Mockito.times(1)).onSendI2cCondition(2);
        }

        @Test
        public void testHandleWriteI2c() {
            Mockito.when(getCharacteristic().getValue()).thenReturn(new byte[]{0x05, 0x3e, 0x74, 0x65, 0x73, 0x74});
            KonashiCharacteristic
                    .valueOf(KonashiUUID.I2C_WRITE_UUID)
                    .handle(getCharacteristic(), getNotifier());
            Mockito.verify(getListener(), Mockito.times(1)).onWriteI2c(new byte[]{0x74, 0x65, 0x73, 0x74}, (byte) 0x1f);
        }

        @Ignore("Not yet implemented...")
        @Test
        public void testHandleReadI2c() {
            // TODO: Not yet implemented...
        }
    }

    @RunWith(AndroidJUnit4.class)
    public static class DeviceInfoCharacteristicTest extends BaseTest {
        @Test
        public void testHandleBatteryLevel() {
            Mockito.when(getCharacteristic().getValue()).thenReturn(new byte[]{0x48});
            KonashiCharacteristic
                    .valueOf(KonashiUUID.BATTERY_LEVEL_UUID)
                    .handle(getCharacteristic(), getNotifier());
            Mockito.verify(getListener(), Mockito.times(1)).onUpdateBatteryLevel(72);
        }
    }

    @RunWith(AndroidJUnit4.class)
    public static class UnknownCharacteristicTest extends BaseTest {
        @Test
        public void testHandleUnknownCharacteristic() {
            KonashiCharacteristic.valueOf(UUID.randomUUID()).handle(getCharacteristic(), getNotifier());
            Mockito.verifyNoMoreInteractions(getListener());
        }
    }
}
