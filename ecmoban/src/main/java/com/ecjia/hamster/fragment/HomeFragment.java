package com.ecjia.hamster.fragment;


import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ecjia.component.network.model.ConfigModel;
import com.ecjia.component.network.model.HomeModel;
import com.ecjia.component.network.model.ShopModel;
import com.ecjia.component.view.ToastView;
import com.ecjia.component.view.XListView;
import com.ecjia.consts.AndroidManager;
import com.ecjia.consts.AppConst;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.hamster.activity.CustomerCenterActivity;
import com.ecjia.hamster.activity.MyCaptureActivity;
import com.ecjia.hamster.activity.ShopChangeActivity;
import com.ecjia.hamster.activity.StatsActivity;
import com.ecjia.hamster.activity.goods.MyGoodsParentActivity;
import com.ecjia.hamster.adapter.HeadPagerAdapter;
import com.ecjia.hamster.model.HttpResponse;
import com.ecjia.hamster.model.SESSION;
import com.ecjia.hamster.model.STATUS;
import com.ecjia.util.EventBus.Event;
import com.ecjia.util.common.ImageUtils;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.sjq.cn.newmojie.shopkeeper.PushActivity;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.UmengRegistrar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
/**
 * 类名介绍：首页
 * Created by sun
 * Created time 2017-03-02.
 */
public class HomeFragment extends Fragment implements
        XListView.IXListViewListener, AppConst.RegisterApp, HttpResponse {
    boolean isRefresh = false;
    private View mainView;
    private XListView myListView;
    private View headviewpager;
    private ViewPager mviewPager;
    private LinearLayout viewOne,viewTwo,group,home_down_two,ll_mygoods,ll_myorders,ll_mymessage;
    private RelativeLayout head_topview,rl_mygoods,rl_myorders,rl_mymessage,rl_mystats;
    public boolean isActive = false;
    private ArrayList<View> pagerlist;
    private HeadPagerAdapter mAdapter;
    private int prePointPosition = 0;
    private TextView tv_t_income,tv_t_order,tv_t_custom,tv_y_income,tv_y_order,tv_y_custom,tv_uname;
    private TextView tv_t_income2,tv_t_order2,tv_t_custom2,tv_y_income2,tv_y_order2,tv_y_custom2,tv_message_num;
    private HomeModel homeModel;
    private ShopModel shopModel;
    public ConfigModel configModel;
    private SharedPreferences shared;
    private SharedPreferences.Editor editor;
    private String uid;
    private String sid;
    private String shopapi;
    private String uname;
    private ImageView iv_change_shop,iv_top_zxing;
    private SESSION session;

    private boolean isumengregister = false;
    private int msgnum;
    Resources resource;
    private ImageView shop_logo;
    private String shopLogUrl;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.home, null);

        EventBus.getDefault().register(this);
        addView();
        resource = getActivity().getResources();
        shared = getActivity().getSharedPreferences("userInfo", 0);

        uid = shared.getString("uid", "");
        sid = shared.getString("sid", "");
        shopapi = shared.getString("shopapi", "");
        uname = shared.getString("shop_name", "");
        shopLogUrl=shared.getString("shop_logo","");
