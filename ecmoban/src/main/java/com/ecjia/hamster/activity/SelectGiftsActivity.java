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
import com.ecjia.hamster.adapter.SelectGiftsAdapter;
import com.ecjia.hamster.model.GIFT;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SelectGiftsActivity extends BaseActivity {

    private TextView top_view_text;
    private ImageView top_view_back;
    private Button btn_gift_save;
    private SwipeListView2 listView;
    private SelectGiftsAdapter adapter;
    private MyDialog myDialog;
    private ArrayList<GIFT> gifts=new ArrayList<GIFT>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_select_gifts);
        Intent intent=getIntent();
        String gift=intent.getStringExtra("gift");
        if(!TextUtils.isEmpty(gift)){
            JSONObject jo=null;
            JSONArray listJsonArray = null;
            try {
                jo=new JSONObject(gift);
                listJsonArray = jo.optJSONArray("gift");
                gifts.clear();
                if (null != listJsonArray && listJsonArray.length() > 0)
                {
                    for (int i = 0; i < listJsonArray.length(); i++)
                    {
                        JSONObject listJsonObject = listJsonArray.getJSONObject(i);
                        GIFT item = GIFT.fromJson(listJsonObject);
                        gifts.add(item);
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
        top_view_text.setText(res.getText(R.string.select_gift_list));
        top_view_back = (ImageView) findViewById(R.id.top_view_back);
        listView = (SwipeListView2) findViewById(R.id.slv_gifts_list);
        btn_gift_save=(Button) findViewById(R.id.btn_gift_save);

        top_view_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(99);
                finish();
            }
        });


        btn_gift_save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gifts.size()>0){
                    JSONObject requestJsonObject = new JSONObject();
                    try
                    {
                        JSONArray itemJSONArray1 = new JSONArray();
                        for(int i =0; i< gifts.size(); i++)
                        {
                                GIFT itemData=gifts.get(i);
                                JSONObject itemJSONObject = itemData.toJson();
                                itemJSONArray1.put(itemJSONObject);
                        }
                        requestJsonObject.put("gift", itemJSONArray1);
                    } catch (JSONException e) {
                        // TODO: handle exception
                    }

                    Intent intent=new Intent();
                    intent.putExtra("gift",requestJsonObject.toString());
                    intent.putExtra("giftnum",gifts.size());
                    setResult(100,intent);
                    finish();
                }
            }
        });

        if (adapter == null) {
            adapter = new SelectGiftsAdapter(this, gifts, listView.getRightViewWidth());
        }

        adapter.setOnAdpItemClickListener(new SelectGiftsAdapter.onAdpItemClickListener() {

            @Override
            public void onAdpItemClick(View v, final int position) {
                if (v.getId() == R.id.iv_gift_del) {
                    listView.showRight(SwipeListView2.mCurrentItemView);
                } else if (v.getId() == R.id.ll_gift_delete) {
                    listView.deleteItem(SwipeListView2.mCurrentItemView);
                            listView.flag = 0;
                            gifts.remove(position);
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
