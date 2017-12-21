package com.ecjia.component.network.responsmodel.goodinfo;

import java.io.Serializable;
import java.util.List;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-03-24.
 */

public class GoodIfoSpecChild implements Serializable {
    //    {"attr_id":81,"attr_name":"颜色","type":"1","values":["红","黄","蓝"]}
    private String attrId;
    private String attrName;
    private String type;
    private List<String> values;

    public String getAttrId() {
        return attrId;
    }

    public void setAttrId(String attrId) {
        this.attrId = attrId;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }
}
