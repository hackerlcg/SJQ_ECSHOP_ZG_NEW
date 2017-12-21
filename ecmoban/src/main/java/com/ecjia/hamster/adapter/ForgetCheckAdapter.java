package com.ecjia.hamster.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ecjia.hamster.model.DBUSER;
import com.ecmoban.android.shopkeeper.sijiqing.R;

import java.util.List;

public class ForgetCheckAdapter extends BaseAdapter {

	private Context context;
	private List<DBUSER> list;
	private LayoutInflater inflater;

	public ForgetCheckAdapter(Context context, List<DBUSER> list) {
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if(convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.shop_check_list_item, null);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_api = (TextView) convertView.findViewById(R.id.tv_api);
			holder.tv_check = (TextView) convertView.findViewById(R.id.tv_check);
			holder.tv_nocheck = (TextView) convertView.findViewById(R.id.tv_nocheck);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final DBUSER users = list.get(position);

        holder.tv_name.setText(users.getName());
        holder.tv_api.setText(users.getApi());

        if(users.getIsCheck()==0){
            holder.tv_nocheck.setVisibility(View.VISIBLE);
            holder.tv_check.setVisibility(View.GONE);
        }else {
            holder.tv_nocheck.setVisibility(View.GONE);
            holder.tv_check.setVisibility(View.VISIBLE);
        }


		holder.tv_nocheck.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mListener != null) {
					mListener.onCheckItemClick(v, position);
				}
			}
		});

		return convertView;
	}

	class ViewHolder {
        private TextView tv_check,tv_nocheck,tv_name,tv_api;
	}

	/**
	 * 单击事件监听器
	 */
	private onCheckItemClickListener mListener = null;

	public void setOnCheckItemClickListener(onCheckItemClickListener listener) {
		mListener = listener;
	}

	public interface onCheckItemClickListener {
		void onCheckItemClick(View v, int position);
	}

}
