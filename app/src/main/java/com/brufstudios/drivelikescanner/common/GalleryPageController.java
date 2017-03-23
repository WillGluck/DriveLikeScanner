package com.brufstudios.drivelikescanner.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.brufstudios.drivelikescanner.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GalleryPageController extends PagerAdapter {

    public static String IMAGE_NAME = "com.brufstudios.drivelikescanner.CollectedPageAdapter.IMAGE_NAME";

    private Context context;
    LayoutInflater inflater;
    ViewPager pager;
    List<String> filesNames = new ArrayList<>();

    public GalleryPageController(@NonNull Context context, ViewPager pager) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.pager = pager;
        this.pager.setAdapter(this);
        this.filesNames = new ArrayList<>();
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
    public int getItemPosition(Object object) {
        View view = (View) object;
        Integer position = filesNames.indexOf(view.getTag());
        if (-1 == position) {
            return POSITION_NONE;
        } else {
            return position;
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View itemView = inflater.inflate(R.layout.gallery_item, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.gallery_item_image);

        String fileName = filesNames.get(position);
        updateImageViewWithFile(imageView, fileName);

        itemView.setTag(fileName);
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    private void updateImageViewWithFile(ImageView imageView, String fileName) {
        Bitmap myBitmap = BitmapFactory.decodeFile(getFileFromName(fileName).getPath());
        imageView.setImageBitmap(myBitmap);
    }

    private File getFileFromName(String fileName) {
        return new File(context.getFilesDir() + File.separator + fileName);
    }

    public void addFileName(String fileName) {
        filesNames.add(fileName);
        notifyDataSetChanged();
        pager.setCurrentItem(pager.getCurrentItem() + 1);
    }

    public void replaceCurrentFileName(String fileName) {
        Integer currentItem = pager.getCurrentItem();
        String removedFileName = filesNames.get(currentItem.intValue());
        updateImageViewWithFile((ImageView) pager.findViewWithTag(removedFileName).findViewById(R.id.gallery_item_image), fileName);
        filesNames.set(currentItem, fileName);
        getFileFromName(removedFileName).delete();
    }

    public void removeCurrentFileName() {
        Integer currentItem = pager.getCurrentItem();
        String removedFileName = filesNames.remove(currentItem.intValue());
        getFileFromName(removedFileName).delete();
        notifyDataSetChanged();
    }

    public Bitmap getSelectedImage() {
        return BitmapFactory.decodeFile(getFileFromName(filesNames.get(pager.getCurrentItem())).getPath());
    }
}
