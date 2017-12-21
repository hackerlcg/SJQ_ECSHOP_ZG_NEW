package com.ecjia.hamster.adapter;


import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.ecjia.hamster.model.DBUSER;
import com.ecmoban.android.shopkeeper.sijiqing.R;

import java.util.List;

public class ShopListAdapter extends BaseAdapter {

	private Context context;
	private List<DBUSER> list;
	private LayoutInflater inflater;
	public Handler parentHandler;
	private int mRightWidth = 0;

	public ShopListAdapter(Context context, List<DBUSER> list, int RightViewWidth) {
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
			convertView = inflater.inflate(R.layout.shop_list_item, parent, false);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.item_left =  (LinearLayout) convertView
					.findViewById(R.id.item_left);
			holder.shop_item_left =  (LinearLayout) convertView
					.findViewById(R.id.shop_item_left);
			holder.item_right = (LinearLayout) convertView
					.findViewById(R.id.item_right);
            holder.tv_api= (TextView) convertView.findViewById(R.id.tv_api);
            holder.iv_default= (ImageView) convertView.findViewById(R.id.iv_default);
            holder.item_delete= (LinearLayout) convertView.findViewById(R.id.ll_delete);
            holder.item_edit= (LinearLayout) convertView.findViewById(R.id.ll_edit);
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
		final DBUSER users = list.get(position);

        holder.tv_name.setText(users.getName());
        holder.tv_api.setText(users.getApi());
        if(users.getIsDefault()==1){
            holder.iv_default.setVisibility(View.VISIBLE);
        }else{
            holder.iv_default.setVisibility(View.INVISIBLE);
        }


		holder.item_delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mListener != null) {
					mListener.onRightItemClick(v, position);
				}
			}
		});
        holder.item_edit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onRightItemClick(v, position);
                }
            }
        });
        holder.shop_item_left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onRightItemClick(v, position);
                }
            }
        });


		return convertView;
	}

	class ViewHolder {
		LinearLayout item_left,shop_item_left;
		LinearLayout item_right;
        LinearLayout item_delete,item_edit;
        private TextView tv_name;
        private TextView tv_api;
        private ImageView iv_default;
	}

	/**
	 * 单击事件监听器
	 */
	private onRightItemClickListener mListener = null;

	public void setOnRightItemClickListener(onRightItemClickListener listener) {
		mListener = listener;
	}

	public interface onRightItemClickListener {
		void onRightItemClick(View v, int position);
	}

}
