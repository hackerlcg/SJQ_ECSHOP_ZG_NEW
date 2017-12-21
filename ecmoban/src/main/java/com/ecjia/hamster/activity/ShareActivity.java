package com.ecjia.hamster.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ecjia.base.BaseActivity;
import com.ecjia.component.network.model.GoodsDelModel;
import com.ecjia.component.network.model.GoodsOFSaleModel;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.component.view.MyDialog;
import com.ecjia.component.view.ToastView;
import com.ecjia.consts.AndroidManager;
import com.ecjia.hamster.model.HttpResponse;
import com.ecjia.hamster.model.SESSION;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.hamster.model.STATUS;
import com.ecjia.util.EventBus.Event;
import com.ecjia.util.EventBus.MyEvent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.EmailHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.SmsHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Set;

import de.greenrobot.event.EventBus;

public class ShareActivity extends BaseActivity implements OnClickListener,HttpResponse {
    LinearLayout sinawb, qq, weixin, circle, sms, email, back;
    private LinearLayout ll_sale,ll_delete;
    private ImageView iv_sale;
    private TextView tv_sale;
    private GoodsDelModel delModel;
    private GoodsOFSaleModel saleModel;
    final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
    Intent intent;
    public Handler Intenthandler;
    //添加邮件到分享
    private EmailHandler emailHandler;
    // 添加短信
    private SmsHandler smsHandler;
    // 添加微信平台
    private UMWXHandler wxHandler;
    // 支持微信朋友圈
    private UMWXHandler wxCircleHandler;
    //添加QQ在分享列表页中
    private UMQQSsoHandler qqSsoHandler;
    private SinaSsoHandler sinaSsoHandler;
    private MySnsPostListener mSnsPostListener;

    private SharedPreferences shared;
    private int type;
    private String id;
    private String uid;
    private String sid;
    private String api;
    private SESSION session;
    private String fstype;
    private MyDialog myDialog;
    private LinearLayout ll_edit_price;
    private String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        Window lp = getWindow();
        lp.setGravity(Gravity.BOTTOM);
        lp.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        EventBus.getDefault().register(this);
        intent = getIntent();
        id=intent.getStringExtra("goodid");
        data=intent.getStringExtra("data");
        type=intent.getIntExtra("type",0);

        startHandler();

        shared = this.getSharedPreferences("userInfo", 0);
        uid = shared.getString("uid", "");
        sid = shared.getString("sid", "");
        api = shared.getString("shopapi", "");

        session=new SESSION();
        session.setUid(uid);
        session.setSid(sid);

        if(delModel==null){
            delModel=new GoodsDelModel(this);
            delModel.addResponseListener(this);
        }
        if(saleModel==null){
            saleModel=new GoodsOFSaleModel(this);
            saleModel.addResponseListener(this);
        }

