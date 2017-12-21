package com.ecjia.hamster.activity.order;

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
import com.ecjia.component.network.query.OrderQuery;
import com.ecjia.component.network.requestmodel.ReturnStatus;
import com.ecjia.component.network.responsmodel.GetGoodInfoImgModel;
import com.ecjia.component.network.responsmodel.ShippingModel;
import com.ecjia.util.common.Base64;
import com.ecjia.util.common.BitmapUtils;
import com.ecjia.util.common.BottomChoosePhotoUtils;
import com.ecjia.util.common.ImageUtils;
import com.ecjia.util.common.T;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ybk.intent.inject.annotation.Extra;
import com.ybk.intent.inject.api.IntentInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import gear.yc.com.gearlibrary.rxjava.helper.RxSchedulersHelper;
import gear.yc.com.gearlibrary.utils.ProgressDialogUtil;
import gear.yc.com.gearlibrary.utils.ToastUtil;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * 类名介绍：申请提交填写物流信息
 * Created by sun
 * Created time 2017-03-10.
 */

public class ApplyAddShippingInfoActivity extends LibActivity {
    @BindView(R.id.top_view_back)
    ImageView top_view_back;
    @BindView(R.id.top_view_text)
    TextView top_view_text;
    @BindView(R.id.top_right_tv)
    TextView top_right_tv;

    @BindView(R.id.spinner_type)
    Spinner spinner_type;
    @BindView(R.id.edit_money)
    EditText edit_money;

    @BindView(R.id.iv_upload1)
    ImageView iv_upload1;
    @BindView(R.id.iv_del_pic1)
    ImageView iv_del_pic1;

    //    private ArrayList<RETURNCAUSE> spinnerTypeList = new ArrayList<>();
    private Map<String, String> spinnerMap = new HashMap<>();
    private String imgPath;

    @Extra
    String retId;


    @Override
    public int getLayoutId() {
        return R.layout.activity_add_shippinginfo;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        IntentInject.inject(this);
        top_view_text.setText("填写物流信息");
        if (TextUtils.isEmpty(retId)) {
            finish();
            return;
        }
        iv_del_pic1.setVisibility(View.GONE);
        getShipping();//请求快递接口
    }

    @OnClick({R.id.top_view_back, R.id.iv_upload1, R.id.iv_del_pic1, R.id.apply})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_view_back:
                finish();
                break;
            case R.id.iv_upload1:
                setChooesImg(200000);
                break;
            case R.id.iv_del_pic1:
                imgPath = "";
                iv_upload1.setImageResource(R.drawable.img_default_null);
                iv_del_pic1.setVisibility(View.GONE);
                break;
            case R.id.apply:
                String rejected_reason = spinnerMap.get(spinner_type.getSelectedItem().toString());
                String rejected_value = edit_money.getText().toString();
                String first_image = TextUtils.isEmpty(imgPath) ? "" : Base64.encode(BitmapUtils.getImage(mActivity, imgPath, 300));
                T.show(mActivity, rejected_reason);
                if (TextUtils.isEmpty(rejected_reason)) {
                    T.show(mActivity, "请选择快递公司");
                    return;
                }
                if (TextUtils.isEmpty(rejected_value)) {
                    T.show(mActivity, "填写快递单号");
                    return;
                }
                getSendCourier(rejected_reason, rejected_value, first_image);
                break;
        }
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

    private void getSendCourier(String shipping_id, String invoice_no, String images) {
        ProgressDialogUtil.getInstance().build(mActivity).show();
        RetrofitAPIManager.getAPIOrder()
                .getSendCourier(OrderQuery.getInstance().getSendCourier(retId, shipping_id, invoice_no, images))
                .compose(liToDestroy())
                .compose(RxSchedulersHelper.io_main())
                .compose(SchedulersHelper.handleResult())
                .doOnTerminate(() -> ProgressDialogUtil.getInstance().dismiss())
                .subscribe(new ResourceSubscriber<ReturnStatus>() {
                    @Override
                    public void onNext(ReturnStatus returnstatus) {
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

    private void setChooesImg(int requestPostion) {
        BottomChoosePhotoUtils.newInstance().show(getSupportFragmentManager(), new BottomChoosePhotoUtils.OnChoosePhoneResult() {
            @Override
            public void onHandlerSuccess(List<PhotoInfo> resultList, int requestPostion) {
                if (requestPostion == 200000) {
                    GetGoodInfoImgModel image = new GetGoodInfoImgModel();
                    image.setMid("0");
                    image.setUrl(resultList.get(0).getPhotoPath());
                    imgPath = resultList.get(0).getPhotoPath();
                    ImageUtils.showImageFilePath(mActivity, resultList.get(0).getPhotoPath(), iv_upload1);
                    iv_del_pic1.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onHandlerFailure(String errorMsg, int requestPostion) {
                ToastUtil.getInstance().makeLongToast(mActivity, errorMsg);

            }
        }, requestPostion);
    }

    @Override
    public void dispose() {

    }
}
