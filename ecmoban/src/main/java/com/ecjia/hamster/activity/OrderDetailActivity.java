package com.ecjia.hamster.activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecjia.component.network.model.OrderCancelModel;
import com.ecjia.component.network.model.OrdersDetailModel;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.component.view.CYTextView;
import com.ecjia.component.view.MyDialog;
import com.ecjia.component.view.ToastView;
import com.ecjia.consts.AppConst;
import com.ecjia.hamster.activity.order.DeliverGoodsActivity_Builder;
import com.ecjia.hamster.activity.order.EditOrderPriceActivity_Builder;
import com.ecjia.hamster.adapter.OrderDetailListAdapter;
import com.ecjia.hamster.model.GOODS;
import com.ecjia.hamster.model.HttpResponse;
import com.ecjia.hamster.model.SESSION;
import com.ecjia.util.FormatUtil;
import com.ecjia.util.LG;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.hamster.model.STATUS;
import com.ecjia.util.EventBus.Event;
import com.ecjia.util.EventBus.MyEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import gear.yc.com.gearlibrary.utils.ToastUtil;


public class OrderDetailActivity extends BaseActivity implements HttpResponse {

    private TextView top_view_text, top_right_tv, tv_status;
    private TextView tv_amount, tv_discount, tv_shipping, tv_total, tv_tax,tv_coupons;
    private TextView tv_consignee, tv_contract, tv_postscript, tv_action_num;
    private CYTextView tv_address;
    private ImageView top_view_back;
    private ListView listView;
    private ArrayList<GOODS> list;
    private OrderDetailListAdapter listAdapter;
    private OrdersDetailModel dataModel;
    private OrderCancelModel cancelModel;
    private int type;
    private String id;
    private SharedPreferences shared;
    private String uid;
    private String sid;
    private String api;
    private LinearLayout ll_recinfo, ll_priceinfo, ll_action;
    private SESSION session;
    private MyDialog myDialog;

    private Button btn_operate;

    public OrderDetailActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_orderdetail);
        EventBus.getDefault().register(this);
        shared = this.getSharedPreferences("userInfo", 0);
        uid = shared.getString("uid", "");
        sid = shared.getString("sid", "");
        api = shared.getString("shopapi", "");

        session = new SESSION();
        session.setUid(uid);
        session.setSid(sid);

        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);
        id = intent.getStringExtra("id");

        if (dataModel == null) {
            dataModel = new OrdersDetailModel(this);
            dataModel.addResponseListener(this);
        }


        list = new ArrayList<GOODS>();
        list.addAll(dataModel.order.getGoodslist());
        initView();
        dataModel.fetchOrderDetail(session, id, formatApi(api));
    }

    private void initView() {
        top_view_text = (TextView) findViewById(R.id.top_view_text);
        top_right_tv = (TextView) findViewById(R.id.top_right_tv);
        tv_status = (TextView) findViewById(R.id.tv_status);
        top_view_text.setText(res.getText(R.string.order_detail));
        listView = (ListView) findViewById(R.id.listView);
        btn_operate = (Button) findViewById(R.id.btn_operate);
        top_right_tv.setVisibility(View.GONE);
        if (type == 1) {
            top_right_tv.setText(res.getString(R.string.order_cancel));
            top_right_tv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    String tips = res.getString(R.string.tip);
                    String tips_content = res.getString(R.string.tips_content_cancel);
                    myDialog = new MyDialog(OrderDetailActivity.this, tips, tips_content);
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
                            if (cancelModel == null) {
                                cancelModel = new OrderCancelModel(OrderDetailActivity.this);
                                cancelModel.addResponseListener(OrderDetailActivity.this);
                            }
                            cancelModel.fetchOrderCancel(session, id, api);
                            myDialog.dismiss();
                        }
                    });

                }
            });
        }
        top_view_back = (ImageView) findViewById(R.id.top_view_back);

        top_view_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        ll_recinfo = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.receiveinfo, null);

        tv_consignee = (TextView) ll_recinfo.findViewById(R.id.tv_consignee);
        tv_contract = (TextView) ll_recinfo.findViewById(R.id.tv_contract);
        tv_address = (CYTextView) ll_recinfo.findViewById(R.id.tv_address);
        tv_postscript = (TextView) ll_recinfo.findViewById(R.id.tv_postscript);

