package com.makvenis.dell.wangcangxianpolic.startActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.makvenis.dell.wangcangxianpolic.R;
import com.makvenis.dell.wangcangxianpolic.tools.Configfile;

/**
 * @解释 用户密码找回
 * @全局 采用
 */

@ContentView(R.layout.activity_update_pass)
public class UpdatePassActivity extends BaseActivity {

    @ViewInject(R.id.user_name)
    EditText userName;

    @ViewInject(R.id.user_password)
    EditText userPass;

    @ViewInject(R.id.user_password_two)
    EditText userPassTwo;

    @ViewInject(R.id.user_submit)
    Button mSubmit;

    /* 处理toolbar 开始 version=2  */
    /* include 里面的点击事件 */
    @ViewInject(R.id.toolbar_callbank)
    ImageView mImageView_bank;
    @ViewInject(R.id.toolbar_callbank_text)
    TextView mBankTextView;
    @ViewInject(R.id.mToolbar_text)
    TextView mTextView;
    /* 处理toolbar 结束 */

    /* 全局用户名称 */
    public String mName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_update_pass);
        ViewUtils.inject(this);

        mTextView.setText("修改密码");
        /* 赋值当前用户名称 */
        userName.setText(mName);

    }

    @Override
    public void getUserName(String s) {
        this.mName=s;
    }

    /* 返回 */
    @OnClick({R.id.toolbar_callbank})
    public void oncklinkViewImage(View v){
        startActivity(new Intent(this, HomeActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    /* 返回 */
    @OnClick({R.id.toolbar_callbank_text})
    public void oncklinkViewTextView(View v){
        startActivity(new Intent(this, HomeActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    /* 提交密码修改 */
    @OnClick({R.id.user_submit})
    public void Submit(View v){
        // 获取用户需要修改的密码
        String mInputPass = getUserInput();

        Configfile.Log(this,mInputPass + " >>> " + mName);

        // TODO: 2018/5/25  提交密码修改

    }

    /* 获取当前用户的输出 */
    public String getUserInput() {

        String passFirst = userPass.getText().toString();
        String passTwo = userPassTwo.getText().toString();

        if(passFirst == null && passTwo == null ){ //&&
            Configfile.Log(this,"密码输入不能为空");
            return "";
        }else if(passFirst == passTwo && passFirst != "" && passTwo != ""){
            return passTwo;
        }else if(passFirst != passTwo){
            Configfile.Log(this,"两次密码输入不一至");
            return "";
        }else {
            return "";
        }

    }
}
