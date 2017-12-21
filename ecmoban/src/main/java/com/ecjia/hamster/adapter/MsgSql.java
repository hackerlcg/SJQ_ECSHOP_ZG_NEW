package com.ecjia.hamster.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ecjia.hamster.model.MYMESSAGE;
import com.ecjia.util.DBhelp;
import com.ecjia.util.LG;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2014/12/15.
 */
public class MsgSql {
    public static SQLiteDatabase db = null;
    DBhelp helper = null;

    public MsgSql(Context ct) {
        helper = new DBhelp(ct);
    }

    //添加数据
    public void insertMessage(MYMESSAGE msg) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String time = df.format(new Date()).toString();
        db = helper.getReadableDatabase();
        ContentValues cv = new ContentValues();

        //一下插入代码一旦写完，顺序不得修改，只能在后面插入
        cv.put("msgtitle", msg.getTitle());         //1
        cv.put("msgcontent", msg.getContent());     //2
        cv.put("msgcustom", msg.getCustom());       //3
        cv.put("msgtime", time);                    //4
        cv.put("msgtype", msg.getType());           //5
        cv.put("msgurl", msg.getUrl());             //6
        cv.put("msgActivity", msg.getGotoActivity());    //7
        cv.put("msg_id", msg.getMsg_id());          //8

        //2.7.0
        cv.put("open_type", msg.getOpen_type());    //9
        cv.put("category_id", msg.getCategory_id());     //10
        cv.put("webUrl", msg.getWebUrl());          //11
        cv.put("goods_id_comment", msg.getGoods_id_comment());    //12
        cv.put("goods_id", msg.getGoods_id());      //13
        cv.put("order_id", msg.getOrder_id());      //14
        cv.put("readStatus",msg.getReadStatus());   //15
        cv.put("keyword",msg.getKeyword());   //16
        db.insert("msginfo", "id", cv);
        db.close();
    }

    //查询数据
    public Cursor getAllmsg() {
        db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("select * from msginfo order by id desc ",null);
        //db.close();
        return c;
    }

    public void updateData(String id) {
        db = helper.getReadableDatabase();
        ContentValues cv = new ContentValues();//实例化ContentValues
        cv.put("readStatus",1);
        String whereClause = "msg_id=?";//修改条件
        String[] whereArgs = {id};//修改条件的参数
        db.update("msginfo",cv,whereClause,whereArgs);//执行修改
    }

    public static MsgSql msg;

    public static MsgSql getIntence(Context c) {
        if (null == msg) {
            msg = new MsgSql(c);
        }
        return msg;
    }

}
