package com.uxxu.konashi.lib.action;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

import com.uxxu.konashi.lib.Konashi;

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
 * Created by e10dokup on 2015/09/20
 **/
public class UartBaudrateActionTest {
    private static final String TAG = UartBaudrateActionTest.class.getSimpleName();
    private final UartBaudrateActionTest self = this;

    @Mock private BluetoothGattService mService;
    @Mock private BluetoothGattCharacteristic mCharacteristic;

    private UartBaudrateAction mAction;

    private ArgumentCaptor<byte[]> mValueCaptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mValueCaptor = ArgumentCaptor.forClass(byte[].class);
        when(mService.getCharacteristic(any(UUID.class))).thenReturn(mCharacteristic);
    }

    @Test
    public void hasValidParams_ForValidValud() throws Exception {
        mAction = new UartBaudrateAction(mService, Konashi.UART_RATE_9K6);
        assertThat(mAction.hasValidParams()).isTrue();
    }

    @Test
    public void hasValidParams_FotInvalidValue() throws Exception {
        mAction = new UartBaudrateAction(mService, 0x00);
        assertThat(mAction.hasValidParams()).isFalse();
    }

    @Test
    public void setValue() throws Exception {
        mAction = new UartBaudrateAction(mService, Konashi.UART_RATE_9K6);
        mAction.setValue();
        verify(mCharacteristic, times(1)).setValue(mValueCaptor.capture());
        assertThat(mValueCaptor.getValue()[0]).isEqualTo((byte) Konashi.UART_RATE_9K6);
    }
}