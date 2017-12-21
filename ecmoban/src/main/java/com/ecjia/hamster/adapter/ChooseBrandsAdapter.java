package com.ecjia.hamster.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecjia.hamster.model.BRAND;
import com.ecmoban.android.shopkeeper.sijiqing.R;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by lm on 2016/4/23.
 */
public class ChooseBrandsAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    public List<BRAND> list;
    final int VIEW_TYPE = 3;
    public String[] sections;//
    public HashMap<String, Integer> alphaIndexer;

    public ChooseBrandsAdapter(Context context, List<BRAND> list) {
        this.inflater = LayoutInflater.from(context);
        this.list = list;

    }

    @Override
    public int getCount() {
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
    public int getViewTypeCount() {
        return VIEW_TYPE;
    }

    public  void resetLetter(){
        alphaIndexer = new HashMap<String, Integer>();
        sections = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            String currentStr = getAlpha(list.get(i).getPinyin());

            String previewStr = (i - 1) >= 0 ? getAlpha(list.get(i - 1)
                    .getPinyin()) : " ";
            if (!previewStr.equals(currentStr)) {
                String name = getAlpha(list.get(i).getPinyin());
                alphaIndexer.put(name, i);
                sections[i] = name;
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.brands_letter_list_item, null);
            holder = new ViewHolder();
            holder.alpha = (TextView) convertView
                    .findViewById(R.id.alpha);
            holder.name = (TextView) convertView
                    .findViewById(R.id.name);
            holder.top=convertView.findViewById(R.id.item_top);
            holder.short_line=convertView.findViewById(R.id.bottom_short_line);
            holder.long_line=convertView.findViewById(R.id.bottom_long_line);
            holder.iv_brand_click=(ImageView)convertView.findViewById(R.id.iv_brand_click);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(list.get(position).isClicked()){
            holder.iv_brand_click.setVisibility(View.VISIBLE);
        }else{
            holder.iv_brand_click.setVisibility(View.INVISIBLE);
        }

        holder.name.setText(list.get(position).getBrand_name());
        String currentStr = getAlpha(list.get(position).getPinyin());
        String previewStr = (position - 1) >= 0 ? getAlpha(list
                .get(position - 1).getPinyin()) : " ";
        String nextStr = (position + 1) <= list.size()-1 ? getAlpha(list
                .get(position + 1).getPinyin()) : " ";
        if (!previewStr.equals(currentStr)) {
            holder.alpha.setVisibility(View.VISIBLE);
            holder.alpha.setText(currentStr);
            holder.top.setVisibility(View.VISIBLE);
        } else {
            holder.alpha.setVisibility(View.GONE);
            holder.top.setVisibility(View.GONE);
        }

        if (!nextStr.equals(currentStr)) {
            holder.long_line.setVisibility(View.VISIBLE);
            holder.short_line.setVisibility(View.GONE);
        } else {
            holder.long_line.setVisibility(View.GONE);
            holder.short_line.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    private class ViewHolder {
        TextView alpha;
        TextView name;
        View top,short_line,long_line;
        ImageView iv_brand_click;
    }


    private String getAlpha(String str) {

        if (str.equals("-")) {
            return "&";
        }
        if (str == null) {
            return "#";
        }
        if (str.trim().length() == 0) {
            return "#";
        }
        char c = str.trim().substring(0, 1).charAt(0);
        // 匹配字母
        Pattern pattern = Pattern.compile("^[A-Za-z]+$");
        if (pattern.matcher(c + "").matches()) {
            return (c + "").toUpperCase();
        } else {
            return "#";
        }
    }
}
