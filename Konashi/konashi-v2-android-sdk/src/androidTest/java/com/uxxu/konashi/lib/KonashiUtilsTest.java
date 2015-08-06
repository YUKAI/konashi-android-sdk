package com.uxxu.konashi.lib;

import android.bluetooth.BluetoothGattCharacteristic;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by izumin on 8/4/15.
 */
@RunWith(Enclosed.class)
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
        assertThat(KonashiUtils.getAnalogValue(Konashi.AIO0, mCharacteristic), is(840));
    }

    @Test
    public void testGetAnaLogValueOnAIO1() {
        byte[] values = new byte[]{0x03, 0x48};
        Mockito.when(mCharacteristic.getValue()).thenReturn(values);
        assertThat(KonashiUtils.getAnalogValue(Konashi.AIO1, mCharacteristic), is(840));
    }

    @Test
    public void testGetAnaLogValueOnAIO2() {
        byte[] values = new byte[]{0x03, 0x48};
        Mockito.when(mCharacteristic.getValue()).thenReturn(values);
        assertThat(KonashiUtils.getAnalogValue(Konashi.AIO2, mCharacteristic), is(840));
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
        assertThat(KonashiUtils.getBatteryLevel(mCharacteristic), is(85));
    }
}
