package com.uxxu.konashi.lib;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.uxxu.konashi.lib.entities.KonashiMessage;
import com.uxxu.konashi.lib.entities.KonashiReadMessage;
import com.uxxu.konashi.lib.entities.KonashiWriteMessage;

/**
 * Created by izumin on 8/4/15.
 */
public class KonashiMessageHandler extends Handler {
    public static final String TAG = KonashiMessageHandler.class.getSimpleName();

    private HandlerThread mHandlerThread;
    private BluetoothGatt mBluetoothGatt;

    public KonashiMessageHandler(HandlerThread handlerThread) {
        super(handlerThread.getLooper());
        mHandlerThread = handlerThread;
    }

    public void start() {
        mHandlerThread.start();
    }

    public boolean stop() {
        return mHandlerThread.quitSafely();
    }

    public void setBluetoothGatt(BluetoothGatt bluetoothGatt) {
        mBluetoothGatt = bluetoothGatt;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case KonashiMessage.MESSAGE_WRITE:
                writeValue(new KonashiWriteMessage(msg.getData()));
                break;
            case KonashiMessage.MESSAGE_READ:
                readValue(new KonashiReadMessage(msg.getData()));
                break;
        }
    }

    private void writeValue(KonashiWriteMessage msg) {
        if (mBluetoothGatt == null) { return; }
        BluetoothGattService service = mBluetoothGatt.getService(KonashiUUID.KONASHI_SERVICE_UUID);
        BluetoothGattCharacteristic characteristic = service.getCharacteristic(msg.getCharacteristicUuid());
        characteristic.setValue(msg.getData());
        mBluetoothGatt.writeCharacteristic(characteristic);
    }

    private void readValue(KonashiReadMessage msg) {
        if (mBluetoothGatt == null) { return; }
        BluetoothGattService service = mBluetoothGatt.getService(msg.getServiceUuid());
        BluetoothGattCharacteristic characteristic = service.getCharacteristic(msg.getCharacteristicUuid());
        mBluetoothGatt.readCharacteristic(characteristic);
    }
}
