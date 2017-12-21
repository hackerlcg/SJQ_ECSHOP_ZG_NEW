package com.ecjia.util;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ecmoban.android.shopkeeper.sijiqing.R;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;



/**
 * Created by hank on 2015/7/6.
 */
public class ImageUtils {

    /**
     * 一般形式，一般默认图
     *
     * @param url
     * @param imageView
     */
    public static void showImage(Context context, String url, ImageView imageView) {
        if (context == null || imageView == null) return;
        showImage(context, url, imageView, R.drawable.default_circle_image);
    }

    /**
     * 自定义默认图
     *
     * @param url         图片地址
     * @param imageView
     * @param placeHolder 默认图
     */
    public static void showImage(Context context, String url, ImageView imageView, int placeHolder) {
        if (context == null || imageView == null) return;
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(placeHolder)
                .crossFade()
                .into(imageView);
    }

    public static void showImageNoAnimate(Context context, String url, ImageView imageView, int placeHolder) {
        if (context == null || imageView == null) return;
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(placeHolder)
                .dontAnimate()
                .into(imageView);
    }

    /**
     * 自定义默认图,资源图片模式FIX_CENTER,区分占位图模式
     *
     * @param url         图片地址
     * @param imageView
     * @param placeHolder 默认图
     */
    public static void showImageFitCenter(Context context, String url, ImageView imageView, int placeHolder) {
        if (context == null || imageView == null) return;
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(placeHolder)
                .crossFade()
                .fitCenter()
                .into(imageView);
    }

    /**
     * 没有默认图
     *
     * @param url       图片地址
     * @param imageView
     */
    public static void showImageNoPlaceHolder(Context context, String url, ImageView imageView) {
        if (context == null || imageView == null) return;
        Glide.with(context)
                .load(  url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(imageView);
    }

    /**
     * 一般默认图的圆角imageView
     *
     * @param url
     * @param imageView
     * @param radius    圆角半径
     */
    public static void showImageCircle(Context context, String url, ImageView imageView, int radius) {
        if (context == null || imageView == null) return;
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.default_circle_image)
                .crossFade()
                .bitmapTransform(new RoundedCornersTransformation(imageView.getContext(), radius, 0))
                .into(imageView);
    }

