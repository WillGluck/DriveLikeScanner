package com.brufstudios.drivelikescanner.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.brufstudios.drivelikescanner.R;
import com.brufstudios.drivelikescanner.fragments.CollectedFragment;
import com.brufstudios.drivelikescanner.fragments.EditorFragment;

public class DriveLikeEditorActivity extends AppCompatActivity implements EditorFragment.EditorFragmentListener, CollectedFragment.CollectedFragmentListener, View.OnClickListener {

    public static String IMAGE_NAME_PARAM = "com.brufstudios.drivelikescanner.IMAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_drive_like_editor);

        configActivity();
        if (null == savedInstanceState)
            loadCollectedFragment();

//        try {
//
//            String fileName = getIntent().getStringExtra(IMAGE_NAME_PARAM);
//
//            ImageView imageView = (ImageView) findViewById(R.id.imageContainer);
//            DisplayMetrics dm = new DisplayMetrics();
//            getWindowManager().getDefaultDisplay().getMetrics(dm);
//
//            File file = getFileStreamPath(fileName);
//
//            //TODO editar imagem.
//            ImageEditor editor= new ImageEditor(file);
//            editor.test();
//
//            Bitmap bm = BitmapFactory.decodeFile(file.getPath());
//            //TODO verificar um jeito de salvar a imagem rotacionada corretamente - Fazer isso na hora de salvar, não na leitura.
//
//            imageView.setMinimumHeight(dm.heightPixels);
//            imageView.setMinimumWidth(dm.widthPixels);
//            imageView.setImageBitmap(bm);
//
//        } catch (Exception e) {
//            Log.e(Constants.TAG, getString(R.string.error_msg_file_not_found, e.getMessage()));
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void configActivity() {
        loadListeners();
    }

    private void loadCollectedFragment() {
        loadFragment(new CollectedFragment());
    }

    private void loadEditorFragment() {
        loadFragment(new EditorFragment());
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragmentContainer, fragment);
        fragmentTransaction.commit();
    }

    private void loadListeners() {
        findViewById(R.id.editor_add_another_image).setOnClickListener(this);
        findViewById(R.id.editor_retake_image).setOnClickListener(this);
        findViewById(R.id.editor_finish).setOnClickListener(this);
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
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.editor_add_another_image:
                break;
            case R.id.editor_retake_image:
                break;
            case R.id.editor_finish:
                break;
        }
    }
}
