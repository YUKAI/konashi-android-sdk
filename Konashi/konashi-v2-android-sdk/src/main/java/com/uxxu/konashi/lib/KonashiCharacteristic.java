package com.uxxu.konashi.lib;

import android.bluetooth.BluetoothGattCharacteristic;
import android.util.Log;

import com.uxxu.konashi.lib.events.KonashiAnalogEvent;
import com.uxxu.konashi.lib.events.KonashiDeviceInfoEvent;
import com.uxxu.konashi.lib.events.KonashiDigitalEvent;
import com.uxxu.konashi.lib.events.KonashiEvent;
import com.uxxu.konashi.lib.events.KonashiPwmEvent;
import com.uxxu.konashi.lib.events.KonashiUartEvent;

import java.util.UUID;

/**
 * Created by izumin on 8/4/15.
 */
public enum KonashiCharacteristic {

    /* ==== Analog ================================================================ */

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

    /* ==== PIO ================================================================ */

    PIO_SETTING(KonashiUUID.PIO_SETTING_UUID, KonashiDigitalEvent.UPDATE_PIO_SETTING) {
        @Override
        public void handle(BluetoothGattCharacteristic characteristic, KonashiNotifier notifier) {
            notifyKonashiEvent(notifier, characteristic.getValue()[0], null);
        }
    },
    PIO_PULLUP(KonashiUUID.PIO_PULLUP_UUID, KonashiDigitalEvent.UPDATE_PIO_PULLUP) {
        @Override
        public void handle(BluetoothGattCharacteristic characteristic, KonashiNotifier notifier) {
            notifyKonashiEvent(notifier, characteristic.getValue()[0], null);
        }
    },
    PIO_INPUT(KonashiUUID.PIO_INPUT_NOTIFICATION_UUID, KonashiDigitalEvent.UPDATE_PIO_INPUT) {
        @Override
        public void handle(BluetoothGattCharacteristic characteristic, KonashiNotifier notifier) {
            notifyKonashiEvent(notifier, characteristic.getValue()[0], null);
        }
    },

    /* ==== PWM ================================================================ */

    PWM_MODE(KonashiUUID.PWM_CONFIG_UUID, KonashiPwmEvent.UPDATE_PWM_CONFIG) {
        @Override
        public void handle(BluetoothGattCharacteristic characteristic, KonashiNotifier notifier) {
            notifyKonashiEvent(notifier, characteristic.getValue()[0], null);
        }
    },
    PWM_PERIOD(KonashiUUID.PWM_PARAM_UUID, KonashiPwmEvent.UPDATE_PWM_PARAM) {
        @Override
        public void handle(BluetoothGattCharacteristic characteristic, KonashiNotifier notifier) {
            int pin = characteristic.getValue()[0];
            int period = KonashiUtils.getPwmPeriod(characteristic);
            notifyKonashiEvent(notifier, pin, period);
        }
    },
    PWM_DUTY(KonashiUUID.PWM_DUTY_UUID, KonashiPwmEvent.UPDATE_PWM_DUTY) {
        @Override
        public void handle(BluetoothGattCharacteristic characteristic, KonashiNotifier notifier) {
            int pin = characteristic.getValue()[0];
            int duty = KonashiUtils.getPwmDuty(characteristic);
            notifyKonashiEvent(notifier, pin, duty);
        }
    },

    /* ==== UART ================================================================ */

    UART_MODE(KonashiUUID.UART_CONFIG_UUID, KonashiUartEvent.UPDATE_UART_MODE) {
        @Override
        public void handle(BluetoothGattCharacteristic characteristic, KonashiNotifier notifier) {
            notifyKonashiEvent(notifier, characteristic.getValue()[0], null);
        }
    },
    UART_BAUDRATE(KonashiUUID.UART_BAUDRATE_UUID, KonashiUartEvent.UPDATE_UART_BAUDRATE) {
        @Override
        public void handle(BluetoothGattCharacteristic characteristic, KonashiNotifier notifier) {
            int baudrate = KonashiUtils.getUartBaudrate(characteristic);
            notifyKonashiEvent(notifier, baudrate, null);
        }
    },
    UART_WRITE(KonashiUUID.UART_TX_UUID, KonashiUartEvent.WRITE_UART) {
        @Override
        public void handle(BluetoothGattCharacteristic characteristic, KonashiNotifier notifier) {
            byte[] bytes = KonashiUtils.getUartWriteBytes(characteristic);
            notifyKonashiEvent(notifier, bytes, null);
        }
    },
    UART_RX(KonashiUUID.UART_RX_NOTIFICATION_UUID, KonashiUartEvent.UART_RX_COMPLETE) {
        @Override
        public void handle(BluetoothGattCharacteristic characteristic, KonashiNotifier notifier) {
            notifyKonashiEvent(notifier, characteristic.getValue(), null);
        }
    },

    /* ==== Device info ================================================================ */

    BATTERY_LEVEL(KonashiUUID.BATTERY_LEVEL_UUID, KonashiDeviceInfoEvent.UPDATE_BATTERY_LEVEL) {
        @Override
        public void handle(BluetoothGattCharacteristic characteristic, KonashiNotifier notifier) {
            int value = KonashiUtils.getBatteryLevel(characteristic);
            notifyKonashiEvent(notifier, value, null);
        }
    },

    /* ==== UNKNOWN ================================================================ */

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
