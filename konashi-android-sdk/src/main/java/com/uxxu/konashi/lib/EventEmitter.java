package com.uxxu.konashi.lib;

import java.util.ArrayList;

import info.izumin.android.bletia.BletiaException;

/**
 * Created by izumin on 9/23/15.
 */
class EventEmitter extends ArrayList<KonashiListener> {
    public void emitConnect(KonashiManager manager) {
        for (KonashiListener listener : this) {
            listener.onConnect(manager);
        }
    }

    public void emitDisconnect(KonashiManager manager) {
        for (KonashiListener listener : this) {
            listener.onDisconnect(manager);
        }
    }

    public void emitError(KonashiManager manager, BletiaException e) {
        for (KonashiListener listener : this) {
            listener.onError(manager, e);
        }
    }

    public void emitUpdatePioOutput(KonashiManager manager, int value) {
        for (KonashiListener listener : this) {
            listener.onUpdatePioOutput(manager, value);
        }
    }

    public void emitUpdateUartRx(KonashiManager manager, byte[] value) {
        for (KonashiListener listener : this) {
            listener.onUpdateUartRx(manager, value);
        }
    }

    public void emitUpdateBatteryLevel(KonashiManager manager, int level) {
        for (KonashiListener listener : this) {
            listener.onUpdateBatteryLevel(manager, level);
        }
    }
}
