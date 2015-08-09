package com.uxxu.konashi.lib;

import android.support.test.runner.AndroidJUnit4;

import com.uxxu.konashi.lib.entities.KonashiWriteMessage;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Created by izumin on 7/31/15.
 */
@RunWith(Enclosed.class)
public class KonashiManagerTest {
    public static final String TAG = KonashiManagerTest.class.getSimpleName();

    abstract public static class BaseTest {
        private KonashiManager mManager;

        @Before
        public void setUp() throws Exception {
            mManager = Mockito.spy(new KonashiManager());
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

        protected ArgumentCaptor<KonashiWriteMessage> getKonashiWriteMessageCaptor() {
            ArgumentCaptor<KonashiWriteMessage> captor = ArgumentCaptor.forClass(KonashiWriteMessage.class);
            Mockito.doNothing().when(mManager).addWriteMessage(captor.capture());
            return captor;
        }

        protected ArgumentCaptor<KonashiErrorReason> getNotifyKonashiErrorCaptor() {
            ArgumentCaptor<KonashiErrorReason> captor = ArgumentCaptor.forClass(KonashiErrorReason.class);
            Mockito.doNothing().when(mManager).notifyKonashiError(captor.capture());
            return captor;
        }
    }

    @RunWith(Enclosed.class)
    public static class I2cTest {

        @RunWith(AndroidJUnit4.class)
        public static class I2cModeTest extends BaseTest {
            @Test
            public void withValidMode() {
                ArgumentCaptor<KonashiWriteMessage> msgCaptor = getKonashiWriteMessageCaptor();
                stubIsEnableAccessKonashi(true);
                getManager().i2cMode(Konashi.I2C_ENABLE);
                Mockito.verify(getManager(), Mockito.times(1))
                        .addWriteMessage(org.mockito.Matchers.any(KonashiWriteMessage.class));
                assertThat(msgCaptor.getValue().getCharacteristicUuid()).isEqualTo(KonashiUUID.I2C_CONFIG_UUID);
                assertThat(msgCaptor.getValue().getData()).isEqualTo(new byte[] {1});
            }

            @Test
            public void withInValidMode() {
                ArgumentCaptor<KonashiErrorReason> errorCaptor = getNotifyKonashiErrorCaptor();
                stubIsEnableAccessKonashi(true);
                getManager().i2cMode(9999);
                Mockito.verify(getManager(), Mockito.times(1))
                        .notifyKonashiError(org.mockito.Matchers.any(KonashiErrorReason.class));
                assertThat(errorCaptor.getValue()).isEqualTo(KonashiErrorReason.INVALID_PARAMETER);
            }

            @Test
            public void whenKonashiIsNotEnable() {
                ArgumentCaptor<KonashiErrorReason> errorCaptor = getNotifyKonashiErrorCaptor();
                stubIsEnableAccessKonashi(false);
                getManager().i2cMode(9999);
                Mockito.verify(getManager(), Mockito.times(1))
                        .notifyKonashiError(org.mockito.Matchers.any(KonashiErrorReason.class));
                assertThat(errorCaptor.getValue()).isEqualTo(KonashiErrorReason.NOT_READY);
            }
        }

        @RunWith(AndroidJUnit4.class)
        public static class I2cStartConditionTest extends BaseTest {
            @Test
            public void whenKonashiIsNotEnable() {
                ArgumentCaptor<KonashiErrorReason> errorCaptor = getNotifyKonashiErrorCaptor();
                stubIsEnableAccessKonashi(false);
                getManager().i2cStartCondition();
                Mockito.verify(getManager(), Mockito.times(1))
                        .notifyKonashiError(org.mockito.Matchers.any(KonashiErrorReason.class));
                assertThat(errorCaptor.getValue()).isEqualTo(KonashiErrorReason.NOT_READY);
            }

            @Test
            public void whenI2cIsNotEnable() {
                ArgumentCaptor<KonashiErrorReason> errorCaptor = getNotifyKonashiErrorCaptor();
                stubIsEnableAccessKonashi(true);
                stubIsEnableI2c(false);
                getManager().i2cStartCondition();
                Mockito.verify(getManager(), Mockito.times(1))
                        .notifyKonashiError(org.mockito.Matchers.any(KonashiErrorReason.class));
                assertThat(errorCaptor.getValue()).isEqualTo(KonashiErrorReason.NOT_ENABLED_I2C);
            }

