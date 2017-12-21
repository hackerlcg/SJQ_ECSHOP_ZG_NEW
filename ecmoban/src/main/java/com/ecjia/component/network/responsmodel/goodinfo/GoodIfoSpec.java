package com.ecjia.component.network.responsmodel.goodinfo;

import java.io.Serializable;
import java.util.List;

/**
 * 类名介绍：商品规格
 * Created by sun
 * Created time 2017-03-24.
 */

public class GoodIfoSpec implements Serializable {
    /**
     * cat_id : 1
     * cat_name : 女装
     * cat : [{"attr_id":81,"attr_name":"颜色","type":"1","values":["红","黄","蓝"]},{"attr_id":82,"attr_name":"尺寸","type":"1","values":["S","M","L","XL","XXL"]}]
     */

    private String cat_id;
    private String cat_name;
    private List<GoodIfoSpecChild> cat;

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public List<GoodIfoSpecChild> getCat() {
        return cat;
    }

    public void setCat(List<GoodIfoSpecChild> cat) {
        this.cat = cat;
    }
}
