package com.ecjia.hamster.adapter.adapter.my;

import android.content.Context;
import android.view.View;

import com.ecjia.hamster.adapter.adapter.CommonAdapter;
import com.ecjia.hamster.adapter.adapter.MultiItemTypeAdapter;
import com.ecjia.hamster.adapter.adapter.base.ViewHolder;
import com.ecjia.hamster.adapter.adapter.wrapper.HeaderAndFooterWrapper;

import java.util.List;

/**
 * 可以添加头部和尾部，上拉自动加载更多（没有更多） Adapter
 * HeaderFooterLoadMoreAdapter = CommonAdapter + HeaderAndFooterWrapper  ; //header + footer
 * Created by s on 2016/8/31.
 */
public abstract class HeaderFooterAdapter<T> implements IBaseNormalAdapter<T> {
    private CommonAdapter<T> adapter;
    private HeaderAndFooterWrapper headerAndFooterWrapper;
    public HeaderFooterAdapter(Context context, int layoutId, List<T> datas) {
        adapter = new CommonAdapter<T>(context, layoutId, datas) {
            @Override
            public void convert(ViewHolder holder, T t, int position) {
                HeaderFooterAdapter.this.convert(holder, t, position);
            }
        };
        headerAndFooterWrapper = new HeaderAndFooterWrapper(adapter);
    }

    public HeaderAndFooterWrapper adapter() {
        return headerAndFooterWrapper;
    }

    @Override
    public void setOnItemClickListener(MultiItemTypeAdapter.OnItemClickListener<T> itemClickListener) {
        adapter.setOnItemClickListener(itemClickListener);
    }

    @Override
    public void setOnItemLongClickListener(MultiItemTypeAdapter.OnItemLongClickListener<T> itemLongClickListener) {
        adapter.setOnItemLongClickListener(itemLongClickListener);
    }

    @Override
    public void notifyDataSetChanged() {
    }

    @Override
    public void setIsRefresh(boolean isRefresh) {

    }

    @Override
    public void netWorkErrorFooter() {
    }

    public void addHeaderView(View view) {
        headerAndFooterWrapper.addHeaderView(view);
    }

    public void addFootView(View view) {
        headerAndFooterWrapper.addFootView(view);
    }

    public abstract void convert(ViewHolder holder, T t, int position);


}
