package com.ecjia.hamster.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ecmoban.android.shopkeeper.sijiqing.R;

import java.util.ArrayList;

public class EditDialogAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Context context;
    public ArrayList<String> lists;

    public EditDialogAdapter(Context context, ArrayList<String> lists) {
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
         final String  str = lists.get(position);

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.edit_dialog_item, null);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_edit_dialog_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_name.setText(str);


        return convertView;
    }


    class ViewHolder {

        private TextView tv_name;

    }

}
