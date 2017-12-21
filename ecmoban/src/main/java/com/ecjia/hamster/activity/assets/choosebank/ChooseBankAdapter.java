package com.ecjia.hamster.activity.assets.choosebank;

import android.content.Context;

import com.ecjia.entity.Bank;
import com.ecjia.hamster.adapter.adapter.base.ViewHolder;
import com.ecjia.hamster.adapter.adapter.my.HeaderFooterLoadMoreAdapter;
import com.ecjia.util.ImageLoaderForLV;
import com.ecmoban.android.shopkeeper.sijiqing.R;

import java.util.List;

/**
 * ecjia-shopkeeper-android
 * Created by YichenZ on 2017/3/15 16:32.
 */

public class ChooseBankAdapter extends HeaderFooterLoadMoreAdapter<Bank>{
    Context mContext;
    public ChooseBankAdapter(Context context, List<Bank> datas) {
        super(context, R.layout.item_bank, datas);
        mContext=context;
    }

    @Override
    public void convert(ViewHolder holder, Bank bank, int position) {
        ImageLoaderForLV.getInstance(mContext).setImageRes(holder.getView(R.id.iv_icon),bank.getIcon());
        holder.setText(R.id.tv_name,bank.getName());
    }
}
