package com.ecjia.hamster.activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.component.view.MyListView;
import com.ecjia.hamster.adapter.SelectedCategoryAdapter;
import com.ecjia.hamster.model.CATEGORY;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SelectedCategoryActivity extends BaseActivity {

    private TextView top_view_text;
    private ImageView top_view_back;
    private MyListView listView;
    private SelectedCategoryAdapter adapter;
    private ArrayList<CATEGORY> categories=new ArrayList<CATEGORY>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_selected_category);
        Intent intent=getIntent();
        String category=intent.getStringExtra("category");
        if(!TextUtils.isEmpty(category)){
            JSONObject jo=null;
            JSONArray listJsonArray = null;
            try {
                jo=new JSONObject(category);
                listJsonArray = jo.optJSONArray("category");
                categories.clear();
                if (null != listJsonArray && listJsonArray.length() > 0)
                {
                    for (int i = 0; i < listJsonArray.length(); i++)
                    {
                        JSONObject listJsonObject = listJsonArray.getJSONObject(i);
                        CATEGORY item = CATEGORY.fromJson(listJsonObject);
                        categories.add(item);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        initView();

    }

    private void initView() {
        top_view_text = (TextView) findViewById(R.id.top_view_text);
        top_view_text.setText(res.getText(R.string.selected_category));
        top_view_back = (ImageView) findViewById(R.id.top_view_back);
        listView = (MyListView) findViewById(R.id.mlv_category_list);

        top_view_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                    finish();
            }
        });


        if (adapter == null) {
            adapter = new SelectedCategoryAdapter(this, categories);
        }


        listView.setAdapter(adapter);
    }

}
