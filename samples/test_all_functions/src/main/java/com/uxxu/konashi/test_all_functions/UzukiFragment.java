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
import org.jdeferred.DonePipe;
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
                                accelerometerHandler.postDelayed(this, 200);
                            }
                        }
                    };
                }
                accelerometerHandler.postDelayed(accelerometerRunnable, 200);
            }
        });

        mReadTemperatureButton = (Button) view.findViewById(R.id.readTemperatureButton);
        mTemperatureResultsTextView = (TextView) view.findViewById(R.id.temperatureResultText);
        mReadTemperatureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // https://github.com/mpression/UzukiSensorShield/blob/Uzuki2.1/konashiSensorShield/konashiSensorShield/FirstViewController.m#L218
                // 5.1.2. Measuring Temperature
                // https://www.silabs.com/Support%20Documents/TechnicalDocs/Si7013-A20.pdf
                mUzuki.i2cReadTemperature()
                        .then(new DonePipe<byte[], Void, BletiaException, Void>() {
                            @Override
                            public Promise<Void, BletiaException, Void> pipeDone(byte[] result) {
                                double temperature = (double) (result[0] << 8 ^ result[1]) * 175.72 / 65536.0 - 46.85;
                                mTemperatureResultsTextView.setText(String.format("temp: %.1f", temperature));
                                return null;
                            }
                        })
                        .then(mKonashiManager.<Void>i2cStopConditionPipe())
                        .fail(new FailCallback<BletiaException>() {
                            @Override
                            public void onFail(final BletiaException result) {
                                final String message = "Failed to read temperature: " + result.getMessage();
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
        });

        return view;
    }

    private void readAccelerometer() {
        // https://github.com/mpression/UzukiSensorShield/blob/Uzuki2.1/konashiSensorShield/konashiSensorShield/FirstViewController.m#L339-L341
        // http://www.analog.com/media/en/technical-documentation/data-sheets/ADXL345.pdf
        mUzuki.i2cReadAccelerometer()
                .then(new DonePipe<byte[], Void, BletiaException, Void>() {
                    @Override
                    public Promise<Void, BletiaException, Void> pipeDone(byte[] result) {
                        try {
                            int y = (result[3] << 8 ^ result[2]) / 256;
                            int z = (result[5] << 8 ^ result[4]) / 256;
                            int x = (result[1] << 8 ^ result[0]) / 256;
                            final String text = String.format("x: %d, y: %d, z: %d", x, y, z);
                            Log.d(TITLE, text);
                            if (getActivity() == null) {
                                return null;
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
                        return null;
                    }
                })
                .then(mKonashiManager.<Void>i2cStopConditionPipe())
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
        private static final byte ADXL345_DATA_FORMAT_REGISTER = (byte) 0x31;
        private static final byte ADXL345_POWER_CONTROL_REGISTER = (byte) 0x2D;
        private static final byte ADXL345_THRESHOLD_ACTIVE = (byte) 0x24;
        private static final byte ADXL345_ACTIVE_INACTIVE_CONTROL_REGISTER = (byte) 0x27;
        private static final byte TEMPERATURE_ADDR = (byte) 0x40;

        private final KonashiManager mKonashiManager;

        public Uzuki(KonashiManager mKonashiManager) {
            this.mKonashiManager = mKonashiManager;
        }

        // https://github.com/mpression/UzukiSensorShield/blob/Uzuki2.1/konashiSensorShield/Sensors/Adxl345.m
        private Promise<BluetoothGattCharacteristic, BletiaException, Void> setupAdxl345() throws InterruptedException {
            return mKonashiManager
                    .i2cMode(Konashi.I2C_ENABLE_100K)
                    .then(mKonashiManager.<BluetoothGattCharacteristic>i2cStartConditionPipe())
                    .then(mKonashiManager.<BluetoothGattCharacteristic>i2cWritePipe(2, new byte[]{ADXL345_DATA_FORMAT_REGISTER, 0x0B}, ADXL345_ADDR))
                    .then(mKonashiManager.<BluetoothGattCharacteristic>i2cStopConditionPipe())
                    .then(mKonashiManager.<BluetoothGattCharacteristic>i2cStartConditionPipe())
                    .then(mKonashiManager.<BluetoothGattCharacteristic>i2cWritePipe(2, new byte[]{ADXL345_POWER_CONTROL_REGISTER, 0x08}, ADXL345_ADDR))
                    .then(mKonashiManager.<BluetoothGattCharacteristic>i2cStopConditionPipe())
                    .then(mKonashiManager.<BluetoothGattCharacteristic>i2cStartConditionPipe())
                    .then(mKonashiManager.<BluetoothGattCharacteristic>i2cWritePipe(2, new byte[]{ADXL345_THRESHOLD_ACTIVE, 0x20}, ADXL345_ADDR))
                    .then(mKonashiManager.<BluetoothGattCharacteristic>i2cStopConditionPipe())
                    .then(mKonashiManager.<BluetoothGattCharacteristic>i2cStartConditionPipe())
                    .then(mKonashiManager.<BluetoothGattCharacteristic>i2cWritePipe(2, new byte[]{ADXL345_ACTIVE_INACTIVE_CONTROL_REGISTER, (byte) 0xF0}, ADXL345_ADDR))
                    .then(mKonashiManager.<BluetoothGattCharacteristic>i2cStopConditionPipe());
        }

        private Promise<byte[], BletiaException, Void> i2cReadAccelerometer() {
            return mKonashiManager
                    .i2cStartCondition()
                    .then(mKonashiManager.<BluetoothGattCharacteristic>i2cWritePipe(1, new byte[]{0x32}, ADXL345_ADDR))
                    .then(mKonashiManager.<BluetoothGattCharacteristic>i2cRestartConditionPipe())
                    .then(mKonashiManager.<BluetoothGattCharacteristic>i2cReadPipe(6, ADXL345_ADDR));
        }

        // https://github.com/mpression/UzukiSensorShield/blob/Uzuki2.1/konashiSensorShield/konashiSensorShield/Si7013.m
        private Promise<byte[], BletiaException, Void> i2cReadTemperature() {
            return mKonashiManager
                    .i2cMode(Konashi.I2C_ENABLE_100K)
                    .then(mKonashiManager.<BluetoothGattCharacteristic>i2cStartConditionPipe())
                    .then(mKonashiManager.<BluetoothGattCharacteristic>i2cWritePipe(1, new byte[]{(byte) 0xE5}, TEMPERATURE_ADDR))
                    .then(mKonashiManager.<BluetoothGattCharacteristic>i2cStopConditionPipe())
                    .then(mKonashiManager.<BluetoothGattCharacteristic>i2cStartConditionPipe())
                    .then(mKonashiManager.<BluetoothGattCharacteristic>i2cReadPipe(3, TEMPERATURE_ADDR));
        }
    }
}
