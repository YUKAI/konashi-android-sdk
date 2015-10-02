package com.uxxu.konashi.test_all_functions;

import android.app.Fragment;
import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.uxxu.konashi.lib.Konashi;
import com.uxxu.konashi.lib.KonashiListener;
import com.uxxu.konashi.lib.KonashiManager;
import com.uxxu.konashi.lib.KonashiUtils;
import com.uxxu.konashi.lib.util.UartUtils;

import org.jdeferred.DoneCallback;
import org.jdeferred.DonePipe;
import org.jdeferred.FailCallback;
import org.jdeferred.Promise;

import info.izumin.android.bletia.BletiaException;

/**
 * Created by kiryu on 7/27/15.
 */
public final class CommunicationFragment extends Fragment {

    public static final String TITLE = "Communication (UART, I2C)";

    private final KonashiManager mKonashiManager = Konashi.getManager();

    private Switch mUartSwitch;
    private Spinner mUartBaudrateSpinner;
    private EditText mUartDataEditText;
    private Button mUartDataSendButton;
    private EditText mUartResultEditText;
    private Button mUartResultClearButton;

    private Switch mI2cSwitch;
    private Spinner mI2cBaudrateSpinner;
    private EditText mI2cDataEditText;
    private Button mI2cDataSendButton;
    private EditText mI2cResultEditText;
    private Button mI2cResultReadButton;
    private Button mI2cResultClearButton;

    private byte[] mValue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(TITLE);
        mKonashiManager.addListener(mKonashiListener);
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
        mKonashiManager.removeListener(mKonashiListener);
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

