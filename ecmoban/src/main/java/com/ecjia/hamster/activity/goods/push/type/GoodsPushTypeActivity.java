package com.ecjia.hamster.activity.goods.push.type;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.View;

import com.ecjia.base.NewBaseActivity;
import com.ecjia.component.network.base.RetrofitAPIManager;
import com.ecjia.component.network.helper.SchedulersHelper;
import com.ecjia.entity.Category;
import com.ecjia.hamster.adapter.adapter.my.MyFragmentPagerAdapter;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecmoban.android.shopkeeper.sijiqing.databinding.ActivityGoodsPushTypeBinding;

import java.util.ArrayList;
import java.util.List;

import gear.yc.com.gearlibrary.rxjava.helper.RxSchedulersHelper;
import gear.yc.com.gearlibrary.utils.ProgressDialogUtil;
import gear.yc.com.gearlibrary.utils.ToastUtil;

public class GoodsPushTypeActivity extends NewBaseActivity {
    ActivityGoodsPushTypeBinding mBinding;

    private List<Fragment> fragmentList;
    private List<String> mTitleList = new ArrayList<>();//页卡标题集合
    private MyFragmentPagerAdapter myFragmentPagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_goods_push_type);
        mBinding.setOnClick(this);
        mBinding.icHead.topViewText.setText("选择分类");
        mBinding.icHead.topViewBack.setOnClickListener(v -> finish());
        fragmentList = new ArrayList<>();

        loadData();
    }

    private void initView() {
        mBinding.tablayout.setTabMode(TabLayout.MODE_FIXED);
//        mBinding.tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList, mTitleList);
        mBinding.viewPagerSearch.setAdapter(myFragmentPagerAdapter);//给ViewPager设置适配器
        mBinding.tablayout.setupWithViewPager(mBinding.viewPagerSearch);//将TabLayout和ViewPager关联起来。
        mBinding.tablayout.setTabsFromPagerAdapter(myFragmentPagerAdapter);//给Tabs设置适配器
        mBinding.viewPagerSearch.setOffscreenPageLimit(fragmentList.size());
        mBinding.viewPagerSearch.setCurrentItem(0);
    }

    private void loadData() {
        ProgressDialogUtil.getInstance().build(this).show();
        RetrofitAPIManager.getAPIPublic()
                .getCategory("")
                .compose(liToDestroy())
                .compose(RxSchedulersHelper.io_main())
                .compose(SchedulersHelper.handleResult())
                .doOnTerminate(() -> ProgressDialogUtil.getInstance().dismiss())
                .subscribe(d -> showData(d), e -> showError(e));
    }

    public void showData(List<Category> data){
        if (data.size() > 0) {
            mBinding.lyChoose.setVisibility(View.VISIBLE);
            fragmentList.clear();
            mTitleList.clear();
            mBinding.tablayout.removeAllTabs();
            for (Category cateParent : data) {
                mTitleList.add(cateParent.getName());
                fragmentList.add(new SearchChildFragment(cateParent));
            }
            initView();
        } else {
            mBinding.lyChoose.setVisibility(View.GONE);
        }
    }

    public void showError(Throwable e){
        ToastUtil.getInstance().makeShortToast(this,e.getMessage());
        mBinding.lyChoose.setVisibility(View.GONE);
    }
}
