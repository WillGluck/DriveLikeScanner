package com.brufstudios.drivelikescanner.fragments;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brufstudios.drivelikescanner.R;

public class EditorFragment extends Fragment implements View.OnClickListener {

    private View view;
    private EditorFragmentListener listener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_editor, container, false);
        configFragment();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        activity.getSupportActionBar().show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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
                listener.callCamera();
                break;
            case R.id.editor_retake_image:
                listener.callCamera();
                break;
            case R.id.editor_finish:
                listener.finishActivity();
                break;
        }
    }

    private void configFragment() {
        loadListeners();
    }

    private void loadListeners() {
        view.findViewById(R.id.editor_add_another_image).setOnClickListener(this);
        view.findViewById(R.id.editor_retake_image).setOnClickListener(this);
        view.findViewById(R.id.editor_finish).setOnClickListener(this);
    }

    public interface EditorFragmentListener {
        void callCamera();
        void finishActivity();
    }
}
