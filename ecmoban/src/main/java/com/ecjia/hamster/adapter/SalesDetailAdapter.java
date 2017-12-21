package com.ecjia.hamster.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.hamster.model.STATS;
import com.ecjia.util.TimeUtil;

import java.util.ArrayList;

public class SalesDetailAdapter extends BaseAdapter {
    public ArrayList<STATS> list;
    private Context context;
    private LayoutInflater inflater;
    private String last_text, now_text, next_text;//标志位月份信息

    public SalesDetailAdapter(ArrayList<STATS> list, Context context) {
        this.list = list;
        this.context = context;
        this.inflater = LayoutInflater.from(context);

    }

    public void setListData(ArrayList<STATS> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        if (list == null) {
            return 0;
        }
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
        final STATS stats = list.get(position);

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.sales_detail_item, null);
            holder.tv_first = (TextView) convertView.findViewById(R.id.tv_first);
            holder.tv_week_time = (TextView) convertView.findViewById(R.id.tv_week_time);
            holder.tv_hour_time = (TextView) convertView.findViewById(R.id.tv_hour_time);
            holder.tv_value = (TextView) convertView.findViewById(R.id.tv_value);
            holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            holder.iv_type = (ImageView) convertView.findViewById(R.id.iv_type);
            holder.item_top_view = convertView.findViewById(R.id.item_top_view);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        now_text = TimeUtil.getFomartMonth(list.get(position).getDefault_time());
        if (position > 0) {
            last_text = TimeUtil.getFomartMonth(list.get(position - 1).getDefault_time());
        }
        if (position < list.size() - 1) {
            next_text = TimeUtil.getFomartMonth(list.get(position + 1).getDefault_time());
        }

        if (list.size() == 1) { //只有一条
            holder.tv_first.setVisibility(View.VISIBLE);
            holder.item_top_view.setVisibility(View.VISIBLE);
        } else {//多条
            if (position == 0) { //第一项
                holder.tv_first.setVisibility(View.VISIBLE);
                holder.item_top_view.setVisibility(View.VISIBLE);

            } else if (position == list.size() - 1) { //最后一项

                if (last_text.equals(now_text)) {  //是否在同年同月
                    holder.item_top_view.setVisibility(View.GONE);
                    holder.tv_first.setVisibility(View.GONE);
                } else {
                    holder.item_top_view.setVisibility(View.VISIBLE);
                    holder.tv_first.setVisibility(View.VISIBLE);
                }

            } else { //中间项

                if (last_text.equals(now_text)) {  //是否在同年同月
                    holder.item_top_view.setVisibility(View.GONE);
                    holder.tv_first.setVisibility(View.GONE);
                } else {
                    holder.item_top_view.setVisibility(View.VISIBLE);
                    holder.tv_first.setVisibility(View.VISIBLE);
                }

            }
        }
        
        holder.tv_first.setText(now_text);
        
        holder.tv_week_time.setText(stats.getFormatted_week_time());
        holder.tv_hour_time.setText(stats.getFormatted_hour_time());
        if (Float.valueOf(stats.getValue()) >= 0) {
            holder.iv_type.setBackgroundResource(R.drawable.iv_order_finish);
            holder.tv_value.setText("+" + stats.getValue());
            holder.tv_content.setText("订单-支付完成，交易关闭");
        } else {
            holder.iv_type.setBackgroundResource(R.drawable.iv_order_cancel);
            holder.tv_value.setText(stats.getValue());
            holder.tv_content.setText("退款-退款完成，交易关闭");
        }


        return convertView;
    }

    class ViewHolder {
        private ImageView iv_type;
        private TextView tv_first, tv_week_time, tv_hour_time, tv_value, tv_content;
        private View item_top_view;
    }

}
