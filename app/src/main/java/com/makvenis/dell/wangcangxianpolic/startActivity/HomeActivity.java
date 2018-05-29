package com.makvenis.dell.wangcangxianpolic.startActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.makvenis.dell.wangcangxianpolic.R;
import com.makvenis.dell.wangcangxianpolic.help.JSON;
import com.makvenis.dell.wangcangxianpolic.help.MessageEventService;
import com.makvenis.dell.wangcangxianpolic.newdbhelp.AppMothedHelper;
import com.makvenis.dell.wangcangxianpolic.service.SimpleServiceMessage;
import com.makvenis.dell.wangcangxianpolic.tools.Configfile;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;
import java.util.Map;

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

@ContentView(R.layout.home_activity)
public class HomeActivity extends BaseActivity {
    @ViewInject(R.id.fl)
    FrameLayout fl;

    @ViewInject(R.id.rg)
    RadioGroup mRg;

    @ViewInject(R.id.Main_button_a)
    RadioButton mRadioButton_a;

    @ViewInject(R.id.Main_button_c)
    RadioButton mRadioButton_c;

    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewUtils.inject(this);

        /* 默认加载第一个碎片 */
        manager=getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fl,new NoticeFragment());
        transaction.commit();
        /* 第一个碎片加载完毕 */

        //处理点击事件
        SetOnlinkRadioButton();
        //注册IntentService 服务
        CreatIntentService();

        /* 注册广播服务器 */
        EventBus.getDefault().register(this);

    }

    /* 注册处理广播事件 */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void executeMessage(MessageEventService msg) throws JSONException {
        Log.e("TAG",new Date() + " BaseActivity 全局获取的消息对象 >>> " + msg.getMessage());
        Log.e("TAG",msg.isBoolean()+"");
        if(msg.isBoolean() == true){
            getLocalMessage(msg.getMessage());
            // 解析Message消息队列
            String message = msg.getMessage();
            JSONObject object=new JSONObject(message);
            JSONArray array = object.getJSONArray("newsList");
            if(array.length() > 0){
                List<Map<String, String>> maps = JSON.GetJson(array.toString(), new String[]{"title","remark","cid","id"});
                if(maps.size() > 0){
                    Map<String, String> stringMap = maps.get(0);
                    // 存储消息队列
                    if(stringMap != null){
                        setNotifyCation(stringMap);
                    }
                }
            }

        }
    }

    /* 当前推送NotifyCation */
    private void setNotifyCation(Map<String, String> map){

        /* 通知ID 有两处需要到这个ID的使用 所以提成全局 */
        int notiflyId=0;
        /* 转化string  因为只有传String类型 */

        Intent intent=new Intent(this, NotiflyActivity.class);
        intent.putExtra("id",map.get("id")); //页面参数ID
        intent.putExtra("url",Configfile.NEWS_ALL_CONTENT_PATH);//页面域名
        intent.putExtra("notiflyId",String.valueOf(notiflyId)); //需要关闭的NotiflyCation的id

        /**
         *  int FLAG_CANCEL_CURRENT：如果该PendingIntent已经存在，则在生成新的之前取消当前的。
         *  int FLAG_NO_CREATE：如果该PendingIntent不存在，直接返回null而不是创建一个PendingIntent。
         *  int FLAG_ONE_SHOT:该PendingIntent只能用一次，在send()方法执行后，自动取消。
         *  int FLAG_UPDATE_CURRENT：如果该PendingIntent已经存在，则用新传入的Intent更新当前的数据。
         *  我们需要把最后一个参数改为PendingIntent.FLAG_UPDATE_CURRENT,这样在启动的Activity里就可以用接收Intent传送数据              *  的方法正常接收。
         */

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager manager = (NotificationManager) this.getSystemService(this.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(this);
        builder.setTicker("通知");
        builder.setSmallIcon(R.drawable.icon);
        builder.setContentTitle(map.get("title"));
        builder.setContentText( map.get("remark"));
        builder.setContentIntent(pendingIntent);
        builder.setOnlyAlertOnce(true);
        Notification notification = builder.build();
        //notification.flags = Notification.FLAG_AUTO_CANCEL;
        manager.notify(notiflyId, notification);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /* 调用服务 */
    private void CreatIntentService() {
        /* 注册SimpleService 服务的消息 */
        Intent intent=new Intent(this, SimpleServiceMessage.class);
        Bundle bundle=new Bundle();
        bundle.putString("mMessage", Configfile.MESSAGE_PATH+getName());
        intent.putExtras(bundle);
        startService(intent);
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

    /* 获取用户信息列表 */
    private String getName(){
        /* 数据库操作 获取当前用户名称 */
        AppMothedHelper helper=new AppMothedHelper(this);
        Map<Object, Object> map = helper.queryByKey(Configfile.USER_DATA_KEY);
        String data = (String) map.get("data");
        Map<String, String> map1 = JSON.GetJsonRegiste(data);
        String s = map1.get("username");
        Log.e("TAG",new Date() + " >>> 当前用户名称 "+s);

        if(s != null){
            getUserName(s);
        }else {
            getUserName("ssdai");
        }
        return s;
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


    @Override
    protected void onResume() {
        int id = getIntent().getIntExtra("bank_id", 0);
        if (id == 2) {//其他Activity跳转第二个碎片里面的ViewPage中第二个页面
            Fragment fragmen = new LinkFragment();
            FragmentManager fmanger = getSupportFragmentManager();
            FragmentTransaction transaction = fmanger.beginTransaction();
            transaction.replace(R.id.fl, fragmen);
            transaction.commit();
            //帮助跳转到指定子fragment
            Intent i=new Intent();
            i.setClass(HomeActivity.this,LinkFragment.class);
            i.putExtra("bank_id",2);

            mRadioButton_c.setChecked(true);

        }else if(id == 1){
            Fragment fragmen = new NoticeFragment();
            FragmentManager fmanger = getSupportFragmentManager();
            FragmentTransaction transaction = fmanger.beginTransaction();
            transaction.replace(R.id.fl, fragmen);
            transaction.commit();
            //帮助跳转到指定子fragment
            Intent i=new Intent();
            i.setClass(HomeActivity.this,NoticeFragment.class);
            i.putExtra("id",1);

            mRadioButton_a.setChecked(true);
        }else if(id == 3){ //其他Activity跳转第二个碎片里面的ViewPage中第一个页面
            Fragment fragmen = new LinkFragment();
            FragmentManager fmanger = getSupportFragmentManager();
            FragmentTransaction transaction = fmanger.beginTransaction();
            transaction.replace(R.id.fl, fragmen);
            transaction.commit();
            //帮助跳转到指定子fragment
            Intent i=new Intent();
            i.setClass(HomeActivity.this,LinkFragment.class);
            i.putExtra("bank_id",1);

            mRadioButton_c.setChecked(true);
        }
        super.onResume();

    }


}
