package com.uxxu.konashi.lib.dispatcher;

import com.uxxu.konashi.lib.KonashiUUID;
import com.uxxu.konashi.lib.store.PwmStore;
import com.uxxu.konashi.lib.util.PwmUtils;

import java.util.UUID;

/**
 * Created by izumin on 9/18/15.
 */
public enum PwmStoreUpdater implements CharacteristicDispatcher.Updater<PwmStore> {
    MODE(KonashiUUID.PWM_CONFIG_UUID) {
        @Override
        public void update(PwmStore store, byte[] value) {
            store.setModes(value[0]);
        }
    },
    PERIOD(KonashiUUID.PWM_PARAM_UUID) {
        @Override
        public void update(PwmStore store, byte[] value) {
            int pin = value[0];
            int period = PwmUtils.getPwmPeriod(value);
            store.setPeriod(pin, period);
        }
    },
    DUTY(KonashiUUID.PWM_DUTY_UUID) {
        @Override
        public void update(PwmStore store, byte[] value) {
            int pin = value[0];
            int duty = PwmUtils.getPwmDuty(value);
            store.setDuty(pin, duty);
        }
    };

    private final UUID mUuid;

    PwmStoreUpdater(UUID uuid) {
        mUuid = uuid;
    }

    public UUID getUuid() {
        return mUuid;
    }
}
