package com.uxxu.konashi.lib;

import android.app.Activity;

import com.uxxu.konashi.lib.listeners.KonashiListener;

/**
 * konashiのイベントをキャッチするためのオブザーバクラス
 * @deprecated This class deprecated in 0.5.0.
 * Use {@link KonashiListener} instead.
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
@Deprecated
public abstract class KonashiObserver implements KonashiListener {
    /**
     * runOnUiThreadするために必要なのです。。。
     */
    private Activity mActivity;
    
    /**
     * コンストラクタ
     * @param activity Activity
     */
    public KonashiObserver(Activity activity){
        mActivity = activity;
    }
    
    /**
     * Activityを取得する
     * @return Activity
     */
    public Activity getActivity(){
        return mActivity;
    }
    
    /**
     * findWithNameで指定した名前のkonashiが見つからなかった時、もしくはまわりにBLEデバイスがなかった時に呼ばれる
     */
    @Override
    public void onNotFoundPeripheral(){}   
    /**
     * konashiに接続した時(まだこの時はkonashiが使える状態ではありません)に呼ばれる
     */
    @Override
    public void onConnected(){}
    /**
     * konashiとの接続を切断した時に呼ばれる
     */
    @Override
    public void onDisconnected(){}
    /**
     * konashiに接続完了した時(この時からkonashiにアクセスできるようになります)に呼ばれる
     */
    @Override
    public void onReady(){}
    /**
     * PIOの入力の状態が変化した時に呼ばれる
     */
    @Override
    public void onUpdatePioInput(byte value){}
    /**
     * PIOの出力の状態が変化した時に呼ばれる
     */
    @Override
    public void onUpdatePioOutput(byte value) {}
    /**
     * AIOのどれかのピンの電圧が取得できた時
     */
    @Override
    public void onUpdateAnalogValue(int pin, int value){}
    /**
     * AIO0の電圧が取得できた時
     */
    @Override
    public void onUpdateAnalogValueAio0(int value){}
    /**
     * AIO1の電圧が取得できた時
     */
    @Override
    public void onUpdateAnalogValueAio1(int value){}
    /**
     * AIO2の電圧が取得できた時
     */
    @Override
    public void onUpdateAnalogValueAio2(int value){}
    @Override
    public void onUpdatePioSetting(int modes) {}
    @Override
    public void onUpdatePioPullup(int pullups) {}
    @Override
    public void onUpdatePwmMode(int modes) {}
    @Override
    public void onUpdatePwmPeriod(int pin, int period) {}
    @Override
    public void onUpdatePwmDuty(int pin, int duty) {}
    @Override
    public void onUpdateUartMode(int mode) {}
    @Override
    public void onUpdateUartBaudrate(int baudrate) {}
    @Override
    public void onWriteUart(byte[] data) {}
    @Override
    public void onUpdateI2cMode(int mode) {}
    @Override
    public void onSendI2cCondition(int condition) {}
    @Override
    public void onWriteI2c(byte[] data, byte address) {}
    @Override
    public void onReadI2c(byte[] data, byte address) {}

    /**
     * UARTのRxからデータを受信した時
     */
    @Override
    public void onCompleteUartRx(byte[] data){}
    /**
     * for konashi v1(old code)
     */
    //@Override
    //public void onCompleteUartRx(byte data){}
    /**
     * konashiのバッテリーのレベルを取得できた時
     */
    @Override
    public void onUpdateBatteryLevel(int level){}
    /**
     * konashiの電波強度を取得できた時
     */
    @Override
    public void onUpdateSignalStrength(int rssi){}
    /**
     * BLEデバイス選択ダイアログをキャンセルした時に呼ばれる
     */
    @Override
    public void onCancelSelectKonashi(){}
    /**
     * エラーが起きた時に呼ばれる
     */
    @Override
    public void onError(KonashiErrorReason errorReason, String message){}
}
