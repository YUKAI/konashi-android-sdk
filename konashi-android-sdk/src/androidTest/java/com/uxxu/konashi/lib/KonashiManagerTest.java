package com.uxxu.konashi.lib;

import android.support.test.runner.AndroidJUnit4;

import com.uxxu.konashi.lib.stores.KonashiAnalogStore;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.internal.util.reflection.Whitebox;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Created by izumin on 7/31/15.
 */
@RunWith(Enclosed.class)
public class KonashiManagerTest {
    public static final String TAG = KonashiManagerTest.class.getSimpleName();

    abstract public static class BaseTest {
        @Spy private KonashiManager mManager;

        private ArgumentCaptor<UUID> mWriteMessageUuidCaptor;
        private ArgumentCaptor<byte[]> mWriteMessageValueCaptor;
        private ArgumentCaptor<KonashiErrorReason> mNotifyKonashiErrorCaptor;

        @Before
        public void setUp() throws Exception {
            MockitoAnnotations.initMocks(this);

            mWriteMessageUuidCaptor = ArgumentCaptor.forClass(UUID.class);
            mWriteMessageValueCaptor = ArgumentCaptor.forClass(byte[].class);
            Mockito.doNothing().when(mManager)
                    .addWriteMessage(mWriteMessageUuidCaptor.capture(), mWriteMessageValueCaptor.capture());

            mNotifyKonashiErrorCaptor = ArgumentCaptor.forClass(KonashiErrorReason.class);
            Mockito.doNothing().when(mManager).notifyKonashiError(mNotifyKonashiErrorCaptor.capture());
        }

        protected KonashiManager getManager() {
            return mManager;
        }

        protected void stubIsEnableAccessKonashi(boolean isEnable) {
            Mockito.doReturn(isEnable).when(mManager).isEnableAccessKonashi();
        }

        protected void stubIsEnableI2c(boolean isEnable) {
            Mockito.doReturn(isEnable).when(mManager).isEnableI2c();
        }

        public UUID getCapturedWrittenUuid() {
            return mWriteMessageUuidCaptor.getValue();
        }

        public byte[] getCapturedWrittenValue() {
            return mWriteMessageValueCaptor.getValue();
        }

        public KonashiErrorReason getCapturedError() {
            return mNotifyKonashiErrorCaptor.getValue();
        }
    }

    @RunWith(Enclosed.class)
    public static class PioTest {
        @RunWith(AndroidJUnit4.class)
        public static class PinModeTest extends BaseTest {
            @Test
            public void whenKonashiIsNotEnable() {
                stubIsEnableAccessKonashi(false);
                getManager().pinMode(9999, 9999);
                assertThat(getCapturedError()).isEqualTo(KonashiErrorReason.NOT_READY);
            }

            @Test
            public void withInvalidPin() {
                stubIsEnableAccessKonashi(true);
                getManager().pinMode(9999, Konashi.OUTPUT);
                assertThat(getCapturedError()).isEqualTo(KonashiErrorReason.INVALID_PARAMETER);
            }

            @Test
            public void withInvalidMode() {
                stubIsEnableAccessKonashi(true);
                getManager().pinMode(Konashi.PIO1, 9999);
                assertThat(getCapturedError()).isEqualTo(KonashiErrorReason.INVALID_PARAMETER);
            }

            @Test
            public void whenPioModeHasNotSet() {
                stubIsEnableAccessKonashi(true);
                getManager().pinMode(Konashi.PIO1, Konashi.OUTPUT);
                assertThat(getCapturedWrittenUuid()).isEqualTo(KonashiUUID.PIO_SETTING_UUID);
                assertThat(getCapturedWrittenValue()).isEqualTo(new byte[] {0x02});
            }

            @Test
            public void whenPioModeHasAlreadySet() {
                stubIsEnableAccessKonashi(true);
                Whitebox.setInternalState(getManager(), "mPioModeSetting", (byte) 0x17);
                getManager().pinMode(Konashi.PIO1, Konashi.INPUT);
                assertThat(getCapturedWrittenUuid()).isEqualTo(KonashiUUID.PIO_SETTING_UUID);
                assertThat(getCapturedWrittenValue()).isEqualTo(new byte[] {0x15});
            }
        }

        @RunWith(AndroidJUnit4.class)
        public static class PinModeAllTest extends BaseTest {
            @Test
            public void whenKonashiIsNotEnable() {
                stubIsEnableAccessKonashi(false);
                getManager().pinModeAll(9999);
                assertThat(getCapturedError()).isEqualTo(KonashiErrorReason.NOT_READY);
            }

