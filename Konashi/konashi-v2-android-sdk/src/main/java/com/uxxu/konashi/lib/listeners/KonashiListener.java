package com.uxxu.konashi.lib.listeners;

import com.uxxu.konashi.lib.listeners.KonashiAnalogListener;
import com.uxxu.konashi.lib.listeners.KonashiConnectionListener;
import com.uxxu.konashi.lib.listeners.KonashiDeviceInfoListener;
import com.uxxu.konashi.lib.listeners.KonashiDigitalListener;
import com.uxxu.konashi.lib.listeners.KonashiUartListener;

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
public interface KonashiListener extends
        KonashiConnectionListener, KonashiDeviceInfoListener,
        KonashiAnalogListener, KonashiDigitalListener, KonashiPwmListener,
        KonashiUartListener, KonashiI2cListener {
}
