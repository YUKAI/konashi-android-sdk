package com.uxxu.konashi.lib.action;

import android.bluetooth.BluetoothGattService;

import com.uxxu.konashi.lib.Konashi;
import com.uxxu.konashi.lib.KonashiUUID;
import com.uxxu.konashi.lib.util.PioUtils;

import java.util.UUID;

/**
 * Created by izumin on 9/16/15.
 */
public class PioDigitalWriteAction extends KonashiWriteCharacteristicAction {

    private static final UUID UUID = KonashiUUID.PIO_OUTPUT_UUID;

    private int mPin;
    private int mOutput;
    private int mOutputs;

    private final boolean mIsForAll;

    public PioDigitalWriteAction(BluetoothGattService service, int outputs) {
        super(service, UUID);
        mIsForAll = true;
        mOutputs = outputs;
    }

    public PioDigitalWriteAction(BluetoothGattService service, int pin, int newOutput, int currentOutputs) {
        super(service, UUID);
        mIsForAll = false;
        mPin = pin;
        mOutput = newOutput;
        mOutputs = currentOutputs;
    }

    @Override
    protected void setValue() {
        if (!mIsForAll) {
            if (mOutput == Konashi.OUTPUT) {
                mOutputs |= (0x01 << mPin);
            } else {
                mOutputs &= (~(0x01 << mPin) & 0xff);
            }
        }
        getCharacteristic().setValue(new byte[]{(byte) mOutputs});
    }

    @Override
    protected boolean hasValidParams() {
        if (mIsForAll) {
            return PioUtils.isValidOutputs(mOutputs);
        } else {
            return PioUtils.isValidPin(mPin) && PioUtils.isValidOutput(mOutput);
        }
    }
}
