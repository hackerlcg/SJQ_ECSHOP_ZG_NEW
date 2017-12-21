package com.ecjia.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;


/**
 * Created by SSD on 2016/3/3 17:27.
 */
public abstract class LibFragment<T> extends NewBaseFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutId(), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        init(view, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dispose();
    }

    public abstract int getLayoutId();//获取布局id

    public abstract void init(View view, @Nullable Bundle savedInstanceState);//初始化操作

    public abstract void dispose();//销毁界面时，RxJava取消事件订阅

//    public abstract View getReplaceView();//需要替换loading布局的控件

}
