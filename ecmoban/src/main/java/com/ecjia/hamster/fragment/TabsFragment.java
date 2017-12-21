package com.ecjia.hamster.fragment;


import android.annotation.TargetApi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecmoban.android.shopkeeper.sijiqing.R;

/**
 * 首页底部的设置
 *
 * @author Administrator
 */
public class TabsFragment extends Fragment {

    ImageView tab_one;
    ImageView tab_two;
    ImageView tab_three;
    ImageView tab_four;

    TextView tv_one;
    TextView tv_two;
    TextView tv_three;
    TextView tv_four;

    LinearLayout ll_root;
    View one_buttom, two_buttom, three_buttom, four_buttom;

    //跳转我的宝贝订单语句
    public boolean selectfour = false;
    public boolean selectfive = false;


    public boolean fromfour = false;
    public boolean fromfive = false;

    public boolean animflag = true;

    public boolean hiddenflag = false;

    public boolean select_one;

    public boolean select_eight = false;

    HomeFragment homeFragment;//主页
    MagicFragment magicFragment;//促销
    ShopFragment shopFragment;//店铺中心
    //    MyGoodsFragment myGoodsFragment;
    MyOrdersFragment myOrdersFragment;//订单
    //    ServiceFragment serviceFragment;//客服
//    FeedBackListFragment feedBackListFragment;

    SharedPreferences remkeywords;
    private String keywords;

    public TabsFragment() {
        instance = this;
    }

    private static TabsFragment instance;

    public static TabsFragment getInstance() {
        if (instance == null) {
            instance = new TabsFragment();
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.toolbar, container, false);
        instance = this;
        init(mainView);

        return mainView;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);
        setRetainInstance(true);
    }

    void init(View mainView) {

        this.ll_root = (LinearLayout) mainView.findViewById(R.id.ll_root);

        this.tv_one = (TextView) mainView.findViewById(R.id.tv_home);
        this.tv_two = (TextView) mainView.findViewById(R.id.tv_search);
        this.tv_three = (TextView) mainView.findViewById(R.id.tv_shop);
        this.tv_four = (TextView) mainView.findViewById(R.id.tv_service);
        one_buttom = mainView.findViewById(R.id.toolbar_tabone_buttom);
        two_buttom = mainView.findViewById(R.id.toolbar_tabtwo_buttom);
        three_buttom = mainView.findViewById(R.id.toolbar_tabthree_buttom);
        four_buttom = mainView.findViewById(R.id.toolbar_tabfour_buttom);


        this.tab_one = (ImageView) mainView.findViewById(R.id.toolbar_tabone);
        this.tab_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnTabSelected("tab_one");
                select_one = true;
            }
        });
        one_buttom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnTabSelected("tab_one");
                select_one = true;
            }
        });

        this.tab_two = (ImageView) mainView.findViewById(R.id.toolbar_tabtwo);
        this.tab_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnTabSelected("tab_two");
            }
        });
        two_buttom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnTabSelected("tab_two");
            }
        });

        this.tab_three = (ImageView) mainView
                .findViewById(R.id.toolbar_tabthree);
        this.tab_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OnTabSelected("tab_three");
            }

        });
        three_buttom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnTabSelected("tab_three");
            }
        });
        tab_four = (ImageView) mainView.findViewById(R.id.toolbar_tabfour);
        tab_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OnTabSelected("tab_four");
            }

        });
        four_buttom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnTabSelected("tab_four");
            }
        });

    }

    /**
     * 我的宝贝编辑时隐藏底部
     */
    public void hidden(boolean flag) {
        if (flag) {
            ll_root.setVisibility(View.VISIBLE);
            hiddenflag = false;
        } else {
            ll_root.setVisibility(View.GONE);
            hiddenflag = true;
        }
    }


    /**
     * 判断是否在编辑中
     */
    public boolean isbeHidden() {
        return hiddenflag;
    }

    public void toMyFragment(int i, String arg) {
        if (i == 1) {
            if (null == homeFragment) {
                homeFragment = new HomeFragment();
            }
            selectfour = false;
            selectfive = false;
            replace(homeFragment);
        }

//        if (i == 4) {
//            if (myGoodsFragment == null) {
//                myGoodsFragment = new MyGoodsFragment();
//            }
//            Bundle bundle = new Bundle();
//            bundle.putString("msgcat", arg);
//            myGoodsFragment.setArguments(bundle);
//            selectfour = true;
//            replace(myGoodsFragment);
//        }
//        if (i == 5) {
//            if (myOrdersFragment == null) {
//                myOrdersFragment = new MyOrdersFragment();
//            }
//            selectfive = true;
//            replace(myOrdersFragment);
//        }

        tab_one.setImageResource(R.drawable.footer_home_active_icon);
        tab_two.setImageResource(R.drawable.footer_search_icon);
        tab_three.setImageResource(R.drawable.footer_shop_icon);
        tab_four.setImageResource(R.drawable.footer_service_icon);

        tv_one.setTextColor(getResources().getColor(R.color.bg_theme_color));
        tv_two.setTextColor(getResources().getColor(R.color.text_login_hint_color));
        tv_three.setTextColor(getResources().getColor(R.color.text_login_hint_color));
        tv_four.setTextColor(getResources().getColor(R.color.text_login_hint_color));
    }

