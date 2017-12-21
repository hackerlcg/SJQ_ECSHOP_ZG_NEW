package com.ecjia.component.network.responsmodel.goodinfo;

import java.io.Serializable;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-03-24.
 */

public class GoodInfoImage implements Serializable {
//    { //图片集合
//        "img_id":331, //图片id
//        "url":"http://mat1.gtimg.com/sports/nba/logo/1602/2.png" //图片url
//        }

    private String img_id;
    private String url;
    private int order;

    public String getImg_id() {
        return img_id;
    }

    public void setImg_id(String img_id) {
        this.img_id = img_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
