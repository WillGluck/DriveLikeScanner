package com.brufstudios.drivelikescanner.common;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.brufstudios.drivelikescanner.R;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder holder;
    private Camera camera;

    public CameraPreview(Context context, Camera camera) {
        super(context);
        this.camera = camera;
        holder = getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    private void startCamera(SurfaceHolder holder) {
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

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        startCamera(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        if (holder.getSurface() == null)
            return;

        try {
            camera.stopPreview();
        } catch (Exception e) {
            //Ignorar
        }

        configureRotation();
        startCamera(holder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //Activity fica responsável por limpar o que for necessário.
    }
}
