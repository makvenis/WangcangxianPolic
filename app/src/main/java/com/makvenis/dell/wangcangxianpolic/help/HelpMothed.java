package com.makvenis.dell.wangcangxianpolic.help;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.makvenis.dell.wangcangxianpolic.tools.Configfile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public class HelpMothed {


    //通过key值查询数据库
    public static String QueryByKey(Context context, String key) {
        List<Map<String, String>> maps = QueryDatabase(context);
        List<String> da=new ArrayList<>();
        for (int i=0;i<maps.size();i++){

            Map<String, String> map = maps.get(i);
            Set<String> set = map.keySet();
            Collection<String> collection = map.values();//返回值是个值的Collection集合
            for (String list:collection) {
                da.add(list);
            }

        }
        if(da.size()>0){
            return da.get(0);
        }else {

            return null;
        }


    }

    //查询数据库
    public static List<Map<String, String>> QueryDatabase(Context context){
        DBHelper database = new DBHelper(context);
        SQLiteDatabase db = database.getWritableDatabase();
        List<Map<String,String>> data=new ArrayList<>();

        Cursor c = db.query("yyjs",null,null,null,null,null,null);//查询并获得游标
        if(c !=null){
            while (c.moveToNext()){
                Map<String,String> map=new HashMap<>();
                String id = c.getString(c.getColumnIndex("key"));
                String name = c.getString(c.getColumnIndex("data"));
                map.put(id,name);
                data.add(map);
            }

            return data;
        }else {
            return new ArrayList<>();
        }



    }

    //判断数据库有无该数据
    public static boolean isQueryByKey(Context context,String key){
        DBHelper database = new DBHelper(context);
        SQLiteDatabase db = database.getWritableDatabase();
        //判断数据库有没有这条信息
        HelpMothed mDatabaseHelpAllmothed=new HelpMothed();
        List<String> mKey=new ArrayList<>();
        List<Map<String, String>> maps = HelpMothed.QueryDatabase(context);
        for (int k=0;k<maps.size();k++){
            Map<String, String> map = maps.get(k);
            Set<String> set = map.keySet(); //取出所有的key值
            for (String link:set) {
                mKey.add(link);
                Log.e("db==",link);
            }
        }

        if(mKey.contains(key)){
            return true;
        }else {
            return false;
        }
    }

    //添加数据库
    public static void AddDataToDatabse(Context context,String key,String data){
        DBHelper database = new DBHelper(context);
        SQLiteDatabase db = database.getWritableDatabase();
        //实例化一个ContentValues用来装载待插入的数据
        final ContentValues cv = new ContentValues();
        cv.put("key",key); //添
        cv.put("data",data); //添
        db.insert("yyjs",null,cv);//执行插入操作*/
    }

    //更新数据库
    public static void UpdateDatabase(Context context,String key,String data){
        DBHelper database = new DBHelper(context);
        SQLiteDatabase db = database.getWritableDatabase();
        /**
         * @ 鉴于更新数据库复杂
         * @ 采用先删除 再添加数据
         *
         */
        //删除数据
        String whereClause = "key=?";//删除的条件
        String[] whereArgs = {key};//删除的条件参数
        db.delete("yyjs",whereClause,whereArgs);//执行删除
        //添加数据
        //实例化一个ContentValues用来装载待插入的数据
        final ContentValues cv = new ContentValues();
        cv.put("key",key); //添
        cv.put("data",data); //添
        db.insert("yyjs",null,cv);//执行插入操作*/
    }

    //删除数据库
    public static boolean DeleteDatabaseBykey(Context context,String key){
        DBHelper database = new DBHelper(context);
        SQLiteDatabase db = database.getWritableDatabase();
        /**
         * @ 鉴于更新数据库复杂
         * @ 采用先删除 再添加数据
         *
         */
        //删除数据
        String whereClause = "key=?";//删除的条件
        String[] whereArgs = {key};//删除的条件参数
        HelpMothed mDB=new HelpMothed();
        boolean is = mDB.isQueryByKey(context, key);
        if(is !=false){
            db.delete("yyjs",whereClause,whereArgs);//执行删除
            return Configfile.ACTION_SUCCESS;
        }else {
            return Configfile.ACTION_DEFEAT;
        }

    }

    /**、
     * @？ 解决的是当这个数据被请求道的时候，如果不存在就去执行存储，存在就去执行更新。
     * @param context
     * @param key
     * @param data
     */
    public static void Execution(Context context,String key,String data){
        boolean queryByKey = HelpMothed.isQueryByKey(context, key);
        if(queryByKey){
            HelpMothed.AddDataToDatabse(context,key,data);
        }else {
            HelpMothed.UpdateDatabase(context,key,data);
        }
    }
}
