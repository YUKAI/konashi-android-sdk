package com.uxxu.konashi.uart_tester;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.uxxu.konashi.lib.Konashi;
import com.uxxu.konashi.lib.KonashiErrorReason;
import com.uxxu.konashi.lib.KonashiListener;
import com.uxxu.konashi.lib.ui.KonashiActivity;


public class MainActivity extends KonashiActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private final MainActivity self = this;

    private LinearLayout mContainer;
    private Button mFindButton;
    private Button mCheckConnectionButton;
    private Button mResetButton;
    private Button mWriteUartButton;
    private EditText mSendCharctorEdit;
    private TextView mUartRxTextView;

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
                if (!getKonashiManager().isReady()) {
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

        mSendCharctorEdit = (EditText)findViewById(R.id.edit_send_charactor);

        mWriteUartButton = (Button)findViewById(R.id.button_write_uart);
        mWriteUartButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch(motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        // 触った時
                        getKonashiManager().uartWrite((mSendCharctorEdit.getText().toString() + "=").getBytes());
                        mSendCharctorEdit.setText("");
                        break;
                }
                return false;
            }
        });

        mUartRxTextView = (TextView)findViewById(R.id.text_uart_rx);

        // konashiのイベントハンドラを設定。定義は下の方にあります
        getKonashiManager().addListener(mKonashiListener);
    }

    /**
     * konashiのイベントハンドラ
     */
    private final KonashiListener mKonashiListener = new KonashiListener() {
        @Override
        public void onNotFoundPeripheral() {}

        @Override
        public void onConnected() {}

        @Override
        public void onDisconnected() {}

        @Override
        public void onReady(){
            Log.d(TAG, "onKonashiReady");

            self.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // findボタンのテキストをdisconnectに
                    mFindButton.setText(getText(R.string.disconnect_button));
                    // ボタンを表示する
                    mContainer.setVisibility(View.VISIBLE);
                }
            });

            // konashiのポートの定義。
            getKonashiManager().uartBaudrate(Konashi.UART_RATE_9K6);
            getKonashiManager().uartMode(Konashi.UART_ENABLE);
        }

        @Override
        public void onUpdatePioInput(byte value) {}

        @Override
        public void onUpdateAnalogValue(int pin, int value) {}

        @Override
        public void onUpdateAnalogValueAio0(int value) {}

        @Override
        public void onUpdateAnalogValueAio1(int value) {}

        @Override
        public void onUpdateAnalogValueAio2(int value) {}

        @Override
        public void onCompleteUartRx(final byte[] data) {
            self.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mUartRxTextView.setText(new String(data));
                }
            });
        }

        @Override
        public void onUpdateBatteryLevel(int level) {}

        @Override
        public void onUpdateSignalStrength(int rssi) {}

        @Override
        public void onCancelSelectKonashi() {}

        @Override
        public void onError(KonashiErrorReason errorReason, String message) {}
    };
}
