package com.ecjia.component.network.responsmodel.goodinfo;

import java.io.Serializable;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-03-24.
 */

public class GoodInfoBase implements Serializable {

    private GoodInfoDetail goods_info;

    public GoodInfoDetail getGoods_info() {
        return goods_info;
    }

    public void setGoods_info(GoodInfoDetail goods_info) {
        this.goods_info = goods_info;
    }
}
