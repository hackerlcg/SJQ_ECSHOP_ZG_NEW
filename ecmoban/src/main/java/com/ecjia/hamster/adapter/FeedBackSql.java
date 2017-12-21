package com.ecjia.hamster.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ecjia.hamster.model.SERVICE_MESSAGE;
import com.ecjia.util.DBhelp;
import com.ecjia.util.LG;
import com.ecjia.util.TimeUtil;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Administrator on 2016/1/8.
 */
public class FeedBackSql {
    public SQLiteDatabase db = null;
    private DBhelp helper = null;
    private static FeedBackSql feedBackSql;
    private Context context;
    public static FeedBackSql getIntence(Context c) {
        if (null == feedBackSql) {
            feedBackSql = new FeedBackSql(c);
        }else{
            feedBackSql.context=c;
        }
        return feedBackSql;
    }

    public FeedBackSql(Context context) {
        this.context=context;
        helper = new DBhelp(context);
    }

    public void InsertMessageData(SERVICE_MESSAGE service_message,String type) {
        if (isSavedMessage(service_message.getFeedback_id())) {
            UpdateMessage(service_message,type);
            LG.e("更新");
        } else {
            InsertValue(service_message,type);
            LG.e("插入");
        }
    }


    private void InsertValue(SERVICE_MESSAGE service_message,String type) {
        db = helper.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("feedback_id", service_message.getFeedback_id());
        cv.put("user_id", service_message.getUser_id());
        cv.put("user_name", service_message.getUser_name());
        cv.put("user_type", type);//类型
        cv.put("avatar_img", service_message.getAvatar_img());
        cv.put("content", service_message.getContent());
        cv.put("formatted_time", service_message.getFormatted_time());
        cv.put("message", service_message.getMessages());
        cv.put("message_type",service_message.getUser_type());
        cv.put("time",Integer.valueOf(service_message.getTime()));
        db.insert("feedback_list", "id", cv);
        db.close();
    }

    private void UpdateMessage(SERVICE_MESSAGE service_message,String type) {
        db = helper.getReadableDatabase();
        db.execSQL("update feedback_list set user_id='" + service_message.getUser_id() + "', user_name='" + service_message.getUser_name() + "', user_type='" + type + "',  avatar_img='" + service_message.getAvatar_img() + "',  content='" + service_message.getContent() + "', formatted_time='" + service_message.getFormatted_time() + "', message=" + service_message.getMessages() +", message_type='" + service_message.getMessage_type() + "' , time=" + Integer.valueOf(service_message.getTime()) + " where feedback_id='" + service_message.getFeedback_id() + "'");
        db.close();
    }

    public void UpdateMessageContent(String content,String formatted_time,String feedback_id) {//刷新内容
        db = helper.getReadableDatabase();
        int time=0;
        try {
            time=(int)(TimeUtil.format.parse(formatted_time).getTime()/1000);
            LG.e("now=="+ time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        db.execSQL("update feedback_list set content='" + content + "', formatted_time='" + formatted_time + "', time=" + time+" where feedback_id='" + feedback_id + "'");
        db.close();
    }


    //分页分类查询数据
    public void getFeedBackListByPage(int page, ArrayList<SERVICE_MESSAGE> Local_Messages, String type) {
        db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("select * from feedback_list where user_type='" + type + "' order by time desc limit " + (page - 1) * 10 + "," + 10, null);
        while (c.moveToNext()) {
            SERVICE_MESSAGE service_message = new SERVICE_MESSAGE();
            service_message.setFeedback_id(c.getString(1));
            service_message.setUser_id(c.getString(2));
            service_message.setUser_name(c.getString(3));
            service_message.setUser_type(c.getString(4));
            service_message.setAvatar_img(c.getString(5));
            service_message.setContent(c.getString(6));
            service_message.setFormatted_time(c.getString(7));
            service_message.setMessages(c.getInt(8));
            service_message.setMessage_type(c.getString(9));
            Local_Messages.add(service_message);
//            LG.e("service_message.getFormatted_time()=="+service_message.getFormatted_time());
//            LG.e("service_message.getTime()=="+service_message.getTime());
        }
        LG.e("查询数据------"+Local_Messages.size());
        c.close();
        db.close();
    }

    //查询数据
    public int getAllFeedBackListCount(String type) {
        db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("select * from feedback_list where user_type='" + type + "' order by id asc ", null);
        int count = c.getCount();
        LG.e("总条数=="+count);
        c.close();
        db.close();
        return count;
    }

    //检查数据是否已经保存过
    private boolean isSavedMessage(String feedback_id) {
        db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("select * from feedback_list where feedback_id='" + feedback_id + "'", null);
        if (c.moveToNext()) {//数据库中已有存储
            c.close();
            db.close();
            return true;
        } else {
            c.close();
            db.close();
            return false;
        }
    }

    //更新数据
    public void setZeroMessageNum( String feedback_id) {
        db = helper.getReadableDatabase();
        db.execSQL("update feedback_list set message=0 where feedback_id='" + feedback_id + "'");
        db.close();
    }

    public void DeleteById(String feedback_id){
        db = helper.getReadableDatabase();
        db.execSQL("delete from feedback_list where feedback_id='" + feedback_id + "'");
        db.close();
    }

}