    /**
     * 带头像默认图的圆形imageView
     * 头像默认图 圆形
     *
     * @param url
     * @param imageView
     */
    public static void showImageCircleHead(Context context, String url, ImageView imageView) {
        if (context == null || imageView == null) return;
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.default_circle_image)
                .crossFade()
                .bitmapTransform(new CropCircleTransformation(imageView.getContext()))
                .into(imageView);
    }

    /**
     * 加载本地的，一般默认图
     *
     * @param drawableInt 本地drawable图片资源
     * @param imageView
     */
    public static void showImage(Context context, int drawableInt, ImageView imageView) {
        if (context == null || imageView == null) return;
        showImage(context, drawableInt, imageView, R.drawable.default_circle_image);
    }

    /**
     * 加载本地的
     *
     * @param drawableInt 本地drawable图片资源
     * @param imageView
     * @param placeHolder 占位图
     */
    public static void showImage(Context context, int drawableInt, ImageView imageView, int placeHolder) {
        if (context == null || imageView == null) return;
        Glide.with(context)
                .load(drawableInt)
                .placeholder(placeHolder)
                .crossFade()
                .into(imageView);
    }

    /**
     * @param filePath  图片sdcard 路径
     * @param imageView
     */
    public static void showImageFilePath(Context context, String filePath, ImageView imageView) {
        if (context == null || imageView == null) return;
        showImageFilePath(context, filePath, imageView, R.drawable.default_circle_image);
    }

    /**
     * 图片sdcard 路径
     *
     * @param context
     * @param filePath
     * @param imageView
     * @param placeholder
     */
    public static void showImageFilePath(Context context, String filePath, ImageView imageView, int placeholder) {
        if (context == null || imageView == null) return;
        Glide.with(context)
                .load("file://" + filePath)
                .placeholder(placeholder)
                .crossFade()
                .into(imageView);
    }

    public static void showImageFilePathNoAnimate(Context context, String filePath, ImageView imageView, int placeholder) {
        if (context == null || imageView == null) return;
        Glide.with(context)
                .load("file://" + filePath)
                .placeholder(placeholder)
                .dontAnimate()
                .into(imageView);
    }

    /**
     * 一般形式，一般默认图
     *
     * @param url
     * @param imageView
     */
    public static void showImage(Fragment fragment, String url, ImageView imageView) {
        if (fragment == null || imageView == null) return;
        showImage(fragment, url, imageView, R.drawable.default_circle_image);
    }

    /**
     * 自定义默认图
     *
     * @param url         图片地址
     * @param imageView
     * @param placeHolder 默认图
     */
    public static void showImage(Fragment fragment, String url, ImageView imageView, int placeHolder) {
        if (fragment == null || imageView == null) return;
        Glide.with(fragment)
                .load( url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(placeHolder)
                .crossFade()
                .into(imageView);
    }

    /**
     * 自定义默认图,资源图片模式FIX_CENTER,区分占位图模式
     *
     * @param url         图片地址
     * @param imageView
     * @param placeHolder 默认图
     */
    public static void showImageFitCenter(Fragment context, String url, ImageView imageView, int placeHolder) {
        if (context == null || imageView == null) return;
        Glide.with(context)
                .load( url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(placeHolder)
                .crossFade()
                .fitCenter()
                .into(imageView);
    }

    /**
     * 没有默认图
     *
     * @param url       图片地址
     * @param imageView
     */
    public static void showImageNoPlaceHolder(Fragment fragment, String url, ImageView imageView) {
        if (fragment == null || imageView == null) return;
        Glide.with(fragment)
                .load( url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(imageView);
    }

    /**
     * 一般默认图的圆角imageView
     *
     * @param url
     * @param imageView
     * @param radius    圆角半径
     */
    public static void showImageCircle(Fragment fragment, String url, ImageView imageView, int radius) {
        if (fragment == null || imageView == null) return;
        Glide.with(fragment)
                .load( url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.default_circle_image)
                .crossFade()
                .bitmapTransform(new RoundedCornersTransformation(imageView.getContext(), radius, 0))
                .into(imageView);
    }

    /**
     * 带头像默认图的圆形imageView
     * 头像默认图 圆形
     *
     * @param url
     * @param imageView
     */
    public static void showImageCircleHead(Fragment fragment, String url, ImageView imageView, int headImgId) {
        if (fragment == null || imageView == null) return;
        Glide.with(fragment)
                .load( url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.default_circle_image)
                .crossFade()
                .bitmapTransform(new CropCircleTransformation(imageView.getContext()))
                .into(imageView);
    }

    /**
     * 加载本地的，一般默认图
     *
     * @param drawableInt 本地drawable图片资源
     * @param imageView
     */
    public static void showImage(Fragment fragment, int drawableInt, ImageView imageView) {
        if (fragment == null || imageView == null) return;
        Glide.with(fragment)
                .load(drawableInt)
                .placeholder(R.drawable.default_circle_image)
                .crossFade()
                .into(imageView);
    }

    /**
     * 加载本地的
     *
     * @param drawableInt 本地drawable图片资源
     * @param imageView
     * @param placeHolder 占位图
     */
    public static void showImage(Fragment fragment, int drawableInt, ImageView imageView, int placeHolder) {
        if (fragment == null || imageView == null) return;
        Glide.with(fragment)
                .load(drawableInt)
                .placeholder(placeHolder)
                .crossFade()
                .into(imageView);
    }

    /**
     * @param filePath  图片sdcard 路径
     * @param imageView
     */
    public static void showImageFilePath(Fragment fragment, String filePath, ImageView imageView) {
        if (fragment == null || imageView == null) return;
        Glide.with(fragment)
                .load("file://" + filePath)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.default_circle_image)
                .crossFade()
                .into(imageView);
    }

//    load SD卡资源：load("file://"+ Environment.getExternalStorageDirectory().getPath()+"/test.jpg")
//    load assets资源：load("file:///android_asset/f003.gif")
//    load raw资源：load("Android.resource://com.frank.glide/raw/raw_1")或load("android.resource://com.frank.glide/raw/"+R.raw.raw_1)
//    load drawable资源：load("android.resource://com.frank.glide/drawable/news")或load("android.resource://com.frank.glide/drawable/"+R.drawable.news)
//    load ContentProvider资源：load("content://media/external/images/media/139469")
}
