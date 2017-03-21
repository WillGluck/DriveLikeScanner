package com.brufstudios.drivelikescanner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by TECBMWMG on 20/03/2017.
 */
public class DriverLikeActivity extends AppCompatActivity {

    private static Integer REQUEST_NEW_PHOTO = 0;
    private static Integer REQUEST_EDITOR = 1;
    private static String PARAM_RESULT = "com.brufstudios.drivelikescanner.DriverLikeActivity.PARAM_RESULT";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (null == savedInstanceState) {
            Intent intent = new Intent(this, ScannerActivity.class);
            startActivityForResult(intent, REQUEST_NEW_PHOTO);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_NEW_PHOTO) {

            if (RESULT_OK == resultCode) {
                String fileName = getIntent().getStringExtra(ScannerActivity.PARAM_IMAGE_NAME);
                Intent intent = new Intent(this, EditorActivity.class);
                intent.putExtra(EditorActivity.PARAM_IMAGE_NAME, fileName);
                startActivityForResult(intent, REQUEST_EDITOR);
            } else {
                finish();
            }

        } else if (requestCode == REQUEST_EDITOR) {

            if (RESULT_OK == resultCode) {
                Intent intent = getIntent();
                intent.putExtra(PARAM_RESULT, data.getStringArrayListExtra(EditorActivity.PARAM_IMAGE_NAME_LIST));
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Integer fileCount = getIntent().getIntExtra(EditorActivity.PARAM_IMAGE_COUNT, 0);
                Intent intent = new Intent(this, ScannerActivity.class);
                intent.putExtra(ScannerActivity.PARAM_SHOW_WARNING, fileCount > 2);
                startActivityForResult(intent, REQUEST_NEW_PHOTO);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
