package com.uxxu.konashi.lib;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

import com.uxxu.konashi.lib.action.KonashiEnableNotificationAction;
import com.uxxu.konashi.lib.dispatcher.DispatcherContainer;
import com.uxxu.konashi.lib.util.KonashiUtils;
import com.uxxu.konashi.lib.util.SpiUtils;
import com.uxxu.konashi.lib.util.UartUtils;

import org.jdeferred.DoneCallback;

import java.util.UUID;

import info.izumin.android.bletia.Bletia;
import info.izumin.android.bletia.BletiaException;
import info.izumin.android.bletia.BletiaListener;

/**
 * Created by izumin on 9/23/15.
 */
class CallbackHandler implements BletiaListener {

    private final EventEmitter mEmitter;
    private final KonashiManager mManager;
    private final DispatcherContainer mDispatcherContainer;

    public CallbackHandler(KonashiManager manager, EventEmitter emitter, DispatcherContainer dispatcherContainer) {
        mManager = manager;
        mEmitter = emitter;
        mDispatcherContainer = dispatcherContainer;
    }

    @Override
    public void onConnect(Bletia bletia) {
        bletia.discoverServices();
    }

    @Override
    public void onDisconnect(Bletia bletia) {
        mEmitter.emitDisconnect(mManager);
    }

    @Override
    public void onError(BletiaException e) {
        KonashiUtils.log(e.getType().getName());
        mEmitter.emitError(mManager, e);
    }

    @Override
    public void onServicesDiscovered(Bletia bletia, int status) {
//        if (status == BluetoothGatt.GATT_SUCCESS) {
//            BluetoothGattService service = bletia.getService(KonashiUUID.KONASHI_SERVICE_UUID);
//            bletia.enableNotification(service.getCharacteristic(KonashiUUID.PIO_INPUT_NOTIFICATION_UUID), true);
//            bletia.enableNotification(service.getCharacteristic(KonashiUUID.UART_RX_NOTIFICATION_UUID), true);
//            bletia.enableNotification(service.getCharacteristic(KonashiUUID.HARDWARE_LOW_BAT_NOTIFICATION_UUID), true);
//            BluetoothGattCharacteristic spiCharacteristic = service.getCharacteristic(KonashiUUID.SPI_NOTIFICATION_UUID);
//            if(spiCharacteristic != null) bletia.enableNotification(spiCharacteristic, true);
//            mEmitter.emitConnect(mManager);
//        }
        if (status == BluetoothGatt.GATT_SUCCESS) {
            BluetoothGattService service = bletia.getService(KonashiUUID.KONASHI_SERVICE_UUID);
            bletia.execute(new KonashiEnableNotificationAction(service.getCharacteristic(KonashiUUID.PIO_INPUT_NOTIFICATION_UUID), true));
            bletia.execute(new KonashiEnableNotificationAction(service.getCharacteristic(KonashiUUID.UART_RX_NOTIFICATION_UUID), true));
            bletia.execute(new KonashiEnableNotificationAction(service.getCharacteristic(KonashiUUID.HARDWARE_LOW_BAT_NOTIFICATION_UUID), true));
            BluetoothGattCharacteristic spiCharacteristic = service.getCharacteristic(KonashiUUID.SPI_NOTIFICATION_UUID);
            if(spiCharacteristic != null) bletia.execute(new KonashiEnableNotificationAction(spiCharacteristic, true));
            mEmitter.emitConnect(mManager);
        }
    }

    @Override
    public void onCharacteristicChanged(Bletia bletia, BluetoothGattCharacteristic characteristic) {
        UUID uuid = characteristic.getUuid();

        if (KonashiUUID.PIO_INPUT_NOTIFICATION_UUID.equals(uuid)) {
            mDispatcherContainer.getPioDispatcher().onDone(characteristic);
            mEmitter.emitUpdatePioOutput(mManager, characteristic.getValue()[0]);
        } else if (KonashiUUID.UART_RX_NOTIFICATION_UUID.equals(uuid)) {
            mDispatcherContainer.getUartDispatcher().onDone(characteristic);
            mEmitter.emitUpdateUartRx(mManager, UartUtils.removeLengthByte(characteristic.getValue()));
        } else if (KonashiUUID.SPI_NOTIFICATION_UUID.equals(uuid)) {
            mManager.spiRead().then(new DoneCallback<BluetoothGattCharacteristic>() {
                @Override
                public void onDone(BluetoothGattCharacteristic result) {
                    mEmitter.emitUpdateSpiMiso(mManager, SpiUtils.getDataFromResult(result.getValue()));
                }
            });
        } else if (KonashiUUID.HARDWARE_LOW_BAT_NOTIFICATION_UUID.equals(uuid)) {
            mEmitter.emitUpdateBatteryLevel(mManager, KonashiUtils.getBatteryLevel(characteristic));
        }
    }

}


