package com.ecjia.hamster.adapter;


import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ecjia.hamster.activity.goods.ReleaseGoodActivity_Builder;
import com.ecjia.hamster.model.GOODS;
import com.ecjia.util.ImageLoaderForLV;
import com.ecjia.util.TimeUtil;
import com.ecmoban.android.shopkeeper.sijiqing.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GoodsListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Context context;
    public ArrayList<GOODS> lists;

    public int flag = 1;
    public int type = 1;
    private String stype;

    public int mRightWidth = 0;
    public static Map<Integer, Boolean> isSelected = new HashMap<Integer, Boolean>();


    public GoodsListAdapter(Context context, ArrayList<GOODS> lists, int type, int flag, int RightViewWidth) {
        this.context = context;
        this.lists = lists;
        this.flag = flag;
        this.type = type;
        this.mRightWidth = RightViewWidth;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    ViewHolder holder;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final GOODS goods = lists.get(position);

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.goods_item, null);

            holder.image = (ImageView) convertView.findViewById(R.id.iv_good);
            holder.iv_promote = (ImageView) convertView.findViewById(R.id.iv_promote);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_status = (TextView) convertView.findViewById(R.id.tv_status);
            holder.tv_status_str = (TextView) convertView.findViewById(R.id.tv_status_str);
            holder.tv_account = (TextView) convertView.findViewById(R.id.tv_account);
            holder.tv_sale = (TextView) convertView.findViewById(R.id.tv_sale);
            holder.tv_click = (TextView) convertView.findViewById(R.id.tv_click);
            holder.first = (TextView) convertView.findViewById(R.id.first);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.bottom_short_line = (View) convertView.findViewById(R.id.bottom_short_line);
            holder.bottom_long_line = (View) convertView.findViewById(R.id.bottom_long_line);
            holder.shop_car_check_item = (LinearLayout) convertView.findViewById(R.id.shop_car_check_item);
            holder.item_right = (LinearLayout) convertView.findViewById(R.id.item_right);
            holder.item_left = (LinearLayout) convertView.findViewById(R.id.goods_item_left);
            holder.selecter_item_left = (LinearLayout) convertView.findViewById(R.id.selecter_item_left);
            holder.ll_delete = (RelativeLayout) convertView.findViewById(R.id.ll_delete);
            holder.shop_car_check_item = (LinearLayout) convertView.findViewById(R.id.shop_car_check_item);
            holder.ll_sale = (RelativeLayout) convertView.findViewById(R.id.ll_sale);
            holder.ll_edit = (RelativeLayout) convertView.findViewById(R.id.ll_edit);
            holder.del = (CheckBox) convertView.findViewById(R.id.shop_car_item_del);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        holder.item_left.setLayoutParams(lp1);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(mRightWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        holder.item_right.setLayoutParams(lp2);


        if (position == lists.size() - 1) {
            holder.bottom_short_line.setVisibility(View.GONE);
            holder.bottom_long_line.setVisibility(View.VISIBLE);
        } else {
            holder.bottom_short_line.setVisibility(View.VISIBLE);
            holder.bottom_long_line.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(goods.getFormatted_promote_start_date())
                || TextUtils.isEmpty(goods.getFormatted_promote_end_date())) {
            holder.iv_promote.setVisibility(View.GONE);
            holder.tv_account.setText(goods.getShop_price());
        } else {
            switch (TimeUtil.compare_promotion(goods.getFormatted_promote_start_date(), goods.getFormatted_promote_end_date())) {
                case 0:
                    holder.iv_promote.setVisibility(View.GONE);
                    holder.tv_account.setText(goods.getShop_price());
                    break;
                case 1:
                    holder.iv_promote.setVisibility(View.VISIBLE);
                    holder.iv_promote.setImageResource(R.drawable.promotion_offline);
                    holder.tv_account.setText(goods.getShop_price());
                    break;
                case 2:
                    holder.iv_promote.setVisibility(View.VISIBLE);
                    holder.iv_promote.setImageResource(R.drawable.promote_list);
                    holder.tv_account.setText(goods.getPromote_price());
                    break;
                case 3:
                    holder.iv_promote.setVisibility(View.VISIBLE);
                    holder.iv_promote.setImageResource(R.drawable.promotion_offline);
                    holder.tv_account.setText(goods.getShop_price());
                    break;
            }
        }

        holder.tv_name.setText(goods.getName());
        //审核状态颜色变更
        holder.tv_status.setText(goods.getStatusStr());
        holder.tv_status_str.setVisibility(View.GONE);
        switch (goods.getStatusColor()){
            case 5:
            case 1:
                holder.tv_status.setTextColor(0xFFFDA166);
                break;
            case 2:
                holder.tv_status_str.setVisibility(View.VISIBLE);
                holder.tv_status.setTextColor(0xFFFB4A49);
                break;
            case 3:
            case 4:
                holder.tv_status.setTextColor(0xFF29D37D);
                break;
        }


        //审核未通过文字变色
        ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
        SpannableStringBuilder builder = new SpannableStringBuilder("审核未通过原因："+goods.getReview_content());
        builder.setSpan(redSpan, 0, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.tv_status_str.setText(builder);

        holder.tv_click.setText(context.getResources().getString(R.string.click_times) + goods.getClicks());
        holder.first.setText("库存数: "+goods.getStock());
        String time = goods.getTime();
        if (time.length() > 10) {
            time = time.substring(0, 11);
        }
        holder.tv_time.setText(time);

        switch (type) {
            case 1:
                holder.ll_sale.setVisibility(View.VISIBLE);
                holder.tv_sale.setText(context.getResources().getString(R.string.off_sale));
                stype = "offline";
                break;
            case 2:
                holder.ll_sale.setVisibility(View.VISIBLE);
                holder.tv_sale.setText(context.getResources().getString(R.string.to_on_sale));
                stype = "online";
                break;
            case 3:
                holder.ll_sale.setVisibility(View.GONE);
                holder.tv_sale.setText("");
                break;
        }

        isSelected.put(position, false);
        // 设置隐藏可见
        holder.shop_car_check_item = (LinearLayout) convertView
                .findViewById(R.id.shop_car_check_item);

        if (flag == 1) {
            holder.shop_car_check_item.setVisibility(View.GONE);
        } else if (flag == 2) {
            holder.shop_car_check_item.setVisibility(View.VISIBLE);
        }

        ImageLoaderForLV.getInstance(context).setImageRes(holder.image, goods.getImg().getThumb());

        //item里的收藏删除按钮
        holder.ll_delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onRightItemClick(view, position);
                }
            }
        });

        holder.selecter_item_left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onRightItemClick(view, position);
                }
            }
        });

        //item里的上下架按钮
        holder.ll_sale.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onRightItemClick(view, position);
                }
            }
        });

        //item里的编辑按钮
        holder.ll_edit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ReleaseGoodActivity_Builder.intent(context).goodId(goods.getId()).start();
            }
        });

        holder.shop_car_check_item.setOnClickListener(new OnClickListener() {

            @Override

            public void onClick(View v) {
                if (goods.isChoose() == true) {
                    goods.setChoose(false);
                } else {
                    goods.setChoose(true);
                }
                GoodsListAdapter.this.notifyDataSetChanged();
            }
        });

        boolean checkcb = goods.isChoose();
        holder.del.setChecked(checkcb);
        if (checkcb) {
            holder.selecter_item_left.setBackgroundColor(context.getResources().getColor(R.color.BgColor));
        } else {
            if (flag == 2) {
                holder.selecter_item_left.setBackgroundColor(context.getResources().getColor(R.color.white));
            } else {
                holder.selecter_item_left.setBackgroundResource(R.drawable.selecter_newitem_press);
            }
        }

        return convertView;
    }


    class ViewHolder {

        private ImageView image, iv_promote;
        private View bottom_short_line, bottom_long_line;
        private TextView tv_name, tv_status, tv_status_str, tv_account, tv_click, first, tv_time, tv_sale;
        private CheckBox del;
        private LinearLayout shop_car_check_item;
        private LinearLayout item_right, item_left, selecter_item_left;
        private RelativeLayout ll_delete, ll_sale, ll_edit;

    }


    /**
     * 单击事件监听器
     */
    private onRightItemClickListener mListener = null;

    public void setOnRightItemClickListener(onRightItemClickListener listener) {
        mListener = listener;
    }

    public interface onRightItemClickListener {
        void onRightItemClick(View v, int position);
    }
}
