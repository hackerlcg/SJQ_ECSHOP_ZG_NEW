
package com.ecjia.hamster.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CATEGORY
{
    private String cat_name; //分类名称
    private int cat_id;  //分类id
    private int parent_id;  //父级id
    private int level;  //级别
    private boolean have_child;  //有子集
    private boolean choose;  //选中
    private String pinyin;//转拼音
    private String all_parent; //所有父类

    public boolean isChoose() {
        return choose;
    }

    public void setChoose(boolean choose) {
        this.choose = choose;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public boolean isHave_child() {
        return have_child;
    }

    public void setHave_child(boolean have_child) {
        this.have_child = have_child;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public int getCat_id() {
        return cat_id;
    }

    public void setCat_id(int cat_id) {
        this.cat_id = cat_id;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getAll_parent() {
        return all_parent;
    }

    public void setAll_parent(String all_parent) {
        this.all_parent = all_parent;
    }

    public static CATEGORY fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return null;
        }

        CATEGORY localItem = new CATEGORY();

        JSONArray subItemArray;

        localItem.cat_name = jsonObject.optString("cat_name");
        localItem.cat_id = jsonObject.optInt("cat_id");
        localItem.parent_id = jsonObject.optInt("parent_id");
        localItem.level = jsonObject.optInt("level");
        localItem.have_child = jsonObject.optBoolean("have");
        localItem.pinyin = jsonObject.optString("pinyin");
        localItem.all_parent = jsonObject.optString("all_parent");
        return localItem;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray = new JSONArray();
        localItemObject.put("cat_name", cat_name);
        localItemObject.put("cat_id", cat_id);
        localItemObject.put("parent_id", parent_id);
        localItemObject.put("level", level);
        localItemObject.put("have", have_child);
        localItemObject.put("pinyin", pinyin);
        localItemObject.put("all_parent", all_parent);
        return localItemObject;
    }

}
