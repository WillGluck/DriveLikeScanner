package com.brufstudios.drivelikescanner.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.brufstudios.drivelikescanner.R;
import com.brufstudios.drivelikescanner.common.CameraManager;
import com.brufstudios.drivelikescanner.common.Constants;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class ScannerActivity extends AppCompatActivity implements View.OnClickListener, CameraManager.CameraManagerListener {

    private CameraManager cameraManager;
    private Boolean rearCamera = true;
    private Boolean flashOff = true;

    public static String PARAM_IMAGE_NAME = "com.brufstudios.drivelikescanner.ScannerActivity.PARAM_IMAGE_NAME";
    public static String PARAM_SHOW_WARNING = "com.brufstudios.drivelikescanner.ScannerActivity.PARAM_SHOW_WARNING";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive_like_scanner);
        if (hasCameraPermission()) {
            configActivity();
        } else {
            handleCameraPermissionRequest();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        configCamera();
    }

    private void configCamera() {
        cameraManager = new CameraManager(this);
        if (!cameraManager.init()) {
            finish();
        }
    }

    private Boolean hasCameraPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, Constants.CAMERA_PERMISSION_REQUEST);
    }

    private void handleCameraPermissionRequest() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle(R.string.alert_dialog_title);
                alertDialog.setMessage(getString(R.string.alert_dialog_camera_permission_message));
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        requestCameraPermission();
                    }
                });
                alertDialog.show();
            } else {
                requestCameraPermission();
            }
        }
    }

    private void configActivity() {
        //hideLoadingScreen();
        configButtons();
    }

//    private void hideLoadingScreen() {
//        final LinearLayout loadingLayer = (LinearLayout) findViewById(R.id.loadingScreen);
//        Integer shortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);
//        loadingLayer.animate().alpha(0.0f).setDuration(shortAnimationDuration).setListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                loadingLayer.setVisibility(View.GONE);
//            }
//        });
//    }
//
//    private void showLoadingScreen() {
//        LinearLayout loadingLayer = (LinearLayout) findViewById(R.id.loadingScreen);
//        loadingLayer.setAlpha(1.0f);
//        loadingLayer.setVisibility(View.VISIBLE);
//    }

    private void configButtons() {
        configToggleButtons( R.id.camera_flash_toggle, PackageManager.FEATURE_CAMERA_FLASH);
        configToggleButtons( R.id.camera_toggle, PackageManager.FEATURE_CAMERA_FRONT);
        findViewById(R.id.camera_take_picture).setOnClickListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (Constants.CAMERA_PERMISSION_REQUEST.equals(requestCode)) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                configActivity();
            } else {
                finish();
            }
        }
    }

    private void configToggleButtons(Integer buttonId, String feature) {

        ImageButton imageButton = (ImageButton) findViewById(buttonId);

        if (getPackageManager().hasSystemFeature(feature)) {
            imageButton.setVisibility(View.VISIBLE);
            imageButton.setOnClickListener(this);
        } else {
            imageButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        ImageButton button = (ImageButton) v;
        switch (v.getId()) {
            case R.id.camera_take_picture:
                cameraManager.takePicture();
                //showLoadingScreen();
                break;
            case R.id.camera_flash_toggle:
                flashOff = !flashOff;
                button.setImageResource(flashOff ? R.drawable.ic_flash_off_white_24px : R.drawable.ic_flash_on_white_24px);
                cameraManager.toggleFlash();
                break;
            case R.id.camera_toggle:
                rearCamera = !rearCamera;
                button.setImageResource(rearCamera ? R.drawable.ic_camera_front_white_24px : R.drawable.ic_camera_rear_white_24px);
                ImageButton imageButton = (ImageButton) findViewById(R.id.camera_flash_toggle);
                if (View.GONE != imageButton.getVisibility()) {
                    imageButton.setVisibility(rearCamera ? View.VISIBLE : View.INVISIBLE);
                }
                cameraManager.toggleCamera();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    private void releaseCamera() {
        if (cameraManager != null) {
            cameraManager.release();
            cameraManager = null;
        }
    }

    @Override
    public void handleImage(byte[] data) {
        FileOutputStream fos = null;
        try {

            //TODO verificar um jeito de salvar a imagem rotacionada corretamente

            String fileName = UUID.randomUUID().toString();
            fos = openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(data);

            Intent intent = new Intent(this, EditorActivity.class);
            intent.putExtra(EditorActivity.PARAM_IMAGE_NAME, fileName);

            setResult(RESULT_OK, intent);
            finish();

        } catch (Exception e) {
            Log.e(Constants.TAG, getString(R.string.error_msg_saving_temp_file, e.getMessage()));
        }  finally {
            try {
                if (null != fos)
                    fos.close();
            } catch (IOException e) {
                Log.e(Constants.TAG, getString(R.string.error_msg_closing_file_output_stream, e.getMessage()));
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
    }
}