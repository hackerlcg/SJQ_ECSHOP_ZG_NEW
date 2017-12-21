package com.ecjia.hamster.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecjia.hamster.activity.order.ApplyOderReturnGoodsDetailActivity_Builder;
import com.ecjia.hamster.activity.order.OrderReutrnInfoActivity_Builder;
import com.ecjia.hamster.model.GOODS;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.util.ImageLoaderForLV;

import java.util.List;

public class OrderDetailListAdapter extends BaseAdapter {
    private Context context;
    public List<GOODS> list;
    private int type;

    public OrderDetailListAdapter(Context c, List<GOODS> list) {
        context = c;
        this.list = list;
    }

    public void setDate(List<GOODS> list) {
        this.list = list;
    }

    @Override
    public int getCount() {

        return list.size();
    }

    @Override
    public Object getItem(int position) {

        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.orderdetail_item, null);
            holder.iv_order = (ImageView) convertView.findViewById(R.id.iv_order);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_account = (TextView) convertView.findViewById(R.id.tv_account);
            holder.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
            holder.tv_theme = (TextView) convertView.findViewById(R.id.tv_theme);
            holder.tv_good_service = (TextView) convertView.findViewById(R.id.tv_good_service);
            holder.tv_goodsn = (TextView) convertView.findViewById(R.id.tv_goodsn);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final GOODS goods = list.get(position);

        holder.tv_name.setText(goods.getName());
        holder.tv_account.setText(goods.getFormatted_shop_price());
        holder.tv_theme.setText(goods.getGoods_attr());
        if (!TextUtils.isEmpty(goods.getRet_id()) && !"0".equals(goods.getRet_id())) {
            holder.tv_good_service.setVisibility(View.VISIBLE);
            holder.tv_good_service.setText("查看售后");
            holder.tv_good_service.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    ApplyOderReturnGoodsDetailActivity_Builder.intent(context).retId(goods.getRet_id()).start();
                    OrderReutrnInfoActivity_Builder.intent(context).retId(goods.getRet_id()).start();
                }
            });
        } else {
            holder.tv_good_service.setVisibility(View.GONE);
        }
//        holder.tv_good_service.setText(goods.getGoods_attr());
        holder.tv_goodsn.setText("货号:  " + goods.getGoods_sn() + "");
        if (!TextUtils.isEmpty(goods.getNumber())) {
            holder.tv_num.setText("x" + goods.getNumber());
        } else {
            holder.tv_num.setText("");
        }

        ImageLoaderForLV.getInstance(context).setImageRes(holder.iv_order, goods.getImg().getThumb());

        return convertView;
    }

    class ViewHolder {

        private ImageView iv_order;
        private TextView tv_num, tv_account, tv_name, tv_theme, tv_good_service, tv_goodsn;
    }
}
