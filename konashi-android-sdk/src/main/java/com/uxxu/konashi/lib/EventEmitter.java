package com.uxxu.konashi.lib;

import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Handler;
import android.os.Looper;

import org.jdeferred.DoneCallback;

import java.util.ArrayList;

import info.izumin.android.bletia.BletiaException;

/**
 * Created by izumin on 9/23/15.
 */
class EventEmitter extends ArrayList<KonashiListener> {

    private final EventEmitter self = this;
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    public void emitConnect(final KonashiManager manager) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                for (KonashiListener listener : self) {
                    listener.onConnect(manager);
                }
            }
        });
    }

    public void emitDisconnect(final KonashiManager manager) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                for (KonashiListener listener : self) {
                    listener.onDisconnect(manager);
                }
            }
        });
    }

    public void emitError(final KonashiManager manager, final BletiaException e) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                for (KonashiListener listener : self) {
                    listener.onError(manager, e);
                }
            }
        });
    }

    public void emitUpdatePioOutput(final KonashiManager manager, final int value) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                for (KonashiListener listener : self) {
                    listener.onUpdatePioOutput(manager, value);
                }
            }
        });
    }

    public void emitUpdateUartRx(final KonashiManager manager, final byte[] value) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                for (KonashiListener listener : self) {
                    listener.onUpdateUartRx(manager, value);
                }
            }
        });
    }

    public void emitUpdateSpiMiso(final KonashiManager manager, final byte[] value) {
        mHandler.post(new UpdateSpiMisoRunnable(self, manager));
    }

    public void emitUpdateBatteryLevel(final KonashiManager manager, final int level) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                for (KonashiListener listener : self) {
                    listener.onUpdateBatteryLevel(manager, level);
                }
            }
        });
    }
}

class UpdateSpiMisoRunnable implements Runnable {

    private EventEmitter mEventEmitter;
    private KonashiManager mKonashiManager;
    private KonashiListener mKonashiListener;

    public UpdateSpiMisoRunnable(EventEmitter eventEmitter, KonashiManager konashiManager) {
        mEventEmitter = eventEmitter;
        mKonashiManager = konashiManager;
    }

    @Override
    public void run() {
        for (KonashiListener listener : mEventEmitter) {
            mKonashiListener = listener;
            mKonashiManager.spiRead().then(new DoneCallback<BluetoothGattCharacteristic>() {
                @Override
                public void onDone(BluetoothGattCharacteristic result) {
                    byte mData[] = new byte[result.getValue().length];
                    for (int i = 0; i < result.getValue().length; i++) {
                        mData[i] = (byte) ((result.getValue()[i] & 0xff));
                        mKonashiListener.onUpdateSpiMiso(mKonashiManager, mData);
                    }
                }
            });
        }
    }
}
