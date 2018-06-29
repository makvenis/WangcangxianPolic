package sc.gys.wcx.and.startActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import sc.gys.wcx.and.R;
import sc.gys.wcx.and.login.RegisteActivity;
import sc.gys.wcx.and.tools.Configfile;
import sc.gys.wcx.and.view.SimpleTimeView;
import com.squareup.picasso.Picasso;

/* 作者  王从文 */
/* 全局采用注解模式 */
/* 旺苍县公安局巡防系统 首页界面 */


/**
 * @// STOPSHIP: 2018/3/31  
 * @ Content: 广告界面
 * @ 解释: 广告界面的数据的加载
 * @ 作用: 启动后台服务(如果需要)
 */

@ContentView(R.layout.adv_activity)
public class AdvertisingActivity extends AppCompatActivity {
    
    /* 用户主动意识标签 */
    private String overTag=null;

    @ViewInject(R.id.adv_img)
    private ImageView mImageView;

    @ViewInject(R.id.adv_downTime)
    private SimpleTimeView mSimpleTimeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);

        // TODO: 2018/3/31 这里可以解析启动界面的图片二进制文件，也可以直接下载
        DownImagerViewRes();

        //开始计算 3秒之后自动跳过
        final CountDownTimer timer=new CountDownTimer(3000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(overTag == "OverTag"){
                    //表示倒计时被用户被用户手动取消
                    cancel();
                    jumpToHomeActivity();
                    finish();
                }else {
                    //Configfile.Log(AdvertisingActivity.this,"倒计时");
                }
            }

            @Override
            public void onFinish() {
                //表示 自动结束跳转
                cancel();
                jumpToHomeActivity();
                finish();
            }
        }.start();
    }

    /* 此方法表示如果用户手动点击跳过那么通知倒计时取消 */
    @OnClick(R.id.adv_downTime)
    public void OverTimerToHomeActivity(View v){
        overTag = "OverTag";
    }

    /* 此函数执行图片下载 */
    public void DownImagerViewRes(){
        Picasso.with(this).load(Configfile.IMAGERVIEW_PATH).into(mImageView);
    }

    public void jumpToHomeActivity(){
        /* 跳转登陆页面 */
        startActivity(new Intent(this,RegisteActivity.class));
    }





}