            @Test
            public void whenKonashiAndI2CIsEnable() {
                ArgumentCaptor<KonashiWriteMessage> msgCaptor = getKonashiWriteMessageCaptor();
                stubIsEnableAccessKonashi(true);
                stubIsEnableI2c(true);
                getManager().i2cStartCondition();
                Mockito.verify(getManager(), Mockito.times(1))
                        .addWriteMessage(org.mockito.Matchers.any(KonashiWriteMessage.class));
                assertThat(msgCaptor.getValue().getCharacteristicUuid()).isEqualTo(KonashiUUID.I2C_START_STOP_UUID);
                assertThat(msgCaptor.getValue().getData()).isEqualTo(new byte[]{Konashi.I2C_START_CONDITION});
            }
        }

        @RunWith(AndroidJUnit4.class)
        public static class I2cRestartConditionTest extends BaseTest {
            @Test
            public void whenKonashiIsNotEnable() {
                ArgumentCaptor<KonashiErrorReason> errorCaptor = getNotifyKonashiErrorCaptor();
                stubIsEnableAccessKonashi(false);
                getManager().i2cRestartCondition();
                Mockito.verify(getManager(), Mockito.times(1))
                        .notifyKonashiError(org.mockito.Matchers.any(KonashiErrorReason.class));
                assertThat(errorCaptor.getValue()).isEqualTo(KonashiErrorReason.NOT_READY);
            }

            @Test
            public void whenI2cIsNotEnable() {
                ArgumentCaptor<KonashiErrorReason> errorCaptor = getNotifyKonashiErrorCaptor();
                stubIsEnableAccessKonashi(true);
                stubIsEnableI2c(false);
                getManager().i2cRestartCondition();
                Mockito.verify(getManager(), Mockito.times(1))
                        .notifyKonashiError(org.mockito.Matchers.any(KonashiErrorReason.class));
                assertThat(errorCaptor.getValue()).isEqualTo(KonashiErrorReason.NOT_ENABLED_I2C);
            }

            @Test
            public void whenKonashiAndI2CIsEnable() {
                ArgumentCaptor<KonashiWriteMessage> msgCaptor = getKonashiWriteMessageCaptor();
                stubIsEnableAccessKonashi(true);
                stubIsEnableI2c(true);
                getManager().i2cRestartCondition();
                Mockito.verify(getManager(), Mockito.times(1))
                        .addWriteMessage(org.mockito.Matchers.any(KonashiWriteMessage.class));
                assertThat(msgCaptor.getValue().getCharacteristicUuid()).isEqualTo(KonashiUUID.I2C_START_STOP_UUID);
                assertThat(msgCaptor.getValue().getData()).isEqualTo(new byte[] {Konashi.I2C_RESTART_CONDITION});
            }
        }

        @RunWith(AndroidJUnit4.class)
        public static class I2cStopConditionTest extends BaseTest {
            @Test
            public void whenKonashiIsNotEnable() {
                ArgumentCaptor<KonashiErrorReason> errorCaptor = getNotifyKonashiErrorCaptor();
                stubIsEnableAccessKonashi(false);
                getManager().i2cStopCondition();
                Mockito.verify(getManager(), Mockito.times(1))
                        .notifyKonashiError(org.mockito.Matchers.any(KonashiErrorReason.class));
                assertThat(errorCaptor.getValue()).isEqualTo(KonashiErrorReason.NOT_READY);
            }

            @Test
            public void whenI2cIsNotEnable() {
                ArgumentCaptor<KonashiErrorReason> errorCaptor = getNotifyKonashiErrorCaptor();
                stubIsEnableAccessKonashi(true);
                stubIsEnableI2c(false);
                getManager().i2cStopCondition();
                Mockito.verify(getManager(), Mockito.times(1))
                        .notifyKonashiError(org.mockito.Matchers.any(KonashiErrorReason.class));
                assertThat(errorCaptor.getValue()).isEqualTo(KonashiErrorReason.NOT_ENABLED_I2C);
            }

