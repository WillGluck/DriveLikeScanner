package com.brufstudios.drivelikescanner.activities;

import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Visibility;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.brufstudios.drivelikescanner.R;
import com.brufstudios.drivelikescanner.common.CameraPreview;
import com.brufstudios.drivelikescanner.utils.Utils;

public class DriveLikeScannerActivity extends AppCompatActivity implements View.OnClickListener{

    private Camera camera;
    private CameraPreview preview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive_like_scanner);

        configActivity();
    }

    private void configActivity() {
        configCamera();
        configButtons();
    }

    private void configCamera() {

        camera = Utils.getCameraInstance(this);
        if (null != camera) {
            preview = new CameraPreview(this, camera);
            FrameLayout previewContainer = (FrameLayout) findViewById(R.id.camera_preview);
            previewContainer.addView(preview);
        } else {
            //TODO finaliza a joça toda.
        }
    }

    private void configButtons() {
        configToggleButtons( R.id.camera_flash_toggle, PackageManager.FEATURE_CAMERA_FLASH);
        configToggleButtons( R.id.camera_toggle, PackageManager.FEATURE_CAMERA_FRONT);
        findViewById(R.id.camera_take_picture).setOnClickListener(this);
    }

    private void configToggleButtons(Integer buttonId, String feature) {

        ImageButton imageButton = (ImageButton) findViewById(buttonId);

        if (getPackageManager().hasSystemFeature(feature)) {
            imageButton.setVisibility(View.VISIBLE);
            imageButton.setOnClickListener(null);
        } else {
            imageButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {

    }


    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();

    }

    private void releaseCamera() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }
}
