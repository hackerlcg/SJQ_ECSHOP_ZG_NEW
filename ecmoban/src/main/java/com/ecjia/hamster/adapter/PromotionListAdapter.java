package com.ecjia.hamster.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.hamster.model.GOODS;
import com.ecjia.util.ImageLoaderForLV;

import java.util.ArrayList;

/**
 *
 * Created by Administrator on 2016/5/4.
 */
public class PromotionListAdapter extends BaseAdapter{
    public ArrayList<GOODS> ArrayList;
    private Context context;
    private int flag;
    public PromotionListAdapter(Context context, ArrayList<GOODS> goodes, int flag){
        super();
        this.context=context;
        this.ArrayList=goodes;
        this.flag=flag;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    @Override
    public int getCount() {
        return ArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return ArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GOODS goods=ArrayList.get(position);
        ViewHolder holder;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.promotion_list_item,null);
            holder=new ViewHolder();
            holder.tv_name= (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_start_time= (TextView) convertView.findViewById(R.id.tv_start_time);
            holder.tv_end_time= (TextView) convertView.findViewById(R.id.tv_end_time);
            holder.tv_promotion_price= (TextView) convertView.findViewById(R.id.tv_promotion_price);
            holder.iv_goods= (ImageView) convertView.findViewById(R.id.iv_goods);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }

        if(flag==2){
            convertView.setAlpha(0.5f);
        }else{
            convertView.setAlpha(1f);
        }

        holder.tv_name.setText(goods.getGoods_name());
        holder.tv_promotion_price.setText(goods.getFormatted_promote_price());
        ImageLoaderForLV.getInstance(context).setImageRes(holder.iv_goods, goods.getImg().getThumb());
        holder.tv_start_time.setText(goods.getFormatted_promote_start_date());
        holder.tv_end_time.setText(goods.getFormatted_promote_end_date());

        return convertView;
    }

    private class ViewHolder{
        private TextView tv_name,tv_promotion_price,tv_start_time,tv_end_time;
        private ImageView iv_goods;
    }
}
