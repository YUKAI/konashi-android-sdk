package com.uxxu.konashi.test_all_functions;

import android.app.Fragment;
import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Bundle;
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

import org.jdeferred.DoneCallback;
import org.jdeferred.DonePipe;
import org.jdeferred.FailCallback;
import org.jdeferred.Promise;

import java.util.Arrays;

import info.izumin.android.bletia.BletiaException;

/**
 * Created by e10dokup on 12/16/15
 */
public final class SpiFragment extends Fragment {
    public static final String TITLE = "SPI";

    private KonashiManager mKonashiManager;

    private Switch mSpiSwitch;
    private Spinner mSpiSpeedSpinner;
    private EditText mSpiDataEditText;
    private Button mSpiDataSendButton;
    private EditText mSpiResultEditText;
    private Button mSpiResultClearButton;

    private byte[] mValue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(TITLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_spi, container, false);

        initSpiViews(view);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mKonashiManager = Konashi.getManager();
        mKonashiManager.addListener(mKonashiListener);
    }

    @Override
    public void onDestroy() {
        mKonashiManager.removeListener(mKonashiListener);
        super.onDestroy();
    }

    private void initSpiViews(final View parent) {
        mSpiSpeedSpinner = (Spinner) parent.findViewById(R.id.spiSpeedSpinner);
        mSpiSpeedSpinner.setSelection(1);
        mSpiSpeedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                resetSpi();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        mSpiSwitch = (Switch) parent.findViewById(R.id.spiSwitch);
        mSpiSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                resetSpi();
            }
        });

        mSpiDataEditText = (EditText) parent.findViewById(R.id.spiDataEditText);

        mSpiDataSendButton = (Button) parent.findViewById(R.id.spiDataSendButton);
        mSpiDataSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mKonashiManager.digitalWrite(Konashi.PIO2, Konashi.LOW)
                        .then(new DonePipe<BluetoothGattCharacteristic, BluetoothGattCharacteristic, BletiaException, Void>() {
                            @Override
                            public Promise<BluetoothGattCharacteristic, BletiaException, Void> pipeDone(BluetoothGattCharacteristic result) {
                                return mKonashiManager.spiWrite(mSpiDataEditText.getText().toString().getBytes());
                            }
                        })
                        .then(new DonePipe<BluetoothGattCharacteristic, BluetoothGattCharacteristic, BletiaException, Void>() {
                            @Override
                            public Promise<BluetoothGattCharacteristic, BletiaException, Void> pipeDone(BluetoothGattCharacteristic result) {
                                return mKonashiManager.digitalWrite(Konashi.PIO2, Konashi.HIGH);
                            }
                        })
                        .then(new DonePipe<BluetoothGattCharacteristic, BluetoothGattCharacteristic, BletiaException, Void>() {
                            @Override
                            public Promise<BluetoothGattCharacteristic, BletiaException, Void> pipeDone(BluetoothGattCharacteristic result) {
                                return mKonashiManager.spiRead();
                            }
                        })
                        .then(new DoneCallback<BluetoothGattCharacteristic>() {
                            @Override
                            public void onDone(BluetoothGattCharacteristic result) {
                                byte data[] = new byte[result.getValue().length];
                                for (int i = 0; i < result.getValue().length; i++) {
                                    data[i] = (byte) ((result.getValue()[i] & 0xff) );
                                }
                                mSpiResultEditText.setText(Arrays.toString(data));
                            }
                        })
                        .fail(new FailCallback<BletiaException>() {
                            @Override
                            public void onFail(BletiaException result) {
                                Toast.makeText(getActivity(), result.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        mSpiResultEditText = (EditText) parent.findViewById(R.id.spiResultEditText);

        mSpiResultClearButton = (Button) parent.findViewById(R.id.spiResultClearButton);
        mSpiResultClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSpiResultEditText.setText("");
            }
        });
        setEnableSpiViews(false);
    }

    private void resetSpi() {
        if (!mKonashiManager.isReady()) {
            return;
        }
        if (mSpiSwitch.isChecked()) {
            int i = mSpiSpeedSpinner.getSelectedItemPosition();
            String[] labels = getResources().getStringArray(R.array.spi_speeds_labels);
            String label = labels[i];
            int speed = Utils.spiLabelToValue(label);
            mKonashiManager.spiConfig(Konashi.SPI_MODE_ENABLE_CPOL0_CPHA0,
                    Konashi.SPI_BIT_ORDER_LITTLE_ENDIAN, speed)
                    .then(new DonePipe<BluetoothGattCharacteristic, BluetoothGattCharacteristic, BletiaException, Void>() {
                        @Override
                        public Promise<BluetoothGattCharacteristic, BletiaException, Void> pipeDone(BluetoothGattCharacteristic result) {
                            return mKonashiManager.pinMode(Konashi.PIO2, Konashi.OUTPUT);
                        }
                    })
                    .then(new DonePipe<BluetoothGattCharacteristic, BluetoothGattCharacteristic, BletiaException, Void>() {
                        @Override
                        public Promise<BluetoothGattCharacteristic, BletiaException, Void> pipeDone(BluetoothGattCharacteristic result) {
                            setEnableSpiViews(true);
                            return mKonashiManager.digitalWrite(Konashi.PIO2, Konashi.HIGH);
                        }
                    })
                    .fail(new FailCallback<BletiaException>() {
                        @Override
                        public void onFail(BletiaException result) {
                            Toast.makeText(getActivity(), result.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            mKonashiManager.spiConfig(Konashi.SPI_MODE_DISABLE, Konashi.SPI_BIT_ORDER_LITTLE_ENDIAN, Konashi.SPI_SPEED_1M)
            .then(new DoneCallback<BluetoothGattCharacteristic>() {
                @Override
                public void onDone(BluetoothGattCharacteristic result) {
                    setEnableSpiViews(false);
                }
            });
        }
    }


    private void setEnableSpiViews(boolean enable) {
        mSpiSpeedSpinner.setEnabled(enable);
        mSpiDataEditText.setEnabled(enable);
        mSpiDataSendButton.setEnabled(enable);
    }

    private final KonashiListener mKonashiListener = new KonashiListener() {
        @Override public void onConnect(KonashiManager manager) {}
        @Override public void onDisconnect(KonashiManager manager) {}
        @Override public void onFindNoDevice(KonashiManager manager) {}
        @Override public void onConnectOtherDevice(KonashiManager manager) {}
        @Override public void onError(KonashiManager manager, BletiaException e) {}
        @Override public void onUpdatePioOutput(KonashiManager manager, int value) {}
        @Override public void onUpdateUartRx(KonashiManager manager, byte[] value) {}

        @Override
        public void onUpdateSpiMiso(KonashiManager manager, byte[] value) {
            mValue = value;
            mSpiResultEditText.append(new String(mValue));
        }

        @Override public void onUpdateBatteryLevel(KonashiManager manager, int level) {}
    };
}
