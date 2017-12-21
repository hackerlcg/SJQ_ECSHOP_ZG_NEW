package com.ecjia.base;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import com.ecjia.component.network.base.RetrofitHttpServices;
import com.ecjia.component.view.MyDialog;
import com.ecjia.consts.AndroidManager;
import com.ecjia.consts.SharedPKeywords;
import com.ecjia.hamster.activity.ECJiaMainActivity;
import com.ecjia.hamster.activity.GoodDetailActivity;
import com.ecjia.hamster.activity.LoginActivity;
import com.ecjia.hamster.activity.OrderDetailActivity;
import com.ecjia.hamster.activity.QRCodeActivity;
import com.ecjia.hamster.activity.SettingActivity;
import com.ecjia.hamster.activity.WebViewActivity;
import com.ecjia.hamster.adapter.MsgSql;
import com.ecjia.hamster.model.MYMESSAGE;
import com.ecjia.hamster.model.SESSION;
import com.ecjia.util.EventBus.MyEvent;
import com.ecjia.util.LG;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.lidroid.xutils.HttpUtils;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.sjq.cn.newmojie.shopkeeper.PushActivity;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import de.greenrobot.event.EventBus;
import gear.yc.com.gearlibrary.network.http.OkHttpManager;

/**
 * Created by Administrator on 2014/12/3.
 */
public class ECJiaApplication extends MultiDexApplication {

    public ActivityManager mActivityManager;
    public String mPackageName;
    public Handler handler;
    public UMessage uMessage = null;
    public MYMESSAGE mymsg;
    private String uid;
    private String sid;
    private int msgnum;
    private SharedPreferences shared;
    private SharedPreferences.Editor editor;
    private int screen_width;
    private HttpUtils httpUtils;
    public int getScreen_width() {
        return screen_width;
    }

    public void setScreen_width(int screen_width) {
        this.screen_width = screen_width;
    }

    public HttpUtils getHttpUtils() {
        return httpUtils;
    }

