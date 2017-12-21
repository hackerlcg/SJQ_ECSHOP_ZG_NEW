package com.ecjia.hamster.activity.goods.push.goodsinfo;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.ecjia.component.network.responsmodel.goodinfo.GoodDescImage;
import com.ecjia.util.ImageLoaderForLV;
import com.ecmoban.android.shopkeeper.sijiqing.R;

import java.util.List;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-04-02.
 */

public class GoodPushImgGridAdapter extends BaseAdapter {

    private LayoutInflater inflater; // 视图容器
    private Context context;
    private int selectedPosition = -1;// 选中的位置
    private boolean shape;
    private List<GoodDescImage> listImg;
    public int maxSize=0;

    public boolean isShape() {
        return shape;
    }

    public void setShape(boolean shape) {
        this.shape = shape;
    }

    public GoodPushImgGridAdapter(Context context, List<GoodDescImage> listImg) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.listImg = listImg;
        maxSize=listImg.size();
    }

    public void update() {
        loading();
    }

    public int getCount() {
        return (listImg.size() + 1);
    }

    public Object getItem(int arg0) {
        return null;
    }

    public long getItemId(int arg0) {
        return 0;
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public  List<GoodDescImage>  getListImg(){
        return listImg;
    }

    /**
     * ListView Item设置
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        final int coord = position;
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.cho_item_published_grida, parent, false);
            holder = new GoodPushImgGridAdapter.ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.imageDel.setTag(position);
        if (position == listImg.size()) {
            //holder.image.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.cho_icon_addpic_unfocused));
            ImageLoaderForLV.getInstance(context).setImageRes(holder.image, "drawable://"+R.drawable.cho_icon_addpic_unfocused);
            if (position == 9) {
                holder.image.setVisibility(View.GONE);
            }
            holder.imageDel.setVisibility(View.GONE);
        } else {
            if("0".equals(listImg.get(position).getFile_id())){
                ImageLoaderForLV.getInstance(context).setImageRes(holder.image, "file://"+listImg.get(position).getFile_path() + "");
            }else {
                ImageLoaderForLV.getInstance(context).setImageRes(holder.image, listImg.get(position).getFile_path() + "");
            }
            //holder.image.setImageBitmap(listImg.get(position));
            holder.imageDel.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    public class ViewHolder {
        public ImageView image;
        public ImageView imageDel;

        public ViewHolder(View convertView) {
            image = (ImageView) convertView.findViewById(R.id.item_grida_image);
            imageDel = (ImageView) convertView.findViewById(R.id.item_del);
            imageDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int) imageDel.getTag();
                    if(listImg.size()==1){
                        listImg.clear();
                        maxSize=0;
                    }else{
                        listImg.remove(position);
                        maxSize--;
                    }
                    update();
                }
            });
        }
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    notifyDataSetChanged();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public void loading() {
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    if (maxSize == listImg.size()) {
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                        break;
                    } else {
                        maxSize += 1;
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    }
                }
            }
        }).start();
    }
}
