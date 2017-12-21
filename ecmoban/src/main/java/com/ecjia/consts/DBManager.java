package com.ecjia.consts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ecjia.hamster.model.DBUSER;
import com.ecjia.util.LG;

import java.util.ArrayList;

/**
 * Created by lm on 2015/7/10.
 */
public class DBManager extends SQLiteOpenHelper {


    private static final String DB_NAME = "user.db"; //数据库名称
    private static final int version = 1; //数据库版本

    public DBManager(Context context) {
        super(context, DB_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表
        db.execSQL("CREATE TABLE user (id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(20), pwd VARCHAR(20), api VARCHAR, isDefault SMALLINT,isCheck SMALLINT)");
        db.execSQL("CREATE TABLE msginfo(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,msgtitle VARCHAR(20) NOT NULL,msgcontent VARCHAR(80) NOT NULL, msgcustom VARCHAR(80) NOT NULL,msgtime VARCHAR(20) NOT NULL)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }

    //删除表
    public void destroy(SQLiteDatabase db){
         db.execSQL("DELETE FROM user");
    }


    //插入数据
    public void insertData(SQLiteDatabase db,DBUSER dbuser) {
        ContentValues cv = new ContentValues();//实例化一个ContentValues用来装载待插入的数据cv.put("username","Jack Johnson");//添加用户名
        cv.put("name",dbuser.getName());
        cv.put("pwd",dbuser.getPwd());
        cv.put("api",dbuser.getApi());
        cv.put("isDefault",dbuser.getIsDefault());
        cv.put("isCheck",dbuser.getIsCheck());
        db.insert("user",null,cv);//执行插入操作
    }


    //查询所有
    public ArrayList<DBUSER> selectAll(SQLiteDatabase db) {
        ArrayList<DBUSER> list=new ArrayList<DBUSER>();
        DBUSER dbuser=new DBUSER();
        Cursor c = db.rawQuery("select * from user order by isDefault desc",null);
        final int j=c.getCount();
        if (c.moveToFirst()) {
            for (int i = 0; i < j; i++) {
                dbuser = new DBUSER(
                        c.getString(c.getColumnIndex("name")),
                        c.getString(c.getColumnIndex("pwd")),
                        c.getString(c.getColumnIndex("api")),
                        c.getInt(c.getColumnIndex("isDefault")),
                        c.getInt(c.getColumnIndex("isCheck"))
                );
                c.moveToNext();
                list.add(dbuser);
            }
            return list;
        }
        return list;
    }

    //判断是否重复数据，靠api判断
    public boolean isDistinct(SQLiteDatabase db, String api) {
        Cursor c = db.rawQuery("select * from user where api=?",new String[]{api});
        if (c.getCount()>=1) {
            return true;
        }else {
            return false;
        }
    }

    //删除条目
    public void deleteData(SQLiteDatabase db, String api) {
        String whereClause = "api=?";//删除的条件
        String[] whereArgs = {api};//删除的条件参数
        db.delete("user",whereClause,whereArgs);//执行删除
    }

    //更新默认登录，只允许一条是1
    public void updateDefault(SQLiteDatabase db, String api) {
        ContentValues cv = new ContentValues();//实例化ContentValues
        cv.put("isDefault",0);//添加要更改的字段及内容
        db.update("user",cv,null,null);//执行修改
        cv.put("isDefault",1);
        String whereClause = "api=?";//修改条件
        String[] whereArgs = {api};//修改条件的参数
        db.update("user",cv,whereClause,whereArgs);//执行修改
    }

    public int selectIsDefault(SQLiteDatabase db, String s) {
        Cursor c = db.rawQuery("select * from user where api=?",new String[]{s});
        if(c.moveToFirst()) {
            int i = c.getInt(c.getColumnIndex("isDefault"));
        }
        return 0;
    }

    public void updateData(SQLiteDatabase db, String name, String pwd, String api,String orapi) {
        ContentValues cv = new ContentValues();//实例化ContentValues
        cv.put("name",name);
        cv.put("pwd",pwd);
        cv.put("api",api);
        String whereClause = "api=?";//修改条件
        String[] whereArgs = {orapi};//修改条件的参数
        db.update("user",cv,whereClause,whereArgs);//执行修改
    }

    public void updateIsCheck(SQLiteDatabase db, String checkapi) {
        ContentValues cv = new ContentValues();//实例化ContentValues
        cv.put("isCheck",1);
        String whereClause = "api=?";//修改条件
        String[] whereArgs = {checkapi};//修改条件的参数
        db.update("user",cv,whereClause,whereArgs);//执行修改
    }

    public void resetCheck(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();//实例化ContentValues
        cv.put("isCheck",0);//添加要更改的字段及内容
        db.update("user",cv,null,null);//执行修改
    }

    public void deletNoCheck(SQLiteDatabase db) {
        String whereClause = "isCheck=?";//删除的条件
        String[] whereArgs = {"0"};//删除的条件参数
        db.delete("user",whereClause,whereArgs);//执行删除
    }
}
