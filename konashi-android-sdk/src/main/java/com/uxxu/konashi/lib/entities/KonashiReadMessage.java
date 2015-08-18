package com.uxxu.konashi.lib.entities;

import android.os.Bundle;
import android.os.Message;

import java.util.UUID;

/**
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
public class KonashiReadMessage extends KonashiMessage {
    public static final String TAG = KonashiReadMessage.class.getSimpleName();

    private static final String KEY_SERVICE_UUID = "service uuid";

    private final UUID mServiceUuid;

    public KonashiReadMessage(Bundle bundle) {
        super(bundle);
        mServiceUuid = (UUID) bundle.getSerializable(KEY_SERVICE_UUID);
    }

    public UUID getServiceUuid() {
        return mServiceUuid;
    }

    public static Message obtain(UUID serviceUuid, UUID characteristicUuid) {
        Message message = Message.obtain();
        message.what = MESSAGE_READ;
        message.setData(getBundle(serviceUuid, characteristicUuid));
        return message;
    }

    private static Bundle getBundle(UUID serviceUuid, UUID characteristicUuid) {
        Bundle bundle = getBundle(characteristicUuid);
        bundle.putSerializable(KEY_SERVICE_UUID, serviceUuid);
        return bundle;
    }
}
