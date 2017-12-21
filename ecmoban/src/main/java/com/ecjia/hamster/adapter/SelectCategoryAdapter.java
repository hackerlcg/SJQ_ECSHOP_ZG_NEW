package com.ecjia.hamster.adapter;


import android.content.Context;
import android.text.TextUtils;
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
import com.ecjia.hamster.model.CATEGORY;

import java.util.ArrayList;

public class SelectCategoryAdapter extends BaseAdapter {

	private Context context;
	public ArrayList<CATEGORY> list;
	private LayoutInflater inflater;
	private int mRightWidth = 0;

	public SelectCategoryAdapter(Context context, ArrayList<CATEGORY> list, int RightViewWidth) {
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
        final CATEGORY category = list.get(position);
		if(convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.select_category_item, parent, false);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.parent_name = (TextView) convertView.findViewById(R.id.parent_name);
			holder.item_left =  (LinearLayout) convertView
					.findViewById(R.id.item_left);
			holder.item_right = (LinearLayout) convertView
					.findViewById(R.id.item_right);
            holder.iv_category_del= (ImageView) convertView.findViewById(R.id.iv_category_del);
            holder.item_delete= (LinearLayout) convertView.findViewById(R.id.ll_category_delete);
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


        LayoutParams lp1 = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		holder.item_left.setLayoutParams(lp1);
		LayoutParams lp2 = new LayoutParams(mRightWidth,
				LayoutParams.MATCH_PARENT);
		holder.item_right.setLayoutParams(lp2);


        holder.tv_name.setText(category.getCat_name());

        if(TextUtils.isEmpty(category.getAll_parent())){
            holder.parent_name.setVisibility(View.GONE);
            holder.parent_name.setText("");
        }else{
            holder.parent_name.setVisibility(View.VISIBLE);
            holder.parent_name.setText(category.getAll_parent());
        }

		holder.item_delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mListener != null) {
					mListener.onAdpItemClick(v, position);
				}
			}
		});
        holder.iv_category_del.setOnClickListener(new OnClickListener() {
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
        private TextView tv_name,parent_name;
        private ImageView iv_category_del;
        private View short_line,long_line,top_line;
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
