package com.e10dokup.konashi_sdk_test;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
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
    private boolean mIsStatePullUp = false;

    private TextView mSwTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ボタン全体のコンテナ
        mContainer = (LinearLayout)findViewById(R.id.container);
        mContainer.setVisibility(View.GONE);

        // スイッチの状態テキスト
        mSwTextView = (TextView)findViewById(R.id.sw_state);
        mSwTextView.setText(getString(R.string.off));

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

        // ボタンの動作を定義
        setButtonAction(R.id.led2_button, Konashi.LED2);
        setButtonAction(R.id.led3_button, Konashi.LED3);
        setButtonAction(R.id.led4_button, Konashi.LED4);
        setButtonAction(R.id.led5_button, Konashi.LED5);

        // konashiのイベントハンドラを設定。定義は下の方にあります
        getKonashiManager().addObserver(mKonashiObserver);
    }

    /**
     * ボタンの動作を定義する。ボタンに触るとLEDがON, ボタンから離すとLEDがOFF
     * @param resId Resource ID
     * @param pin ボタンを押した時に反応するポート
     */
    private void setButtonAction(int resId, final int pin){
        Button button = (Button)findViewById(resId);
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        // 触った時
                        getKonashiManager().digitalWrite(pin, Konashi.HIGH);  // HIGHでLEDが点灯
                        break;

                    case MotionEvent.ACTION_UP:
                        // 離した時
                        getKonashiManager().digitalWrite(pin, Konashi.LOW);  // LOWでLEDが消灯
                        break;
                }
                return false;
            }
        });
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

            // konashiのポートの定義。LED2〜5を出力に設定。
            getKonashiManager().pinMode(Konashi.LED2, Konashi.OUTPUT);
            getKonashiManager().pinMode(Konashi.LED3, Konashi.OUTPUT);
            getKonashiManager().pinMode(Konashi.LED4, Konashi.OUTPUT);
            getKonashiManager().pinMode(Konashi.LED5, Konashi.OUTPUT);

            // konashiのポートの定義。S1をINPUTに（デフォルトでINPUTですが）
            getKonashiManager().pinMode(Konashi.S1, Konashi.INPUT);
        }

        @Override
        public void onUpdatePioInput(byte value){
            Log.d(TAG, "onUpdatePioInput: " + value);

            // スイッチの状態を見て、テキストを変え，各ピンをプルアップし，出力する．
            if(getKonashiManager().digitalRead(Konashi.S1)==Konashi.HIGH){
                mSwTextView.setText(getString(R.string.on));

                if(!mIsStatePullUp){
                    getKonashiManager().pinPullup(Konashi.LED2, Konashi.PULLUP);
                    getKonashiManager().pinPullup(Konashi.LED3, Konashi.PULLUP);
                    getKonashiManager().pinPullup(Konashi.LED4, Konashi.PULLUP);
                    getKonashiManager().pinPullup(Konashi.LED5, Konashi.PULLUP);

                    getKonashiManager().digitalWrite(Konashi.LED2, Konashi.HIGH);
                    getKonashiManager().digitalWrite(Konashi.LED3, Konashi.HIGH);
                    getKonashiManager().digitalWrite(Konashi.LED4, Konashi.HIGH);
                    getKonashiManager().digitalWrite(Konashi.LED5, Konashi.HIGH);

                    mIsStatePullUp = true;
                }else{
                    getKonashiManager().pinPullup(Konashi.LED2, Konashi.NO_PULLS);
                    getKonashiManager().pinPullup(Konashi.LED3, Konashi.NO_PULLS);
                    getKonashiManager().pinPullup(Konashi.LED4, Konashi.NO_PULLS);
                    getKonashiManager().pinPullup(Konashi.LED5, Konashi.NO_PULLS);

                    getKonashiManager().digitalWrite(Konashi.LED2, Konashi.LOW);
                    getKonashiManager().digitalWrite(Konashi.LED3, Konashi.LOW);
                    getKonashiManager().digitalWrite(Konashi.LED4, Konashi.LOW);
                    getKonashiManager().digitalWrite(Konashi.LED5, Konashi.LOW);

                    mIsStatePullUp = false;
                }
            } else {
                mSwTextView.setText(getString(R.string.off));
            }
        }
    };
}
