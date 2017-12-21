package com.ecjia.util.gallery;

import android.content.Context;
import android.graphics.Color;
import android.os.Environment;


import com.ecmoban.android.shopkeeper.sijiqing.R;

import java.io.File;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.ThemeConfig;

/**
 * 需要使用相册，拍照处先调用 configGallery()方法
 */
public class GalleryImageUtils {
    //配置GalleryFinal //裁剪
    public static void configGallery(Context context) {
        ThemeConfig theme = new ThemeConfig.Builder()
                .setTitleBarBgColor(Color.rgb(84, 199, 140))
                .setFabNornalColor(Color.argb(200, 84, 199, 140))//设置Floating按钮Nornal状态颜色
                .setFabPressedColor(Color.rgb(84, 199, 140))//设置Floating按钮Pressed状态颜色
                .setCheckSelectedColor(Color.rgb(84, 199, 140))//选择框选中颜色
                .setCropControlColor(Color.rgb(84, 199, 140))//设置裁剪控制点和裁剪框颜色
//                .setCheckNornalColor(R.color.colorAccent)//选择框未选颜色
                .setPreviewBg(context.getResources().getDrawable(R.drawable.default_image))//设置预览页背景
                //.setTitleBarTextColor(R.color._ffffff)//标题栏文本字体颜色
                //.setTitleBarBgColor(R.color._242529)//标题栏背景颜色
                .setIconBack(R.drawable.ic_gf_back)//设置返回按钮icon
                .setIconCamera(0)//设置相机icon
                .setIconPreview(0)//设置预览按钮icon
                .setEditPhotoBgTexture(context.getResources().getDrawable(R.drawable.stripes))//设置图片编辑页面图片margin外背景
                .build();
//                setTitleBarIconColor//标题栏icon颜色，如果设置了标题栏icon，设置setTitleBarIconColor将无效
//                setFabNornalColor//设置Floating按钮Nornal状态颜色
//                setFabPressedColor//设置Floating按钮Pressed状态颜色
//                setIconCrop//设置裁剪icon
//                setIconRotate//设置旋转icon
//                setIconClear//设置清楚选择按钮icon（标题栏清除选择按钮）
//                setIconFolderArrow//设置标题栏文件夹下拉arrow图标
//                setIconDelete//设置多选编辑页删除按钮icon
//                setIconCheck//设置checkbox和文件夹已选icon
//                setIconFab//设置Floating按钮icon
//                setEditPhotoBgTexture//设置图片编辑页面图片margin外背景

        FunctionConfig functionConfig = new FunctionConfig.Builder()
//                .setEnableCamera(true)//开启相机功能
                .setEnableEdit(true)//开启编辑功能
//                .setEnablePreview(false)//是否开启预览功能
                .setEnableCrop(true)//开启裁剪功能
                .setCropSquare(true)//裁剪正方形
                .setEnableRotate(true)//开启旋转功能
//                .setMutiSelectMaxSize(1)//配置多选数量
//                .setMutiSelect(false)//配置是否多选
                .setForceCrop(true)//启动强制裁剪功能,一进入编辑页面就开启图片裁剪，不需要用户手动点击裁剪，此功能只针对单选操作
                .setForceCropEdit(false)//在开启强制裁剪功能时是否可以对图片进行编辑（也就是是否显示旋转图标和拍照图标）
                .build();
//                .setEnableRotate(true)//开启旋转功能
//                .setMutiSelect(true)//配置是否多选
//                .setCropWidth(100)//裁剪
//                .setCropHeight(100)//裁剪高度
//                .setSelected(List)//添加已选列表,只是在列表中默认呗选中不会过滤图片
//                .setFilter(List list)//添加图片过滤，也就是不在GalleryFinal中显示
//                .setRotateReplaceSource(true)//配置选择图片时是否替换原始图片，默认不替换
//                .setCropReplaceSource(true)//配置裁剪图片时是否替换原始图片，默认不替换
//        ImageLoader imageLoader = new UILImageLoader();
        ImageLoader imageLoader = new GlideImageLoader();

        CoreConfig coreConfig = new CoreConfig.Builder(context, imageLoader, theme)
                .setFunctionConfig(functionConfig)
                .setNoAnimcation(true)
                .setEditPhotoCacheFolder(new File(Environment.getExternalStorageDirectory(), "/DCIM/" + "SJQMASTER/"))
                .setTakePhotoFolder(new File(Environment.getExternalStorageDirectory(), "/DCIM/" + "SJQMASTER/"))
                .build();
        GalleryFinal.init(coreConfig);
    }

