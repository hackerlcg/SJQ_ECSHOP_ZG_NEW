package com.ecjia.hamster.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Kevin Song on 2014/08/19.
 */
public class MYMESSAGE {

    public static final int UNREAD = 0;
    public static final int HAVEREAD = 1;

    private String title;
    private String content;
    private String custom;
    private String time;
    private String type;
    private String url;
    private String gotoActivity;
    private String msg_id;
    private String date;

    private String open_type;//打开方式
    private String category_id;//商品分类ID
    private String webUrl;//webView的URL
    private String goods_id_comment;//商品评论的ID
    private String goods_id;//商品详情的ID
    private String order_id;//订单详情的ID
    private String keyword;//关键词

    public int getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(int readStatus) {
        this.readStatus = readStatus;
    }

    private int readStatus;//读取状态

    public MYMESSAGE() {

    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getGoods_id_comment() {
        return goods_id_comment;
    }

    public void setGoods_id_comment(String goods_id_comment) {
        this.goods_id_comment = goods_id_comment;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOpen_type() {
        return open_type;
    }

    public void setOpen_type(String open_type) {
        this.open_type = open_type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getGotoActivity() {
        return gotoActivity;
    }

    public void setGotoActivity(String gotoActivity) {
        this.gotoActivity = gotoActivity;
    }

    public String getCustom() {
        return custom;
    }

    public void setCustom(String custom) {
        this.custom = custom;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public JSONObject toJson() throws JSONException {

        JSONObject localItemObject = new JSONObject();

        localItemObject.put("title", title);

        localItemObject.put("content", content);

        localItemObject.put("custom", custom);

        localItemObject.put("time", time);

        localItemObject.put("type", type);

        localItemObject.put("url", url);

        localItemObject.put("gotoActivity", gotoActivity);

        localItemObject.put("msg_id", msg_id);

        localItemObject.put("open_type", open_type);//打开方式

        localItemObject.put("category_id", category_id);//商品分类

        localItemObject.put("webUrl", webUrl); //webView的Url

        localItemObject.put("goods_id_comment", goods_id_comment);//商品评论的ID

        localItemObject.put("goods_id", goods_id);//商品详情的ID

        localItemObject.put("order_id", order_id);//订单详情的ID

        localItemObject.put("readStatus", readStatus);//消息是否已读

        localItemObject.put("keyword", keyword);//关键词

        return localItemObject;
    }

    public static MYMESSAGE fromJson(JSONObject jsonObject) throws JSONException {
        if (null == jsonObject) {
            return null;
        }

        MYMESSAGE localItem = new MYMESSAGE();

        JSONArray subItemArray;

        localItem.title = jsonObject.optString("title");

        localItem.content = jsonObject.optString("content");

        localItem.custom = jsonObject.optString("custom");

        localItem.time = jsonObject.optString("time");

        localItem.type = jsonObject.optString("type");

        localItem.url = jsonObject.optString("url");

        localItem.gotoActivity = jsonObject.optString("gotoActivity");

        localItem.msg_id = jsonObject.optString("msg_id");

        localItem.open_type = jsonObject.optString("open_type");

        localItem.category_id = jsonObject.optString("category_id");

        localItem.webUrl = jsonObject.optString("webUrl");

        localItem.goods_id_comment = jsonObject.optString("goods_id_comment");

        localItem.goods_id = jsonObject.optString("goods_id");

        localItem.order_id = jsonObject.optString("order_id");

        localItem.readStatus = jsonObject.optInt("readStatus");

        localItem.keyword = jsonObject.optString("keyword");

        return localItem;
    }


}
