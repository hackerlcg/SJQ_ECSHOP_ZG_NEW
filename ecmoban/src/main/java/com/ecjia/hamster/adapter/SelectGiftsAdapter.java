package com.ecjia.hamster.adapter;


import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.hamster.model.GIFT;
import com.ecjia.util.ImageLoaderForLV;

import java.util.ArrayList;

public class SelectGiftsAdapter extends BaseAdapter {

	private Context context;
	public ArrayList<GIFT> list;
	private LayoutInflater inflater;
	private int mRightWidth = 0;

	public SelectGiftsAdapter(Context context, ArrayList<GIFT> list, int RightViewWidth) {
		this.context = context;
		this.list = list;
		mRightWidth = RightViewWidth;
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
			convertView = inflater.inflate(R.layout.select_gifts_item, parent, false);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.item_left =  (LinearLayout) convertView
					.findViewById(R.id.item_left);
			holder.item_right = (LinearLayout) convertView
					.findViewById(R.id.item_right);
            holder.tv_origin_price= (TextView) convertView.findViewById(R.id.tv_origin_price);
            holder.tv_gifts_price= (TextView) convertView.findViewById(R.id.tv_gifts_price);
            holder.iv_gift= (ImageView) convertView.findViewById(R.id.iv_gift);
            holder.iv_gift_del= (ImageView) convertView.findViewById(R.id.iv_gift_del);
            holder.item_delete= (LinearLayout) convertView.findViewById(R.id.ll_gift_delete);
            holder.tv_origin_price.getPaint().setAntiAlias(true);
            holder.tv_origin_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		LayoutParams lp1 = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		holder.item_left.setLayoutParams(lp1);
		LayoutParams lp2 = new LayoutParams(mRightWidth,
				LayoutParams.MATCH_PARENT);
		holder.item_right.setLayoutParams(lp2);
		final GIFT gift = list.get(position);

        holder.tv_name.setText(gift.getName());
        holder.tv_gifts_price.setText(gift.getPrice());
        holder.tv_origin_price.setText(gift.getOrigin_price());
        ImageLoaderForLV.getInstance(context).setImageRes(holder.iv_gift,gift.getImage());

		holder.item_delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mListener != null) {
					mListener.onAdpItemClick(v, position);
				}
			}
		});
        holder.iv_gift_del.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onAdpItemClick(v, position);
                }
            }
        });


		return convertView;
	}

	class ViewHolder {
        private LinearLayout item_left;
        private LinearLayout item_right;
        private LinearLayout item_delete;
        private TextView tv_name;
        private TextView tv_gifts_price,tv_origin_price;
        private ImageView iv_gift,iv_gift_del;
	}

	/**
	 * 单击事件监听器
	 */
	private onAdpItemClickListener mListener = null;

	public void setOnAdpItemClickListener(onAdpItemClickListener listener) {
		mListener = listener;
	}

	public interface onAdpItemClickListener {
		void onAdpItemClick(View v, int position);
	}

}
