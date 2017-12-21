package com.ecjia.hamster.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ecjia.hamster.model.FEEDBACK_MESSAGE;
import com.ecjia.hamster.model.SERVICE_MESSAGE;
import com.ecjia.util.DBhelp;
import com.ecjia.util.LG;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/1/11.
 */
public class FeedBackMessageSql {
    public SQLiteDatabase db = null;
    private DBhelp helper = null;
    private static FeedBackMessageSql feedBackMessageSql;

    public static FeedBackMessageSql getIntence(Context c) {
        if (null == feedBackMessageSql) {
            feedBackMessageSql = new FeedBackMessageSql(c);
        }
        return feedBackMessageSql;
    }

    public FeedBackMessageSql(Context context){
        helper = new DBhelp(context);
    }

    public void InsertValue(FEEDBACK_MESSAGE feedback_message,String user_type) {
        if(!isSavedMessage(feedback_message.getFeedback_id())){
            db = helper.getReadableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("feedback_id", feedback_message.getFeedback_id());
            cv.put("user_type",user_type);
            cv.put("content", feedback_message.getContent());
            cv.put("formatted_time", feedback_message.getFormatted_time());
            cv.put("is_myself",feedback_message.getIs_myself());
            cv.put("time",feedback_message.getTime());
            cv.put("user_id",feedback_message.getUser_id());
            db.insert("feedback_message", "id", cv);
            db.close();
        }
    }

    //检查数据是否已经保存过
    private boolean isSavedMessage(String feedback_id) {
        db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("select * from feedback_message where feedback_id='" + feedback_id + "'", null);
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

    //分页分类查询数据
    public void getFeedBackMessageListByPage(int page, ArrayList<FEEDBACK_MESSAGE> Local_Messages, String type,String user_id) {
        LG.e("page==="+page);
        db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("select * from feedback_message where user_type='" + type + "' and user_id='"+user_id+"' order by time desc limit " + (page - 1) * 10 + "," + 10, null);
        while (c.moveToNext()) {
            FEEDBACK_MESSAGE feedback_message = new FEEDBACK_MESSAGE();
            feedback_message.setFeedback_id(c.getString(1));
            feedback_message.setContent(c.getString(3));
            feedback_message.setFormatted_time(c.getString(4));
            feedback_message.setIs_myself(c.getInt(5));
            feedback_message.setTime(c.getString(6));
            Local_Messages.add(0,feedback_message);
        }
        c.close();
        db.close();
    }


    //查询数据条数
    public int getAllFeedBackListCount(String type) {
        db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("select * from feedback_message where user_type='" + type + "'", null);
        int count = c.getCount();
        c.close();
        db.close();
        return count;
    }

    //查询数据条数
    public int getFeedBackListCountbyUser(String type,String user_id) {
        db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("select * from feedback_message where user_type='" + type + "' and user_id='"+user_id+"'", null);
        int count = c.getCount();
        c.close();
        db.close();
        return count;
    }

}
