package com.ecjia.hamster.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.hamster.model.ACTION;

import java.util.ArrayList;

public class ActionAdapter extends BaseAdapter {
    private Context context;
    public ArrayList<ACTION> list;
    private int size=0;
    public ActionAdapter(Context c, ArrayList<ACTION> list) {
        context = c;
        this.list = list;
        size=list.size();
    }

    public void setDate(ArrayList<ACTION> list) {
        this.list = list;
        size=list.size();
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
                    R.layout.action_item, null);
            holder.tv_logid= (TextView) convertView.findViewById(R.id.log_id);
            holder.tv_action_time = (TextView) convertView.findViewById(R.id.tv_action_time);
            holder.tv_log_description= (TextView) convertView.findViewById(R.id.log_description);
            holder.center_view=convertView.findViewById(R.id.center_view);
            holder.buttom_view=convertView.findViewById(R.id.buttom_view);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ACTION action=list.get(position);
        holder.tv_logid.setText((position+1)+".");
        holder.tv_log_description.setText(action.getLog_description());
        holder.tv_action_time.setText(action.getAction_time());
        if(size==1){
            holder.center_view.setVisibility(View.GONE);
            holder.buttom_view.setVisibility(View.VISIBLE);
        }else if(size==2){
            if(position==0){
                holder.center_view.setVisibility(View.VISIBLE);
                holder.buttom_view.setVisibility(View.GONE);
            }else{
                holder.center_view.setVisibility(View.GONE);
                holder.buttom_view.setVisibility(View.VISIBLE);
            }
        }else{
            if(position==size-1){
                holder.center_view.setVisibility(View.GONE);
                holder.buttom_view.setVisibility(View.VISIBLE);
            }else{
                holder.center_view.setVisibility(View.VISIBLE);
                holder.buttom_view.setVisibility(View.GONE);
            }
        }

        return convertView;
    }

    class ViewHolder {

        private TextView tv_logid,tv_action_time,tv_log_description;
        private View center_view,buttom_view;
    }
}
