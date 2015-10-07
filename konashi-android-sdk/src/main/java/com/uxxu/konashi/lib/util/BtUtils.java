package com.uxxu.konashi.lib.util;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

/**
 * Created by izumin on 10/7/15.
 */
public final class BtUtils {

    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_STATE_CHANGE_BT = 2;

    private BtUtils() {
        throw new AssertionError("constructor of the utility class should not be called");
    }

    public static boolean isBluetoothSupported() {
        return BluetoothAdapter.getDefaultAdapter() != null;
    }

    public static boolean isBluetoothEnabled() {
        return BluetoothAdapter.getDefaultAdapter().isEnabled();
    }

    public static boolean isBleSupported(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }

    public static void startRequestEnableBluetoothActivityForResult(Activity activity) {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(intent, REQUEST_ENABLE_BT);
    }
}