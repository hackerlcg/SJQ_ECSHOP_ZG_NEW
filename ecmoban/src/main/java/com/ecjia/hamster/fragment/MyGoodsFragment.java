package com.ecjia.hamster.fragment;


import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ecjia.component.network.model.GoodsDelModel;
import com.ecjia.component.network.model.GoodsListModel;
import com.ecjia.component.network.model.GoodsOFSaleModel;
import com.ecjia.component.view.LeftSlidingListView;
import com.ecjia.component.view.MyDialog;
import com.ecjia.component.view.ToastView;
import com.ecjia.component.view.XListView;
import com.ecjia.consts.AppConst;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.hamster.activity.GoodsFilterActivity;
import com.ecjia.hamster.activity.SearchActivity;
import com.ecjia.hamster.activity.WebViewActivity;
import com.ecjia.hamster.activity.goods.MyGoodsParentActivity;
import com.ecjia.hamster.adapter.GoodsListAdapter;
import com.ecjia.hamster.model.CATEGORY;
import com.ecjia.hamster.model.GOODS;
import com.ecjia.hamster.model.HttpResponse;
import com.ecjia.hamster.model.PAGINATED;
import com.ecjia.hamster.model.SESSION;
import com.ecjia.hamster.model.STATUS;
import com.ecjia.util.EventBus.MyEvent;
import com.ecmoban.android.shopkeeper.sijiqing.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

/**
 * 类名介绍：我的宝贝页面
 * Created by sun
 * Created time 2017-03-02.
 */
