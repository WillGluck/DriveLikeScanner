package com.brufstudios.drivelikescanner.common;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import com.brufstudios.drivelikescanner.R;
import com.brufstudios.drivelikescanner.utils.Utils;

public class CameraManager <Listener extends CameraManager.CameraManagerListener> extends SurfaceView implements SurfaceHolder.Callback,  Camera.PictureCallback {

    public interface CameraManagerListener {
        void handleImage(byte[] data);
    }

    private Listener listener;
    private Camera camera;
    private FrameLayout container;
    private SurfaceHolder holder;
    private Integer selectedCamera;

    public CameraManager(Context context, Listener listener, FrameLayout container) {
        super(context);
        this.listener = listener;
        this.container = container;
        holder = getHolder();
        selectedCamera = Camera.CameraInfo.CAMERA_FACING_BACK;
    }

    public Boolean init() {

        container.removeAllViews();
        container.addView(this);

        camera = Utils.getCameraInstance(getContext(), selectedCamera);

        if (null != camera) {
            camera.setDisplayOrientation(90);
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

    public void release() {
        if (null != camera)
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
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //Activity fica responsável por limpar o que for necessário.
    }

}
