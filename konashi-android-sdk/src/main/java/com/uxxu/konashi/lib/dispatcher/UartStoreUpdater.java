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
            store.setUartBaudrate(value);
        }
    },
    CONFIG(KonashiUUID.UART_CONFIG_UUID) {
        @Override
        public void update(UartStore store, byte[] value) {
            store.setUartSetting(value[0]);
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
