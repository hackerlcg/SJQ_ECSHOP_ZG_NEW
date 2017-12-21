package com.ecjia.hamster.adapter;

import android.content.Context;
import android.view.View;

import com.ecjia.component.network.responsmodel.ReturnGoodsOrderInfoModel;
import com.ecjia.hamster.adapter.adapter.CommonAdapter;
import com.ecjia.hamster.adapter.adapter.base.ViewHolder;
import com.ecmoban.android.shopkeeper.sijiqing.R;

import java.util.List;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-03-22.
 */

public class OderReturnGoodsDetailAdapter extends CommonAdapter<ReturnGoodsOrderInfoModel.ReturnLog> {

    private List<ReturnGoodsOrderInfoModel.ReturnLog> datas;

    public OderReturnGoodsDetailAdapter(Context context, List<ReturnGoodsOrderInfoModel.ReturnLog> datas) {
        super(context, R.layout.item_oder_returngoodsdetail, datas);
        this.datas = datas;
    }

    @Override
    protected void convert(ViewHolder holder, ReturnGoodsOrderInfoModel.ReturnLog returnGoodInfoList, int position) {
        if (position == 0) {
            holder.setVisibility(R.id.line1, View.INVISIBLE);
        } else {
            holder.setVisibility(R.id.line1, View.VISIBLE);
        }
        if (position == datas.size() - 1) {
            holder.setVisibility(R.id.line2, View.INVISIBLE);
        } else {
            holder.setVisibility(R.id.line2, View.VISIBLE);
        }
        holder.setText(R.id.tv_status, returnGoodInfoList.getReturn_status() + "");
        holder.setText(R.id.tv_content, returnGoodInfoList.getAction_note() + "");
        holder.setText(R.id.tv_time, returnGoodInfoList.getLog_time() + "");
        holder.setVisibility(R.id.tv_see, View.GONE);
//        if (TextUtils.isEmpty(returnGoodInfoList.getInvoice_no())) {
//            holder.setVisibility(R.id.tv_see, View.GONE);
//        } else {
//            holder.setVisibility(R.id.tv_see, View.VISIBLE);
//            holder.setOnClickListener(R.id.tv_see, new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //跳转
//                    //物流信息
//                    Intent intent2 = new Intent(context, LogisticsActivity.class);
//                    intent2.putExtra("order_status", "");
//                    intent2.putExtra("shippingname", returnGoodInfoList.getShipping_code());
//                    intent2.putExtra("shipping_number", returnGoodInfoList.getInvoice_no());
//                    context.startActivity(intent2);
//                }
//            });
//        }


    }
}