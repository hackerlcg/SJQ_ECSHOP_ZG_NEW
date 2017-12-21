package com.ecjia.hamster.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.component.network.model.FavourModel;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.component.view.EditDialog;
import com.ecjia.component.view.MyDialog;
import com.ecjia.component.view.ToastView;
import com.ecjia.consts.AppConst;
import com.ecjia.hamster.model.BRAND;
import com.ecjia.hamster.model.CATEGORY;
import com.ecjia.hamster.model.GIFT;
import com.ecjia.hamster.model.GOODS;
import com.ecjia.hamster.model.HttpResponse;
import com.ecjia.hamster.model.STATUS;
import com.ecjia.hamster.model.USERRANK;
import com.ecjia.util.EventBus.MyEvent;
import com.ecjia.util.TimeUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

public class AddDiscountActivity extends BaseActivity implements View.OnClickListener,HttpResponse{
    private TextView tv_title;
    private ImageView back;
    private EditText et_discount_name;
    private TextView tv_start_time,tv_end_time;
    private TextView tv_level;
    private TextView tv_del_tips;
    private TextView tv_discount_setting,tv_option_goods;
    private TextView tv_choice_range,tv_choice_num;
    private LinearLayout ll_level,ll_discount_setting,ll_option_goods,ll_range_num;
    private Button btn_save,btn_del;
    private String rank_id;
    private int TYPE; //0,添加 1,编辑 2,结束状态编辑
    private FavourModel model;
    private EditDialog dialog;
    private ArrayList<String> options =new ArrayList<String>();
    private String act_name,start_time,end_time,user_rank,act_range_ext;
    private int act_range=0;  //（0：全部商品、1：指定分类、2：指定品牌、3：指定商品）
    private String min_amount,max_amount, act_type_ext,gift,brandstr,goodsstr,categorystr;
    private int act_type=-1;
    private int act_id=0;
    private ArrayList<GIFT> gifts=new ArrayList<GIFT>();
    private ArrayList<BRAND> brands=new ArrayList<BRAND>();
    private ArrayList<GOODS> goodses=new ArrayList<GOODS>();
    private ArrayList<CATEGORY> categories=new ArrayList<CATEGORY>();
    private MyDialog myDialog;
    private int giftnum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_add_discount);
        EventBus.getDefault().register(this);

        Intent intent=getIntent();
        TYPE=intent.getIntExtra("TYPE",0);


        model=new FavourModel(this);
        model.addResponseListener(this);

        options.add(res.getString(R.string.add_discount_all_goods));
        options.add(res.getString(R.string.add_discount_part_category));
        options.add(res.getString(R.string.add_discount_part_brand));
        options.add(res.getString(R.string.add_discount_part_goods));

        initView();

        if(TYPE==1||TYPE==2){
            btn_del.setVisibility(View.VISIBLE);
            act_id=intent.getIntExtra("act_id",0);
            model.getFavourInfo(act_id,api);
        }
    }

    private void initView() {
        tv_title= (TextView) findViewById(R.id.top_view_text);
        tv_title.setText(res.getString(R.string.magic_discount));
        back= (ImageView) findViewById(R.id.top_view_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backTips();
            }
        });

        et_discount_name= (EditText) findViewById(R.id.et_discount_name);
        tv_start_time= (TextView) findViewById(R.id.tv_start_time);
        tv_end_time= (TextView) findViewById(R.id.tv_end_time);
        tv_level= (TextView) findViewById(R.id.tv_level);
        tv_del_tips= (TextView) findViewById(R.id.tv_del_tips);
        tv_discount_setting= (TextView) findViewById(R.id.tv_discount_setting);
        tv_option_goods= (TextView) findViewById(R.id.tv_option_goods);
        tv_choice_range= (TextView) findViewById(R.id.tv_choice_range);
        tv_choice_num= (TextView) findViewById(R.id.tv_choice_num);

        if(TYPE==2){
            tv_del_tips.setVisibility(View.VISIBLE);
        }

        ll_level=(LinearLayout) findViewById(R.id.ll_level);
        ll_discount_setting=(LinearLayout) findViewById(R.id.ll_discount_setting);
        ll_option_goods=(LinearLayout) findViewById(R.id.ll_option_goods);
        ll_range_num=(LinearLayout) findViewById(R.id.ll_range_num);

        btn_save= (Button) findViewById(R.id.btn_discount_save);
        btn_del= (Button) findViewById(R.id.btn_discount_del);

        tv_start_time.setOnClickListener(this);
        tv_end_time.setOnClickListener(this);
        ll_level.setOnClickListener(this);
        ll_discount_setting.setOnClickListener(this);
