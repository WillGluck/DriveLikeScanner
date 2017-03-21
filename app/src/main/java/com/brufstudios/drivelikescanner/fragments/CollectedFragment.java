package com.brufstudios.drivelikescanner.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.brufstudios.drivelikescanner.R;

import java.util.ArrayList;
import java.util.List;

public class CollectedFragment extends Fragment {

    private static String SAVED_FILE_NAME_LIST = "bundleFileNameList";
    private static String SAVED_SELECTED_FILE = "bundleSelectedFile";

    public static String PARAM_IMAGE_NAME = "com.brufstudios.drivelikescanner.CollectedFragment.IMAGE_NAME";

    private CollectedFragmentListener listener;
    private List<String> files;
    private String selectedFile;


    public CollectedFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (null != savedInstanceState) {
            files = savedInstanceState.getStringArrayList(SAVED_FILE_NAME_LIST);
            selectedFile = savedInstanceState.getString(SAVED_SELECTED_FILE);
        } else {
            files = new ArrayList<>();
            selectedFile = null;
        }

        if (null != getArguments() && getArguments().containsKey(PARAM_IMAGE_NAME)) {
            if (0 < files.size()) {
                replaceSelectedImage(getArguments().getString(PARAM_IMAGE_NAME));
            } else {

            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_collected, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_collected, container, false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putStringArrayList(SAVED_FILE_NAME_LIST, (ArrayList<String>) files);
        outState.putString(SAVED_SELECTED_FILE, selectedFile);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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

    public void addNewImage(String fileName) {
        //TODO
    }

    public void replaceSelectedImage(String fileName) {
        //TODO
    }

    private void removeSelectedImage() {
        //TODO
    }

    private void rotateSelectedImage() {
        //TODO
    }

    private void editSelectedImage() {
        //TODO
    }

    public List<String> getFiles() {
        return files;
    }

    public String getSelectedFile() {
        return selectedFile;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.collected_crop:
                listener.editFileWithName(selectedFile);
                break;
            case R.id.collected_edit:
                editSelectedImage();
                break;
            case R.id.collected_remove:
                removeSelectedImage();
                break;
            case R.id.collected_rotate:
                rotateSelectedImage();
                break;
            default:
                return super.onOptionsItemSelected(item);

        }
        return true;
    }


    public interface CollectedFragmentListener {
        void editFileWithName(String fileName);
    }
}
