package com.uxxu.konashi.sample.piosample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.uxxu.konashi.lib.Konashi;
import com.uxxu.konashi.lib.KonashiListener;
import com.uxxu.konashi.lib.KonashiManager;

import info.izumin.android.bletia.BletiaException;

public class MainActivity extends AppCompatActivity {
    private final MainActivity self = this;

    private Button mBlinkButton;
    private Button mConnectButton;
    private Button mDisconnectButton;

    private KonashiManager mKonashiManager;

    private boolean isBlinking = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBlinkButton = (Button)findViewById(R.id.btn_blink);
        mBlinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBlinking) mKonashiManager.digitalWrite(Konashi.PIO2, Konashi.HIGH);
                else mKonashiManager.digitalWrite(Konashi.PIO2, Konashi.LOW);
                isBlinking = !isBlinking;
            }
        });
        mConnectButton = (Button)findViewById(R.id.btn_connect);
        mConnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mKonashiManager.find(self);
            }
        });
        mDisconnectButton = (Button)findViewById(R.id.btn_disconnect);
        mDisconnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mKonashiManager.disconnect();
            }
        });

        mKonashiManager = new KonashiManager();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mKonashiManager.addListener(mKonashiListener);
        mKonashiManager.initialize(this);
        refreshViews();
    }

    private void refreshViews() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                boolean isReady = mKonashiManager.isReady();
                mConnectButton.setEnabled(!isReady);
                mDisconnectButton.setEnabled(isReady);
                mBlinkButton.setEnabled(isReady);
            }
        });
    }



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