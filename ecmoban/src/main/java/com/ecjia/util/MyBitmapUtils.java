package com.ecjia.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;

/**
 * Created by Administrator on 2015/4/29.
 */
public class MyBitmapUtils {
    Drawable d=null;
    private BitmapDisplayConfig config,small_img_config;
    private static MyBitmapUtils instance=null;
    BitmapUtils bitmapUtils;
    private BitmapDisplayConfig tanconfig2;

    public static  MyBitmapUtils getInstance(Context context){
        if(null==instance){
            instance=new MyBitmapUtils();
            instance.d=context.getResources().getDrawable(R.drawable.default_image);
            instance.bitmapUtils=new BitmapUtils(context);
            instance.bitmapUtils.configMemoryCacheEnabled(true);
            instance.bitmapUtils.configDiskCacheEnabled(false);
            instance.bitmapUtils.configDefaultLoadingImage(instance.d);
            instance.bitmapUtils.configDefaultLoadFailedImage(instance.d);
            instance.config=new BitmapDisplayConfig();
            instance.small_img_config=new BitmapDisplayConfig();
            instance.small_img_config.setLoadFailedDrawable(context.getResources().getDrawable(R.drawable.iv_user_default));
            instance.small_img_config.setLoadingDrawable(context.getResources().getDrawable(R.drawable.iv_user_default));

            instance.tanconfig2 = new BitmapDisplayConfig();
            instance.tanconfig2.setLoadFailedDrawable(context.getResources().getDrawable(R.drawable.default_image));
            instance.tanconfig2.setLoadingDrawable(context.getResources().getDrawable(R.drawable.default_image));
        }
        return instance;
    }
    public  <T extends android.view.View>  void displayImage(T container,String url,boolean option){
        if(option) {
            config.setBitmapConfig(Bitmap.Config.RGB_565);
            if (!(container == null || url == null)) {
                if (!url.equals((String)container.getTag())) {
                    instance.bitmapUtils.display(container,url);
                    container.setTag(url);
                }
            }
        }else{
            if (!(container == null || url == null)) {
                if (!url.equals((String)container.getTag())) {
                    instance.bitmapUtils.display(container,url);
                    container.setTag(url);
                }
            }
        }
    }

    public  <T extends android.view.View>  void displayImage(T container,String url){
        if (!(container == null || url == null)) {
            if (!url.equals((String)container.getTag())) {
                instance.bitmapUtils.display(container,url);
                container.setTag(url);
            }
        }
    }

    public  <T extends android.view.View>  void displaySmallImage(T container,String url){
        if (!(container == null || url == null)) {
            if (!url.equals((String)container.getTag())) {
                instance.bitmapUtils.display(container,url,instance.small_img_config);
                container.setTag(url);
            }
        }
    }

    public <T extends android.view.View> void displayTanImage2(T container, String url) {
        if (!(container == null || url == null)) {
            if (!url.equals((String) container.getTag())) {
                bitmapUtils.display(container, url, instance.tanconfig2);
                container.setTag(url);
            }
        }
    }

}
