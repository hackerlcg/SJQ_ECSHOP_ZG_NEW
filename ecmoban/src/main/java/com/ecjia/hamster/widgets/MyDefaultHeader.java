package com.ecjia.hamster.widgets;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.ecmoban.android.shopkeeper.sijiqing.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;


/**
 * 类名介绍：默认系统下拉刷新头部
 * Created by sun
 * Created time 2017-03-02.
 */

public class MyDefaultHeader extends FrameLayout implements PtrUIHandler {
    private LinearLayout mContainer;
    private ImageView mArrowImageView;
    private ProgressBar mProgressBar;
    private TextView mHintTextView;
    private TextView mTimeTextView;


    public AnimationDrawable animationDrawable;

    public MyDefaultHeader(Context context) {
        super(context);
        initViews( context);
    }

    public MyDefaultHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews( context);
    }

    public MyDefaultHeader(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews( context);
    }

    protected void initViews(Context context) {

        View header = LayoutInflater.from(getContext()).inflate(R.layout.xlistview_header, this);
        // 初始情况，设置下拉刷新view高度为0
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 0);
//        mContainer = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.xlistview_header, null);
//        addView(mContainer, lp);
        //setGravity(Gravity.BOTTOM);
        mArrowImageView = (ImageView) header.findViewById(R.id.xlistview_header_arrow);
        mHintTextView = (TextView) header.findViewById(R.id.xlistview_header_hint_textview);
        mProgressBar = (ProgressBar) header.findViewById(R.id.xlistview_header_progressbar);
        mTimeTextView = (TextView) header.findViewById(R.id.xlistview_header_time);

        mArrowImageView.setImageResource(R.drawable.animation_xlistview);
        animationDrawable = (AnimationDrawable) mArrowImageView.getDrawable();
        mTimeTextView.setText(date());
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {

    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        mHintTextView.setText("下拉刷新");
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        mHintTextView.setText("正在刷新");
        if (animationDrawable.isOneShot()) {
            animationDrawable.setOneShot(false);
        }
        animationDrawable.start();
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        mHintTextView.setText("刷新完成");
        if (animationDrawable.isRunning()) {
            animationDrawable.stop();
        }
        mTimeTextView.setText(date());
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        final int mOffsetToRefresh = frame.getOffsetToRefresh();
        final int currentPos = ptrIndicator.getCurrentPosY();
        final int lastPos = ptrIndicator.getLastPosY();
        if (currentPos < mOffsetToRefresh && lastPos >= mOffsetToRefresh) {
            if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
                crossRotateLineFromBottomUnderTouch();
            }
        } else if (currentPos > mOffsetToRefresh && lastPos <= mOffsetToRefresh) {
            if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
                crossRotateLineFromTopUnderTouch();
            }
        }
    }
    //step 2 下拉-达到了释放刷新的位置
    private void crossRotateLineFromTopUnderTouch() {
        mHintTextView.setText("释放刷新");
        //imageView.startAnimation(getAnim1());
    }

    //step 2 达到释放刷新的位置继续往上移动到不可释放刷新的位置
    private void crossRotateLineFromBottomUnderTouch() {
        mHintTextView.setText("下拉刷新");
        //imageView.startAnimation(getAnim2());
    }

    // 获取系统时间
    public String date() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String t = sdf.format(date);
        return t;
    }
}
