package com.ecjia.hamster.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.hamster.model.USERRANK;

import java.util.ArrayList;

public class RankListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Context context;
    public ArrayList<USERRANK> lists;

    public RankListAdapter(Context context, ArrayList<USERRANK> lists) {
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
         final USERRANK  userrank = lists.get(position);

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.ranks_item, null);

            holder.image = (ImageView) convertView.findViewById(R.id.iv_check);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_rank_name);
            holder.first_line = (View) convertView.findViewById(R.id.first_line);
            holder.bottom_short_line = (View) convertView.findViewById(R.id.bottom_short_line);
            holder.bottom_long_line = (View) convertView.findViewById(R.id.bottom_long_line);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(position==0){
            holder.first_line.setVisibility(View.VISIBLE);
        }else{
            holder.first_line.setVisibility(View.GONE);
        }

        if(position==lists.size()-1){
            holder.bottom_short_line.setVisibility(View.GONE);
            holder.bottom_long_line.setVisibility(View.VISIBLE);
        }else{
            holder.bottom_short_line.setVisibility(View.VISIBLE);
            holder.bottom_long_line.setVisibility(View.GONE);
        }

        holder.tv_name.setText(userrank.getRank_name());

        if(userrank.isChecked()){
            holder.image.setImageResource(R.drawable.goods_cb_checked);
        }else{
            holder.image.setImageResource(R.drawable.goods_cb_unchecked);
        }

        return convertView;
    }


    class ViewHolder {

        private ImageView image;
        private View first_line,bottom_short_line,bottom_long_line;
        private TextView tv_name;

    }

}