            @Test
            public void withInvalidMode() {
                stubIsEnableAccessKonashi(true);
                getManager().pinModeAll(9999);
                assertThat(getCapturedError()).isEqualTo(KonashiErrorReason.INVALID_PARAMETER);
            }

            @Test
            public void whenPioModeAll() {
                stubIsEnableAccessKonashi(true);
                getManager().pinModeAll(0x17);
                assertThat(getCapturedWrittenUuid()).isEqualTo(KonashiUUID.PIO_SETTING_UUID);
                assertThat(getCapturedWrittenValue()).isEqualTo(new byte[] {0x17});
            }
        }

        @RunWith(AndroidJUnit4.class)
        public static class PinPullupTest extends BaseTest {
            @Test
            public void whenKonashiIsNotEnable() {
                stubIsEnableAccessKonashi(false);
                getManager().pinPullup(9999, 9999);
                assertThat(getCapturedError()).isEqualTo(KonashiErrorReason.NOT_READY);
            }

            @Test
            public void withInvalidPin() {
                stubIsEnableAccessKonashi(true);
                getManager().pinPullup(9999, Konashi.PULLUP);
                assertThat(getCapturedError()).isEqualTo(KonashiErrorReason.INVALID_PARAMETER);
            }

            @Test
            public void withInvalidMode() {
                stubIsEnableAccessKonashi(true);
                getManager().pinPullup(Konashi.PIO1, 9999);
                assertThat(getCapturedError()).isEqualTo(KonashiErrorReason.INVALID_PARAMETER);
            }

            @Test
            public void whenPioPullupHasNotSet() {
                stubIsEnableAccessKonashi(true);
                getManager().pinPullup(Konashi.PIO1, Konashi.PULLUP);
                assertThat(getCapturedWrittenUuid()).isEqualTo(KonashiUUID.PIO_PULLUP_UUID);
                assertThat(getCapturedWrittenValue()).isEqualTo(new byte[] {0x02});
            }

            @Test
            public void whenPioPullupHasAlreadySet() {
                stubIsEnableAccessKonashi(true);
                Whitebox.setInternalState(getManager(), "mPioPullup", (byte) 0x17);
                getManager().pinPullup(Konashi.PIO1, Konashi.NO_PULLS);
                assertThat(getCapturedWrittenUuid()).isEqualTo(KonashiUUID.PIO_PULLUP_UUID);
                assertThat(getCapturedWrittenValue()).isEqualTo(new byte[] {0x15});
            }
        }
    }

    @RunWith(Enclosed.class)
    public static class AnalogTest {
        @RunWith(AndroidJUnit4.class)
        public static class AnalogReadTest extends BaseTest {
            @Spy private KonashiAnalogStore mAnalogStore;

            @Before
            public void setUp() throws Exception {
                super.setUp();
                Whitebox.setInternalState(getManager(), "mAnalogStore", mAnalogStore);
            }

            @Test
            public void whenKonashiIsNotEnable() {
                stubIsEnableAccessKonashi(false);
                getManager().analogRead(9999);
                assertThat(getCapturedError()).isEqualTo(KonashiErrorReason.NOT_READY);
            }

            @Test
            public void withInvalidPin() {
                stubIsEnableAccessKonashi(true);
                getManager().analogRead(9999);
                assertThat(getCapturedError()).isEqualTo(KonashiErrorReason.INVALID_PARAMETER);
            }

            @Test
            public void withValidPin() {
                stubIsEnableAccessKonashi(true);
                getManager().analogRead(Konashi.AIO1);
                Mockito.verify(mAnalogStore, Mockito.times(1)).getAnalogValue(Konashi.AIO1);
            }
        }
    }

    @RunWith(Enclosed.class)
    public static class I2cTest {

        @RunWith(AndroidJUnit4.class)
        public static class I2cModeTest extends BaseTest {
            @Test
            public void withInValidMode() {
                stubIsEnableAccessKonashi(true);
                getManager().i2cMode(9999);
                assertThat(getCapturedError()).isEqualTo(KonashiErrorReason.INVALID_PARAMETER);
            }

            @Test
            public void whenKonashiIsNotEnable() {
                stubIsEnableAccessKonashi(false);
                getManager().i2cMode(9999);
                assertThat(getCapturedError()).isEqualTo(KonashiErrorReason.NOT_READY);
            }

