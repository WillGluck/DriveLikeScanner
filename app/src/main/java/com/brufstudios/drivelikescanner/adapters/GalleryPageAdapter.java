package com.brufstudios.drivelikescanner.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.brufstudios.drivelikescanner.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GalleryPageAdapter extends PagerAdapter {

    public static String IMAGE_NAME = "com.brufstudios.drivelikescanner.CollectedPageAdapter.IMAGE_NAME";

    private Context context;
    LayoutInflater inflater;
    List<String> filesNames = new ArrayList<>();

    public GalleryPageAdapter(@NonNull Context context, @NonNull List<String> filesNames) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.filesNames = filesNames;
    }

    @Override
    public int getCount() {
        return filesNames.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View itemView = inflater.inflate(R.layout.gallery_item, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.gallery_item_image);

        String fileName = filesNames.get(position);
        Bitmap myBitmap = BitmapFactory.decodeFile(context.getFilesDir() + File.separator + fileName);
        imageView.setImageBitmap(myBitmap);

        container.addView(itemView);
        return itemView;
    }
    @Override
    public int getItemPosition(Object object){
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void addFileName(String fileName) {
        filesNames.add(fileName);
        notifyDataSetChanged();
    }

    public void replaceFileNameForIndex(Integer index, String fileName) {
        filesNames.remove(index.intValue());
        filesNames.add(index, fileName);
        notifyDataSetChanged();
    }

    public void removeFileName(Integer position) {
        filesNames.remove(position.intValue());
        notifyDataSetChanged();
    }
}
