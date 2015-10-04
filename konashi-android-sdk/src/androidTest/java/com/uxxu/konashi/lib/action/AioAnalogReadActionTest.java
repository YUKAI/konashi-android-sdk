package com.uxxu.konashi.lib.action;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

import com.uxxu.konashi.lib.Konashi;
import com.uxxu.konashi.lib.KonashiUUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Method;
import java.util.UUID;

import info.izumin.android.bletia.BletiaException;
import info.izumin.android.bletia.wrapper.BluetoothGattWrapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by e10dokup on 2015/10/04
 **/
public class AioAnalogReadActionTest {

    @Mock private BluetoothGattService mService;
    @Mock private BluetoothGattCharacteristic mCharacteristic;
    @Mock private BluetoothGattWrapper mGattWrapper;

    private AioAnalogReadAction mAction;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(mService.getCharacteristic(KonashiUUID.ANALOG_READ0_UUID)).thenReturn(mCharacteristic);
    }

    @Test
    public void actionCharacteristic_InvalidPinNumber() throws Exception{
        mAction = new AioAnalogReadAction(mService, 5);
        assertThat(mAction.getCharacteristic() == null).isEqualTo(true);
    }

    @Test
    public void actionCharacteristic_ValidPinNumber() throws Exception{
        mAction = new AioAnalogReadAction(mService, Konashi.AIO0);
        assertThat(mAction.getCharacteristic() == null).isEqualTo(false);
    }
}