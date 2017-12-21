package com.ecjia.hamster.model;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ACT {

    private int id;

    private String name;

    private String  image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static ACT fromJson(JSONObject jsonObject) throws JSONException {
        if (null == jsonObject) {
            return null;
        }

        ACT localItem = new ACT();
        JSONArray subItemArray;

        localItem.id = jsonObject.optInt("id");
        localItem.name = jsonObject.optString("name");
        localItem.image = jsonObject.optString("image");
        return localItem;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray = new JSONArray();
        localItemObject.put("id", id);
        localItemObject.put("name", name);
        localItemObject.put("image", image);

        return localItemObject;
    }


}