//        ll_priceinfo=(LinearLayout) LayoutInflater.from(this).inflate(R.layout.priceinfo, null);

        tv_amount = (TextView) ll_recinfo.findViewById(R.id.tv_amount);
        tv_discount = (TextView) ll_recinfo.findViewById(R.id.tv_discount);
        tv_tax = (TextView) ll_recinfo.findViewById(R.id.tv_tax);
        tv_shipping = (TextView) ll_recinfo.findViewById(R.id.tv_shipping);
        tv_total = (TextView) ll_recinfo.findViewById(R.id.tv_total);
        tv_coupons= (TextView) ll_recinfo.findViewById(R.id.tv_coupons);
        tv_action_num = (TextView) ll_recinfo.findViewById(R.id.tv_action_num);

        ll_action = (LinearLayout) ll_recinfo.findViewById(R.id.ll_action);
        ll_action.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dataModel.order.getActions().size() > 0) {
                    Intent intent = new Intent(OrderDetailActivity.this, ActionLogActivity.class);
                    try {
                        intent.putExtra("data", dataModel.order.toJson().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);
                } else {
                    ToastView toast = new ToastView(OrderDetailActivity.this, res.getString(R.string.action_null));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });


        if (listAdapter == null) {
            listAdapter = new OrderDetailListAdapter(this, list);
        }


        listView.addFooterView(ll_recinfo);
