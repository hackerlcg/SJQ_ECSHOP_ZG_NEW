package com.ecjia.util;

import android.content.Context;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Adam on 2015/2/4.
 */
public class ImageLoaderForLV extends LoaderImage {

    private static Context mContext;
    private static ImageLoaderForLV imageLoaderForLV;

    public static ImageLoaderForLV getInstance(Context context) {
        if (imageLoaderForLV == null) {
            imageLoaderForLV = new ImageLoaderForLV();
        }
        mContext = context;
        return imageLoaderForLV;
    }

    private ImageLoaderForLV() {

    }

    @Override
    public void setImageRes(ImageView imageView, String url) {
        if (!(imageView == null || url == null)) {
            if (!url.equals((String)imageView.getTag())) {
                ImageLoader.getInstance().displayImage(url, imageView);
                imageView.setTag(url);
            }
        }
    }

    @Override
    public void setImageRes(ImageView imageView, int resource) {

    }

    @Override
    public void setImageRes(ImageView imageView, String url, Object anything) {

    }
}