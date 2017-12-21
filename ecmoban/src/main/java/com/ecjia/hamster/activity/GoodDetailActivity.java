package com.ecjia.hamster.activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecjia.component.network.model.ConfigModel;
import com.ecjia.component.network.model.GoodsDelModel;
import com.ecjia.component.network.model.GoodsDetailModel;
import com.ecjia.component.network.model.GoodsOFSaleModel;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.component.view.MyDialog;
import com.ecjia.component.view.ToastView;
import com.ecjia.consts.AppConst;
import com.ecjia.hamster.model.HttpResponse;
import com.ecjia.hamster.model.PHOTO;
import com.ecjia.hamster.model.SESSION;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.hamster.model.STATUS;
import com.ecjia.util.EventBus.MyEvent;
import com.ecjia.util.ImageLoaderForLV;
import com.ecjia.util.LG;
import com.ecjia.util.TimeUtil;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ObjectAnimator;
import com.umeng.socialize.media.UMImage;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;
import gear.yc.com.gearlibrary.utils.ToastUtil;


public class GoodDetailActivity extends BaseActivity implements HttpResponse,OnClickListener {

    private TextView top_view_text,top_right_tv;
    private ImageView top_view_back,right_view,iv_good,iv_countdown;
    private TextView tv_name,tv_account,tv_price,tv_type,tv_time,tv_countdown,tv_shop_price;
    private LinearLayout ll_top,ll_detail,ll_countdown;
    private RelativeLayout rl_shop_price,rl_type;
    private FrameLayout fl_null,fl_notnull;
    private GoodsDetailModel dataModel;
    private String id;
    private SharedPreferences shared;
    private int type;
    private String uid;
    private String sid;
    private String api;
    private SESSION session;
    private String uname;
    private Timer timer;

    private LinearLayout ll_edit_open,ll_goodsdetail_bottom;
    private ImageView iv_open_edit,iv_refresh_open;
    private LinearLayout ll_goodsdetail_data;
    private boolean openFlag;

    private LinearLayout ll_sale,ll_delete,ll_fill,ll_edit_price;
    private ImageView iv_sale;
    private TextView tv_sale;
    private GoodsDelModel delModel;
    private GoodsOFSaleModel saleModel;
    private MyDialog myDialog;
    private String fstype;

