package com.brufstudios.drivelikescanner.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brufstudios.drivelikescanner.R;

public class EditorFragment extends Fragment {

    private EditorFragmentListener listener;

    public EditorFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_editor, container, false);
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
