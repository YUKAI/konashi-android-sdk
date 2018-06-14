package com.example.ishimotokiko.gettingstarted;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.uxxu.konashi.lib.Konashi;
import com.uxxu.konashi.lib.KonashiListener;
import com.uxxu.konashi.lib.KonashiManager;

import info.izumin.android.bletia.BletiaException;

public class MainActivity extends AppCompatActivity {

    private KonashiManager mKonashiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mKonashiManager = new KonashiManager(getApplicationContext());
        final MainActivity self = this;
        ((ToggleButton)findViewById(R.id.tgl_blink)).setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton v, boolean isChecked) {
                mKonashiManager.digitalWrite(Konashi.LED2, isChecked ? Konashi.HIGH : Konashi.LOW);
            }
        });
        findViewById(R.id.btn_find).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mKonashiManager.find(self);
            }
        });
        mKonashiManager.addListener(new KonashiListener() {
            @Override
            public void onConnect(KonashiManager manager) {
                refreshViews();
                mKonashiManager.pinMode(Konashi.LED2, Konashi.OUTPUT);
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
            public void onUpdateSpiMiso(KonashiManager manager, byte[] value) {

            }

            @Override
            public void onUpdateBatteryLevel(KonashiManager manager, int level) {

            }
        });

    }

    private void refreshViews() {
        boolean isReady = mKonashiManager.isReady();
        findViewById(R.id.btn_find).setVisibility(isReady ? View.GONE : View.VISIBLE);
        findViewById(R.id.tgl_blink).setVisibility(isReady ? View.VISIBLE : View.GONE);
    }
}