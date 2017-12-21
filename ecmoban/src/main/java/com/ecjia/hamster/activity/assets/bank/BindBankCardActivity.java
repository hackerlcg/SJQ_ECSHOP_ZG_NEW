package com.ecjia.hamster.activity.assets.bank;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;

import com.ecjia.base.NewBaseActivity;
import com.ecjia.component.network.base.RetrofitAPIManager;
import com.ecjia.component.network.helper.SchedulersHelper;
import com.ecjia.component.network.query.AssetsQuery;
import com.ecjia.entity.Assets;
import com.ecjia.hamster.activity.assets.choosebank.ChooseBankActivity;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecmoban.android.shopkeeper.sijiqing.databinding.BindBankCardActivityBinding;

import java.util.concurrent.TimeUnit;

import gear.yc.com.gearlibrary.rxjava.helper.RxSchedulersHelper;
import gear.yc.com.gearlibrary.rxjava.rxbus.RxBus;
import gear.yc.com.gearlibrary.rxjava.rxbus.annotation.Subscribe;
import gear.yc.com.gearlibrary.utils.ProgressDialogUtil;
import gear.yc.com.gearlibrary.utils.ToastUtil;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * ecjia-shopkeeper-android
 * 绑定银行卡
 * Created by YichenZ on 2017/3/15 11:21.
 */

public class BindBankCardActivity extends NewBaseActivity {
    BindBankCardActivityBinding mBinding;
    Assets mAssets;
    AssetsQuery mAssetsQuery = new AssetsQuery();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.bind_bank_card_activity);
        mBinding.icHead.topViewText.setText("绑定银行卡");
        mBinding.setOnClick(this);
        mBinding.icHead.topViewBack.setOnClickListener(v -> finish());
        RxBus.getInstance().register(this);

        initData();
    }

    @Override
    protected void onDestroy() {
        RxBus.getInstance().unRegister(this);
        super.onDestroy();
    }

    private void initData() {
        mAssets = (Assets) getIntent().getSerializableExtra("assets");
        mBinding.setData(mAssets);
    }

    @Subscribe(tag = RxBus.TAG_CHANGE)
    public void onBankCard(String name) {
        mBinding.tvChooseBank.setGravity(Gravity.LEFT);
        mBinding.tvChooseBank.setText(name);
    }

    /**
     * 获取验证码
     */
    boolean isSms = true;

    private void getSms() {
        if ("".equals(mAssets.getMobile())) {
            ToastUtil.getInstance().makeShortToast(this, "手机账号不能为空");
            return;
        }
        if(mAssets.getMobile().trim().length() != 11){
            ToastUtil.getInstance().makeShortToast(this, "请输入正确的手机号");
            return;
        }
        if (isSms) {
            ProgressDialogUtil.getInstance().build(this).show();
            RetrofitAPIManager.getAPIPublic()
                    .getSms(mAssetsQuery.getSms(1, 0, mAssets.getMobile()))
                    .compose(liToDestroy())
                    .compose(RxSchedulersHelper.io_main())
                    .compose(SchedulersHelper.handleResult())
                    .doOnTerminate(() -> ProgressDialogUtil.getInstance().dismiss())
                    .subscribe(d -> onSmsCode(d),
                            e -> showError(e));
            //按钮倒计时
            onButtonTimes();
        }
    }

    String code = "";
    public void onSmsCode(Assets assets){
        code = assets.getCode();
    }

    /**
     * 提交银行卡信息
     */
    public void putAssets() {
        //审核数据
        if("".equals(mAssets.getSellerName())){
            ToastUtil.getInstance().makeShortToast(this,"姓名不能为空");
            return;
        } else if("".equals(mAssets.getCard())){
            ToastUtil.getInstance().makeShortToast(this,"储蓄卡号不能为空");
            return;
        } else if("".equals(mAssets.getBankName())){
            ToastUtil.getInstance().makeShortToast(this,"开户行不能为空");
            return;
        } else if("".equals(mAssets.getIdentity())){
            ToastUtil.getInstance().makeShortToast(this,"身份证号不能为空");
            return;
        } else if("".equals(mAssets.getMobile())){
            ToastUtil.getInstance().makeShortToast(this,"手机号不能为空");
            return;
        } else if("".equals(mAssets.getSms()) || null == mAssets.getSms()){
            ToastUtil.getInstance().makeShortToast(this,"短信验证码不能为空");
            return;
        }
        if(!code.equals(mAssets.getSms())){
            ToastUtil.getInstance().makeShortToast(this,"短信验证码不正确，请重新输入");
            return;
        }
        ProgressDialogUtil.getInstance().build(this).show();
        RetrofitAPIManager.getAPIAssets()
                .putAssets(mAssetsQuery.getQuery(mAssets))
                .compose(liToDestroy())
                .compose(RxSchedulersHelper.io_main())
                .compose(SchedulersHelper.handleResult())
                .doOnTerminate(() -> ProgressDialogUtil.getInstance().dismiss())
                .subscribe(d -> onSuccess("信息上传成功"), e -> showError(e));
    }

    public void onSuccess(String str) {
        ToastUtil.getInstance().makeLongToast(this, str);
        RxBus.getInstance().post(RxBus.TAG_UPDATE, true);
        finish();
    }

    public void showError(Throwable e) {
        ToastUtil.getInstance().makeShortToast(this, e.getMessage());
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.rl_bank_card:
                startActivity(new Intent(this, ChooseBankActivity.class));
                break;
            case R.id.tv_cash_btn:
                putAssets();
                break;
            case R.id.tv_get_sms:
                getSms();
                break;
        }
    }

    /**
     * 设置短信验证码60s倒计时
     */
    short times = 60;
    Disposable disTimes;
    private void onButtonTimes() {
        disTimes = Flowable.interval(0,1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    if(times <= 0){
                        times = 60;
                        isSms = true;
                        mBinding.tvGetSms.setText("获取验证码");
                        disTimes.dispose();
                        return;
                    }
                    isSms = false;
                    mBinding.tvGetSms.setText("已发送"+String.valueOf(times)+"s");
                    times--;
                }, e -> ToastUtil.getInstance().makeShortToast(mActivity,e.getMessage()));
    }

}
