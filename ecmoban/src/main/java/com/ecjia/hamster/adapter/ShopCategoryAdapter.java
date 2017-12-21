package com.ecjia.hamster.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.hamster.model.CATEGORY;

import java.util.ArrayList;

public class ShopCategoryAdapter extends BaseAdapter {
	public ArrayList<CATEGORY> list;
	private Context context;
	private LayoutInflater inflater;

	public ShopCategoryAdapter(ArrayList<CATEGORY> list, Context context){
		this.list=list;
		this.context=context;
		this.inflater= LayoutInflater.from(context);

	}
	public void setListData(ArrayList<CATEGORY> list){
		this.list=list;
	}
	
	@Override
	public int getCount() {
		 if(list==null){
			 return 0;
		 }
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
		ViewHolder holder=null;
        final CATEGORY category=list.get(position);
		if(convertView==null){
			holder=new ViewHolder();
			convertView=inflater.inflate(R.layout.goods_filter_item, null);
			holder.filter_name=(TextView) convertView.findViewById(R.id.filter_name);
			holder.ll_right_filter=(LinearLayout) convertView.findViewById(R.id.ll_right_filter);
			convertView.setTag(holder);
			
		}else{
			holder=(ViewHolder) convertView.getTag();
		}

        holder.ll_right_filter.setVisibility(View.GONE);

        if(category.isChoose()){
            holder.filter_name.setTextColor(context.getResources().getColor(R.color.bg_theme_color));
        }else{
            holder.filter_name.setTextColor(context.getResources().getColor(R.color.text_login_color));
        }

        holder.filter_name.setText(category.getCat_name());

        holder.filter_name.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onRightItemClick(view, position);
                }
            }
        });

        holder.ll_right_filter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onRightItemClick(view, position);
                }
            }
        });



		return convertView;
	}
	
	class ViewHolder{
		TextView filter_name;
		LinearLayout ll_right_filter;
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
