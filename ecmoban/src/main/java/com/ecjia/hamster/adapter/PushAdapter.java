package com.ecjia.hamster.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecjia.component.view.CYTextView;
import com.ecjia.hamster.model.MYMESSAGE;
import com.ecmoban.android.shopkeeper.sijiqing.R;
import com.ecjia.util.TimeUtil;

import java.util.ArrayList;

/**
 * Created by Administrator on 2014/12/12.
 */
public class PushAdapter extends BaseAdapter{
    public ArrayList<MYMESSAGE> list;
    private Context context;
    public PushAdapter(Context context, ArrayList<MYMESSAGE> list){
        this.context=context;
        this.list=list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        final MYMESSAGE msg=list.get(position);
        if(view==null){
            holder=new ViewHolder();
            view= LayoutInflater.from(context).inflate(R.layout.pushmsg_item,null);
            holder.tv_push_title= (TextView) view.findViewById(R.id.tv_push_title);
            holder.tv_push_time= (TextView) view.findViewById(R.id.tv_push_time);
            holder.tv_push_content= (CYTextView) view.findViewById(R.id.tv_push_content);
            holder.ll_watch= (LinearLayout) view.findViewById(R.id.ll_watch);
            holder.iv_read= (ImageView) view.findViewById(R.id.iv_read);
            holder.iv_good= (ImageView) view.findViewById(R.id.iv_good);
           view.setTag(holder);
        }else{
            holder= (ViewHolder) view.getTag();
        }

        holder.tv_push_title.setText(msg.getTitle());
        holder.tv_push_time.setText(TimeUtil.getFomartNewsTopTime(msg.getTime()));
        holder.tv_push_content.SetText(msg.getContent());

        if("goods_detail".equals(msg.getOpen_type())||"goods_list".equals(msg.getOpen_type())){
            holder.iv_good.setImageResource(R.drawable.goods_msg);
        }else if ("orders_detail".equals(msg.getOpen_type())||"orders_list".equals(msg.getOpen_type())){
            holder.iv_good.setImageResource(R.drawable.orders_msg);
        }else {
            holder.iv_good.setImageResource(R.drawable.default_image);
        }

        if(msg.getReadStatus()==0){
            holder.iv_read.setVisibility(View.VISIBLE);
        }else{
            holder.iv_read.setVisibility(View.GONE);
        }

        holder.ll_watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onRightItemClick(view, position);
                }
            }
        });

        return view;
    }
    class ViewHolder{
        private TextView tv_push_title,tv_push_time;
        private CYTextView tv_push_content;
        private LinearLayout ll_watch;
        private ImageView iv_good,iv_read;
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
