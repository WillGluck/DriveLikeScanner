package com.brufstudios.drivelikescanner.activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.brufstudios.drivelikescanner.R;
import com.brufstudios.drivelikescanner.common.Constants;
import com.brufstudios.drivelikescanner.fragments.EditorFragment;
import com.brufstudios.drivelikescanner.fragments.ScannerFragment;

/**
 * Created by TECBMWMG on 20/03/2017.
 */
public class DriverLikeActivity extends AppCompatActivity implements ScannerFragment.ScannerFragmentListener, EditorFragment.EditorFragmentListener {

    enum DriverLikeBreadcrump {
        CAMERA, COLLECTED, CAMERA_AGAIN, EDITOR;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive_like_activity);
        if (null == savedInstanceState) {
            loadScannerFragment(false);
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void handleInternalImageWithName(String fileName) {
        loadEditorFragment(fileName);
    }

    @Override
    public void callCamera() {
        loadScannerFragment(true);
    }

    @Override
    public void finishActivity() {

    }

    private void loadScannerFragment(Boolean addToStack) {
        loadFragment(new ScannerFragment(), Constants.TAG_SCANNER_FRAGMENT, addToStack);
    }

    private void loadEditorFragment(String fileName) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        getSupportActionBar().show();
        EditorFragment e = new EditorFragment();
        Bundle args = new Bundle();
        args.putString(Constants.PARAM_IMAGE_NAME, fileName);
        loadFragment(e, Constants.TAG_EDITOR_FRAGMENT, true);
    }

    private void loadFragment(Fragment fragment, String tag, Boolean addToStack) {
        Log.d(Constants.TAG, "Itens da stack: " + getSupportFragmentManager().getBackStackEntryCount());
        FragmentManager m = getSupportFragmentManager();
        if (2 == m.getBackStackEntryCount()) {
            m.popBackStack();
            m.popBackStack();
        }
        FragmentTransaction t = getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment, tag);
        if (addToStack) {
           t.addToBackStack(null);
        }
        t.commit();
    }

}
