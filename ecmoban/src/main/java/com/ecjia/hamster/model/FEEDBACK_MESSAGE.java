package com.ecjia.hamster.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/1/5.
 */
public class FEEDBACK_MESSAGE {
    private String feedback_id;
    private String user_id;
    private String content;
    private String time;
    private String formatted_time;
    private int is_myself;

    public String getFeedback_id() {
        return feedback_id;
    }

    public void setFeedback_id(String feedback_id) {
        this.feedback_id = feedback_id;
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

    public int getIs_myself() {
        return is_myself;
    }

    public void setIs_myself(int is_myself) {
        this.is_myself = is_myself;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public static FEEDBACK_MESSAGE fromJson(JSONObject jsonObject)  throws JSONException {
        if(null == jsonObject){
            return null;
        }
        FEEDBACK_MESSAGE feedback_message=new FEEDBACK_MESSAGE();
        feedback_message.feedback_id=jsonObject.optString("feedback_id");
        feedback_message.content=jsonObject.optString("content");
        if(feedback_message.content.contains("'")){
            feedback_message.content=feedback_message.content.replace("'","''");
        }
        feedback_message.time=jsonObject.optString("time");
        feedback_message.formatted_time=jsonObject.optString("formatted_time");
        feedback_message.is_myself=jsonObject.optInt("is_myself");
        return feedback_message;
    }

}
