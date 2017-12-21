package com.ecjia.hamster.activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.component.network.model.StatsModel;
import com.ecjia.component.view.XListView;
import com.ecjia.hamster.adapter.SalesDetailAdapter;
import com.ecjia.hamster.model.HttpResponse;
import com.ecjia.hamster.model.PAGINATED;
import com.ecjia.hamster.model.SESSION;
import com.ecjia.hamster.model.STATUS;
import com.ecjia.util.TimeUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class SalesDetailActivity extends BaseActivity implements HttpResponse,XListView.IXListViewListener {

	
	private TextView title;
	private ImageView back;
    private SharedPreferences shared;
    private String uid;
    private String sid;
    private String api;
    private SESSION session;
    private StatsModel statsModel;
    private XListView listview;
    private SalesDetailAdapter listAdapter;
    private PAGINATED paginated;
    private String[] selectedday,today,week,Month,nintydays;
    private Intent intent;
    private String selected="";
    private LinearLayout null_page;
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_sales_detail);
        shared = this.getSharedPreferences("userInfo", 0);
        uid = shared.getString("uid", "");
        sid = shared.getString("sid", "");
        api = shared.getString("shopapi", "");

        session=new SESSION();
        session.setUid(uid);
        session.setSid(sid);
        
		title = (TextView) findViewById(R.id.top_view_text);
        String det=res.getString(R.string.sales_detail);
		title.setText(det);
		
		back = (ImageView) findViewById(R.id.top_view_back);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});



        if(statsModel==null){
            statsModel = new StatsModel(this);
            statsModel.addResponseListener(this);
        }
        initView();
        statsModel.fetchSaleDetail(session,selectedday[0],selectedday[1],api,true);

    }

    private void initView() {
        today= TimeUtil.getDateBeforeToday(0, 0);//今天
        week=TimeUtil.getWeekdays();//本周
        Month=TimeUtil.getDateBeforeToday(-30,-1);//前30天
        nintydays=TimeUtil.getDateBeforeToday(-90,-1);//前90天

        intent=getIntent();
        selected=intent.getStringExtra("selectedday");
        if("today".equals(selected)){
            selectedday=today;
        }else if("week".equals(selected)){
            selectedday=week;
        }else if("month".equals(selected)){
            selectedday=Month;
        }else if("nintydays".equals(selected)){
            selectedday=nintydays;
        }else{
            selectedday=today;
        }


        listview=(XListView)findViewById(R.id.listview);

        if(listAdapter==null){
            listAdapter=new SalesDetailAdapter(statsModel.salesList,SalesDetailActivity.this);
        }

        listview.setAdapter(listAdapter);
        null_page= (LinearLayout) findViewById(R.id.null_page);
        listview.setPullLoadEnable(false);
        listview.setPullRefreshEnable(true);
        listview.setXListViewListener(this, 0);
        listview.setRefreshTime();
    }


    @Override
    public void OnMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if (url .equals(ProtocolConst.STATS_SALESDETAIL)) {
            if (status.getSucceed() == 1) {
                if(statsModel.salesList.size()>0) {
                    listview.stopRefresh();
                    listview.stopLoadMore();
                    listview.setRefreshTime();
                    paginated = statsModel.paginated;
                    if (0 == paginated.getMore()) {
                        listview.setPullLoadEnable(false);
                    } else {
                        listview.setPullLoadEnable(true);
                    }
                    listAdapter.notifyDataSetChanged();
                    listview.setVisibility(View.VISIBLE);
                    null_page.setVisibility(View.GONE);
                }else{
                    listview.setVisibility(View.GONE);
                    null_page.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void onRefresh(int id) {
        statsModel.fetchSaleDetail(session,selectedday[0],selectedday[1],api,false);
    }

    @Override
    public void onLoadMore(int id) {
        statsModel.fetchSaleDetailMore(session,selectedday[0],selectedday[1],api);
    }


}
