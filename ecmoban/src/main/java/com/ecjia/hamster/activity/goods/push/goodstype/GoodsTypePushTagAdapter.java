package com.ecjia.hamster.activity.goods.push.goodstype;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ecjia.entity.Species;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * ecjia-shopkeeper-android
 * Created by YichenZ on 2017/3/22 10:02.
 */

public class GoodsTypePushTagAdapter extends TagAdapter<Species.SpeciBean.CatBean.Value> {
    Context mContext;
    private Set<Integer> pos = new HashSet<>();

    public GoodsTypePushTagAdapter(Context mContext, List<Species.SpeciBean.CatBean.Value> datas) {
        super(datas);
        this.mContext = mContext;
    }

    @Override
    public View getView(FlowLayout parent, int position, Species.SpeciBean.CatBean.Value value) {
        TextView tv = (TextView) LayoutInflater.from(mContext).inflate(R.layout.tv,
                parent, false);
        tv.setText(value.getValue());
            if ("1".equals(value.getValue_show())) {
                pos.add(position);
            }
        return tv;
    }

    public void showCheck() {
        setSelectedList(pos);
    }
}
