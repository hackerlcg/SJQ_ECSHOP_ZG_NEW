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
import com.ecjia.hamster.adapter.SelectBrandsAdapter;
import com.ecjia.hamster.model.BRAND;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SelectBrandsActivity extends BaseActivity {

    private TextView top_view_text;
    private ImageView top_view_back;
    private Button btn_brands_save;
    private SwipeListView2 listView;
    private SelectBrandsAdapter adapter;
    private MyDialog myDialog;
    private ArrayList<BRAND> brands=new ArrayList<BRAND>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_select_brands);
        Intent intent=getIntent();
        String gift=intent.getStringExtra("brands");
        if(!TextUtils.isEmpty(gift)){
            JSONObject jo=null;
            JSONArray listJsonArray = null;
            try {
                jo=new JSONObject(gift);
                listJsonArray = jo.optJSONArray("brands");
                brands.clear();
                if (null != listJsonArray && listJsonArray.length() > 0)
                {
                    for (int i = 0; i < listJsonArray.length(); i++)
                    {
                        JSONObject listJsonObject = listJsonArray.getJSONObject(i);
                        BRAND item = BRAND.fromJson(listJsonObject);
                        brands.add(item);
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
        top_view_text.setText(res.getText(R.string.select_brand_list));
        top_view_back = (ImageView) findViewById(R.id.top_view_back);
        listView = (SwipeListView2) findViewById(R.id.slv_brands_list);
        btn_brands_save=(Button) findViewById(R.id.btn_brands_save);

        top_view_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(99);
                    finish();
            }
        });


        btn_brands_save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(brands.size()>0){
                    JSONObject requestJsonObject = new JSONObject();
                    try
                    {
                        JSONArray itemJSONArray1 = new JSONArray();
                        for(int i =0; i< brands.size(); i++)
                        {
                                BRAND itemData=brands.get(i);
                                JSONObject itemJSONObject = itemData.toJson();
                                itemJSONArray1.put(itemJSONObject);
                        }
                        requestJsonObject.put("brands", itemJSONArray1);
                    } catch (JSONException e) {
                        // TODO: handle exception
                    }

                    Intent intent=new Intent();
                    intent.putExtra("brands",requestJsonObject.toString());
                    intent.putExtra("brandsnum",brands.size());
                    setResult(100,intent);
                    finish();
                }
            }
        });

        if (adapter == null) {
            adapter = new SelectBrandsAdapter(this, brands, listView.getRightViewWidth());
        }

        adapter.setOnAdpItemClickListener(new SelectBrandsAdapter.onAdpItemClickListener() {

            @Override
            public void onAdpItemClick(View v, final int position) {
                if (v.getId() == R.id.iv_brands_del) {
                    listView.showRight(SwipeListView2.mCurrentItemView);
                } else if (v.getId() == R.id.ll_brands_delete) {
                    listView.deleteItem(SwipeListView2.mCurrentItemView);
                    listView.flag = 0;
                    brands.remove(position);
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
