package com.uxxu.konashi.lib;

import android.bluetooth.BluetoothGattCharacteristic;

import com.uxxu.konashi.lib.events.KonashiAnalogEvent;
import com.uxxu.konashi.lib.events.KonashiDeviceInfoEvent;
import com.uxxu.konashi.lib.events.KonashiDigitalEvent;
import com.uxxu.konashi.lib.events.KonashiEvent;
import com.uxxu.konashi.lib.events.KonashiUartEvent;

import java.util.UUID;

/**
 * Created by izumin on 8/4/15.
 */
public enum KonashiCharacteristic {
    ANALOG_READ0(KonashiUUID.ANALOG_READ0_UUID, KonashiAnalogEvent.UPDATE_ANALOG_VALUE_AIO0) {
        @Override
        public void handle(BluetoothGattCharacteristic characteristic, KonashiNotifier notifier) {
            int value = KonashiUtils.getAnalogValue(Konashi.AIO0, characteristic);
            notifyKonashiEvent(notifier, value, null);
        }
    },
    ANALOG_READ1(KonashiUUID.ANALOG_READ1_UUID, KonashiAnalogEvent.UPDATE_ANALOG_VALUE_AIO1) {
        @Override
        public void handle(BluetoothGattCharacteristic characteristic, KonashiNotifier notifier) {
            int value = KonashiUtils.getAnalogValue(Konashi.AIO1, characteristic);
            notifyKonashiEvent(notifier, value, null);
        }
    },
    ANALOG_READ2(KonashiUUID.ANALOG_READ2_UUID, KonashiAnalogEvent.UPDATE_ANALOG_VALUE_AIO2) {
        @Override
        public void handle(BluetoothGattCharacteristic characteristic, KonashiNotifier notifier) {
            int value = KonashiUtils.getAnalogValue(Konashi.AIO2, characteristic);
            notifyKonashiEvent(notifier, value, null);
        }
    },
    BATTERY_LEVEL(KonashiUUID.BATTERY_LEVEL_UUID, KonashiDeviceInfoEvent.UPDATE_BATTERY_LEVEL) {
        @Override
        public void handle(BluetoothGattCharacteristic characteristic, KonashiNotifier notifier) {
            int value = KonashiUtils.getBatteryLevel(characteristic);
            notifyKonashiEvent(notifier, value, null);
        }
    },
    PIO_INPUT(KonashiUUID.PIO_INPUT_NOTIFICATION_UUID, KonashiDigitalEvent.UPDATE_PIO_INPUT) {
        @Override
        public void handle(BluetoothGattCharacteristic characteristic, KonashiNotifier notifier) {
            notifyKonashiEvent(notifier, characteristic.getValue()[0], null);
        }
    },
    UART_RX(KonashiUUID.UART_RX_NOTIFICATION_UUID, KonashiUartEvent.UART_RX_COMPLETE) {
        @Override
        public void handle(BluetoothGattCharacteristic characteristic, KonashiNotifier notifier) {
            notifyKonashiEvent(notifier, characteristic.getValue(), null);
        }
    },
    UNKNOWN(null, null) {
        @Override
        public void handle(BluetoothGattCharacteristic characteristic, KonashiNotifier notifier) {
            // Do nothing...
        }
    };

    private UUID mUuid;
    private KonashiEvent mEvent;

    KonashiCharacteristic(UUID uuid, KonashiEvent event) {
        mUuid = uuid;
        mEvent = event;
    }

    public static KonashiCharacteristic valueOf(UUID uuid) {
        if (uuid != null) {
            for (KonashiCharacteristic handler : values()) {
                if (uuid.equals(handler.getUuid())) {
                    return handler;
                }
            }
        }
        return UNKNOWN;
    }

    protected void notifyKonashiEvent(KonashiNotifier notifier, Object param1, Object param2) {
        notifier.notifyKonashiEvent(mEvent, param1, param2);
    }

    public UUID getUuid() {
        return mUuid;
    }

    public KonashiEvent getKonashiEvent() {
        return mEvent;
    }

    abstract public void handle(BluetoothGattCharacteristic characteristic, KonashiNotifier notifier);
}
