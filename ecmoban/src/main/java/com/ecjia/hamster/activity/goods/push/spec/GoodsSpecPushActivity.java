package com.ecjia.hamster.activity.goods.push.spec;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;

import com.ecjia.base.NewBaseActivity;
import com.ecjia.entity.Species;
import com.ecjia.hamster.activity.goods.ReleaseGoodActivity;
import com.ecjia.hamster.activity.goods.push.goodstype.GoodsTypePushTagAdapter;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecmoban.android.shopkeeper.sijiqing.databinding.ActivityGoodsSpecPushBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import gear.yc.com.gearlibrary.rxjava.rxbus.RxBus;
import gear.yc.com.gearlibrary.utils.ToastUtil;

/**
 * ecjia-shopkeeper-android
 * 商品规格
 * Created by YichenZ on 2017/3/21 15:39.
 */

public class GoodsSpecPushActivity extends NewBaseActivity {
    ActivityGoodsSpecPushBinding mBinding;
    Species.SpeciBean data;

    Species.SpeciBean.CatBean color;
    Species.SpeciBean.CatBean size;

    GoodsTypePushTagAdapter colorAdapter;
    GoodsTypePushTagAdapter sizeAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_goods_spec_push);
        mBinding.icHead.topViewBack.setOnClickListener(v -> toActivity());
        mBinding.icHead.topViewText.setText("选择商品规格");
        mBinding.setOnClick(this);

        initData();
        initUI();
    }

    private void initUI() {
        mBinding.tvChooseColor.setText("请选择"+color.getAttrName());
        mBinding.tvChooseSize.setText("请选择"+size.getAttrName());

        if("1".equals(color.getInput_type())){
            mBinding.etInputColor.setVisibility(View.GONE);
            mBinding.btnAddColor.setVisibility(View.GONE);
        } else {
            mBinding.etInputColor.setVisibility(View.VISIBLE);
            mBinding.btnAddColor.setVisibility(View.VISIBLE);
        }
        if("1".equals(size.getInput_type())){
            mBinding.etInputSize.setVisibility(View.GONE);
            mBinding.btnAddSize.setVisibility(View.GONE);
        } else {
            mBinding.etInputSize.setVisibility(View.VISIBLE);
            mBinding.btnAddSize.setVisibility(View.VISIBLE);
        }
        mBinding.etInputColor.setHint("请输入自定义"+color.getAttrName());
        mBinding.etInputSize.setHint("请输入自定义"+size.getAttrName());

        mBinding.tfColor.setAdapter(colorAdapter);
        mBinding.tfSize.setAdapter(sizeAdapter);

        colorAdapter.showCheck();
        sizeAdapter.showCheck();
    }

    private void initData() {
        data = (Species.SpeciBean)getIntent().getSerializableExtra("spec");

        if(data == null){
            ToastUtil.getInstance().makeShortToast(this,"暂无数据");
            finish();
        }

        if(data.getCat().size() >= 1){
            color = data.getCat().get(0);
        }
        if(data.getCat().size() >= 2){
            size = data.getCat().get(1);
        }
        if(color == null || size == null){
            ToastUtil.getInstance().makeShortToast(this,"暂无数据");
            finish();
        }
        colorAdapter = new GoodsTypePushTagAdapter(this,color.getValues());
        sizeAdapter = new GoodsTypePushTagAdapter(this,size.getValues());
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.tv_cash_btn:
                update();
                break;
            case R.id.btn_add_color:
                String str = mBinding.etInputColor.getText().toString().trim();
                if("".equals(str)){
                    ToastUtil.getInstance().makeShortToast(this,color.getAttrName()+"不能为空");
                    return;
                }
                if(color != null){
                    color.getValues().add(
                            new Species.SpeciBean.CatBean.Value(str));
                    colorAdapter.notifyDataChanged();
                    mBinding.etInputColor.setText("");
                }
                break;
            case R.id.btn_add_size:
                String str2 = mBinding.etInputSize.getText().toString().trim();
                if("".equals(str2)){
                    ToastUtil.getInstance().makeShortToast(this,size.getAttrName()+"不能为空");
                    return;
                }
                if(size != null){
                    size.getValues().add(
                            new Species.SpeciBean.CatBean.Value(str2));
                    sizeAdapter.notifyDataChanged();
                    mBinding.etInputSize.setText("");
                }
                break;
        }
    }

    /**
     * 把选择的信息同步到数据中返回
     */
    private void update() {
        Set<Integer> colorIn = mBinding.tfColor.getSelectedList();
        Set<Integer> sizeIn = mBinding.tfSize.getSelectedList();

        //判断是否有空值，有则提示
        if(colorIn.size() <= 0){
            ToastUtil.getInstance().makeShortToast(this,"请至少选择一个"+color.getAttrName());
            return;
        } else if(sizeIn.size() <= 0){
            ToastUtil.getInstance().makeShortToast(this,"请至少选择一个"+size.getAttrName());
            return;
        }

        Species.SpeciBean initData = new Species.SpeciBean();//返回数据组
        List<Species.SpeciBean.CatBean> catBeens =new ArrayList<>();
        Species.SpeciBean.CatBean catBean =new Species.SpeciBean.CatBean();

        initData.setCatId(data.getCatId());//设置值
        initData.setCatName(data.getCatName());
        catBean.setAttrId(color.getAttrId());
        catBean.setAttrName(color.getAttrName());

        List<Species.SpeciBean.CatBean.Value> values =new ArrayList<>();
        for (Integer integer : colorIn) {
            Species.SpeciBean.CatBean.Value value = color.getValues().get(integer);
            values.add(value);
        }
        catBean.setValues(values);
        catBeens.add(catBean);

        catBean = new Species.SpeciBean.CatBean();//第二次设置值
        catBean.setAttrId(size.getAttrId());
        catBean.setAttrName(size.getAttrName());
        values =new ArrayList<>();
        for (Integer integer : sizeIn) {
            Species.SpeciBean.CatBean.Value value = size.getValues().get(integer);
            values.add(value);
        }
        catBean.setValues(values);
        catBeens.add(catBean);

        initData.setCat(catBeens);

        RxBus.getInstance().post(ReleaseGoodActivity.IN_SPEC,initData);
        toActivity();
    }

    private void toActivity(){
        startActivity(new Intent(this, ReleaseGoodActivity.class));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(KeyEvent.KEYCODE_BACK == keyCode){
            toActivity();
        }
        return true;
    }
}
