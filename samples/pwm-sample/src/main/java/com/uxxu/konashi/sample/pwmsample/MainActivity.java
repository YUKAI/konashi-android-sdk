package com.uxxu.konashi.sample.pwmsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.uxxu.konashi.lib.KonashiListener;
import com.uxxu.konashi.lib.KonashiManager;

import info.izumin.android.bletia.BletiaException;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.Callback {
    public static final String TAG = MainActivity.class.getSimpleName();
    public final MainActivity self = this;

    private Menu mMenu;
    private TextView mTextNoConnection;

    private KonashiManager mKonashiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextNoConnection = (TextView) findViewById(R.id.text_no_connection);
        mKonashiManager = new KonashiManager();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mKonashiManager.addListener(mKonashiListener);
        mKonashiManager.initialize(this);
    }

    @Override
    protected void onPause() {
        mKonashiManager.removeListener(mKonashiListener);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mKonashiManager != null) {
            mKonashiManager.reset();
            mKonashiManager.disconnect();
            mKonashiManager = null;
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_find_konashi:
                mKonashiManager.find(this);
                return true;
            case R.id.action_disconnect:
                mKonashiManager.disconnect();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public KonashiManager getKonashiManager() {
        return mKonashiManager;
    }

    private void refreshViews() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                boolean isReady = mKonashiManager.isReady();
                mMenu.findItem(R.id.action_find_konashi).setVisible(!isReady);
                mMenu.findItem(R.id.action_disconnect).setVisible(isReady);
                mTextNoConnection.setVisibility(isReady ? View.GONE : View.VISIBLE);
            }
        });
    }

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