    public void setHttpUtils(HttpUtils httpUtils) {
        this.httpUtils = httpUtils;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initSession();
        mPackageName = getPackageName();
        mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        shared = this.getSharedPreferences("userInfo", 0);
        editor=shared.edit();
        screen_width=getApplicationContext().getResources().getDisplayMetrics().widthPixels;
        httpUtils=new HttpUtils();
        RetrofitHttpServices.getInstance()
                .setBaseUrl(AndroidManager.SERVICE_URL)
                .build(OkHttpManager.getInstance().build().getClient());
        /**用户自定义消息接收
         * 该Handler是在BroadcastReceiver中被调用，故
         * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
         * */

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                try {
                    uMessage = new UMessage(new JSONObject(msg.obj.toString()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                final MYMESSAGE mymsg0 = mymsg;
                if (msg.what == 0) {
                    final MyDialog myDialog = new MyDialog(getApplicationContext(), uMessage.title, uMessage.text);
                    myDialog.version_positive.setVisibility(View.VISIBLE);
                    myDialog.sureText.setText("确定");
                    myDialog.dialog_buttom.setVisibility(View.GONE);
                    myDialog.version_positive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EventBus.getDefault().post(new MyEvent("REFRESHPUSH"));
                            myDialog.dismiss();
                        }
                    });
                    myDialog.mDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                    myDialog.show();
                }

                if (msg.what == 1) {
                    final MyDialog myDialog = new MyDialog(getApplicationContext(), uMessage.title, uMessage.text);
                    myDialog.version_positive.setVisibility(View.GONE);
                    myDialog.dialog_buttom.setVisibility(View.VISIBLE);
                    myDialog.positiveText.setText("打开");
                    myDialog.negativeText.setText("忽略");
                    myDialog.positive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EventBus.getDefault().post(new MyEvent("REFRESHPUSH"));
                            myDialog.dismiss();
                            oPendAction(ECJiaApplication.this, uMessage);
                        }
                    });
                    myDialog.negative.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myDialog.dismiss();

                        }
                    });
                    myDialog.mDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                    myDialog.show();
                }

            }
        };


        UmengMessageHandler messageHandler = new UmengMessageHandler() {

            @Override
            public void handleMessage(Context context, final UMessage uMessage) {
                super.handleMessage(context, uMessage);
                mymsg = new MYMESSAGE();
                mymsg.setTitle(uMessage.title);
                mymsg.setContent(uMessage.text);
                mymsg.setCustom(uMessage.custom);
                mymsg.setMsg_id(uMessage.msg_id);
                mymsg.setType(uMessage.after_open);
                mymsg.setUrl(uMessage.url);
                mymsg.setGotoActivity(uMessage.activity);
                if (null != uMessage.extra) {
                    mymsg.setOpen_type(uMessage.extra.get("open_type"));

                    if ("webview".equals(mymsg.getOpen_type())) {
                        mymsg.setWebUrl(uMessage.extra.get("url"));
                    } else if ("goods_list".equals(mymsg.getOpen_type())) {
                        mymsg.setCategory_id(uMessage.extra.get("category_id"));

                    } else if ("goods_comment".equals(mymsg.getOpen_type())) {
                        mymsg.setGoods_id_comment(uMessage.extra.get("goods_id"));

                    } else if ("goods_detail".equals(mymsg.getOpen_type())) {
                        mymsg.setGoods_id(uMessage.extra.get("goods_id"));

                    } else if ("orders_detail".equals(mymsg.getOpen_type())) {
                        mymsg.setOrder_id(uMessage.extra.get("order_id"));

                    } else if ("keyword".equals(mymsg.getKeyword())) {
                        mymsg.setKeyword(uMessage.extra.get("keyword"));
                    }
                }
                mymsg.setReadStatus(MYMESSAGE.UNREAD);
                MsgSql.getIntence(context).insertMessage(mymsg);

                uid = shared.getString("uid", "");
                sid = shared.getString("sid", "");
                msgnum = shared.getInt("msgnum", 0);

                msgnum+=1;
                editor.putInt("msgnum",msgnum);
                editor.commit();
                EventBus.getDefault().post(new MyEvent("MSGNUM"));

                if (isAppOnForeground()) {
                    if (null != uMessage.extra) {
                        Message message = new Message();
                        message.obj = uMessage.getRaw().toString();
                        message.what = 1;
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.obj = uMessage.getRaw().toString();
                        message.what = 0;
                        handler.sendMessage(message);
                    }
                    UTrack.getInstance(getApplicationContext()).trackMsgClick(uMessage);
                }

            }

            @Override
            public Notification getNotification(Context context, UMessage uMessage) {
                return super.getNotification(context, uMessage);
            }

        };
        PushAgent.getInstance(this).setMessageHandler(messageHandler);

        //前台时显示开关
        PushAgent.getInstance(getApplicationContext()).setNotificaitonOnForeground(false);

        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {

            @Override
            public void openUrl(Context context, UMessage uMessage) {
            }

            @Override
            public void openActivity(Context context, UMessage uMessage) {
                super.openActivity(context, uMessage);
            }

            @Override
            public void launchApp(Context context, UMessage uMessage) {
                oPendAction(context, uMessage);
            }


            @SuppressLint("NewApi")
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                Intent intent = new Intent(context, PushActivity.class);
                intent.putExtra("refresh", true);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }

        };

        PushAgent.getInstance(this).

                setNotificationClickHandler(notificationClickHandler);
        //初始化ImageLoader
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().showStubImage(R.drawable.default_image).showImageForEmptyUri(R.drawable.default_image).showImageOnFail(R.drawable.default_image).showImageOnLoading(R.drawable.default_image).cacheInMemory(true).cacheOnDisk(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT).bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).defaultDisplayImageOptions(defaultOptions).threadPriority(Thread.NORM_PRIORITY - 2).memoryCache(new WeakMemoryCache()).memoryCacheSize((int) (3 * 1024 * 1024)).denyCacheImageMultipleSizesInMemory().memoryCacheExtraOptions(300, 300)
                .discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
    }

    public boolean isAppOnForeground() {
        List<ActivityManager.RunningTaskInfo> tasksInfo = mActivityManager.getRunningTasks(1);
        if (tasksInfo.size() > 0) {
            LG.i("top Activity = " + tasksInfo.get(0).topActivity.getPackageName());
            // 应用程序位于堆栈的顶层
            if (mPackageName.equals(tasksInfo.get(0).topActivity.getPackageName())) {
                LG.i("在前台1");
                return true;
            }
        }
        LG.i("在后台1");
        return false;
    }

    public String getTopActivity(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);

        if (runningTaskInfos != null) {
            LG.i("(runningTaskInfos.get(0).topActivity).toString()================" + (runningTaskInfos.get(0).topActivity).toString());
            return (runningTaskInfos.get(0).topActivity).toString();
        } else {
            return null;
        }
    }

    private void oPendAction(Context context, UMessage uMessage) {

        if (null != uMessage.extra) {
            if (TextUtils.isEmpty(uid)|| TextUtils.isEmpty(sid)) {
                Intent intent = new Intent(context, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("from", uMessage.extra.get("open_type"));
                intent.putExtra("orderid", uMessage.extra.get("order_id"));
                intent.putExtra("goodid", uMessage.extra.get("goods_id"));
                intent.putExtra("webUrl", uMessage.extra.get("url"));
                intent.putExtra("keyword", uMessage.extra.get("keyword"));
                intent.putExtra("msgid", uMessage.msg_id);
                context.startActivity(intent);
            } else {
                msgnum-=1;
                editor.putInt("msgnum",msgnum);
                editor.commit();
                MsgSql.getIntence(context).updateData(uMessage.msg_id);

                Intent intent1 = new Intent(context, ECJiaMainActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if ("search".equals(uMessage.extra.get("open_type"))) {//搜索
                    intent1.putExtra("msgfrom",uMessage.extra.get("open_type"));
                    intent1.putExtra("keyword",uMessage.extra.get("keyword"));
                    context.startActivity(intent1);
                } else if ("orders_list".equals(uMessage.extra.get("open_type"))) {//订单列表
                    intent1.putExtra("msgfrom",uMessage.extra.get("open_type"));
                    context.startActivity(intent1);
                } else if ("goods_list".equals(uMessage.extra.get("open_type"))) {//商品列表
                    intent1.putExtra("msgfrom",uMessage.extra.get("open_type"));
                    context.startActivity(intent1);
                }else
                if ("main".equals(uMessage.extra.get("open_type"))) {//主页
                    Intent intent = new Intent(context, ECJiaMainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else if ("setting".equals(uMessage.extra.get("open_type"))) {//设置
                    Intent intent = new Intent(context, SettingActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else if ("webview".equals(uMessage.extra.get("open_type"))) {//浏览器
                    Intent intent = new Intent(context, WebViewActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("webUrl", uMessage.extra.get("url"));
                    context.startActivity(intent);
                } else if ("goods_detail".equals(uMessage.extra.get("open_type"))) {//商品详情
                    Intent intent = new Intent(context, GoodDetailActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("id", uMessage.extra.get("goods_id"));
                    context.startActivity(intent);
                } else if ("qrshare".equals(uMessage.extra.get("open_type"))) {//二维码分享
                    Intent intent = new Intent(context, QRCodeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else if ("signin".equals(uMessage.extra.get("open_type"))) {//登录
                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else if ("orders_detail".equals(uMessage.extra.get("open_type"))) {//订单详情
                    Intent intent = new Intent(context, OrderDetailActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("id", uMessage.extra.get("order_id"));
                    context.startActivity(intent);
                } else if ("message".equals(uMessage.extra.get("open_type"))) {//消息中心
                    Intent intent = new Intent(context, PushActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else {//默认消息中心
                    Intent intent = new Intent(context, PushActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        } else {//默认消息中心
            if (TextUtils.isEmpty(uid)|| TextUtils.isEmpty(sid)) {
                Intent intent = new Intent(context, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } else {
                Intent intent = new Intent(context, PushActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }

    }

    private void initSession() {
        SharedPreferences shared = this.getSharedPreferences(SharedPKeywords.SPUSER, 0);
        SESSION session = SESSION.getInstance();
        session.setUid(shared.getString(SharedPKeywords.SPUSER_KUID, ""));
        session.setSid(shared.getString(SharedPKeywords.SPUSER_KSID, ""));
    }


}
