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

public class GoodsChooseAdapter extends BaseAdapter {
	public ArrayList<GOODS> list;
	private Context context;
	private LayoutInflater inflater;

	public GoodsChooseAdapter(ArrayList<GOODS> list, Context context){
		this.list=list;
		this.context=context;
		this.inflater= LayoutInflater.from(context);

	}
	public void setListData(ArrayList<GOODS> list){
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
        final GOODS goods=list.get(position);

		if(convertView==null){
			holder=new ViewHolder();
			convertView=inflater.inflate(R.layout.goods_choose_item, null);
			holder.ll_goods_item=(LinearLayout) convertView.findViewById(R.id.ll_goods_item);
            holder.iv_promote = (ImageView) convertView.findViewById(R.id.iv_promote);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_account = (TextView) convertView.findViewById(R.id.tv_account);
            holder.tv_click = (TextView) convertView.findViewById(R.id.tv_click);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.short_line = (View) convertView.findViewById(R.id.bottom_short_line);
            holder.long_line = (View) convertView.findViewById(R.id.bottom_long_line);
			holder.iv_goods_check=(ImageView) convertView.findViewById(R.id.iv_goods_check);
			holder.iv_goods=(ImageView) convertView.findViewById(R.id.iv_goods);
			convertView.setTag(holder);
			
		}else{
			holder=(ViewHolder) convertView.getTag();
		}

        if(goods.isChecked()){
            holder.iv_goods_check.setImageResource(R.drawable.goods_cb_checked);
        }else{
            holder.iv_goods_check.setImageResource(R.drawable.goods_cb_unchecked);
        }

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

        holder.ll_goods_item.setOnClickListener(new View.OnClickListener() {
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
        private TextView tv_name,tv_account,tv_click,tv_time;
        private LinearLayout ll_goods_item;
        private ImageView iv_goods_check,iv_goods,iv_promote;
        private View short_line,long_line;
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
