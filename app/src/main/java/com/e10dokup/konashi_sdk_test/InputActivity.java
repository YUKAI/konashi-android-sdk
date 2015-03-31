package com.e10dokup.konashi_sdk_test;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.uxxu.konashi.lib.Konashi;
import com.uxxu.konashi.lib.KonashiActivity;
import com.uxxu.konashi.lib.KonashiObserver;

public class InputActivity extends KonashiActivity {
    private static final String TAG = InputActivity.class.getSimpleName();
    private final InputActivity self = this;

    private LinearLayout mContainer;
    private Button mFindButton;
    private TextView mSwTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

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
                    getKonashiManager().find(InputActivity.this);

                    // konashiを明示的に指定して、選択ダイアログを出さない
                    //mKonashiManager.findWithName(MainActivity.this, "konashi#4-0452");
                } else {
                    // konashiバイバイ
                    getKonashiManager().disconnect();

                    // disconnectの表示をfindに変更。コンテナも非表示に
                    mFindButton.setText(getText(R.string.find_button));
                    mContainer.setVisibility(View.GONE);
                }
            }
        });

        // konashiのイベントハンドラを設定。定義は下の方にあります
        getKonashiManager().addObserver(mKonashiObserver);
    }

    /**
     * konashiのイベントハンドラ
     */
    private final KonashiObserver mKonashiObserver = new KonashiObserver(InputActivity.this) {
        @Override
        public void onReady(){
            Log.d(TAG, "onKonashiReady");

            // findボタンのテキストをdisconnectに
            mFindButton.setText(getText(R.string.disconnect_button));
            // コンテナを表示する
            mContainer.setVisibility(View.VISIBLE);

            // konashiのポートの定義。S1をINPUTに（デフォルトでINPUTですが）
            getKonashiManager().pinMode(Konashi.S1, Konashi.INPUT);
        }

        @Override
        public void onUpdatePioInput(byte value){
            Log.d(TAG, "onUpdatePioInput: " + value);

            // スイッチの状態を見て、テキスト変える
            if(getKonashiManager().digitalRead(Konashi.S1)==Konashi.HIGH){
                mSwTextView.setText(getString(R.string.on));
            } else {
                mSwTextView.setText(getString(R.string.off));
            }
        }
    };
}