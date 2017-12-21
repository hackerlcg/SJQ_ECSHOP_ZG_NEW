package com.ecjia.hamster.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.hamster.model.USERRANK;

import java.util.ArrayList;

public class RankAdapter extends BaseAdapter {
	public ArrayList<USERRANK> list;
	private Context context;
	private LayoutInflater inflater;

	public RankAdapter(ArrayList<USERRANK> list, Context context){
		this.list=list;
		this.context=context;
		this.inflater= LayoutInflater.from(context);

	}
	public void setListData(ArrayList<USERRANK> list){
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
        final USERRANK userrank=list.get(position);

		if(convertView==null){
			holder=new ViewHolder();
			convertView=inflater.inflate(R.layout.vip_price_item, null);
			holder.tv_name=(TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_per=(TextView) convertView.findViewById(R.id.tv_per);
			holder.et_vip_price=(EditText) convertView.findViewById(R.id.et_vip_price);
			convertView.setTag(holder);
			
		}else{
			holder=(ViewHolder) convertView.getTag();
		}

        holder.tv_name.setText(userrank.getRank_name());
        holder.tv_per.setText("("+userrank.getDiscount()+"%)");
        holder.et_vip_price.setText(userrank.getPrice());

        holder.et_vip_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                 userrank.setPrice(s.toString());
            }
        });

		return convertView;
	}
	
	class ViewHolder{
		EditText et_vip_price;
        TextView tv_name,tv_per;
	}

}
