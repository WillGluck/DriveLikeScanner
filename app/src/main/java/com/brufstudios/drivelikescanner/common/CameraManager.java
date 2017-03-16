package com.brufstudios.drivelikescanner.common;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.brufstudios.drivelikescanner.R;
import com.brufstudios.drivelikescanner.utils.Utils;

public class CameraManager <ActivityListener extends Activity & CameraManager.CameraManagerListener>
        extends SurfaceView
        implements SurfaceHolder.Callback,  Camera.PictureCallback {

    public interface CameraManagerListener {
        void handleImage(byte[] data);
    }

    private ActivityListener listener;
    private Camera camera;
    private SurfaceHolder holder;
    private Integer selectedCamera;

    public CameraManager(ActivityListener activity) {

        super(activity);
        listener = activity;
        holder = getHolder();
        selectedCamera = Camera.CameraInfo.CAMERA_FACING_BACK;
    }

    public Boolean init() {

        FrameLayout previewContainer = (FrameLayout) listener.findViewById(R.id.camera_preview);
        previewContainer.removeAllViews();
        previewContainer.addView(this);

        camera = Utils.getCameraInstance(getContext(), selectedCamera);
        if (null != camera) {
            holder.addCallback(this);
            holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            return true;
        } else {
            return false;
        }
    }

    public void takePicture() {
        camera.takePicture(null, null, this);
    }

    public Boolean toggleCamera() {
        if (Camera.CameraInfo.CAMERA_FACING_BACK == selectedCamera) {
            selectedCamera = Camera.CameraInfo.CAMERA_FACING_FRONT;
        } else {
            selectedCamera = Camera.CameraInfo.CAMERA_FACING_BACK;
        }
        release();
        return init();
    }

    public void toggleFlash() {
        String flashMode;
        if (Camera.Parameters.FLASH_MODE_ON.equals(camera.getParameters().getFlashMode())) {
            flashMode = Camera.Parameters.FLASH_MODE_OFF;
        } else {
            flashMode = Camera.Parameters.FLASH_MODE_ON;
        }
        Camera.Parameters parameters = camera.getParameters();
        parameters.setFlashMode(flashMode);
        camera.setParameters(parameters);
        camera.startPreview();
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        release();
        listener.handleImage(data);
    }

    private void startCamera() {
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (Exception e) {
            Log.d(Constants.TAG, getContext().getString(R.string.error_msg_setting_camera_preview, e.getMessage()));
        }
    }

    private void configureRotation() {

        Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        switch (display.getRotation()) {
            case Surface.ROTATION_0:
                this.camera.setDisplayOrientation(90);
                break;
            case Surface.ROTATION_90:
                break;
            case Surface.ROTATION_180:
                this.camera.setDisplayOrientation(270);
                break;
            case Surface.ROTATION_270:
                this.camera.setDisplayOrientation(180);
                break;
            default:
                break;
        }
    }

    public void release() {
        camera.release();
        holder.removeCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        startCamera();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        if (this.holder.getSurface() == null)
            return;

        try {
            camera.stopPreview();
        } catch (Exception e) {
            //Ignorar
        }

        configureRotation();
        startCamera();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //Activity fica responsável por limpar o que for necessário.
    }

}
