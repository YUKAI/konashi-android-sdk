package com.uxxu.konashi.lib;

import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;

import com.uxxu.konashi.lib.listeners.KonashiBaseListener;

import org.jdeferred.Promise;

import info.izumin.android.bletia.BletiaException;

/**
 * konashi APIのインタフェース
 * 
 * @author monakaz, YUKAI Engineering
 * http://konashi.ux-xu.com
 * ========================================================================
 * Copyright 2014 Yukai Engineering Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
public interface KonashiApiInterface {
    // Observer
    public void addListener(KonashiBaseListener observer);
    public void removeListener(KonashiBaseListener observer);
    public void removeAllListeners();
    public void addObserver(KonashiObserver observer);
    public void removeObserver(KonashiObserver observer);
    public void removeAllObservers();
    
    // initialization
    public void initialize(Context context);
    public void find(Activity activity);
    public void find(Activity activity, boolean isShowKonashiOnly);
    public void findWithName(Activity activity, String name);
    
    // PIO
    public Promise<BluetoothGattCharacteristic, BletiaException, Object> pinMode(int pin, int mode);
    public Promise<BluetoothGattCharacteristic, BletiaException, Object> pinModeAll(int modes);
    public Promise<BluetoothGattCharacteristic, BletiaException, Object> pinPullup(int pin, int pullup);
    public Promise<BluetoothGattCharacteristic, BletiaException, Object> pinPullupAll(int pullups);
    public int digitalRead(int pin);
    public int digitalReadAll();
    public Promise<BluetoothGattCharacteristic, BletiaException, Object> digitalWrite(int pin, int value);
    public Promise<BluetoothGattCharacteristic, BletiaException, Object> digitalWriteAll(int value);
    
    // PWM
    public Promise<BluetoothGattCharacteristic, BletiaException, Object> pwmMode(int pin, int mode);
    public Promise<BluetoothGattCharacteristic, BletiaException, Object> pwmPeriod(int pin, int period);
    public Promise<BluetoothGattCharacteristic, BletiaException, Object> pwmDuty(int pin, int duty);
    public Promise<BluetoothGattCharacteristic, BletiaException, Object> pwmLedDrive(int pin, float dutyRatio);
    public Promise<BluetoothGattCharacteristic, BletiaException, Object> pwmLedDrive(int pin, double dutyRatio);
    
    // AIO
    public Promise<Integer, BletiaException, Object> analogRead(int pin);
//    public void analogWrite(int pin, int milliVolt);

    // I2C
    public Promise<BluetoothGattCharacteristic, BletiaException, Object> i2cMode(int mode);
    public Promise<BluetoothGattCharacteristic, BletiaException, Object> i2cStartCondition();
    public Promise<BluetoothGattCharacteristic, BletiaException, Object> i2cRestartCondition();
    public Promise<BluetoothGattCharacteristic, BletiaException, Object> i2cStopCondition();
    public void i2cWrite(int length, byte[] data, byte address);
    public void i2cReadRequest(int length, byte address);
    public byte[] i2cRead(int length);
    
    // UART
    public void uartMode(int mode);
    public void uartBaudrate(int baudrate);
    public void uartWrite(byte[] data);
    //public void uartWrite(byte data); //for konashi v1(old code)
    
    // Hardware
    public void reset();
    public void batteryLevelReadRequest();
    public int getBatteryLevel();
    public void signalStrengthReadRequest();
    public int getSignalStrength();
}
