
package com.ecjia.hamster.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class REGIONS
{
    private String name; //分类名称
    private int id;  //分类id
    private int parent_id;  //父级id
    private int level;  //级别
    private boolean have_child;  //有子集
    private boolean choose;  //选中

    public boolean isChoose() {
        return choose;
    }

    public void setChoose(boolean choose) {
        this.choose = choose;
    }

    public boolean isHave_child() {
        return have_child;
    }

    public void setHave_child(boolean have_child) {
        this.have_child = have_child;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public static REGIONS fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return null;
        }

        REGIONS localItem = new REGIONS();

        JSONArray subItemArray;

        localItem.name = jsonObject.optString("name");
        localItem.id = jsonObject.optInt("id");
        localItem.parent_id = jsonObject.optInt("parent_id");
        localItem.level = jsonObject.optInt("level");
        localItem.have_child = jsonObject.optBoolean("have");
        return localItem;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray = new JSONArray();
        localItemObject.put("name", name);
        localItemObject.put("id", id);
        localItemObject.put("parent_id", parent_id);
        localItemObject.put("level", level);
        localItemObject.put("have", have_child);
        return localItemObject;
    }

}
