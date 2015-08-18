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
abstract public class KonashiMessage {
    public static final String TAG = KonashiMessage.class.getSimpleName();

    private static final String KEY_CHARACTERISTIC_UUID = "characteristic uuid";

    private UUID mCharacteristicUuid;

    protected KonashiMessage(UUID characteristicUuid) {
        mCharacteristicUuid = characteristicUuid;
    }

    protected KonashiMessage(Bundle bundle) {
        mCharacteristicUuid = (UUID) bundle.getSerializable(KEY_CHARACTERISTIC_UUID);
    }

    public UUID getCharacteristicUuid() {
        return mCharacteristicUuid;
    }

    public Bundle getBundle() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_CHARACTERISTIC_UUID, mCharacteristicUuid);
        return bundle;
    }

    public Message getMessage() {
        Message msg = Message.obtain();
        msg.what = getWhat();
        msg.setData(getBundle());
        return msg;
    }

    abstract protected int getWhat();
}
