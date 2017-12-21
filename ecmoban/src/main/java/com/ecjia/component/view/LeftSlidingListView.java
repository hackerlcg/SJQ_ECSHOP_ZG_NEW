package com.ecjia.component.view;

import com.ecjia.consts.AppConst;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.util.LG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Adam on 2015/1/6.
 */
public class LeftSlidingListView extends XListView {
    private Boolean mIsHorizontal;
    private View mPreItemView;
    public static View mCurrentItemView;
    private float mFirstX;
    private float mFirstY;
    private int mRightViewWidth;
    private final int mDuration = 100;
    private final int mDurationStep = 10;
    public static boolean isleftsliding = false;
    public static boolean ishidden = false;
    private int realPosition;
    private boolean mIsShown;
    public int flag=0;
    private View HeadView;
    public LeftSlidingListView(Context context) {
        this(context, null);
    }

    public LeftSlidingListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LeftSlidingListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
                R.styleable.swipelistviewstyle);
        // 获取自定义属性和默认值
        mRightViewWidth = (int) mTypedArray.getDimension(
                R.styleable.swipelistviewstyle_right_width, AppConst.getResources(this.getContext()).getDimension(R.dimen.dim450));
        mTypedArray.recycle();
    }

    public View getHeadView() {
        return HeadView;
    }

    public void setHeadView(View headView) {
        HeadView = headView;
    }

    /**
     * return true, deliver to listView. return false, deliver to child. if
     * move, return true
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        float lastX = ev.getX();
        float lastY = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                flag = 0;
                mIsHorizontal = null;
                mFirstX = lastX;
                mFirstY = lastY;
                int motionPosition = pointToPosition((int) mFirstX, (int) mFirstY);
                realPosition = motionPosition;

                if (motionPosition < 0)
                    return true;
                if (motionPosition >= 0) {
                    View currentItemView = getChildAt(motionPosition
                            - getFirstVisiblePosition());
                    mPreItemView = mCurrentItemView;
                    mCurrentItemView = currentItemView;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                float dx = lastX - mFirstX;
                float dy = lastY - mFirstY;

                if (Math.abs(dx) >= 5 && Math.abs(dy) >= 5) {
                    return true;
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mIsShown
                        && (mPreItemView != mCurrentItemView || isHitCurItemLeft(lastX))) {
                    /**
                     * 情况一： 一个Item的右边布局已经显示， 这时候点击任意一个item, 那么那个右边布局显示的item隐藏其右边布局
                     */
                    flag = 1;
                    hiddenRight(mPreItemView);
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    private boolean isHitCurItemLeft(float x) {
        return x < getWidth() - mRightViewWidth;
    }

    /**
     * @param dx
     * @param dy
     * @return judge if can judge scroll direction
     */
    private boolean judgeScrollDirection(float dx, float dy) {
        boolean canJudge = true;
        if (Math.abs(dx) > 30 && Math.abs(dx) > 2 * Math.abs(dy)) {
            mIsHorizontal = true;
        } else if (Math.abs(dy) > 30 && Math.abs(dy) > 2 * Math.abs(dx)) {
            mIsHorizontal = false;
        } else {
            canJudge = false;
        }
        return canJudge;
    }

    /**
     * return false, can't move any direction. return true, cant't move
     * vertical, can move horizontal. return super.onTouchEvent(ev), can move
     * both.
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        float lastX = ev.getX();
        float lastY = ev.getY();
        if (super.getFootView() != mCurrentItemView&&mCurrentItemView!=HeadView) {

            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    flag = 0;
                    break;

                case MotionEvent.ACTION_MOVE:
                    float dx = lastX - mFirstX;
                    float dy = lastY - mFirstY;
                    if (mIsHorizontal == null) {
                        if (!judgeScrollDirection(dx, dy)) {
                            break;
                        }
                    }

                    if (mIsHorizontal) {
                        if (mIsShown && mPreItemView != mCurrentItemView) {
                            /**
                             * 情况二： 一个Item的右边布局已经显示，
                             * 这时候左右滑动另外一个item,那个右边布局显示的item隐藏其右边布局
                             * 向左滑动只触发该情况，向右滑动还会触发情况五
                             */
                            flag = 1;
                            hiddenRight(mPreItemView);
                        }

                        if (mIsShown && mPreItemView == mCurrentItemView) {
                            dx = dx - mRightViewWidth;
                        }
                        if (dx < 0 && dx > -mRightViewWidth) {
                            if (!isleftsliding) {
                                if (needReturn(mCurrentItemView)) return true;
                                mCurrentItemView.scrollTo((int) (-dx), 0);
                            }
//                        mCurrentItemView.scrollTo((int) (-dx), 0);
                        }
                        return true;
                    } else {
                        if (mIsShown) {
                            /**
                             * 情况三： 一个Item的右边布局已经显示，
                             * 这时候上下滚动ListView,那么那个右边布局显示的item隐藏其右边布局
                             */
                            flag = 1;
                            hiddenRight(mPreItemView);
                        }
                    }

                    break;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