//    public void toServiceFragment() {
//        if (serviceFragment == null) {
//            serviceFragment = new ServiceFragment();
//        }
//        select_eight = false;
//        replaceWithAnim(serviceFragment);
//    }

    private void replaceWithAnim(Fragment fragment) {

        FragmentTransaction localFragmentTransaction = getFragmentManager()
                .beginTransaction();
        if (fragment instanceof ServiceFragment) {
            localFragmentTransaction.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out);
        } else {
            localFragmentTransaction.setCustomAnimations(R.anim.push_right_in, R.anim.push_right_out);
        }
        localFragmentTransaction.replace(R.id.fragment_container,
                fragment);
        localFragmentTransaction.commitAllowingStateLoss();
    }

    public void SearchFragment(int i, String arg, boolean flag) {
        if (i == 2) {
            if (null == magicFragment) {
                magicFragment = new MagicFragment();
            }
            replace(magicFragment);
        }

        tab_one.setImageResource(R.drawable.footer_home_icon);
        tab_two.setImageResource(R.drawable.footer_search_active_icon);
        tab_three.setImageResource(R.drawable.footer_shop_icon);
        tab_four.setImageResource(R.drawable.footer_service_icon);

        tv_one.setTextColor(getResources().getColor(R.color.text_login_hint_color));
        tv_two.setTextColor(getResources().getColor(R.color.bg_theme_color));
        tv_three.setTextColor(getResources().getColor(R.color.text_login_hint_color));
        tv_four.setTextColor(getResources().getColor(R.color.text_login_hint_color));

    }

    public void msgFragment(int i, String arg) {
//        if (i == 4) {
//            myGoodsFragment = new MyGoodsFragment();
//            selectfour = true;
//            FragmentTransaction localFragmentTransaction = getFragmentManager()
//                    .beginTransaction();
//            localFragmentTransaction.replace(R.id.fragment_container,
//                    myGoodsFragment);
//            localFragmentTransaction.commitAllowingStateLoss();
//
//            tab_one.setImageResource(R.drawable.footer_home_active_icon);
//            tab_two.setImageResource(R.drawable.footer_search_icon);
//            tab_three.setImageResource(R.drawable.footer_shop_icon);
//            tab_four.setImageResource(R.drawable.footer_service_icon);
//
//            tv_one.setTextColor(getResources().getColor(R.color.bg_theme_color));
//            tv_two.setTextColor(getResources().getColor(R.color.text_login_hint_color));
//            tv_three.setTextColor(getResources().getColor(R.color.text_login_hint_color));
//            tv_four.setTextColor(getResources().getColor(R.color.text_login_hint_color));
//        } else
//        if (i == 5) {
//            myOrdersFragment = new MyOrdersFragment();
//            selectfive = true;
//            replace(myOrdersFragment);
//
//            tab_one.setImageResource(R.drawable.footer_home_active_icon);
//            tab_two.setImageResource(R.drawable.footer_search_icon);
//            tab_three.setImageResource(R.drawable.footer_shop_icon);
//            tab_four.setImageResource(R.drawable.footer_service_icon);
//
//            tv_one.setTextColor(getResources().getColor(R.color.bg_theme_color));
//            tv_two.setTextColor(getResources().getColor(R.color.text_login_hint_color));
//            tv_three.setTextColor(getResources().getColor(R.color.text_login_hint_color));
//            tv_four.setTextColor(getResources().getColor(R.color.text_login_hint_color));
//        } else

        if (i == 2) {

            if (selectfour) {
                selectfour = false;
                fromfour = true;
            }

            if (selectfive) {
                selectfive = false;
                fromfive = true;
            }

            magicFragment = new MagicFragment();


            FragmentTransaction localFragmentTransaction = getFragmentManager()
                    .beginTransaction();
            localFragmentTransaction.replace(R.id.fragment_container,
                    magicFragment, "tab_two");
            localFragmentTransaction.commitAllowingStateLoss();

            tab_one.setImageResource(R.drawable.footer_home_icon);
            tab_two.setImageResource(R.drawable.footer_search_active_icon);
            tab_three.setImageResource(R.drawable.footer_shop_icon);
            this.tab_four.setImageResource(R.drawable.footer_service_icon);

            tv_one.setTextColor(getResources().getColor(R.color.text_login_hint_color));
            tv_two.setTextColor(getResources().getColor(R.color.bg_theme_color));
            tv_three.setTextColor(getResources().getColor(R.color.text_login_hint_color));
            tv_four.setTextColor(getResources().getColor(R.color.text_login_hint_color));
        } else if (i == 3) {
            if (shopFragment == null)
                shopFragment = new ShopFragment();

            FragmentTransaction localFragmentTransaction = getFragmentManager()
                    .beginTransaction();
            localFragmentTransaction.replace(R.id.fragment_container,
                    shopFragment, "tab_three");
            localFragmentTransaction.commit();

            if (selectfour) {
                selectfour = false;
                fromfour = true;
            }

            if (selectfive) {
                selectfive = false;
                fromfive = true;
            }

            this.tab_one.setImageResource(R.drawable.footer_home_icon);
            this.tab_two.setImageResource(R.drawable.footer_search_icon);
            this.tab_three.setImageResource(R.drawable.footer_shop_active_icon);
            this.tab_four.setImageResource(R.drawable.footer_service_icon);

            this.tv_one.setTextColor(getResources().getColor(R.color.text_login_hint_color));
            this.tv_two.setTextColor(getResources().getColor(R.color.text_login_hint_color));
            this.tv_three.setTextColor(getResources().getColor(R.color.bg_theme_color));
            this.tv_four.setTextColor(getResources().getColor(R.color.text_login_hint_color));
        }
//        else if (i == 8) {//咨询内部子页面
//            if (feedBackListFragment == null)
//                feedBackListFragment = new FeedBackListFragment();
//            select_eight = true;
//            Bundle bundle = new Bundle();
//            bundle.putString("type", arg);
//            feedBackListFragment.setArguments(bundle);
//
//            replace(feedBackListFragment);
//
//            this.tab_one.setImageResource(R.drawable.footer_home_icon);
//            this.tab_two.setImageResource(R.drawable.footer_search_icon);
//            this.tab_three.setImageResource(R.drawable.footer_shop_icon);
//            this.tab_four.setImageResource(R.drawable.footer_service_active_icon);
//
//            this.tv_one.setTextColor(getResources().getColor(R.color.text_login_hint_color));
//            this.tv_two.setTextColor(getResources().getColor(R.color.text_login_hint_color));
//            this.tv_three.setTextColor(getResources().getColor(R.color.text_login_hint_color));
//            this.tv_four.setTextColor(getResources().getColor(R.color.bg_theme_color));
//        }

    }


    private void replace(Fragment fragment) {

        FragmentTransaction localFragmentTransaction = getFragmentManager()
                .beginTransaction();
        if (fragment instanceof HomeFragment || fragment instanceof MagicFragment) {
            localFragmentTransaction.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out);
        } else if (animflag) {
            localFragmentTransaction.setCustomAnimations(R.anim.push_right_in, R.anim.push_right_out);
        } else {
        }
        animflag = true;
        localFragmentTransaction.replace(R.id.fragment_container,
                fragment);
        localFragmentTransaction.commitAllowingStateLoss();
    }

    // 切换Fragment
    public void OnTabSelected(String tabName) {

        if (tabName == "tab_one" && !selectfour && !selectfive) {

//            if (fromfour) {
//                animflag = false;
//                toMyFragment(4, "");
//                fromfour = false;
//            } else
            if (fromfive) {
                animflag = false;
                toMyFragment(5, "");
                fromfive = false;
            } else {

                if (null == homeFragment) {
                    homeFragment = new HomeFragment();
                }

                FragmentTransaction localFragmentTransaction = getFragmentManager()
                        .beginTransaction();
                localFragmentTransaction.replace(R.id.fragment_container,
                        homeFragment, "tab_one");
                localFragmentTransaction.commit();

            }
            this.tab_one.setImageResource(R.drawable.footer_home_active_icon);
            this.tab_two.setImageResource(R.drawable.footer_search_icon);
            this.tab_three.setImageResource(R.drawable.footer_shop_icon);
            this.tab_four.setImageResource(R.drawable.footer_service_icon);

            this.tv_one.setTextColor(getResources().getColor(R.color.bg_theme_color));
            this.tv_two.setTextColor(getResources().getColor(R.color.text_login_hint_color));
            this.tv_three.setTextColor(getResources().getColor(R.color.text_login_hint_color));
            this.tv_four.setTextColor(getResources().getColor(R.color.text_login_hint_color));


        } else if (tabName == "tab_two") {

            if (selectfour) {
                selectfour = false;
                fromfour = true;
            }

            if (selectfive) {
                selectfive = false;
                fromfive = true;
            }
            remkeywords = getActivity().getSharedPreferences("keywords", 0);


            magicFragment = new MagicFragment();

            FragmentTransaction localFragmentTransaction = getFragmentManager()
                    .beginTransaction();
            localFragmentTransaction.replace(R.id.fragment_container,
                    magicFragment, "tab_two");
            localFragmentTransaction.commit();


            this.tab_one.setImageResource(R.drawable.footer_home_icon);
            this.tab_two.setImageResource(R.drawable.footer_search_active_icon);
            this.tab_three.setImageResource(R.drawable.footer_shop_icon);
            this.tab_four.setImageResource(R.drawable.footer_service_icon);

            this.tv_one.setTextColor(getResources().getColor(R.color.text_login_hint_color));
            this.tv_two.setTextColor(getResources().getColor(R.color.bg_theme_color));
            this.tv_three.setTextColor(getResources().getColor(R.color.text_login_hint_color));
            this.tv_four.setTextColor(getResources().getColor(R.color.text_login_hint_color));


        } else if (tabName == "tab_three") {
            shopFragment = new ShopFragment();

            FragmentTransaction localFragmentTransaction = getFragmentManager()
                    .beginTransaction();
            localFragmentTransaction.replace(R.id.fragment_container,
                    shopFragment, "tab_three");
            localFragmentTransaction.commit();

            if (selectfour) {
                selectfour = false;
                fromfour = true;
            }

            if (selectfive) {
                selectfive = false;
                fromfive = true;
            }

            this.tab_one.setImageResource(R.drawable.footer_home_icon);
            this.tab_two.setImageResource(R.drawable.footer_search_icon);
            this.tab_three.setImageResource(R.drawable.footer_shop_active_icon);
            this.tab_four.setImageResource(R.drawable.footer_service_icon);

            this.tv_one.setTextColor(getResources().getColor(R.color.text_login_hint_color));
            this.tv_two.setTextColor(getResources().getColor(R.color.text_login_hint_color));
            this.tv_three.setTextColor(getResources().getColor(R.color.bg_theme_color));
            this.tv_four.setTextColor(getResources().getColor(R.color.text_login_hint_color));


        } else if (tabName == "tab_four") {


            if (selectfour) {
                selectfour = false;
                fromfour = true;
            }

            if (selectfive) {
                selectfive = false;
                fromfive = true;
            }

//            if (select_eight) {
//                if(feedBackListFragment!=null){
//                    FragmentTransaction localFragmentTransaction = getFragmentManager()
//                            .beginTransaction();
//                    localFragmentTransaction.replace(R.id.fragment_container,
//                            feedBackListFragment, "tab_four");
//                    localFragmentTransaction.commit();
//                }
//            } else {
//                if (serviceFragment == null)
//                    serviceFragment = new ServiceFragment();
//
//                FragmentTransaction localFragmentTransaction = getFragmentManager()
//                        .beginTransaction();
//                localFragmentTransaction.replace(R.id.fragment_container,
//                        serviceFragment, "tab_four");
//                localFragmentTransaction.commit();
//            }
            if (myOrdersFragment == null)
                myOrdersFragment = new MyOrdersFragment();
            FragmentTransaction localFragmentTransaction = getFragmentManager().beginTransaction();
            localFragmentTransaction.replace(R.id.fragment_container, myOrdersFragment, "tab_four");
            localFragmentTransaction.commit();

            this.tab_one.setImageResource(R.drawable.footer_home_icon);
            this.tab_two.setImageResource(R.drawable.footer_search_icon);
            this.tab_three.setImageResource(R.drawable.footer_shop_icon);
            this.tab_four.setImageResource(R.drawable.footer_service_active_icon);

            this.tv_one.setTextColor(getResources().getColor(R.color.text_login_hint_color));
            this.tv_two.setTextColor(getResources().getColor(R.color.text_login_hint_color));
            this.tv_three.setTextColor(getResources().getColor(R.color.text_login_hint_color));
            this.tv_four.setTextColor(getResources().getColor(R.color.bg_theme_color));
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onResume() {
        super.onResume();

    }

}