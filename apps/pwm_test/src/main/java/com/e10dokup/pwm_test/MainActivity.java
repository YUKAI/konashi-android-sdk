package com.e10dokup.pwm_test;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.uxxu.konashi.lib.Konashi;
import com.uxxu.konashi.lib.KonashiObserver;
import com.uxxu.konashi.lib.ui.KonashiActivity;


public class MainActivity extends KonashiActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private final MainActivity self = this;

    private LinearLayout mContainer;
    private Button mFindButton;
    private Button mCheckConnectionButton;
    private Button mResetButton;

    private SeekBar mSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ボタン全体のコンテナ
        mContainer = (LinearLayout)findViewById(R.id.container);
        mContainer.setVisibility(View.GONE);


        // 一番上に表示されるボタン。konashiにつないだり、切断したり
        mFindButton = (Button)findViewById(R.id.find_button);
        mFindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!getKonashiManager().isReady()){
                    // konashiを探して接続。konashi選択ダイアログがでます
                    getKonashiManager().find(MainActivity.this);

                    // konashiを明示的に指定して、選択ダイアログを出さない
                    //mKonashiManager.findWithName(MainActivity.this, "konashi#4-0452");
                } else {
                    // konashiバイバイ
                    getKonashiManager().disconnect();

                    mFindButton.setText(getText(R.string.find_button));
                    mContainer.setVisibility(View.GONE);
                }
            }
        });

        mCheckConnectionButton = (Button)findViewById(R.id.check_connection_button);
        mCheckConnectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getKonashiManager().isConnected()){
                    Toast.makeText(self, getKonashiManager().getPeripheralName() + " is connected!", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(self, "konashi isn't connected!", Toast.LENGTH_LONG).show();
                }
            }
        });

        mResetButton = (Button)findViewById(R.id.reset_button);
        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getKonashiManager().reset();

                mFindButton.setText(getText(R.string.find_button));
                mContainer.setVisibility(View.GONE);
            }
        });

        mSeekBar = (SeekBar)findViewById(R.id.seekbar_pwm);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                getKonashiManager().pwmLedDrive(Konashi.LED3, i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        // konashiのイベントハンドラを設定。定義は下の方にあります
        getKonashiManager().addObserver(mKonashiObserver);
    }

    /**
     * konashiのイベントハンドラ
     */
    private final KonashiObserver mKonashiObserver = new KonashiObserver(MainActivity.this) {
        @Override
        public void onReady(){
            Log.d(TAG, "onKonashiReady");

            // findボタンのテキストをdisconnectに
            mFindButton.setText(getText(R.string.disconnect_button));
            // ボタンを表示する
            mContainer.setVisibility(View.VISIBLE);

            // konashiのポートの定義。LED2をPWMに設定。
            getKonashiManager().pwmMode(Konashi.LED2, Konashi.PWM_ENABLE);
            getKonashiManager().pwmPeriod(Konashi.LED2, 10000);
            getKonashiManager().pwmDuty(Konashi.LED2, 1000);
            getKonashiManager().pwmMode(Konashi.LED3, Konashi.PWM_ENABLE_LED_MODE);
        }

        @Override
        public void onUpdatePioInput(byte value){
            Log.d(TAG, "onUpdatePioInput: " + value);
        }
    };
}
