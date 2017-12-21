package com.ecjia.hamster.adapter;


import android.content.Context;
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
import com.ecjia.hamster.model.GOODS;
import com.ecjia.util.ImageLoaderForLV;

import java.util.ArrayList;

public class SelectGoodsAdapter extends BaseAdapter {

	private Context context;
	public ArrayList<GOODS> list;
	private LayoutInflater inflater;
	private int mRightWidth = 0;

	public SelectGoodsAdapter(Context context, ArrayList<GOODS> list, int RightViewWidth) {
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
			convertView = inflater.inflate(R.layout.select_goods_item, parent, false);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.item_left =  (LinearLayout) convertView
					.findViewById(R.id.item_left);
			holder.item_right = (LinearLayout) convertView
					.findViewById(R.id.item_right);
            holder.iv_promote = (ImageView) convertView.findViewById(R.id.iv_promote);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_account = (TextView) convertView.findViewById(R.id.tv_account);
            holder.tv_click = (TextView) convertView.findViewById(R.id.tv_click);
            holder.iv_goods=(ImageView) convertView.findViewById(R.id.iv_goods);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.short_line = (View) convertView.findViewById(R.id.bottom_short_line);
            holder.long_line = (View) convertView.findViewById(R.id.bottom_long_line);
            holder.iv_goods_del= (ImageView) convertView.findViewById(R.id.iv_goods_del);
            holder.item_delete= (LinearLayout) convertView.findViewById(R.id.ll_goods_delete);
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
		final GOODS goods = list.get(position);

        if(position==list.size()-1){
            holder.short_line.setVisibility(View.GONE);
            holder.long_line.setVisibility(View.VISIBLE);
        }else{
            holder.short_line.setVisibility(View.VISIBLE);
            holder.long_line.setVisibility(View.GONE);
        }

        if("0".equals(goods.getPromote_price())){
            holder.iv_promote.setVisibility(View.GONE);
            holder.tv_account.setText(goods.getShop_price());
        }else{
            holder.iv_promote.setVisibility(View.VISIBLE);
            holder.tv_account.setText(goods.getPromote_price());
        }

        holder.tv_name.setText(goods.getName());
        holder.tv_click.setText(goods.getClicks());
        String time=goods.getTime();
        if(time.length()>10){
            time=time.substring(0,11);
        }
        holder.tv_time.setText(time);


        ImageLoaderForLV.getInstance(context).setImageRes(holder.iv_goods,goods.getImg().getThumb());


        holder.item_delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mListener != null) {
					mListener.onAdpItemClick(v, position);
				}
			}
		});
        holder.iv_goods_del.setOnClickListener(new OnClickListener() {
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
        private ImageView iv_goods,iv_promote;
        private View short_line,long_line;
        private TextView tv_name,tv_account,tv_click,tv_time;
        private ImageView iv_goods_del;
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
