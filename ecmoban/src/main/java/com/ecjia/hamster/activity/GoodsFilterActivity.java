package com.ecjia.hamster.activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.consts.AppConst;
import com.ecjia.hamster.adapter.GoodsCategoryAdapter;
import com.ecjia.hamster.model.CATEGORY;
import com.ecjia.util.EventBus.MyEvent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;


public class GoodsFilterActivity extends BaseActivity {

    private TextView tv_filter_name;
    private LinearLayout ll_top_null,ll_top_back,ll_filter_all,ll_filter_out;
    private ImageView iv_filter_all;
    private String title;
    private String origintitle;
    private ListView listview;
    private SharedPreferences share;
    private ArrayList<CATEGORY> originlist;
    private ArrayList<CATEGORY> categories;
    private GoodsCategoryAdapter goodsCategoryAdapter;
    private int id,parentid,outid;
    private boolean fromlist=false;

    public GoodsFilterActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_goods_filter);

        EventBus.getDefault().register(this);

        origintitle=res.getString(R.string.filter_all);
        Intent intent =getIntent();
        title=intent.getStringExtra("title");
        id=intent.getIntExtra("id", 0);
        outid=intent.getIntExtra("outid", 0);
        fromlist=intent.getBooleanExtra("fromlist", false);

        share=getSharedPreferences("CATEGORY",0);
        String s=share.getString("data","");

        originlist=new ArrayList<CATEGORY>();
        categories=new ArrayList<CATEGORY>();

        if(TextUtils.isEmpty(title)){
            title=origintitle;
        }
        EventBus.getDefault().post(new MyEvent(title, AppConst.NEWTITLE));

        if (null != s && s.length() > 0)
        {
            try{
                JSONObject jo = new JSONObject(s);
                JSONArray contentArray = jo.optJSONArray("category");

                originlist.clear();
                if (null != contentArray && contentArray.length() > 0)
                {
                    for (int i = 0; i < contentArray.length(); i++)
                    {
                        JSONObject contentJsonObject = contentArray.getJSONObject(i);
                        CATEGORY Item = CATEGORY.fromJson(contentJsonObject);
                            originlist.add(Item);
                    }
                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        if(outid!=0){
            id=outid;
            int temp=0;
            for(int i = 0; i < originlist.size(); i++){
                if(id==originlist.get(i).getCat_id()){
                    temp=originlist.get(i).getParent_id();
                    break;
                }
            }

            if(temp==0){
                id=temp;
            }

            for(int i = 0; i < originlist.size(); i++) {
                if (temp == originlist.get(i).getParent_id()) {
                    categories.add(originlist.get(i));
                }
                if (temp == originlist.get(i).getCat_id()) {
                    parentid=originlist.get(i).getParent_id();
                }
            }
        }else{
            for(int i = 0; i < originlist.size(); i++){
                if(id==originlist.get(i).getParent_id()){
                    categories.add(originlist.get(i));
                }
                if(id==originlist.get(i).getCat_id()){
                    parentid=originlist.get(i).getParent_id();
                }
            }
        }

//        LG.e("====id===="+id);
//        LG.e("====parentid===="+parentid);
//        LG.e("---------------");

        initView();

    }

    private void initView() {
        tv_filter_name=(TextView)findViewById(R.id.tv_filter_name);
        iv_filter_all=(ImageView)findViewById(R.id.iv_filter_all);
        ll_top_null=(LinearLayout)findViewById(R.id.ll_top_null);
        ll_top_back=(LinearLayout)findViewById(R.id.ll_top_back);
        ll_filter_out=(LinearLayout)findViewById(R.id.ll_filter_out);
        ll_filter_all=(LinearLayout)findViewById(R.id.ll_filter_all);

        tv_filter_name.setText(title);
        String temp=title.trim();
        if(temp.length()>4){
            tv_filter_name.setTextColor(res.getColor(R.color.bg_theme_color));
            iv_filter_all.setImageResource(R.drawable.filter_all_active);
        }else{
            tv_filter_name.setTextColor(res.getColor(R.color.text_login_color));
            iv_filter_all.setImageResource(R.drawable.filter_all);
        }

        if(fromlist){
            ll_filter_all.setVisibility(View.VISIBLE);
        }

        listview=(ListView)findViewById(R.id.filter_list);

        goodsCategoryAdapter=new GoodsCategoryAdapter(categories,this);
        listview.setAdapter(goodsCategoryAdapter);

        goodsCategoryAdapter.setOnRightItemClickListener(new GoodsCategoryAdapter.onRightItemClickListener() {
            @Override
            public void onRightItemClick(View v, int position) {
                if(v.getId()==R.id.ll_right_filter){
                    String newtitle=title
                            +" > "+goodsCategoryAdapter.list.get(position).getCat_name();
                    if(fromlist){
                        EventBus.getDefault().post(new MyEvent("FILTERVIS"));
                        ll_filter_all.setVisibility(View.INVISIBLE);
                    }
                    Intent intent=new Intent(GoodsFilterActivity.this,GoodsFilterActivity.class);
                    intent.putExtra("title",newtitle);
                    intent.putExtra("id",goodsCategoryAdapter.list.get(position).getCat_id());
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.push_right_in,
                            R.anim.push_right_out);

                }else if(v.getId()==R.id.filter_name){
                    EventBus.getDefault().post(new MyEvent(goodsCategoryAdapter.list.get(position).getCat_name()+"==="+
                            goodsCategoryAdapter.list.get(position).getCat_id() , AppConst.CATEGORYT));
                    finish();
                    overridePendingTransition(R.anim.push_top_in, R.anim.push_top_out);
                }
            }
        });


        ll_top_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(parentid==0&&id==0){
                    EventBus.getDefault().post(new MyEvent("", AppConst.CATEGORYT));
                    finish();
                    overridePendingTransition(R.anim.push_top_in, R.anim.push_top_out);
                }else{
                    String newtitle=title;
                    String[]sp=newtitle.split(">");
                    newtitle="";
                    for (int i=0;i<sp.length-1;i++){
                        newtitle+=">"+sp[i];
                    }
                    if(!TextUtils.isEmpty(newtitle)){
                        newtitle=newtitle.substring(1,newtitle.length());
                    }
                    if(fromlist){
                        EventBus.getDefault().post(new MyEvent("FILTERVIS"));
                        ll_filter_all.setVisibility(View.INVISIBLE);
                    }
                    Intent intent=new Intent(GoodsFilterActivity.this,GoodsFilterActivity.class);
                    intent.putExtra("id",parentid);
                    intent.putExtra("title",newtitle);
                    startActivity(intent);
                    finish();
                }
            }
        });
        ll_top_null.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new MyEvent("", AppConst.CATEGORYT));
                finish();
                overridePendingTransition(R.anim.push_top_in, R.anim.push_top_out);
            }
        });
        ll_filter_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(id==0){
                    EventBus.getDefault().post(new MyEvent("===0", AppConst.CATEGORYT));
                    finish();
                    overridePendingTransition(R.anim.push_top_in, R.anim.push_top_out);
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(parentid==0&&id==0){
                EventBus.getDefault().post(new MyEvent("", AppConst.CATEGORYT));
                finish();
                overridePendingTransition(R.anim.push_top_in, R.anim.push_top_out);
            }else{
                String newtitle=title;
                String[]sp=newtitle.split(">");
                newtitle="";
                for (int i=0;i<sp.length-1;i++){
                    newtitle+=">"+sp[i];
                }
                if(!TextUtils.isEmpty(newtitle)){
                    newtitle=newtitle.substring(1,newtitle.length());
                }
                if(fromlist){
                    EventBus.getDefault().post(new MyEvent("FILTERVIS"));
                    ll_filter_all.setVisibility(View.INVISIBLE);
                }
                Intent intent=new Intent(GoodsFilterActivity.this,GoodsFilterActivity.class);
                intent.putExtra("id",parentid);
                intent.putExtra("title",newtitle);
                startActivity(intent);
                finish();
            }
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void onEvent(MyEvent event) {
    }

}
