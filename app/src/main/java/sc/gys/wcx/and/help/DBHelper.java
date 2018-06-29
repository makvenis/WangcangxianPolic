package sc.gys.wcx.and.help;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {
    //数据库名称
    private static final String DATABASE_NAME = "database.db";
    //数据库版本
    private static final int DATABASE_VERSION = 1000;

    private static DBHelper instance = null;



    public DBHelper(Context context) {
        //第三个参数CursorFactory指定在执行查询时获得一个游标实例的工厂类,设置为null,代表使用系统默认的工厂类
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public synchronized static DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //执行创建数据库表
        db.execSQL(SQL.CREAT_SIMP);
        // 若不是第一个版本安装，直接执行数据库升级
        // 请不要修改FIRST_DATABASE_VERSION的值，其为第一个数据库版本大小
        final int FIRST_DATABASE_VERSION = 1000;
        onUpgrade(db, FIRST_DATABASE_VERSION, DATABASE_VERSION);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 使用for实现跨版本升级数据库
        for (int i = oldVersion; i < newVersion; i++) {
            switch (i) {

                default:
                    break;
            }
        }
    }


}
