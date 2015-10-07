package com.uxxu.konashi.lib.util;

import android.bluetooth.BluetoothGattCharacteristic;
import android.support.test.runner.AndroidJUnit4;

import com.uxxu.konashi.lib.Konashi;

import org.assertj.core.api.Assertions;
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
    public void testGetBatteryLevel() {
        byte[] values = new byte[]{0x55};
        Mockito.when(mCharacteristic.getValue()).thenReturn(values);
        Assertions.assertThat(KonashiUtils.getBatteryLevel(mCharacteristic)).isEqualTo(85);
    }

    @Test
    public void testInt2bytes() {
        int value = Konashi.UART_ENABLE;
        assertThat(KonashiUtils.int2bytes(Konashi.UART_ENABLE)[0]).isEqualTo((byte)value);

    }
}
