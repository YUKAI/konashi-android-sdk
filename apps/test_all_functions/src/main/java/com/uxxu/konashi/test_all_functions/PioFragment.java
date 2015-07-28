package com.uxxu.konashi.test_all_functions;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.uxxu.konashi.lib.Konashi;

/**
 * Created by kiryu on 7/27/15.
 */
public class PioFragment extends MainActivity.BaseFragment {

    public static final String TITLE = "PIO";

    private static final int[] PIO_LIST = new int[]{Konashi.PIO0, Konashi.PIO1, Konashi.PIO2, Konashi.PIO3, Konashi.PIO4, Konashi.PIO5};

    private TableLayout mTableLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(TITLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pio, container, false);
        mTableLayout = (TableLayout) view.findViewById(R.id.tableLayout);
        mTableLayout.addView(new HeaderTableRow(getActivity()));
        for (int pinNumber : PIO_LIST) {
            mTableLayout.addView(PioTableRow.createWithPinNumber(getActivity(), pinNumber));
        }
        return view;
    }

    public static final class HeaderTableRow extends TableRow {

        public HeaderTableRow(Context context) {
            super(context);

            addRow("PIN");
            addRow("Mode");
            addRow("Output");
            addRow("Input");
        }

        private void addRow(String text) {
            TextView textView = new TextView(getContext());
            textView.setText(text);
            textView.setTypeface(null, Typeface.BOLD);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            addView(textView, Utils.createTableRowLayoutParamwWithWeight(1));
        }
    }

    public static final class PioTableRow extends TableRow {

        private final TextView mPositionTextView;
        private final ToggleButton mIoToggleButton;
        private final ToggleButton mOutputToggleButton;
        private final ToggleButton mInputToggleButton;
        private int mPinNumber;

        public static PioTableRow createWithPinNumber(Context context, final int pinNumber) {
            PioTableRow row = new PioTableRow(context);
            row.setPinNumber(pinNumber);
            return row;
        }

        public PioTableRow(Context context) {
            super(context);

            setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

            mPositionTextView = new TextView(context);
            mPositionTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            addView(mPositionTextView, Utils.createTableRowLayoutParamwWithWeight(3));

            mIoToggleButton = new ToggleButton(context);
            mIoToggleButton.setTextOff("input");
            mIoToggleButton.setTextOn("output");
            mIoToggleButton.setText(mIoToggleButton.getTextOff());
            mIoToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    int mode = b ? Konashi.OUTPUT : Konashi.INPUT;
                    switch (mode) {
                        case Konashi.OUTPUT:
                            mInputToggleButton.setEnabled(false);
                            mOutputToggleButton.setEnabled(true);
                            break;
                        case Konashi.INPUT:
                            mInputToggleButton.setEnabled(true);
                            mOutputToggleButton.setEnabled(false);
                            break;
                    }
                    Konashi.getManager().pinMode(mPinNumber, mode);
                }
            });
            addView(mIoToggleButton, Utils.createTableRowLayoutParamwWithWeight(2));

            mOutputToggleButton = new ToggleButton(context);
            mOutputToggleButton.setTextOff("low");
            mOutputToggleButton.setTextOn("high");
            mOutputToggleButton.setText(mOutputToggleButton.getTextOff());
            mOutputToggleButton.setEnabled(false);
            mOutputToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    Konashi.getManager().digitalWrite(mPinNumber, b ? Konashi.HIGH : Konashi.LOW);
                }
            });
            addView(mOutputToggleButton, Utils.createTableRowLayoutParamwWithWeight(3));

            mInputToggleButton = new ToggleButton(context);
            mInputToggleButton.setTextOff("low");
            mInputToggleButton.setTextOn("high");
            mInputToggleButton.setText(mInputToggleButton.getTextOff());
            addView(mInputToggleButton, Utils.createTableRowLayoutParamwWithWeight(3));
        }

        public void setPinNumber(int pinNumber) {
            this.mPinNumber = pinNumber;
            mPositionTextView.setText(String.valueOf(pinNumber));
        }
    }
}
