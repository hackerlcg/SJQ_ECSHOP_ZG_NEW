package com.ecjia.hamster.activity.goods.push.goodstype;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ecjia.entity.Species;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecmoban.android.shopkeeper.sijiqing.databinding.ItemGoodsTypePushBinding;

import java.util.ArrayList;

import gear.yc.com.gearlibrary.ui.adapter.GearRecyclerViewAdapter;

/**
 * ecjia-shopkeeper-android
 * Created by YichenZ on 2017/3/21 10:38.
 */

public class GoodsTypePushAdapter extends GearRecyclerViewAdapter<Species.SpeciBean,GoodsTypePushAdapter.ViewHolder>{


    public GoodsTypePushAdapter(Context mContext, ArrayList<Species.SpeciBean> mData) {
        super(mContext, mData);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemGoodsTypePushBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext),
                R.layout.item_goods_type_push, parent, false);
        ViewHolder viewHolder = new ViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.mBinding.getRoot().setOnClickListener(this);
        holder.mBinding.getRoot().setTag(mData.get(position));
        holder.mBinding.tvName.setText(mData.get(position).getCatName());
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ItemGoodsTypePushBinding mBinding;
        public ViewHolder(ItemGoodsTypePushBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }
}
