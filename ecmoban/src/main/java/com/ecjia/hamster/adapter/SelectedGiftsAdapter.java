package com.ecjia.hamster.adapter;


import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.hamster.model.GIFT;
import com.ecjia.util.ImageLoaderForLV;

import java.util.ArrayList;

public class SelectedGiftsAdapter extends BaseAdapter {

	private Context context;
	public ArrayList<GIFT> list;
	private LayoutInflater inflater;
	private int mRightWidth = 0;

	public SelectedGiftsAdapter(Context context, ArrayList<GIFT> list) {
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
			convertView = inflater.inflate(R.layout.selected_gifts_item, parent, false);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.item_left =  (LinearLayout) convertView
					.findViewById(R.id.item_left);
            holder.tv_gifts_price= (TextView) convertView.findViewById(R.id.tv_gifts_price);
            holder.iv_gift= (ImageView) convertView.findViewById(R.id.iv_gift);
            holder.tv_origin_price= (TextView) convertView.findViewById(R.id.tv_origin_price);
            holder.tv_origin_price.getPaint().setAntiAlias(true);
            holder.tv_origin_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final GIFT gift = list.get(position);

        holder.tv_name.setText(gift.getName());
        holder.tv_gifts_price.setText(gift.getPrice());
        holder.tv_origin_price.setText(gift.getOrigin_price());
        ImageLoaderForLV.getInstance(context).setImageRes(holder.iv_gift,gift.getImage());

		return convertView;
	}

	class ViewHolder {
        private LinearLayout item_left;
        private TextView tv_name;
        private TextView tv_origin_price;
        private TextView tv_gifts_price;
        private ImageView iv_gift;
	}


}