    public static void configGallerySelect(Context context) {
        ThemeConfig theme = new ThemeConfig.Builder()
                .setTitleBarBgColor(Color.rgb(84, 199, 140))
                .setFabNornalColor(Color.argb(200, 84, 199, 140))//设置Floating按钮Nornal状态颜色
                .setFabPressedColor(Color.rgb(84, 199, 140))//设置Floating按钮Pressed状态颜色
                .setCheckSelectedColor(Color.rgb(84, 199, 140))//选择框选中颜色
                .setCropControlColor(Color.rgb(84, 199, 140))//设置裁剪控制点和裁剪框颜色
//                .setCheckNornalColor(R.color.colorAccent)//选择框未选颜色
                .setPreviewBg(context.getResources().getDrawable(R.drawable.default_image))//设置预览页背景
                //.setTitleBarTextColor(R.color._ffffff)//标题栏文本字体颜色
                //.setTitleBarBgColor(R.color._242529)//标题栏背景颜色
                .setIconBack(R.drawable.ic_gf_back)//设置返回按钮icon
                .setIconCamera(0)//设置相机icon
                .setIconPreview(0)//设置预览按钮icon
                .setEditPhotoBgTexture(context.getResources().getDrawable(R.drawable.stripes))//设置图片编辑页面图片margin外背景
                .build();
//                setTitleBarIconColor//标题栏icon颜色，如果设置了标题栏icon，设置setTitleBarIconColor将无效
//                setFabNornalColor//设置Floating按钮Nornal状态颜色
//                setFabPressedColor//设置Floating按钮Pressed状态颜色
//                setIconCrop//设置裁剪icon
//                setIconRotate//设置旋转icon
//                setIconClear//设置清楚选择按钮icon（标题栏清除选择按钮）
//                setIconFolderArrow//设置标题栏文件夹下拉arrow图标
//                setIconDelete//设置多选编辑页删除按钮icon
//                setIconCheck//设置checkbox和文件夹已选icon
//                setIconFab//设置Floating按钮icon
//                setEditPhotoBgTexture//设置图片编辑页面图片margin外背景

        FunctionConfig functionConfig = new FunctionConfig.Builder()
//                .setEnableCamera(true)//开启相机功能
                .setEnableEdit(false)//开启编辑功能
                .setEnablePreview(true)//是否开启预览功能
                .setEnableCrop(false)//开启裁剪功能
                .setCropSquare(false)//裁剪正方形
                .setEnableRotate(true)//开启旋转功能
//                .setMutiSelect(true)//配置是否多选
                .setMutiSelectMaxSize(9)//配置多选数量
                .setForceCrop(false)//启动强制裁剪功能,一进入编辑页面就开启图片裁剪，不需要用户手动点击裁剪，此功能只针对单选操作
                .setForceCropEdit(false)//在开启强制裁剪功能时是否可以对图片进行编辑（也就是是否显示旋转图标和拍照图标）
                .build();
//                .setEnableRotate(true)//开启旋转功能
//                .setMutiSelect(true)//配置是否多选
//                .setCropWidth(100)//裁剪
//                .setCropHeight(100)//裁剪高度
//                .setSelected(List)//添加已选列表,只是在列表中默认呗选中不会过滤图片
//                .setFilter(List list)//添加图片过滤，也就是不在GalleryFinal中显示
//                .setRotateReplaceSource(true)//配置选择图片时是否替换原始图片，默认不替换
//                .setCropReplaceSource(true)//配置裁剪图片时是否替换原始图片，默认不替换
//        ImageLoader imageLoader = new UILImageLoader();
        ImageLoader imageLoader = new GlideImageLoader();

        CoreConfig coreConfig = new CoreConfig.Builder(context, imageLoader, theme)
                .setFunctionConfig(functionConfig)
                .setNoAnimcation(true)
                .setEditPhotoCacheFolder(new File(Environment.getExternalStorageDirectory(), "/DCIM/" + "SJQMASTER/"))
                .setTakePhotoFolder(new File(Environment.getExternalStorageDirectory(), "/DCIM/" + "SJQMASTER/"))
                .build();
        GalleryFinal.init(coreConfig);
    }
}
