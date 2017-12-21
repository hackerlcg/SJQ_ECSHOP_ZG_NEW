package com.ecjia.hamster.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecjia.hamster.model.ORDERS;
import com.ecjia.util.ImageLoaderForLV;
import com.ecmoban.android.shopkeeper.sijiqing.R;

import java.util.List;

public class OrdersListAdapter extends BaseAdapter {
    private Context context;
    public List<ORDERS> list;

    public OrdersListAdapter(Context c, List<ORDERS> list) {
        context = c;
        this.list = list;
    }

    public void setDate(List<ORDERS> list) {
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
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.order_item, null);

            holder.iv_order = (ImageView) convertView
                    .findViewById(R.id.iv_order);

            holder.tv_id = (TextView) convertView.findViewById(R.id.tv_id);
            holder.tv_account = (TextView) convertView.findViewById(R.id.tv_account);
            holder.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
            holder.tv_status = (TextView) convertView.findViewById(R.id.tv_status);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);

            holder.item = (LinearLayout) convertView.findViewById(R.id.item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ORDERS orders=list.get(position);

        holder.tv_time.setText(orders.getTime());
        holder.tv_id.setText(orders.getSn());
        holder.tv_account.setText(orders.getTotal());
//        holder.tv_status.setText(orders.getPayment());
        holder.tv_status.setText(orders.getStatus());

        String origin=context.getResources().getString(R.string.order_num);
        origin=origin.replaceAll("0",orders.getNumber());
        holder.tv_num.setText(origin);

        ImageLoaderForLV.getInstance(context).setImageRes(holder.iv_order, orders.getGoodslist().get(0).getImg().getThumb());

        return convertView;
    }

    class ViewHolder {

        private ImageView iv_order;
        private TextView tv_time,tv_num,tv_id,tv_account,tv_status;
        private LinearLayout item;
    }
}
