package com.uxxu.konashi.lib.action;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

import com.uxxu.konashi.lib.Konashi;

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
import static org.mockito.Matchers.isNull;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;

/**
 * Created by e10dokup on 2015/10/04
 **/
public class AioAnalogReadActionTest {

    @Mock private BluetoothGattService mService;
    @Mock private BluetoothGattWrapper mGattWrapper;

    private AioAnalogReadAction mAction;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void actionCharacteristic_InvalidPinNumber() throws Exception{
        mAction = new AioAnalogReadAction(mService, 5);
        assertThat(mAction.getCharacteristic()).isNull();
    }

    @Test
    public void actionCharacteristic_ValidPinNumber() throws Exception{
        mAction = new AioAnalogReadAction(mService, Konashi.AIO0);
        assertThat(mAction.getCharacteristic()).isEqualTo(notNull());
    }
}