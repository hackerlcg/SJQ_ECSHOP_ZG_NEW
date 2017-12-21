package com.ecjia.hamster.activity;

import android.content.Context;
import android.content.Intent;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import com.ecjia.base.BaseActivity;
import com.ecjia.component.network.model.GoodsListModel;
import com.ecjia.component.network.model.OrdersDetailModel;
import com.ecjia.component.network.model.OrdersListModel;
import com.ecjia.component.network.model.PromotionModel;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.component.view.ToastView;
import com.ecjia.component.view.XListView;
import com.ecjia.consts.AppConst;
import com.ecjia.hamster.adapter.GoodsSearchListAdapter;
import com.ecjia.hamster.adapter.OrdersSearchListAdapter;
import com.ecjia.hamster.adapter.PromotionListAdapter;
import com.ecmoban.android.shopkeeper.sijiqing.R;


import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;


import com.ecjia.hamster.model.HttpResponse;
import com.ecjia.hamster.model.PAGINATED;
import com.ecjia.hamster.model.STATUS;
import com.ecjia.util.FormatUtil;
import com.ecjia.util.TimeUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class SearchActivity extends BaseActivity implements OnClickListener,XListView.IXListViewListener,HttpResponse {

    private EditText search_input;
    private String KEYWORDS,TYPE;
    private LinearLayout ll_search_back;
    private TextView tv_search_cancel;
    private FrameLayout fl_goods_null,fl_order_null,fl_promotion_null;
    private View bg_w_color;
    private XListView listView;
    private GoodsSearchListAdapter goodsAdapter;
    private OrdersSearchListAdapter ordersAdapter;
    private PromotionListAdapter promotionsAdapter;
    private PromotionModel promotionModel;
    private GoodsListModel goodsListModel;
    private OrdersListModel ordersListModel;
    private PAGINATED paginated;
    private OrdersDetailModel detailModel;
    private String goods_id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_new);
        KEYWORDS=getIntent().getStringExtra("KEYWORDS");
        TYPE=getIntent().getStringExtra("type");

        if("order".equals(TYPE)){
            ordersListModel=new OrdersListModel(this);
            ordersListModel.addResponseListener(this);
            detailModel=new OrdersDetailModel(this);
            detailModel.addResponseListener(this);
        }else if("goods".equals(TYPE)){
            goodsListModel=new GoodsListModel(this);
            goodsListModel.addResponseListener(this);
        }else if("pgoods".equals(TYPE)){
            goods_id=getIntent().getStringExtra("goods_id");
            goodsListModel=new GoodsListModel(this);
            goodsListModel.addResponseListener(this);
        }else if("promotion".equals(TYPE)){
            promotionModel=new PromotionModel(this);
            promotionModel.addResponseListener(this);
        }

        initView();

    }


    private void initView() {
        search_input = (EditText) findViewById(R.id.et_search_input);
        listView=(XListView)findViewById(R.id.search_listview);

        bg_w_color=(View)findViewById(R.id.bg_w_color);
        fl_goods_null=(FrameLayout)findViewById(R.id.fl_goods_null);
        fl_promotion_null=(FrameLayout)findViewById(R.id.fl_promotion_null);
        fl_order_null=(FrameLayout)findViewById(R.id.fl_order_null);

        if("order".equals(TYPE)){
            search_input.setInputType(InputType.TYPE_CLASS_NUMBER);
            search_input.setHint(res.getString(R.string.search_input_orders));
            ordersAdapter=new OrdersSearchListAdapter(this,ordersListModel.ordersList);
            listView.setAdapter(ordersAdapter);
        }else if("goods".equals(TYPE)||"pgoods".equals(TYPE)){
            goodsAdapter=new GoodsSearchListAdapter(this,goodsListModel.goodsList);
            listView.setAdapter(goodsAdapter);
        }else if("promotion".equals(TYPE)){
            promotionsAdapter=new PromotionListAdapter(this,promotionModel.goodsList,0);
            listView.setAdapter(promotionsAdapter);
        }else if("brands".equals(TYPE)){
            search_input.setHint(res.getString(R.string.search_input_brands));
        }else if("category".equals(TYPE)){
            search_input.setHint(res.getString(R.string.search_input_category));
        }


        ll_search_back = (LinearLayout) findViewById(R.id.ll_search_back);
        ll_search_back.setOnClickListener(this);

        tv_search_cancel = (TextView) findViewById(R.id.tv_search_cancel);
        tv_search_cancel.setOnClickListener(this);

        if(!TextUtils.isEmpty(KEYWORDS)){
            search_input.setText(KEYWORDS);
        }

        search_input.setOnClickListener(this);

        listView.setPullLoadEnable(false);
        listView.setPullRefreshEnable(true);
        listView.setXListViewListener(this, 0);
        listView.setRefreshTime();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    position=position-1;
                }
                if("order".equals(TYPE)){
                    detailModel.fetchOrderDetail(session,ordersListModel.ordersList.get(position).getId(),api);

                }else if("promotion".equals(TYPE)){
                    Intent intent=new Intent(SearchActivity.this, AddPromotionActivity.class);
                    intent.putExtra("TYPE",1);
                    intent.putExtra("goods_id",promotionModel.goodsList.get(position).getId());
                    startActivity(intent);
                }else if("pgoods".equals(TYPE)){
                    if(TextUtils.isEmpty(goods_id)){
                        goods_id="0";
                    }
                    String start=goodsListModel.goodsList.get(position).getFormatted_promote_start_date();
                    String end=goodsListModel.goodsList.get(position).getFormatted_promote_end_date();

                    boolean flag=true;
                    if(TextUtils.isEmpty(start) ||TextUtils.isEmpty(end)){
                        flag=false;
                    }else{
                        switch (TimeUtil.compare_promotion(start,end)){
                            case 0:
                                flag=false;
                                break;
                            case 1:
                                flag=true;
                                break;
                            case 2:
                                flag=true;
                                break;
                            case 3:
                                flag=true;
                                break;
                        }
                    }

                    if(goods_id.equals(goodsListModel.goodsList.get(position).getId())){
                        ToastView toast = new ToastView(SearchActivity.this, res.getString(R.string.search_promotion_tips));
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }else if(flag){
                        ToastView toast = new ToastView(SearchActivity.this, res.getString(R.string.search_promotion_tips2));
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }else{
                        Intent intent=new Intent();
                        intent.putExtra("goods_id",goodsListModel.goodsList.get(position).getId());
                        intent.putExtra("goods_name",goodsListModel.goodsList.get(position).getName());
                        intent.putExtra("shop_price", FormatUtil.formatToDigetPrice(goodsListModel.goodsList.get(position).getShop_price()));
                        setResult(99,intent);
                        finish();
                    }
                }else{
                    Intent intent=new Intent(SearchActivity.this, GoodDetailActivity.class);
                    intent.putExtra("id",goodsListModel.goodsList.get(position).getId());
                    startActivity(intent);
                }
            }
        });


        search_input.setFocusable(true);
        search_input.setFocusableInTouchMode(true);
        search_input.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) search_input.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(search_input, 0);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

                           public void run() {
                               InputMethodManager inputManager =
                                       (InputMethodManager) search_input.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                               inputManager.showSoftInput(search_input, 0);
                           }

                       },
                300);

        search_input.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                // TODO Auto-generated method stub
                String please_input = res.getString(R.string.search_please_input);
                CloseKeyBoard();
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String inputstr = search_input.getText().toString();
                    inputstr =inputstr.replaceAll("\\s*", "");
                    KEYWORDS=inputstr;
                    if("order".equals(TYPE)){
                        ordersListModel.fetchPreSearch(session,"",KEYWORDS,api,true);
                    }else if("goods".equals(TYPE)||"pgoods".equals(TYPE)){
                        goodsListModel.fetchPreSearch(session,"","","click_desc",KEYWORDS,0,api,true,false);
                    }else if("promotion".equals(TYPE)){
                        promotionModel.getPromotionList("", KEYWORDS, api, true);
                    }else if("gifts".equals(TYPE)||"brands".equals(TYPE)||"category".equals(TYPE)){
                        Intent intent=new Intent();
                        intent.putExtra("KEYWORDS",KEYWORDS);
                        setResult(100,intent);
                        CloseKeyBoard();
                        finish();
                    }
                }
                return false;
            }
        });


    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.animation_4, 0);
    }

    @Override
    public void onClick(View view) {

        Intent intent=new Intent();
        switch (view.getId()) {
            case R.id.tv_search_cancel:
                if("gifts".equals(TYPE)||"brands".equals(TYPE)||"category".equals(TYPE)){
                    intent.putExtra("KEYWORDS","");
                }
                setResult(100,intent);
                CloseKeyBoard();
                finish();
                break;
            case R.id.ll_search_back:
                if("gifts".equals(TYPE)||"brands".equals(TYPE)||"category".equals(TYPE)){
                    setResult(99,intent);
                }else{
                    setResult(100,intent);
                }
                CloseKeyBoard();
                finish();
                break;

            default:
                break;
        }

    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }


    // 关闭键盘
    public void CloseKeyBoard() {
        search_input.clearFocus();
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(search_input.getWindowToken(), 0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if("gifts".equals(TYPE)||"brands".equals(TYPE)||"category".equals(TYPE)){
                setResult(99);
            }else{
                setResult(100);
            }
              CloseKeyBoard();
              finish();
        }
        return true;
    }


    @Override
    public void onRefresh(int id) {
        if("order".equals(TYPE)){
            ordersListModel.fetchPreSearch(session,"",KEYWORDS,api,false);
        }else if("goods".equals(TYPE)||"pgoods".equals(TYPE)){
            goodsListModel.fetchPreSearch(session,"","","click_desc",KEYWORDS,0,api,false,false);
        }else if("promotion".equals(TYPE)){
            promotionModel.getPromotionList("", KEYWORDS, api, false);
        }
    }

    @Override
    public void onLoadMore(int id) {
        if("order".equals(TYPE)){
            ordersListModel.fetchPreSearchMore(session,"",KEYWORDS,api);
        }else if("goods".equals(TYPE)){
            goodsListModel.fetchPreSearchMore(session,"","","click_desc",KEYWORDS,0,api,false);
        }else if("promotion".equals(TYPE)){
            promotionModel.getPromotionListMore("", KEYWORDS, api);
        }
    }

    @Override
    public void OnMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if (url.equals(ProtocolConst.GOODS)) {
            bg_w_color.setVisibility(View.VISIBLE);
            ll_search_back.setVisibility(View.GONE);
            listView.stopRefresh();
            listView.stopLoadMore();
            listView.setRefreshTime();
            if (status.getSucceed() == 1) {

                if(goodsListModel.goodsList.size()>0) {

                    listView.setVisibility(View.VISIBLE);
                    fl_goods_null.setVisibility(View.GONE);
                    paginated = goodsListModel.paginated;
                    if (0 == paginated.getMore()) {
                        listView.setPullLoadEnable(false);
                    } else {
                        listView.setPullLoadEnable(true);
                    }

                }else{
                    listView.setVisibility(View.GONE);
                    fl_goods_null.setVisibility(View.VISIBLE);
                }
                goodsAdapter.notifyDataSetChanged();
            }
        }else
        if (url.equals(ProtocolConst.ADMIN_PROMOTION_LIST)) {
            bg_w_color.setVisibility(View.VISIBLE);
            ll_search_back.setVisibility(View.GONE);
            listView.stopRefresh();
            listView.stopLoadMore();
            listView.setRefreshTime();
            if (status.getSucceed() == 1) {

                if(promotionModel.goodsList.size()>0) {

                    listView.setVisibility(View.VISIBLE);
                    fl_promotion_null.setVisibility(View.GONE);
                    paginated = promotionModel.paginated;
                    if (0 == paginated.getMore()) {
                        listView.setPullLoadEnable(false);
                    } else {
                        listView.setPullLoadEnable(true);
                    }

                }else{
                    listView.setVisibility(View.GONE);
                    fl_promotion_null.setVisibility(View.VISIBLE);
                }
                promotionsAdapter.notifyDataSetChanged();
            }
        }else
        if (url.equals(ProtocolConst.ORDERS)) {
            bg_w_color.setVisibility(View.VISIBLE);
            ll_search_back.setVisibility(View.GONE);
            listView.stopRefresh();
            listView.stopLoadMore();
            listView.setRefreshTime();
            if (status.getSucceed() == 1) {
                if(ordersListModel.ordersList.size()>0){
                listView.setVisibility(View.VISIBLE);
                    fl_order_null.setVisibility(View.GONE);
                paginated = ordersListModel.paginated;
                if (0 == paginated.getMore()) {
                    listView.setPullLoadEnable(false);
                } else {
                    listView.setPullLoadEnable(true);
                }
                }else {
                    listView.setVisibility(View.GONE);
                    fl_order_null.setVisibility(View.VISIBLE);
                }

            }
            ordersAdapter.notifyDataSetChanged();
        }else
        if (url.equals(ProtocolConst.ORDERDETAIL)) {
            String error13 = res.getString(R.string.error_13);
            String error101 = res.getString(R.string.error_101);

            if (status.getSucceed() == 1) {
                if(detailModel.order.getSuborderses().size()>0){
                    Intent intent = new Intent(SearchActivity.this, SubOrderActivity.class);
                    try {
                        intent.putExtra("data",detailModel.order.toJson().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    intent.putExtra("type", TYPE);
                    intent.putExtra("id", detailModel.order.getId());
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(SearchActivity.this, OrderDetailActivity.class);
                    intent.putExtra("id", detailModel.order.getId());
                    intent.putExtra("type", TYPE);
                    startActivity(intent);
                }

            } else if (status.getSucceed() == AppConst.UnexistInformation) {

                ToastView toast = new ToastView(SearchActivity.this, error13);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }else if (status.getSucceed() == AppConst.InvalidParameter) {

                ToastView toast = new ToastView(SearchActivity.this, error101);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }

    }
}
