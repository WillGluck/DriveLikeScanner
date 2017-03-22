package com.brufstudios.drivelikescanner.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.brufstudios.drivelikescanner.R;
import com.brufstudios.drivelikescanner.common.Constants;
import com.brufstudios.drivelikescanner.fragments.EditorFragment;
import com.brufstudios.drivelikescanner.fragments.ScannerFragment;

/**
 * Created by TECBMWMG on 20/03/2017.
 */
public class DriverLikeActivity extends AppCompatActivity implements ScannerFragment.ScannerFragmentListener, EditorFragment.EditorFragmentListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive_like_activity);

        if (null == savedInstanceState) {

            EditorFragment editorFragment = new EditorFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, editorFragment, Constants.TAG_EDITOR_FRAGMENT)
                    .hide(editorFragment)
                    .add(R.id.container, new ScannerFragment(), Constants.TAG_SCANNER_FRAGMENT)
                    .commit();
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void handleInternalImageWithName(String fileName) {
        showEditorFragment(fileName);
    }

    @Override
    public void callCamera() {
        showScannerFragment();
    }

    @Override
    public void finishActivity() {
        finish();
    }

    private EditorFragment getEditorFragment() {
        return (EditorFragment) getSupportFragmentManager().findFragmentByTag(Constants.TAG_EDITOR_FRAGMENT);
    }

    private ScannerFragment getScannerFragment() {
        return (ScannerFragment) getSupportFragmentManager().findFragmentByTag(Constants.TAG_SCANNER_FRAGMENT);
    }

    private void showScannerFragment() {
        getSupportFragmentManager().beginTransaction().add(R.id.container, new ScannerFragment(), Constants.TAG_SCANNER_FRAGMENT).addToBackStack(null).hide(getEditorFragment()).commit();
    }

    private void showEditorFragment(String fileName) {

        FragmentManager manager = getSupportFragmentManager();
        EditorFragment editorFragment = getEditorFragment();

        manager.beginTransaction().remove(getScannerFragment()).show(editorFragment).commit();

        if (2 == manager.getBackStackEntryCount()) {
            manager.popBackStack();
            manager.popBackStack();
        }

        editorFragment.addFileName(fileName);
    }

}
