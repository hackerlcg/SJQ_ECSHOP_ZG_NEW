package com.ecjia.hamster.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.widget.*;

import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.hamster.activity.DiscountListActivity;
import com.ecjia.hamster.activity.PromotionListActivity;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 类名介绍：促销
 * Created by sun
 * Created time 2017-03-15.
 */
public class MagicFragment extends Fragment{
    private View view;
    private TextView tv_title;
    private LinearLayout ll_discount,ll_magic,ll_promotion;
    private ImageView top_view_back;
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_magic, null);//加载整个Fragment布局

        initView();
		return view;
	}

    private void initView() {
        tv_title= (TextView) view.findViewById(R.id.top_view_text);
        top_view_back= (ImageView) view.findViewById(R.id.top_view_back);
        top_view_back.setVisibility(View.INVISIBLE);
        tv_title.setText(getActivity().getResources().getString(R.string.magic_cube));
        ll_magic= (LinearLayout) view.findViewById(R.id.ll_magic_item);
        ll_discount= (LinearLayout) view.findViewById(R.id.ll_discount_item);
        ll_promotion= (LinearLayout) view.findViewById(R.id.ll_promotion_item);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.width=getDisplayMetricsWidth();
        params.height=getDisplayMetricsWidth()/3;
        ll_magic.setLayoutParams(params);
        ll_discount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DiscountListActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);
            }
        });
        ll_promotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), PromotionListActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
    public int getDisplayMetricsWidth() {
        int i = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        int j = getActivity().getWindowManager().getDefaultDisplay()
                .getHeight();
        return Math.min(i, j);
    }
}