            @Test
            public void whenKonashiAndI2CIsEnable() {
                ArgumentCaptor<KonashiWriteMessage> msgCaptor = getKonashiWriteMessageCaptor();
                stubIsEnableAccessKonashi(true);
                stubIsEnableI2c(true);
                getManager().i2cStopCondition();
                Mockito.verify(getManager(), Mockito.times(1))
                        .addWriteMessage(org.mockito.Matchers.any(KonashiWriteMessage.class));
                assertThat(msgCaptor.getValue().getCharacteristicUuid()).isEqualTo(KonashiUUID.I2C_START_STOP_UUID);
                assertThat(msgCaptor.getValue().getData()).isEqualTo(new byte[] {Konashi.I2C_STOP_CONDITION});
            }
        }

        @RunWith(AndroidJUnit4.class)
        public static class I2cWriteTest extends BaseTest {
            private String mData = "test";
            private byte mAddress = 0x1f;

            @Test
            public void whenKonashiIsNotEnable() {
                ArgumentCaptor<KonashiErrorReason> errorCaptor = getNotifyKonashiErrorCaptor();
                stubIsEnableAccessKonashi(false);
                getManager().i2cWrite(mData.length(), mData.getBytes(), mAddress);
                Mockito.verify(getManager(), Mockito.times(1))
                        .notifyKonashiError(org.mockito.Matchers.any(KonashiErrorReason.class));
                assertThat(errorCaptor.getValue()).isEqualTo(KonashiErrorReason.NOT_READY);
            }

            @Test
            public void whenI2cIsNotEnable() {
                ArgumentCaptor<KonashiErrorReason> errorCaptor = getNotifyKonashiErrorCaptor();
                stubIsEnableAccessKonashi(true);
                stubIsEnableI2c(false);
                getManager().i2cWrite(mData.length(), mData.getBytes(), mAddress);
                Mockito.verify(getManager(), Mockito.times(1))
                        .notifyKonashiError(org.mockito.Matchers.any(KonashiErrorReason.class));
                assertThat(errorCaptor.getValue()).isEqualTo(KonashiErrorReason.NOT_ENABLED_I2C);
            }

            @Test
            public void withDataThatIsTooLong() {
                ArgumentCaptor<KonashiErrorReason> errorCaptor = getNotifyKonashiErrorCaptor();
                stubIsEnableAccessKonashi(true);
                stubIsEnableI2c(true);
                String invalidData = "too long data string";
                getManager().i2cWrite(invalidData.length(), invalidData.getBytes(), mAddress);
                Mockito.verify(getManager(), Mockito.times(1))
                        .notifyKonashiError(org.mockito.Matchers.any(KonashiErrorReason.class));
                assertThat(errorCaptor.getValue()).isEqualTo(KonashiErrorReason.INVALID_PARAMETER);
            }

            @Test
            public void withValidArguments() {
                ArgumentCaptor<KonashiWriteMessage> msgCaptor = getKonashiWriteMessageCaptor();
                stubIsEnableAccessKonashi(true);
                stubIsEnableI2c(true);
                getManager().i2cWrite(mData.length(), mData.getBytes(), mAddress);
                Mockito.verify(getManager(), Mockito.times(1))
                        .addWriteMessage(org.mockito.Matchers.any(KonashiWriteMessage.class));
                assertThat(msgCaptor.getValue().getCharacteristicUuid()).isEqualTo(KonashiUUID.I2C_WRITE_UUID);
                byte[] value = new byte[] {
                        0x05, 0x3E, 0x74, 0x65, 0x73, 0x74, 0x00, 0x00, 0x00, 0x00,
                        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
                };
                assertThat(msgCaptor.getValue().getData()).isEqualTo(value);
            }
        }

        @RunWith(AndroidJUnit4.class)
        public static class I2cReadRequestTest extends BaseTest {
            private byte mAddress = 0x1f;

            @Test
            public void whenKonashiIsNotEnable() {
                ArgumentCaptor<KonashiErrorReason> errorCaptor = getNotifyKonashiErrorCaptor();
                stubIsEnableAccessKonashi(false);
                getManager().i2cReadRequest(0x13, mAddress);
                Mockito.verify(getManager(), Mockito.times(1))
                        .notifyKonashiError(org.mockito.Matchers.any(KonashiErrorReason.class));
                assertThat(errorCaptor.getValue()).isEqualTo(KonashiErrorReason.NOT_READY);
            }

