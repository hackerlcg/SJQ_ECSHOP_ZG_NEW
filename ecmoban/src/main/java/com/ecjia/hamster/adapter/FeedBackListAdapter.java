package com.ecjia.hamster.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.hamster.activity.FeedBackDetailActivity;
import com.ecjia.hamster.model.SERVICE_MESSAGE;
import com.ecjia.util.LG;
import com.ecjia.util.MyBitmapUtils;
import com.ecjia.util.TimeUtil;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/1/5.
 */
public class FeedBackListAdapter extends BaseAdapter {
    private Context context;
    private String type;
    private ArrayList<SERVICE_MESSAGE> service_messages = new ArrayList<SERVICE_MESSAGE>();
    private MyBitmapUtils myBitmapUtils;
    private int size;
    private int distance;
    public FeedBackListAdapter(Context context, ArrayList<SERVICE_MESSAGE> messages) {
        this.context = context;
        service_messages = messages;
        myBitmapUtils = MyBitmapUtils.getInstance(context);
        size = messages.size();
        distance= (int) context.getResources().getDimension(R.dimen.dp_80);
    }


    public ArrayList<SERVICE_MESSAGE> getService_messages() {
        return service_messages;
    }

    public void setService_messages(ArrayList<SERVICE_MESSAGE> service_messages) {
        this.service_messages = service_messages;
    }

    @Override
    public int getCount() {
        return service_messages.size();
    }

    @Override
    public Object getItem(int i) {
        return service_messages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        final SERVICE_MESSAGE message = service_messages.get(i);
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.feed_back_item, null);
            holder = new ViewHolder();
            holder.iv_user_img = (ImageView) view.findViewById(R.id.user_img);
            holder.tv_user_name = (TextView) view.findViewById(R.id.user_name);
            holder.tv_time = (TextView) view.findViewById(R.id.message_time);
            holder.tv_content = (TextView) view.findViewById(R.id.message_content);
            holder.tv_message_num = (TextView) view.findViewById(R.id.message_num);
            holder.top_line = view.findViewById(R.id.top_line);
            holder.short_line = view.findViewById(R.id.buttom_short_line);
            holder.long_line = view.findViewById(R.id.buttom_long_line);
            holder.item = (LinearLayout) view.findViewById(R.id.feed_back_item);
            holder.left_item= (LinearLayout) view.findViewById(R.id.item_left);
            holder.right_item= (LinearLayout) view.findViewById(R.id.item_right);
            holder.item_delete= (RelativeLayout) view.findViewById(R.id.item_delete);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        holder.left_item.setLayoutParams(lp1);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(distance,
                distance);
        holder.right_item.setLayoutParams(lp2);

        myBitmapUtils.displaySmallImage(holder.iv_user_img, message.getAvatar_img());
        holder.tv_user_name.setText(message.getUser_name());
        if("udid".equals(message.getMessage_type())){
            holder.tv_user_name.setText(context.getString(R.string.anonymity));
        }else{
            holder.tv_user_name.setText(message.getUser_name());
        }
        holder.tv_time.setText(TimeUtil.getFomartDate(message.getFormatted_time(), context.getResources()));
        holder.tv_content.setText(message.getContent());
        if (message.getMessages() > 0) {
            holder.tv_message_num.setVisibility(View.VISIBLE);
            if (message.getMessages() < 99) {
                holder.tv_message_num.setText("" + message.getMessages());
            } else {
                holder.tv_message_num.setText(99 + "+");
            }
        } else {
            holder.tv_message_num.setVisibility(View.GONE);
        }
        if (size == 1) {
            holder.top_line.setVisibility(View.VISIBLE);
            holder.short_line.setVisibility(View.GONE);
            holder.long_line.setVisibility(View.VISIBLE);
        } else {

            if (i == 0) {
                holder.top_line.setVisibility(View.VISIBLE);
                holder.short_line.setVisibility(View.VISIBLE);
                holder.long_line.setVisibility(View.GONE);
            } else {
                holder.top_line.setVisibility(View.GONE);
                if (i < size - 1) {
                    holder.short_line.setVisibility(View.VISIBLE);
                    holder.long_line.setVisibility(View.GONE);
                } else {
                    holder.short_line.setVisibility(View.GONE);
                    holder.long_line.setVisibility(View.VISIBLE);
                }
            }
        }
        holder.item_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onRightItemClick(view, i);
                }
            }
        });
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FeedBackDetailActivity.class);
                intent.putExtra("feedback_id", message.getFeedback_id());
                intent.putExtra("type", type);
                intent.putExtra("position",i);
                LG.e("feedback_id=="+message.getFeedback_id()+"type=="+type+"position=="+i);
                context.startActivity(intent);
                ((Activity)context).overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);
                if(message.getMessages()!=0) {
                    message.setMessages(0);
                    FeedBackSql.getIntence(context).setZeroMessageNum(message.getFeedback_id());
                }
                notifyDataSetChanged();
            }
        });
        return view;
    }

    private class ViewHolder {
        private ImageView iv_user_img;
        private TextView tv_user_name, tv_time, tv_content, tv_message_num;
        private View top_line, short_line, long_line;
        private LinearLayout item;
        private LinearLayout left_item,right_item;
        private RelativeLayout item_delete;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public void notifyDataSetChanged() {
        size = service_messages.size();
        super.notifyDataSetChanged();
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