//        ImageLoaderForLV.getInstance(getActivity()).setImageRes(shop_logo,shopLogUrl);
        ImageUtils.showImageCircleHead(getActivity(),shopLogUrl,shop_logo);
        session=new SESSION();
        session.setUid(uid);
        session.setSid(sid);

        if(shopModel==null){
            shopModel=new ShopModel(getActivity());
            shopModel.addResponseListener(this);
        }
        if(shopModel.regionses==null||shopModel.regionses.size()==0){
            shopModel.getShopArea(session,shopapi,true);
        }

        if(homeModel==null){
            homeModel=new HomeModel(getActivity());
            homeModel.addResponseListener(this);
        }
        if(homeModel.homes==null||homeModel.homes.size()==0){
            homeModel.getHomeData(session,shopapi,true);
        }else{
            if(homeModel.homes.size()==6) {
                tv_t_income.setText(homeModel.homes.get(0).getValue());
                tv_t_order.setText(homeModel.homes.get(1).getValue());
                tv_t_custom.setText(homeModel.homes.get(2).getValue());

                tv_y_income.setText(homeModel.homes.get(3).getValue());
                tv_y_order.setText(homeModel.homes.get(4).getValue());
                tv_y_custom.setText(homeModel.homes.get(5).getValue());

                tv_t_income2.setText(homeModel.homes.get(0).getLabel());
                tv_t_order2.setText(homeModel.homes.get(1).getLabel());
                tv_t_custom2.setText(homeModel.homes.get(2).getLabel());

                tv_y_income2.setText(homeModel.homes.get(3).getLabel());
                tv_y_order2.setText(homeModel.homes.get(4).getLabel());
                tv_y_custom2.setText(homeModel.homes.get(5).getLabel());
            }
        }

        if (null == ConfigModel.getInstance()) {
            configModel = new ConfigModel(getActivity());
            configModel.getConfig(shopapi,session);
        }

        if (AndroidManager.SUPPORT_PUSH) {
            isumengregister = shared.getBoolean("isumengregister", false);//是否已注册推送
            configModel.setDeviceToken(UmengRegistrar.getRegistrationId(this.getActivity()));
        }
        myListView.setAdapter(null);


        myListView.addHeaderView(head_topview);
        myListView.addHeaderView(headviewpager);
        myListView.addHeaderView(home_down_two);

        myListView.setPullLoadEnable(false);
        myListView.setPullRefreshEnable(true);
        myListView.setXListViewListener(this, 0);
        myListView.setRefreshTime();

        return mainView;

}

    private void addView() {

        if(null==pagerlist){pagerlist=new ArrayList<View>();}else{pagerlist.clear();}
        myListView=(XListView)mainView.findViewById(R.id.home_listview);

        // 添加banner
        headviewpager = (View) LayoutInflater.from(getActivity()).inflate(R.layout.home_headviewpager, null);
        mviewPager=(ViewPager)headviewpager.findViewById(R.id.home_viewpager);
        viewOne = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.home_banner, null);
        viewTwo = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.home_banner2, null);
        pagerlist.add(viewOne);
        pagerlist.add(viewTwo);
        mAdapter=new HeadPagerAdapter(pagerlist);

        group = (LinearLayout) headviewpager.findViewById(R.id.home_viewGroup);

        viewOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        viewTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        AddPoint();

        tv_t_income=(TextView)viewOne.findViewById(R.id.tv_t_income);
        tv_t_order=(TextView)viewOne.findViewById(R.id.tv_t_order);
        tv_t_custom=(TextView)viewOne.findViewById(R.id.tv_t_custom);
        tv_y_income=(TextView)viewTwo.findViewById(R.id.tv_y_income);
        tv_y_order=(TextView)viewTwo.findViewById(R.id.tv_y_order);
        tv_y_custom=(TextView)viewTwo.findViewById(R.id.tv_y_custom);

        tv_t_income2=(TextView)viewOne.findViewById(R.id.tv_t_income2);
        tv_t_order2=(TextView)viewOne.findViewById(R.id.tv_t_order2);
        tv_t_custom2=(TextView)viewOne.findViewById(R.id.tv_t_custom2);
        tv_y_income2=(TextView)viewTwo.findViewById(R.id.tv_y_income2);
        tv_y_order2=(TextView)viewTwo.findViewById(R.id.tv_y_order2);
        tv_y_custom2=(TextView)viewTwo.findViewById(R.id.tv_y_custom2);

        tv_t_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), StatsActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });
        tv_y_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), StatsActivity.class);
                intent.putExtra("type",1);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });
        tv_t_income2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), StatsActivity.class);
                intent.putExtra("type",1);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });
        tv_y_income2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), StatsActivity.class);
                intent.putExtra("type",1);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });
