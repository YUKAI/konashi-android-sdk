package com.uxxu.konashi.sample.piosample;

import android.bluetooth.BluetoothGattCharacteristic;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.uxxu.konashi.lib.Konashi;
import com.uxxu.konashi.lib.KonashiListener;
import com.uxxu.konashi.lib.KonashiManager;
import com.uxxu.konashi.lib.KonashiUtils;

import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;

import javax.security.auth.callback.CallbackHandler;

import info.izumin.android.bletia.BletiaException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private KonashiManager mKonashiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((ToggleButton) findViewById(R.id.toggle_blink)).setOnCheckedChangeListener(mOnBlinkCheckedChangeListener);
        findViewById(R.id.btn_find).setOnClickListener(this);

        mKonashiManager = new KonashiManager();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mKonashiManager.addListener(mKonashiListener);
        mKonashiManager.initialize(this);
        refreshViews();
    }

    @Override
    protected void onPause() {
        mKonashiManager.removeListener(mKonashiListener);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mKonashiManager.reset()
                .then(new DoneCallback<BluetoothGattCharacteristic>() {
                    @Override
                    public void onDone(BluetoothGattCharacteristic result) {
                        mKonashiManager.disconnect();
                    }
                });
            }
        }).start();
        super.onDestroy();
    }

    private void refreshViews() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                boolean isReady = mKonashiManager.isReady();
                findViewById(R.id.btn_find).setVisibility(!isReady ? View.VISIBLE : View.GONE);
                findViewById(R.id.toggle_blink).setVisibility(isReady ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    public void onClick(View view) {
        mKonashiManager.find(this);
    }

    private final CompoundButton.OnCheckedChangeListener mOnBlinkCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            int value = b ? Konashi.HIGH : Konashi.LOW;
            mKonashiManager.digitalWrite(Konashi.PIO1, value);
        }
    };

    private final KonashiListener mKonashiListener = new KonashiListener() {
        @Override
        public void onConnect(KonashiManager manager) {
            refreshViews();
            mKonashiManager.pinMode(Konashi.PIO1, Konashi.OUTPUT)
            .fail(new FailCallback<BletiaException>() {
                @Override
                public void onFail(BletiaException result) {
                    KonashiUtils.log(result.getMessage());
                }
            });
        }

        @Override
        public void onDisconnect(KonashiManager manager) {
            refreshViews();
        }

        @Override
        public void onError(KonashiManager manager, BletiaException e) {

        }

        @Override
        public void onUpdatePioOutput(KonashiManager manager, int value) {

        }

        @Override
        public void onUpdateUartRx(KonashiManager manager, byte[] value) {

        }

        @Override
        public void onUpdateBatteryLevel(KonashiManager manager, int level) {

        }
    };
}