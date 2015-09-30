package com.uxxu.konashi.lib.action;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

import com.uxxu.konashi.lib.store.I2cStore;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by izumin on 9/21/15.
 */
public class I2cWriteActionTest {

    @Mock private BluetoothGattService mService;
    @Mock private BluetoothGattCharacteristic mCharacteristic;
    @Mock private I2cStore mStore;

    private I2cWriteAction mAction;
    private byte[] mData = new byte[] {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09};

    private ArgumentCaptor<byte[]> mValueCaptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mValueCaptor = ArgumentCaptor.forClass(byte[].class);
        when(mService.getCharacteristic(any(UUID.class))).thenReturn(mCharacteristic);
        when(mStore.isEnabled()).thenReturn(true);
    }

    @Test
    public void hasValidParams_WithOverLength() throws Exception {
        mAction = new I2cWriteAction(mService, (byte) 0xa6, new byte[]{
                0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09,
                0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10, 0x11, 0x12, 0x13, 0x14
        }, mStore);
        assertThat(mAction.hasValidParams()).isFalse();
    }

    @Test
    public void hasValidParams_WithValidParams() throws Exception {
        mAction = new I2cWriteAction(mService, (byte) 0xa6, mData, mStore);
        assertThat(mAction.hasValidParams()).isTrue();
    }

    @Test
    public void setValue_WithValidParams() throws Exception {
        mAction = new I2cWriteAction(mService, (byte) 0x53 , mData, mStore);
        mAction.setValue();
        verify(mCharacteristic, times(1)).setValue(mValueCaptor.capture());
        assertThat(mValueCaptor.getValue()[0]).isEqualTo((byte) 0x0a);
        assertThat(mValueCaptor.getValue()[1]).isEqualTo((byte) 0xa6);
        for (int i = 0; i < mData.length; i++) {
            assertThat(mValueCaptor.getValue()[i+2]).isEqualTo(mData[i]);
        }
    }
}

