package com.makvenis.dell.wangcangxianpolic.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.makvenis.dell.wangcangxianpolic.R;
import com.makvenis.dell.wangcangxianpolic.startActivity.BaseActivity;

/* 作者  王从文 */
/* 全局采用注解模式 */
/* 旺苍县公安局巡防系统 登陆注册页面 */


@ContentView(R.layout.activity_user_login)
public class UserLoginActivity extends BaseActivity {

    @ViewInject(R.id.login_zc)
    private Button mButton_zc;

    @ViewInject(R.id.login_dl)
    private Button mButton_dl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_user_login);
        ViewUtils.inject(this);

    }

    //用户登陆
    @OnClick(R.id.login_dl)
    public void pointUserRegiste(View view){
        // TODO: 2018/4/3 判断当前用户是否登陆 如果登陆需要让他选择是否注销当前用户
        startActivity(new Intent(this,RegisteActivity.class));
    }
}
