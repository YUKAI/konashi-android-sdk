package com.uxxu.konashi.sample.piosample;

import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.uxxu.konashi.lib.Konashi;
import com.uxxu.konashi.lib.KonashiErrorReason;
import com.uxxu.konashi.lib.KonashiManager;
import com.uxxu.konashi.lib.listeners.KonashiDigitalListener;

import org.jdeferred.DoneCallback;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private static final int[] PIO_PINS = new int[] {
            Konashi.PIO0, Konashi.PIO1, Konashi.PIO2,
            Konashi.PIO3, Konashi.PIO4, Konashi.PIO5
    };

    private static final int[] PIO_MODE_BUTTON_IDS = new int[] {
            R.id.btn_mode_pio0, R.id.btn_mode_pio1, R.id.btn_mode_pio2,
            R.id.btn_mode_pio3, R.id.btn_mode_pio4, R.id.btn_mode_pio5
    };

    private static final int[] PIO_OUTPUT_BUTTON_IDS = new int[] {
            R.id.btn_output_pio0, R.id.btn_output_pio1, R.id.btn_output_pio2,
            R.id.btn_output_pio3, R.id.btn_output_pio4, R.id.btn_output_pio5
    };

    private static final int[] PIO_INPUT_TEXT_IDS = new int[] {
            R.id.text_input_pio0, R.id.text_input_pio1, R.id.text_input_pio2,
            R.id.text_input_pio3, R.id.text_input_pio4, R.id.text_input_pio5
    };

    private static final int[] PIO_PULLUP_CHECKBOX_IDS = new int[] {
            R.id.checkbox_pullup_pio0, R.id.checkbox_pullup_pio1, R.id.checkbox_pullup_pio2,
            R.id.checkbox_pullup_pio3, R.id.checkbox_pullup_pio4, R.id.checkbox_pullup_pio5
    };

    private Callback mCallback;
    private ViewHolder[] mViewHolders = new ViewHolder[PIO_PINS.length];

    public MainActivityFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof Callback) {
            mCallback = (Callback) activity;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        for (int i = 0; i < PIO_PINS.length; i++) {
            mViewHolders[i] = new ViewHolder(
                    PIO_PINS[i],
                    mCallback,
                    (ToggleButton) v.findViewById(PIO_MODE_BUTTON_IDS[i]),
                    (ToggleButton) v.findViewById(PIO_OUTPUT_BUTTON_IDS[i]),
                    (TextView) v.findViewById(PIO_INPUT_TEXT_IDS[i]),
                    (CheckBox) v.findViewById(PIO_PULLUP_CHECKBOX_IDS[i])
            );
        }
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCallback.getKonashiManager().addListener(mKonashiDigitalListener);
    }

    @Override
    public void onDestroyView() {
        mCallback.getKonashiManager().removeListener(mKonashiDigitalListener);
        super.onDestroyView();
    }

    private final KonashiDigitalListener mKonashiDigitalListener = new KonashiDigitalListener() {
        @Override public void onUpdatePioSetting(int modes) {}
        @Override public void onUpdatePioPullup(int pullups) {}

        @Override
        public void onUpdatePioInput(byte value) {
            mViewHolders[0].setInputValue(value);
        }

        @Override
        public void onUpdatePioOutput(byte value) {

        }

        @Override public void onError(KonashiErrorReason errorReason, String message) {}
    };

    public interface Callback {
        KonashiManager getKonashiManager();
    }

    private static final class ViewHolder implements CompoundButton.OnCheckedChangeListener {
        private final int mPin;
        private final Callback mCallback;
        private final ToggleButton mPioModeButton;
        private final ToggleButton mPioOutputButton;
        private final TextView mPioInputText;
        private final CheckBox mPioPullupCheckbox;
        private final Handler mHandler;

        public ViewHolder(int pin, Callback callback,
                          ToggleButton pioModeButton, ToggleButton pioOutputButton,
                          TextView pioInputText, CheckBox pioPullupCheckbox) {
            mPin = pin;
            mCallback = callback;
            mPioModeButton = pioModeButton;
            mPioOutputButton = pioOutputButton;
            mPioInputText = pioInputText;
            mPioPullupCheckbox = pioPullupCheckbox;

            mPioModeButton.setOnCheckedChangeListener(this);
            mPioOutputButton.setOnCheckedChangeListener(this);
            mPioPullupCheckbox.setOnCheckedChangeListener(this);

            mHandler = new Handler(Looper.getMainLooper());
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
            if (buttonView.equals(mPioModeButton)) {
                mCallback.getKonashiManager()
                        .pinMode(mPin, isChecked ? Konashi.OUTPUT : Konashi.INPUT)
                        .then(new DoneCallback<BluetoothGattCharacteristic>() {
                                  @Override
                                  public void onDone(BluetoothGattCharacteristic result) {
                                      mHandler.post(new Runnable() {
                                          @Override
                                          public void run() {
                                              mPioOutputButton.setEnabled(isChecked);
                                              mPioInputText.setEnabled(!isChecked);
                                          }
                                      });
                                  }
                              }
                        );
            }
            if (buttonView.equals(mPioOutputButton)) {
                mCallback.getKonashiManager()
                        .digitalWrite(mPin, isChecked ? Konashi.HIGH : Konashi.LOW)
                        .then(new DoneCallback<BluetoothGattCharacteristic>() {
                            @Override
                            public void onDone(BluetoothGattCharacteristic result) {
                                Log.d(MainActivityFragment.class.getSimpleName(), "digitalWrite().then()");
                            }
                        });
            }
            if (buttonView.equals(mPioPullupCheckbox)) {
                mCallback.getKonashiManager()
                        .pinPullup(mPin, isChecked ? Konashi.PULLUP : Konashi.NO_PULLS)
                        .then(new DoneCallback<BluetoothGattCharacteristic>() {
                            @Override
                            public void onDone(BluetoothGattCharacteristic result) {
                                Log.d(MainActivityFragment.class.getSimpleName(), "pinPullup().then()");
                            }
                        });
            }
        }

        public void setInputValue(byte value) {
            mPioInputText.setText(value == Konashi.HIGH ? "HIGH" : "LOW");
        }
    }
}
