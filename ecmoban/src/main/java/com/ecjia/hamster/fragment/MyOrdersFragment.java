package com.ecjia.hamster.fragment;


import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ecjia.component.network.model.OrdersDetailModel;
import com.ecjia.component.network.model.OrdersListModel;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.component.view.ToastView;
import com.ecjia.component.view.XListView;
import com.ecjia.consts.AppConst;
import com.ecjia.hamster.activity.OrderDetailActivity;
import com.ecjia.hamster.activity.SearchActivity;
import com.ecjia.hamster.activity.SubOrderActivity;
import com.ecjia.hamster.activity.order.OrderReutrnInfoActivity_Builder;
import com.ecjia.hamster.adapter.OrdersListAdapter;
import com.ecjia.hamster.adapter.OrdersListReturnAdapter;
import com.ecjia.hamster.model.HttpResponse;
import com.ecjia.hamster.model.PAGINATED;
import com.ecjia.hamster.model.SESSION;
import com.ecjia.util.LG;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.hamster.model.STATUS;
import com.ecjia.util.EventBus.MyEvent;


import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;
import gear.yc.com.gearlibrary.utils.ToastUtil;

/**
 * 类名介绍：我的订单页面
 * Created by sun
 * Created time 2017-03-02.
 */
public class MyOrdersFragment extends Fragment implements View.OnClickListener, HttpResponse {

    private View view;
    private SharedPreferences shared;
    public Handler Intenthandler;
    private RelativeLayout rl_1, rl_2, rl_3, rl_4, rl_5, rl_6;
    private TextView tv_1, tv_2, tv_3, tv_4, tv_5, tv_6;
    private View line_1, line_2, line_3, line_4, line_5, line_6;
    private int TYPE;
    private String KEYWORDS;
    private TextView top_view_text;
    private ImageView top_view_back;
    private String uid, sid, api;
    private FrameLayout fl_null, fl_notnull;
    private SESSION session;
    private XListView listview;
    private XListView listview_return;

    private OrdersListModel dataModel;
    private OrdersDetailModel detailModel;

    private OrdersListAdapter listAdapter;
    private OrdersListReturnAdapter returnListAdapter;

