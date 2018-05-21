package com.makvenis.dell.wangcangxianpolic.newdbhelp;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库帮助类，用于管理数据库
 * @author Administrator
 *
 */


public class AppSqliteOpenHelper extends SQLiteOpenHelper {

    public AppSqliteOpenHelper(Context context) {
        //数据库名，数据库版本号
        super(context, "book.db", null, 1);
    }

    public AppSqliteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //操作数据库
        String sql="create table java(id integer primary key," +
                "key text," + //存储的键
                "data text);"; //存储的值

        //操作数据库
        String sql_addrs="create table addrs(id integer primary key," +
                "className text," + //栏目(意思也就是当前检查了那个步骤)
                "zhuangtai text," + //状态（完成，未完成）
                "danwei text," +    //单位（根据单位ID来从服务器获取，这里也只存储了单位I编号）
                "user text," +      //用户名称
                "time text);";      //操作时间
        db.execSQL(sql);
        db.execSQL(sql_addrs);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("drop if table exists java");
        db.execSQL("drop if table exists  addrs");
        onCreate(db);
    }
}
