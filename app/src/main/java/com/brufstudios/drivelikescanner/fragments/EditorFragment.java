package com.brufstudios.drivelikescanner.fragments;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.brufstudios.drivelikescanner.R;
import com.brufstudios.drivelikescanner.adapters.GalleryPageController;

public class EditorFragment extends Fragment implements View.OnClickListener {

    private View view;
    private EditorFragmentListener listener;
    private GalleryPageController pagerController;
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
        Toast.makeText(getContext(), "Não implementado ainda", Toast.LENGTH_SHORT).show();
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
