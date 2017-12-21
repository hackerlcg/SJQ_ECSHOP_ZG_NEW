package com.ecjia.hamster.activity.goods.push.type;

import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ecjia.entity.Category;
import com.ecjia.hamster.activity.goods.ReleaseGoodActivity;
import com.ecjia.hamster.adapter.adapter.CommonAdapter;
import com.ecjia.hamster.adapter.adapter.base.ViewHolder;
import com.ecmoban.android.shopkeeper.sijiqing.R;

import java.util.List;

import gear.yc.com.gearlibrary.rxjava.rxbus.RxBus;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-02-09.
 */

public class PushTypeParentAdapter extends CommonAdapter<Category> {
    private Activity context;
    private String typeName;

    public PushTypeParentAdapter(Activity context, List<Category> datas, String typeName) {
        super(context, R.layout.item_searchparent, datas);
        this.context = context;
        this.typeName = typeName;
    }

    @Override
    public void convert(ViewHolder holder, final Category category, int position) {
        holder.setText(R.id.tv_choose, category.getName());
        holder.setOnClickListener(R.id.rl_choose, v -> {

        });
        RecyclerView mRecyclerView = holder.getView(R.id.mRecyclerView);
        PushTypeChildAdapter adapter = new PushTypeChildAdapter(context, category.getChildren());
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mRecyclerView.setLayoutParams(mParams);
        mRecyclerView.setLayoutManager(new GridLayoutManager(context, 4));
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((view, holder1, position1) -> {
            RxBus.getInstance().post(ReleaseGoodActivity.IN_CAT,
                    category.getChildren().get(position1).getId());
            RxBus.getInstance().post(ReleaseGoodActivity.IN_CAT_STR, typeName+"-"
                    +category.getName()+"-"
                    +category.getChildren().get(position1).getName());
            context.finish();
        });
    }
}
