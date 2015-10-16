package com.uxxu.konashi.test_all_functions;

import android.app.Fragment;
import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.uxxu.konashi.lib.Konashi;
import com.uxxu.konashi.lib.KonashiManager;

import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;
import org.jdeferred.Promise;

import info.izumin.android.bletia.BletiaException;

/**
 * Reference:
 * http://jsdo.it/p.esrd.demo/hCBN
 * Created by kiryu on 7/27/15.
 */
public final class UzukiFragment extends Fragment {

    public static final String TITLE = "Uzuki";

    private KonashiManager mKonashiManager;
    private Uzuki mUzuki;
    private Button mSetupAdxl345Button;
    private TextView mAccelerometerResultTextView;
    private Button mReadAccelerometerButton;
    private TextView mTemperatureResultsTextView;
    private Button mReadTemperatureButton;

    private Handler accelerometerHandler = new Handler();
    private Runnable accelerometerRunnable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(TITLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_uzuki, container, false);

        mAccelerometerResultTextView = (TextView) view.findViewById(R.id.accelerometerResultText);

        mSetupAdxl345Button = (Button) view.findViewById(R.id.setupAdxl345Button);
        mSetupAdxl345Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mUzuki.setupAdxl345()
                            .done(new DoneCallback<BluetoothGattCharacteristic>() {
                                @Override
                                public void onDone(BluetoothGattCharacteristic result) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getActivity(), "Finished to setup ADXL345", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            })
                            .fail(new FailCallback<BletiaException>() {
                                @Override
                                public void onFail(BletiaException result) {
                                    final String message = "Failed to setup adx345: " + result.getMessage();
                                    Log.d(TITLE, message + result, result);
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                } catch (Exception e) {
                    Log.d(TITLE, "error", e);
                }
            }
        });

        mReadAccelerometerButton = (Button) view.findViewById(R.id.readAccelerometerButton);
        mReadAccelerometerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (accelerometerRunnable == null) {
                    accelerometerRunnable = new Runnable() {
                        @Override
                        public void run() {
                            readAccelerometer();
                            if (getView() != null && getView().getVisibility() == View.VISIBLE) {
                                accelerometerHandler.postDelayed(this, 300);
                            }
                        }
                    };
                }
                accelerometerHandler.postDelayed(accelerometerRunnable, 300);
            }
        });

        mReadTemperatureButton = (Button) view.findViewById(R.id.readTemperatureButton);
        mTemperatureResultsTextView = (TextView) view.findViewById(R.id.temperatureResultText);
        mReadTemperatureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // https://github.com/mpression/UzukiSensorShield/blob/Uzuki2.1/konashiSensorShield/konashiSensorShield/FirstViewController.m#L218
                mUzuki.i2cReadTemperature().done(new DoneCallback<byte[]>() {
                    @Override
                    public void onDone(byte[] result) {
                        double temperature = (double) (result[0] << 8 ^ result[1]) * 175.72 / 65536.0 - 46.85;
                        mTemperatureResultsTextView.setText(String.format("temp: %.1f", temperature));
                    }
                });
            }
        });

        return view;
    }

    private void readAccelerometer() {
        // https://github.com/mpression/UzukiSensorShield/blob/Uzuki2.1/konashiSensorShield/konashiSensorShield/FirstViewController.m#L339-L341
        mUzuki.i2cReadAccelerometer()
                .done(new DoneCallback<byte[]>() {
                    @Override
                    public void onDone(byte[] result) {
                        try {
                            int y = (result[3] << 8 ^ result[2]) / 256;
                            int z = (result[5] << 8 ^ result[4]) / 256;
                            int x = (result[1] << 8 ^ result[0]) / 256;
                            final String text = String.format("x: %d, y: %d, z: %d", x, y, z);
                            Log.d(TITLE, text);
                            if (getActivity() == null) {
                                return;
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAccelerometerResultTextView.setText(text);
                                }
                            });
                        } catch (Exception e) {
                            Log.d(TITLE, "Error", e);
                        }
                    }
                })
                .fail(new FailCallback<BletiaException>() {
                    @Override
                    public void onFail(BletiaException result) {
                        final String message = "Failed to read accelerometer: " + result.getMessage();
                        Log.d(TITLE, message, result);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mKonashiManager = Konashi.getManager();
        mUzuki = new Uzuki(mKonashiManager);

    }

    private final class Uzuki {

        private static final byte ADXL345_ADDR = (byte) 0x1D;
        private static final byte TEMPERATURE_ADDR = (byte) 0x40;

        private final KonashiManager mKonashiManager;

        public Uzuki(KonashiManager mKonashiManager) {
            this.mKonashiManager = mKonashiManager;
        }

        private Promise<BluetoothGattCharacteristic, BletiaException, Void> setupAdxl345() throws InterruptedException {
            return mKonashiManager
                    .i2cMode(Konashi.I2C_ENABLE_100K)
                    .then(mKonashiManager.<BluetoothGattCharacteristic>i2cStartConditionPipe())
                    .then(mKonashiManager.<BluetoothGattCharacteristic>i2cWritePipe(2, new byte[]{0x31, 0x0b}, ADXL345_ADDR))
                    .then(mKonashiManager.<BluetoothGattCharacteristic>i2cStopConditionPipe())
                    .then(mKonashiManager.<BluetoothGattCharacteristic>i2cStartConditionPipe())
                    .then(mKonashiManager.<BluetoothGattCharacteristic>i2cWritePipe(2, new byte[]{0x2d, 0x08}, ADXL345_ADDR))
                    .then(mKonashiManager.<BluetoothGattCharacteristic>i2cStopConditionPipe());
        }

        private Promise<byte[], BletiaException, Void> i2cReadAccelerometer() {
            return mKonashiManager
                    .i2cStartCondition()
                    .then(mKonashiManager.<BluetoothGattCharacteristic>i2cWritePipe(1, new byte[]{0x32}, ADXL345_ADDR))
                    .then(mKonashiManager.<BluetoothGattCharacteristic>i2cRestartConditionPipe())
                    .then(mKonashiManager.<BluetoothGattCharacteristic>i2cReadPipe(6, ADXL345_ADDR));
        }

        private Promise<byte[], BletiaException, Void> i2cReadTemperature() {
            return mKonashiManager
                    .i2cMode(Konashi.I2C_ENABLE_100K)
                    .then(mKonashiManager.<BluetoothGattCharacteristic>i2cStartConditionPipe())
                    .then(mKonashiManager.<BluetoothGattCharacteristic>i2cWritePipe(1, new byte[]{(byte) 0xE5}, TEMPERATURE_ADDR))
                    .then(mKonashiManager.<BluetoothGattCharacteristic>i2cRestartConditionPipe())
                    .then(mKonashiManager.<BluetoothGattCharacteristic>i2cReadPipe(3, TEMPERATURE_ADDR));
        }

    }
}
