package com.ecjia.hamster.activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.component.network.model.GoodsListModel;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.consts.AppConst;
import com.ecjia.hamster.adapter.ShopCategoryAdapter;
import com.ecjia.hamster.model.CATEGORY;
import com.ecjia.hamster.model.HttpResponse;
import com.ecjia.hamster.model.SESSION;
import com.ecjia.hamster.model.STATUS;
import com.ecjia.util.EventBus.MyEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;


public class ShopFilterActivity extends BaseActivity implements HttpResponse {

    private SharedPreferences shared;
    private String uid;
    private String sid;
    private String api;
    private SESSION session;
    private GoodsListModel dataModel;
    private TextView top_view_text;
    private ImageView top_view_back;
    private ArrayList<CATEGORY> list;
    private ListView listView;
    private ShopCategoryAdapter shopCategoryAdapter;
    private String category;

    public ShopFilterActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_shop_filter);

        EventBus.getDefault().register(this);
        shared = this.getSharedPreferences("userInfo", 0);
        uid = shared.getString("uid", "");
        sid = shared.getString("sid", "");
        api = shared.getString("shopapi", "");

        session=new SESSION();
        session.setUid(uid);
        session.setSid(sid);

        list=new ArrayList<CATEGORY>();

        Intent intent=getIntent();
        category=intent.getStringExtra("category");

        if(dataModel==null){
            dataModel=new GoodsListModel(this);
            dataModel.addResponseListener(this);
        }


        initView();

        dataModel.getGoodsCategory(session,api);

    }

    private void initView() {
        top_view_text = (TextView) findViewById(R.id.top_view_text);
        top_view_back = (ImageView) findViewById(R.id.top_view_back);
        listView = (ListView) findViewById(R.id.listView);

        top_view_text.setText(res.getText(R.string.filter_shop));

        top_view_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if(null==shopCategoryAdapter){
            shopCategoryAdapter=new ShopCategoryAdapter(list,this);
        }
        listView.setAdapter(shopCategoryAdapter);

        shopCategoryAdapter.setOnRightItemClickListener(new ShopCategoryAdapter.onRightItemClickListener() {
            @Override
            public void onRightItemClick(View v, int position) {
                if(v.getId()==R.id.filter_name){
                    EventBus.getDefault().post(new MyEvent(
                            shopCategoryAdapter.list.get(position).getCat_id()+"" , AppConst.SCATEGORYT));
                    finish();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void onEvent(MyEvent event) {

    }

    @Override
    public void OnMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if (url.equals(ProtocolConst.GOODSCATEGORY)) {
            if(status.getSucceed()==1){
                ArrayList<CATEGORY> temp=new ArrayList<CATEGORY>();
                temp.addAll(dataModel.categories);
                for(int i=0;i<temp.size();i++){
                    if(temp.get(i).getLevel()==0){
                      if(category.equals(temp.get(i).getCat_name())){
                          temp.get(i).setChoose(true);
                      }
                      list.add(temp.get(i));
                   }
                }
                shopCategoryAdapter.notifyDataSetChanged();
            }
        }

    }
}
