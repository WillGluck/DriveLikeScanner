package com.brufstudios.drivelikescanner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.brufstudios.drivelikescanner.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static Integer DIGITALIZAR = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.testButton).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.testButton:
                goToScanner();
                break;
        }
    }

    private void goToScanner() {
        Intent intent = new Intent(this, DriverLikeActivity.class);
        startActivityForResult(intent, DIGITALIZAR);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (DIGITALIZAR == requestCode && RESULT_OK == resultCode) {
            //TODO pega resultado
        }
    }
}
