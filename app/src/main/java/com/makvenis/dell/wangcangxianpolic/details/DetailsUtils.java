package com.makvenis.dell.wangcangxianpolic.details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.makvenis.dell.wangcangxianpolic.correctActivity.CorrectCommandActivity;

import java.util.ArrayList;

/**
 * 继续检查的工具类
 */

public class DetailsUtils {
/*
    case R.id.toolbar_r_2: //限期整改
    *//* 构建显示的数据 *//*
    ArrayList<String> mGoneData1 = new ArrayList<>();
                        mGoneData1.add("mCardView_19");
                        mGoneData1.add("mCardView_9");
                        mGoneData1.add("mCardView_20");
                        mGoneData1.add("mCardView_23");
                        mGoneData1.add("mCardView_17");
                        mGoneData1.add("mCardView_16");
    jumpActivity("旺苍县公安局责令限期整改治安隐患通知书",mGoneData1);
                        break;
                    case R.id.toolbar_r_3: //责令改正

    *//* 构建显示的数据 *//*
    ArrayList<String> mGoneData = new ArrayList<>();
                        mGoneData.add("mCardView_9");
                        mGoneData.add("mCardView_11");
                        mGoneData.add("mCardView_13");
                        mGoneData.add("mCardView_15");
                        mGoneData.add("mCardView_16");
                        mGoneData.add("mCardView_18");
    jumpActivity("旺苍县公安局责令改正通知书",mGoneData);

                        break;
                    case R.id.toolbar_r_4: //当场处罚
    *//* 构建显示的数据 *//*
    ArrayList<String> mGoneData2 = new ArrayList<>();
                        mGoneData2.add("mCardView_9");
                        mGoneData2.add("mCardView_10");
                        mGoneData2.add("mCardView_11");
                        mGoneData2.add("mCardView_12");
                        mGoneData2.add("mCardView_21");
                        mGoneData2.add("mCardView_14");
                        mGoneData2.add("mCardView_15");
                        mGoneData2.add("mCardView_22");
                        mGoneData2.add("mCardView_16");
                        mGoneData2.add("mCardView_17");
    jumpActivity("旺苍县公安局当场处罚决定书",mGoneData2);
                        break;*/


    public static void getMethondDetails(int id,String mCid,Context mContext){

        switch (id) {

            case 1: //限期整改
                /* 构建显示的数据 */
                ArrayList<String> mGoneData1 = new ArrayList<>();
                mGoneData1.add("mCardView_19");
                mGoneData1.add("mCardView_9");
                mGoneData1.add("mCardView_20");
                mGoneData1.add("mCardView_23");
                mGoneData1.add("mCardView_17");
                mGoneData1.add("mCardView_16");
                jumpActivity("旺苍县公安局责令限期整改治安隐患通知书", mGoneData1,mCid,mContext);
                break;
            case 2: //责令改正

                /* 构建显示的数据 */
                ArrayList<String> mGoneData = new ArrayList<>();
                mGoneData.add("mCardView_9");
                mGoneData.add("mCardView_11");
                mGoneData.add("mCardView_13");
                mGoneData.add("mCardView_15");
                mGoneData.add("mCardView_16");
                mGoneData.add("mCardView_18");
                jumpActivity("旺苍县公安局责令改正通知书", mGoneData,mCid,mContext);

                break;
            case 3: //当场处罚
                /* 构建显示的数据 */
                ArrayList<String> mGoneData2 = new ArrayList<>();
                mGoneData2.add("mCardView_9");
                mGoneData2.add("mCardView_10");
                mGoneData2.add("mCardView_11");
                mGoneData2.add("mCardView_12");
                mGoneData2.add("mCardView_21");
                mGoneData2.add("mCardView_14");
                mGoneData2.add("mCardView_15");
                mGoneData2.add("mCardView_22");
                mGoneData2.add("mCardView_16");
                mGoneData2.add("mCardView_17");
                jumpActivity("旺苍县公安局当场处罚决定书", mGoneData2,mCid,mContext);
                break;
        }
    }

    /**
     * @param mTitle AppToolbar 的标题
     * @param mGoneData AppView 当前文书需要显示的布局控件
     * {@link=into} 参考枚举
     */
    public static void jumpActivity(String mTitle, ArrayList<String> mGoneData, String mCid, Context mContext){
        //参数构建
        Bundle bundle=new Bundle();
        bundle.putString("mTitle",mTitle);
        bundle.putStringArrayList("mGoneData",mGoneData);
        bundle.putString("id",mCid); //单位id
        //跳转
        Intent intent=new Intent(mContext, CorrectCommandActivity.class);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

}
