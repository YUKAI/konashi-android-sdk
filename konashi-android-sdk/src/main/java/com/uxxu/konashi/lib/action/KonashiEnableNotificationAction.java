package com.uxxu.konashi.lib.action;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.util.Log;


import com.uxxu.konashi.lib.KonashiUUID;

import java.util.UUID;

import info.izumin.android.bletia.BleErrorType;
import info.izumin.android.bletia.BletiaException;
import info.izumin.android.bletia.action.EnableNotificationAction;
import info.izumin.android.bletia.wrapper.BluetoothGattWrapper;

/**
 * Created by e10dokup on 2016/07/21
 **/
public class KonashiEnableNotificationAction extends EnableNotificationAction {
    private static final String TAG = KonashiEnableNotificationAction.class.getSimpleName();
    private final KonashiEnableNotificationAction self = this;

    public KonashiEnableNotificationAction(BluetoothGattService service, UUID uuid, boolean enabled) {
        super(service, uuid, enabled);
    }

    public KonashiEnableNotificationAction(BluetoothGattCharacteristic characteristic, boolean enabled) {
        super(characteristic, enabled);
    }

    @Override
    public Type getType() {
        return super.getType();
    }

    @Override
    public boolean execute(BluetoothGattWrapper gattWrapper) {
        if (gattWrapper.setCharacteristicNotification(getCharacteristic(), getEnabled())) {
            // Konashi 3 already has descriptors. So We must get descriptor from devices.
            BluetoothGattDescriptor descriptor = getCharacteristic().getDescriptor(KonashiUUID.CLIENT_CHARACTERISTIC_CONFIG);
            if (descriptor != null) {
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                gattWrapper.writeDescriptor(descriptor);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean getEnabled() {
        return super.getEnabled();
    }
}