        setInfo();

    }

    //基础信息
    void setInfo() {
        ll_edit_price = (LinearLayout) findViewById(R.id.ll_edit_price);

        sinawb = (LinearLayout) findViewById(R.id.share_sinawb);
        qq = (LinearLayout) findViewById(R.id.share_qqfriend);
        weixin = (LinearLayout) findViewById(R.id.share_weixinitem);
        circle = (LinearLayout) findViewById(R.id.share_circle);
        sms = (LinearLayout) findViewById(R.id.share_smsitem);
        email = (LinearLayout) findViewById(R.id.share_emailitem);
        back = (LinearLayout) findViewById(R.id.share_cancle);
        ll_delete = (LinearLayout) findViewById(R.id.ll_delete);
        ll_sale = (LinearLayout) findViewById(R.id.ll_sale);
        iv_sale=(ImageView)findViewById(R.id.iv_sale);
        tv_sale=(TextView)findViewById(R.id.tv_sale);

        if(type==1){
            iv_sale.setImageResource(R.drawable.share_off);
            tv_sale.setText(res.getString(R.string.off_sale));
            ll_sale.setVisibility(View.VISIBLE);
        }else if(type==2){
            iv_sale.setImageResource(R.drawable.share_on);
            tv_sale.setText(res.getString(R.string.to_on_sale));
            ll_sale.setVisibility(View.VISIBLE);
        }else if(type==3){
            ll_sale.setVisibility(View.GONE);
        }

        sinawb.setOnClickListener(this);
        qq.setOnClickListener(this);
        weixin.setOnClickListener(this);
        circle.setOnClickListener(this);
        sms.setOnClickListener(this);
        email.setOnClickListener(this);
        back.setOnClickListener(this);
        ll_delete.setOnClickListener(this);
        ll_sale.setOnClickListener(this);
        ll_edit_price.setOnClickListener(this);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_delete:
                String tips = res.getString(R.string.tip);
                String tips_content =res.getString(R.string.tips_content_del);
                myDialog = new MyDialog(ShareActivity.this, tips, tips_content);
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
                String tip_content ="";
                if(type==1){
                    tip_content = res.getString(R.string.tips_content_off_sale);
                }else if(type==2){
                    tip_content = res.getString(R.string.tips_content_on_sale);
                }
                myDialog = new MyDialog(ShareActivity.this, tip, tip_content);
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
            case R.id.ll_edit_price:
                Intent intent = new Intent(ShareActivity.this, PriceEditActivity.class);
                intent.putExtra("data",data);
                startActivityForResult(intent, 1);
                break;
            case R.id.share_sinawb:
                startShare(SHARE_MEDIA.SINA);
                break;
            case R.id.share_qqfriend:
                startShare(SHARE_MEDIA.QQ);
                break;
            case R.id.share_weixinitem:
                startShare(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.share_circle:
                startShare(SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            case R.id.share_smsitem:
                startShare(SHARE_MEDIA.SMS);
                break;
            case R.id.share_emailitem:
                startShare(SHARE_MEDIA.EMAIL);
                break;
            case R.id.share_cancle:
                this.finish();
                break;
        }
    }

    private String getFType(int type){
        fstype="";
        switch (type){
            case 1:
                fstype="offline";
                break;
            case 2:
                fstype="online";
                break;
        }
        return fstype;
    }

    private void startShare(SHARE_MEDIA shareType) {
        mController.getConfig().closeToast();
        // 设置分享
        mController.setShareContent(intent.getStringExtra("mycontent"));
        // 设置分享图片, 参数2为图片的url地址
        if (SHARE_MEDIA.SMS == shareType) {
//            UMImage img = imageopt(new UMImage(ShareActivity.this, intent.getStringExtra("goodurl")));
//            mController.setShareMedia(img);
        } else {
            mController.setShareMedia(new UMImage(ShareActivity.this, intent.getStringExtra("goodurl")));
        }
        mController.getConfig().setDefaultShareLocation(false);
        //新浪微博
        if (SHARE_MEDIA.SINA == shareType) {
//            if (!OauthHelper.isAuthenticated(this, shareType)) {
//                mController.doOauthVerify(this, shareType, new MyUMAuthListener());
//                mController.getPlatformInfo(ShareActivity.this, shareType, new MyUMDataListener());
//            }
            mController.getConfig().setSsoHandler(sinaSsoHandler);
            SinaShareContent sinaShareContent=new SinaShareContent();
            sinaShareContent.setShareContent(intent.getStringExtra("mycontent"));
            sinaShareContent.setTitle(intent.getStringExtra("goodsname"));
            sinaShareContent.setTargetUrl(intent.getStringExtra("goodsurl"));
            sinaShareContent.setShareImage(new UMImage(ShareActivity.this, intent.getStringExtra("goodurl")));
            mController.setShareMedia(sinaShareContent);
        }

        //腾讯QQ
        if (SHARE_MEDIA.QQ == shareType) {
//            if (!OauthHelper.isAuthenticated(this, shareType)) {
//                mController.doOauthVerify(this, SHARE_MEDIA.QQ, new MyUMAuthListener());
//                mController.getPlatformInfo(ShareActivity.this, shareType, new MyUMDataListener());
//            }
            mController.getConfig().setSsoHandler(qqSsoHandler);
            QQShareContent qqShareContent=new QQShareContent();
            qqShareContent.setShareContent(intent.getStringExtra("mycontent"));
            qqShareContent.setTitle(intent.getStringExtra("goodsname"));
            qqShareContent.setTargetUrl(intent.getStringExtra("goodsurl"));
            qqShareContent.setShareImage(new UMImage(ShareActivity.this, intent.getStringExtra("goodurl")));
            mController.setShareMedia(qqShareContent);
        }
        //微信好友
        if (SHARE_MEDIA.WEIXIN == shareType) {
//            if (!OauthHelper.isAuthenticated(this, shareType)) {
//                mController.doOauthVerify(this, shareType, new MyUMAuthListener());
//                mController.getPlatformInfo(ShareActivity.this, shareType, new MyUMDataListener());
//            }
            mController.getConfig().setSsoHandler(wxHandler);
            WeiXinShareContent weixinContent = new WeiXinShareContent();
            weixinContent.setShareContent(intent.getStringExtra("mycontent"));
            weixinContent.setTitle(intent.getStringExtra("goodsname"));
            weixinContent.setTargetUrl(intent.getStringExtra("goodsurl"));
            weixinContent.setShareImage(new UMImage(ShareActivity.this, intent.getStringExtra("goodurl")));
            mController.setShareMedia(weixinContent);
        }
        //微信朋友圈
        if (SHARE_MEDIA.WEIXIN_CIRCLE == shareType) {
//            if (!OauthHelper.isAuthenticated(this, shareType)) {
//                mController.doOauthVerify(this, shareType, new MyUMAuthListener());
//                mController.getPlatformInfo(ShareActivity.this, shareType, new MyUMDataListener());
//            }
            mController.getConfig().setSsoHandler(wxHandler);
            CircleShareContent circleMedia = new CircleShareContent();
            circleMedia.setShareContent(intent.getStringExtra("mycontent"));
            circleMedia.setTitle(intent.getStringExtra("goodsname"));
            circleMedia.setTargetUrl(intent.getStringExtra("goodsurl"));
            circleMedia.setShareImage(new UMImage(ShareActivity.this, intent.getStringExtra("goodurl")));
            mController.setShareMedia(circleMedia);
        }
        mSnsPostListener = new MySnsPostListener();
        //分享
        mController.postShare(ShareActivity.this, shareType, mSnsPostListener);
        //注册分享监听
        mController.registerListener(mSnsPostListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**使用SSO授权必须添加如下代码 */
        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }


    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mController.unregisterListener(null);
    }

    @Override
    public void OnMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if (url.equals(ProtocolConst.GOODSDEL)) {
            if (status.getSucceed() == 1) {
                EventBus.getDefault().post(new MyEvent("GOODSDEL"));
                finish();
            }
        }
        if (url.equals( ProtocolConst.GOODSSALE)) {
            if (status.getSucceed() == 1) {
                if(type==1){
                    EventBus.getDefault().post(new MyEvent("OFFLINE"));
                    ToastView toast = new ToastView(ShareActivity.this,res.getString(R.string.off_sale_success));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.setDuration(200);
                    toast.show();
                }else if(type==2){
                    EventBus.getDefault().post(new MyEvent("ONLINE"));
                ToastView toast = new ToastView(ShareActivity.this,res.getString(R.string.to_on_sale_success));
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.setDuration(200);
                toast.show();}
                finish();
            }
        }
    }

    //授权第三方登录
    class MyUMAuthListener implements SocializeListeners.UMAuthListener {

        @Override
        public void onStart(SHARE_MEDIA platform) {
            Toast.makeText(ShareActivity.this, "授权开始", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SocializeException e, SHARE_MEDIA platform) {
            Toast.makeText(ShareActivity.this, "授权错误", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onComplete(Bundle value, SHARE_MEDIA platform) {

            if (value != null && !TextUtils.isEmpty(value.getString("uid"))) {
                Toast.makeText(ShareActivity.this, "授权完成", Toast.LENGTH_SHORT).show();
                //获取相关授权信息或者跳转到自定义的分享编辑页面
                //一般情况获取第三方登录信息都在这里完成
//                mController.getPlatformInfo(ShareActivity.this, platform, new MyUMDataListener());
            } else {
                Toast.makeText(ShareActivity.this, "授权失败", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(ShareActivity.this, "授权取消", Toast.LENGTH_SHORT).show();
        }

    }

    //获取第三方登录信息
    class MyUMDataListener implements SocializeListeners.UMDataListener {

        @Override
        public void onStart() {
            Toast.makeText(ShareActivity.this, "获取平台数据开始...", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onComplete(int status, Map<String, Object> info) {
            if (status == 200 && info != null) {
                StringBuilder sb = new StringBuilder();
                Set<String> keys = info.keySet();
                for (String key : keys) {
                    sb.append(key + "=" + info.get(key).toString() + "\r\n");
                }
                Log.d("TestData", sb.toString());
            } else {
                Log.d("TestData", "发生错误：" + status);
            }
        }
    }

    //分享
    class MySnsPostListener implements SocializeListeners.SnsPostListener {
        @Override
        public void onStart() {
//            Toast.makeText(ShareActivity.this, "开始分享.", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
            Resources resources = getBaseContext().getResources();
            if (eCode == 200) {
                if(platform==SHARE_MEDIA.EMAIL||platform==SHARE_MEDIA.SMS){

                }else{
                    ToastView toast = new ToastView(ShareActivity.this,resources.getString(R.string.share_succeed));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.setDuration(200);
                    toast.show();}
            } else {
                String eMsg = "";
                if (eCode == -101) {
                    eMsg = resources.getString(R.string.share_no_accredit);
                    ToastView toast = new ToastView(ShareActivity.this, resources.getString(R.string.share_failed));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.setDuration(200);
                    toast.show();
                }else{
                    ToastView toast = new ToastView(ShareActivity.this, resources.getString(R.string.share_cancel));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.setDuration(200);
                    toast.show();
                }
            }
            ShareActivity.this.finish();
        }
    }

    private void startHandler() {
        smsHandler = new SmsHandler();
        emailHandler = new EmailHandler();
        sinaSsoHandler = new SinaSsoHandler();
        if(!TextUtils.isEmpty(AndroidManager.WXAPPID)){
            wxHandler = new UMWXHandler(this, AndroidManager.WXAPPID, AndroidManager.WXAPPSECRET);
            wxHandler.addToSocialSDK();
            wxCircleHandler = new UMWXHandler(this, AndroidManager.WXAPPID, AndroidManager.WXAPPSECRET);
            wxCircleHandler.setToCircle(true);
            wxCircleHandler.addToSocialSDK();
        }
        if(!TextUtils.isEmpty(AndroidManager.QQAPPID)){
            qqSsoHandler = new UMQQSsoHandler(this, AndroidManager.QQAPPID, AndroidManager.QQAPPSECRET);
            qqSsoHandler.addToSocialSDK();
        }
        smsHandler.addToSocialSDK();
        emailHandler.addToSocialSDK();
        sinaSsoHandler.addToSocialSDK();
    }


    public void onEvent(Event event) {
        if("PRICEREFRESH".equals(event.getMsg())){
            finish();
        }
    }

}
