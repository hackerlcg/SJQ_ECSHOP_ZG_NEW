package com.ecjia.hamster.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.hamster.model.GOODS;
import com.ecjia.util.ImageLoaderForLV;

import java.util.ArrayList;

public class GiftChooseAdapter extends BaseAdapter {
	public ArrayList<GOODS> list;
	private Context context;
	private LayoutInflater inflater;

	public GiftChooseAdapter(ArrayList<GOODS> list, Context context){
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
        final GOODS good=list.get(position);

		if(convertView==null){
			holder=new ViewHolder();
			convertView=inflater.inflate(R.layout.gift_choose_item, null);
			holder.ll_gift_item=(LinearLayout) convertView.findViewById(R.id.ll_gift_item);
			holder.tv_name=(TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_origin_price=(TextView) convertView.findViewById(R.id.tv_origin_price);
			holder.et_price=(EditText) convertView.findViewById(R.id.et_gift_price);
            holder.short_line = (View) convertView.findViewById(R.id.bottom_short_line);
            holder.long_line = (View) convertView.findViewById(R.id.bottom_long_line);
			holder.iv_clear_et=(ImageView) convertView.findViewById(R.id.iv_clear_et);
            holder.iv_clear_et.setOnClickListener(new MyOnClickListener(holder) {
                @Override
                public void onClick(View v, ViewHolder holder) {
                    int position=(Integer) holder.et_price.getTag();
                    list.get(position).setGift_price("");
                    notifyDataSetChanged();
                }
            });
            holder.et_price.setTag(position);
            holder.et_price.addTextChangedListener(new MyTextWatcher(holder) {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s,ViewHolder holder) {
                    if(!TextUtils.isEmpty(s)){
                        int position=(Integer) holder.et_price.getTag();
                        list.get(position).setGift_price(s.toString());
                        if(!TextUtils.isEmpty(s)){
                            holder.iv_clear_et.setVisibility(View.VISIBLE);
                        }else{
                            holder.iv_clear_et.setVisibility(View.GONE);
                        }
                    }
                }
            });


			holder.iv_gift_check=(ImageView) convertView.findViewById(R.id.iv_gift_check);
			holder.iv_gift=(ImageView) convertView.findViewById(R.id.iv_gift);
			convertView.setTag(holder);
			
		}else{
			holder=(ViewHolder) convertView.getTag();
            holder.et_price.setTag(position);
		}

        if(good.isChecked()){
            holder.iv_gift_check.setImageResource(R.drawable.goods_cb_checked);
        }else{
            holder.iv_gift_check.setImageResource(R.drawable.goods_cb_unchecked);
        }

        if(position==list.size()-1){
            holder.short_line.setVisibility(View.GONE);
            holder.long_line.setVisibility(View.VISIBLE);
        }else{
            holder.short_line.setVisibility(View.VISIBLE);
            holder.long_line.setVisibility(View.GONE);
        }

        holder.tv_name.setText(good.getName());

        holder.tv_origin_price.setText(good.getShop_price());
//        if("0".equals(good.getPromote_price())){
//            holder.tv_origin_price.setText(good.getShop_price());
//        }else{
//            holder.tv_origin_price.setText(good.getPromote_price());
//        }

        holder.et_price.setText(good.getGift_price());

        if(!TextUtils.isEmpty(good.getGift_price())){
            holder.iv_clear_et.setVisibility(View.VISIBLE);
        }else{
            holder.iv_clear_et.setVisibility(View.GONE);
        }


        ImageLoaderForLV.getInstance(context).setImageRes(holder.iv_gift,good.getImg().getThumb());

        holder.ll_gift_item.setOnClickListener(new View.OnClickListener() {
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
		private EditText et_price;
        private TextView tv_name;
        private TextView tv_origin_price;
        private LinearLayout ll_gift_item;
        private ImageView iv_gift_check,iv_gift,iv_clear_et;
        public View short_line,long_line;
    }

    private abstract class MyTextWatcher implements TextWatcher{
        private ViewHolder mHolder;

        public MyTextWatcher(ViewHolder holder) {
            this.mHolder=holder;
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
        }
        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                  int arg3) {
        }
        @Override
        public void afterTextChanged(Editable s) {
            afterTextChanged(s, mHolder);
        }
        public abstract void afterTextChanged(Editable s,ViewHolder holder);
    }

    private abstract class MyOnClickListener implements View.OnClickListener {

        private ViewHolder mHolder;

        public MyOnClickListener(ViewHolder holder) {
            this.mHolder=holder;
        }

        @Override
        public void onClick(View v) {
            onClick(v, mHolder);
        }
        public abstract void onClick(View v,ViewHolder holder);

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
