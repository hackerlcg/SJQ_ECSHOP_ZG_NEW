package com.ecjia.hamster.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.component.view.EditDialog;
import com.ecjia.component.view.ToastView;
import com.ecjia.util.EventBus.MyEvent;
import com.ecjia.util.FormatUtil;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

public class DiscountSettingActivity extends BaseActivity implements View.OnClickListener{
    private TextView tv_title;
    private ImageView back;
    private EditText et_one,et_two,et_three;
    private TextView tv_set_type,tv_tips_one,tv_three,tv_tips_two,tv_have_choose,tv_goods_num;
    private LinearLayout ll_set_type,ll_discount_body,ll_three,ll_gift_area,ll_add_goods,ll_goods_num;
    private Button btn_save;
    private int TYPE; //0,添加 1,编辑
    private EditDialog dialog;
    private ArrayList<String> options =new ArrayList<String>();
    private String min_amount,max_amount, act_type_ext,gift;
    private int act_type=-1;
    private int giftnum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_discount_setting);
        EventBus.getDefault().register(this);

        Intent intent=getIntent();
        TYPE=intent.getIntExtra("TYPE",0);

//        options.add(res.getString(R.string.discount_setting_gift));
        options.add(res.getString(R.string.discount_setting_cash));
        options.add(res.getString(R.string.discount_setting_discount));
        //（0：享受赠品、1：享受现金减免、2：享受价格折扣）

        initView();

        Intent data=getIntent();

        act_type= data.getIntExtra("act_type", -1);
        giftnum= data.getIntExtra("giftnum", 0);

        if(act_type==-1){
            tv_set_type.setText("");
        }else {

            min_amount = data.getStringExtra("min_amount");
            max_amount = data.getStringExtra("max_amount");
            act_type_ext = data.getStringExtra("act_type_ext");
            gift = data.getStringExtra("gift");
            switch (act_type) {
                case 0:
                    tv_set_type.setText(options.get(0));
                    break;
                case 1:
                    tv_set_type.setText(options.get(1));
                    break;
                case 2:
                    tv_set_type.setText(options.get(2));
                    break;
            }
        }

        refreshUI(0);
    }

    private void initView() {
        tv_title= (TextView) findViewById(R.id.top_view_text);
        tv_title.setText(res.getString(R.string.add_discount_setting));

        back= (ImageView) findViewById(R.id.top_view_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        et_one= (EditText) findViewById(R.id.et_one);
        et_two= (EditText) findViewById(R.id.et_two);
        et_three= (EditText) findViewById(R.id.et_three);

        tv_set_type= (TextView) findViewById(R.id.tv_set_type);
        tv_three= (TextView) findViewById(R.id.tv_three);
        tv_tips_two= (TextView) findViewById(R.id.tv_tips_two);
        tv_have_choose= (TextView) findViewById(R.id.tv_have_choose);
        tv_goods_num= (TextView) findViewById(R.id.tv_goods_num);

        ll_set_type=(LinearLayout) findViewById(R.id.ll_set_type);
        ll_discount_body=(LinearLayout) findViewById(R.id.ll_discount_body);
        ll_three=(LinearLayout) findViewById(R.id.ll_three);
        ll_gift_area=(LinearLayout) findViewById(R.id.ll_gift_area);
        ll_add_goods=(LinearLayout) findViewById(R.id.ll_add_goods);
        ll_goods_num=(LinearLayout) findViewById(R.id.ll_goods_num);

        btn_save= (Button) findViewById(R.id.btn_discount_setting_save);

        ll_set_type.setOnClickListener(this);
        ll_add_goods.setOnClickListener(this);
        ll_goods_num.setOnClickListener(this);
        btn_save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.ll_add_goods:
//                if(!TextUtils.isEmpty(gift)){
//                    ll_goods_num.setVisibility(View.GONE);
//                    tv_goods_num.setText("已选中0件商品");
//                    tv_have_choose.setText("");
//
//                }
                    intent =new Intent(DiscountSettingActivity.this,GiftChooseActivity.class);
                    startActivityForResult(intent,101);
            break;
            case R.id.ll_goods_num:
                intent=new Intent(DiscountSettingActivity.this,SelectedGiftsActivity.class);
                intent.putExtra("gift",gift);
                startActivity(intent);
            break;
            case R.id.ll_set_type:
                dialog = new EditDialog(DiscountSettingActivity.this,options);
                dialog.title.setText("请选择优惠方式");
                dialog.lv_edit_dialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if(!tv_set_type.getText().toString().equals(options.get(position))){
                            tv_set_type.setText(options.get(position));
                            refreshUI(1);
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
            case R.id.btn_discount_setting_save:
                min_amount=et_one.getText().toString();
                max_amount=et_two.getText().toString();
                act_type_ext=et_three.getText().toString();

                if(act_type==-1){
                    ToastView toast = new ToastView(DiscountSettingActivity.this, res.getString(R.string.discount_setting_toast1));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else{
                    if(TextUtils.isEmpty(min_amount)||TextUtils.isEmpty(max_amount)||TextUtils.isEmpty(act_type_ext)){

                        ToastView toast = new ToastView(DiscountSettingActivity.this, res.getString(R.string.discount_setting_toast2));
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }else if(act_type==0&&TextUtils.isEmpty(gift)) {
                        ToastView toast = new ToastView(DiscountSettingActivity.this, res.getString(R.string.discount_setting_toast3));
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }else if(FormatUtil.formatStrToFloat(min_amount)>=FormatUtil.formatStrToFloat(max_amount)) {
                        ToastView toast = new ToastView(DiscountSettingActivity.this, res.getString(R.string.discount_setting_toast4));
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                    }else {
                        intent = new Intent();
                        intent.putExtra("act_type", act_type);
                        intent.putExtra("gift", gift);
                        intent.putExtra("min_amount", min_amount);
                        intent.putExtra("max_amount", max_amount);
                        intent.putExtra("giftnum", giftnum);
                        intent.putExtra("act_type_ext", act_type_ext);
                        setResult(100, intent);
                        finish();
                    }

                }

            break;
        }
    }

    private void refreshUI(int type) {
        if(res.getString(R.string.discount_setting_gift).equals(tv_set_type.getText().toString())){
            ll_discount_body.setVisibility(View.VISIBLE);
//            ll_three.setVisibility(View.GONE);
//            tv_tips_two.setVisibility(View.GONE);
            tv_tips_two.setText(res.getString(R.string.discount_setting_tips3));
            tv_three.setText(res.getString(R.string.gift_num));
            ll_gift_area.setVisibility(View.VISIBLE);
            et_one.setText(min_amount);
            if(!TextUtils.isEmpty(min_amount)){
                et_one.setSelection(min_amount.length());
            }
            et_two.setText(max_amount);
            if(!TextUtils.isEmpty(max_amount)) {
                et_two.setSelection(max_amount.length());
            }
            et_three.setInputType(InputType.TYPE_CLASS_NUMBER);

            if(type==0) {
                et_three.setText(act_type_ext);
                if (!TextUtils.isEmpty(act_type_ext)) {
                    et_three.setSelection(act_type_ext.length());
                }
            }else{
                et_three.setText("");
            }
            act_type=0;

            if(giftnum>0){
                ll_goods_num.setVisibility(View.VISIBLE);
                tv_goods_num.setText("已选中"+giftnum+"件商品");
                tv_have_choose.setText(res.getString(R.string.gift_reset));
            }
        }else if(res.getString(R.string.discount_setting_cash).equals(tv_set_type.getText().toString())){
            ll_discount_body.setVisibility(View.VISIBLE);
//            ll_three.setVisibility(View.VISIBLE);
//            tv_tips_two.setVisibility(View.GONE);
            tv_tips_two.setText("");
            tv_three.setText(res.getString(R.string.cut_yuan));
            et_three.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            ll_gift_area.setVisibility(View.GONE);
            et_one.setText(min_amount);
            if(!TextUtils.isEmpty(min_amount)){
                et_one.setSelection(min_amount.length());
            }
            et_two.setText(max_amount);
            if(!TextUtils.isEmpty(max_amount)) {
                et_two.setSelection(max_amount.length());
            }
            if(type==0){
                et_three.setText(act_type_ext);
                if(!TextUtils.isEmpty(act_type_ext)) {
                    et_three.setSelection(act_type_ext.length());
                }
            }else{
                et_three.setText("");
            }

            act_type=1;
        }else if(res.getString(R.string.discount_setting_discount).equals(tv_set_type.getText().toString())){
            ll_discount_body.setVisibility(View.VISIBLE);
//            ll_three.setVisibility(View.VISIBLE);
            tv_three.setText(res.getString(R.string.discount3));
            et_three.setInputType(InputType.TYPE_CLASS_NUMBER);
            ll_gift_area.setVisibility(View.GONE);
            tv_tips_two.setText(res.getString(R.string.discount_setting_tips2));
            et_one.setText(min_amount);
            if(!TextUtils.isEmpty(min_amount)){
                et_one.setSelection(min_amount.length());
            }
            et_two.setText(max_amount);
            if(!TextUtils.isEmpty(max_amount)) {
                et_two.setSelection(max_amount.length());
            }
            if(type==0) {
                et_three.setText(act_type_ext);
                if (!TextUtils.isEmpty(act_type_ext)) {
                    et_three.setSelection(act_type_ext.length());
                }
            }else{
                et_three.setText("");
            }
            act_type=2;
        }else{
            ll_discount_body.setVisibility(View.GONE);
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
            gift=data.getStringExtra("gift");
            giftnum=data.getIntExtra("giftnum",0);

            if(giftnum>0){
                ll_goods_num.setVisibility(View.VISIBLE);
                tv_goods_num.setText("已选中"+giftnum+"件商品");
                tv_have_choose.setText(res.getString(R.string.gift_reset));
            }else{
                ll_goods_num.setVisibility(View.GONE);
                tv_goods_num.setText("已选中0件商品");
                tv_have_choose.setText("");
            }

        }
    }

    public void onEvent(MyEvent event) {

    }
}
