package com.brufstudios.drivelikescanner.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brufstudios.drivelikescanner.R;

public class EditorFragment extends Fragment {

    private String SAVED_SELECTED_FILE = "bundleSelectedFile";
    public static String PARAM_IMAGE_NAME = "com.brufstudios.drivelikescanner.EditorFragment.IMAGE_NAME";

    private EditorFragmentListener listener;
    private String selectedFile;

    public EditorFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            selectedFile = getArguments().getString(PARAM_IMAGE_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_editor, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        selectedFile = savedInstanceState.getString(SAVED_SELECTED_FILE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(SAVED_SELECTED_FILE, selectedFile);
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

    public interface EditorFragmentListener {

    }

}
