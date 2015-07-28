package com.uxxu.konashi.test_all_functions;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.uxxu.konashi.lib.Konashi;
import com.uxxu.konashi.lib.KonashiManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kiryu on 7/27/15.
 */
public class PwmFragment extends MainActivity.BaseFragment {

    public static final String TITLE = "PWM";

    private EditText mOptionPinEditText;
    private EditText mOptionPeriodEditText;
    private EditText mOptionDutyEditText;
    private Button mSubmitButton;

    private TableLayout mTableLayout;
    private List<PwmTableRow> rows = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(TITLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pwm, container, false);

        mOptionPinEditText = (EditText) view.findViewById(R.id.optionPinEditText);
        mOptionPeriodEditText = (EditText) view.findViewById(R.id.optionPeriodEditText);
        mOptionDutyEditText = (EditText) view.findViewById(R.id.optionDutyEditText);
        mSubmitButton = (Button) view.findViewById(R.id.submitButton);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pinNumber = Integer.valueOf(mOptionPinEditText.getText().toString());
                if (pinNumber < 0 || Utils.PIO_PINS.length <= pinNumber) {
                    return;
                }
                rows.get(pinNumber).setValues(
                        Integer.valueOf(mOptionPeriodEditText.getText().toString()),
                        Integer.valueOf(mOptionDutyEditText.getText().toString()));
            }
        });

        mTableLayout = (TableLayout) view.findViewById(R.id.tableLayout);
        mTableLayout.addView(new HeaderTableRow(getActivity()));
        for (int pinNumber : Utils.PIO_PINS) {
            PwmTableRow row = PwmTableRow.createWithPinNumber(getActivity(), pinNumber);
            mTableLayout.addView(row);
            rows.add(row);
        }
        return view;
    }

    public static final class HeaderTableRow extends TableRow {

        public HeaderTableRow(Context context) {
            super(context);

            addRow("PIN", 1);
            addRow("PWN", 1);
            addRow("Duty", 6);
        }

        private void addRow(String text, float weight) {
            TextView textView = new TextView(getContext());
            textView.setText(text);
            textView.setTypeface(null, Typeface.BOLD);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            addView(textView, Utils.createTableRowLayoutParamwWithWeight(weight));
        }
    }

    public static final class PwmTableRow extends TableRow {

        private final TextView mPinTextView;
        private final ToggleButton mPwmToggleButton;
        private final SeekBar mDutySeekBar;
        private int mPinNumber;

        public static PwmTableRow createWithPinNumber(Context context, final int pinNumber) {
            PwmTableRow row = new PwmTableRow(context);
            row.setPinNumber(pinNumber);
            return row;
        }

        public PwmTableRow(final Context context) {
            super(context);

            setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

            mPinTextView = new TextView(context);
            mPinTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            addView(mPinTextView, Utils.createTableRowLayoutParamwWithWeight(1));

            mPwmToggleButton = new ToggleButton(context);
            mPwmToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    final int pwmMode = b ? Konashi.PWM_ENABLE_LED_MODE : Konashi.PWM_DISABLE;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Utils.sleep();
                            Konashi.getManager().pwmMode(mPinNumber, pwmMode);
                            if (pwmMode == Konashi.PWM_ENABLE_LED_MODE) {
                                Utils.sleep();
                                Konashi.getManager().pwmLedDrive(mPinNumber, mDutySeekBar.getProgress());
                            }
                        }
                    }).start();
                    mDutySeekBar.setEnabled(b);
                }
            });
            addView(mPwmToggleButton, Utils.createTableRowLayoutParamwWithWeight(1));

            mDutySeekBar = new SeekBar(context);
            mDutySeekBar.setMax(100);
            mDutySeekBar.setEnabled(false);
            mDutySeekBar.setProgress(mDutySeekBar.getMax() / 2);
            mDutySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    final int drive = i;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Konashi.getManager().pwmLedDrive(mPinNumber, drive);
                        }
                    }).start();
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(final SeekBar seekBar) {
                }
            });
            addView(mDutySeekBar, Utils.createTableRowLayoutParamwWithWeight(6));
        }

        public void setPinNumber(int pinNumber) {
            this.mPinNumber = pinNumber;
            mPinTextView.setText(String.valueOf(pinNumber));
        }

        public void setValues(final int period, final int duty) {
            mPwmToggleButton.setChecked(true);
            mDutySeekBar.setProgress(duty);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    KonashiManager manager = Konashi.getManager();
                    Utils.sleep();
                    manager.pwmPeriod(mPinNumber, period);
                    Utils.sleep();
                    manager.pwmLedDrive(mPinNumber, Konashi.PWM_ENABLE_LED_MODE);
                    Utils.sleep();
                    manager.pwmLedDrive(mPinNumber, duty);
                }
            }).start();
        }
    }
}