            @Test
            public void withValidMode() {
                stubIsEnableAccessKonashi(true);
                getManager().i2cMode(Konashi.I2C_ENABLE);
                assertThat(getCapturedWrittenUuid()).isEqualTo(KonashiUUID.I2C_CONFIG_UUID);
                assertThat(getCapturedWrittenValue()).isEqualTo(new byte[] {1});
            }
        }

        @RunWith(AndroidJUnit4.class)
        public static class I2cStartConditionTest extends BaseTest {
            @Test
            public void whenKonashiIsNotEnable() {
                stubIsEnableAccessKonashi(false);
                getManager().i2cStartCondition();
                assertThat(getCapturedError()).isEqualTo(KonashiErrorReason.NOT_READY);
            }

            @Test
            public void whenI2cIsNotEnable() {
                stubIsEnableAccessKonashi(true);
                stubIsEnableI2c(false);
                getManager().i2cStartCondition();
                assertThat(getCapturedError()).isEqualTo(KonashiErrorReason.NOT_ENABLED_I2C);
            }

            @Test
            public void whenKonashiAndI2CIsEnable() {
                stubIsEnableAccessKonashi(true);
                stubIsEnableI2c(true);
                getManager().i2cStartCondition();
                assertThat(getCapturedWrittenUuid()).isEqualTo(KonashiUUID.I2C_START_STOP_UUID);
                assertThat(getCapturedWrittenValue()).isEqualTo(new byte[]{Konashi.I2C_START_CONDITION});
            }
        }

        @RunWith(AndroidJUnit4.class)
        public static class I2cRestartConditionTest extends BaseTest {
            @Test
            public void whenKonashiIsNotEnable() {
                stubIsEnableAccessKonashi(false);
                getManager().i2cRestartCondition();
                assertThat(getCapturedError()).isEqualTo(KonashiErrorReason.NOT_READY);
            }

            @Test
            public void whenI2cIsNotEnable() {
                stubIsEnableAccessKonashi(true);
                stubIsEnableI2c(false);
                getManager().i2cRestartCondition();
                assertThat(getCapturedError()).isEqualTo(KonashiErrorReason.NOT_ENABLED_I2C);
            }

            @Test
            public void whenKonashiAndI2CIsEnable() {
                stubIsEnableAccessKonashi(true);
                stubIsEnableI2c(true);
                getManager().i2cRestartCondition();
                assertThat(getCapturedWrittenUuid()).isEqualTo(KonashiUUID.I2C_START_STOP_UUID);
                assertThat(getCapturedWrittenValue()).isEqualTo(new byte[] {Konashi.I2C_RESTART_CONDITION});
            }
        }

        @RunWith(AndroidJUnit4.class)
        public static class I2cStopConditionTest extends BaseTest {
            @Test
            public void whenKonashiIsNotEnable() {
                stubIsEnableAccessKonashi(false);
                getManager().i2cStopCondition();
                assertThat(getCapturedError()).isEqualTo(KonashiErrorReason.NOT_READY);
            }

            @Test
            public void whenI2cIsNotEnable() {
                stubIsEnableAccessKonashi(true);
                stubIsEnableI2c(false);
                getManager().i2cStopCondition();
                assertThat(getCapturedError()).isEqualTo(KonashiErrorReason.NOT_ENABLED_I2C);
            }

            @Test
            public void whenKonashiAndI2CIsEnable() {
                stubIsEnableAccessKonashi(true);
                stubIsEnableI2c(true);
                getManager().i2cStopCondition();
                assertThat(getCapturedWrittenUuid()).isEqualTo(KonashiUUID.I2C_START_STOP_UUID);
                assertThat(getCapturedWrittenValue()).isEqualTo(new byte[] {Konashi.I2C_STOP_CONDITION});
            }
        }

        @RunWith(AndroidJUnit4.class)
        public static class I2cWriteTest extends BaseTest {
            private String mData = "test";
            private byte mAddress = 0x1f;

            @Test
            public void whenKonashiIsNotEnable() {
                stubIsEnableAccessKonashi(false);
                getManager().i2cWrite(mData.length(), mData.getBytes(), mAddress);
                assertThat(getCapturedError()).isEqualTo(KonashiErrorReason.NOT_READY);
            }

            @Test
            public void whenI2cIsNotEnable() {
                stubIsEnableAccessKonashi(true);
                stubIsEnableI2c(false);
                getManager().i2cWrite(mData.length(), mData.getBytes(), mAddress);
                assertThat(getCapturedError()).isEqualTo(KonashiErrorReason.NOT_ENABLED_I2C);
            }

