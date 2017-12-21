package com.ecjia.hamster.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.hamster.model.BRAND;

import java.util.ArrayList;

public class SelectedBrandsAdapter extends BaseAdapter {

	private Context context;
	public ArrayList<BRAND> list;
	private LayoutInflater inflater;
	private int mRightWidth = 0;

	public SelectedBrandsAdapter(Context context, ArrayList<BRAND> list) {
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
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
	public int getItemViewType(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
		if(convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.selected_brands_item, parent, false);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.item_left =  (LinearLayout) convertView
					.findViewById(R.id.item_left);
            holder.short_line = (View) convertView.findViewById(R.id.bottom_short_line);
            holder.long_line = (View) convertView.findViewById(R.id.bottom_long_line);
            holder.top_line = (View) convertView.findViewById(R.id.top_line);
            convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

        if(position==0){
           holder.top_line.setVisibility(View.VISIBLE);
        }else{
           holder.top_line.setVisibility(View.GONE);
        }

        if(position==(list.size()-1)){
           holder.long_line.setVisibility(View.VISIBLE);
           holder.short_line.setVisibility(View.GONE);
        }else{
           holder.short_line.setVisibility(View.VISIBLE);
           holder.long_line.setVisibility(View.GONE);
        }

		final BRAND brand = list.get(position);

        holder.tv_name.setText(brand.getBrand_name());

		return convertView;
	}

	class ViewHolder {
        private LinearLayout item_left;
        private TextView tv_name;
        private View short_line,long_line,top_line;
	}


}
