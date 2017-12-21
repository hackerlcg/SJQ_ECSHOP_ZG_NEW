package com.ecjia.hamster.adapter.adapter.my;

import com.ecjia.hamster.adapter.adapter.MultiItemTypeAdapter;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-02-13.
 */

public interface IBaseNormalAdapter<T> {

    void setOnItemClickListener(MultiItemTypeAdapter.OnItemClickListener<T> itemClickListener);

    void setOnItemLongClickListener(MultiItemTypeAdapter.OnItemLongClickListener<T> itemLongClickListener);

    void notifyDataSetChanged();

    void netWorkErrorFooter();

    void setIsRefresh(boolean isRefresh);

}
