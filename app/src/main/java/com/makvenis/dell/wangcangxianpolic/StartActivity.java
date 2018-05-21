package com.makvenis.dell.wangcangxianpolic;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.makvenis.dell.wangcangxianpolic.login.RegisteActivity;
import com.makvenis.dell.wangcangxianpolic.newdbhelp.AppMothedHelper;
import com.makvenis.dell.wangcangxianpolic.startActivity.AdvertisingActivity;
import com.makvenis.dell.wangcangxianpolic.startActivity.HomeActivity;
import com.makvenis.dell.wangcangxianpolic.tools.Configfile;
import com.makvenis.dell.wangcangxianpolic.tools.NetworkTools;

import java.util.Map;

/* 作者  王从文 */
/* 全局采用注解模式 */
/* 旺苍县公安局巡防系统 首页界面 */

/**
 * @ STOPSHIP: 2018/3/31
 * @ Context: 预加载部分条件
 * @ Context: 选择执行的条件 获取网络连接情况
 *
 *
 */
@ContentView(R.layout.activity_start)
public class StartActivity extends AppCompatActivity {

    //页面标签
    private String ObjectName="引导页面";
    //LOG日志
    private static final String TAG="TAG";
    //全局时间规定
    public static int TIME = 3;
    //标签
    public static String TAG_DOWN = null;

    //首页图片
    @ViewInject(R.id.start_activity_img)
    private ImageView mImageView;

    //全局Handler
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 0X000002){
                TAG_DOWN = ((String) msg.obj);
            }
            super.handleMessage(msg);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);

        /* 当倒计时结束或者图片资源下载完毕跳转 跳转广告页面然后再跳转HomeActivity */
        /* 当时间过3秒之后时间图片资源还未下载完毕 跳转HomeActivity */
        /* 子线程下载 */
        new Thread(new Runnable() {
            @Override
            public void run() {
                NetworkTools.NetShow(Configfile.IMAGERVIEW_PATH,mHandler);
            }
        }).start();
        /* 启动计时器 */
        CountTime();

        /* 动画 */
        CreatAnimacationAll();

    }

    private void CreatAnimacationAll() {

        //定义动画集合
        AnimationSet set=new AnimationSet(true);

        //缩放
        Animation alphAnimation=new AlphaAnimation(0f, 1f);
        alphAnimation.setDuration(3000);//设置动画持续时间为3秒
        alphAnimation.setFillAfter(true);//设置动画结束后保持当前的位置（即不返回到动画开始前的位置）
        //渐变

        Animation scaleAnimation= AnimationUtils.loadAnimation(this, R.anim.login_scale_img);
        //加载Xml文件中的动画imgShow.startAnimation(scaleAnimation2);

        set.addAnimation(alphAnimation);
        set.addAnimation(scaleAnimation);
        mImageView.startAnimation(set);


    }

    private void CountTime() {
        /** 倒计时4秒，一次1秒 */
        CountDownTimer timer = new CountDownTimer(TIME*1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // TODO Auto-generated method stub
                //假如时间还没有结束 但是图片下载完成跳转
                if(TAG_DOWN == "Down"){
                    //停止倒计时
                    cancel();
                    //判断是否经过广告
                    if(Configfile.IS_ADV == true){
                        //通过广告
                        startActivity(new Intent(StartActivity.this, AdvertisingActivity.class));
                        finish();
                    }else {
                        boolean base = setUserDataBase();
                        if(base == true){ //数据库存在用户
                            //直接通过HOME
                            startActivity(new Intent(StartActivity.this, HomeActivity.class));
                        }else { //通过登陆才进入
                            //进入登陆页面
                            startActivity(new Intent(StartActivity.this, RegisteActivity.class));
                        }
                        finish();
                    }
                }
            }

            @Override
            public void onFinish() {
                //假如时间完成 不管图片下载完成与否页跳转
                boolean base = setUserDataBase();
                if(base == true){ //数据库存在用户
                    Log.e("TAG","图片加载完成>>>存在用户信息");
                    //直接通过HOME
                    startActivity(new Intent(StartActivity.this, HomeActivity.class));
                }else { //通过登陆才进入
                    Log.e("TAG","图片加载完成>>>不存在用户信息");
                    //进入登陆页面
                    startActivity(new Intent(StartActivity.this, RegisteActivity.class));
                }
                finish();
            }
        }.start();

    }

    /* 用户数据转存 */
    public boolean setUserDataBase(){
        /* 判断当前是否有数据obj */
        AppMothedHelper helper=new AppMothedHelper(this);
        Map<Object, Object> map = helper.queryByKey(Configfile.USER_DATA_KEY);
        String data = (String) map.get("data");
        if(data != null){
            Log.e("TAG","存在用户信息");
            return true;
        }else { //存在用户信息
            Log.e("TAG","不存在用户信息");
            return false;
        }
    }

}
