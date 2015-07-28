package com.uxxu.konashi.test_all_functions;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.uxxu.konashi.lib.Konashi;
import com.uxxu.konashi.lib.KonashiManager;
import com.uxxu.konashi.lib.KonashiObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kiryu on 7/27/15.
 */
public final class AioFragment extends MainActivity.BaseFragment {

    public static final String TITLE = "Analog I/O";

    private TableLayout mTableLayout;

    private List<AioTableRow> rows = new ArrayList<>();

    private KonashiObserver mAnalogReadObserver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(TITLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_aio, container, false);

        mTableLayout = (TableLayout) view.findViewById(R.id.tableLayout);
        mTableLayout.addView(new HeaderTableRow(getActivity()));
        for (int pinNumber : Utils.AIO_PINS) {
            AioTableRow row = AioTableRow.createWithPinNumber(getActivity(), pinNumber);
            mTableLayout.addView(row);
            rows.add(row);
        }

        mAnalogReadObserver = new KonashiObserver(getActivity()) {
            @Override
            public void onUpdateAnalogValueAio0(int value) {
                rows.get(0).setVoltage(value / 1000.0f);
            }

            @Override
            public void onUpdateAnalogValueAio1(int value) {
                rows.get(1).setVoltage(value / 1000.0f);
            }

            @Override
            public void onUpdateAnalogValueAio2(int value) {
                rows.get(2).setVoltage(value / 1000.0f);
            }
        };

        mKonashiManager.addObserver(mAnalogReadObserver);

        return view;
    }

    @Override
    public void onDestroyView() {
        mKonashiManager.removeObserver(mAnalogReadObserver);
        super.onDestroyView();
    }

    public static final class HeaderTableRow extends TableRow {

        public HeaderTableRow(Context context) {
            super(context);

            addRow("PIN", 1);
            addRow("Voltage", 5);
            addRow("Read", 2);
        }

        private void addRow(String text, float weight) {
            TextView textView = new TextView(getContext());
            textView.setText(text);
            textView.setTypeface(null, Typeface.BOLD);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            addView(textView, Utils.createTableRowLayoutParamwWithWeight(weight));
        }
    }

    public static final class AioTableRow extends TableRow {

        private final TextView mPinTextView;
        private final TextView mVoltageTextView;
        private final Button mReadButton;
        private final KonashiManager mKonashiManager = Konashi.getManager();
        private int mPinNumber;

        public static AioTableRow createWithPinNumber(Context context, final int pinNumber) {
            AioTableRow row = new AioTableRow(context);
            row.setPinNumber(pinNumber);
            return row;
        }

        public AioTableRow(final Context context) {
            super(context);

            setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

            mPinTextView = new TextView(context);
            mPinTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            addView(mPinTextView, Utils.createTableRowLayoutParamwWithWeight(1));

            mVoltageTextView = new TextView(context);
            mVoltageTextView.setGravity(Gravity.CENTER_HORIZONTAL);
            addView(mVoltageTextView, Utils.createTableRowLayoutParamwWithWeight(5));

            mReadButton = new Button(context);
            mReadButton.setText("Read");
            mReadButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    mKonashiManager.analogReadRequest(mPinNumber);
                }
            });
            addView(mReadButton, Utils.createTableRowLayoutParamwWithWeight(2));
        }

        public void setPinNumber(int pinNumber) {
            this.mPinNumber = pinNumber;
            mPinTextView.setText(String.valueOf(pinNumber));
        }

        public void setVoltage(float value) {
            mVoltageTextView.setText(String.format("%.3f V", value));
        }
    }
}
