package com.ecjia.hamster.adapter;


import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.hamster.model.GOODS;
import com.ecjia.util.ImageLoaderForLV;
import com.ecjia.util.TimeUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GoodsSearchListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Context context;
    public ArrayList<GOODS> lists;


    public static Map<Integer, Boolean> isSelected = new HashMap<Integer, Boolean>();


    public GoodsSearchListAdapter(Context context, ArrayList<GOODS> lists) {
        this.context = context;
        this.lists = lists;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    ViewHolder holder;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
         final GOODS  goods = lists.get(position);

        // TODO Auto-generated method stub
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.search_goods_item, null);

            holder.image = (ImageView) convertView.findViewById(R.id.iv_good);
            holder.iv_promote = (ImageView) convertView.findViewById(R.id.iv_promote);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_account = (TextView) convertView.findViewById(R.id.tv_account);
            holder.tv_sale = (TextView) convertView.findViewById(R.id.tv_sale);
            holder.tv_click = (TextView) convertView.findViewById(R.id.tv_click);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.bottom_short_line = (View) convertView.findViewById(R.id.bottom_short_line);
            holder.bottom_long_line = (View) convertView.findViewById(R.id.bottom_long_line);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(position==lists.size()-1){
            holder.bottom_short_line.setVisibility(View.GONE);
            holder.bottom_long_line.setVisibility(View.VISIBLE);
        }else{
            holder.bottom_short_line.setVisibility(View.VISIBLE);
            holder.bottom_long_line.setVisibility(View.GONE);
        }

        if(TextUtils.isEmpty(goods.getFormatted_promote_start_date())
                ||TextUtils.isEmpty(goods.getFormatted_promote_end_date())){
            holder.iv_promote.setVisibility(View.GONE);
            holder.tv_account.setText(goods.getShop_price());
        }else{
            switch (TimeUtil.compare_promotion(goods.getFormatted_promote_start_date(),goods.getFormatted_promote_end_date())){
                case 0:
                    holder.iv_promote.setVisibility(View.GONE);
                    holder.tv_account.setText(goods.getShop_price());
                break;
                case 1:
                    holder.iv_promote.setVisibility(View.VISIBLE);
                    holder.iv_promote.setImageResource(R.drawable.promotion_offline);
                    holder.tv_account.setText(goods.getShop_price());
                break;
                case 2:
                    holder.iv_promote.setVisibility(View.VISIBLE);
                    holder.iv_promote.setImageResource(R.drawable.promote_list);
                    holder.tv_account.setText(goods.getPromote_price());
                break;
                case 3:
                    holder.iv_promote.setVisibility(View.VISIBLE);
                    holder.iv_promote.setImageResource(R.drawable.promotion_offline);
                    holder.tv_account.setText(goods.getShop_price());
                break;
            }
        }

        holder.tv_name.setText(goods.getName());
        holder.tv_click.setText(goods.getClicks());
        String time=goods.getTime();
        if(time.length()>10){
            time=time.substring(0,11);
        }
        holder.tv_time.setText(time);

        ImageLoaderForLV.getInstance(context).setImageRes(holder.image, goods.getImg().getThumb());

        return convertView;
    }


    class ViewHolder {

        private ImageView image,iv_promote;
        private View bottom_short_line,bottom_long_line;
        private TextView tv_name,tv_account,tv_click,tv_time,tv_sale;

    }


}
