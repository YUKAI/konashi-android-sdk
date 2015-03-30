package com.e10dokup.konashi_sdk_test;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;

import com.uxxu.konashi.lib.Konashi;

public class ControllerActivity extends Activity {
    private static final String TAG = "konashi";

    private SeekBar mSeekbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);

        mSeekbar = (SeekBar)findViewById(R.id.seekbar);
        mSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d(TAG, "onProgress: " + progress);

                Konashi.getManager().pwmLedDrive(Konashi.LED2, progress);
            }
        });
    }
}