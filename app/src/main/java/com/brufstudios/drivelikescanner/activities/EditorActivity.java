package com.brufstudios.drivelikescanner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.brufstudios.drivelikescanner.R;
import com.brufstudios.drivelikescanner.fragments.CollectedFragment;
import com.brufstudios.drivelikescanner.fragments.EditorFragment;

import java.util.ArrayList;

public class EditorActivity extends AppCompatActivity implements EditorFragment.EditorFragmentListener, CollectedFragment.CollectedFragmentListener, View.OnClickListener {

    public static String PARAM_IMAGE_NAME = "com.brufstudios.drivelikescanner.EditorActivity.IMAGE_NAME";
    public static String PARAM_IMAGE_COUNT = "com.brufstudios.drivelikescanner.EditorActivity.IMAGE_COUNT";
    public static String PARAM_IMAGE_NAME_LIST = "com.brufstudios.drivelikescanner.EditorActivity.IMAGE_NAME_LIST";

    private static Integer REQUEST_NEW_PHOTO = 0;
    private static Integer REQUEST_RETRY_PHOTO = 1;
    private static String COLLECTED_TAG = "com.brufstudios.drivelikescanner.CollectedTag";
    private static String EDITOR_TAG = "com.brufstudios.drivelikescanner.EditorTag";

    private static String SAVED_SELECTED_FRAGMENT = "bundleSelectedFragment";

    private EnumSelectedFragment selectedFragment = null;

    private enum EnumSelectedFragment {

        COLLECTED(0), EDITOR(1);

        private Integer value;

        EnumSelectedFragment(Integer value) {
            this.value = value;
        }
        public Integer getValue() {
            return value;
        }

        public EnumSelectedFragment getFromValue(Integer value) {
            if (COLLECTED.getValue().equals(value)) {
                return COLLECTED;
            } else if (EDITOR.getValue().equals(value)) {
                return EDITOR;
            }
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_drive_like_editor);
        configActivity();

        if (null == savedInstanceState) {
            loadCollectedFragment(getIntent().getStringExtra(PARAM_IMAGE_NAME));
            selectedFragment = EnumSelectedFragment.COLLECTED;
        } else {
            selectedFragment = EnumSelectedFragment.COLLECTED.getFromValue(savedInstanceState.getInt(SAVED_SELECTED_FRAGMENT));
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent = getIntent();
            intent.putExtra(EditorActivity.PARAM_IMAGE_COUNT, getCollectedFragment().getFiles().size());
            setResult(RESULT_CANCELED, intent);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SAVED_SELECTED_FRAGMENT, selectedFragment.getValue());
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void configActivity() {
        loadListeners();
    }

    private void loadListeners() {
        findViewById(R.id.editor_add_another_image).setOnClickListener(this);
        findViewById(R.id.editor_retake_image).setOnClickListener(this);
        findViewById(R.id.editor_finish).setOnClickListener(this);
    }

    private void loadCollectedFragment(String fileName) {
        loadFragment(new CollectedFragment(), COLLECTED_TAG, fileName, CollectedFragment.PARAM_IMAGE_NAME);
    }

    private void loadEditorFragment(String fileName) {
        loadFragment(new EditorFragment(), EDITOR_TAG, fileName, EditorFragment.PARAM_IMAGE_NAME);
    }

    private void loadFragment(Fragment fragment, String TAG, String fileName, String paramName) {
        if (null != fileName) {
            Bundle extras = new Bundle();
            extras.putString(paramName, fileName);
            fragment.setArguments(extras);
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragmentContainer, fragment, TAG);
        fragmentTransaction.commit();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Start activity
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        CollectedFragment galleryFragment = getCollectedFragment();
        String fileName = data.getStringExtra(PARAM_IMAGE_NAME);

        if (null != galleryFragment) {

            if (REQUEST_NEW_PHOTO == requestCode && RESULT_OK == resultCode) {
                galleryFragment.addNewImage(fileName);
            } else if (REQUEST_RETRY_PHOTO == requestCode && RESULT_OK == resultCode) {
                galleryFragment.replaceSelectedImage(fileName);
            }
        }

        //TODO Recebe imagem
        //File file = new File(context.getFilesDir(), filename);
        //Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        //ImageView myImage = (ImageView) findViewById(R.id.imageviewTest);
        //myImage.setImageBitmap(myBitmap);

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.editor_add_another_image:
                addNewImage();
                break;
            case R.id.editor_retake_image:
                replaceSelectedImage();
                break;
            case R.id.editor_finish:
                finishActivity();
                break;
        }
    }

    private CollectedFragment getCollectedFragment() {
        return (CollectedFragment) getSupportFragmentManager().findFragmentByTag(COLLECTED_TAG);
    }

    private void addNewImage() {
        startActivityForResult(new Intent(this, ScannerActivity.class), REQUEST_NEW_PHOTO);
    }

    private void replaceSelectedImage() {
        startActivityForResult(new Intent(this, ScannerActivity.class), REQUEST_RETRY_PHOTO);
    }

    private void finishActivity() {
        Intent intent = getIntent();
        intent.putStringArrayListExtra(PARAM_IMAGE_NAME_LIST, (ArrayList<String>) getCollectedFragment().getFiles());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void editFileWithName(String fileName) {
        loadEditorFragment(fileName);
    }
}
