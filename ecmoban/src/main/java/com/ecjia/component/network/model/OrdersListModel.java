package com.ecjia.component.network.model;


import android.content.Context;
import android.content.res.Resources;

import com.ecjia.component.view.MyProgressDialog;
import com.ecjia.consts.AndroidManager;
import com.ecjia.consts.AppConst;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.hamster.model.ORDERS;
import com.ecjia.hamster.model.ORDERSRETURN;
import com.ecjia.hamster.model.PAGINATED;
import com.ecjia.hamster.model.PAGINATION;
import com.ecjia.hamster.model.SESSION;
import com.ecjia.hamster.model.STATUS;
import com.ecjia.util.LG;
import com.ecjia.util.common.JsonUtil;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OrdersListModel extends BaseModel {

    public ArrayList<ORDERS> ordersList = new ArrayList<ORDERS>();

    public MyProgressDialog pd;
    public static final int PAGE_COUNT = 10;

    public OrdersListModel(Context context) {
        super(context);
        pd = MyProgressDialog.createDialog(context);
        Resources resources = AppConst.getResources(context);
        pd.setMessage(resources.getString(R.string.loading));
    }

    public PAGINATED paginated;

    public void fetchPreSearch(SESSION session, String type, String keywords, String api, final boolean isfalse) {
        final String url = ProtocolConst.ORDERS;
        if (isfalse) {
            pd.show();
        }

        PAGINATION pagination = new PAGINATION();
        pagination.setPage(1);
        pagination.setCount(PAGE_COUNT);

        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("pagination", pagination.toJson());
            requestJsonObject.put("device", device.toJson());
            requestJsonObject.put("type", type);
            requestJsonObject.put("keywords", keywords);
        } catch (JSONException e) {
            // TODO: handle exception
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("传入参数==" + requestJsonObject.toString());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, api + url, params,
                new RequestCallBack<String>() {


                    @Override
                    public void onFailure(HttpException arg0, String arg1) {

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        if (isfalse) {
                            if (pd.isShowing()) {
                                pd.dismiss();
                            }
                        }
                        JSONObject jo;
                        LG.i("返回参数==" + arg0.result);
                        try {
                            jo = new JSONObject(arg0.result);
                            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                            paginated = PAGINATED.fromJson(jo.optJSONObject("paginated"));
                            if (responseStatus.getSucceed() == 1) {
                                JSONArray ordersListJsonArray = jo.optJSONArray("data");
                                ordersList.clear();
                                if (null != ordersListJsonArray && ordersListJsonArray.length() > 0) {
                                    ordersList.clear();
                                    for (int i = 0; i < ordersListJsonArray.length(); i++) {
                                        JSONObject ordersListJsonObject = ordersListJsonArray.getJSONObject(i);
                                        ORDERS orders = ORDERS.fromJson(ordersListJsonObject);
                                        ordersList.add(orders);
                                    }
                                }
                            }
                            OrdersListModel.this.OnMessageResponse(url, jo, responseStatus);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                        super.onLoading(total, current, isUploading);
                    }
                });
    }

    public void fetchPreSearchMore(SESSION session, String type, String keywords, String api) {
        final String url = ProtocolConst.ORDERS;
        //  pd.show();
        PAGINATION pagination = new PAGINATION();
        pagination.setPage((int) Math.ceil((double) ordersList.size() * 1.0 / PAGE_COUNT) + 1);
        pagination.setCount(PAGE_COUNT);

        JSONObject requestJsonObject = new JSONObject();

        try {
            requestJsonObject.put("session", session.toJson());
            requestJsonObject.put("pagination", pagination.toJson());
            requestJsonObject.put("type", type);
            requestJsonObject.put("keywords", keywords);
        } catch (JSONException e) {
            // TODO: handle exception
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, api + url, params,
                new RequestCallBack<String>() {


                    @Override
                    public void onFailure(HttpException arg0, String arg1) {

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        //   pd.dismiss();
                        JSONObject jo;
                        try {
                            jo = new JSONObject(arg0.result);
                            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                            paginated = PAGINATED.fromJson(jo.optJSONObject("paginated"));
                            if (responseStatus.getSucceed() == 1) {
                                JSONArray ordersListJsonArray = jo.optJSONArray("data");
                                LG.i("返回参数==" + jo.toString());
                                if (null != ordersListJsonArray && ordersListJsonArray.length() > 0) {
                                    for (int i = 0; i < ordersListJsonArray.length(); i++) {
                                        JSONObject ordersListJsonObject = ordersListJsonArray.getJSONObject(i);
                                        ORDERS orders = ORDERS.fromJson(ordersListJsonObject);
                                        ordersList.add(orders);
                                    }
                                }

                            }
                            OrdersListModel.this.OnMessageResponse(url, jo, responseStatus);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                        super.onLoading(total, current, isUploading);
                    }
                });
    }

    public ArrayList<ORDERSRETURN> order_return_list = new ArrayList<ORDERSRETURN>();

    //获取退换货订单列表
    public void fetchPreReturnSearch(final boolean isfalse) {
        final String url = ProtocolConst.ORDERS_RETURN;
        if (isfalse) {
            pd.show();
        }
        PAGINATION pagination = new PAGINATION();
        pagination.setPage(1);
        pagination.setCount(PAGE_COUNT);
        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("session", SESSION.getInstance().toJson());
            requestJsonObject.put("device", device.toJson());
            requestJsonObject.put("pagination", pagination.toJson());
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("传入参数==" + requestJsonObject.toString());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AndroidManager.APPAPI + url, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        if (isfalse) {
                            if (pd.isShowing()) {
                                pd.dismiss();
                            }
                        }
                        JSONObject jo;
                        LG.i("返回参数==" + arg0.result);
                        try {
                            jo = new JSONObject(arg0.result);
                            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                            paginated = PAGINATED.fromJson(jo.optJSONObject("paginated"));
                            if (responseStatus.getSucceed() == 1) {
                                order_return_list.clear();
                                order_return_list.addAll(JsonUtil.getListObj(jo.getString("data"), ORDERSRETURN.class));
                            }
                            OrdersListModel.this.OnMessageResponse(url, jo, responseStatus);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                        super.onLoading(total, current, isUploading);
                    }
                });
    }

    public void fetchPreReturnSearchMore() {
        final String url = ProtocolConst.ORDERS_RETURN;
        PAGINATION pagination = new PAGINATION();
        pagination.setPage((int) Math.ceil((double) order_return_list.size() * 1.0 / PAGE_COUNT) + 1);
        pagination.setCount(PAGE_COUNT);
        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("session", SESSION.getInstance().toJson());
            requestJsonObject.put("pagination", pagination.toJson());
            requestJsonObject.put("device", device.toJson());
        } catch (JSONException e) {
        }

        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AndroidManager.APPAPI + url, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        //   pd.dismiss();
                        JSONObject jo;
                        try {
                            jo = new JSONObject(arg0.result);
                            STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                            paginated = PAGINATED.fromJson(jo.optJSONObject("paginated"));
                            if (responseStatus.getSucceed() == 1) {
                                order_return_list.addAll(JsonUtil.getListObj(jo.getString("data"), ORDERSRETURN.class));
                            }
                            OrdersListModel.this.OnMessageResponse(url, jo, responseStatus);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                        super.onLoading(total, current, isUploading);
                    }
                });
    }


}
