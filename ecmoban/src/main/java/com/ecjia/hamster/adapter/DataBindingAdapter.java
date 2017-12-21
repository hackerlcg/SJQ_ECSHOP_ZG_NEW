package com.ecjia.hamster.adapter;

import android.databinding.BindingAdapter;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecjia.util.ImageLoaderForLV;

/**
 * SJQ_ECSHOP_MJ_NEW
 * Created by YichenZ on 2017/2/22 13:32.
 */

public class DataBindingAdapter {
    @BindingAdapter("android:src")
    public static void setImageUrl(ImageView view, String url){
        ImageLoaderForLV.getInstance(view.getContext()).setImageRes(view, url);
    }

    @BindingAdapter("android:text")
    public static void setTextView(TextView textView,Object object){
        if(object instanceof String && null == object){
            textView.setText("");
        } else {
            textView.setText(String.valueOf(object));
        }
    }

    @BindingAdapter("android:gravity")
    public static void setTextView(EditText editView, String str){
        if("".equals(str) || null == str){
            editView.setGravity(Gravity.RIGHT);
        } else {
            editView.setGravity(Gravity.LEFT);
        }
    }
}