    private LinearLayout ll_search, topview, searchlayout_in;
    private View searchlayout_bg;
    private FrameLayout searchLayout;
    private TextView et_order_search;
    private View ll_bottom;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.myorders, null);//加载整个Fragment布局
        EventBus.getDefault().register(this);
        shared = getActivity().getSharedPreferences("userInfo", 0);
        uid = shared.getString("uid", "");
        sid = shared.getString("sid", "");
        api = shared.getString("shopapi", "");
        session = new SESSION();
        session.setUid(uid);
        session.setSid(sid);

        KEYWORDS = "";

        if (null == dataModel) {
            dataModel = new OrdersListModel(getActivity());
            dataModel.addResponseListener(this);
        }
        if (null == detailModel) {
            detailModel = new OrdersDetailModel(getActivity());
            detailModel.addResponseListener(this);
        }
        initView();
        if (TYPE != 0) {
            refreshUI(TYPE);
            if (TYPE == 5) {
                dataModel.fetchPreReturnSearch(true);
            }else {
                dataModel.fetchPreSearch(session, getFtype(TYPE), KEYWORDS, api, true);
            }
        } else {
            TYPE = 1;
            dataModel.fetchPreSearch(session, getFtype(TYPE), KEYWORDS, api, true);
        }

        return view;
    }

    private void initView() {
        fl_null = (FrameLayout) view.findViewById(R.id.fl_null);
        fl_notnull = (FrameLayout) view.findViewById(R.id.fl_notnull);

        listview = (XListView) view.findViewById(R.id.listview);
        listview_return = (XListView) view.findViewById(R.id.listview_return);

        rl_1 = (RelativeLayout) view.findViewById(R.id.rl_1);
        rl_2 = (RelativeLayout) view.findViewById(R.id.rl_2);
        rl_3 = (RelativeLayout) view.findViewById(R.id.rl_3);
        rl_4 = (RelativeLayout) view.findViewById(R.id.rl_4);
        rl_5 = (RelativeLayout) view.findViewById(R.id.rl_5);
        rl_6 = (RelativeLayout) view.findViewById(R.id.rl_6);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) getDisplayMetricsWidth() / 4, ViewGroup.LayoutParams.MATCH_PARENT);

        rl_1.setLayoutParams(params);
        rl_2.setLayoutParams(params);
        rl_3.setLayoutParams(params);
        rl_4.setLayoutParams(params);
        rl_5.setLayoutParams(params);
        rl_6.setLayoutParams(params);

        rl_1.setOnClickListener(this);
        rl_2.setOnClickListener(this);
        rl_3.setOnClickListener(this);
        rl_4.setOnClickListener(this);
        rl_5.setOnClickListener(this);
        rl_6.setOnClickListener(this);

        tv_1 = (TextView) view.findViewById(R.id.tv_1);
        tv_2 = (TextView) view.findViewById(R.id.tv_2);
        tv_3 = (TextView) view.findViewById(R.id.tv_3);
        tv_4 = (TextView) view.findViewById(R.id.tv_4);
        tv_5 = (TextView) view.findViewById(R.id.tv_5);
        tv_6 = (TextView) view.findViewById(R.id.tv_6);

        line_1 = (View) view.findViewById(R.id.line_1);
        line_2 = (View) view.findViewById(R.id.line_2);
        line_3 = (View) view.findViewById(R.id.line_3);
        line_4 = (View) view.findViewById(R.id.line_4);
        line_5 = (View) view.findViewById(R.id.line_5);
        line_6 = (View) view.findViewById(R.id.line_6);

        top_view_text = (TextView) view.findViewById(R.id.top_view_text);
        top_view_text.setText(getActivity().getResources().getString(R.string.my_orders));

        top_view_back = (ImageView) view.findViewById(R.id.top_view_back);
        top_view_back.setVisibility(View.GONE);
        top_view_back.setOnClickListener(this);

        ll_search = (LinearLayout) view.findViewById(R.id.ll_search);
        searchlayout_bg = (View) view.findViewById(R.id.fragment_order_searchlayout_bg);
        et_order_search = (TextView) view.findViewById(R.id.et_order_search);
        searchlayout_in = (LinearLayout) view.findViewById(R.id.fragment_order_searchlayout_in);
        topview = (LinearLayout) view.findViewById(R.id.order_search_topview);
        ll_bottom = (View) view.findViewById(R.id.ll_bottom);
        searchLayout = (FrameLayout) view.findViewById(R.id.fragment_order_searchlayout);
        searchLayout.setOnClickListener(this);


        if (null == listAdapter) {
            listAdapter = new OrdersListAdapter(getActivity(), dataModel.ordersList);
        }
        listview.setAdapter(listAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (position != 0) {
                    position = position - 1;
                }
                detailModel.fetchOrderDetail(session, dataModel.ordersList.get(position).getId(), api);
            }
        });

        listview.setPullLoadEnable(false);
        listview.setPullRefreshEnable(true);
        listview.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh(int id) {
                dataModel.fetchPreSearch(session, getFtype(TYPE), KEYWORDS, api, false);
            }

            @Override
            public void onLoadMore(int id) {
                dataModel.fetchPreSearchMore(session, getFtype(TYPE), KEYWORDS, api);
            }
        }, 0);
        listview.setRefreshTime();

        //退换货的listViewView

        if (null == returnListAdapter) {
            returnListAdapter = new OrdersListReturnAdapter(getActivity(), dataModel.order_return_list);
        }
        listview_return.setAdapter(returnListAdapter);

        listview_return.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (position != 0) {
                    position = position - 1;
                }
               // ToastUtil.getInstance().makeLongToast(getActivity(), dataModel.order_return_list.get(position).getRet_id() + "");
                OrderReutrnInfoActivity_Builder.intent(getActivity()).retId(dataModel.order_return_list.get(position).getRet_id()).start();
            }
        });

        listview_return.setPullLoadEnable(false);
        listview_return.setPullRefreshEnable(true);
        listview_return.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh(int id) {
                dataModel.fetchPreReturnSearch(false);
            }

            @Override
            public void onLoadMore(int id) {
                dataModel.fetchPreReturnSearchMore();
            }
        }, 0);
        listview_return.setRefreshTime();
    }

    @Override
    public void onResume() {
        super.onResume();
//        LG.d("OkHttp返回参数+onResume++++"+"TYPE++++"+TYPE);
        if (5==TYPE) {
            dataModel.fetchPreReturnSearch(false);
        } else {
            dataModel.fetchPreSearch(session, getFtype(TYPE), KEYWORDS, api, false);
        }

    }

    // 获取屏幕宽度
    public int getDisplayMetricsWidth() {
        int i = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        int j = getActivity().getWindowManager().getDefaultDisplay()
                .getHeight();
        return Math.min(i, j);
    }

    private void checkNull() {
        if (dataModel.ordersList.size() == 0) {
            fl_null.setVisibility(View.VISIBLE);
            fl_notnull.setVisibility(View.GONE);
        } else {
            fl_null.setVisibility(View.GONE);
            fl_notnull.setVisibility(View.VISIBLE);
        }
    }

    private void checkReturnNull() {
        if (dataModel.order_return_list.size() == 0) {
            fl_null.setVisibility(View.VISIBLE);
            fl_notnull.setVisibility(View.GONE);
        } else {
            fl_null.setVisibility(View.GONE);
            fl_notnull.setVisibility(View.VISIBLE);
        }
    }


    //1待付款
    //2待发货
    //3已发货
    //4已完成
    //5退款
    //6已关闭
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_1:
                if (TYPE != 1) {
                    TYPE = 1;
                    refreshUI(TYPE);
                    dataModel.fetchPreSearch(session, getFtype(TYPE), KEYWORDS, api, true);
                    listview_return.setVisibility(View.GONE);
                    listview.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.rl_2:
                if (TYPE != 2) {
                    TYPE = 2;
                    refreshUI(TYPE);
                    dataModel.fetchPreSearch(session, getFtype(TYPE), KEYWORDS, api, true);
                    listview_return.setVisibility(View.GONE);
                    listview.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.rl_3:
                if (TYPE != 3) {
                    TYPE = 3;
                    refreshUI(TYPE);
                    dataModel.fetchPreSearch(session, getFtype(TYPE), KEYWORDS, api, true);
                    listview_return.setVisibility(View.GONE);
                    listview.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.rl_4:
                if (TYPE != 4) {
                    TYPE = 4;
                    refreshUI(TYPE);
                    dataModel.fetchPreSearch(session, getFtype(TYPE), KEYWORDS, api, true);
                    listview_return.setVisibility(View.GONE);
                    listview.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.rl_5://退款
                if (TYPE != 5) {
                    TYPE = 5;
                    refreshUI(TYPE);
                    dataModel.fetchPreReturnSearch(true);
                    listview_return.setVisibility(View.VISIBLE);
                    listview.setVisibility(View.GONE);
//                      dataModel.fetchPreSearch(session, getFtype(TYPE), KEYWORDS, api, true);
                }
                break;
            case R.id.rl_6:
                if (TYPE != 6) {
                    TYPE = 6;
                    refreshUI(TYPE);
                    dataModel.fetchPreSearch(session, getFtype(TYPE), KEYWORDS, api, true);
                    listview_return.setVisibility(View.GONE);
                    listview.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.top_view_back:
//                  TabsFragment.getInstance().toMyFragment(1,"");
                break;
            case R.id.fragment_order_searchlayout:
                ll_bottom.setVisibility(View.GONE);
                TranslateAnimation animation = new TranslateAnimation(0, 0, 0, -topview.getHeight());
                ScaleAnimation animation1 = new ScaleAnimation(1f, 0.85f, 1f, 1f);
                int screenWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth();
                int leftMagin = (int) getActivity().getResources().getDimension(R.dimen.dim20);
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
                        Intent intent = new Intent(getActivity(), SearchActivity.class);
                        intent.putExtra("type", "order");
                        intent.putExtra("KEYWORDS", KEYWORDS);
                        startActivityForResult(intent, 101);
                        getActivity().overridePendingTransition(R.anim.animation_2, R.anim.animation_1);
                    }
                });
                ll_search.startAnimation(animation);
                searchlayout_bg.startAnimation(animation1);
                searchlayout_in.startAnimation(animation2);
                break;
        }
    }

    private String getFtype(int type) {
        String ftype = "await_pay";
        switch (type) {
            case 1:
                ftype = "await_pay";
                break;
            case 2:
                ftype = "await_ship";
                break;
            case 3:
                ftype = "shipped";
                break;
            case 4:
                ftype = "finished";
                break;
            case 5:
                ftype = "refund";
                break;
            case 6:
                ftype = "closed";
                break;
        }
        return ftype;
    }


    private void refreshUI(int type) {
        switch (type) {
            case 1:
                tv_1.setTextColor(getActivity().getResources().getColor(R.color.bg_theme_color));
                tv_2.setTextColor(getActivity().getResources().getColor(R.color.text_login_hint_color));
                tv_3.setTextColor(getActivity().getResources().getColor(R.color.text_login_hint_color));
                tv_4.setTextColor(getActivity().getResources().getColor(R.color.text_login_hint_color));
                tv_5.setTextColor(getActivity().getResources().getColor(R.color.text_login_hint_color));
                tv_6.setTextColor(getActivity().getResources().getColor(R.color.text_login_hint_color));
                line_1.setVisibility(View.VISIBLE);
                line_2.setVisibility(View.INVISIBLE);
                line_3.setVisibility(View.INVISIBLE);
                line_4.setVisibility(View.INVISIBLE);
                line_5.setVisibility(View.INVISIBLE);
                line_6.setVisibility(View.INVISIBLE);
                break;
            case 2:
                tv_1.setTextColor(getActivity().getResources().getColor(R.color.text_login_hint_color));
                tv_2.setTextColor(getActivity().getResources().getColor(R.color.bg_theme_color));
                tv_3.setTextColor(getActivity().getResources().getColor(R.color.text_login_hint_color));
                tv_4.setTextColor(getActivity().getResources().getColor(R.color.text_login_hint_color));
                tv_5.setTextColor(getActivity().getResources().getColor(R.color.text_login_hint_color));
                tv_6.setTextColor(getActivity().getResources().getColor(R.color.text_login_hint_color));
                line_1.setVisibility(View.INVISIBLE);
                line_2.setVisibility(View.VISIBLE);
                line_3.setVisibility(View.INVISIBLE);
                line_4.setVisibility(View.INVISIBLE);
                line_5.setVisibility(View.INVISIBLE);
                line_6.setVisibility(View.INVISIBLE);
                break;
            case 3:
                tv_1.setTextColor(getActivity().getResources().getColor(R.color.text_login_hint_color));
                tv_2.setTextColor(getActivity().getResources().getColor(R.color.text_login_hint_color));
                tv_3.setTextColor(getActivity().getResources().getColor(R.color.bg_theme_color));
                tv_4.setTextColor(getActivity().getResources().getColor(R.color.text_login_hint_color));
                tv_5.setTextColor(getActivity().getResources().getColor(R.color.text_login_hint_color));
                tv_6.setTextColor(getActivity().getResources().getColor(R.color.text_login_hint_color));
                line_1.setVisibility(View.INVISIBLE);
                line_2.setVisibility(View.INVISIBLE);
                line_3.setVisibility(View.VISIBLE);
                line_4.setVisibility(View.INVISIBLE);
                line_5.setVisibility(View.INVISIBLE);
                line_6.setVisibility(View.INVISIBLE);
                break;
            case 4:
                tv_1.setTextColor(getActivity().getResources().getColor(R.color.text_login_hint_color));
                tv_2.setTextColor(getActivity().getResources().getColor(R.color.text_login_hint_color));
                tv_3.setTextColor(getActivity().getResources().getColor(R.color.text_login_hint_color));
                tv_4.setTextColor(getActivity().getResources().getColor(R.color.bg_theme_color));
                tv_5.setTextColor(getActivity().getResources().getColor(R.color.text_login_hint_color));
                tv_6.setTextColor(getActivity().getResources().getColor(R.color.text_login_hint_color));
                line_1.setVisibility(View.INVISIBLE);
                line_2.setVisibility(View.INVISIBLE);
                line_3.setVisibility(View.INVISIBLE);
                line_4.setVisibility(View.VISIBLE);
                line_5.setVisibility(View.INVISIBLE);
                line_6.setVisibility(View.INVISIBLE);
                break;
            case 5:
                tv_1.setTextColor(getActivity().getResources().getColor(R.color.text_login_hint_color));
                tv_2.setTextColor(getActivity().getResources().getColor(R.color.text_login_hint_color));
                tv_3.setTextColor(getActivity().getResources().getColor(R.color.text_login_hint_color));
                tv_4.setTextColor(getActivity().getResources().getColor(R.color.text_login_hint_color));
                tv_5.setTextColor(getActivity().getResources().getColor(R.color.bg_theme_color));
                tv_6.setTextColor(getActivity().getResources().getColor(R.color.text_login_hint_color));
                line_1.setVisibility(View.INVISIBLE);
                line_2.setVisibility(View.INVISIBLE);
                line_3.setVisibility(View.INVISIBLE);
                line_4.setVisibility(View.INVISIBLE);
                line_5.setVisibility(View.VISIBLE);
                line_6.setVisibility(View.INVISIBLE);
                break;
            case 6:
                tv_1.setTextColor(getActivity().getResources().getColor(R.color.text_login_hint_color));
                tv_2.setTextColor(getActivity().getResources().getColor(R.color.text_login_hint_color));
                tv_3.setTextColor(getActivity().getResources().getColor(R.color.text_login_hint_color));
                tv_4.setTextColor(getActivity().getResources().getColor(R.color.text_login_hint_color));
                tv_5.setTextColor(getActivity().getResources().getColor(R.color.text_login_hint_color));
                tv_6.setTextColor(getActivity().getResources().getColor(R.color.bg_theme_color));
                line_1.setVisibility(View.INVISIBLE);
                line_2.setVisibility(View.INVISIBLE);
                line_3.setVisibility(View.INVISIBLE);
                line_4.setVisibility(View.INVISIBLE);
                line_5.setVisibility(View.INVISIBLE);
                line_6.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void onEvent(MyEvent event) {
        if ("ORDERCANCEL".equals(event.getMsg())) {
            dataModel.fetchPreSearch(session, getFtype(TYPE), KEYWORDS, api, true);
        }
    }

    @Override
    public void OnMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if (url.equals(ProtocolConst.ORDERS)) {
            if (status.getSucceed() == 1) {
                listview.stopRefresh();
                listview.stopLoadMore();
                listview.setRefreshTime();
                PAGINATED paginated = dataModel.paginated;
                if (0 == paginated.getMore()) {
                    listview.setPullLoadEnable(false);
                } else {
                    listview.setPullLoadEnable(true);
                }
                listAdapter.notifyDataSetChanged();
                checkNull();
                listview_return.setVisibility(View.GONE);
                listview.setVisibility(View.VISIBLE);
            }
        }
        if (url.equals(ProtocolConst.ORDERS_RETURN)) {
            if (status.getSucceed() == 1) {
                listview_return.stopRefresh();
                listview_return.stopLoadMore();
                listview_return.setRefreshTime();
                PAGINATED paginated = dataModel.paginated;
                if (0 == paginated.getMore()) {
                    listview_return.setPullLoadEnable(false);
                } else {
                    listview_return.setPullLoadEnable(true);
                }
                returnListAdapter.notifyDataSetChanged();
                checkReturnNull();
                listview_return.setVisibility(View.VISIBLE);
                listview.setVisibility(View.GONE);
            }
        }
        if (url.equals(ProtocolConst.ORDERDETAIL)) {
            String error13 = getActivity().getResources().getString(R.string.error_13);
            String error101 = getActivity().getResources().getString(R.string.error_101);

            if (status.getSucceed() == 1) {
                if (detailModel.order.getSuborderses().size() > 0) {
                    Intent intent = new Intent(getActivity(), SubOrderActivity.class);
                    try {
                        intent.putExtra("data", detailModel.order.toJson().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    intent.putExtra("type", TYPE);
                    intent.putExtra("id", detailModel.order.getId());
                    getActivity().startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.push_right_in,
                            R.anim.push_right_out);
                } else {
                    Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
                    intent.putExtra("id", detailModel.order.getId());
                    intent.putExtra("type", TYPE);
                    getActivity().startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.push_right_in,
                            R.anim.push_right_out);
                }

            } else if (status.getSucceed() == AppConst.UnexistInformation) {

                ToastView toast = new ToastView(getActivity(), error13);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else if (status.getSucceed() == AppConst.InvalidParameter) {

                ToastView toast = new ToastView(getActivity(), error101);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 100 && requestCode == 101) {

            int screenWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth();
            int leftMagin = (int) getActivity().getResources().getDimension(R.dimen.dim10);
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
            ll_bottom.setVisibility(View.VISIBLE);
        }

    }
}
