package com.brufstudios.drivelikescanner.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.brufstudios.drivelikescanner.R;
import com.brufstudios.drivelikescanner.common.ImageEditor;

public class DriveLikeEditorActivity extends AppCompatActivity {

    public static String IMAGE_PARAM = "com.brufstudios.drivelikescanner.IMAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive_like_editor);

        byte[] data = getIntent().getByteArrayExtra(IMAGE_PARAM);

        ImageEditor editor = new ImageEditor(data);
        byte[] novaImagem = editor.test();

        ImageView imgViewer = (ImageView) findViewById(R.id.imageContainer);
        Bitmap bm = BitmapFactory.decodeByteArray(novaImagem, 0, novaImagem.length);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        imgViewer.setMinimumHeight(dm.heightPixels);
        imgViewer.setMinimumWidth(dm.widthPixels);
        imgViewer.setImageBitmap(bm);

    }


}
