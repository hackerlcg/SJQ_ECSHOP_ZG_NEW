package com.ecjia.hamster.adapter.adapter.my;

import android.content.Context;
import android.view.View;

import com.ecjia.hamster.adapter.adapter.CommonAdapter;
import com.ecjia.hamster.adapter.adapter.MultiItemTypeAdapter;
import com.ecjia.hamster.adapter.adapter.base.ViewHolder;
import com.ecjia.hamster.adapter.adapter.wrapper.HeaderAndFooterWrapper;
import com.ecjia.hamster.adapter.adapter.wrapper.LoadMoreWrapper;

import java.util.List;

/**
 * 可以添加头部和尾部，上拉自动加载更多（没有更多） Adapter
 * HeaderFooterLoadMoreAdapter = CommonAdapter + HeaderAndFooterWrapper + LoadMoreWrapper ; //header + footer + 加载更多(没有更多)
 * Created by s on 2016/8/31.
 */
public abstract class HeaderFooterLoadMoreAdapter<T> implements IBaseAdapter<T> {
    private CommonAdapter<T> adapter;
    private HeaderAndFooterWrapper headerAndFooterWrapper;
    private LoadMoreWrapper loadMoreWrapper;

    public HeaderFooterLoadMoreAdapter(Context context, int layoutId, List<T> datas) {
        adapter = new CommonAdapter<T>(context, layoutId, datas) {
            @Override
            public void convert(ViewHolder holder, T t, int position) {
                HeaderFooterLoadMoreAdapter.this.convert(holder, t, position);
            }
        };
        headerAndFooterWrapper = new HeaderAndFooterWrapper(adapter);
        loadMoreWrapper = new LoadMoreWrapper(headerAndFooterWrapper);
    }

    @Override
    public LoadMoreWrapper adapter() {
        return loadMoreWrapper;
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
    public void setOnLoadMoreListener(LoadMoreWrapper.OnLoadMoreListener onLoadMoreListener) {
        loadMoreWrapper.setOnLoadMoreListener(onLoadMoreListener);
    }

    @Override
    public void noMore(boolean isNoMore) {
        loadMoreWrapper.noMore(isNoMore);
    }

    @Override
    public void notifyDataSetChanged() {
        loadMoreWrapper.notifyDataSetChanged();
        loadMoreWrapper.initializeLastLoadMorePosition();
    }

    @Override
    public void setIsRefresh(boolean isRefresh) {
        loadMoreWrapper.setRefresh(isRefresh);
    }

    @Override
    public void netWorkErrorFooter() {
        loadMoreWrapper.netWorkErrorFooter();
    }

    public void addHeaderView(View view) {
        headerAndFooterWrapper.addHeaderView(view);
    }

    public void addFootView(View view) {
        headerAndFooterWrapper.addFootView(view);
    }

    public abstract void convert(ViewHolder holder, T t, int position);

    public CommonAdapter<T> getAdapter() {
        return adapter;
    }

    public void setAdapter(CommonAdapter<T> adapter) {
        this.adapter = adapter;
    }
}