    public GoodDetailActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_gooddetail);

        EventBus.getDefault().register(this);
        shared = this.getSharedPreferences("userInfo", 0);
        uid = shared.getString("uid", "");
        sid = shared.getString("sid", "");
        api = shared.getString("shopapi", "");

        session=new SESSION();
        session.setUid(uid);
        session.setSid(sid);

        Intent intent=getIntent();
        id=intent.getStringExtra("id");
        type=intent.getIntExtra("type",0);

        if(dataModel==null){
            dataModel=new GoodsDetailModel(this);
            dataModel.addResponseListener(this);
        }
        if(null == saleModel){
            saleModel=new GoodsOFSaleModel(this);
            saleModel.addResponseListener(this);
        }
        if(null == delModel){
            delModel=new GoodsDelModel(this);
            delModel.addResponseListener(this);
        }

        initView();

        dataModel.fetchGoodDetail(session, id, api);
    }

    private void initView() {
        top_view_text = (TextView) findViewById(R.id.top_view_text);
        top_right_tv = (TextView) findViewById(R.id.top_right_tv);

        fl_notnull = (FrameLayout) findViewById(R.id.fl_notnull);
        fl_null = (FrameLayout) findViewById(R.id.fl_null);

        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_account = (TextView) findViewById(R.id.tv_account);
        TextPaint tp =tv_account.getPaint();
        tp.setFakeBoldText(true);
        tv_price = (TextView) findViewById(R.id.tv_price);
        tv_type = (TextView) findViewById(R.id.tv_type);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_shop_price = (TextView) findViewById(R.id.tv_shop_price);
        tv_countdown = (TextView) findViewById(R.id.tv_countdown);

        ll_top=(LinearLayout)findViewById(R.id.ll_top);
        ll_detail=(LinearLayout)findViewById(R.id.ll_detail);
        ll_countdown=(LinearLayout)findViewById(R.id.ll_countdown);

        rl_shop_price=(RelativeLayout)findViewById(R.id.rl_shop_price);
        rl_type=(RelativeLayout)findViewById(R.id.rl_type);

        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,getDisplayMetricsWidth());
        ll_top.setLayoutParams(params);

        top_view_text.setText(res.getText(R.string.good_info));
        top_right_tv.setVisibility(View.GONE);
        iv_countdown = (ImageView) findViewById(R.id.iv_countdown);
        right_view = (ImageView) findViewById(R.id.right_view);
        right_view.setVisibility(View.VISIBLE);
        right_view.setImageResource(R.drawable.more);
        right_view.setEnabled(false);
        iv_good = (ImageView) findViewById(R.id.iv_good);
        top_view_back = (ImageView) findViewById(R.id.top_view_back);

        top_view_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                    finish();
            }
        });
        ll_detail.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent=new Intent(GoodDetailActivity.this,WebViewActivity.class);
                    intent.putExtra("id",id);
                    intent.putExtra("webUrl",dataModel.goods.getGoods_desc());
                    intent.putExtra("webtitle",res.getString(R.string.good_detail));
                startActivity(intent);
            }
        });
        right_view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                String goodsname = dataModel.goods.getName();
                String sale = res.getString(R.string.detail_sale);
                String good = res.getString(R.string.detail_good);
                PHOTO photo = dataModel.goods.getImg();
                // 定义所需传入的的url地址
                String imageurl[] = {photo.getSmall(), photo.getThumb(), photo.getUrl()};
                UMImage umimg = null;
                String url = null;
                // 判断分享的图片是否是默认图片
                for (String x : imageurl) {
                    umimg = new UMImage(GoodDetailActivity.this, x);
                    if (!isDefaultIamge(umimg)) {
                        LG.i(x);
                        url = x;
                        break;
                    } else {
                        ToastView toast = new ToastView(GoodDetailActivity.this, res.getString(R.string.share_pic_null));
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        return;
                    }
                }
                String goodsurl="";
                if(!TextUtils.isEmpty(ConfigModel.getInstance().config.getSite_url())){

                    goodsurl = ConfigModel.getInstance().config.getSite_url()+"/sites/touch/index.php?m=goods&c=index&a=init&id="
                            +  id;
                    uname=ConfigModel.getInstance().config.getShop_name();
                }
                String mycontent = uname + sale + goodsname + good;

                Intent it = new Intent(GoodDetailActivity.this,ShareActivity.class);
                it.putExtra("mycontent", mycontent);
                it.putExtra("goodurl", url);
                it.putExtra("goodsurl", goodsurl);
                it.putExtra("type",type);
                it.putExtra("goodid", id);
                it.putExtra("goodsname", goodsname);
                try {
                    it.putExtra("data",dataModel.goods.toJson().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(it);
                 overridePendingTransition(R.anim.push_share_in,R.anim.push_buttom_out);

            }
        });

        initBottomArea();
    }

    private void initBottomArea() {

        ll_edit_open=(LinearLayout)findViewById(R.id.ll_edit_open);
        ll_goodsdetail_bottom=(LinearLayout)findViewById(R.id.ll_goodsdetail_bottom);
        ll_goodsdetail_data=(LinearLayout)findViewById(R.id.ll_goodsdetail_data);
        iv_open_edit=(ImageView)findViewById(R.id.iv_open_edit);
        iv_refresh_open=(ImageView)findViewById(R.id.iv_refresh_open);

        ll_delete = (LinearLayout) findViewById(R.id.ll_delete);
        ll_sale = (LinearLayout) findViewById(R.id.ll_sale);
        ll_fill = (LinearLayout) findViewById(R.id.ll_fill);
        ll_edit_price = (LinearLayout) findViewById(R.id.ll_edit_price2);
        iv_sale=(ImageView)findViewById(R.id.iv_sale);
        tv_sale=(TextView)findViewById(R.id.tv_sale);

        ll_delete.setOnClickListener(this);
        ll_sale.setOnClickListener(this);
        ll_edit_price.setOnClickListener(this);

        if(type==1){
            iv_sale.setImageResource(R.drawable.share_off);
            tv_sale.setText(res.getString(R.string.off_sale));
            ll_sale.setVisibility(View.VISIBLE);
            ll_fill.setVisibility(View.GONE);
        }else if(type==2){
            iv_sale.setImageResource(R.drawable.share_on);
            tv_sale.setText(res.getString(R.string.to_on_sale));
            ll_sale.setVisibility(View.VISIBLE);
            ll_fill.setVisibility(View.GONE);
        }else if(type==3){
            ll_sale.setVisibility(View.GONE);
            ll_fill.setVisibility(View.VISIBLE);
        }

        ll_goodsdetail_data.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (openFlag) {
                    iv_open_edit.setEnabled(false);
                    int height = ll_edit_open.getHeight();
                    ObjectAnimator anim3 = ObjectAnimator.ofFloat(ll_edit_open, "translationY",
                            Float.valueOf(height + ll_goodsdetail_bottom.getHeight()-1));
                    anim3.setDuration(600);//毫秒计算
                    anim3.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            openFlag = false;
                            iv_open_edit.setEnabled(true);
                            ll_goodsdetail_data.setEnabled(false);
                        }
                    });
                    anim3.start();

                    final AlphaAnimation animation = new AlphaAnimation(1, 0);
                    animation.setDuration(600);
                    ll_goodsdetail_data.setAnimation(animation);
                    animation.start();
                    ll_goodsdetail_data.setVisibility(View.GONE);
                }
            }
        });

        iv_refresh_open.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dataModel.fetchGoodDetail(session,id,api);
            }
        });

        iv_open_edit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (openFlag) {
                    iv_open_edit.setEnabled(false);
                    int height = ll_edit_open.getHeight();

                    ObjectAnimator anim3 = ObjectAnimator.ofFloat(ll_edit_open, "translationY",
                            Float.valueOf(height + ll_goodsdetail_bottom.getHeight()-1));
                    anim3.setDuration(600);//毫秒计算
                    anim3.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            openFlag = false;
                            iv_open_edit.setEnabled(true);
                            ll_goodsdetail_data.setEnabled(false);
                        }
                    });
                    anim3.start();

                    final AlphaAnimation animation = new AlphaAnimation(1, 0);
                    animation.setDuration(600);
                    ll_goodsdetail_data.setAnimation(animation);
                    animation.start();
                    ll_goodsdetail_data.setVisibility(View.GONE);

                } else {
                    iv_open_edit.setEnabled(false);
                    int height = ll_edit_open.getHeight();
                    ObjectAnimator anim3 = ObjectAnimator.ofFloat(ll_edit_open, "translationY",
                            0f ,  -Float.valueOf(height + ll_goodsdetail_bottom.getHeight()-1));
                    anim3.setDuration(600);//毫秒计算
                    anim3.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            openFlag = true;
                            iv_open_edit.setEnabled(true);
                            ll_goodsdetail_data.setEnabled(true);
                        }
                    });
                    anim3.start();

                    final AlphaAnimation animation = new AlphaAnimation(0, 1);
                    animation.setDuration(600);
                    ll_goodsdetail_data.setAnimation(animation);
                    ll_goodsdetail_data.setVisibility(View.VISIBLE);
                    animation.start();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_delete:
                String tips = res.getString(R.string.tip);
                String tips_content = res.getString(R.string.tips_content_del);
                myDialog = new MyDialog(GoodDetailActivity.this, tips, tips_content);
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
                        delModel.GoodsDel(session, id, api);
                        myDialog.dismiss();
                    }
                });
                break;
            case R.id.ll_sale:
                String tip = res.getString(R.string.tip);
                String tip_content = "";
                if (type == 1) {
                    tip_content = res.getString(R.string.tips_content_off_sale);
                } else if (type == 2) {
                    tip_content = res.getString(R.string.tips_content_on_sale);
                }
                myDialog = new MyDialog(GoodDetailActivity.this, tip, tip_content);
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
                        saleModel.GoodsSale(session, id, getFType(type), api);
                        myDialog.dismiss();
                    }
                });
                break;
            case R.id.ll_edit_price2:
                Intent intent = new Intent(GoodDetailActivity.this, PriceEditActivity.class);
                try {
                    intent.putExtra("data",dataModel.goods.toJson().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
                break;
        }
    }

    private String getFType(int type) {
        fstype = "";
        switch (type) {
            case 1:
                fstype = "offline";
                break;
            case 2:
                fstype = "online";
                break;
        }
        return fstype;
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int flag = msg.what;
            if (flag == 5) {
                countDownPromote();
                return;
            }
        }
    };

    public void countDownPromote() {
        String contentString = "";
        contentString += "剩" + TimeUtil.timeLeft(dataModel.goods.getPromote_end_date());
        Spanned htmlString = Html.fromHtml(contentString);
        tv_countdown.setText(htmlString);

    }



    // 获取屏幕宽度
    public int getDisplayMetricsWidth() {
        int i = this.getWindowManager().getDefaultDisplay().getWidth();
        int j = this.getWindowManager().getDefaultDisplay()
                .getHeight();
        return Math.min(i, j);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        if (null != timer) {
            timer.cancel();
            timer = null;
        }
        super.onDestroy();
    }

    public void onEvent(MyEvent event) {
        if ("GOODSDEL".equals(event.getMsg())) {
            LG.i("运行==");
            this.finish();
        }else if("ONLINE".equals(event.getMsg())){
            type=1;
        }else if("OFFLINE".equals(event.getMsg())){
            type=2;
        }else if("PRICEREFRESH".equals(event.getMsg())){
            if(null!=dataModel){
                dataModel.fetchGoodDetail(session,id,api);
            }
        }

    }

    /**
     * 判断分享的图片是否是默认图片
     *
     * @return boolean
     */
    public boolean isDefaultIamge(UMImage image) {
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.umeng_socialize_share_pic);
        return image == new UMImage(this,b);
    }

    @Override
    public void OnMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        String error13 = res.getString(R.string.error_13);
        String error101 = res.getString(R.string.error_101);

        if (url.equals(ProtocolConst.GOODSDETAIL)) {
            if (status.getSucceed() == 1) {
                int promoteflag=0;
                if(!"0".equals(dataModel.goods.getPromote_price())){
                    promoteflag=1;
                }
                tv_name.setText(dataModel.goods.getName());
                tv_time.setText(dataModel.goods.getTime());
                tv_price.setText(dataModel.goods.getMarket_price());
                tv_shop_price.setText(dataModel.goods.getShop_price());

                if (null != timer) {
                    timer.cancel();
                    timer = null;
                }

                if(promoteflag==1){
                   ll_countdown.setVisibility(View.VISIBLE);
                   iv_countdown.setVisibility(View.VISIBLE);
                   rl_shop_price.setVisibility(View.VISIBLE);
                    tv_account.setText(dataModel.goods.getPromote_price());

                  if(!TextUtils.isEmpty(dataModel.goods.getPromote_end_date())){
                      timer = new Timer();
                      timer.schedule(new TimerTask() {
                          @Override
                          public void run() {
                              Message msg = handler.obtainMessage();
                              msg.what = 5;
                              handler.sendMessage(msg);
                          }
                      }, new Date(), 1000);
                  }else{
                      ll_countdown.setVisibility(View.GONE);
                  }

                }else{
                    ll_countdown.setVisibility(View.GONE);
                    iv_countdown.setVisibility(View.GONE);
                    rl_shop_price.setVisibility(View.GONE);
                    tv_account.setText(dataModel.goods.getShop_price());
                }

//                tv_type.setText(dataModel.goods.getCategory());

                ImageLoaderForLV.getInstance(GoodDetailActivity.this).setImageRes(iv_good, dataModel.goods.getImg().getThumb());

                right_view.setEnabled(true);

                fl_notnull.setVisibility(View.VISIBLE);
                fl_null.setVisibility(View.GONE);

            } else if (status.getError_code()== AppConst.UnexistInformation) {

                ToastView toast = new ToastView(GoodDetailActivity.this, error13);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

                fl_notnull.setVisibility(View.GONE);
                fl_null.setVisibility(View.VISIBLE);

            }else if (status.getError_code() == AppConst.InvalidParameter) {

                ToastView toast = new ToastView(GoodDetailActivity.this, error101);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

                fl_notnull.setVisibility(View.GONE);
                fl_null.setVisibility(View.VISIBLE);
            }
        }else if (url.equals(ProtocolConst.GOODSSALE)){
            if(status.getSucceed()==1){
                ToastUtil.getInstance().makeShortToast(this,"操作成功");
                finish();
            }
        } else if (url.equals(ProtocolConst.GOODSDEL)) {
            if (status.getSucceed() == 1) {
                ToastUtil.getInstance().makeShortToast(this,"操作成功");
                finish();
            }
        }

    }


}
