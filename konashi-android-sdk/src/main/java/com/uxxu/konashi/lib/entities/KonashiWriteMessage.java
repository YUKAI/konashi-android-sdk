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
public class KonashiWriteMessage extends KonashiMessage {
    public static final String TAG = KonashiWriteMessage.class.getSimpleName();

    private static final String KEY_DATA = "data";

    private final byte[] mData;

    public KonashiWriteMessage(Bundle bundle) {
        super(bundle);
        mData = bundle.getByteArray(KEY_DATA);
    }

    public byte[] getData() {
        return mData;
    }

    public static Message obtain(UUID characteristicUuid, byte[] data) {
        Message message = Message.obtain();
        message.what = MESSAGE_WRITE;
        message.setData(getBundle(characteristicUuid, data));
        return message;
    }

    private static Bundle getBundle(UUID characteristicUuid, byte[] data) {
        Bundle bundle = getBundle(characteristicUuid);
        bundle.putByteArray(KEY_DATA, data);
        return bundle;
    }
}
