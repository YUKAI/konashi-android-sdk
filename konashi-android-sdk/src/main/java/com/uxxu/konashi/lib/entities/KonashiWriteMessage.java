package com.uxxu.konashi.lib.entities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.uxxu.konashi.lib.KonashiMessageHandler;

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

    private byte[] mData;

    public KonashiWriteMessage(UUID characteristicUuid, byte[] data){
        super(characteristicUuid);
        mData = data;
    }

    public KonashiWriteMessage(Bundle bundle) {
        super(bundle);
        mData = bundle.getByteArray(KEY_DATA);
    }

    public byte[] getData() {
        return mData;
    }

    @Override
    public Bundle getBundle() {
        Bundle bundle = super.getBundle();
        bundle.putByteArray(KEY_DATA, mData);
        return bundle;
    }

    @Override
    public Message getMessage(Handler handler) {
        Message msg = handler.obtainMessage(KonashiMessageHandler.MESSAGE_WRITE);
        msg.setData(getBundle());
        return msg;
    }
}
