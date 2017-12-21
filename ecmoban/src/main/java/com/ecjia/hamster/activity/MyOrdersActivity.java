package com.ecjia.hamster.activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.component.network.model.OrdersDetailModel;
import com.ecjia.component.network.model.OrdersListModel;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.component.view.ToastView;
import com.ecjia.component.view.XListView;
import com.ecjia.consts.AppConst;
import com.ecjia.hamster.adapter.OrdersListAdapter;
import com.ecjia.hamster.model.HttpResponse;
import com.ecjia.hamster.model.PAGINATED;
import com.ecjia.hamster.model.SESSION;
import com.ecjia.hamster.model.STATUS;
import com.ecjia.util.EventBus.MyEvent;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

public class MyOrdersActivity extends BaseActivity implements View.OnClickListener,HttpResponse,
        XListView.IXListViewListener{
    private SharedPreferences shared;
    public Handler Intenthandler;
    private RelativeLayout rl_1,rl_2,rl_3,rl_4,rl_5,rl_6;
    private TextView tv_1,tv_2,tv_3,tv_4,tv_5,tv_6;
    private View line_1,line_2,line_3,line_4,line_5,line_6;
    private int TYPE;
    private String KEYWORDS;
    private TextView top_view_text;
    private ImageView top_view_back;
    private String uid,sid,api;
    private FrameLayout fl_null,fl_notnull;
    private SESSION session;
    private XListView listview;
    private OrdersListModel dataModel;
    private OrdersDetailModel detailModel;
    private OrdersListAdapter listAdapter;
    private View ll_bottom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myorders);

        EventBus.getDefault().register(this);
        shared = this.getSharedPreferences("userInfo", 0);
        uid=shared.getString("uid", "");
        sid=shared.getString("sid","");
        api=shared.getString("shopapi","");
        session=new SESSION();
        session.setUid(uid);
        session.setSid(sid);

        KEYWORDS="";

        if (null == dataModel) {
            dataModel = new OrdersListModel(this);
            dataModel.addResponseListener(this);
        }
        if (null == detailModel) {
            detailModel = new OrdersDetailModel(this);
            detailModel.addResponseListener(this);
        }
        initView();
        if(TYPE!=0){
            refreshUI(TYPE);
            dataModel.fetchPreSearch(session, getFtype(TYPE), KEYWORDS, api, true);
        }else{
            TYPE=1;
            dataModel.fetchPreSearch(session,getFtype(TYPE),KEYWORDS,api,true);
        }
    }

    private void initView() {
        fl_null=(FrameLayout)findViewById(R.id.fl_null);
        fl_notnull=(FrameLayout)findViewById(R.id.fl_notnull);

        ll_bottom=(View)findViewById(R.id.ll_bottom);
        ll_bottom.setVisibility(View.GONE);

        listview=(XListView)findViewById(R.id.listview);

        rl_1=(RelativeLayout)findViewById(R.id.rl_1);
        rl_2=(RelativeLayout)findViewById(R.id.rl_2);
        rl_3=(RelativeLayout)findViewById(R.id.rl_3);
        rl_4=(RelativeLayout)findViewById(R.id.rl_4);
        rl_5=(RelativeLayout)findViewById(R.id.rl_5);
        rl_6=(RelativeLayout)findViewById(R.id.rl_6);

        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams((int)getDisplayMetricsWidth()/4, ViewGroup.LayoutParams.MATCH_PARENT);

        rl_1.setLayoutParams(params);
        rl_2.setLayoutParams(params);
        rl_3.setLayoutParams(params);
        rl_4.setLayoutParams(params);
        rl_5.setLayoutParams(params);
        rl_6.setLayoutParams(params);

        rl_1.setOnClickListener(this);
        rl_2.setOnClickListener(this);
        rl_3.setOnClickListener(this);
        rl_4.setOnClickListener(this);
        rl_5.setOnClickListener(this);
        rl_6.setOnClickListener(this);

        tv_1=(TextView)findViewById(R.id.tv_1);
        tv_2=(TextView)findViewById(R.id.tv_2);
        tv_3=(TextView)findViewById(R.id.tv_3);
        tv_4=(TextView)findViewById(R.id.tv_4);
        tv_5=(TextView)findViewById(R.id.tv_5);
        tv_6=(TextView)findViewById(R.id.tv_6);

        line_1=(View)findViewById(R.id.line_1);
        line_2=(View)findViewById(R.id.line_2);
        line_3=(View)findViewById(R.id.line_3);
        line_4=(View)findViewById(R.id.line_4);
        line_5=(View)findViewById(R.id.line_5);
        line_6=(View)findViewById(R.id.line_6);

        top_view_text=(TextView)findViewById(R.id.top_view_text);
        top_view_text.setText(this.getResources().getString(R.string.my_orders));

        top_view_back=(ImageView)findViewById(R.id.top_view_back);
        top_view_back.setOnClickListener(this);


        if (null == listAdapter) {
            listAdapter = new OrdersListAdapter(this, dataModel.ordersList);
        }
        listview.setAdapter(listAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(position!=0){
                    position=position-1;
                }
                detailModel.fetchOrderDetail(session,dataModel.ordersList.get(position).getId(),api);

            }
        });

        listview.setPullLoadEnable(false);
        listview.setPullRefreshEnable(true);
        listview.setXListViewListener(this, 0);
        listview.setRefreshTime();
    }

    @Override
    public void onRefresh(int id) {
        dataModel.fetchPreSearch(session,getFtype(TYPE),KEYWORDS,api,false);
    }

    @Override
    public void onLoadMore(int id) {
        dataModel.fetchPreSearchMore(session, getFtype(TYPE), KEYWORDS, api);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    // 获取屏幕宽度
    public int getDisplayMetricsWidth() {
        int i = this.getWindowManager().getDefaultDisplay().getWidth();
        int j = this.getWindowManager().getDefaultDisplay()
                .getHeight();
        return Math.min(i, j);
    }

    private void checkNull() {
        if(dataModel.ordersList.size()==0){
            fl_null.setVisibility(View.VISIBLE);
            fl_notnull.setVisibility(View.GONE);
        }else {
            fl_null.setVisibility(View.GONE);
            fl_notnull.setVisibility(View.VISIBLE);
        }
    }


    //1待付款
    //2待发货
    //3已发货
    //4已完成
    //5退款
    //6已关闭
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_1:
                if(TYPE!=1) {
                    TYPE = 1;
                    refreshUI(TYPE);
                    dataModel.fetchPreSearch(session, getFtype(TYPE), KEYWORDS, api, true);
                }
                break;
            case R.id.rl_2:
                if(TYPE!=2) {
                    TYPE = 2;
                    refreshUI(TYPE);
                    dataModel.fetchPreSearch(session, getFtype(TYPE), KEYWORDS, api, true);
                }
                break;
            case R.id.rl_3:
                if(TYPE!=3) {
                    TYPE = 3;
                    refreshUI(TYPE);
                    dataModel.fetchPreSearch(session, getFtype(TYPE), KEYWORDS, api, true);
                }
                break;
            case R.id.rl_4:
                if(TYPE!=4) {
                    TYPE = 4;
                    refreshUI(TYPE);
                    dataModel.fetchPreSearch(session, getFtype(TYPE), KEYWORDS, api, true);
                }
                break;
            case R.id.rl_5:
                if(TYPE!=5) {
                    TYPE = 5;
                    refreshUI(TYPE);
                    dataModel.fetchPreSearch(session, getFtype(TYPE), KEYWORDS, api, true);
                }
                break;
            case R.id.rl_6:
                if(TYPE!=6) {
                    TYPE = 6;
                    refreshUI(TYPE);
                    dataModel.fetchPreSearch(session, getFtype(TYPE), KEYWORDS, api, true);
                }
                break;
            case R.id.top_view_back:
                finish();
                break;
        }
    }
    private String getFtype(int type){
        String ftype="await_pay";
        switch (type){
            case 1:
                ftype="await_pay";
                break;
            case 2:
                ftype="await_ship";
                break;
            case 3:
                ftype="shipped";
                break;
            case 4:
                ftype="finished";
                break;
            case 5:
                ftype="refund";
                break;
            case 6:
                ftype="closed";
                break;
        }
        return ftype;
    }


    private void refreshUI(int type) {
        switch (type){
            case 1:
                tv_1.setTextColor(this.getResources().getColor(R.color.bg_theme_color));
                tv_2.setTextColor(this.getResources().getColor(R.color.text_login_hint_color));
                tv_3.setTextColor(this.getResources().getColor(R.color.text_login_hint_color));
                tv_4.setTextColor(this.getResources().getColor(R.color.text_login_hint_color));
                tv_5.setTextColor(this.getResources().getColor(R.color.text_login_hint_color));
                tv_6.setTextColor(this.getResources().getColor(R.color.text_login_hint_color));
                line_1.setVisibility(View.VISIBLE);
                line_2.setVisibility(View.INVISIBLE);
                line_3.setVisibility(View.INVISIBLE);
                line_4.setVisibility(View.INVISIBLE);
                line_5.setVisibility(View.INVISIBLE);
                line_6.setVisibility(View.INVISIBLE);
                break;
            case 2:
                tv_1.setTextColor(this.getResources().getColor(R.color.text_login_hint_color));
                tv_2.setTextColor(this.getResources().getColor(R.color.bg_theme_color));
                tv_3.setTextColor(this.getResources().getColor(R.color.text_login_hint_color));
                tv_4.setTextColor(this.getResources().getColor(R.color.text_login_hint_color));
                tv_5.setTextColor(this.getResources().getColor(R.color.text_login_hint_color));
                tv_6.setTextColor(this.getResources().getColor(R.color.text_login_hint_color));
                line_1.setVisibility(View.INVISIBLE);
                line_2.setVisibility(View.VISIBLE);
                line_3.setVisibility(View.INVISIBLE);
                line_4.setVisibility(View.INVISIBLE);
                line_5.setVisibility(View.INVISIBLE);
                line_6.setVisibility(View.INVISIBLE);
                break;
            case 3:
                tv_1.setTextColor(this.getResources().getColor(R.color.text_login_hint_color));
                tv_2.setTextColor(this.getResources().getColor(R.color.text_login_hint_color));
                tv_3.setTextColor(this.getResources().getColor(R.color.bg_theme_color));
                tv_4.setTextColor(this.getResources().getColor(R.color.text_login_hint_color));
                tv_5.setTextColor(this.getResources().getColor(R.color.text_login_hint_color));
                tv_6.setTextColor(this.getResources().getColor(R.color.text_login_hint_color));
                line_1.setVisibility(View.INVISIBLE);
                line_2.setVisibility(View.INVISIBLE);
                line_3.setVisibility(View.VISIBLE);
                line_4.setVisibility(View.INVISIBLE);
                line_5.setVisibility(View.INVISIBLE);
                line_6.setVisibility(View.INVISIBLE);
                break;
            case 4:
                tv_1.setTextColor(this.getResources().getColor(R.color.text_login_hint_color));
                tv_2.setTextColor(this.getResources().getColor(R.color.text_login_hint_color));
                tv_3.setTextColor(this.getResources().getColor(R.color.text_login_hint_color));
                tv_4.setTextColor(this.getResources().getColor(R.color.bg_theme_color));
                tv_5.setTextColor(this.getResources().getColor(R.color.text_login_hint_color));
                tv_6.setTextColor(this.getResources().getColor(R.color.text_login_hint_color));
                line_1.setVisibility(View.INVISIBLE);
                line_2.setVisibility(View.INVISIBLE);
                line_3.setVisibility(View.INVISIBLE);
                line_4.setVisibility(View.VISIBLE);
                line_5.setVisibility(View.INVISIBLE);
                line_6.setVisibility(View.INVISIBLE);
                break;
            case 5:
                tv_1.setTextColor(this.getResources().getColor(R.color.text_login_hint_color));
                tv_2.setTextColor(this.getResources().getColor(R.color.text_login_hint_color));
                tv_3.setTextColor(this.getResources().getColor(R.color.text_login_hint_color));
                tv_4.setTextColor(this.getResources().getColor(R.color.text_login_hint_color));
                tv_5.setTextColor(this.getResources().getColor(R.color.bg_theme_color));
                tv_6.setTextColor(this.getResources().getColor(R.color.text_login_hint_color));
                line_1.setVisibility(View.INVISIBLE);
                line_2.setVisibility(View.INVISIBLE);
                line_3.setVisibility(View.INVISIBLE);
                line_4.setVisibility(View.INVISIBLE);
                line_5.setVisibility(View.VISIBLE);
                line_6.setVisibility(View.INVISIBLE);
                break;
            case 6:
                tv_1.setTextColor(this.getResources().getColor(R.color.text_login_hint_color));
                tv_2.setTextColor(this.getResources().getColor(R.color.text_login_hint_color));
                tv_3.setTextColor(this.getResources().getColor(R.color.text_login_hint_color));
                tv_4.setTextColor(this.getResources().getColor(R.color.text_login_hint_color));
                tv_5.setTextColor(this.getResources().getColor(R.color.text_login_hint_color));
                tv_6.setTextColor(this.getResources().getColor(R.color.bg_theme_color));
                line_1.setVisibility(View.INVISIBLE);
                line_2.setVisibility(View.INVISIBLE);
                line_3.setVisibility(View.INVISIBLE);
                line_4.setVisibility(View.INVISIBLE);
                line_5.setVisibility(View.INVISIBLE);
                line_6.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void onEvent(MyEvent event) {
        if ("ORDERCANCEL".equals(event.getMsg())) {
            dataModel.fetchPreSearch(session,getFtype(TYPE),KEYWORDS,api,true);
        }
    }

    @Override
    public void OnMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if (url.equals(ProtocolConst.ORDERS)) {
            if (status.getSucceed() == 1) {
                listview.stopRefresh();
                listview.stopLoadMore();
                listview.setRefreshTime();
                PAGINATED paginated = dataModel.paginated;
                if (0 == paginated.getMore()) {
                    listview.setPullLoadEnable(false);
                } else {
                    listview.setPullLoadEnable(true);
                }
                listAdapter.notifyDataSetChanged();
                checkNull();
            }
        }

        if (url.equals(ProtocolConst.ORDERDETAIL)) {
            String error13 = this.getResources().getString(R.string.error_13);
            String error101 = this.getResources().getString(R.string.error_101);

            if (status.getSucceed() == 1) {
                if(detailModel.order.getSuborderses().size()>0){
                    Intent intent = new Intent(this, SubOrderActivity.class);
                    try {
                        intent.putExtra("data",detailModel.order.toJson().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    intent.putExtra("type", TYPE);
                    intent.putExtra("id", detailModel.order.getId());
                    this.startActivity(intent);
                    this.overridePendingTransition(R.anim.push_right_in,
                            R.anim.push_right_out);
                }else{
                    Intent intent = new Intent(this, OrderDetailActivity.class);
                    intent.putExtra("id", detailModel.order.getId());
                    intent.putExtra("type", TYPE);
                    this.startActivity(intent);
                    this.overridePendingTransition(R.anim.push_right_in,
                            R.anim.push_right_out);
                }

            } else if (status.getSucceed() == AppConst.UnexistInformation) {

                ToastView toast = new ToastView(this, error13);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }else if (status.getSucceed() == AppConst.InvalidParameter) {

                ToastView toast = new ToastView(this, error101);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
    }
}
