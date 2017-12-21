package com.ecjia.hamster.adapter.adapter.my;

import android.content.Context;

import com.ecjia.hamster.adapter.adapter.CommonAdapter;
import com.ecjia.hamster.adapter.adapter.MultiItemTypeAdapter;
import com.ecjia.hamster.adapter.adapter.base.ViewHolder;
import com.ecjia.hamster.adapter.adapter.wrapper.LoadMoreWrapper;

import java.util.List;

/**
 * 不能添加头部和尾部，但是可以上拉自动加载更多（没有更多）
 * LoadMoreAdapter = CommonAdapter + LoadMoreWrapper ; // 加载更多
 * Created by s on 2016/8/31.
 */
public abstract class LoadMoreAdapter<T> implements IBaseAdapter<T> {
    private CommonAdapter<T> adapter;
    private LoadMoreWrapper loadMoreWrapper;

    public LoadMoreAdapter(Context context, int layoutId, List<T> datas) {
        adapter = new CommonAdapter<T>(context, layoutId, datas) {
            @Override
            public void convert(ViewHolder holder, T t, int position) {
                LoadMoreAdapter.this.convert(holder, t, position);
            }
        };
        loadMoreWrapper = new LoadMoreWrapper(adapter);
    }

    public Context getContext() {
        return adapter.getContext();
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
    public void netWorkErrorFooter() {
        loadMoreWrapper.netWorkErrorFooter();
        notifyDataSetChanged();
    }

    @Override
    public void setIsRefresh(boolean isRefresh) {
        loadMoreWrapper.setRefresh(isRefresh);
    }

    public abstract void convert(ViewHolder holder, T t, int position);
}
