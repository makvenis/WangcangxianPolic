package com.makvenis.dell.wangcangxianpolic.startActivity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.makvenis.dell.wangcangxianpolic.R;

/* 作者  王从文 */
/* 全局采用注解模式 */
/* 旺苍县公安局巡防系统 首页界面 */

/**
 * @// STOPSHIP: 2018/4/1
 * @ Content: 首页加载
 * @ 解释: 通过RadioButton加载不同项，默认加载第一个项目
 * @ 物理返回键: 重写物理返回键
 * @
 */


public class HomeActivity extends BaseActivity {
    private FrameLayout fl;
    private RadioGroup mRg;
    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home_activity);

        /* 控件查找 */
        fl = ((FrameLayout) findViewById(R.id.fl));
        mRg = ((RadioGroup) findViewById(R.id.rg));

        /* 默认加载第一个碎片 */
        manager=getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fl,new NoticeFragment());
        transaction.commit();
        /* 第一个碎片加载完毕 */

        //处理点击事件
        SetOnlinkRadioButton();

        /* NotiflyCation 的事件传递 */

    }

    /* 处理Button事件机制 */
    private void SetOnlinkRadioButton() {
        //RadioButton点击
        mRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int i) {
                if(i==R.id.Main_button_a){
                    manager=getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.fl,new NoticeFragment());
                    transaction.commit();
                }/*else if(i==R.id.Main_button_b){
                    manager=getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.fl,new AssessmentFragment());
                    transaction.commit();
                }*/else if(i==R.id.Main_button_c){
                    manager=getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.fl,new LinkFragment());
                    transaction.commit();
                }
            }
        });


    }

    /* 重写物理返回键 */
    private long mOlTime;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() ==  KeyEvent.ACTION_DOWN){
            if(System.currentTimeMillis()-2000 > mOlTime){
                mOlTime=System.currentTimeMillis();
                Toast.makeText(this,"再次点击退出",Toast.LENGTH_LONG).show();
            }else {
                finish();
                System.exit(0);
                // TODO: 2018/4/2 退出APP的事件
            }
        }
        return true;
    }
}