//        ll_option_goods.setOnClickListener(this);
        ll_range_num.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        btn_del.setOnClickListener(this);
    }

    /**
     * Gift数据解析
     * @param type 0：array to str  1:str to array
     */
    private void parseGiftJson(int type){
        if(type==0){
            if(gifts.size()>0) {
                JSONObject requestJsonObject = new JSONObject();
                try {
                    JSONArray itemJSONArray1 = new JSONArray();
                    for (int i = 0; i < gifts.size(); i++) {
                        GIFT itemData = gifts.get(i);
                        JSONObject itemJSONObject = itemData.toJson();
                        itemJSONArray1.put(itemJSONObject);
                    }
                    requestJsonObject.put("gift", itemJSONArray1);
                } catch (JSONException e) {
                    // TODO: handle exception
                }
                gift=requestJsonObject.toString();
            }
        }else if(type==1){
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
        }

    }
    /**
     * Brand数据解析
     * @param type 0：array to str  1:str to array
     */
    private void parseBrandJson(int type){
        if(type==0){
            if(brands.size()>0) {
                JSONObject requestJsonObject = new JSONObject();
                try {
                    JSONArray itemJSONArray1 = new JSONArray();
                    for (int i = 0; i < brands.size(); i++) {
                        BRAND itemData = brands.get(i);
                        JSONObject itemJSONObject = itemData.toJson();
                        itemJSONArray1.put(itemJSONObject);
                    }
                    requestJsonObject.put("brands", itemJSONArray1);
                } catch (JSONException e) {
                    // TODO: handle exception
                }
                brandstr=requestJsonObject.toString();
            }
        }else if(type==1){
            if(!TextUtils.isEmpty(brandstr)){
                JSONObject jo=null;
                JSONArray listJsonArray = null;
                try {
                    jo=new JSONObject(brandstr);
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
        }

    }
    /**
     * Category数据解析
     * @param type 0：array to str  1:str to array
     */
    private void parseCategoryJson(int type){
        if(type==0){
            if(categories.size()>0) {
                JSONObject requestJsonObject = new JSONObject();
                try {
                    JSONArray itemJSONArray1 = new JSONArray();
                    for (int i = 0; i < categories.size(); i++) {
                        CATEGORY itemData = categories.get(i);
                        JSONObject itemJSONObject = itemData.toJson();
                        itemJSONArray1.put(itemJSONObject);
                    }
                    requestJsonObject.put("category", itemJSONArray1);
                } catch (JSONException e) {
                    // TODO: handle exception
                }
                categorystr=requestJsonObject.toString();
            }
        }else if(type==1){
            if(!TextUtils.isEmpty(categorystr)){
                JSONObject jo=null;
                JSONArray listJsonArray = null;
                try {
                    jo=new JSONObject(categorystr);
                    listJsonArray = jo.optJSONArray("category");
                    categories.clear();
                    if (null != listJsonArray && listJsonArray.length() > 0)
                    {
                        for (int i = 0; i < listJsonArray.length(); i++)
                        {
                            JSONObject listJsonObject = listJsonArray.getJSONObject(i);
                            CATEGORY item = CATEGORY.fromJson(listJsonObject);
                            categories.add(item);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

    }
    /**
     * Goods数据解析
     * @param type 0：array to str  1:str to array
     */
    private void parseGoodsJson(int type){
        if(type==0){
            if(goodses.size()>0) {
                JSONObject requestJsonObject = new JSONObject();
                try {
                    JSONArray itemJSONArray1 = new JSONArray();
                    for (int i = 0; i < goodses.size(); i++) {
                        GOODS itemData = goodses.get(i);
                        JSONObject itemJSONObject = itemData.toJson();
                        itemJSONArray1.put(itemJSONObject);
                    }
                    requestJsonObject.put("goods", itemJSONArray1);
                } catch (JSONException e) {
                    // TODO: handle exception
                }
                goodsstr=requestJsonObject.toString();
            }
        }else if(type==1){
            if(!TextUtils.isEmpty(goodsstr)){
                JSONObject jo=null;
                JSONArray listJsonArray = null;
                try {
                    jo=new JSONObject(goodsstr);
                    listJsonArray = jo.optJSONArray("goods");
                    goodses.clear();
                    if (null != listJsonArray && listJsonArray.length() > 0)
                    {
                        for (int i = 0; i < listJsonArray.length(); i++)
                        {
                            JSONObject listJsonObject = listJsonArray.getJSONObject(i);
                            GOODS item = GOODS.fromJson(listJsonObject);
                            goodses.add(item);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.tv_start_time :
            intent=new Intent(AddDiscountActivity.this,DateActivity.class);
            intent.putExtra("date", tv_start_time.getText().toString());
            intent.putExtra("code", AppConst.DISCOUNTS);
            startActivity(intent);
            overridePendingTransition(R.anim.push_share_in, R.anim.push_buttom_out);
            break;
            case R.id.tv_end_time :
            intent=new Intent(AddDiscountActivity.this,DateActivity.class);
            intent.putExtra("date", tv_start_time.getText().toString());
            intent.putExtra("code", AppConst.DISCOUNTE);
            startActivity(intent);
            overridePendingTransition(R.anim.push_share_in, R.anim.push_buttom_out);
            break;
            case R.id.ll_level:
                intent=new Intent(AddDiscountActivity.this,SelectRankActivity.class);
                String rank=tv_level.getText().toString();
                if(!TextUtils.isEmpty(rank)){
                    intent.putExtra("rank", tv_level.getText().toString());
                }
                startActivityForResult(intent,101);
            break;
            case R.id.ll_discount_setting:
                intent=new Intent(AddDiscountActivity.this,DiscountSettingActivity.class);
                intent.putExtra("act_type",act_type);
                if(act_type!=-1){
                    intent.putExtra("giftnum",giftnum);
                    intent.putExtra("gift",gift);
                    intent.putExtra("min_amount",min_amount);
                    intent.putExtra("max_amount",max_amount);
                    intent.putExtra("act_type_ext",act_type_ext);
                }
                startActivityForResult(intent,102);
            break;
            case R.id.ll_range_num:
                switch (act_range){
                    case 1:
                        intent=new Intent(AddDiscountActivity.this,SelectedCategoryActivity.class);
                        parseCategoryJson(0);
                        intent.putExtra("category",categorystr);
                        startActivity(intent);
                    break;
                    case 2:
                        intent=new Intent(AddDiscountActivity.this,SelectedBrandsActivity.class);
                        parseBrandJson(0);
                        intent.putExtra("brands",brandstr);
                        startActivity(intent);
                    break;
                    case 3:
                        intent=new Intent(AddDiscountActivity.this,SelectedGoodsActivity.class);
                        parseGoodsJson(0);
                        intent.putExtra("goods",goodsstr);
                        startActivity(intent);
                    break;
                }
            break;
            case R.id.ll_option_goods:
                dialog = new EditDialog(AddDiscountActivity.this,options);
                dialog.title.setText("请选择商品");

                dialog.lv_edit_dialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent;
                        switch (position){
                            case 0:
                                tv_option_goods.setText(options.get(position));
                                act_range=0;
                                act_range_ext="";
                                ll_range_num.setVisibility(View.GONE);
                                break;
                            case 1:
                                intent=new Intent(AddDiscountActivity.this,CategoryChooseActivity.class);
                                startActivityForResult(intent, 121);
                                break;
                            case 2:
                                intent=new Intent(AddDiscountActivity.this,BrandChooseActivity.class);
                                startActivityForResult(intent, 122);
                            break;
                            case 3:
                                intent=new Intent(AddDiscountActivity.this,GoodsChooseActivity.class);
                                startActivityForResult(intent, 123);
                            break;
                        }

                        dialog.dismiss();
                    }
                });
                dialog.cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            break;
            case R.id.btn_discount_save:
                act_name=et_discount_name.getText().toString();
                start_time=tv_start_time.getText().toString();
                end_time=tv_end_time.getText().toString();
                user_rank=rank_id;

                if(TextUtils.isEmpty(act_name)){
                    ToastView toast = new ToastView(AddDiscountActivity.this, res.getString(R.string.add_discount_name_toast));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else
                if(TextUtils.isEmpty(start_time)||TextUtils.isEmpty(end_time)){
                    ToastView toast = new ToastView(AddDiscountActivity.this, res.getString(R.string.add_discount_time_toast));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else
                if(TextUtils.isEmpty(user_rank)){
                    ToastView toast = new ToastView(AddDiscountActivity.this, res.getString(R.string.add_discount_rank_toast));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else
                if(act_type==-1){
                    ToastView toast = new ToastView(AddDiscountActivity.this, res.getString(R.string.add_discount_type_toast));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else{
                    if(act_type==0){
                        parseGiftJson(1);
                    }
                    if(TYPE==0){
                        model.addFavour(act_name,start_time,end_time,user_rank,act_range,act_range_ext,
                                min_amount,max_amount,act_type,act_type_ext,gifts,api);
                    }else if(TYPE==1||TYPE==2){
                        model.updateFavour(act_id,act_name,start_time,end_time,user_rank,act_range,act_range_ext,
                                min_amount,max_amount,act_type,act_type_ext,gifts,api);
                    }

                }

            break;
            case R.id.btn_discount_del:
                myDialog = new MyDialog(this, res.getString(R.string.tip), res.getString(R.string.tips_content_del2));
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
                        myDialog.dismiss();
                        model.delFavour(act_id,api);
                    }
                });

            break;
        }
    }

    private void backTips(){
        act_name=et_discount_name.getText().toString();
        start_time=tv_start_time.getText().toString();
        end_time=tv_end_time.getText().toString();
        user_rank=rank_id;
        if(!TextUtils.isEmpty(start_time)||!TextUtils.isEmpty(end_time)||
                !TextUtils.isEmpty(act_name)||!TextUtils.isEmpty(user_rank)||act_type!=-1
                ){
            String main_exit = res.getString(R.string.tip);
            String main_exit_content="";
            if(TYPE==0){
                main_exit_content = res.getString(R.string.add_discount_back_tips);
            }else if(TYPE==1||TYPE==2){
                main_exit_content = res.getString(R.string.edit_discount_back_tips);
            }
            myDialog = new MyDialog(this, main_exit, main_exit_content);
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
                    myDialog.dismiss();
                    finish();
                }
            });
        }else{
            finish();
        }

    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101&&resultCode==100){
            tv_level.setText(data.getStringExtra("rank"));
            rank_id=data.getStringExtra("rank_id");
        }else
        if(requestCode==102&&resultCode==100){
            act_type=data.getIntExtra("act_type",-1);
            if(act_type==-1){
                tv_discount_setting.setText("");
            }else{
                min_amount=data.getStringExtra("min_amount");
                max_amount=data.getStringExtra("max_amount");
                act_type_ext=data.getStringExtra("act_type_ext");
                giftnum=data.getIntExtra("giftnum", 0);
                gift=data.getStringExtra("gift");
                showActType();
            }

        }else
        if(requestCode==121&&resultCode==100) {
            act_range=1;

            ll_range_num.setVisibility(View.VISIBLE);
            tv_choice_range.setText(res.getString(R.string.selected_category));

            categorystr=data.getStringExtra("category");
            int num=data.getIntExtra("categorynum",0);
            tv_choice_num.setText("已选择"+num+"个分类");
            tv_option_goods.setText(res.getString(R.string.reset_discount_range));
            parseCategoryJson(1);

                StringBuffer temprange=new StringBuffer();
                for (int i=0;i<categories.size();i++){
                    temprange.append(","+categories.get(i).getCat_id());
                }
                if(!TextUtils.isEmpty(temprange.toString())){
                    temprange.deleteCharAt(0);
                }
                act_range_ext=temprange.toString();
        }else
        if(requestCode==122&&resultCode==100) {
            act_range=2;

            ll_range_num.setVisibility(View.VISIBLE);
            tv_choice_range.setText(res.getString(R.string.selected_brands));

            brandstr=data.getStringExtra("brands");
            int num=data.getIntExtra("brandsnum",0);
            tv_choice_num.setText("已选择"+num+"个品牌");
            tv_option_goods.setText(res.getString(R.string.reset_discount_range));
            parseBrandJson(1);

                StringBuffer temprange=new StringBuffer();
                for (int i=0;i<brands.size();i++){
                    temprange.append(","+brands.get(i).getBrand_id());
                }
                if(!TextUtils.isEmpty(temprange.toString())){
                    temprange.deleteCharAt(0);
                }
                act_range_ext=temprange.toString();

        }else
        if(requestCode==123&&resultCode==100) {
            act_range=3;

            ll_range_num.setVisibility(View.VISIBLE);
            tv_choice_range.setText(res.getString(R.string.selected_goods));

            goodsstr=data.getStringExtra("goods");
            int num=data.getIntExtra("goodsnum",0);
            tv_choice_num.setText("已选择"+num+"个商品");
            tv_option_goods.setText(res.getString(R.string.reset_discount_range));
            parseGoodsJson(1);

                StringBuffer temprange=new StringBuffer();
                for (int i=0;i<goodses.size();i++){
                    temprange.append(","+goodses.get(i).getId());
                }
                if(!TextUtils.isEmpty(temprange.toString())){
                    temprange.deleteCharAt(0);
                }
                act_range_ext=temprange.toString();


        }
    }

    private void showActType() {
        switch (act_type){
            case 0:
                tv_discount_setting.setText(res.getString(R.string.discount_setting_gift));
                break;
            case 1:
                tv_discount_setting.setText(res.getString(R.string.discount_setting_cash));
                break;
            case 2:
                tv_discount_setting.setText(res.getString(R.string.discount_setting_discount));
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            backTips();
            return true;
        }
        return true;
    }

    public void onEvent(MyEvent event) {
        if (AppConst.DISCOUNTS==event.getmTag()) {
            if(!TextUtils.isEmpty(tv_end_time.getText().toString())){
                int result = TimeUtil.compare_date(event.getMsg(), tv_end_time.getText().toString());
                if(result==1){
                    ToastView toast = new ToastView(AddDiscountActivity.this,res.getString(R.string.wrong_sdate));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.setDuration(200);
                    toast.show();
                }
                else{
                    tv_start_time.setText(event.getMsg());
                }
            }else{
                tv_start_time.setText(event.getMsg());
            }
        }else if(AppConst.DISCOUNTE==event.getmTag()) {
            if(!TextUtils.isEmpty(tv_start_time.getText().toString())){
                int result =TimeUtil.compare_date(tv_start_time.getText().toString(),event.getMsg());
                if(result==1){
                    ToastView toast = new ToastView(AddDiscountActivity.this,res.getString(R.string.wrong_edate));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.setDuration(200);
                    toast.show();
                }
                else{
                    tv_end_time.setText(event.getMsg());
                }
            }else{
                tv_end_time.setText(event.getMsg());
            }
        }

    }

    @Override
    public void OnMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if (url.equals(ProtocolConst.ADMIN_FAV_ADD)) {
            if (status.getSucceed() == 1) {
                ToastView toast = new ToastView(AddDiscountActivity.this, res.getString(R.string.add_discount_success));
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                setResult(100);
                finish();
            }else{
                ToastView toast = new ToastView(AddDiscountActivity.this, status.getError_desc());
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }else
        if (url.equals(ProtocolConst.ADMIN_FAV_UPDATE)) {
            if (status.getSucceed() == 1) {
                ToastView toast = new ToastView(AddDiscountActivity.this, res.getString(R.string.edit_discount_success));
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                setResult(100);
                finish();
            }else{
                ToastView toast = new ToastView(AddDiscountActivity.this, status.getError_desc());
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }else
        if (url.equals(ProtocolConst.ADMIN_FAV_DELETE)) {
            if (status.getSucceed() == 1) {
                ToastView toast = new ToastView(AddDiscountActivity.this, res.getString(R.string.del_discount_success));
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                setResult(100);
                finish();
            }else{
                ToastView toast = new ToastView(AddDiscountActivity.this, status.getError_desc());
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }else
        if (url.equals(ProtocolConst.ADMIN_FAV_INFO)) {
            if (status.getSucceed() == 1) {
                act_name=model.favour_info.getAct_name();
                start_time=model.favour_info.getFormatted_start_time();
                end_time=model.favour_info.getFormatted_end_time();
                min_amount=model.favour_info.getMin_amount();
                max_amount=model.favour_info.getMax_amount();
                if(!TextUtils.isEmpty(model.favour_info.getAct_type())){
                    act_type=Integer.parseInt(model.favour_info.getAct_type());
                }else{
                    act_type=-1;
                }
                act_type_ext=model.favour_info.getAct_type_ext();

                act_range=model.favour_info.getAct_range();

                tv_option_goods.setText(options.get(act_range));
                switch (act_range){
                    case 0:
                        act_range=0;
                        act_range_ext="";
                        ll_range_num.setVisibility(View.GONE);
                        break;
                    case 1:
                        act_range=1;
                        categories.clear();
                        categories.addAll(model.categories);
                        tv_choice_range.setText(res.getString(R.string.selected_category));
                        tv_option_goods.setText(res.getString(R.string.reset_discount_range));
                        tv_choice_num.setText("已选择"+categories.size()+"个分类");
                        StringBuffer temprange1=new StringBuffer();
                        for (int i=0;i<categories.size();i++){
                            temprange1.append(","+categories.get(i).getCat_id());
                        }
                        if(!TextUtils.isEmpty(temprange1.toString())){
                            temprange1.deleteCharAt(0);
                        }
                        act_range_ext=temprange1.toString();
                        ll_range_num.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        act_range=2;
                        brands.clear();
                        brands.addAll(model.brands);
                        tv_choice_range.setText(res.getString(R.string.selected_brands));
                        tv_option_goods.setText(res.getString(R.string.reset_discount_range));
                        tv_choice_num.setText("已选择"+brands.size()+"个品牌");
                        StringBuffer temprange2=new StringBuffer();
                        for (int i=0;i<brands.size();i++){
                            temprange2.append(","+brands.get(i).getBrand_id());
                        }
                        if(!TextUtils.isEmpty(temprange2.toString())){
                            temprange2.deleteCharAt(0);
                        }
                        act_range_ext=temprange2.toString();
                        ll_range_num.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        act_range=3;
                        goodses.clear();
                        goodses.addAll(model.goodses);
                        tv_choice_range.setText(res.getString(R.string.selected_goods));
                        tv_option_goods.setText(res.getString(R.string.reset_discount_range));
                        tv_choice_num.setText("已选择"+goodses.size()+"个商品");
                        StringBuffer temprange3=new StringBuffer();
                        for (int i=0;i<goodses.size();i++){
                            temprange3.append(","+goodses.get(i).getId());
                        }
                        if(!TextUtils.isEmpty(temprange3.toString())){
                            temprange3.deleteCharAt(0);
                        }
                        act_range_ext=temprange3.toString();
                        ll_range_num.setVisibility(View.VISIBLE);
                        break;
                }

                ArrayList<USERRANK> arrayList=model.favour_info.getUser_rank_list();
                StringBuffer ranks=new StringBuffer();
                StringBuffer rank_ids=new StringBuffer();
                for (int i=0;i<arrayList.size();i++){
                    if (arrayList.get(i).isChecked()){
                        ranks.append(","+arrayList.get(i).getRank_name());
                        rank_ids.append(","+arrayList.get(i).getRank_id());
                    }
                }
                if(!TextUtils.isEmpty(ranks.toString())&&!TextUtils.isEmpty(rank_ids.toString())){
                    ranks.deleteCharAt(0);
                    rank_ids.deleteCharAt(0);
                }
                rank_id=rank_ids.toString();

                showActType();

                if(act_type==0){
                    gifts=model.favour_info.getGift_items();
                    giftnum=gifts.size();
                    parseGiftJson(0);
                }


                et_discount_name.setText(act_name);
                et_discount_name.setSelection(act_name.length());
                tv_start_time.setText(start_time);
                tv_end_time.setText(end_time);
                tv_level.setText(ranks);


            }else{
                ToastView toast = new ToastView(AddDiscountActivity.this, status.getError_desc());
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                finish();
            }
        }
    }
}
