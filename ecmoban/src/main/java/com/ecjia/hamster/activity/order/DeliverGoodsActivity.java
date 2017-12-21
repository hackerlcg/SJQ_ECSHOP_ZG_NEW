package com.ecjia.hamster.activity.order;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.ecjia.base.LibActivity;
import com.ecjia.component.network.base.RetrofitAPIManager;
import com.ecjia.component.network.helper.SchedulersHelper;
import com.ecjia.component.network.responsmodel.ShippingModel;
import com.ecjia.component.network.responsmodel.ShippingStatusModel;
import com.ecjia.component.network.query.OrderQuery;
import com.ecjia.component.network.requestmodel.DeliveryGoods;
import com.ecjia.component.view.MyDialog;
import com.ecjia.hamster.activity.MyCaptureActivity;
import com.ecjia.util.EventBus.MyEvent;
import com.ecjia.util.LG;
import com.ecjia.util.common.DialogUtils;
import com.ecjia.util.common.IntentUtils;
import com.ecjia.util.common.JsonUtil;
import com.ecjia.util.common.T;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.ybk.intent.inject.annotation.Extra;
import com.ybk.intent.inject.api.IntentInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import de.greenrobot.event.EventBus;
import gear.yc.com.gearlibrary.rxjava.helper.RxSchedulersHelper;
import gear.yc.com.gearlibrary.utils.ProgressDialogUtil;
import gear.yc.com.gearlibrary.utils.ToastUtil;
import io.reactivex.functions.Consumer;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * 类名介绍：订单发货
 * Created by sun
 * Created time 2017-03-16.
 */

public class DeliverGoodsActivity extends LibActivity {
    @BindView(R.id.top_view_back)
    ImageView top_view_back;
    @BindView(R.id.top_view_text)
    TextView top_view_text;
    @BindView(R.id.top_right_tv)
    TextView top_right_tv;

    @BindView(R.id.spinner_type)
    Spinner spinner_type;
    @BindView(R.id.edit_shipping_num)
    EditText edit_shipping_num;
    @BindView(R.id.img_zxing)
    ImageView img_zxing;

    @Extra
    String orderId;

    private Map<String, String> spinnerMap = new HashMap<>();

    @Override
    public int getLayoutId() {
        return R.layout.act_delivergoods;
    }


    @Override
    public void init(Bundle savedInstanceState) {
        IntentInject.inject(this);//add this line cod
        getShipping();
        top_view_text.setText("订单发货");
    }

    @OnClick({R.id.top_view_back, R.id.btn_save, R.id.img_zxing})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_view_back:
                finish();
                break;
            case R.id.btn_save:
                String rejected_reason = spinnerMap.get(spinner_type.getSelectedItem().toString());
                String rejected_value = edit_shipping_num.getText().toString();
                if (TextUtils.isEmpty(rejected_reason)) {
                    T.show(mActivity, "请选择快递公司");
                    return;
                }
                if (TextUtils.isEmpty(rejected_value)) {
                    T.show(mActivity, "填写快递单号");
                    return;
                }
                DeliveryGoods deliveryGoods = new DeliveryGoods();
                deliveryGoods.setOrder_id(orderId);
                deliveryGoods.setInvoice_no(rejected_value + "");
                deliveryGoods.setShipping_id(rejected_reason + "");
                deliveryGoods.setShipping_name(spinner_type.getSelectedItem().toString() + "");
                backTips(JsonUtil.toHttpFormatString(deliveryGoods));
                break;
            case R.id.img_zxing:
                RxPermissions rxPermissions = new RxPermissions(mActivity);
                rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean aBoolean) throws Exception {
                                if (aBoolean) {
                                    //打开扫描界面扫描条形码或二维码
                                    Intent openCameraIntent = new Intent(mActivity, MyCaptureActivity.class);
                                    openCameraIntent.putExtra("startType", 1);
                                    openCameraIntent.putExtra("from", "delivergoodsactivity");
                                    startActivityForResult(openCameraIntent, 1);
                                } else {
                                    DialogUtils.showDialog(mActivity, "提示", "相机 或者 读写手机存储 权限被关闭，是否设置开启？", new DialogUtils.ButtonClickListener() {
                                        @Override
                                        public void negativeButton() {
                                        }

                                        @Override
                                        public void positiveButton() {
                                            IntentUtils.IntentAppSetting(mActivity, 1111);
                                        }
                                    });
                                }
                            }
                        });
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //处理扫描结果（在界面上显示）
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
//            ToastUtil.getInstance().makeLongToast(mActivity,scanResult);
            edit_shipping_num.setText(scanResult);
        }
    }

    private void backTips(String jsonStr) {
        MyDialog myDialog = new MyDialog(this, getResources().getString(R.string.tip), "确认发货吗");
        myDialog.show();
        myDialog.negative.setOnClickListener(new View.OnClickListener() {//取消
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
        myDialog.positive.setOnClickListener(new View.OnClickListener() {//确定
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
                getDeliveryGoods(jsonStr);
            }
        });
    }

    private void getShipping() {
        ProgressDialogUtil.getInstance().build(mActivity).show();
        RetrofitAPIManager.getAPIOrder()
                .getShipping(OrderQuery.getInstance().getShipping())
                .compose(liToDestroy())
                .compose(RxSchedulersHelper.io_main())
                .compose(SchedulersHelper.handleResult())
                .doOnTerminate(() -> ProgressDialogUtil.getInstance().dismiss())
                .subscribe(new ResourceSubscriber<ArrayList<ShippingModel>>() {
                    @Override
                    public void onNext(ArrayList<ShippingModel> returncauses) {
                        int size = returncauses.size();
                        String[] arr = new String[size];
                        for (int i = 0; i < size; i++) {
                            arr[i] = returncauses.get(i).getShipping_name();
                            spinnerMap.put(arr[i], returncauses.get(i).getShipping_id());
                        }
                        ArrayAdapter<String> zhuyingAdapter = new ArrayAdapter<String>(mActivity, android.R.layout.simple_spinner_item, arr);
                        zhuyingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        //绑定 Adapter到控件
                        spinner_type.setAdapter(zhuyingAdapter);
                        spinner_type.setSelection(0);
                    }

                    @Override
                    public void onError(Throwable t) {
                        T.show(mActivity, t.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void getDeliveryGoods(String jsonStr) {
        ProgressDialogUtil.getInstance().build(mActivity).show();
        RetrofitAPIManager.getAPIOrder()
                .getDeliveryGoods(jsonStr)
                .compose(liToDestroy())
                .compose(RxSchedulersHelper.io_main())
                .compose(SchedulersHelper.handleResult())
                .doOnTerminate(() -> ProgressDialogUtil.getInstance().dismiss())
                .subscribe(new ResourceSubscriber<ShippingStatusModel>() {
                    @Override
                    public void onNext(ShippingStatusModel string) {
                        T.show(mActivity, "发货成功");
                        EventBus.getDefault().post(new MyEvent("DeliverGoodsActivity"));
                        finish();
                    }

                    @Override
                    public void onError(Throwable t) {
                        T.show(mActivity, t.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void dispose() {

    }
}
