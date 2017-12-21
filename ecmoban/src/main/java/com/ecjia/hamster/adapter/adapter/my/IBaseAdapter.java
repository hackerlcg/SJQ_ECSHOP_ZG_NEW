package com.ecjia.hamster.adapter.adapter.my;


import com.ecjia.hamster.adapter.adapter.MultiItemTypeAdapter;
import com.ecjia.hamster.adapter.adapter.wrapper.LoadMoreWrapper;

/**
 * Created by yubao on 2016/9/25.
 */
public interface IBaseAdapter<T> {

    LoadMoreWrapper adapter();

    void setOnItemClickListener(MultiItemTypeAdapter.OnItemClickListener<T> itemClickListener);

    void setOnItemLongClickListener(MultiItemTypeAdapter.OnItemLongClickListener<T> itemLongClickListener);

    void setOnLoadMoreListener(LoadMoreWrapper.OnLoadMoreListener onLoadMoreListener);

    void noMore(boolean isNoMore);

    void notifyDataSetChanged();

    void netWorkErrorFooter();

    void setIsRefresh(boolean isRefresh);

}