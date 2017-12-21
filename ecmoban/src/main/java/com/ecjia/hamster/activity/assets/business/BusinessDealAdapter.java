package com.ecjia.hamster.activity.assets.business;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecjia.entity.BusinessDeal;
import com.ecjia.hamster.adapter.adapter.base.ViewHolder;
import com.ecjia.hamster.adapter.adapter.my.HeaderFooterLoadMoreAdapter;
import com.ecjia.util.common.DUtils;
import com.ecmoban.android.shopkeeper.sijiqing.R;

import java.util.List;

/**
 * ecjia-shopkeeper-android
 * Created by YichenZ on 2017/3/15 09:29.
 */

public class BusinessDealAdapter extends HeaderFooterLoadMoreAdapter<BusinessDeal> {
    private Context mContext;

    public BusinessDealAdapter(Context context, List<BusinessDeal> datas) {
        super(context, R.layout.item_business_deal, datas);
        mContext = context;
    }

    @Override
    public void convert(ViewHolder holder, BusinessDeal businessDeal, int position) {
        LinearLayout llDiv = (LinearLayout)holder.getConvertView().findViewById(R.id.ll_div);
        if(position == 0) {
            llDiv.setVisibility(View.VISIBLE);
        } else {
            if(businessDeal.getYear().equals(getAdapter().getDatas().get(position-1).getYear())){
                llDiv.setVisibility(View.GONE);
            } else {
                llDiv.setVisibility(View.VISIBLE);
            }
        }
        TextView tvTag = holder.getView(R.id.tv_tag);
        tvTag.setText(businessDeal.showTag());
        TextView tvPrice = holder.getView(R.id.tv_price);
        tvPrice.setText(businessDeal.showPrice());
        if(businessDeal.tag()){
            //收
            tvTag.setTextColor(0xFFDE0000);
            tvPrice.setTextColor(0xFFDE0000);
        } else {
            //支
            tvTag.setTextColor(0xFF01A307);
            tvPrice.setTextColor(0xFF01A307);
        }
        holder.setText(R.id.tv_name, businessDeal.getName())
                .setText(R.id.tv_time, DUtils.getBirth(businessDeal.getTime())).setText(R.id.tv_price, businessDeal.showPrice())
                .setText(R.id.tv_year, businessDeal.getYear());

        TextView piad = holder.getView(R.id.tv_paid);
        if("1".equals(businessDeal.getPaid())){
            piad.setVisibility(View.GONE);
        }else{
            piad.setVisibility(View.VISIBLE);
        }
    }
}
