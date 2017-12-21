package com.ecjia.hamster.activity.goods.push.setprice;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ecjia.entity.ValuePrice;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecmoban.android.shopkeeper.sijiqing.databinding.ItemSetPriceBinding;

import java.util.ArrayList;

import gear.yc.com.gearlibrary.ui.adapter.GearRecyclerViewAdapter;

/**
 * ecjia-shopkeeper-android
 * Created by YichenZ on 2017/3/22 18:39.
 */

public class SetPriceAdapter extends GearRecyclerViewAdapter<ValuePrice,SetPriceAdapter.Holder> {

    public SetPriceAdapter(Context mContext, ArrayList<ValuePrice> mData) {
        super(mContext, mData);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemSetPriceBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext),
                R.layout.item_set_price, parent, false);
        Holder holder = new Holder(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.mBinding.setData(mData.get(position));

        holder.mBinding.ivDelete.setOnClickListener(v -> {
            if(mData.size() > 1) {
                mData.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    public class Holder extends RecyclerView.ViewHolder{
        ItemSetPriceBinding mBinding;
        public Holder(ItemSetPriceBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }
}
