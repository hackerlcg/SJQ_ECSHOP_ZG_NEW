package com.ecjia.hamster.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecjia.hamster.fragment.TabsFragment;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.util.OtherUtil;

import java.util.List;

public class SearchHistoryAdapter extends BaseAdapter {
	public List<String> list;
	private Context context;
	private LayoutInflater inflater;
    SharedPreferences remkeywords;
    SharedPreferences.Editor edit;

	public SearchHistoryAdapter(List<String> list, Context context){
		this.list=list;
		this.context=context;
		this.inflater= LayoutInflater.from(context);
        remkeywords = context.getSharedPreferences("keywords", 0);
        edit=remkeywords.edit();
		
	}
	public void setListData(List<String> list){
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
		final String search_list=list.get(position);
		ViewHolder holder=null;
		if(convertView==null){
			holder=new ViewHolder();
			convertView=inflater.inflate(R.layout.search_history_item, null);
			holder.history_name=(TextView) convertView.findViewById(R.id.history_name);
			holder.search_item=(LinearLayout) convertView.findViewById(R.id.search_item);
			convertView.setTag(holder);
			
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		holder.history_name.setText(search_list);
		holder.search_item.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
                edit.putString("keywords",search_list);
                edit.commit();
                if(OtherUtil.isNumber(search_list)){
                    TabsFragment.getInstance().SearchFragment(5,search_list,true);
                }else{
                    TabsFragment.getInstance().SearchFragment(4,search_list,true);
                }

			}
		});
		return convertView;
	}
	
	class ViewHolder{
		TextView history_name;
		LinearLayout search_item;
	}

}
