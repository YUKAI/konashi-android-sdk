package com.uxxu.konashi.sample.piosample;

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

import info.izumin.android.bletia.BletiaException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Switch mBlinkSwitch;

    private KonashiManager mKonashiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBlinkSwitch = (Switch)findViewById(R.id.switch_blink);
        mBlinkSwitch.setOnCheckedChangeListener(mOnCheckedChangeListener);

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
        if (mKonashiManager != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mKonashiManager.reset();
                    mKonashiManager.disconnect();
                    mKonashiManager = null;
                }
            }).start();
        }
        super.onDestroy();
    }

    private void refreshViews() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                boolean isReady = mKonashiManager.isReady();
                findViewById(R.id.btn_connect).setEnabled(!isReady);
                findViewById(R.id.btn_disconnect).setEnabled(isReady);
                mBlinkSwitch.setEnabled(isReady);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_connect:
                mKonashiManager.find(this);
            case R.id.btn_disconnect:
                mKonashiManager.disconnect();
        }
    }

    CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b) mKonashiManager.digitalWrite(Konashi.PIO2, Konashi.HIGH);
            else mKonashiManager.digitalWrite(Konashi.PIO2, Konashi.LOW);
        }
    };

    KonashiListener mKonashiListener = new KonashiListener() {
        @Override
        public void onConnect(KonashiManager manager) {
            refreshViews();
            mKonashiManager.pinMode(Konashi.PIO2, Konashi.OUTPUT);
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