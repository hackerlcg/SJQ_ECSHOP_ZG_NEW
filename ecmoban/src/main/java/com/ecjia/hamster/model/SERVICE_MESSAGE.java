package com.ecjia.hamster.model;

import com.ecjia.util.LG;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/1/4.
 */
public class SERVICE_MESSAGE {
    private String feedback_id;
    private String user_id;
    private String user_name;
    private String avatar_img;
    private String content;
    private String time;
    private String formatted_time;
    private int messages;
    private String user_type;
    private String message_type;

    public String getFeedback_id() {
        return feedback_id;
    }

    public void setFeedback_id(String feedback_id) {
        this.feedback_id = feedback_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getAvatar_img() {
        return avatar_img;
    }

    public void setAvatar_img(String avatar_img) {
        this.avatar_img = avatar_img;
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

    public String getFormatted_time() {
        return formatted_time;
    }

    public void setFormatted_time(String formatted_time) {
        this.formatted_time = formatted_time;
    }

    public int getMessages() {
        return messages;
    }

    public void setMessages(int messages) {
        this.messages = messages;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getMessage_type() {
        return message_type;
    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }

    public static SERVICE_MESSAGE fromJson(JSONObject jsonObject)throws JSONException {
        if(null == jsonObject){
            return null;
        }
        SERVICE_MESSAGE localItem=new SERVICE_MESSAGE();
        localItem.feedback_id=jsonObject.optString("feedback_id");
        localItem.user_id=jsonObject.optString("user_id");
        localItem.user_name=jsonObject.optString("user_name");
        localItem.avatar_img=jsonObject.optString("avatar_img");
        localItem.content=jsonObject.optString("content");
        if(localItem.content.contains("'")){
            localItem.content=localItem.content.replace("'","''");
        }
        localItem.time=jsonObject.optString("time");
        localItem.formatted_time=jsonObject.optString("formatted_time");
        localItem.messages=jsonObject.optInt("messages", 0);
        localItem.user_type=jsonObject.optString("user_type");
        localItem.message_type=jsonObject.optString("user_type");
        return localItem;
    }

}