//----------------------------------
        tv_t_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), StatsActivity.class);
                intent.putExtra("type",2);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });
        tv_y_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), StatsActivity.class);
                intent.putExtra("type",2);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });
        tv_t_order2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), StatsActivity.class);
                intent.putExtra("type",2);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });
        tv_y_order2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), StatsActivity.class);
                intent.putExtra("type",2);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });
//---------------------------------------------
        tv_t_custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), StatsActivity.class);
                intent.putExtra("type",3);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });
        tv_y_custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), StatsActivity.class);
                intent.putExtra("type",3);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });
        tv_t_custom2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), StatsActivity.class);
                intent.putExtra("type",3);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });
        tv_y_custom2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), StatsActivity.class);
                intent.putExtra("type",3);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });





        mviewPager.setAdapter(mAdapter);
        mviewPager.setCurrentItem(0);
        mviewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int position) {
                position = position % pagerlist.size();
                //把对于的position位置的点选中 图片的描述要切换到position的位置上
                //那些对应的小点
                group.getChildAt(prePointPosition).setEnabled(false);   //前一个点的颜色变为没有被选中的状态
                group.getChildAt(position).setEnabled(true);

                prePointPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        head_topview=(RelativeLayout)LayoutInflater.from(getActivity()).inflate(R.layout.home_headtop, null);

        AbsListView.LayoutParams rlp = new AbsListView.LayoutParams(getDisplayMetricsWidth(),(int)(getResources().getDimension(R.dimen.px400)));

        head_topview.setLayoutParams(rlp);

        tv_uname=(TextView)head_topview.findViewById(R.id.tv_uname);
        iv_change_shop=(ImageView)mainView.findViewById(R.id.iv_change_shop);
        iv_change_shop.setVisibility(View.GONE);
        iv_top_zxing=(ImageView)mainView.findViewById(R.id.iv_top_zxing);
        shop_logo= (ImageView) head_topview.findViewById(R.id.shop_logo);
        shop_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), CustomerCenterActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });

        //顶部二维码扫描
        iv_top_zxing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MyCaptureActivity.class);
                intent.putExtra("startType", 1);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });


        tv_uname.setText(uname);

        iv_change_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), ShopChangeActivity.class);
                intent.putExtra("fromInner",true);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });


        home_down_two=(LinearLayout)LayoutInflater.from(getActivity()).inflate(R.layout.home_down_two, null);

        AbsListView.LayoutParams llp = new AbsListView.LayoutParams(
                getDisplayMetricsWidth(),
                (int)(getResources().getDimension(R.dimen.dp_255)));
        getDPI();
        home_down_two.setLayoutParams(llp);

        tv_message_num=(TextView)home_down_two.findViewById(R.id.tv_message_num);


        adddownlistener();


    }

    private void adddownlistener() {
        rl_mygoods=(RelativeLayout)home_down_two.findViewById(R.id.rl_mygoods);
        rl_mygoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MyGoodsParentActivity.class));
