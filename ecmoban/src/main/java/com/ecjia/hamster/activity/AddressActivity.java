package com.ecjia.hamster.activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.component.network.model.ShopModel;
import com.ecjia.component.view.ToastView;
import com.ecjia.component.wheel.ScreenInfo;
import com.ecjia.component.wheel.WheelAddress;
import com.ecjia.consts.AppConst;
import com.ecjia.hamster.model.HttpResponse;
import com.ecjia.hamster.model.REGIONS;
import com.ecjia.hamster.model.SESSION;
import com.ecjia.hamster.model.STATUS;
import com.ecjia.util.EventBus.MyEvent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import de.greenrobot.event.EventBus;


public class AddressActivity extends BaseActivity implements HttpResponse {

    private SharedPreferences shared;
    private String uid;
    private String sid;
    private String api;
    private SESSION session;
    private ShopModel dataModel;
    private TextView top_view_text,top_right_tv,tv_address;
    private ImageView top_view_back;
    private ArrayList<REGIONS> list;
    private String province,city;
    private WheelAddress wheelAddress;
    private Calendar calendar;
    private String area;
    private int privilege;

    public AddressActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater=LayoutInflater.from(AddressActivity.this);
        final View timepickerview=inflater.inflate(R.layout.act_address, null);
        setContentView(timepickerview);

        ScreenInfo screenInfo = new ScreenInfo(AddressActivity.this);
        wheelAddress = new WheelAddress(timepickerview);
        wheelAddress.screenheight = screenInfo.getHeight();

        EventBus.getDefault().register(this);
        shared = this.getSharedPreferences("userInfo", 0);
        uid = shared.getString("uid", "");
        sid = shared.getString("sid", "");
        api = shared.getString("shopapi", "");
        area= shared.getString("area", "");

        session=new SESSION();
        session.setUid(uid);
        session.setSid(sid);

        Intent intent=getIntent();
        province=intent.getStringExtra("province");
        city=intent.getStringExtra("city");
        privilege=intent.getIntExtra("privilege", 1);

        list=new ArrayList<REGIONS>();

        if (null != area && area.length() > 0)
        {
            try{
                JSONArray contentArray = new JSONArray(area);

                list.clear();
                if (null != contentArray && contentArray.length() > 0)
                {
                    for (int i = 0; i < contentArray.length(); i++)
                    {
                        JSONObject contentJsonObject = contentArray.getJSONObject(i);
                        REGIONS Item = REGIONS.fromJson(contentJsonObject);
                        if(Item.getId()!=0){
                            list.add(Item);
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(dataModel==null){
            dataModel=new ShopModel(this);
            dataModel.addResponseListener(this);
        }


        initView();

    }

    private void initView() {
        top_view_text = (TextView) findViewById(R.id.top_view_text);
        top_right_tv = (TextView) findViewById(R.id.top_right_tv);
        tv_address = (TextView) findViewById(R.id.tv_address);
        top_view_back = (ImageView) findViewById(R.id.top_view_back);

        top_view_text.setText(res.getText(R.string.area));
        top_right_tv.setText(res.getText(R.string.save));
        if(privilege<2){
            top_right_tv.setVisibility(View.GONE);
        }

        tv_address.setText(city+" "+province);

        top_view_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        wheelAddress.initAreaPicker(list,province,city);

        top_right_tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(tv_address.getText().toString())){
                    EventBus.getDefault().post(new MyEvent(wheelAddress.getAddressId(), AppConst.AREA));
                    finish();
                }else{
                    ToastView toast = new ToastView(AddressActivity.this,res.getString(R.string.area_tips));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.setDuration(200);
                    toast.show();
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
        if (url.equals(ProtocolConst.SHOPAREA)) {
            if(status.getSucceed()==1){

            }
        }

    }
}
