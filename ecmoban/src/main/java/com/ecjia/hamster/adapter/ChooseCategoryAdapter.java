package com.ecjia.hamster.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.hamster.model.CATEGORY;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by lm on 2016/4/26.
 */
public class ChooseCategoryAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    public List<CATEGORY> list;
    public String[] sections;//
    public HashMap<String, Integer> alphaIndexer;

    public ChooseCategoryAdapter(Context context, List<CATEGORY> list) {
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
            convertView = inflater.inflate(R.layout.category_letter_list_item, null);
            holder = new ViewHolder();
            holder.alpha = (TextView) convertView
                    .findViewById(R.id.alpha);
            holder.name = (TextView) convertView
                    .findViewById(R.id.name);
            holder.parent_name = (TextView) convertView
                    .findViewById(R.id.parent_name);
            holder.top=convertView.findViewById(R.id.item_top);
            holder.short_line=convertView.findViewById(R.id.bottom_short_line);
            holder.long_line=convertView.findViewById(R.id.bottom_long_line);
            holder.iv_category_click=(ImageView)convertView.findViewById(R.id.iv_category_click);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(list.get(position).isChoose()){
            holder.iv_category_click.setVisibility(View.VISIBLE);
        }else{
            holder.iv_category_click.setVisibility(View.INVISIBLE);
        }

        holder.name.setText(list.get(position).getCat_name());
        if(TextUtils.isEmpty(list.get(position).getAll_parent())){
            holder.parent_name.setVisibility(View.GONE);
            holder.parent_name.setText("");
        }else{
            holder.parent_name.setVisibility(View.VISIBLE);
            holder.parent_name.setText(list.get(position).getAll_parent());
        }
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
        TextView name,parent_name;
        View top,short_line,long_line;
        ImageView iv_category_click;
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
