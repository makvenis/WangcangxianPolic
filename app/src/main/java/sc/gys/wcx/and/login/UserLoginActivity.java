package sc.gys.wcx.and.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import sc.gys.wcx.and.R;

/* 作者  王从文 */
/* 全局采用注解模式 */
/* 旺苍县公安局巡防系统 登陆注册页面 */


@ContentView(R.layout.activity_user_login)
public class UserLoginActivity extends AppCompatActivity {

    @ViewInject(R.id.login_zc)
    private Button mButton_zc;

    @ViewInject(R.id.login_dl)
    private Button mButton_dl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);

    }

    //用户登陆
    @OnClick(R.id.login_dl)
    public void pointUserRegiste(View view){
        // TODO: 2018/4/3 判断当前用户是否登陆 如果登陆需要让他选择是否注销当前用户
        startActivity(new Intent(this,RegisteActivity.class));
    }
}
