package com.uxxu.konashi.test_all_functions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ToggleButton;

import com.uxxu.konashi.lib.Konashi;
import com.uxxu.konashi.lib.KonashiObserver;

/**
 * Created by kiryu on 7/27/15.
 */
public final class CommunicationFragment extends MainActivity.BaseFragment {

    public static final String TITLE = "Communication (UART, I2C)";

    private ToggleButton mUartToggleButton;
    private Spinner mUartBaudrateSpinner;
    private EditText mUartDataEditText;
    private Button mUartDataSendButton;
    private EditText mUartResultEditText;
    private Button mUartResultClearButton;

    private ToggleButton mI2cToggleButton;
    private Spinner mI2cBaudrateSpinner;
    private EditText mI2cDataEditText;
    private Button mI2cDataSendButton;
    private EditText mI2cResultEditText;
    private Button mI2cResultReadButton;
    private Button mI2cResultClearButton;

    private KonashiObserver mCommunicationObserver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(TITLE);

        mCommunicationObserver = new KonashiObserver(getActivity()) {
            @Override
            public void onCompleteUartRx(byte[] data) {
                mUartResultEditText.append(new String(data));
            }
        };
        mKonashiManager.addObserver(mCommunicationObserver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_communication, container, false);

        initUartViews(view);
        initI2cViews(view);

        return view;
    }

    @Override
    public void onDestroy() {
        mKonashiManager.removeObserver(mCommunicationObserver);
        super.onDestroy();
    }

    private void initUartViews(final View parent) {
        mUartBaudrateSpinner = (Spinner) parent.findViewById(R.id.uartBaudrateSpinner);
        mUartBaudrateSpinner.setSelection(1);
        mUartBaudrateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                resetUart();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        mUartToggleButton = (ToggleButton) parent.findViewById(R.id.uartToggleButton);
        mUartToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                resetUart();
            }
        });

        mUartDataEditText = (EditText) parent.findViewById(R.id.uartDataEditText);

        mUartDataSendButton = (Button) parent.findViewById(R.id.uartDataSendButton);
        mUartDataSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mKonashiManager.uartWrite(mUartDataEditText.getText().toString().getBytes());
            }
        });

        mUartResultEditText = (EditText) parent.findViewById(R.id.uartResultEditText);

        mUartResultClearButton = (Button) parent.findViewById(R.id.uartResultClearButton);
        mUartResultClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUartResultEditText.setText("");
            }
        });
    }

    private void initI2cViews(View parent) {
        mI2cBaudrateSpinner = (Spinner) parent.findViewById(R.id.i2cBaudrateSpinner);
        mI2cBaudrateSpinner.setSelection(1);
        mI2cBaudrateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                resetI2c();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        mI2cToggleButton = (ToggleButton) parent.findViewById(R.id.i2cToggleButton);
        mI2cToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                resetI2c();
            }
        });

        mI2cDataEditText = (EditText) parent.findViewById(R.id.i2cDataEditText);

        mI2cDataSendButton = (Button) parent.findViewById(R.id.i2cDataSendButton);
        mI2cDataSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.sleep();
                mKonashiManager.i2cStartCondition();
                Utils.sleep();
                String text = mI2cDataEditText.getText().toString().trim();
                if (Konashi.I2C_DATA_MAX_LENGTH < text.length()) {
                    text = text.substring(0, Konashi.I2C_DATA_MAX_LENGTH);
                }
                mKonashiManager.i2cWrite(
                        text.length(),
                        text.getBytes(),
                        (byte) 0x1F);
                Utils.sleep();
                mKonashiManager.i2cStopCondition();
                Utils.sleep();
            }
        });

        mI2cResultEditText = (EditText) parent.findViewById(R.id.i2cResultEditText);

        mI2cResultReadButton = (Button) parent.findViewById(R.id.i2cResultReadButton);
        mI2cResultReadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mKonashiManager.i2cStartCondition();
                Utils.sleep();
                mKonashiManager.i2cReadRequest(Konashi.I2C_DATA_MAX_LENGTH, (byte) 0x1F);
                Utils.sleep();
                mKonashiManager.i2cStopCondition();
            }
        });

        mI2cResultClearButton = (Button) parent.findViewById(R.id.i2cResultClearButton);
        mI2cResultClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mI2cResultEditText.setText("");
            }
        });
    }

    private void resetUart() {
        if (mUartToggleButton.isChecked()) {
            mKonashiManager.uartMode(Konashi.UART_ENABLE);
            Utils.sleep();
            int i = mUartBaudrateSpinner.getSelectedItemPosition();
            int[] values = getResources().getIntArray(R.array.uart_baudrates_values);
            int value = values[i];
            mKonashiManager.uartBaudrate(value);

            mUartBaudrateSpinner.setEnabled(true);
            mUartDataEditText.setEnabled(true);
            mUartDataSendButton.setEnabled(true);
            mUartResultClearButton.setEnabled(true);
        } else {
            mKonashiManager.uartMode(Konashi.UART_DISABLE);

            mUartBaudrateSpinner.setEnabled(false);
            mUartDataEditText.setEnabled(false);
            mUartDataSendButton.setEnabled(false);
            mUartResultClearButton.setEnabled(false);
        }
    }

    private void resetI2c() {
        if (mI2cToggleButton.isChecked()) {
            int i = mI2cBaudrateSpinner.getSelectedItemPosition();
            if (i == 0) {
                mKonashiManager.i2cMode(Konashi.I2C_ENABLE_100K);
            } else {
                mKonashiManager.i2cMode(Konashi.I2C_ENABLE_400K);
            }

            mI2cBaudrateSpinner.setEnabled(true);
            mI2cDataEditText.setEnabled(true);
            mI2cDataSendButton.setEnabled(true);
            mI2cResultReadButton.setEnabled(true);
            mI2cResultClearButton.setEnabled(true);
        } else {
            mKonashiManager.i2cMode(Konashi.I2C_DISABLE);

            mI2cBaudrateSpinner.setEnabled(false);
            mI2cDataEditText.setEnabled(false);
            mI2cDataSendButton.setEnabled(false);
            mI2cResultReadButton.setEnabled(false);
            mI2cResultClearButton.setEnabled(false);
        }
    }
}
