package com.ecjia.hamster.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecmoban.android.shopkeeper.sijiqing.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/24.
 */
public class MagicGridViewAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<String> datalist=new ArrayList<String>();
    private int width;
    public MagicGridViewAdapter(Context context,ArrayList datalist){
        this.context=context;
        this.datalist=datalist;
        width=getDisplayMetricsWidth();
    }

    @Override
    public int getCount() {
        return datalist.size();
    }

    @Override
    public Object getItem(int i) {
        return datalist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    ViewHolder holder;
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null){
            view= LayoutInflater.from(context).inflate(R.layout.magic_gv_item,null);
            holder=new ViewHolder();
            holder.ll_discount= (LinearLayout) view.findViewById(R.id.gv_item);
            holder.tv_discount= (TextView) view.findViewById(R.id.tv_discount);
            holder.iv_discount= (ImageView) view.findViewById(R.id.iv_discount);
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.width=width/3;
            params.height=width/3;
            holder.ll_discount.setLayoutParams(params);
            view.setTag(holder);
        }else{
            holder= (ViewHolder) view.getTag();
        }
        holder.iv_discount.setBackgroundResource(R.drawable.iv_magic_discount);
        holder.tv_discount.setText(datalist.get(i));
        return view;
    }
    private class ViewHolder{
        private TextView tv_discount;
        private ImageView iv_discount;
        private LinearLayout ll_discount;
    }

    // 获取屏幕宽度
    public int getDisplayMetricsWidth() {
        int i = ((Activity)context).getWindowManager().getDefaultDisplay().getWidth();
        int j = ((Activity)context).getWindowManager().getDefaultDisplay()
                .getHeight();
        return Math.min(i, j);
    }
}
