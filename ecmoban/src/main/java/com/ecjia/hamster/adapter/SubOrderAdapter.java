package com.ecjia.hamster.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.component.view.MyListView;
import com.ecjia.hamster.activity.OrderDetailActivity;
import com.ecjia.hamster.model.SUBORDERS;

import java.util.ArrayList;

public class SubOrderAdapter extends BaseAdapter {
    private Context context;
    public ArrayList<SUBORDERS> list;
    private SubOrderItemAdapter subOrderItemAdapter;
    private int type;

    public SubOrderAdapter(Context c, ArrayList<SUBORDERS> list,int type) {
        context = c;
        this.list = list;
        this.type = type;
    }

    public void setDate(ArrayList<SUBORDERS> list) {
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
                    R.layout.suborder_item, null);

            holder.tv_order_state = (TextView) convertView.findViewById(R.id.tv_order_state);
            holder.item_listView = (MyListView) convertView.findViewById(R.id.item_listView);
            holder.ll_order_state = (LinearLayout) convertView.findViewById(R.id.ll_order_state);
            holder.item_listView.setTouchable(false);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final SUBORDERS suborders=list.get(position);

        Resources res=context.getResources();

        String state="【"+res.getString(R.string.child_order)+(position+1)+"】"+res.getString(R.string.order_state)+suborders.getStatus();

        holder.tv_order_state.setText(state);
        holder.ll_order_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OrderDetailActivity.class);
                intent.putExtra("id", suborders.getId());
                intent.putExtra("type", type);
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);
            }
        });
        subOrderItemAdapter=new SubOrderItemAdapter(context,suborders.getGoodslist());
        holder.item_listView.setAdapter(subOrderItemAdapter);

        return convertView;
    }

    class ViewHolder {

        private TextView tv_order_state;
        private MyListView item_listView;
        private LinearLayout ll_order_state;
    }
}
