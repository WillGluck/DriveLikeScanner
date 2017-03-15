package com.brufstudios.drivelikescanner.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brufstudios.drivelikescanner.R;

public class CollectedFragment extends Fragment {

    private CollectedFragmentListener listener;

    public CollectedFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_collected, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CollectedFragmentListener) {
            listener = (CollectedFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement CollectedFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface CollectedFragmentListener {

    }
}