//                  TabsFragment.getInstance().toMyFragment(4,"");
            }
        });

        rl_myorders=(RelativeLayout)home_down_two.findViewById(R.id.rl_myorders);
        ll_myorders=(LinearLayout)home_down_two.findViewById(R.id.ll_myorders);
        rl_myorders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TabsFragment.getInstance().OnTabSelected("tab_four");;
            }
        });

        rl_mymessage=(RelativeLayout)home_down_two.findViewById(R.id.rl_mymessage);
        rl_mymessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), PushActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });

        rl_mystats=(RelativeLayout)home_down_two.findViewById(R.id.rl_stats);
        rl_mystats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), StatsActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });




    }


    // 获取屏幕宽度
    public int getDisplayMetricsWidth() {
        int i = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        int j = getActivity().getWindowManager().getDefaultDisplay()
                .getHeight();
        return Math.min(i, j);
    }
    // 获取屏幕高度
    public int getDisplayMetricsHeight() {
        int i = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        int j = getActivity().getWindowManager().getDefaultDisplay()
                .getHeight();
        return Math.max(i, j);
    }

    // 添加轮播图小点
    public void AddPoint() {
        final Resources res = getResources();
        group.removeAllViews();
        if (pagerlist.size() == 0) {

        } else {

            for (int i = 0; i < pagerlist.size(); i++) {
                //像LinearLayout中添加一个view 对象 设置为点的背景
                View view = new View(getActivity());
                //点的大小吧   这里的LayoutParams是这个view父容器的
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        (int) res.getDimension(R.dimen.dim12),
                        (int) res.getDimension(R.dimen.dim12));
                lp.setMargins(
                        (int) res.getDimension(R.dimen.dim8), 0,
                        (int) res.getDimension(R.dimen.dim8), 0);
                //上面是设置view的参数  然后把参数设置为这个view
                view.setLayoutParams(lp);
                view.setBackgroundResource(R.drawable.select_point_bg);
                view.setEnabled(false);
                // 把点设置为没选中状态  开始都是没选选中的
                group.addView(view);

            }
            group.getChildAt(0).setEnabled(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isActive) {
            isActive = true;
            AppConst.registerApp(this);
        }

        refreshMsg();

        MobclickAgent.onPageStart("Home");
    }

    public void onRefresh(int id) {
        homeModel.getHomeData(session,shopapi,false);
    }

    @Override
    public void onLoadMore(int id) {

    }

    @Override
    public void onStop() {

        super.onStop();
        if (!isAppOnForeground()) {
            // app 进入后台
            isActive = false;
        }
    }


    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public boolean isAppOnForeground() {
        // Returns a list of application processes that are running on the
        // device

        ActivityManager activityManager = (ActivityManager) getActivity()
                .getApplicationContext().getSystemService(
                        Context.ACTIVITY_SERVICE);
        String packageName = getActivity().getApplicationContext()
                .getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void onRegisterResponse(boolean success) {

    }


    @Override
    public void OnMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {

        String invalid = resource.getString(R.string.session_invalid);

        if (url .equals( ProtocolConst.HOMEDATA)) {
            if (status.getSucceed() == 1) {
                if(homeModel.homes.size()==6) {
                    tv_t_income.setText(homeModel.homes.get(0).getValue());
                    tv_t_order.setText(homeModel.homes.get(1).getValue());
                    tv_t_custom.setText(homeModel.homes.get(2).getValue());

                    tv_y_income.setText(homeModel.homes.get(3).getValue());
                    tv_y_order.setText(homeModel.homes.get(4).getValue());
                    tv_y_custom.setText(homeModel.homes.get(5).getValue());

                    tv_t_income2.setText(homeModel.homes.get(0).getLabel());
                    tv_t_order2.setText(homeModel.homes.get(1).getLabel());
                    tv_t_custom2.setText(homeModel.homes.get(2).getLabel());

                    tv_y_income2.setText(homeModel.homes.get(3).getLabel());
                    tv_y_order2.setText(homeModel.homes.get(4).getLabel());
                    tv_y_custom2.setText(homeModel.homes.get(5).getLabel());
                }

                tv_uname.setText(uname);

            } else  {
                ToastView toast = new ToastView(getActivity(), invalid);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
        if (url.equals( ProtocolConst.SETDEVICE_TOKEN)) {
            if (status.getSucceed() == 1) {
                editor = shared.edit();
                editor.putBoolean("isumengregister", true);
                editor.commit();
            } else {

            }
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    public void onEvent(Event event) {
        if ("MSGNUM".equals(event.getMsg())) {
            refreshMsg();
        }
    }

    private void refreshMsg() {
        msgnum = shared.getInt("msgnum", 0);

        if(msgnum>0){
            tv_message_num.setVisibility(View.VISIBLE);
            if(msgnum>99){
                tv_message_num.setText("99+");
            }else{
                tv_message_num.setText(msgnum+"");
            }
        }else{
            tv_message_num.setVisibility(View.GONE);
        }
    }

    private float getDPI() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        return displayMetrics.densityDpi; //得到DPI
    }
    private float getDensity(){
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        return displayMetrics.density; //得到密度
    }
}
