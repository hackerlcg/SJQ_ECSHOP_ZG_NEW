package com.ecjia.hamster.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.hamster.model.FAVOURABLE;

import java.util.ArrayList;

/**
 *
 * Created by Administrator on 2016/3/24.
 */
public class DiscountListAdapter extends BaseAdapter{
    public ArrayList<FAVOURABLE> favourableArrayList;
    private Context context;
    private String max_amount;
    private int flag;
    public DiscountListAdapter(Context context, ArrayList<FAVOURABLE> favourables,int flag){
        super();
        this.context=context;
        this.favourableArrayList=favourables;
        max_amount=context.getResources().getString(R.string.max_amount);
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
        return favourableArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return favourableArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FAVOURABLE favourable=favourableArrayList.get(position);
        ViewHolder holder;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.discount_list_item,null);
            holder=new ViewHolder();
            holder.tv_label_act_type= (TextView) convertView.findViewById(R.id.tv_label_act_type);
            holder.tv_act_name= (TextView) convertView.findViewById(R.id.tv_act_name);
            holder.tv_max_amount= (TextView) convertView.findViewById(R.id.tv_max_amount);
            holder.tv_start_time= (TextView) convertView.findViewById(R.id.tv_start_time);
            holder.tv_end_time= (TextView) convertView.findViewById(R.id.tv_end_time);
            holder.vg_rank_names= (ViewGroup) convertView.findViewById(R.id.viewgroup_ranknames);
            holder.iv_discount_seller= (ImageView) convertView.findViewById(R.id.iv_discount_seller);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        holder.vg_rank_names.removeAllViews();
        if(favourable.getRank_nams().length>0){
            String[] strings=favourable.getRank_nams();
            for(String s:strings){
                View view= LayoutInflater.from(context).inflate(R.layout.tv_discount_rank_name,null);
                TextView tv_rank_name= (TextView) view.findViewById(R.id.tv_rank_name);
                tv_rank_name.setText(s);
                holder.vg_rank_names.addView(view);
            }
            holder.vg_rank_names.setVisibility(View.VISIBLE);
        }else{
            holder.vg_rank_names.setVisibility(View.GONE);
        }
        holder.tv_label_act_type.setText(favourable.getLabel_act_type());
        holder.tv_act_name.setText(favourable.getAct_name());
        if(TextUtils.isEmpty(favourable.getMax_amount())||Float.valueOf(favourable.getMax_amount())==0){
            holder.tv_max_amount.setVisibility(View.GONE);
        }else {
            holder.tv_max_amount.setVisibility(View.VISIBLE);
            holder.tv_max_amount.setText(max_amount + favourable.getMax_amount());
        }
        if("0".equals(favourable.getSeller_id())){
            holder.iv_discount_seller.setBackgroundResource(R.drawable.iv_discount_self);
        }else{
            holder.iv_discount_seller.setBackgroundResource(R.drawable.iv_discount_shop);
        }
        if(flag==2){
            convertView.setAlpha(0.5f);
        }else{
            convertView.setAlpha(1f);
        }
        holder.tv_start_time.setText(favourable.getFormatted_start_time());
        holder.tv_end_time.setText(favourable.getFormatted_end_time());
        return convertView;
    }

    private class ViewHolder{
        private TextView tv_act_name,tv_max_amount,tv_start_time,tv_end_time,tv_label_act_type;
        private ViewGroup vg_rank_names;
        private ImageView iv_discount_seller;
    }
}
