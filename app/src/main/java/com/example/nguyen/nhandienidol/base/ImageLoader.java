package com.example.nguyen.nhandienidol.base;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.module.AppGlideModule;
import com.example.nguyen.nhandienidol.R;

@GlideModule
public class ImageLoader extends AppGlideModule {
    private static ImageLoader instance;

    public ImageLoader() {
    }

    public static ImageLoader getInstance() {
        if (instance == null) {
            instance = new ImageLoader();
        }
        return instance;
    }

    public void loadImageIdol(final Context activity, final String url, final ImageView ivImage) {
        if (url == null || url.isEmpty()) {
            loadImageNothing(activity, ivImage);
            return;
        }
//        GlideApp.with(activity)
//                .load(url)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .placeholder(R.drawable.ic_launcher_background)
//                .error(R.drawable.ic_launcher_background)
//                .override(200, 200)
//                .centerCrop()
//                .into(ivImage)
//                .waitForLayout();
        Glide.with(activity)
                .load(url)
                .into(ivImage);
    }

    private void loadImageNothing(Context activity, ImageView imageView) {
        Glide.with(activity)
                .load(R.drawable.ic_launcher_background)
                .into(imageView);
    }
}
