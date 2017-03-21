package com.brufstudios.drivelikescanner.fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.brufstudios.drivelikescanner.R;
import com.brufstudios.drivelikescanner.common.CameraManager;
import com.brufstudios.drivelikescanner.common.Constants;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class ScannerFragment  extends Fragment implements View.OnClickListener, CameraManager.CameraManagerListener{

    private View view;
    private ScannerFragmentListener listener;
    private CameraManager cameraManager;
    private Boolean rearCamera = true;
    private Boolean flashOff = true;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ScannerFragmentListener) {
            listener = (ScannerFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement ScannerFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_scanner, container, false);
        if (hasCameraPermission()) {
            configFragment();
        } else {
            handleCameraPermissionRequest();
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        configCamera();
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        activity.getSupportActionBar().hide();
    }

    @Override
    public void onPause() {
        super.onPause();
        releaseCamera();
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
                ImageButton imageButton = (ImageButton) view.findViewById(R.id.camera_flash_toggle);
                if (View.GONE != imageButton.getVisibility()) {
                    imageButton.setVisibility(rearCamera ? View.VISIBLE : View.INVISIBLE);
                }
                cameraManager.toggleCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (Constants.CAMERA_PERMISSION_REQUEST.equals(requestCode)) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                configFragment();
            } else {
                getActivity().finish();
            }
        }
    }

    @Override
    public void handleImage(byte[] data) {
        FileOutputStream fos = null;
        try {

            //TODO verificar um jeito de salvar a imagem rotacionada corretamente

            String fileName = UUID.randomUUID().toString();
            fos = getContext().openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(data);

            listener.handleInternalImageWithName(fileName);

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

    private void configCamera() {
        cameraManager = new CameraManager(getContext(), this, (FrameLayout) view.findViewById(R.id.camera_preview));
        if (!cameraManager.init()) {
            getActivity().finish();
        }
    }

    private Boolean hasCameraPermission() {
        return ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, Constants.CAMERA_PERMISSION_REQUEST);
    }

    private void releaseCamera() {
        if (cameraManager != null) {
            cameraManager.release();
            cameraManager = null;
        }
    }

    private void handleCameraPermissionRequest() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
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

    private void configFragment() {
        //hideLoadingScreen();
        configButtons();
    }

    private void configButtons() {
        configToggleButtons( R.id.camera_flash_toggle, PackageManager.FEATURE_CAMERA_FLASH);
        configToggleButtons( R.id.camera_toggle, PackageManager.FEATURE_CAMERA_FRONT);
        view.findViewById(R.id.camera_take_picture).setOnClickListener(this);
    }

    private void configToggleButtons(Integer buttonId, String feature) {

        ImageButton imageButton = (ImageButton) view.findViewById(buttonId);

        if (getContext().getPackageManager().hasSystemFeature(feature)) {
            imageButton.setVisibility(View.VISIBLE);
            imageButton.setOnClickListener(this);
        } else {
            imageButton.setVisibility(View.GONE);
        }
    }


    public interface ScannerFragmentListener {

        void handleInternalImageWithName(String fileName);

    }
}
