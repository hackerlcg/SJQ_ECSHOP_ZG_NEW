package com.ecjia.hamster.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;

import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.hamster.model.VOLUME;

import java.util.ArrayList;

public class GoodsBonusAdapter extends BaseAdapter {
	public ArrayList<VOLUME> list;
	public ArrayList<String> slist=new ArrayList<String>();
	private Context context;
	private LayoutInflater inflater;

	public GoodsBonusAdapter(ArrayList<VOLUME> list, Context context){
		this.list=list;
		this.context=context;
		this.inflater= LayoutInflater.from(context);

	}
	public void setListData(ArrayList<VOLUME> list){
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
		if(convertView==null){
			holder=new ViewHolder();
			convertView=inflater.inflate(R.layout.bonus_item, null);
			holder.et_num=(EditText) convertView.findViewById(R.id.et_num);
			holder.et_price=(EditText) convertView.findViewById(R.id.et_price);
			holder.iv_delete=(ImageView) convertView.findViewById(R.id.iv_delete);
			convertView.setTag(holder);
			
		}else{
			holder=(ViewHolder) convertView.getTag();
		}

        if(position==list.size()-1){
            holder.iv_delete.setVisibility(View.GONE);
        }else{
            holder.iv_delete.setVisibility(View.VISIBLE);
        }

        //item里的删除按钮
        holder.iv_delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onRightItemClick(view, position);
                }
            }
        });

        holder.et_price.setText(list.get(position).getPrice());
        holder.et_num.setText(list.get(position).getNumber());
        holder.et_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String content=s.toString();
                list.get(position).setNumber(content);

            }
        });

        holder.et_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                list.get(position).setPrice(s.toString());
            }
        });

        String content=holder.et_num.getText().toString();
        slist.add(content);

		return convertView;
	}
	
	class ViewHolder{
		EditText et_num,et_price;
		ImageView iv_delete;
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
