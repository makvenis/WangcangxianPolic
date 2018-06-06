package com.makvenis.dell.wangcangxianpolic.otherActivity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.makvenis.dell.wangcangxianpolic.R;
import com.makvenis.dell.wangcangxianpolic.newdbhelp.AppMothedHelper;
import com.makvenis.dell.wangcangxianpolic.startActivity.BaseActivity;
import com.makvenis.dell.wangcangxianpolic.startActivity.HomeActivity;
import com.makvenis.dell.wangcangxianpolic.tools.Configfile;

import java.io.File;
import java.util.Date;

@ContentView(R.layout.activity_set)
public class SetActivity extends BaseActivity {

    public Context mContext=SetActivity.this;

    /* include 里面的点击事件 */
    @ViewInject(R.id.toolbar_callbank)
    ImageView mImageView_bank;
    @ViewInject(R.id.mToolbar_text)
    TextView mTextView;

    @ViewInject(R.id.mSet_dingwei)
    SwitchCompat mSet_dingwei;
    @ViewInject(R.id.mSet_renwu)
    SwitchCompat mSet_renwu;
    @ViewInject(R.id.mSet_clean_addrs)
    TextView mDatabase; //清除数据库表Adds
    @ViewInject(R.id.mSet_clean_cache)
    TextView mCache;

    @ViewInject(R.id.mSet_tuisong)
    SwitchCompat mSet_tuisong;

    /* 当前用户名称 */
    public String mName;

    /**
     * 代码示例：
     SharedPreferences pref = MainActivity.this.getSharedPreferences(“data”,MODE_PRIVATE);
     SharedPreferences.Editor editor = pref.edit();
     editor.putString(“name”,”lily”);
     editor.putString(“age”,”20”);
     editor.putBoolean(“married”,false);
     editor.commit();

     如果要读取数据：
     SharedPreferences pref = getSharedPreferences(“data”,MODE_PRIVATE);
     String name = pref.getString(“name”,”“);//第二个参数为默认值
     int age = pref.getInt(“age”,0);//第二个参数为默认值
     boolean married = pref.getBoolean(“married”,false);//第二个参数为默认值
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
        /* 标题赋值 */
        mTextView.setText("设置");

        /* 设置用户的信息 */
        setUser();

        /* 当设置发生改变的时候的存储信息 */
        getUserSet();

        /* 读取文件大小并且赋值 */
        /* 路径 */
        File mFile = this.getExternalFilesDir(null);
        Log.e("TAG","文件大小"+mFile.length()+"");
        mCache.setText(mFile.length()+"kb");
    }

    /* 返回 */
    @OnClick({R.id.toolbar_callbank})
    public void oncklinkView(View v){
        Intent intent=new Intent(this,HomeActivity.class);
        intent.putExtra("bank_id",2);
        // TODO: 2018/6/5
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        finish();
    }

    /* 返回 */
    @OnClick({R.id.toolbar_callbank_text})
    public void oncklinkViewTextView(View v){
        Intent intent=new Intent(this,HomeActivity.class);
        intent.putExtra("bank_id",2);
        // TODO: 2018/6/5
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        finish();
    }

    /* 重写方法 获取用户名称 */

    @Override
    public void getUserName(String s) {
        this.mName=s;
    }

    /* 获取用户操作 */
    public void getUserSet(){
        /* 存储用户设置 */
        SharedPreferences pref = mContext.getSharedPreferences("set",MODE_PRIVATE);
        final SharedPreferences.Editor editor = pref.edit();

        mSet_dingwei.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("dingwei",isChecked);          //是否开启本地定位
                if(isChecked){
                    Configfile.Log(mContext,"开启定位");
                }else {
                    Configfile.Log(mContext,"关闭定位");
                }

                editor.commit();
            }
        });

        mSet_renwu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("localHistory",isChecked);    //是否开启本里历史
                if(isChecked){
                    Configfile.Log(mContext,"开启历史查询");
                }else {
                    Configfile.Log(mContext,"关闭历史查询");
                }
                editor.commit();
            }
        });

        mSet_tuisong.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("tuisong",isChecked);    //是否开启本里历史
                if(isChecked){
                    Configfile.Log(mContext,"开启推送");
                }else {
                    Configfile.Log(mContext,"关闭推送");
                }
                editor.commit();
            }
        });

        mDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sql="delete from addrs where user = "+"'"+mName+"'";
                Log.e("TAG",new Date()+" >>> 预备执行的SQL语句"+sql);
                AppMothedHelper helper=new AppMothedHelper(mContext);
                helper.executeSql(sql);
                Configfile.Log(mContext,"清除检查历史成功！");
            }
        });

        mCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2018/5/19 删除所有文件 这里涉及很多
                mCache.setText("0KB");
            }
        });



    }

    /* 获取用户的设置 */
    public void setUser(){
        SharedPreferences pref = getSharedPreferences("set",MODE_PRIVATE);
        boolean dingwei = pref.getBoolean("dingwei", false);
        boolean history = pref.getBoolean("localHistory", false);
        boolean tuisong = pref.getBoolean("tuisong", false);
        mSet_renwu.setChecked(history);
        mSet_dingwei.setChecked(dingwei);
        mSet_tuisong.setChecked(tuisong);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() ==  KeyEvent.ACTION_DOWN){
            Intent intent=new Intent(this,HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        return true;
    }
}
