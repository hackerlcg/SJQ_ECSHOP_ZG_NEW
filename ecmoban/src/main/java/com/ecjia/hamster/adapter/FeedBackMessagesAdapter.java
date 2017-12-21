package com.ecjia.hamster.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.hamster.activity.ServiceDetailActivity;
import com.ecjia.hamster.model.FEEDBACK_MESSAGE;
import com.ecjia.util.LG;
import com.ecjia.util.MyBitmapUtils;
import com.ecjia.util.TimeUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/1/6.
 */
public class FeedBackMessagesAdapter extends BaseAdapter {

    public interface ConsultViewType {
        int FROM_BUSINESS = 0;
        int FROM_CUSTOM = 1;
    }

    public MyBitmapUtils myBitmapUtils;
    private ArrayList<FEEDBACK_MESSAGE> consultionList;
    private String img_url = "";
    private Context ctx;
    private LayoutInflater mInflater;
    private final static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private long now_time, last_time;
    private Resources res;

    public FeedBackMessagesAdapter(Context context, ArrayList<FEEDBACK_MESSAGE> consultionList) {
        ctx = context;
        this.consultionList = consultionList;
        mInflater = LayoutInflater.from(context);
        myBitmapUtils = MyBitmapUtils.getInstance(context);
        res = context.getResources();
    }

    public int getCount() {
        return consultionList.size();
    }

    public Object getItem(int position) {
        return consultionList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public ArrayList<FEEDBACK_MESSAGE> getConsultionList() {
        return consultionList;
    }

    public void setConsultionList(ArrayList<FEEDBACK_MESSAGE> consultionList) {
        this.consultionList = consultionList;
    }

    public int getItemViewType(int position) {
        FEEDBACK_MESSAGE consultion = consultionList.get(position);
        if (1 == Integer.valueOf(consultion.getIs_myself())) {
            return ConsultViewType.FROM_CUSTOM;
        } else {
            return ConsultViewType.FROM_BUSINESS;
        }
    }

    public int getViewTypeCount() {
        return 2;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        if (consultionList.size() == 0) {
            return null;
        }
        FEEDBACK_MESSAGE consultion = consultionList.get(position);

        int isComMsg = Integer.valueOf(consultion.getIs_myself());
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.consult_item, null);
            viewHolder.layout_business = (LinearLayout) convertView.findViewById(R.id.consult_item_business);
            viewHolder.layout_custom = (LinearLayout) convertView.findViewById(R.id.consult_item_custom);
            viewHolder.tvContent_custom = (TextView) convertView.findViewById(R.id.tv_chatcontent_custom);
            viewHolder.iv_userhead_custom = (ImageView) convertView.findViewById(R.id.iv_userhead_custom);
            viewHolder.tvContent_business = (TextView) convertView.findViewById(R.id.tv_chatcontent_business);
            viewHolder.iv_userhead_business = (ImageView) convertView.findViewById(R.id.iv_userhead_business);
            viewHolder.tv_business_time = (TextView) convertView.findViewById(R.id.business_title_time);
            viewHolder.tv_custom_time = (TextView) convertView.findViewById(R.id.custom_title_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        LG.e("now_time==" + consultion.getFormatted_time());
        try {
            now_time = (format.parse(consultion.getFormatted_time()).getTime()) / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (position == 0) {
            if (isComMsg == 1) {
                viewHolder.tv_custom_time.setVisibility(View.VISIBLE);
                viewHolder.tv_custom_time.setText(TimeUtil.getFeedBackFormattedDate(consultion.getFormatted_time(), res));
            } else {
                viewHolder.tv_business_time.setVisibility(View.VISIBLE);
                viewHolder.tv_business_time.setText(TimeUtil.getFeedBackFormattedDate(consultion.getFormatted_time(), res));
            }
        } else {
            try {
                if (position > 0)
                    last_time = (format.parse(consultionList.get(position-1).getFormatted_time()).getTime()) / 1000;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if ((now_time - last_time)  <= 180 ) {
                viewHolder.tv_business_time.setVisibility(View.GONE);
                viewHolder.tv_custom_time.setVisibility(View.GONE);
                LG.e("now_time-last_time===" + ((now_time - last_time)));
            } else {
                if (isComMsg == 1) {
                    viewHolder.tv_custom_time.setVisibility(View.VISIBLE);
                    viewHolder.tv_custom_time.setText(TimeUtil.getFeedBackFormattedDate(consultion.getFormatted_time(), res));
                } else {
                    viewHolder.tv_business_time.setVisibility(View.VISIBLE);
                    viewHolder.tv_business_time.setText(TimeUtil.getFeedBackFormattedDate(consultion.getFormatted_time(), res));
                }
            }
        }

        if (isComMsg == 1) {
            viewHolder.layout_custom.setVisibility(View.VISIBLE);
            viewHolder.layout_business.setVisibility(View.GONE);
            viewHolder.tvContent_custom.setText(consultion.getContent());
            setUserHead(viewHolder.iv_userhead_custom, isComMsg);
        } else {
            viewHolder.layout_custom.setVisibility(View.GONE);
            viewHolder.layout_business.setVisibility(View.VISIBLE);
            viewHolder.tvContent_business.setText(consultion.getContent());
            setUserHead(viewHolder.iv_userhead_business, isComMsg);
        }
        viewHolder.iv_userhead_custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx, ServiceDetailActivity.class);
                ctx.startActivity(intent);
            }
        });
        viewHolder.iv_userhead_business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(ctx, CustomerDetailActivity.class);
//                ctx.startActivity(intent);
            }
        });
        return convertView;

    }

    private class ViewHolder {
        public TextView tvContent_business, tvContent_custom, tv_business_time, tv_custom_time;
        public LinearLayout layout_business, layout_custom;
        public ImageView iv_userhead_business, iv_userhead_custom;
    }

    public void setUserHead(ImageView imageView, int type) {
        if (type == 1) {
            imageView.setImageResource(R.drawable.ecmoban_logo);
        } else if (type == 0) {
            myBitmapUtils.displaySmallImage(imageView, img_url);
        }
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}
