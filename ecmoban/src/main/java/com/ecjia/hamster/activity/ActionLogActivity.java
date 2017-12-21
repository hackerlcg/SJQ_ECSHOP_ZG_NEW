package com.ecjia.hamster.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecjia.hamster.adapter.ActionAdapter;
import com.ecjia.hamster.model.ACTION;
import com.ecmoban.android.shopkeeper.sijiqing.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ActionLogActivity extends BaseActivity
{
    private ImageView back;
    private TextView title;
    private ArrayList<ACTION> action;
    private ListView listview;
    private ActionAdapter actionAdapter;
    private FrameLayout fl_null,fl_notnull;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_action_log);

        title = (TextView) findViewById(R.id.top_view_text);
        title.setText(res.getString(R.string.action_log));
        back = (ImageView) findViewById(R.id.top_view_back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        action=new ArrayList<ACTION>();
        Intent intent=getIntent();
        String s = intent.getStringExtra("data");
        if (null != s && s.length() > 0)
        {
            try{
                JSONObject jo = new JSONObject(s);
                JSONArray contentArray = jo.optJSONArray("action_logs");

                action.clear();
                if (null != contentArray && contentArray.length() > 0)
                {
                    for (int i = 0; i < contentArray.length(); i++)
                    {
                        JSONObject contentJsonObject = contentArray.getJSONObject(i);
                        ACTION Item = ACTION.fromJson(contentJsonObject);
//                        if(Item.getId()!=0){
                            action.add(Item);
//                        }
                    }
                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        initView();
    }

    private void initView() {
        fl_null=(FrameLayout)findViewById(R.id.fl_null);
        fl_notnull=(FrameLayout)findViewById(R.id.fl_notnull);

        if(action.size()>0){
            fl_notnull.setVisibility(View.VISIBLE);
            fl_null.setVisibility(View.GONE);
        }else{
            fl_null.setVisibility(View.VISIBLE);
            fl_notnull.setVisibility(View.GONE);
        }

        listview=(ListView)findViewById(R.id.listView);
        actionAdapter=new ActionAdapter(this,action);
        listview.setAdapter(actionAdapter);
    }

}