            @Test
            public void withDataThatIsTooLong() {
                stubIsEnableAccessKonashi(true);
                stubIsEnableI2c(true);
                String invalidData = "too long data string";
                getManager().i2cWrite(invalidData.length(), invalidData.getBytes(), mAddress);
                assertThat(getCapturedError()).isEqualTo(KonashiErrorReason.INVALID_PARAMETER);
            }

            @Test
            public void withValidArguments() {
                stubIsEnableAccessKonashi(true);
                stubIsEnableI2c(true);
                getManager().i2cWrite(mData.length(), mData.getBytes(), mAddress);
                assertThat(getCapturedWrittenUuid()).isEqualTo(KonashiUUID.I2C_WRITE_UUID);
                byte[] value = new byte[] {
                        0x05, 0x3E, 0x74, 0x65, 0x73, 0x74, 0x00, 0x00, 0x00, 0x00,
                        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
                };
                assertThat(getCapturedWrittenValue()).isEqualTo(value);
            }
        }

        @RunWith(AndroidJUnit4.class)
        public static class I2cReadRequestTest extends BaseTest {
            private byte mAddress = 0x1f;

            @Test
            public void whenKonashiIsNotEnable() {
                stubIsEnableAccessKonashi(false);
                getManager().i2cReadRequest(0x13, mAddress);
                assertThat(getCapturedError()).isEqualTo(KonashiErrorReason.NOT_READY);
            }

            @Test
            public void whenI2cIsNotEnable() {
                stubIsEnableAccessKonashi(true);
                stubIsEnableI2c(false);
                getManager().i2cReadRequest(0x13, mAddress);
                assertThat(getCapturedError()).isEqualTo(KonashiErrorReason.NOT_ENABLED_I2C);
            }

            @Test
            public void withDataThatIsTooLong() {
                stubIsEnableAccessKonashi(true);
                stubIsEnableI2c(true);
                getManager().i2cReadRequest(0xff, mAddress);
                assertThat(getCapturedError()).isEqualTo(KonashiErrorReason.INVALID_PARAMETER);
            }

            @Test
            public void withValidArguments() {
                stubIsEnableAccessKonashi(true);
                stubIsEnableI2c(true);
                getManager().i2cReadRequest(0x13, mAddress);
                assertThat(getCapturedWrittenUuid()).isEqualTo(KonashiUUID.I2C_READ_PARAM_UUID);
                assertThat(getCapturedWrittenValue()).isEqualTo(new byte[] {0x13, 0x3f});
            }
        }

        @RunWith(AndroidJUnit4.class)
        public static class I2cReadTest extends BaseTest {
            @Before
            public void setUp() throws Exception {
                super.setUp();
                // FIXME: 壊れやすそう
                String data = "test";
                Whitebox.setInternalState(getManager(), "mI2cReadDataLength", data.length());
                Whitebox.setInternalState(getManager(), "mI2cReadData", data.getBytes());
            }

            @Test
            public void whenKonashiIsNotEnable() {
                stubIsEnableAccessKonashi(false);
                assertThat(getManager().i2cRead(0x13)).isNull();
                assertThat(getCapturedError()).isEqualTo(KonashiErrorReason.NOT_READY);
            }

            @Test
            public void whenI2cIsNotEnable() {
                stubIsEnableAccessKonashi(true);
                stubIsEnableI2c(false);
                assertThat(getManager().i2cRead(0x13)).isNull();
                assertThat(getCapturedError()).isEqualTo(KonashiErrorReason.NOT_ENABLED_I2C);
            }

            @Test
            public void withTooLargeLength() {
                stubIsEnableAccessKonashi(true);
                stubIsEnableI2c(true);
                assertThat(getManager().i2cRead(0xff)).isNull();
                assertThat(getCapturedError()).isEqualTo(KonashiErrorReason.INVALID_PARAMETER);
            }

            @Test
            public void withInvalidLength() {
                stubIsEnableAccessKonashi(true);
                stubIsEnableI2c(true);
                assertThat(getManager().i2cRead(0x01)).isNull();
                assertThat(getCapturedError()).isEqualTo(KonashiErrorReason.INVALID_PARAMETER);
            }

            @Test
            public void withValidLength() {
                stubIsEnableAccessKonashi(true);
                stubIsEnableI2c(true);
                assertThat(getManager().i2cRead(0x04)).isEqualTo(new byte[] {0x74, 0x65, 0x73, 0x74});
            }
        }
    }
}
