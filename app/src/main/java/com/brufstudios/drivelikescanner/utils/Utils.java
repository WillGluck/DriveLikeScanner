package com.brufstudios.drivelikescanner.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.util.Log;

import com.brufstudios.drivelikescanner.R;
import com.brufstudios.drivelikescanner.common.Constants;

public class Utils {

    public static Boolean checkCameraHardware(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    public static Camera getCameraInstance(Context context, Integer cameraId) {
        Camera c = null;
        try {
            c = Camera.open(cameraId);
        } catch (Exception e) {
            Log.e(Constants.TAG, context.getString(R.string.error_msg_camera_unavaliable, e.getMessage()));
        }
        return c;
    }

}