public class MyGoodsFragment extends Fragment implements View.OnClickListener, HttpResponse,
        XListView.IXListViewListener {

    private View view;
    private SharedPreferences shared;
    private RelativeLayout rl_1, rl_2, rl_3, rl_4;
    private LinearLayout ll_bottom, ll_sale, ll_delete, ll_top_bg;
    private TextView tv_1, tv_2, tv_3;
    private View line_1, line_2, line_3;
    private int TYPE;
    private int CATEGORYID;
    private String SORT;
    private String KEYWORDS;
    private TextView top_view_text, top_right_tv, tv_ofsale;
    private ImageView top_view_back, iv_sale, iv_sort;
    private String uid, sid, api;
    private FrameLayout fl_null, fl_notnull;
    private SESSION session;
    private LeftSlidingListView listview;
    private GoodsListModel dataModel;
    private GoodsDelModel delModel;
    private GoodsOFSaleModel saleModel;
    private GoodsListAdapter listAdapter;
    private GOODS goods;
    private MyDialog myDialog;
    private boolean isEdit = false;
    private TextView tv_sort1, tv_sort2, tv_sort3, tv_sort4;
    private final String SORT1 = "click_desc";
    private final String SORT2 = "click_asc";
    private final String SORT3 = "price_desc";
    private final String SORT4 = "price_asc";
    private int width1, width2;
    private PAGINATED paginated;
    private TextView top_view_text2;
    private ImageView center_view;
    private LinearLayout ll_center, ll_filter_all;
    private SharedPreferences share;
    private SharedPreferences.Editor editor;
    private boolean openflag = false;
    private boolean catflag = false;
    private String tvcont, title;
    private TextView tv_filter_name;
    private ImageView iv_filter_all;
    private LinearLayout ll_search, topview, searchlayout_in;
    private View searchlayout_bg;
    private FrameLayout searchLayout;
    private TextView et_goods_search;
    private View ll_search_bottom;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        EventBus.getDefault().register(this);
        view = inflater.inflate(R.layout.mygoods, null);//加载整个Fragment布局
        shared = getActivity().getSharedPreferences("userInfo", 0);
        uid = shared.getString("uid", "");
        sid = shared.getString("sid", "");
        api = shared.getString("shopapi", "");

        session = new SESSION();
        session.setUid(uid);
        session.setSid(sid);

        width1 = (int) getActivity().getResources().getDimension(R.dimen.dim150);
        width2 = (int) getActivity().getResources().getDimension(R.dimen.dim300);

        KEYWORDS = "";

        if (null == dataModel) {
            dataModel = new GoodsListModel(getActivity());
            dataModel.addResponseListener(this);
        }
        if (null == delModel) {
            delModel = new GoodsDelModel(getActivity());
            delModel.addResponseListener(this);
        }
        if (null == saleModel) {
            saleModel = new GoodsOFSaleModel(getActivity());
            saleModel.addResponseListener(this);
        }
        initView();
        if (TYPE == 0) {
            TYPE = 1;
            SORT = SORT1;
        } else {
            listview.flag = 0;
            refreshUI(TYPE);
        }
        addAdapter();

        return view;
    }

    private void addAdapter() {
        if (null == listAdapter) {
            listAdapter = new GoodsListAdapter(getActivity(), dataModel.goodsList, TYPE, 1, listview.getRightViewWidth());
            listview.setAdapter(listAdapter);
        } else {
            listview.setAdapter(listAdapter);
            listAdapter.lists = dataModel.goodsList;
            listAdapter.type = TYPE;
            listAdapter.notifyDataSetChanged();

        }

        listAdapter.setOnRightItemClickListener(new GoodsListAdapter.onRightItemClickListener() {
            @Override
            public void onRightItemClick(View v, final int position) {
                if (v.getId() == R.id.selecter_item_left) {
                    startActivity(new Intent(getActivity(), WebViewActivity.class)
                    .putExtra("webUrl","http://www.sjqhz.cn/mobile/index.php?r=goods/index/detail&id="+dataModel.goodsList.get(position).getId())
                    .putExtra("webtitle","商品详情"));
                    getActivity().overridePendingTransition(R.anim.push_right_in,
                                                            R.anim.push_right_out);
//                    if (listview.isIsShown()) {
//                        Intent intent = new Intent(getActivity(), GoodDetailActivity.class);
//                        intent.putExtra("id", dataModel.goodsList.get(position).getId());
//                        intent.putExtra("type", TYPE);
//                        startActivity(intent);
//                        getActivity().overridePendingTransition(R.anim.push_right_in,
//                                R.anim.push_right_out);
//                    }
                }
                if (v.getId() == R.id.ll_delete) {
                    String tips = getActivity().getResources().getString(R.string.tip);
                    String tips_content = getActivity().getResources().getString(R.string.tips_content_del);
                    myDialog = new MyDialog(getActivity(), tips, tips_content);
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
                            delModel.GoodsDel(session, dataModel.goodsList.get(position).getId(), api);
                            dataModel.goodsList.remove(position);
                            listview.hiddenRight(LeftSlidingListView.mCurrentItemView);
                            listview.flag = 0;
                            myDialog.dismiss();
                        }
                    });

                }
                if (v.getId() == R.id.ll_sale) {
                    String tips = getActivity().getResources().getString(R.string.tip);
                    String tips_content = "";
                    if (TYPE == 1) {
                        tips_content = getActivity().getResources().getString(R.string.tips_content_off_sale);
                    } else if (TYPE == 2) {
                        tips_content = getActivity().getResources().getString(R.string.tips_content_on_sale);
                    }
                    myDialog = new MyDialog(getActivity(), tips, tips_content);
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
                            saleModel.GoodsSale(session, dataModel.goodsList.get(position).getId(), getFType(TYPE), api);
                            dataModel.goodsList.remove(position);
                            listview.hiddenRight(LeftSlidingListView.mCurrentItemView);
                            listview.flag = 0;
                            //                            listAdapter.lists = AppConst.shopcarlist;
                            //                            listAdapter.notifyDataSetChanged();
                            myDialog.dismiss();
                        }
                    });

                }
            }
        });
        listview.setAdapter(listAdapter);

        listview.setPullLoadEnable(false);
        listview.setPullRefreshEnable(true);
        listview.setXListViewListener(this, 0);
        listview.setRefreshTime();
    }

    private void initView() {
        fl_null = (FrameLayout) view.findViewById(R.id.fl_null);
        fl_notnull = (FrameLayout) view.findViewById(R.id.fl_notnull);

        tv_filter_name = (TextView) view.findViewById(R.id.tv_filter_name);
        iv_filter_all = (ImageView) view.findViewById(R.id.iv_filter_all);
        ll_filter_all = (LinearLayout) view.findViewById(R.id.ll_filter_all);

        ll_bottom = (LinearLayout) view.findViewById(R.id.ll_bottom);
        ll_center = (LinearLayout) view.findViewById(R.id.ll_center);
        ll_center.setOnClickListener(this);
        ll_sale = (LinearLayout) view.findViewById(R.id.ll_sale);
        ll_sale.setOnClickListener(this);
        ll_top_bg = (LinearLayout) view.findViewById(R.id.ll_top_bg);
        ll_delete = (LinearLayout) view.findViewById(R.id.ll_delete);
        ll_delete.setOnClickListener(this);

        listview = (LeftSlidingListView) view.findViewById(R.id.listview);

        rl_1 = (RelativeLayout) view.findViewById(R.id.rl_1);
        rl_2 = (RelativeLayout) view.findViewById(R.id.rl_2);
        rl_3 = (RelativeLayout) view.findViewById(R.id.rl_3);
        rl_4 = (RelativeLayout) view.findViewById(R.id.rl_4);

        rl_1.setOnClickListener(this);
        rl_2.setOnClickListener(this);
        rl_3.setOnClickListener(this);
        rl_4.setOnClickListener(this);

        tv_1 = (TextView) view.findViewById(R.id.tv_1);
        tv_2 = (TextView) view.findViewById(R.id.tv_2);
        tv_3 = (TextView) view.findViewById(R.id.tv_3);
        tv_ofsale = (TextView) view.findViewById(R.id.tv_ofsale);

        line_1 = (View) view.findViewById(R.id.line_1);
        line_2 = (View) view.findViewById(R.id.line_2);
        line_3 = (View) view.findViewById(R.id.line_3);

        top_view_text = (TextView) view.findViewById(R.id.top_view_text);
        top_view_text.setText(getActivity().getResources().getString(R.string.my_goods));
        top_view_text2 = (TextView) view.findViewById(R.id.top_view_text2);

        top_right_tv = (TextView) view.findViewById(R.id.top_right_tv);
        top_right_tv.setText(getActivity().getResources().getString(R.string.edit));
        top_right_tv.setOnClickListener(this);

        iv_sale = (ImageView) view.findViewById(R.id.iv_sale);
        iv_sort = (ImageView) view.findViewById(R.id.iv_sort);

        center_view = (ImageView) view.findViewById(R.id.center_view);
        top_view_back = (ImageView) view.findViewById(R.id.top_view_back);
        top_view_back.setOnClickListener(this);

        ll_search = (LinearLayout) view.findViewById(R.id.ll_search);
        searchlayout_bg = (View) view.findViewById(R.id.fragment_goods_searchlayout_bg);
        et_goods_search = (TextView) view.findViewById(R.id.et_goods_search);
        searchlayout_in = (LinearLayout) view.findViewById(R.id.fragment_goods_searchlayout_in);
        topview = (LinearLayout) view.findViewById(R.id.goods_search_topview);
        ll_search_bottom = (View) view.findViewById(R.id.ll_search_bottom);
        searchLayout = (FrameLayout) view.findViewById(R.id.fragment_goods_searchlayout);
        searchLayout.setOnClickListener(this);

        if (0 != CATEGORYID) {
            top_view_text2.setVisibility(View.VISIBLE);
            top_view_text2.setText(tvcont);
            top_view_text.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        } else {
            top_view_text2.setVisibility(View.GONE);
            top_view_text2.setText("");
            top_view_text.setGravity(Gravity.CENTER);
        }
    }


    //添加数据
    private void getData() {
        for (int i = 0; i < dataModel.goodsList.size(); i++) {
            dataModel.goodsList.get(i).setChoose(false);
        }
    }


    // 删除商品
    private void deletegoodsitems() {

        for (int i = 0; i < dataModel.goodsList.size(); i++) {
            if (dataModel.goodsList.get(i).isChoose() == true) {
                goods = dataModel.goodsList.get(i);
                delModel.GoodsDel(session, goods.getId(), api);
                dataModel.goodsList.remove(i);
                i--;
            }
        }

    }

    // 上下架商品
    private void salegoodsitems() {

        for (int i = 0; i < dataModel.goodsList.size(); i++) {
            if (dataModel.goodsList.get(i).isChoose() == true) {
                goods = dataModel.goodsList.get(i);
                saleModel.GoodsSale(session, goods.getId(), getFType(TYPE), api);
                dataModel.goodsList.remove(i);
                i--;
            }
        }
        listAdapter.lists = dataModel.goodsList;
        listAdapter.notifyDataSetChanged();

    }

    @Override
    public void onRefresh(int id) {
        dataModel.fetchPreSearch(session, getFSale(TYPE), getFStock(TYPE), SORT, KEYWORDS, CATEGORYID, api, false, false);
    }

    @Override
    public void onLoadMore(int id) {
        dataModel.fetchPreSearchMore(session, getFSale(TYPE), getFStock(TYPE), SORT, KEYWORDS, CATEGORYID, api, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        dataModel.fetchPreSearch(session, getFSale(TYPE), getFStock(TYPE), SORT, KEYWORDS, CATEGORYID, api, true, false);
        if (dataModel.categories == null || dataModel.categories.size() == 0) {
            dataModel.getGoodsCategory(session, api);
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
        if (dataModel.goodsList.size() == 0) {
            fl_null.setVisibility(View.VISIBLE);
            fl_notnull.setVisibility(View.GONE);
            top_right_tv.setClickable(false);
            rl_4.setClickable(false);
            if (TextUtils.isEmpty(top_view_text2.getText().toString())) {
                ll_center.setClickable(false);
            }
        } else {
            fl_null.setVisibility(View.GONE);
            fl_notnull.setVisibility(View.VISIBLE);
            top_right_tv.setClickable(true);
            rl_4.setClickable(true);
            ll_center.setClickable(true);
        }
    }

    private void showPopupWindow(View view) {

        View contentView = LayoutInflater.from(getActivity()).inflate(
                R.layout.goods_sort, null);
        LinearLayout bottom_area = (LinearLayout) contentView.findViewById(R.id.bottom_area);
        tv_sort1 = (TextView) contentView.findViewById(R.id.tv_sort1);
        tv_sort2 = (TextView) contentView.findViewById(R.id.tv_sort2);
        tv_sort3 = (TextView) contentView.findViewById(R.id.tv_sort3);
        tv_sort4 = (TextView) contentView.findViewById(R.id.tv_sort4);

        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);

        refreshPopUI(SORT);

        tv_sort1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!SORT.equals(SORT1)) {
                    SORT = SORT1;
                    refreshPopUI(SORT);
                    popupWindow.dismiss();
                    dataModel.fetchPreSearch(session, getFSale(TYPE), getFStock(TYPE), SORT, KEYWORDS, CATEGORYID, api, true, false);
                    listview.setSelection(0);
                }
            }
        });
        tv_sort2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!SORT.equals(SORT2)) {
                    SORT = SORT2;
                    refreshPopUI(SORT);
                    popupWindow.dismiss();
                    dataModel.fetchPreSearch(session, getFSale(TYPE), getFStock(TYPE), SORT, KEYWORDS, CATEGORYID, api, true, false);
                    listview.setSelection(0);
                }
            }
        });
        tv_sort3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!SORT.equals(SORT3)) {
                    SORT = SORT3;
                    refreshPopUI(SORT);
                    popupWindow.dismiss();
                    dataModel.fetchPreSearch(session, getFSale(TYPE), getFStock(TYPE), SORT, KEYWORDS, CATEGORYID, api, true, false);
                    listview.setSelection(0);
                }
            }
        });
        tv_sort4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!SORT.equals(SORT4)) {
                    SORT = SORT4;
                    refreshPopUI(SORT);
                    popupWindow.dismiss();
                    dataModel.fetchPreSearch(session, getFSale(TYPE), getFStock(TYPE), SORT, KEYWORDS, CATEGORYID, api, true, false);
                    listview.setSelection(0);
                }
            }
        });
        bottom_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ll_top_bg.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
                iv_sort.setImageResource(R.drawable.sort_off);
            }
        });

        ColorDrawable dw = new ColorDrawable(0x90000000);
        popupWindow.setBackgroundDrawable(dw);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAsDropDown(view);

    }

    private void showTopPopupWindow(View view) {

        View contentView = LayoutInflater.from(getActivity()).inflate(
                R.layout.goods_sort, null);
        LinearLayout bottom_area = (LinearLayout) contentView.findViewById(R.id.bottom_area);

        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);

        bottom_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ll_top_bg.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
                iv_sort.setImageResource(R.drawable.sort_off);
            }
        });

        ColorDrawable dw = new ColorDrawable(0x90000000);
        popupWindow.setBackgroundDrawable(dw);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAsDropDown(view);

    }

    private void refreshPopUI(String sort) {
        if (sort.equals(SORT1)) {
            tv_sort1.setTextColor(getActivity().getResources().getColor(R.color.bg_theme_color));
            tv_sort2.setTextColor(getActivity().getResources().getColor(R.color.text_login_hint_color));
            tv_sort3.setTextColor(getActivity().getResources().getColor(R.color.text_login_hint_color));
            tv_sort4.setTextColor(getActivity().getResources().getColor(R.color.text_login_hint_color));

        } else if (sort.equals(SORT2)) {
            tv_sort1.setTextColor(getActivity().getResources().getColor(R.color.text_login_hint_color));
            tv_sort2.setTextColor(getActivity().getResources().getColor(R.color.bg_theme_color));
            tv_sort3.setTextColor(getActivity().getResources().getColor(R.color.text_login_hint_color));
            tv_sort4.setTextColor(getActivity().getResources().getColor(R.color.text_login_hint_color));

        } else if (sort.equals(SORT3)) {
            tv_sort1.setTextColor(getActivity().getResources().getColor(R.color.text_login_hint_color));
            tv_sort2.setTextColor(getActivity().getResources().getColor(R.color.text_login_hint_color));
            tv_sort3.setTextColor(getActivity().getResources().getColor(R.color.bg_theme_color));
            tv_sort4.setTextColor(getActivity().getResources().getColor(R.color.text_login_hint_color));

        } else if (sort.equals(SORT4)) {
            tv_sort1.setTextColor(getActivity().getResources().getColor(R.color.text_login_hint_color));
            tv_sort2.setTextColor(getActivity().getResources().getColor(R.color.text_login_hint_color));
            tv_sort3.setTextColor(getActivity().getResources().getColor(R.color.text_login_hint_color));
            tv_sort4.setTextColor(getActivity().getResources().getColor(R.color.bg_theme_color));

        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_center:
                if (openflag || !catflag) {


                } else {
                    openflag = true;
                    center_view.setImageResource(R.drawable.goods_filter_up);
                    top_right_tv.setVisibility(View.GONE);
                    searchLayout.setVisibility(View.GONE);
                    Intent intent = new Intent(getActivity(), GoodsFilterActivity.class);
                    intent.putExtra("outid", CATEGORYID);
                    intent.putExtra("title", title);
                    intent.putExtra("fromlist", true);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out);
                }

                break;
            case R.id.rl_1:
                if (TYPE != 1) {
                    TYPE = 1;
                    listAdapter.type = TYPE;
                    refreshUI(TYPE);
                    dataModel.fetchPreSearch(session, getFSale(TYPE), getFStock(TYPE), SORT, KEYWORDS, CATEGORYID, api, true, false);
                }
                break;
            case R.id.rl_2:
                if (TYPE != 2) {
                    TYPE = 2;
                    listAdapter.type = TYPE;
                    refreshUI(TYPE);
                    dataModel.fetchPreSearch(session, getFSale(TYPE), getFStock(TYPE), SORT, KEYWORDS, CATEGORYID, api, true, false);
                }
                break;
            case R.id.rl_3:
                if (TYPE != 3) {
                    TYPE = 3;
                    listAdapter.type = TYPE;
                    refreshUI(TYPE);
                    dataModel.fetchPreSearch(session, getFSale(TYPE), getFStock(TYPE), SORT, KEYWORDS, CATEGORYID, api, true, false);
                }
                break;
            case R.id.rl_4:
                showPopupWindow(view);
                ll_top_bg.setBackgroundColor(getActivity().getResources().getColor(R.color.newitem_press));
                iv_sort.setImageResource(R.drawable.sort_on);
                break;
            case R.id.top_view_back:
                getActivity().finish();
                if (TabsFragment.getInstance().isbeHidden()) {
                    closeEdit();
                }
                TabsFragment.getInstance().toMyFragment(1, "");
                break;
            case R.id.ll_delete:
                if (haveselect()) {
                    String tips = getActivity().getResources().getString(R.string.tip);
                    String tips_content = getActivity().getResources().getString(R.string.tips_content_del);
                    myDialog = new MyDialog(getActivity(), tips, tips_content);
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
                            deletegoodsitems();
                            closeEdit();
                            myDialog.dismiss();
                        }
                    });
                } else {
                    ToastView toast = new ToastView(getActivity(), getActivity().getResources().getString(R.string.no_select));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.setDuration(200);
                    toast.show();
                }

                break;
            case R.id.ll_sale:
                if (haveselect()) {
                    String tip = getActivity().getResources().getString(R.string.tip);
                    String tip_content = "";
                    if (TYPE == 1) {
                        tip_content = getActivity().getResources().getString(R.string.tips_content_off_sale);
                    } else if (TYPE == 2) {
                        tip_content = getActivity().getResources().getString(R.string.tips_content_on_sale);
                    }
                    myDialog = new MyDialog(getActivity(), tip, tip_content);
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
                            salegoodsitems();
                            closeEdit();
                            myDialog.dismiss();
                        }
                    });
                } else {
                    ToastView toast = new ToastView(getActivity(), getActivity().getResources().getString(R.string.no_select));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.setDuration(200);
                    toast.show();
                }
                break;
            case R.id.top_right_tv:
                String done = getActivity().getResources().getString(R.string.done);

                if (!isEdit) {
                    listAdapter.flag = 2;
                    listAdapter.notifyDataSetChanged();
                    listview.setPullRefreshEnable(false);
                    listview.setPullLoadEnable(false);
                    ll_center.setClickable(false);
                    rl_1.setClickable(false);
                    rl_2.setClickable(false);
                    rl_3.setClickable(false);
                    rl_4.setClickable(false);
                    searchLayout.setVisibility(View.GONE);
                    ll_search_bottom.setVisibility(View.GONE);
                    LeftSlidingListView.isleftsliding = true;
                    listview.hiddenRight(LeftSlidingListView.mCurrentItemView);
                    listview.flag = 0;
                    isEdit = true;
                    top_right_tv.setText(done);
                    ll_bottom.setVisibility(View.VISIBLE);
                    //                      TabsFragment.getInstance().hidden(false);
                    MyGoodsParentActivity parentActivity = (MyGoodsParentActivity) getActivity();
                    parentActivity.setBottomShow(false);
                } else {
                    closeEdit();
                }
                break;
            case R.id.fragment_goods_searchlayout:
                ll_search_bottom.setVisibility(View.GONE);
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
                        intent.putExtra("type", "goods");
                        intent.putExtra("KEYWORDS", KEYWORDS);
                        startActivityForResult(intent, 102);
                        getActivity().overridePendingTransition(R.anim.animation_2, R.anim.animation_1);
                    }
                });
                ll_search.startAnimation(animation);
                searchlayout_bg.startAnimation(animation1);
                searchlayout_in.startAnimation(animation2);
                break;
        }
    }

    private void closeEdit() {
        listAdapter.flag = 1;
        listview.setPullRefreshEnable(true);
        if (0 == paginated.getMore()) {
            listview.setPullLoadEnable(false);
        } else {
            listview.setPullLoadEnable(true);
        }
        getData();
        ll_center.setClickable(true);
        rl_1.setClickable(true);
        rl_2.setClickable(true);
        rl_3.setClickable(true);
        rl_4.setClickable(true);
        LeftSlidingListView.isleftsliding = false;
        top_right_tv.setText(getActivity().getResources().getString(R.string.edit));
        searchLayout.setVisibility(View.VISIBLE);
        ll_search_bottom.setVisibility(View.VISIBLE);
        isEdit = false;
        ll_bottom.setVisibility(View.GONE);
        //        TabsFragment.getInstance().hidden(true);
        MyGoodsParentActivity parentActivity = (MyGoodsParentActivity) getActivity();
        parentActivity.setBottomShow(true);
        listAdapter.notifyDataSetChanged();
    }

    private String getFSale(int type) {
        String fsale = "true";
        switch (type) {
            case 1:
                fsale = "true";
                break;
            case 2:
                fsale = "false";
                break;
            case 3:
                fsale = "";
                break;
        }
        return fsale;
    }

    private String getFStock(int type) {
        String fstock = "true";
        switch (type) {
            case 1:
                fstock = "true";
                break;
            case 2:
                fstock = "true";
                break;
            case 3:
                fstock = "false";
                break;
        }
        return fstock;
    }

    private String getFType(int type) {
        String fstype = "";
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


    private void refreshUI(int type) {
        switch (type) {
            case 1:
                tv_1.setTextColor(getActivity().getResources().getColor(R.color.bg_theme_color));
                tv_2.setTextColor(getActivity().getResources().getColor(R.color.text_login_hint_color));
                tv_3.setTextColor(getActivity().getResources().getColor(R.color.text_login_hint_color));
                line_1.setVisibility(View.VISIBLE);
                line_2.setVisibility(View.INVISIBLE);
                line_3.setVisibility(View.INVISIBLE);
                ll_sale.setVisibility(View.VISIBLE);
                listview.setRightViewWidth(width2+width1);
                tv_ofsale.setText(getActivity().getResources().getString(R.string.off_sale));
                iv_sale.setImageResource(R.drawable.blue_offsale);
                break;
            case 2:
                tv_1.setTextColor(getActivity().getResources().getColor(R.color.text_login_hint_color));
                tv_2.setTextColor(getActivity().getResources().getColor(R.color.bg_theme_color));
                tv_3.setTextColor(getActivity().getResources().getColor(R.color.text_login_hint_color));
                line_1.setVisibility(View.INVISIBLE);
                line_2.setVisibility(View.VISIBLE);
                line_3.setVisibility(View.INVISIBLE);
                ll_sale.setVisibility(View.VISIBLE);
                listview.setRightViewWidth(width2+width1);
                tv_ofsale.setText(getActivity().getResources().getString(R.string.to_on_sale));
                iv_sale.setImageResource(R.drawable.blue_onsale);
                break;
            case 3:
                tv_1.setTextColor(getActivity().getResources().getColor(R.color.text_login_hint_color));
                tv_2.setTextColor(getActivity().getResources().getColor(R.color.text_login_hint_color));
                tv_3.setTextColor(getActivity().getResources().getColor(R.color.bg_theme_color));
                listview.setRightViewWidth(width1);
                line_1.setVisibility(View.INVISIBLE);
                line_2.setVisibility(View.INVISIBLE);
                line_3.setVisibility(View.VISIBLE);
                ll_sale.setVisibility(View.INVISIBLE);
                break;
        }
    }

    private boolean haveselect() {
        for (int i = 0; i < dataModel.goodsList.size(); i++) {
            if (dataModel.goodsList.get(i).isChoose() == true) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void onEvent(MyEvent event) {
        if ("GOODSDEL".equals(event.getMsg())) {
            dataModel.fetchPreSearch(session, getFSale(TYPE), getFStock(TYPE), SORT, KEYWORDS, CATEGORYID, api, true, false);
        } else if ("ONLINE".equals(event.getMsg())) {
            TYPE = 1;
            refreshUI(TYPE);
            dataModel.fetchPreSearch(session, getFSale(TYPE), getFStock(TYPE), SORT, KEYWORDS, CATEGORYID, api, true, false);
        } else if ("OFFLINE".equals(event.getMsg())) {
            TYPE = 2;
            refreshUI(TYPE);
            dataModel.fetchPreSearch(session, getFSale(TYPE), getFStock(TYPE), SORT, KEYWORDS, CATEGORYID, api, true, false);
        } else if ("PRICEREFRESH".equals(event.getMsg())) {
            dataModel.fetchPreSearch(session, getFSale(TYPE), getFStock(TYPE), SORT, KEYWORDS, CATEGORYID, api, true, false);
        } else if ("NEEDCLOSE".equals(event.getMsg())) {
            if (TabsFragment.getInstance().isbeHidden()) {
                closeEdit();
            }
        } else if (AppConst.CATEGORYT == event.getmTag()) {
            ll_filter_all.setVisibility(View.GONE);
            searchLayout.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(event.getMsg())) {
                String s = event.getMsg();
                String[] sp = s.split("===");
                tvcont = sp[0];
                if (!TextUtils.isEmpty(tvcont)) {
                    top_view_text2.setVisibility(View.VISIBLE);
                    top_view_text2.setText(tvcont);
                    top_view_text.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
                } else {
                    top_view_text2.setVisibility(View.GONE);
                    top_view_text2.setText("");
                    top_view_text.setGravity(Gravity.CENTER);
                }
                int cid = Integer.parseInt(sp[1]);
                CATEGORYID = cid;
                title = tv_filter_name.getText().toString();
                dataModel.fetchPreSearch(session, getFSale(TYPE), getFStock(TYPE), SORT, KEYWORDS, CATEGORYID, api, true, false);
            }
            openflag = false;
            center_view.setImageResource(R.drawable.goods_filter_down);
            top_right_tv.setVisibility(View.VISIBLE);
        } else if (AppConst.NEWTITLE == event.getmTag()) {
            String tcontent = event.getMsg();
            tcontent = tcontent.trim();
            tv_filter_name.setText(event.getMsg());
            if (tcontent.length() > 4) {
                tv_filter_name.setTextColor(getActivity().getResources().getColor(R.color.bg_theme_color));
                iv_filter_all.setImageResource(R.drawable.filter_all_active);
            } else {
                tv_filter_name.setTextColor(getActivity().getResources().getColor(R.color.text_login_color));
                iv_filter_all.setImageResource(R.drawable.filter_all);
            }
        } else if ("FILTERVIS".equals(event.getMsg())) {
            ll_filter_all.setVisibility(View.VISIBLE);
            searchLayout.setVisibility(View.GONE);
        }


    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 100 && requestCode == 102) {
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
            ll_search_bottom.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void OnMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if (url.equals(ProtocolConst.GOODS)) {
            if (status.getSucceed() == 1) {
                getData();
                listview.stopRefresh();
                listview.stopLoadMore();
                listview.setRefreshTime();
                listview.hiddenRight(LeftSlidingListView.mCurrentItemView);
                listview.flag = 0;
                paginated = dataModel.paginated;
                if (0 == paginated.getMore()) {
                    listview.setPullLoadEnable(false);
                } else {
                    listview.setPullLoadEnable(true);
                }
                listAdapter.type = TYPE;
                listAdapter.notifyDataSetChanged();
                checkNull();
            }
        } else if (url.equals(ProtocolConst.GOODSDEL)) {
            if (status.getSucceed() == 1) {
                listAdapter.lists = dataModel.goodsList;
                listAdapter.notifyDataSetChanged();
                checkNull();
            }
        } else if (url.equals(ProtocolConst.GOODSSALE)) {
            if (status.getSucceed() == 1) {
                listAdapter.lists = dataModel.goodsList;
                listAdapter.notifyDataSetChanged();
                checkNull();
            }
        } else if (url.equals(ProtocolConst.GOODSCATEGORY)) {
            if (status.getSucceed() == 1) {
                share = getActivity().getSharedPreferences("CATEGORY", 0);
                editor = share.edit();
                JSONObject localItemObject = new JSONObject();
                JSONArray itemJSONArray = new JSONArray();
                for (int i = 0; i < dataModel.categories.size(); i++) {
                    CATEGORY itemData = dataModel.categories.get(i);
                    JSONObject itemJSONObject = itemData.toJson();
                    itemJSONArray.put(itemJSONObject);
                }
                localItemObject.put("category", itemJSONArray);

                editor.putString("data", localItemObject.toString());
                editor.commit();
                catflag = true;
            }
        }
    }
}
