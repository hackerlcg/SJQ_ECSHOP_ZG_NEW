package com.ecjia.util.common;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by hank on 2016/1/13/15:15:11
 * GSON解析工具
 */
public class GsonUtils {

    private static GsonUtils gsonUtils;
    private Gson gson;

    private GsonUtils() {
        gson = new Gson();
    }

    public static GsonUtils getInstance() {
        if (gsonUtils == null) {
            gsonUtils = new GsonUtils();
        }
        return gsonUtils;
    }

    public String toJson(Object object) {
        return gson.toJson(object);
    }

    /**
     * 使用gson工具把数据转换成 HTTP请求的Json
     *
     * @param value 是对解析的集合的类型
     */
    public String toHttpGsonString(Object value) {
        return toJson(value).replace("\\","").replace("\"{","{").replace("}\"","}");
    }

    public <D> D getObject(String json, Class<D> d) {

        return gson.fromJson(json, d);
    }

    public Map<String, String> getMapString(String json) {
        return gson.fromJson(json, new TypeToken<Map<String, String>>() {
        }.getType());
    }

    public Map<String, String> getMapString(Object object) {
        return gson.fromJson(toJson(object), new TypeToken<Map<String, String>>() {
        }.getType());
    }

    //解析简单Json数据
    public <T> T parseJsonToEntity(String jsonData,Class<T> entityType){
        return gson.fromJson(jsonData, entityType);
    }

    //解析JsonArray数据
    public <T> ArrayList<T> parseJsonArray(String jsonArrayData,Class<T> entityType){
        return gson.fromJson(jsonArrayData,new TypeToken<ArrayList<T>>(){}.getType());
    }

}
