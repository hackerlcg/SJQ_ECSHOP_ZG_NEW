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
import com.ecjia.hamster.adapter.SelectedGoodsAdapter;
import com.ecjia.hamster.model.GOODS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SelectedGoodsActivity extends BaseActivity {

    private TextView top_view_text;
    private ImageView top_view_back;
    private MyListView listView;
    private SelectedGoodsAdapter adapter;
    private ArrayList<GOODS> goodses=new ArrayList<GOODS>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_selected_gifts);
        Intent intent=getIntent();
        String goods=intent.getStringExtra("goods");
        if(!TextUtils.isEmpty(goods)){
            JSONObject jo=null;
            JSONArray listJsonArray = null;
            try {
                jo=new JSONObject(goods);
                listJsonArray = jo.optJSONArray("goods");
                goodses.clear();
                if (null != listJsonArray && listJsonArray.length() > 0)
                {
                    for (int i = 0; i < listJsonArray.length(); i++)
                    {
                        JSONObject listJsonObject = listJsonArray.getJSONObject(i);
                        GOODS item = GOODS.fromJson(listJsonObject);
                        goodses.add(item);
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
        top_view_text.setText(res.getText(R.string.selected_goods));
        top_view_back = (ImageView) findViewById(R.id.top_view_back);
        listView = (MyListView) findViewById(R.id.mlv_gifts_list);

        top_view_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                    finish();
            }
        });


        if (adapter == null) {
            adapter = new SelectedGoodsAdapter(this, goodses);
        }


        listView.setAdapter(adapter);
    }

}
