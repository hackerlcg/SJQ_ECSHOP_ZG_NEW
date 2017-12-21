package com.ecjia.hamster.activity.goods.push.type;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ecjia.entity.Category;
import com.ecjia.hamster.adapter.adapter.CommonAdapter;
import com.ecjia.hamster.adapter.adapter.base.ViewHolder;
import com.ecjia.util.ImageLoaderForLV;
import com.ecjia.util.common.DensityUtil;
import com.ecmoban.android.shopkeeper.sijiqing.R;

import java.util.List;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-02-10.
 */

public class PushTypeChildAdapter extends CommonAdapter<Category> {
    private Context context;
    private int distance;

    public PushTypeChildAdapter(Context context, List<Category> datas) {
        super(context, R.layout.gridview_item, datas);
        this.context = context;
        distance = (int) context.getResources().getDimension(R.dimen.seven_dp);
    }

    @Override
    public void convert(ViewHolder holder, Category category, int position) {
        ImageView img = holder.getView(R.id.iv_img);
        holder.setText(R.id.tv_text, category.getName());
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
                DensityUtil.getDisplayMetricsWidth(context) *  1 / 4 - distance,
                DensityUtil.getDisplayMetricsWidth(context) * 1 / 4 - distance);
        img.setLayoutParams(mParams);
        ImageLoaderForLV.getInstance(context).setImageRes(img, category.getImage());
    }
}
