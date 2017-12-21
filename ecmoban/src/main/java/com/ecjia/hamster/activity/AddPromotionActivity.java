package com.ecjia.hamster.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.component.network.model.PromotionModel;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.component.view.EditDialog;
import com.ecjia.component.view.MyDialog;
import com.ecjia.component.view.ToastView;
import com.ecjia.consts.AppConst;
import com.ecjia.hamster.model.HttpResponse;
import com.ecjia.hamster.model.STATUS;
import com.ecjia.util.EventBus.MyEvent;
import com.ecjia.util.FormatUtil;
import com.ecjia.util.TimeUtil;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

public class AddPromotionActivity extends BaseActivity implements View.OnClickListener,HttpResponse{
    private TextView tv_title;
    private ImageView back;
    private EditText et_promotion_price;
    private TextView tv_start_time,tv_end_time;
    private TextView tv_promotion_name,tv_promotion_name_hint;
    private TextView add_promotion_tips;
    private Button btn_save,btn_del;
    private int TYPE; //0,添加 1,编辑 2,结束状态编辑
    private PromotionModel model;
    private EditDialog dialog;
    private String start_time,end_time;
    private MyDialog myDialog;
    private String goods_id;
    private String goods_name;
    private String goods_price;
    private LinearLayout ll_promote_shop_price;
    private LinearLayout ll_search,topview,searchlayout_in;
    private View searchlayout_bg;
    private FrameLayout searchLayout;
    private TextView tv_promotion_search;
    private View ll_search_bottom;
    private String goods_shop_price;
    private TextView tv_promotion_shop_price;
    private View tips_view;
    private LinearLayout ll_root_promotion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_add_promotion);
        EventBus.getDefault().register(this);

        Intent intent=getIntent();
        TYPE=intent.getIntExtra("TYPE",0);


        model=new PromotionModel(this);
        model.addResponseListener(this);

        initView();

        if(TYPE==1||TYPE==2){
            btn_del.setVisibility(View.VISIBLE);
            goods_id=intent.getStringExtra("goods_id");
            model.getPromotionDetail(goods_id, api);
        }
    }

    private void initView() {
        tv_title= (TextView) findViewById(R.id.top_view_text);


        back= (ImageView) findViewById(R.id.top_view_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backTips();
            }
        });

        tv_start_time= (TextView) findViewById(R.id.tv_start_time);
        tv_end_time= (TextView) findViewById(R.id.tv_end_time);
        tv_promotion_name= (TextView) findViewById(R.id.tv_promotion_name);
        tv_promotion_name_hint= (TextView) findViewById(R.id.tv_promotion_name_hint);
        tv_promotion_shop_price= (TextView) findViewById(R.id.tv_promotion_shop_price);
        add_promotion_tips= (TextView) findViewById(R.id.add_promotion_tips);
        tips_view= (View) findViewById(R.id.tips_view);
        et_promotion_price= (EditText) findViewById(R.id.et_promotion_price);

        ll_root_promotion = (LinearLayout) findViewById(R.id.ll_root_promotion);

        ll_promote_shop_price = (LinearLayout) findViewById(R.id.ll_promote_shop_price);
        ll_search = (LinearLayout) findViewById(R.id.ll_search);
        searchlayout_bg = (View)findViewById(R.id.fragment_promotion_searchlayout_bg);
        tv_promotion_search = (TextView)findViewById(R.id.tv_promotion_search);
        searchlayout_in = (LinearLayout) findViewById(R.id.fragment_promotion_searchlayout_in);
        topview = (LinearLayout) findViewById(R.id.promotion_search_topview);
        searchLayout = (FrameLayout) findViewById(R.id.fragment_promotion_searchlayout);

        btn_save= (Button) findViewById(R.id.btn_promotion_save);
        btn_del= (Button) findViewById(R.id.btn_promotion_del);

        if(TYPE==1||TYPE==2){
            tv_title.setText(res.getString(R.string.edit_promotion_goods));
            btn_del.setVisibility(View.VISIBLE);
            searchLayout.setVisibility(View.GONE);
            add_promotion_tips.setVisibility(View.GONE);
            tips_view.setVisibility(View.VISIBLE);
            if(TYPE==2){
//            tv_del_tips.setVisibility(View.VISIBLE);
            }
        }else{
            tv_title.setText(res.getString(R.string.add_promotion_goods));
            btn_del.setVisibility(View.GONE);

            searchLayout.setVisibility(View.VISIBLE);
            add_promotion_tips.setVisibility(View.VISIBLE);
        }

        tv_start_time.setOnClickListener(this);
        tv_promotion_name_hint.setOnClickListener(this);
        tv_end_time.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        btn_del.setOnClickListener(this);
        searchLayout.setOnClickListener(this);

        float margin=res.getDimension(R.dimen.dim30);

        controlKeyboardLayout(ll_root_promotion,et_promotion_price,margin);
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.tv_start_time :
            intent=new Intent(AddPromotionActivity.this,DateActivity.class);
            intent.putExtra("date", tv_start_time.getText().toString());
            intent.putExtra("code", AppConst.DISCOUNTS);
            startActivity(intent);
            overridePendingTransition(R.anim.push_share_in, R.anim.push_buttom_out);
            break;
            case R.id.tv_end_time :
            intent=new Intent(AddPromotionActivity.this,DateActivity.class);
            intent.putExtra("date", tv_start_time.getText().toString());
            intent.putExtra("code", AppConst.DISCOUNTE);
            startActivity(intent);
            overridePendingTransition(R.anim.push_share_in, R.anim.push_buttom_out);
            break;
            case R.id.btn_promotion_save:
                goods_name=tv_promotion_name.getText().toString();
                goods_price=et_promotion_price.getText().toString();
                start_time=tv_start_time.getText().toString();
                end_time=tv_end_time.getText().toString();

                goods_price= FormatUtil.formatToDigetPrice(goods_price);

                if(TextUtils.isEmpty(goods_name)){
                    ToastView toast = new ToastView(AddPromotionActivity.this, res.getString(R.string.add_discount_name_toast));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else
                if(TextUtils.isEmpty(start_time)||TextUtils.isEmpty(end_time)){
                    ToastView toast = new ToastView(AddPromotionActivity.this, res.getString(R.string.add_discount_time_toast));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else
                if(TextUtils.isEmpty(goods_price)){
                    ToastView toast = new ToastView(AddPromotionActivity.this, res.getString(R.string.add_discount_rank_toast));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else{
                    if(TYPE==0){
                        model.addPromotion(goods_id, start_time, end_time, goods_price, api);
                    }else if(TYPE==1||TYPE==2){
                        model.updatePromotion(goods_id, start_time, end_time, goods_price, api);
                    }

                }

                break;
            case R.id.btn_promotion_del:
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
                        model.deletePromotion(goods_id, api);
                    }
                });

            break;
            case  R.id.tv_promotion_name_hint:
                searchAnima();
            break;
            case  R.id.fragment_promotion_searchlayout:
                searchAnima();
            break;
        }
    }

    private void searchAnima() {
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, -topview.getHeight());
        ScaleAnimation animation1 = new ScaleAnimation(1f, 0.85f, 1f, 1f);
        int screenWidth = this.getWindowManager().getDefaultDisplay().getWidth();
        int leftMagin = (int) this.getResources().getDimension(R.dimen.dim20);
        TranslateAnimation animation2 = new TranslateAnimation(0, -screenWidth / 2 + 2 * leftMagin + searchlayout_in.getWidth() / 2, 0, 0);
        animation.setDuration(300);
        animation1.setDuration(300);
        animation2.setDuration(300);
        animation.setFillAfter(true);
        animation1.setFillAfter(true);
        animation2.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(AddPromotionActivity.this, SearchActivity.class);
                intent.putExtra("type","pgoods");
                intent.putExtra("goods_id",goods_id);
                startActivityForResult(intent, 101);
                overridePendingTransition(R.anim.animation_2, R.anim.animation_1);
            }
        });
        ll_search.startAnimation(animation);
        searchlayout_bg.startAnimation(animation1);
        searchlayout_in.startAnimation(animation2);
    }

    private void backTips(){
        goods_name=tv_promotion_name.getText().toString();
        goods_price=et_promotion_price.getText().toString();
        start_time=tv_start_time.getText().toString();
        end_time=tv_end_time.getText().toString();
        if(!TextUtils.isEmpty(start_time)||!TextUtils.isEmpty(end_time)||
                !TextUtils.isEmpty(goods_name)||!TextUtils.isEmpty(goods_price)
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
        if(requestCode==101){
            int screenWidth = this.getWindowManager().getDefaultDisplay().getWidth();
            int leftMagin = (int) this.getResources().getDimension(R.dimen.dim10);
            //动画
            TranslateAnimation animation = new TranslateAnimation(0, 0, -topview.getHeight(), 0);
            ScaleAnimation animation1 = new ScaleAnimation(0.85f, 1f, 1f, 1f);
            TranslateAnimation animation2 = new TranslateAnimation(-screenWidth / 2 + 2 * leftMagin + searchlayout_in.getWidth() / 2, 0, 0, 0);
            animation.setDuration(300);
            animation1.setDuration(300);
            animation2.setDuration(300);
            animation.setFillAfter(true);
            animation1.setFillAfter(true);
            animation2.setFillAfter(true);
            ll_search.startAnimation(animation);
            searchlayout_bg.startAnimation(animation1);
            searchlayout_in.startAnimation(animation2);
            if(resultCode==99){
                goods_id=data.getStringExtra("goods_id");
                goods_name=data.getStringExtra("goods_name");
                goods_shop_price=data.getStringExtra("shop_price");
                if(!TextUtils.isEmpty(goods_shop_price)){
                    tv_promotion_shop_price.setText(goods_shop_price);
                    ll_promote_shop_price.setVisibility(View.VISIBLE);
                }else{
                    ll_promote_shop_price.setVisibility(View.GONE);
                }
                tv_promotion_name_hint.setVisibility(View.GONE);
                tv_promotion_name.setVisibility(View.VISIBLE);
                tv_promotion_name.setText(goods_name);
            }
        }
    }

    /**
     * @param root         最外层布局，需要调整的布局
     * @param scrollToView 被键盘遮挡的scrollToView，滚动root,使scrollToView在root可视区域的底部
     */
    private void controlKeyboardLayout(final View root, final View scrollToView, final float margin) {
        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                //获取root在窗体的可视区域
                root.getWindowVisibleDisplayFrame(rect);
                //获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
                int rootInvisibleHeight = root.getRootView().getHeight() - rect.bottom;
                //若不可视区域高度大于100，则键盘显示
                if (rootInvisibleHeight > 100) {
                    int[] location = new int[2];
                    //获取scrollToView在窗体的坐标
                    scrollToView.getLocationInWindow(location);
                    //计算root滚动高度，使scrollToView在可见区域
                    int scrollHeight = (location[1] + scrollToView.getHeight() + (int) margin) - rect.bottom;

                    if (scrollHeight > 0) {
                        root.scrollTo(0, scrollHeight);
                    } else {
                        root.scrollTo(0, 0);
                    }
                } else {
                    //键盘隐藏
                    root.scrollTo(0, 0);
                }
            }
        });
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
                    ToastView toast = new ToastView(AddPromotionActivity.this,res.getString(R.string.wrong_sdate));
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
                    ToastView toast = new ToastView(AddPromotionActivity.this,res.getString(R.string.wrong_edate));
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
        if (url.equals(ProtocolConst.ADMIN_PROMOTION_ADD)) {
            if (status.getSucceed() == 1) {
                ToastView toast = new ToastView(AddPromotionActivity.this, res.getString(R.string.add_promotion_success));
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                setResult(100);
                finish();
            }else{
                ToastView toast = new ToastView(AddPromotionActivity.this, status.getError_desc());
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }else
        if (url.equals(ProtocolConst.ADMIN_PROMOTION_UPDATE)) {
            if (status.getSucceed() == 1) {
                ToastView toast = new ToastView(AddPromotionActivity.this, res.getString(R.string.edit_promotion_success));
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                setResult(100);
                finish();
            }else{
                ToastView toast = new ToastView(AddPromotionActivity.this, status.getError_desc());
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }else
        if (url.equals(ProtocolConst.ADMIN_PROMOTION_DELETE)) {
            if (status.getSucceed() == 1) {
                ToastView toast = new ToastView(AddPromotionActivity.this, res.getString(R.string.del_promotion_success));
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                setResult(100);
                finish();
            }else{
                ToastView toast = new ToastView(AddPromotionActivity.this, status.getError_desc());
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }else
        if (url.equals(ProtocolConst.ADMIN_PROMOTION_DETAIL)) {
            if (status.getSucceed() == 1) {
                goods_id=model.goods.getId();
                goods_name=model.goods.getGoods_name();
                goods_price=model.goods.getFormatted_promote_price();
                goods_price=FormatUtil.formatToDigetPrice(goods_price);
                start_time=model.goods.getFormatted_promote_start_date();
                end_time=model.goods.getFormatted_promote_end_date();

                goods_shop_price=model.goods.getShop_price();

                tv_promotion_shop_price.setText(goods_shop_price);
                ll_promote_shop_price.setVisibility(View.VISIBLE);
                tv_promotion_name_hint.setVisibility(View.GONE);
                tv_promotion_name.setVisibility(View.VISIBLE);
                tv_promotion_name.setText(goods_name);
                tv_start_time.setText(start_time);
                tv_end_time.setText(end_time);
                et_promotion_price.setText(goods_price);
                et_promotion_price.setSelection(goods_price.length());

            }else{
                ToastView toast = new ToastView(AddPromotionActivity.this, status.getError_desc());
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                finish();
            }
        }
    }
}
