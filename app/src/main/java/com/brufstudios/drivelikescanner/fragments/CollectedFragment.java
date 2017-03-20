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

    }

    public void replaceSelectedImage(String fileName) {

    }
    public List<String> getFiles() {
        return files;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.collected_crop:
                listener.editFileWithName("teste");
                break;
            case R.id.collected_edit:
                break;
            case R.id.collected_remove:
                break;
            case R.id.collected_rename:
                break;
            case R.id.collected_rotate:
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
