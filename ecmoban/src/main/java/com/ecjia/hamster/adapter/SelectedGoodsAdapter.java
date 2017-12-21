package com.ecjia.hamster.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.hamster.model.GOODS;
import com.ecjia.util.ImageLoaderForLV;

import java.util.ArrayList;

public class SelectedGoodsAdapter extends BaseAdapter {

	private Context context;
	public ArrayList<GOODS> list;
	private LayoutInflater inflater;
	private int mRightWidth = 0;

	public SelectedGoodsAdapter(Context context, ArrayList<GOODS> list) {
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
			convertView = inflater.inflate(R.layout.selected_goods_item, parent, false);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.item_left =  (LinearLayout) convertView
					.findViewById(R.id.item_left);
            holder.iv_goods= (ImageView) convertView.findViewById(R.id.iv_goods);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final GOODS goods = list.get(position);

        holder.tv_name.setText(goods.getName());
        ImageLoaderForLV.getInstance(context).setImageRes(holder.iv_goods,goods.getImage());

		return convertView;
	}

	class ViewHolder {
        private LinearLayout item_left;
        private TextView tv_name;
        private ImageView iv_goods;
	}


}
