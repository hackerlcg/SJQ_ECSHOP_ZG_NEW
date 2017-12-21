package com.ecjia.hamster.activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.component.network.model.GoodsDetailModel;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.component.view.CYTextView;
import com.ecjia.component.view.MyDialog;
import com.ecjia.component.view.ToastView;
import com.ecjia.consts.AppConst;
import com.ecjia.hamster.adapter.GoodsBonusAdapter;
import com.ecjia.hamster.adapter.RankAdapter;
import com.ecjia.hamster.model.GOODS;
import com.ecjia.hamster.model.HttpResponse;
import com.ecjia.hamster.model.SESSION;
import com.ecjia.hamster.model.STATUS;
import com.ecjia.hamster.model.USERRANK;
import com.ecjia.hamster.model.VOLUME;
import com.ecjia.util.EventBus.MyEvent;
import com.ecjia.util.TimeUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import de.greenrobot.event.EventBus;

public class PriceEditActivity extends BaseActivity implements HttpResponse {
	private TextView title;
	private ImageView back;
    private SharedPreferences shared;
    private String uid;
    private String sid;
    private String api;
    private SESSION session;
    private LinearLayout ll_vip_price,ll_vip_bottom,ll_bonus_price,ll_promote_bottom,ll_points,ll_points_bottom,ll_add_bonus,ll_bonus_bottom;
    private View line_vip_price,line_promote_price,line_about_points,line_bouns_price;
    private TextView cal_in_marketprice,format_price,tv_promote_stime,tv_promote_etime;
    private EditText et_shop_price,et_market_price,et_promote_price,et_consume_points,et_level_points,et_points_limit;
    private ImageView iv_vip_price,iv_bonus_price,iv_points;
    private CheckBox cb_lock;
    private Button btn_confirm;
    private CYTextView cy_tv;
    private ListView lv_bonus,lv_vip;
    private GoodsBonusAdapter goodsBonusAdapter;
    private RankAdapter rankAdapter;
    private MyDialog myDialog;
    private Calendar mycalendar;
    private ArrayList<VOLUME> volumes;
    private ArrayList<USERRANK> userranks;
    private ArrayList<VOLUME> finvolume;
    private ArrayList<USERRANK> finrank;
    private GOODS goods;
    private GoodsDetailModel dataModel;
    private boolean confirmflag;
    private MyDialog myDialog2;
    private String id,shop_price,market_price,promote_price,promote_start_date
            ,promote_end_date,give_integral,rank_integral,integral;
    private int confirm1,confirm2;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_priceedit);
		Intent intent = getIntent();
        shared = this.getSharedPreferences("userInfo", 0);
        uid = shared.getString("uid", "");
        sid = shared.getString("sid", "");
        api = shared.getString("shopapi", "");

        session=new SESSION();
        session.setUid(uid);
        session.setSid(sid);

        EventBus.getDefault().register(this);

        volumes=new ArrayList<VOLUME>();
        userranks=new ArrayList<USERRANK>();

        String s = intent.getStringExtra("data");

        if (null != s && s.length() > 0)
        {
            try{
                JSONObject jo = new JSONObject(s);
                goods = GOODS.fromJson(jo);
                volumes.addAll(goods.getVolume_number());
                userranks.addAll(goods.getUser_rank());

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

		title = (TextView) findViewById(R.id.top_view_text);
        String det=res.getString(R.string.price_edit_title);
		title.setText(det);
		
		back = (ImageView) findViewById(R.id.top_view_back);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

        if(null==dataModel){
            dataModel=new GoodsDetailModel(this);
            dataModel.addResponseListener(this);
        }

        initView();
        initListener();

	}

    private void initListener() {
        ll_vip_price.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ll_vip_bottom.getVisibility()==View.GONE){
                    ll_vip_bottom.setVisibility(View.VISIBLE);
                    line_vip_price.setVisibility(View.GONE);
                    iv_vip_price.setImageResource(R.drawable.edit_item_up);
                }else{
                    ll_vip_bottom.setVisibility(View.GONE);
                    line_vip_price.setVisibility(View.VISIBLE);
                    iv_vip_price.setImageResource(R.drawable.edit_item_down);
                }
            }
        });

        ll_points.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ll_points_bottom.getVisibility()==View.GONE){
                    ll_points_bottom.setVisibility(View.VISIBLE);
                    line_about_points.setVisibility(View.GONE);
                    iv_points.setImageResource(R.drawable.edit_item_up);
//                    et_consume_points.requestFocus();
                }else{
                    ll_points_bottom.setVisibility(View.GONE);
                    line_about_points.setVisibility(View.VISIBLE);
                    iv_points.setImageResource(R.drawable.edit_item_down);
                }
            }
        });

        ll_bonus_price.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ll_bonus_bottom.getVisibility()==View.GONE){
                    ll_bonus_bottom.setVisibility(View.VISIBLE);
                    line_bouns_price.setVisibility(View.GONE);
                    iv_bonus_price.setImageResource(R.drawable.edit_item_up);
                }else{
                    ll_bonus_bottom.setVisibility(View.GONE);
                    line_bouns_price.setVisibility(View.VISIBLE);
                    iv_bonus_price.setImageResource(R.drawable.edit_item_down);
                }
            }
        });

        cb_lock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ll_promote_bottom.setVisibility(View.VISIBLE);
                    line_promote_price.setVisibility(View.GONE);
                }else{
                    ll_promote_bottom.setVisibility(View.GONE);
                    line_promote_price.setVisibility(View.VISIBLE);
                }
            }
        });

        ll_add_bonus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                VOLUME volume=new VOLUME();
                volumes.add(volume);
                goodsBonusAdapter.notifyDataSetChanged();
                setListViewHeightBasedOnChildren(lv_bonus);
            }
        });

        goodsBonusAdapter.setOnRightItemClickListener(new GoodsBonusAdapter.onRightItemClickListener() {
            @Override
            public void onRightItemClick(View v,final int position) {
                if (v.getId() == R.id.iv_delete) {
                    String tips = res.getString(R.string.tip);
                    String tips_content = res.getString(R.string.tips_content_del);
                    myDialog = new MyDialog(PriceEditActivity.this, tips, tips_content);
                    myDialog.show();
                    myDialog.negative.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            myDialog.dismiss();

                        }
                    });
                    myDialog.positive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            volumes.remove(volumes.get(position));
                            goodsBonusAdapter.notifyDataSetChanged();
                            setListViewHeightBasedOnChildren(lv_bonus);
                            myDialog.dismiss();
                        }
                    });

                }
            }
        });


        tv_promote_stime.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                et_promote_price.requestFocus();
                Intent intent=new Intent(PriceEditActivity.this,DateActivity.class);
                intent.putExtra("date", tv_promote_stime.getText().toString());
                intent.putExtra("code", AppConst.DISCOUNTS);
                startActivity(intent);
                overridePendingTransition(R.anim.push_share_in, R.anim.push_buttom_out);
            }
        });
        tv_promote_etime.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                et_promote_price.requestFocus();
                Intent intent=new Intent(PriceEditActivity.this,DateActivity.class);
                intent.putExtra("date", tv_promote_etime.getText().toString());
                intent.putExtra("code", AppConst.DISCOUNTE);
                startActivity(intent);
                overridePendingTransition(R.anim.push_share_in, R.anim.push_buttom_out);
            }
        });

        cal_in_marketprice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String s=et_shop_price.getText().toString();
                if(!TextUtils.isEmpty(s)){
                    double i=1.2*Double.parseDouble(s);
                    et_market_price.setText(i+"");
                }

            }
        });

        format_price.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String s=et_market_price.getText().toString();
                if(!TextUtils.isEmpty(s)){
                    int i=(int)Double.parseDouble(s);
                    et_market_price.setText(i+"");
                }
            }
        });

        btn_confirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                id=goods.getId();
                shop_price=et_shop_price.getText().toString();
                market_price=et_market_price.getText().toString();
                promote_price=et_promote_price.getText().toString();
                promote_start_date=tv_promote_stime.getText().toString();
                promote_end_date=tv_promote_etime.getText().toString();
                give_integral=et_consume_points.getText().toString();
                rank_integral=et_level_points.getText().toString();
                integral=et_points_limit.getText().toString();


                finrank=new ArrayList<USERRANK>();
                finrank.addAll(userranks);
                for(int i=0;i<finrank.size();i++){
                    if(finrank.get(i).getRank_id()==0){
                        finrank.remove(i);
                    }
                }

                finvolume=new ArrayList<VOLUME>();
                finvolume.addAll(volumes);
                for(int i=0;i<volumes.size();i++){
                    if(TextUtils.isEmpty(volumes.get(i).getPrice())||TextUtils.isEmpty(volumes.get(i).getNumber())){
                        finvolume.remove(i);
                    }
                }

                boolean volumeflag=false;

                if(finvolume.size()>1){
                    int j=0;
                    for(int i=0;i<finvolume.size();i++){
                        for(int k=0;k<finvolume.size();k++){
                            if(k!=i) {
                                if (finvolume.get(i).getNumber().equals(finvolume.get(k).getNumber())) {
                                    j += 1;
                                }
                                if (j >= 2) {
                                    volumeflag = true;
                                    break;
                                }
                            }
                        }
                    }
                }

                if (!cb_lock.isChecked()){
                    promote_price="";
                    promote_start_date="";
                    promote_end_date="";
                }

                confirm1=1;
                confirm2=1;

                if(volumeflag){
                    ToastView toast = new ToastView(PriceEditActivity.this,res.getString(R.string.wrong_volume_num));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.setDuration(200);
                    toast.show();
                }
                else {
                    if (cb_lock.isChecked() &&
                            (TextUtils.isEmpty(promote_price) || TextUtils.isEmpty(promote_start_date) || TextUtils.isEmpty(promote_end_date))) {
                        confirmflag = true;
                    } else if (cb_lock.isChecked()&&!TextUtils.isEmpty(promote_price)&&!TextUtils.isEmpty(promote_start_date)&&!TextUtils.isEmpty(promote_end_date)){
                        if(Double.parseDouble(promote_price)==0){
                            ToastView toast = new ToastView(PriceEditActivity.this,res.getString(R.string.wrong_promote2));
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.setDuration(200);
                            toast.show();
                            confirm2=0;
                            }
                        confirmflag = false;
                    } else {
                        confirmflag = false;
                    }


                    if (TextUtils.isEmpty(shop_price) || TextUtils.isEmpty(market_price)
                            || TextUtils.isEmpty(give_integral) || TextUtils.isEmpty(rank_integral) || TextUtils.isEmpty(integral)
                            ) {
                        confirm1 = 0;
                        String tips = res.getString(R.string.tip);
                        String tips_content = res.getString(R.string.wrong_null);
                        myDialog2 = new MyDialog(PriceEditActivity.this, tips, tips_content);
                        myDialog2.show();
                        myDialog2.negative.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                myDialog2.dismiss();

                            }
                        });
                        myDialog2.positive.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                confirm1 += 1;
                                confirm();
                                myDialog2.dismiss();
                            }
                        });
                    }

                    if (confirmflag) {
                        confirm2 = 0;
                        String tips = res.getString(R.string.tip);
                        String tips_content = res.getString(R.string.wrong_promote);
                        myDialog = new MyDialog(PriceEditActivity.this, tips, tips_content);
                        myDialog.show();
                        myDialog.negative.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                myDialog.dismiss();
                                if(confirm1 == 0){
                                    myDialog2.dismiss();
                                }

                            }
                        });
                        myDialog.positive.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                confirm2 += 1;
                                promote_price="";
                                promote_start_date="";
                                promote_end_date="";
                                cb_lock.setChecked(false);
                                ll_promote_bottom.setVisibility(View.GONE);
                                line_promote_price.setVisibility(View.VISIBLE);
                                confirm();
                                myDialog.dismiss();
                            }


                        });

                    }

                    confirm();

                }
            }

            private void confirm() {
                if(confirm1>0&&confirm2>0){
                    dataModel.updatePrice(session,id,shop_price,market_price,promote_price,promote_start_date
                            ,promote_end_date,give_integral,rank_integral,integral,finrank,finvolume,api);
                }
            }
        });


    }

    private void initView() {
        ll_vip_price=(LinearLayout)findViewById(R.id.ll_vip_price);
        ll_vip_bottom=(LinearLayout)findViewById(R.id.ll_vip_bottom);
        ll_bonus_price=(LinearLayout)findViewById(R.id.ll_bonus_price);
        ll_bonus_bottom=(LinearLayout)findViewById(R.id.ll_bonus_bottom);
        ll_promote_bottom=(LinearLayout)findViewById(R.id.ll_promote_bottom);
        ll_points=(LinearLayout)findViewById(R.id.ll_points);
        ll_points_bottom=(LinearLayout)findViewById(R.id.ll_points_bottom);
        ll_add_bonus=(LinearLayout)findViewById(R.id.ll_add_bonus);

        line_vip_price=(View)findViewById(R.id.line_vip_price);
        line_promote_price=(View)findViewById(R.id.line_promote_price);
        line_about_points=(View)findViewById(R.id.line_about_points);
        line_bouns_price=(View)findViewById(R.id.line_bouns_price);

        cal_in_marketprice=(TextView)findViewById(R.id.cal_in_marketprice);
        format_price=(TextView)findViewById(R.id.format_price);
        cy_tv=(CYTextView)findViewById(R.id.cy_tv);
        cy_tv.SetText(res.getString(R.string.vip_price_tips));

        et_shop_price=(EditText)findViewById(R.id.et_shop_price);
        et_shop_price.setText(goods.getPrice());

        et_market_price=(EditText)findViewById(R.id.et_market_price);
        et_market_price.setText(goods.getUnformatted_market_price());

        lv_vip=(ListView)findViewById(R.id.vip_price_listView);
        rankAdapter=new RankAdapter(userranks,this);
        lv_vip.setAdapter(rankAdapter);

        setListViewHeightBasedOnChildren(lv_vip);

        if(volumes.size()==0){
            VOLUME volume=new VOLUME();
            volumes.add(volume);
        }

        et_promote_price=(EditText)findViewById(R.id.et_promote_price);
        tv_promote_stime=(TextView)findViewById(R.id.tv_promote_stime);
        tv_promote_etime=(TextView)findViewById(R.id.tv_promote_etime);
        cb_lock=(CheckBox)findViewById(R.id.cb_lock);

        if("false".equals(goods.getIs_promote())){
            cb_lock.setChecked(false);
            ll_promote_bottom.setVisibility(View.GONE);
            line_promote_price.setVisibility(View.VISIBLE);
        }else{
            cb_lock.setChecked(true);
            ll_promote_bottom.setVisibility(View.VISIBLE);
            line_promote_price.setVisibility(View.GONE);
            et_promote_price.setText(goods.getUnformatted_promote_price());
            tv_promote_stime.setText(TimeUtil.fomartPromoteDate(goods.getPromote_start_date()));
            tv_promote_etime.setText(TimeUtil.fomartPromoteDate(goods.getPromote_end_date()));
        }

        if(userranks.size()>0){
            if(!"-1".equals(userranks.get(0).getPrice())){
                ll_vip_bottom.setVisibility(View.VISIBLE);
                line_vip_price.setVisibility(View.GONE);
            }
        }

            if(!TextUtils.isEmpty(volumes.get(0).getNumber())){
                ll_bonus_bottom.setVisibility(View.VISIBLE);
                line_bouns_price.setVisibility(View.GONE);
            }

        if(!TextUtils.isEmpty(goods.getGive_integral())||!TextUtils.isEmpty(goods.getRank_integral())||!TextUtils.isEmpty(goods.getIntegral())){
            if(!"-1".equals(goods.getGive_integral())||!"-1".equals(goods.getRank_integral())||!"-1".equals(goods.getIntegral())) {
                ll_points_bottom.setVisibility(View.VISIBLE);
                line_about_points.setVisibility(View.GONE);
            }
        }


        et_consume_points=(EditText)findViewById(R.id.et_consume_points);
        et_level_points=(EditText)findViewById(R.id.et_level_points);
        et_points_limit=(EditText)findViewById(R.id.et_points_limit);

        et_consume_points.setText(goods.getGive_integral());
        et_level_points.setText(goods.getRank_integral());
        et_points_limit.setText(goods.getIntegral());

        iv_vip_price=(ImageView)findViewById(R.id.iv_vip_price);
        iv_bonus_price=(ImageView)findViewById(R.id.iv_bonus_price);
        iv_points=(ImageView)findViewById(R.id.iv_points);

        btn_confirm=(Button)findViewById(R.id.btn_confirm);

        lv_bonus=(ListView)findViewById(R.id.lv_bonus);

        goodsBonusAdapter=new GoodsBonusAdapter(volumes,this);
        lv_bonus.setAdapter(goodsBonusAdapter);

        setListViewHeightBasedOnChildren(lv_bonus);
    }


    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }


    @Override
    public void OnMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if (url .equals(ProtocolConst.UPDATEPRICE)) {
            if(status.getSucceed()==1){
                EventBus.getDefault().post(new MyEvent("PRICEREFRESH"));
                finish();
            }else{
                ToastView toast = new ToastView(PriceEditActivity.this,res.getString(R.string.edit_price_failed));
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.setDuration(200);
                toast.show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void onEvent(MyEvent event) {
        if (AppConst.DISCOUNTS==event.getmTag()) {
            if(!TextUtils.isEmpty(tv_promote_etime.getText().toString())){
                int result =TimeUtil.compare_date(event.getMsg(),tv_promote_etime.getText().toString());
                if(result==1){
                    ToastView toast = new ToastView(PriceEditActivity.this,res.getString(R.string.wrong_sdate));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.setDuration(200);
                    toast.show();
                }
                else{
                    tv_promote_stime.setText(event.getMsg());
                }
            }else{
                tv_promote_stime.setText(event.getMsg());
            }
        }else if(AppConst.DISCOUNTE==event.getmTag()) {
            if(!TextUtils.isEmpty(tv_promote_stime.getText().toString())){
                int result =TimeUtil.compare_date(tv_promote_stime.getText().toString(),event.getMsg());
                if(result==1){
                    ToastView toast = new ToastView(PriceEditActivity.this,res.getString(R.string.wrong_edate));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.setDuration(200);
                    toast.show();
                }
                else{
                    tv_promote_etime.setText(event.getMsg());
                }
            }else{
                tv_promote_etime.setText(event.getMsg());
            }
        }

    }
}
