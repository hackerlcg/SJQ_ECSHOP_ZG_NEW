package com.ecjia.hamster.activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.component.view.MyDialog;
import com.ecjia.component.view.SwipeListView2;
import com.ecjia.hamster.adapter.SelectCategoryAdapter;
import com.ecjia.hamster.model.CATEGORY;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SelectCategoryActivity extends BaseActivity {

    private TextView top_view_text;
    private ImageView top_view_back;
    private Button btn_category_save;
    private SwipeListView2 listView;
    private SelectCategoryAdapter adapter;
    private MyDialog myDialog;
    private ArrayList<CATEGORY> categories=new ArrayList<CATEGORY>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_select_category);
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
        top_view_text.setText(res.getText(R.string.select_category_list));
        top_view_back = (ImageView) findViewById(R.id.top_view_back);
        listView = (SwipeListView2) findViewById(R.id.slv_category_list);
        btn_category_save=(Button) findViewById(R.id.btn_category_save);

        top_view_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(99);
                    finish();
            }
        });


        btn_category_save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(categories.size()>0){
                    JSONObject requestJsonObject = new JSONObject();
                    try
                    {
                        JSONArray itemJSONArray1 = new JSONArray();
                        for(int i =0; i< categories.size(); i++)
                        {
                                CATEGORY itemData=categories.get(i);
                                JSONObject itemJSONObject = itemData.toJson();
                                itemJSONArray1.put(itemJSONObject);
                        }
                        requestJsonObject.put("category", itemJSONArray1);
                    } catch (JSONException e) {
                        // TODO: handle exception
                    }

                    Intent intent=new Intent();
                    intent.putExtra("category",requestJsonObject.toString());
                    intent.putExtra("categorynum",categories.size());
                    setResult(100,intent);
                    finish();
                }
            }
        });

        if (adapter == null) {
            adapter = new SelectCategoryAdapter(this, categories, listView.getRightViewWidth());
        }

        adapter.setOnAdpItemClickListener(new SelectCategoryAdapter.onAdpItemClickListener() {

            @Override
            public void onAdpItemClick(View v, final int position) {
                if (v.getId() == R.id.iv_category_del) {
                    listView.showRight(SwipeListView2.mCurrentItemView);
                } else if (v.getId() == R.id.ll_category_delete) {
                    listView.deleteItem(SwipeListView2.mCurrentItemView);
                    listView.flag = 0;
                    categories.remove(position);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        listView.setAdapter(adapter);
        listView.flag=0;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(99);
            finish();
        }
        return true;
    }

}
