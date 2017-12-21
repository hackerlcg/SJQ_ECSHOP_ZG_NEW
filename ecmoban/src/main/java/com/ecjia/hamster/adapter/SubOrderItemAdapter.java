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

import java.util.ArrayList;

public class SubOrderItemAdapter extends BaseAdapter {
    private Context context;
    public ArrayList<GOODS> list;

    public SubOrderItemAdapter(Context c, ArrayList<GOODS> list) {
        context = c;
        this.list = list;
    }

    public void setDate(ArrayList<GOODS> list) {
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
                    R.layout.orderdetail_item, null);

            holder.iv_order = (ImageView) convertView
                    .findViewById(R.id.iv_order);

            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_account = (TextView) convertView.findViewById(R.id.tv_account);
            holder.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
            holder.tv_theme = (TextView) convertView.findViewById(R.id.tv_theme);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final GOODS goods=list.get(position);

        holder.tv_name.setText(goods.getName());
        holder.tv_account.setText(goods.getShop_price());
//        holder.tv_theme.setText(goods.getGoods_attr());

        if(!TextUtils.isEmpty(goods.getNumber2())){
            holder.tv_num.setText("x"+goods.getNumber2());
        }else{
            holder.tv_num.setText("");
        }

        ImageLoaderForLV.getInstance(context).setImageRes(holder.iv_order, goods.getImg().getThumb());

        return convertView;
    }

    class ViewHolder {

        private ImageView iv_order;
        private TextView tv_num,tv_account,tv_name,tv_theme;
    }
}