//        listView.addFooterView(ll_priceinfo);
        listView.setAdapter(listAdapter);


    }


    private String formatApi(String api) {
        if (!api.contains("http://")) {
            api = "http://" + api;
        }
        return api;
    }

    // 获取屏幕宽度
    public int getDisplayMetricsWidth() {
        int i = this.getWindowManager().getDefaultDisplay().getWidth();
        int j = this.getWindowManager().getDefaultDisplay()
                .getHeight();
        return Math.min(i, j);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    public void onEvent(Event event) {
        LG.d("返回参数+" + event.getMsg());
        if ("EditOrderPriceActivity".equals(event.getMsg())) {
            dataModel.fetchOrderDetail(session, id, formatApi(api));
        } else if ("DeliverGoodsActivity".equals(event.getMsg())) {
            this.finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void OnMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        String error13 = res.getString(R.string.error_13);
        String error101 = res.getString(R.string.error_101);
        String success = res.getString(R.string.cancel_success);

        if (url.equals(ProtocolConst.ORDERDETAIL)) {
            if (status.getSucceed() == 1) {
                tv_status.setText(dataModel.order.getStatus());
                getFtype(type);
                tv_consignee.setText(dataModel.order.getConsignee());
                tv_contract.setText(dataModel.order.getMobile());
                tv_address.SetText(dataModel.order.getCountry() + dataModel.order.getProvince() + dataModel.order.getCity()
                        + dataModel.order.getDistrict() + dataModel.order.getAddress());

                if (dataModel.order.getActions().size() > 0) {
                    tv_action_num.setText(dataModel.order.getActions().size() + "");
                }

                if (TextUtils.isEmpty(dataModel.order.getPostscript())) {
                    tv_postscript.setText(res.getString(R.string.no));
                } else {
                    tv_postscript.setText(dataModel.order.getPostscript());
                }

                tv_amount.setText(dataModel.order.getAmount());
                if (!TextUtils.isEmpty(dataModel.order.getDiscount())) {
                    tv_discount.setText("-" + dataModel.order.getDiscount());
                }
                if (!TextUtils.isEmpty(dataModel.order.getTax())) {
                    tv_tax.setText("+" + dataModel.order.getTax());
                }
                if (!TextUtils.isEmpty(dataModel.order.getShipping())) {
                    tv_shipping.setText("+" + dataModel.order.getShipping());
                }
                tv_total.setText(dataModel.order.getTotal());
                tv_coupons.setText("-"+dataModel.order.getFormated_coupons());

                list.clear();
                list.addAll(dataModel.order.getGoodslist());

                listAdapter.notifyDataSetChanged();

            } else if (status.getSucceed() == AppConst.UnexistInformation) {

                ToastView toast = new ToastView(OrderDetailActivity.this, error13);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else if (status.getSucceed() == AppConst.InvalidParameter) {

                ToastView toast = new ToastView(OrderDetailActivity.this, error101);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }

        if (url.equals(ProtocolConst.ORDERCANCEL)) {
            if (status.getSucceed() == 1) {
                ToastView toast = new ToastView(OrderDetailActivity.this, success);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                EventBus.getDefault().post(new MyEvent("ORDERCANCEL"));
                finish();

            } else if (status.getSucceed() == AppConst.InvalidParameter) {
                ToastView toast = new ToastView(OrderDetailActivity.this, error101);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
    }

    private void getFtype(int type) {
        String ftype = "await_pay";
        switch (type) {
            case 1:
                ftype = "await_pay";//待付款
                if ("0".equals(dataModel.order.getPay_status())&&!"group_buy".equals(dataModel.order.getExtension_code())) {
                    btn_operate.setVisibility(View.VISIBLE);
                    btn_operate.setText("改价");
                    btn_operate.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EditOrderPriceActivity_Builder.intent(OrderDetailActivity.this)
                                    .discount(FormatUtil.formatStrToFloat(dataModel.order.getDiscount()))
                                    .goodsAmount(FormatUtil.formatStrToFloat(dataModel.order.getAmount()))
                                    .shippingFee(FormatUtil.formatStrToFloat(dataModel.order.getShipping()))
                                    .orderId(dataModel.order.getId())
                                    .start();
                        }
                    });
                } else {
                    btn_operate.setVisibility(View.GONE);
                }
                break;
            case 2:
                ftype = "await_ship";//待发货
                int count = 0;
                int statuCount = 0;
                for (int i = 0; i < dataModel.order.getGoodslist().size(); i++) {
                    if (!TextUtils.isEmpty(dataModel.order.getGoodslist().get(i).getRet_id()) && !"0".equals(dataModel.order.getGoodslist().get(i).getRet_id())) {
                        count++;
                        if ("9".equals(dataModel.order.getGoodslist().get(i).getReturn_status())) {
                            statuCount++;
                        }
                    }
                }
                if ("0".equals(dataModel.order.getShipping_status())) {
//                    if(count<=0||statuCount==count){
//                        btn_operate.setVisibility(View.VISIBLE);
//                        btn_operate.setText("发货");
//                        btn_operate.setOnClickListener(new OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                DeliverGoodsActivity_Builder.intent(OrderDetailActivity.this).orderId(dataModel.order.getId()).start();
//                            }
//                        });
//                    }else{
//                        btn_operate.setVisibility(View.VISIBLE);
//                        btn_operate.setBackgroundColor(getResources().getColor(R.color.gray));
//                        btn_operate.setText("发货");
//                        btn_operate.setOnClickListener(new OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                ToastUtil.getInstance().makeShortToast(OrderDetailActivity.this,"你有申请售后单子未处理，暂无法确认收货");
//                            }
//                        });
//                    }
                    if (count >= dataModel.order.getGoodslist().size()&&statuCount < dataModel.order.getGoodslist().size() ) {
                        btn_operate.setVisibility(View.GONE);
                    } else {
                        btn_operate.setVisibility(View.VISIBLE);
                        btn_operate.setText("发货");
                        btn_operate.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DeliverGoodsActivity_Builder.intent(OrderDetailActivity.this).orderId(dataModel.order.getId()).start();
                            }
                        });
                    }
                } else {
                    btn_operate.setVisibility(View.GONE);
                }
                break;
            case 3:
                ftype = "shipped";//已发货
                break;
            case 4:
                ftype = "finished";//已完成
                break;
            case 5:
                ftype = "refund";//退款
                break;
            case 6:
                ftype = "closed";//已经关闭
                break;
        }
//        return ftype;
    }
}