            @Test
            public void whenI2cIsNotEnable() {
                ArgumentCaptor<KonashiErrorReason> errorCaptor = getNotifyKonashiErrorCaptor();
                stubIsEnableAccessKonashi(true);
                stubIsEnableI2c(false);
                getManager().i2cReadRequest(0x13, mAddress);
                Mockito.verify(getManager(), Mockito.times(1))
                        .notifyKonashiError(org.mockito.Matchers.any(KonashiErrorReason.class));
                assertThat(errorCaptor.getValue()).isEqualTo(KonashiErrorReason.NOT_ENABLED_I2C);
            }

            @Test
            public void withDataThatIsTooLong() {
                ArgumentCaptor<KonashiErrorReason> errorCaptor = getNotifyKonashiErrorCaptor();
                stubIsEnableAccessKonashi(true);
                stubIsEnableI2c(true);
                getManager().i2cReadRequest(0xff, mAddress);
                Mockito.verify(getManager(), Mockito.times(1))
                        .notifyKonashiError(org.mockito.Matchers.any(KonashiErrorReason.class));
                assertThat(errorCaptor.getValue()).isEqualTo(KonashiErrorReason.INVALID_PARAMETER);
            }

            @Test
            public void withValidArguments() {
                ArgumentCaptor<KonashiWriteMessage> msgCaptor = getKonashiWriteMessageCaptor();
                stubIsEnableAccessKonashi(true);
                stubIsEnableI2c(true);
                getManager().i2cReadRequest(0x13, mAddress);
                Mockito.verify(getManager(), Mockito.times(1))
                        .addWriteMessage(org.mockito.Matchers.any(KonashiWriteMessage.class));
                assertThat(msgCaptor.getValue().getCharacteristicUuid()).isEqualTo(KonashiUUID.I2C_READ_PARAM_UUID);
                assertThat(msgCaptor.getValue().getData()).isEqualTo(new byte[] {0x13, 0x3f});
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
                ArgumentCaptor<KonashiErrorReason> errorCaptor = getNotifyKonashiErrorCaptor();
                stubIsEnableAccessKonashi(false);
                assertThat(getManager().i2cRead(0x13)).isNull();
                Mockito.verify(getManager(), Mockito.times(1))
                        .notifyKonashiError(org.mockito.Matchers.any(KonashiErrorReason.class));
                assertThat(errorCaptor.getValue()).isEqualTo(KonashiErrorReason.NOT_READY);
            }

            @Test
            public void whenI2cIsNotEnable() {
                ArgumentCaptor<KonashiErrorReason> errorCaptor = getNotifyKonashiErrorCaptor();
                stubIsEnableAccessKonashi(true);
                stubIsEnableI2c(false);
                assertThat(getManager().i2cRead(0x13)).isNull();
                Mockito.verify(getManager(), Mockito.times(1))
                        .notifyKonashiError(org.mockito.Matchers.any(KonashiErrorReason.class));
                assertThat(errorCaptor.getValue()).isEqualTo(KonashiErrorReason.NOT_ENABLED_I2C);
            }

            @Test
            public void withTooLargeLength() {
                ArgumentCaptor<KonashiErrorReason> errorCaptor = getNotifyKonashiErrorCaptor();
                stubIsEnableAccessKonashi(true);
                stubIsEnableI2c(true);
                assertThat(getManager().i2cRead(0xff)).isNull();
                Mockito.verify(getManager(), Mockito.times(1))
                        .notifyKonashiError(org.mockito.Matchers.any(KonashiErrorReason.class));
                assertThat(errorCaptor.getValue()).isEqualTo(KonashiErrorReason.INVALID_PARAMETER);
            }

            @Test
            public void withInvalidLength() {
                ArgumentCaptor<KonashiErrorReason> errorCaptor = getNotifyKonashiErrorCaptor();
                stubIsEnableAccessKonashi(true);
                stubIsEnableI2c(true);
                assertThat(getManager().i2cRead(0x01)).isNull();
                Mockito.verify(getManager(), Mockito.times(1))
                        .notifyKonashiError(org.mockito.Matchers.any(KonashiErrorReason.class));
                assertThat(errorCaptor.getValue()).isEqualTo(KonashiErrorReason.INVALID_PARAMETER);
            }


            @Test
            public void withValidLength() {
                ArgumentCaptor<KonashiWriteMessage> msgCaptor = getKonashiWriteMessageCaptor();
                stubIsEnableAccessKonashi(true);
                stubIsEnableI2c(true);
                assertThat(getManager().i2cRead(0x04)).isEqualTo(new byte[] {0x74, 0x65, 0x73, 0x74});
            }
        }
    }
}
