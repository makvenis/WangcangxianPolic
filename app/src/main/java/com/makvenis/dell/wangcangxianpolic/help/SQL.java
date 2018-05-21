package com.makvenis.dell.wangcangxianpolic.help;


public class SQL {
    public static final String T_FAVORITE = "favorite";


    public static final String CREATE_TABLE_FAVORITE =
            "CREATE TABLE IF NOT EXISTS " + T_FAVORITE + "(" +
                    "id VARCHAR PRIMARY KEY, " +
                    "title VARCHAR, " +
                    "url VARCHAR, " +
                    "createDate VARCHAR " +
                    ")";
    public static  final String CREAT_SIMP="CREATE TABLE IF NOT EXISTS yyjs (personid integer primary key autoincrement, key varchar(200000), data varchar(200000))";
}
