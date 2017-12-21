package com.ecjia.util;

import com.ecjia.entity.Species;
import com.ecjia.util.common.StringUtils;

import java.util.List;

/**
 * ecjia-shopkeeper-android
 * 处理字符串
 * Created by YichenZ on 2017/3/24 19:49.
 */

public class StringSpeciBeanUtil {

    public static String getSpecString(List<Species.SpeciBean.CatBean> cat) {
        if(cat.size()<=0){
            return "";
        }
        StringBuffer str = new StringBuffer();
        for (Species.SpeciBean.CatBean v : cat) {
            str.append(getSelectString(v.getValues()) + "/");
        }
        return str.deleteCharAt(str.length() - 1).toString();
    }


    public static String getSelectString(List<Species.SpeciBean.CatBean.Value> values) {
        if(values.size()<=0){
            return "";
        }
        StringBuffer str = new StringBuffer();
        for (Species.SpeciBean.CatBean.Value v : values) {
            str.append(v.getValue() + ",");
        }
        return   "[" + StringUtils.getLengthMax(str.deleteCharAt(str.length() - 1).toString(), 6) + "]";
    }
}
