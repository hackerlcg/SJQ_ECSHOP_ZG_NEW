package com.ecjia.hamster.activity.goods.push.type;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ecjia.base.LibFragment;
import com.ecjia.entity.Category;
import com.ecjia.hamster.adapter.adapter.wrapper.HeaderAndFooterWrapper;
import com.ecjia.util.MyBitmapUtils;
import com.ecjia.util.common.DensityUtil;
import com.ecmoban.android.shopkeeper.sijiqing.R;

import butterknife.BindView;


/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-02-13.
 */

@SuppressLint("ValidFragment")
public class SearchChildFragment extends LibFragment {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;//分类的控件

    private Category parentData;
    private PushTypeParentAdapter adapter;
    private HeaderAndFooterWrapper wrapper;
    private ImageView headImageView;//顶部图片

    public SearchChildFragment(Category parentData) {
        super();
        this.parentData = parentData;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_search_child;
    }

    @Override
    public void init(View view, @Nullable Bundle savedInstanceState) {
        getHeadView();
        adapter = new PushTypeParentAdapter(getActivity(), parentData.getChildren(), parentData.getName());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        wrapper = new HeaderAndFooterWrapper(adapter);
        wrapper.addHeaderView(headImageView);
        mRecyclerView.setAdapter(wrapper);
        MyBitmapUtils.getInstance(getActivity()).displayTanImage2(headImageView, parentData.getImage());
    }

    @Override
    public void dispose() {}

    private void getHeadView() {
        headImageView = new ImageView(getContext());
        int width = DensityUtil.getDisplayMetricsWidth(getContext())-(int)getResources().getDimension(R.dimen.layout_marginLR)*2;
        headImageView.setLayoutParams(new ViewGroup.LayoutParams(width, width/15*8));
//        headImageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        headImageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)getResources().getDimension(R.dimen.px250)));
//        context.getResources().getDimension(R.dimen.seven_dp)   DensityUtil.getDisplayMetricsWidth(context) * 5 / 14 - distance,
//        headImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        headImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        headImageView.setScaleType(ImageView.ScaleType.FIT_XY);
    }

}