        mUartSwitch = (Switch) parent.findViewById(R.id.uartSwitch);
        mUartSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
                mKonashiManager.uartWrite(mUartDataEditText.getText().toString().getBytes())
                .then(new DoneCallback<BluetoothGattCharacteristic>() {
                    @Override
                    public void onDone(BluetoothGattCharacteristic result) {
                    }
                });
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
        setEnableUartViews(false);
    }

    private void initI2cViews(View parent) {
        mI2cBaudrateSpinner = (Spinner) parent.findViewById(R.id.i2cBaudrateSpinner);
        mI2cBaudrateSpinner.setSelection(0);
        mI2cBaudrateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                resetI2c();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        mI2cSwitch = (Switch) parent.findViewById(R.id.i2cSwitch);
        mI2cSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
                mKonashiManager.i2cMode(Konashi.I2C_ENABLE_100K);

                mKonashiManager.i2cStartCondition()
                .then(new DonePipe<BluetoothGattCharacteristic, BluetoothGattCharacteristic, BletiaException, Object>() {
                    @Override
                    public Promise<BluetoothGattCharacteristic, BletiaException, Object> pipeDone(BluetoothGattCharacteristic result) {
                        /*String text = mI2cDataEditText.getText().toString().trim();
                        if (Konashi.I2C_DATA_MAX_LENGTH < text.length()) {
                            text = text.substring(0, Konashi.I2C_DATA_MAX_LENGTH);
                        }*/
                        byte[] data0 = {0x31,0x00};
                        return mKonashiManager.i2cWrite(2,data0, (byte) 0x53);
                    }
                })
                .then(new DoneCallback<BluetoothGattCharacteristic>() {
                    @Override
                    public void onDone(BluetoothGattCharacteristic result) {
                        mKonashiManager.i2cStopCondition();
                    }
                })
                .then(new DoneCallback<BluetoothGattCharacteristic>() {
                    @Override
                    public void onDone(BluetoothGattCharacteristic result) {
                        mKonashiManager.i2cStartCondition();
                    }
                })
                .then(new DonePipe<BluetoothGattCharacteristic, BluetoothGattCharacteristic, BletiaException, Object>() {
                    @Override
                    public Promise<BluetoothGattCharacteristic, BletiaException, Object> pipeDone(BluetoothGattCharacteristic result) {
                /*String text = mI2cDataEditText.getText().toString().trim();
                if (Konashi.I2C_DATA_MAX_LENGTH < text.length()) {
                    text = text.substring(0, Konashi.I2C_DATA_MAX_LENGTH);
                }*/
                        byte[] data1 = {0x2d, 0x08};
                        return mKonashiManager.i2cWrite(2, data1, (byte) 0x53);
                    }
                })
                .then(new DoneCallback<BluetoothGattCharacteristic>() {
                    @Override
                    public void onDone(BluetoothGattCharacteristic result) {
                        mKonashiManager.i2cStopCondition();
                    }
                })
                .then(new DoneCallback<BluetoothGattCharacteristic>() {
                    @Override
                    public void onDone(BluetoothGattCharacteristic result) {
                        mKonashiManager.i2cStartCondition();
                    }
                })
                .then(new DonePipe<BluetoothGattCharacteristic, BluetoothGattCharacteristic, BletiaException, Object>() {
                    @Override
                    public Promise<BluetoothGattCharacteristic, BletiaException, Object> pipeDone(BluetoothGattCharacteristic result) {
                        byte[] data2 = {0x32};
                        return mKonashiManager.i2cWrite(1, data2, (byte) 0x53);
                    }
                })
                .then(new DoneCallback<BluetoothGattCharacteristic>() {
                    @Override
                    public void onDone(BluetoothGattCharacteristic result) {
                        mKonashiManager.i2cStopCondition();
                    }
                });
            }
        });

        mI2cResultEditText = (EditText) parent.findViewById(R.id.i2cResultEditText);

        mI2cResultReadButton = (Button) parent.findViewById(R.id.i2cResultReadButton);
        mI2cResultReadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mKonashiManager.i2cStartCondition()
                        .then(new DonePipe<BluetoothGattCharacteristic, byte[], BletiaException, Object>() {
                            @Override
                            public Promise<byte[], BletiaException, Object> pipeDone(BluetoothGattCharacteristic result) {
                                return mKonashiManager.i2cRead(6, (byte) 0x53);
                            }
                        })
                        .then(new DoneCallback<byte[]>() {
                            @Override
                            public void onDone(final byte[] result) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        int x = (((int) result[1]) << 8) | result[0];
                                        int y = (((int) result[3]) << 8) | result[2];
                                        int z = (((int) result[5]) << 8) | result[4];
                                        mI2cResultEditText.setText("x:" + x + " y:" + y + " z:" + z);
                                        mKonashiManager.i2cStopCondition();
                                    }
                                });
                            }
                        });
            }
        });

        mI2cResultClearButton = (Button) parent.findViewById(R.id.i2cResultClearButton);
        mI2cResultClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mI2cResultEditText.setText("");
            }
        });
        setEnableI2cViews(false);
    }

    private void resetUart() {
        if (!mKonashiManager.isReady()) {
            return;
        }
        if (mUartSwitch.isChecked()) {
            mKonashiManager.uartMode(Konashi.UART_ENABLE)
            .then(new DonePipe<BluetoothGattCharacteristic, BluetoothGattCharacteristic, BletiaException, Object>() {
                @Override
                public Promise<BluetoothGattCharacteristic, BletiaException, Object> pipeDone(BluetoothGattCharacteristic result) {
                    int i = mUartBaudrateSpinner.getSelectedItemPosition();
                    String[] labels = getResources().getStringArray(R.array.uart_baudrates_labels);
                    String label = labels[i];
                    return mKonashiManager.uartBaudrate(Utils.uartLabelToValue(label));
                }
            })
            .then(new DoneCallback<BluetoothGattCharacteristic>() {
                @Override
                public void onDone(BluetoothGattCharacteristic result) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setEnableUartViews(true);
                        }
                    });
                }
            });
        } else {
            mKonashiManager.uartMode(Konashi.UART_DISABLE)
            .then(new DoneCallback<BluetoothGattCharacteristic>() {
                @Override
                public void onDone(BluetoothGattCharacteristic result) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setEnableUartViews(false);
                        }
                    });
                }
            });
        }
    }

    private void resetI2c() {
        if (!mKonashiManager.isReady()) {
            return;
        }
        if (mI2cSwitch.isChecked()) {
            int i = mI2cBaudrateSpinner.getSelectedItemPosition();
            if (i == 0) {
                mKonashiManager.i2cMode(Konashi.I2C_ENABLE_100K)
                .then(new DoneCallback<BluetoothGattCharacteristic>() {
                    @Override
                    public void onDone(BluetoothGattCharacteristic result) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setEnableI2cViews(true);
                            }
                        });
                    }
                })
                .fail(new FailCallback<BletiaException>() {
                    @Override
                    public void onFail(BletiaException result) {
                        Log.d("i2cMode", result.getMessage());
                    }
                });
            } else {
                mKonashiManager.i2cMode(Konashi.I2C_ENABLE_400K)
                .then(new DoneCallback<BluetoothGattCharacteristic>() {
                    @Override
                    public void onDone(BluetoothGattCharacteristic result) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setEnableI2cViews(true);
                            }
                        });
                    }
                });
            }
        } else {
            mKonashiManager.i2cMode(Konashi.I2C_DISABLE)
            .then(new DoneCallback<BluetoothGattCharacteristic>() {
                @Override
                public void onDone(BluetoothGattCharacteristic result) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setEnableI2cViews(false);
                        }
                    });
                }
            });
        }
    }

    private void setEnableUartViews(boolean enable) {
        mUartBaudrateSpinner.setEnabled(enable);
        mUartDataEditText.setEnabled(enable);
        mUartDataSendButton.setEnabled(enable);
    }

    private void setEnableI2cViews(boolean enable) {
        mI2cBaudrateSpinner.setEnabled(enable);
        mI2cDataEditText.setEnabled(enable);
        mI2cDataSendButton.setEnabled(enable);
        mI2cResultReadButton.setEnabled(enable);
    }

    private final KonashiListener mKonashiListener = new KonashiListener() {
        @Override public void onConnect(KonashiManager manager) {}
        @Override public void onDisconnect(KonashiManager manager) {}
        @Override public void onError(KonashiManager manager, BletiaException e) {}
        @Override public void onUpdatePioOutput(KonashiManager manager, int value) {}

        @Override
        public void onUpdateUartRx(KonashiManager manager, byte[] value) {
            mValue = value;

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mUartResultEditText.append(new String(mValue));
                }
            });
        }

        @Override public void onUpdateBatteryLevel(KonashiManager manager, int level) {}
    };
}