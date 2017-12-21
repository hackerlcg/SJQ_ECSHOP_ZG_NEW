package com.ecjia.component.network.query;

import org.json.JSONException;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-03-24.
 */

public class GoodQuery extends PageQuery{
    protected static GoodQuery instance;

    public static GoodQuery getInstance() {
        if (instance == null) {
            synchronized (GoodQuery.class) {
                if (instance == null) {
                    instance = new GoodQuery();
                }
            }
        }
        return instance;
    }

    ////获取商品发布详情-商品编辑
    public String getGoodDetails(String goods_id){
        try {
            requestJsonObject.put("goods_id", goods_id);
        }catch (JSONException e){
            e.getMessage();
        }
        return super.get();
    }

    ////获取商品发布详情-商品图片上传
    public String uploudGoodDetailsImg(String base_img,String order){
        try {
            requestJsonObject.put("base_img", base_img);
            requestJsonObject.put("order", order);
        }catch (JSONException e){
            e.getMessage();
        }
        return super.get();
    }

    ////获取商品发布详情描述-商品图片上传
    public String uploudGoodDescImg(String img){
        try {
            requestJsonObject.put("img", img);
        }catch (JSONException e){
            e.getMessage();
        }
        return super.get();
    }
}
