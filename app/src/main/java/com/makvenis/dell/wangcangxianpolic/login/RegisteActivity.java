package com.makvenis.dell.wangcangxianpolic.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.makvenis.dell.wangcangxianpolic.R;
import com.makvenis.dell.wangcangxianpolic.help.JSON;
import com.makvenis.dell.wangcangxianpolic.newdbhelp.AppMothedHelper;
import com.makvenis.dell.wangcangxianpolic.startActivity.HomeActivity;
import com.makvenis.dell.wangcangxianpolic.tools.Configfile;
import com.makvenis.dell.wangcangxianpolic.tools.NetworkTools;
import com.makvenis.dell.wangcangxianpolic.view.SimpleLoadingDialog;

import java.util.Map;

import cn.com.mozile.tools.Base64Util;


/* 作者  王从文 */
/* 全局采用注解模式 */
/* 旺苍县公安局巡防系统 用户登陆界面 */

/***
 * @ 解释: 用户登陆界面:
 * @ 动画效果: 点击输入框 隐藏上面的用户名并且不占用位置
 *
 */

@ContentView(R.layout.activity_registe)
public class RegisteActivity extends AppCompatActivity {

    @ViewInject(R.id.register_user_edit_name)
    private EditText mEditText_name;
    @ViewInject(R.id.register_user_edit_pass)
    private EditText mEditText_pass;
    @ViewInject(R.id.register_user_dl)
    private Button mButton;
    @ViewInject(R.id.register_user_wjmm)
    private TextView register_user_wjmm;
    /* Dialog 启动*/
    private SimpleLoadingDialog mDialog;

    /* 服务器返回的api接口数据 */
    private String obj;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            /* 获取下载的信息 */
            if(msg.what == 0X000003){
                obj = ((String) msg.obj);
                Log.e("TAG","子线程回传数据:"+((String) msg.obj));
                /* 判断是否有数据返回 */
                if(obj != ""){
                    /* 数据库操纵 */
                    AppMothedHelper helper=new AppMothedHelper(RegisteActivity.this);
                    boolean byKey = helper.isDismisData(RegisteActivity.this,Configfile.USER_DATA_KEY);
                    if(byKey == true){
                        helper.update(Configfile.USER_DATA_KEY,obj);
                    }else {
                        helper.dbInsert(new String[]{Configfile.USER_DATA_KEY,obj});
                    }

                    Map<String, String> map = JSON.GetJsonRegiste(obj);
                    String mUserName = map.get("username");
                    String mName="";
                    if(mUserName == null){
                        mName = "ERROR_APP";
                    }else {
                        mName = mUserName;
                    }
                    Log.e("TAG","解析回调结果--用户名--解码结果:"+mUserName);
                    Log.e("TAG","获取用户输入结果--用户名:"+mEditText_name.getText().toString());
                    if(mName.equals( mEditText_name.getText().toString())){
                        Configfile.Log(RegisteActivity.this,"登陆成功");
                        Log.e("TAG","解析回调结果--用户名:"+mUserName);
                        mDialog.dismiss();
                        /* 跳转主页面 */
                        startActivity(new Intent(RegisteActivity.this, HomeActivity.class));
                        /* 登陆成功 删除此页面 */
                        finish();

                    }else {
                        Configfile.Log(RegisteActivity.this,"账号密码错误");
                        mDialog.dismiss();
                    }
                }else {
                    mDialog.dismiss();
                    Configfile.Log(RegisteActivity.this,"连接服务器失败！");
                    Log.e("TAG","连接服务器失败:"+obj);
                }

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewUtils.inject(this);
        //启动Dialog
        mDialog = new SimpleLoadingDialog(this);

        //忘记密码？
        boolenPassWorld();
    }

    /* 忘记密码 */
    private void boolenPassWorld() {
        register_user_wjmm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisteActivity.this,UserLoginActivity.class));
            }
        });
    }

    /* 用户登陆 */
    @OnClick(R.id.register_user_dl)
    public void SetUserQuery(View view){
        getUserInput();
    }

    /* 获取用户的输入 */
    public void getUserInput(){
        String mName = mEditText_name.getText().toString();
        String mPass = mEditText_pass.getText().toString();
        //转码
        String mMD_user_pass = Base64Util.encodeData(mPass);
        final String[] key=new String[]{"username","pwd"};
        final String[] value=new String[]{mName,mMD_user_pass};

        /* 子线程请求 */
        new Thread(new Runnable() {
            @Override
            public void run() {
                //请求数据并且执行存储
                NetworkTools.OkHttpUtils(key,value, Configfile.REGISTE_URL,handler);
            }
        }).start();

        mDialog.setMessage("正在登陆中...").show();

    }

    /* 用户数据转存 */
    public void setUserDataBase(){
        /* 判断当前是否有数据obj */
        Log.e("TAG","用户信息在存储之前是否获取到值-obj"+obj);
        /* 执行数据操作 */
        AppMothedHelper helper=new AppMothedHelper(this);
        Map<Object, Object> map = helper.queryByKey(Configfile.USER_DATA_KEY);

        boolean data = helper.isDismisData(this, Configfile.USER_DATA_KEY);
        Log.e("TAG","再执行更新或者存储之前的查询结果 >>>> "+ ((String) map.get("data"))+" >>>> 是否存在数据 "+data);
        if(data = true){ //执行更新
            helper.update(Configfile.USER_DATA_KEY,obj);
            Log.e("TAG","更新用户基本信息数据-upload-obj"+obj);
        }else {
            helper.dbInsert(new String[]{Configfile.USER_DATA_KEY,obj});
            Log.e("TAG","添加用户基本信息数据-add-obj === "+obj);
        }
    }
}
