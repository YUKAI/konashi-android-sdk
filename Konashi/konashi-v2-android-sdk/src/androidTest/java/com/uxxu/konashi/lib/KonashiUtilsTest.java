package com.uxxu.konashi.lib;

import android.bluetooth.BluetoothGattCharacteristic;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by izumin on 8/4/15.
 */
@RunWith(AndroidJUnit4.class)
public class KonashiUtilsTest {
    public static final String TAG = KonashiUtilsTest.class.getSimpleName();

    @Mock private BluetoothGattCharacteristic mCharacteristic;
    @Rule public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAnaLogValueOnAIO0() {
        byte[] values = new byte[]{0x03, 0x48};
        Mockito.when(mCharacteristic.getValue()).thenReturn(values);
        assertThat(KonashiUtils.getAnalogValue(Konashi.AIO0, mCharacteristic)).isEqualTo(840);
    }

    @Test
    public void testGetAnaLogValueOnAIO1() {
        byte[] values = new byte[]{0x03, 0x48};
        Mockito.when(mCharacteristic.getValue()).thenReturn(values);
        assertThat(KonashiUtils.getAnalogValue(Konashi.AIO1, mCharacteristic)).isEqualTo(840);
    }

    @Test
    public void testGetAnaLogValueOnAIO2() {
        byte[] values = new byte[]{0x03, 0x48};
        Mockito.when(mCharacteristic.getValue()).thenReturn(values);
        assertThat(KonashiUtils.getAnalogValue(Konashi.AIO2, mCharacteristic)).isEqualTo(840);
    }

    @Test
    public void testGetAnaLogValueOnUnknownAIO() {
        byte[] values = new byte[]{0x03, 0x48};
        Mockito.when(mCharacteristic.getValue()).thenReturn(values);
        thrown.expect(IllegalArgumentException.class);
        KonashiUtils.getAnalogValue(9999, mCharacteristic);
    }

    @Test
    public void testGetBatteryLevel() {
        byte[] values = new byte[]{0x55};
        Mockito.when(mCharacteristic.getValue()).thenReturn(values);
        assertThat(KonashiUtils.getBatteryLevel(mCharacteristic)).isEqualTo(85);
    }

    @Test
    public void testGetPwmPeriod() {
        byte[] values = new byte[]{0x01, 0x00, 0x00, 0x27, 0x10};
        Mockito.when(mCharacteristic.getValue()).thenReturn(values);
        assertThat(KonashiUtils.getPwmPeriod(mCharacteristic), is(10000));
    }

    @Test
    public void testGetPwmDuty() {
        byte[] values = new byte[]{0x01, 0x00, 0x00, 0x27, 0x10};
        Mockito.when(mCharacteristic.getValue()).thenReturn(values);
        assertThat(KonashiUtils.getPwmDuty(mCharacteristic), is(10000));
    }

    @Test
    public void testGetUartBaudrate() {
        byte[] values = new byte[] {0x00, 0x28};
        Mockito.when(mCharacteristic.getValue()).thenReturn(values);
        assertThat(KonashiUtils.getUartBaudrate(mCharacteristic), is(0x0028));
    }

    @Test
    public void testGetUartWriteData() {
        byte[] values = new byte[] {4, 0x74, 0x65, 0x73, 0x74};
        Mockito.when(mCharacteristic.getValue()).thenReturn(values);
        assertThat(KonashiUtils.getUartWriteData(mCharacteristic), is(new byte[]{0x74, 0x65, 0x73, 0x74}));
    }

    @Test
    public void testGetI2cWriteAddress() {
        byte[] values = new byte[] {5, 0x3e, 0x74, 0x65, 0x73, 0x74};
        Mockito.when(mCharacteristic.getValue()).thenReturn(values);
        assertThat(KonashiUtils.getI2cWriteAddress(mCharacteristic), is((byte) 0x1f));
    }

    @Test
    public void testGetI2cWriteData() {
        byte[] values = new byte[] {5, 0x3e, 0x74, 0x65, 0x73, 0x74};
        Mockito.when(mCharacteristic.getValue()).thenReturn(values);
        assertThat(KonashiUtils.getI2cWriteData(mCharacteristic), is(new byte[]{0x74, 0x65, 0x73, 0x74}));
    }
}
