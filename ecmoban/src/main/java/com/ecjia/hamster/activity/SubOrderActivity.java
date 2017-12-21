package com.ecjia.hamster.activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.component.network.model.OrdersDetailModel;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.hamster.adapter.SubOrderAdapter;
import com.ecjia.hamster.model.HttpResponse;
import com.ecjia.hamster.model.SESSION;
import com.ecjia.hamster.model.STATUS;
import com.ecjia.hamster.model.SUBORDERS;
import com.ecjia.util.EventBus.Event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;


public class SubOrderActivity extends BaseActivity implements HttpResponse
{
    private ImageView back;
    private TextView title;
    private ArrayList<SUBORDERS> suborders;
    private ListView listview;
    private SubOrderAdapter subOrderAdapter;
    private int type;
    private String id;
    private OrdersDetailModel detailModel;
    private Intent intent;
    private SharedPreferences shared;
    private String uid,sid,api;
    private SESSION session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_suborder_log);

        EventBus.getDefault().register(this);
        title = (TextView) findViewById(R.id.top_view_text);
        title.setText(res.getString(R.string.sub_order));
        back = (ImageView) findViewById(R.id.top_view_back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        suborders=new ArrayList<SUBORDERS>();
        intent=getIntent();
        type=intent.getIntExtra("type",0);
        String s = intent.getStringExtra("data");
        if (null != s && s.length() > 0)
        {
            try{
                JSONObject jo = new JSONObject(s);
                JSONArray contentArray = jo.optJSONArray("sub_orders");

                if (null != contentArray && contentArray.length() > 0)
                {
                    suborders.clear();
                    for (int i = 0; i < contentArray.length(); i++)
                    {
                        JSONObject contentJsonObject = contentArray.getJSONObject(i);
                        SUBORDERS Item = SUBORDERS.fromJson(contentJsonObject);
                        if(!TextUtils.isEmpty(Item.getId())){
                            suborders.add(Item);
                        }
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
        listview=(ListView)findViewById(R.id.listView);
        subOrderAdapter=new SubOrderAdapter(this,suborders,type);
        listview.setAdapter(subOrderAdapter);
    }

    @Override
    public void OnMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if (url.equals(ProtocolConst.ORDERDETAIL)) {
            if (status.getSucceed() == 1) {
                suborders.clear();
                suborders.addAll(detailModel.order.getSuborderses());

                if(suborders.size()>0){
                    subOrderAdapter.notifyDataSetChanged();
                }else{
                    finish();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    public void onEvent(Event event) {
        if ("ORDERCANCEL".equals(event.getMsg())) {
            if (null == detailModel) {
                detailModel = new OrdersDetailModel(SubOrderActivity.this);
                detailModel.addResponseListener(SubOrderActivity.this);
            }
            id=intent.getStringExtra("id");
            shared = this.getSharedPreferences("userInfo", 0);
            uid = shared.getString("uid", "");
            sid = shared.getString("sid", "");
            api = shared.getString("shopapi", "");

            session=new SESSION();
            session.setUid(uid);
            session.setSid(sid);

            detailModel.fetchOrderDetail(session,id,api);
        }
    }
}
