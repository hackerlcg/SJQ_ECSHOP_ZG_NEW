package com.ecjia.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBhelp extends SQLiteOpenHelper {
    private static final String DBNAME = "LastBrowse.db";
    public static final int version = 3;

    public DBhelp(Context context) {
        super(context, DBNAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE goodbrowse(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,goodid INTEGER NOT NULL,goodname VARCHAR(20), goodrating INTEGER, goodprice VARCHAR(10),goodimage BLOB )";
        db.execSQL(sql);
        String msgsql="CREATE TABLE msginfo(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,msgtitle VARCHAR(20) NOT NULL,msgcontent VARCHAR(80) NOT NULL, msgcustom VARCHAR(80) NOT NULL,msgtime VARCHAR(20) NOT NULL,msgtype VARCHAR(20) NOT NULL,msgurl VARCHAR(80),msgActivity VARCHAR(20),msg_id VARCHAR(20) NOT NULL,open_type VARCHAR(20),category_id VARCHAR(20),webUrl VARCHAR(50),goods_id_comment VARCHAR(20),goods_id VARCHAR(20),order_id VARCHAR(20),readStatus SMALLINT,keyword VARCHAR(20))";
        db.execSQL(msgsql);
        String feedbackList="CREATE TABLE feedback_list(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,feedback_id VARCHAR(10) NOT NULL,user_id VARCHAR(10) NOT NULL,user_name VARCHAR(20) NOT NULL,user_type VARCHAR(10) NOT NULL,avatar_img VARCHAR(100),content VARCHAR(100) NOT NULL,formatted_time VARCHAR(25) NOT NULL,message INTEGER NOT NULL,message_type VARCHAR(10) NOT NULL,time INTEGER NOT NULL)";
        db.execSQL(feedbackList);
        String feedbackMessage = "CREATE TABLE feedback_message(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,feedback_id VARCHAR(10) NOT NULL,user_type VARCHAR(10) NOT NULL,content VARCHAR(100) NOT NULL,formatted_time VARCHAR(25) NOT NULL,is_myself INTEGER NOT NULL,time VARCHAR(15) NOT NULL,user_id VARCHAR(50) NOT NULL)";
        db.execSQL(feedbackMessage);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        LG.e("升级"+oldVersion+newVersion);
        if(newVersion==3) {
            LG.e("升级更新");
            String feedbackList = "CREATE TABLE  if not exists feedback_list(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,feedback_id VARCHAR(10) NOT NULL,user_id VARCHAR(10) NOT NULL,user_name VARCHAR(20) NOT NULL,user_type VARCHAR(10) NOT NULL,avatar_img VARCHAR(100),content VARCHAR(100) NOT NULL,formatted_time VARCHAR(25) NOT NULL,message INTEGER NOT NULL,message_type VARCHAR(10) NOT NULL,time INTEGER NOT NULL)";
            db.execSQL(feedbackList);
            String feedbackMessage = "CREATE TABLE  if not exists feedback_message(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,feedback_id VARCHAR(10) NOT NULL,user_type VARCHAR(10) NOT NULL,content VARCHAR(100) NOT NULL,formatted_time VARCHAR(25) NOT NULL,is_myself INTEGER NOT NULL,time VARCHAR(15) NOT NULL,user_id VARCHAR(50) NOT NULL)";
            db.execSQL(feedbackMessage);
            db.execSQL("ALTER TABLE feedback_message ADD user_id VARCHAR(50) NOT NULL");//增减项 保存用户数据
        }
    }

}
