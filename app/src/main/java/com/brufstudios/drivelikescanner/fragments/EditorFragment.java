package com.brufstudios.drivelikescanner.fragments;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.brufstudios.drivelikescanner.R;
import com.brufstudios.drivelikescanner.common.GalleryPageController;
import com.brufstudios.drivelikescanner.views.PolygonCanvas;
import com.brufstudios.drivelikescanner.views.PolygonView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditorFragment extends Fragment implements View.OnClickListener {

    private View view;
    private EditorFragmentListener listener;
    private GalleryPageController pagerController;
    private PolygonCanvas polygonView;
    private Boolean isWaitingForNewImage = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_editor, container, false);
        configFragment();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("asd", "asd");
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            activity.getSupportActionBar().show();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof EditorFragmentListener) {
            listener = (EditorFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement EditorFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.editor_add_another_image:
                isWaitingForNewImage = true;
                listener.callCamera();
                break;
            case R.id.editor_retake_image:
                isWaitingForNewImage = false;
                listener.callCamera();
                break;
            case R.id.editor_finish:
                listener.finishActivity();
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_editor, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_cofigurations:
                configurations();
                break;
            case R.id.menu_crop:
                crop();
                break;
            case R.id.menu_edit:
                edit();
                break;
            case R.id.menu_remove:
                remove();
                break;
            case R.id.menu_rename:
                rename();
                break;
            case R.id.menu_rotate:
                rotate();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addFileName(String imageName) {
        if (isWaitingForNewImage) {
            pagerController.addFileName(imageName);
        } else {
            pagerController.replaceCurrentFileName(imageName);
        }
    }

    private void configurations() {
        Toast.makeText(getContext(), "Não implementado ainda", Toast.LENGTH_SHORT).show();
    }

    private void crop() {

        loadImageToPolygonView();

        //Toast.makeText(getContext(), "Não implementado ainda", Toast.LENGTH_SHORT).show();
    }

    private void loadImageToPolygonView() {

        view.findViewById(R.id.galleryContainer).setVisibility(View.GONE);
        view.findViewById(R.id.editorContainer).setVisibility(View.VISIBLE);

        polygonView = (PolygonCanvas) view.findViewById(R.id.editorPolygons);
        ImageView sourceImageView = (ImageView) view.findViewById(R.id.editorImage);
        FrameLayout sourceFrame = (FrameLayout) view.findViewById(R.id.editorImageContainer);

        Bitmap original = pagerController.getSelectedImage();
        //Bitmap scaledBitmap = scaledBitmap(original, sourceFrame.getWidth(), sourceFrame.getHeight());
        sourceImageView.setImageBitmap(original);
        Bitmap tempBitmap = ((BitmapDrawable) sourceImageView.getDrawable()).getBitmap();
        Map<Integer, PointF> pointFs = getEdgePoints(tempBitmap);
        polygonView.setPoints(pointFs);
        polygonView.setVisibility(View.VISIBLE);
        int padding = 10;
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(tempBitmap.getWidth() + 2 * padding, tempBitmap.getHeight() + 2 * padding);
        layoutParams.gravity = Gravity.CENTER;
        polygonView.setLayoutParams(layoutParams);
    }

    private Bitmap scaledBitmap(Bitmap bitmap, int width, int height) {
        Matrix m = new Matrix();
        m.setRectToRect(new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight()), new RectF(0, 0, width, height), Matrix.ScaleToFit.CENTER);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
    }


    private Map<Integer, PointF> getOutlinePoints(Bitmap tempBitmap) {
        Map<Integer, PointF> outlinePoints = new HashMap<>();
        outlinePoints.put(0, new PointF(0, 0));
        outlinePoints.put(1, new PointF(tempBitmap.getWidth(), 0));
        outlinePoints.put(2, new PointF(0, tempBitmap.getHeight()));
        outlinePoints.put(3, new PointF(tempBitmap.getWidth(), tempBitmap.getHeight()));
        return outlinePoints;
    }

    private Map<Integer, PointF> orderedValidEdgePoints(Bitmap tempBitmap, List<PointF> pointFs) {
        Map<Integer, PointF> orderedPoints = polygonView.getOrderedPoints(pointFs);
        if (!polygonView.isValidShape(orderedPoints)) {
            orderedPoints = getOutlinePoints(tempBitmap);
        }
        return orderedPoints;
    }

    private List<PointF> getContourEdgePoints(Bitmap tempBitmap) {
        float[] points = getPoints(tempBitmap);
        float x1 = points[0];
        float x2 = points[1];
        float x3 = points[2];
        float x4 = points[3];

        float y1 = points[4];
        float y2 = points[5];
        float y3 = points[6];
        float y4 = points[7];

        List<PointF> pointFs = new ArrayList<>();
        pointFs.add(new PointF(x1, y1));
        pointFs.add(new PointF(x2, y2));
        pointFs.add(new PointF(x3, y3));
        pointFs.add(new PointF(x4, y4));
        return pointFs;
    }


    private float[] getPoints(Bitmap tempBitmap) {
        return new float[]{0.0f, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f};
    }

    private Map<Integer, PointF> getEdgePoints(Bitmap tempBitmap) {
        List<PointF> pointFs = getContourEdgePoints(tempBitmap);
        Map<Integer, PointF> orderedPoints = orderedValidEdgePoints(tempBitmap, pointFs);
        return orderedPoints;
    }


    private void edit() {
        Toast.makeText(getContext(), "Não implementado ainda", Toast.LENGTH_SHORT).show();
    }

    private void remove() {
        pagerController.removeCurrentFileName();
        if (0 == pagerController.getCount()) {
            listener.finishActivity();
        }
    }

    private void rename() {
        Toast.makeText(getContext(), "Não implementado ainda", Toast.LENGTH_SHORT).show();
    }

    private void rotate() {
        Toast.makeText(getContext(), "Não implementado ainda", Toast.LENGTH_SHORT).show();
    }

    private void configFragment() {
        loadListeners();
        configGallery();
    }

    private void loadListeners() {
        view.findViewById(R.id.editor_add_another_image).setOnClickListener(this);
        view.findViewById(R.id.editor_retake_image).setOnClickListener(this);
        view.findViewById(R.id.editor_finish).setOnClickListener(this);
    }

    private void configGallery() {
        pagerController = new GalleryPageController(getContext(), (ViewPager) view.findViewById(R.id.galleryPager));
    }

    public interface EditorFragmentListener {
        void callCamera();
        void finishActivity();
    }
}