//                clearPressedState();
                    if (mIsShown) {
                        /**
                         * 情况四： 一个Item的右边布局已经显示， 这时候左右滑动当前一个item,那个右边布局显示的item隐藏其右边布局
                         */
                        flag = 1;
                        hiddenRight(mPreItemView);
                    }

                    if (mIsHorizontal != null && mIsHorizontal) {
                        if (mFirstX - lastX > mRightViewWidth / 2) {
                            if (!isleftsliding) {
                                showRight(mCurrentItemView);
                            }
//                        showRight(mCurrentItemView);
                        } else {
                            /**
                             * 情况五：
                             * <p>
                             * 向右滑动一个item,且滑动的距离超过了右边View的宽度的一半，隐藏之。
                             */
                            flag = 1;
                            hiddenRight(mCurrentItemView);
                        }
                        return true;
                    }
                    break;
            }
        }
        return super.onTouchEvent(ev);
    }

    private void clearPressedState() {
        mCurrentItemView.setPressed(false);
        setPressed(false);
        refreshDrawableState();
    }

    private void showRight(View view) {

        if (needReturn(view)) return;
        Message msg = new MoveHandler().obtainMessage();
        msg.obj = view;
        if (!isleftsliding) {
            msg.arg1 = view.getScrollX();
        }
        msg.arg1 = view.getScrollX();
        msg.arg2 = mRightViewWidth;
        msg.sendToTarget();
        mIsShown = true;
    }

    public void hiddenRight(View view) {
        if (mCurrentItemView == null) {
            return;
        }
        if (needReturn(view)) return;
        Message msg = new MoveHandler().obtainMessage();//
        msg.obj = view;
        msg.arg1 = view.getScrollX();
        msg.arg2 = 0;
        msg.sendToTarget();
        mIsShown = false;
    }

    public boolean isIsShown() {
        boolean fin = true;
        LG.e("===finf==="+flag);
        if (flag == 1) {
            fin = false;
        }
        return fin;
    }

    /**
     * show or hide right layout animation
     */
    @SuppressLint("HandlerLeak")
    class MoveHandler extends Handler {
        int stepX = 0;
        int fromX;
        int toX;
        View view;
        private boolean mIsInAnimation = false;

        private void animatioOver() {
            mIsInAnimation = false;
            stepX = 0;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (stepX == 0) {
                if (mIsInAnimation) {
                    return;
                }
                mIsInAnimation = true;
                view = (View) msg.obj;
                fromX = msg.arg1;
                toX = msg.arg2;
                stepX = (int) ((toX - fromX) * mDurationStep * 1.0 / mDuration);
                if (stepX < 0 && stepX > -1) {
                    stepX = -1;
                } else if (stepX > 0 && stepX < 1) {
                    stepX = 1;
                }
                if (Math.abs(toX - fromX) < 10) {
                    view.scrollTo(toX, 0);
                    animatioOver();
                    return;
                }
            }

            fromX += stepX;
            boolean isLastStep = (stepX > 0 && fromX > toX)
                    || (stepX < 0 && fromX < toX);
            if (isLastStep) {
                fromX = toX;
            }
            view.scrollTo(fromX, 0);
            invalidate();
            if (!isLastStep) {
                this.sendEmptyMessageDelayed(0, mDurationStep);
            } else {
                animatioOver();
            }
        }
    }

    public int getRightViewWidth() {
        return mRightViewWidth;
    }

    public void setRightViewWidth(int mRightViewWidth) {
        this.mRightViewWidth = mRightViewWidth;
    }

    public void deleteItem(View v) {
        hiddenRight(v);
    }

    private boolean needReturn(View view) {
        return view == null || realPosition < 0;
    }
}
