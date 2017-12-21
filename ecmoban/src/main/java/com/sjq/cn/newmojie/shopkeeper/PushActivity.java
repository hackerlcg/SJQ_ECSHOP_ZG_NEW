package com.sjq.cn.newmojie.shopkeeper;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecjia.component.view.XListView;
import com.ecjia.base.BaseActivity;
import com.ecjia.hamster.activity.ECJiaMainActivity;
import com.ecjia.hamster.activity.GoodDetailActivity;
import com.ecjia.hamster.activity.LoginActivity;
import com.ecjia.hamster.activity.OrderDetailActivity;
import com.ecjia.hamster.activity.QRCodeActivity;
import com.ecjia.hamster.activity.SettingActivity;
import com.ecjia.hamster.activity.WebViewActivity;
import com.ecjia.hamster.activity.goods.MyGoodsParentActivity;
import com.ecjia.hamster.fragment.TabsFragment;
import com.ecjia.hamster.model.MYMESSAGE;
import com.ecjia.hamster.adapter.PushAdapter;
import com.ecjia.hamster.adapter.MsgSql;
import com.ecjia.util.EventBus.MyEvent;
import com.ecjia.util.LG;
import com.ecmoban.android.shopkeeper.sijiqing.R;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

public class PushActivity extends BaseActivity implements
        XListView.IXListViewListener{
    private TextView title;
    private ImageView back;
    private SharedPreferences shared;
    private int page = 0;
    private ArrayList<MYMESSAGE> list, datalist;
    private PushAdapter adapter;
    private XListView listView;
    private Handler handler;
    private FrameLayout fl_null,fl_notnull;
    private int dbnum;
    private SharedPreferences share;
    private SharedPreferences.Editor editor;
    private final static int PAGESIZE=8; //加载条目限制


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push);

        EventBus.getDefault().register(this);

        if(datalist==null){
            datalist = new ArrayList<MYMESSAGE>();
        }
        share= getSharedPreferences("userInfo", 0);
        editor=share.edit();

        handler = new Handler() {
            public void handleMessage(Message msg) {

                if (msg.arg1 == 1) {
                    if(list.size()<datalist.size()){
                        listView.setPullLoadEnable(true);
                    }else{
                        listView.setPullLoadEnable(false);
                    }
                    listView.stopLoadMore();
                    listView.stopRefresh();
                    listView.setRefreshTime();
                    if(adapter!=null) {
                        adapter.list=list;
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        };
        setInfo();
        getData();
        addData(0);
        adapter = new PushAdapter(this, list);

        adapter.setOnRightItemClickListener(new PushAdapter.onRightItemClickListener() {
            @Override
            public void onRightItemClick(View v, int position) {
                if(v.getId()==R.id.ll_watch){
                    MYMESSAGE msg=adapter.list.get(position);
                    if("goods_detail".equals(msg.getOpen_type())){
                        Intent intent=new Intent(PushActivity.this, GoodDetailActivity.class);
                        intent.putExtra("id",msg.getGoods_id());
                        startActivity(intent);
                    }else if ("orders_detail".equals(msg.getOpen_type())){
                        Intent intent=new Intent(PushActivity.this, OrderDetailActivity.class);
                        intent.putExtra("id",msg.getOrder_id());
                        startActivity(intent);
                    }else if ("main".equals(msg.getOpen_type())) {//主页
                            Intent intent = new Intent(PushActivity.this, ECJiaMainActivity.class);
                            startActivity(intent);
                    } else if ("webview".equals(msg.getOpen_type())) {//浏览器
                        Intent intent = new Intent(PushActivity.this, WebViewActivity.class);
                        intent.putExtra("webUrl", msg.getWebUrl());
                        startActivity(intent);
                    } else if ("signin".equals(msg.getOpen_type())) {//登录
                        Intent intent = new Intent(PushActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }else if ("search".equals(msg.getOpen_type())){
                        finish();
                        TabsFragment.getInstance().msgFragment(2, msg.getKeyword());
                    }else if ("qrshare".equals(msg.getOpen_type())) {//二维码分享
                            Intent intent = new Intent(PushActivity.this, QRCodeActivity.class);
                            startActivity(intent);
                    } else if ("setting".equals(msg.getOpen_type())) {//设置
                        Intent intent = new Intent(PushActivity.this, SettingActivity.class);
                        startActivity(intent);
                    } else if ("orders_list".equals(msg.getOpen_type())) {//订单列表（先进首页，再跳fragment）
                        finish();
//                        TabsFragment.getInstance().msgFragment(5,"");
                        TabsFragment.getInstance().OnTabSelected("tab_four");
                    } else if ("goods_list".equals(msg.getOpen_type())) {//商品列表（先进首页，再跳fragment）
                        finish();
                        startActivity(new Intent(PushActivity.this, MyGoodsParentActivity.class));
//                        TabsFragment.getInstance().msgFragment(4,"");
                    }else {
                    }
                    MsgSql.getIntence(PushActivity.this).updateData(msg.getMsg_id());
                    getData();
                    addData(page);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        listView.setAdapter(adapter);
    }

    @SuppressLint("NewApi")
    private void getData() {
        if(list==null) {
            list = new ArrayList<MYMESSAGE>();
        }else{
            list.clear();
        }

        dbnum=0;
        datalist.clear();

        Cursor c = MsgSql.getIntence(this).getAllmsg();
        while (c.moveToNext()) {
            MYMESSAGE msg = new MYMESSAGE();
            String msgtitle = c.getString(1);
            String msgcontent = c.getString(2);
            String msgcustom = c.getString(3);
            String msgtime = c.getString(4);
            String msgid = c.getString(8);
            String msgopentype = c.getString(9);
            String msgorderid = c.getString(13);
            String msggoodid = c.getString(14);
            String msgstatus = c.getString(15);
            String msgkeyword = c.getString(16);


            msg.setTitle(msgtitle);
            msg.setContent(msgcontent);
            msg.setCustom(msgcustom);
            msg.setTime(msgtime);
            msg.setMsg_id(msgid);
            msg.setOpen_type(msgopentype);
            msg.setOrder_id(msgorderid);
            msg.setGoods_id(msggoodid);
            msg.setReadStatus(Integer.parseInt(msgstatus));
            msg.setKeyword(msgkeyword);

            if(Integer.parseInt(msgstatus)==0){
                dbnum+=1;
            }

            datalist.add(msg);
        }

        editor.putInt("msgnum",dbnum);
        editor.commit();

        if(datalist.size()>0){
            fl_notnull.setVisibility(View.VISIBLE);
            fl_null.setVisibility(View.GONE);
            if(datalist.size() > PAGESIZE){
                listView.setPullLoadEnable(true);
                listView.setPullRefreshEnable(true);
            }else{
                listView.setPullRefreshEnable(true);
                listView.setPullLoadEnable(false);
            }
        }else{
            fl_null.setVisibility(View.VISIBLE);
            fl_notnull.setVisibility(View.GONE);
        }
    }

    private void setInfo() {
        fl_null=(FrameLayout)findViewById(R.id.fl_null);
        fl_notnull=(FrameLayout)findViewById(R.id.fl_notnull);
        back = (ImageView) findViewById(R.id.top_view_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                PushActivity.this.overridePendingTransition(
                        R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        title = (TextView) findViewById(R.id.top_view_text);
        title.setText(res.getString(R.string.push_title));
        listView = (XListView) findViewById(R.id.push_listview);
        listView.setXListViewListener(this,1);
        listView.setRefreshTime();
    }

    private void addData(int page) {
        int start = page * PAGESIZE;
        int end = page * PAGESIZE + PAGESIZE;
        for (int i = start; i < end; i++) {
            if (datalist.size() > i)
                list.add(datalist.get(i));
            else {
                break;
            }
        }
        Message msg=new Message();
        msg.arg1=1;
        handler.sendMessage(msg);
    }



    @Override
    public void onRefresh(int id) {
        page=0;
        list.clear();
        addData(0);
        if(datalist.size()>PAGESIZE) {
            listView.setPullLoadEnable(true);
        }
    }

    @Override
    public void onLoadMore(int id) {
        ++page;
        addData(page);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        list.clear();
        datalist.clear();
        MsgSql.db.close();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    public void onEvent(MyEvent event) {
        if ("REFRESHPUSH".equals(event.getMsg())) {
            LG.i("运行");
            getData();
            addData(page);
            adapter.notifyDataSetChanged();
        }
    }
}
