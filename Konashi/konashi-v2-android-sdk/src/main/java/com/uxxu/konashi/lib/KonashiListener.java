package com.uxxu.konashi.lib;

/**
 * konashiのイベントをキャッチするためのインタフェース
 *
 * @author izumin5210
 * http://izumin.info
 * ========================================================================
 * Copyright 2015 Yukai Engineering Inc.
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
public interface KonashiListener {
    /**
     * findWithNameで指定した名前のkonashiが見つからなかった時、もしくはまわりにBLEデバイスがなかった時に呼ばれる
     */
    void onNotFoundPeripheral();
    /**
     * konashiに接続した時(まだこの時はkonashiが使える状態ではありません)に呼ばれる
     */
    void onConnected();
    /**
     * konashiとの接続を切断した時に呼ばれる
     */
    void onDisconnected();
    /**
     * konashiに接続完了した時(この時からkonashiにアクセスできるようになります)に呼ばれる
     */
    void onReady();
    /**
     * PIOの入力の状態が変化した時に呼ばれる
     */
    void onUpdatePioInput(byte value);
    /**
     * AIOのどれかのピンの電圧が取得できた時
     */
    void onUpdateAnalogValue(int pin, int value);
    /**
     * AIO0の電圧が取得できた時
     */
    void onUpdateAnalogValueAio0(int value);
    /**
     * AIO1の電圧が取得できた時
     */
    void onUpdateAnalogValueAio1(int value);
    /**
     * AIO2の電圧が取得できた時
     */
    void onUpdateAnalogValueAio2(int value);
    /**
     * UARTのRxからデータを受信した時
     */
    void onCompleteUartRx(byte[] data);
    /**
     * for konashi v1(old code)
     */
    //void onCompleteUartRx(byte data);
    /**
     * konashiのバッテリーのレベルを取得できた時
     */
    void onUpdateBatteryLevel(int level);
    /**
     * konashiの電波強度を取得できた時
     */
    void onUpdateSignalStrength(int rssi);
    /**
     * BLEデバイス選択ダイアログをキャンセルした時に呼ばれる
     */
    void onCancelSelectKonashi();
    /**
     * エラーが起きた時に呼ばれる
     */
    void onError(KonashiErrorReason errorReason, String message);
}
