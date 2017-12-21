
package com.ecjia.hamster.model;
import org.json.JSONException;
import org.json.JSONObject;


public class ACTION
{

    private int id;
    private String order_status;  //订单状态
    private String pay_status;  //付款状态
    private String shippint_status;  //配送状态
    private String action_user;  //操作管理员
    private String action_time;  //操作时间
    private String note;     //操作备注信息
    private String log_description;//操作描述

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getPay_status() {
        return pay_status;
    }

    public void setPay_status(String pay_status) {
        this.pay_status = pay_status;
    }

    public String getShippint_status() {
        return shippint_status;
    }

    public void setShippint_status(String shippint_status) {
        this.shippint_status = shippint_status;
    }

    public String getAction_user() {
        return action_user;
    }

    public void setAction_user(String action_user) {
        this.action_user = action_user;
    }

    public String getAction_time() {
        return action_time;
    }

    public void setAction_time(String action_time) {
        this.action_time = action_time;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getLog_description() {
        return log_description;
    }

    public void setLog_description(String log_description) {
        this.log_description = log_description;
    }

    public static ACTION fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return null;
        }

        ACTION localItem = new ACTION();

        localItem.id = jsonObject.optInt("id");
        localItem.order_status = jsonObject.optString("order_status");
        localItem.pay_status = jsonObject.optString("pay_status");
        localItem.shippint_status = jsonObject.optString("shippint_status");
        localItem.action_user=jsonObject.optString("action_user");
        localItem.action_time=jsonObject.optString("action_time");
        localItem.note=jsonObject.optString("note");
        localItem.log_description=jsonObject.optString("log_description");
        return localItem;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        localItemObject.put("id", id);
        localItemObject.put("order_status", order_status);
        localItemObject.put("pay_status",pay_status);
        localItemObject.put("shippint_status",shippint_status);
        localItemObject.put("action_user", action_user);
        localItemObject.put("action_time", action_time);
        localItemObject.put("note", note);
        localItemObject.put("log_description",log_description);
        return localItemObject;
    }

}
