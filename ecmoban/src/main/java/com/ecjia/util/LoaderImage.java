package com.ecjia.util;

import android.widget.ImageView;

/**
 * Created by Adam on 2015/2/4.
 */
public abstract class LoaderImage {

    public abstract  void setImageRes(ImageView imageView,String url);
    public abstract void setImageRes(ImageView imageView,int resource);
    public abstract void setImageRes(ImageView imageView,String url,Object anything);
}
