package com.makvenis.dell.wangcangxianpolic.startActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.makvenis.dell.wangcangxianpolic.R;
import com.makvenis.dell.wangcangxianpolic.StartActivity;
import com.makvenis.dell.wangcangxianpolic.utils.AppJudiment;


/* 作者  王从文 */
/* 全局采用注解模式 */
/* 旺苍县公安局巡防系统 首页界面 */

/**
 * @ 解释: 判断APP是否第一次安装
 * @ yes: 跳转值引导页面
 * @ no: 直接跳转StartActivity界面
 * */


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        //判断是否第一次启动
        boolean startApp = AppJudiment.isFirstStartApp(this);
        Log.e("TAG","是否是第一次启动===="+startApp);
        if(startApp){
            Intent intent = new Intent(this, GuideActivity.class);
            startActivity(intent);
            finish();
        }else {
            Intent intent = new Intent(this, StartActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
