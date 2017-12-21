package com.ecjia.hamster.adapter.adapter.wrapper;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ecjia.hamster.adapter.adapter.base.ViewHolder;
import com.ecjia.hamster.adapter.adapter.utils.WrapperUtils;
import com.ecmoban.android.shopkeeper.sijiqing.R;

/**
 * Created by zhy on 16/6/23.
 */
public class LoadMoreWrapper<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private int type = ITEM_TYPE_LOAD_MORE;//底部布局类型
    private static final int ITEM_TYPE_LOAD_MORE = 1000;//加载更多，正在加载
    private static final int ITEM_TYPE_NO_MORE = 1001;//加载更多：没有更多
    private static final int ITEM_TYPE_NET_WORK_ERROR = 1002;//加载更多:没有网络

    private RecyclerView recyclerView;
    private boolean isShowEnd;//item是否超过一屏

    private int lastLoadMorePosition;//上一次上拉加载跟多，底部view的位置，如果相同不调用加载更多接口

    private RecyclerView.Adapter<RecyclerView.ViewHolder> mInnerAdapter;

    public LoadMoreWrapper(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
        mInnerAdapter = adapter;
    }

    private boolean isShowLoadMore(int position) {
        return (position >= mInnerAdapter.getItemCount());
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowLoadMore(position)) {
            return type;
        }
        return mInnerAdapter.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        if (recyclerView != null && !isShowEnd) {
            isShowEnd = recyclerView.canScrollVertically(-1);//检测能否能上滑动
        }
        if (viewType == ITEM_TYPE_LOAD_MORE) {//正在加载更多
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_load_more, parent, false);
            itemView.setVisibility(isShowEnd ? View.VISIBLE : View.GONE);
            return ViewHolder.createViewHolder(parent.getContext(), itemView);
        } else if (viewType == ITEM_TYPE_NO_MORE) {//没有更多提示
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_no_more, parent, false);
            itemView.setVisibility(isShowEnd ? View.VISIBLE : View.GONE);
            return ViewHolder.createViewHolder(parent.getContext(), itemView);
        } else if (viewType == ITEM_TYPE_NET_WORK_ERROR) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_more_net_work_error, parent, false);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnLoadMoreListener != null && !isRefresh) {
                        mOnLoadMoreListener.onLoadMoreRequested();
                        type = ITEM_TYPE_LOAD_MORE;
                        notifyDataSetChanged();
                    }
                }
            });
            itemView.setVisibility(isShowEnd ? View.VISIBLE : View.GONE);
            return new ViewHolder(parent.getContext(), itemView);
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);//一般item
    }

    /**
     * 当重新请求数据时：包括下拉刷新，上拉加载，刷新数据后清除上一次底部位置为0,为了保证下次滑动到底部可以自动加载更多
     */
    public void initializeLastLoadMorePosition() {
        lastLoadMorePosition = 0;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isShowLoadMore(position)) {
            if (mOnLoadMoreListener != null && type == ITEM_TYPE_LOAD_MORE && !isRefresh) {
                int dataSize = mInnerAdapter instanceof HeaderAndFooterWrapper ? ((HeaderAndFooterWrapper) mInnerAdapter).getRealItemCount() : mInnerAdapter.getItemCount();
                if (dataSize > 0 && position >= mInnerAdapter.getItemCount() && type == ITEM_TYPE_LOAD_MORE && lastLoadMorePosition != position) {
                    if (mOnLoadMoreListener != null) {
                        mOnLoadMoreListener.onLoadMoreRequested();
                        lastLoadMorePosition = position;
                    }
                }
            }
            return;
        }
        mInnerAdapter.onBindViewHolder(holder, position);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        WrapperUtils.onAttachedToRecyclerView(mInnerAdapter, recyclerView, new WrapperUtils.SpanSizeCallback() {
            @Override
            public int getSpanSize(GridLayoutManager layoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position) {
                if (isShowLoadMore(position))
                    return layoutManager.getSpanCount();
                if (oldLookup != null)
                    return oldLookup.getSpanSize(position);
                return 1;
            }
        });
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        mInnerAdapter.onViewAttachedToWindow(holder);
        if (isShowLoadMore(holder.getLayoutPosition())) {
            setFullSpan(holder);
        }
    }

    private void setFullSpan(RecyclerView.ViewHolder holder) {
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(true);
        }
    }

    @Override
    public int getItemCount() {
        return mInnerAdapter.getItemCount() + 1;
    }

    public interface OnLoadMoreListener {
        void onLoadMoreRequested();
    }

    private boolean isRefresh;//是否正在刷新，正在刷新不加载下一页

    public void setRefresh(boolean isRefresh) {
        this.isRefresh = isRefresh;
    }

    private OnLoadMoreListener mOnLoadMoreListener;

    public LoadMoreWrapper setOnLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        if (loadMoreListener != null) {
            mOnLoadMoreListener = loadMoreListener;
        }
        return this;
    }

    public void noMore(boolean isNoMore) {
        type = isNoMore ? ITEM_TYPE_NO_MORE : ITEM_TYPE_LOAD_MORE;
    }

    public void netWorkErrorFooter() {
        type = ITEM_TYPE_NET_WORK_ERROR;
    }
}
