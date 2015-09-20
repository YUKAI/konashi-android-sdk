package com.uxxu.konashi.lib.dispatcher;

import com.uxxu.konashi.lib.Konashi;
import com.uxxu.konashi.lib.KonashiUUID;
import com.uxxu.konashi.lib.stores.UartStore;

import java.util.UUID;

/**
 * Created by e10dokup on 9/20/15
 */
public enum UartStoreUpdater implements CharacteristicDispatcher.Updater<UartStore> {
    BAUDRATE(KonashiUUID.UART_BAUDRATE_UUID) {
        @Override
        public void update(UartStore store, byte[] value) {
        }
    },
    CONFIG(KonashiUUID.UART_CONFIG_UUID) {
        @Override
        public void update(UartStore store, byte[] value) {

        }
    },
    RX_NOTIFICATION(KonashiUUID.UART_RX_NOTIFICATION_UUID) {
        @Override
        public void update(UartStore store, byte[] value) {

        }
    },
    TX(KonashiUUID.UART_TX_UUID) {
        @Override
        public void update(UartStore store, byte[] value) {

        }
    };

    private final UUID mUuid;

    UartStoreUpdater(UUID uuid) {
        mUuid = uuid;
    }

    public UUID getUuid() {
        return mUuid;
    }
}
