package com.uxxu.konashi.sample.piosample;

import android.bluetooth.BluetoothGattCharacteristic;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.uxxu.konashi.lib.Konashi;
import com.uxxu.konashi.lib.KonashiListener;
import com.uxxu.konashi.lib.KonashiManager;
import com.uxxu.konashi.lib.KonashiUtils;

import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;

import info.izumin.android.bletia.BletiaException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Switch mBlinkSwitch, mModeSwitch;

    private KonashiManager mKonashiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBlinkSwitch = (Switch)findViewById(R.id.switch_blink);
        mBlinkSwitch.setOnCheckedChangeListener(mOnBlinkCheckedChangeListener);
        mModeSwitch = (Switch)findViewById(R.id.switch_mode);
        mModeSwitch.setOnCheckedChangeListener(mOnModeCheckedChangeListener);
        findViewById(R.id.btn_connect).setOnClickListener(this);
        findViewById(R.id.btn_disconnect).setOnClickListener(this);

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
                findViewById(R.id.btn_connect).setEnabled(!isReady);
                findViewById(R.id.btn_disconnect).setEnabled(isReady);
                mModeSwitch.setEnabled(isReady);
                mBlinkSwitch.setEnabled(isReady);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_connect:
                mKonashiManager.find(this);
                break;
            case R.id.btn_disconnect:
                mKonashiManager.disconnect();
                break;
        }
    }

    private final CompoundButton.OnCheckedChangeListener mOnBlinkCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            int value = b ? Konashi.HIGH : Konashi.LOW;
            mKonashiManager.digitalWrite(Konashi.PIO1, value);
        }
    };

    private final CompoundButton.OnCheckedChangeListener mOnModeCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            int value = b ? Konashi.OUTPUT : Konashi.INPUT;
            mKonashiManager.pinMode(Konashi.PIO1, value);
        }
    };

    private final KonashiListener mKonashiListener = new KonashiListener() {
        @Override
        public void onConnect(KonashiManager manager) {
            refreshViews();
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