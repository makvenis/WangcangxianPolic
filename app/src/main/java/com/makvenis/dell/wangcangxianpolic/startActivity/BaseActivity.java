package com.makvenis.dell.wangcangxianpolic.startActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Window;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.makvenis.dell.wangcangxianpolic.R;
import com.makvenis.dell.wangcangxianpolic.help.JSON;
import com.makvenis.dell.wangcangxianpolic.help.MessageEvent;
import com.makvenis.dell.wangcangxianpolic.help.SimpleBaiDuMap;
import com.makvenis.dell.wangcangxianpolic.newdbhelp.AppMothedHelper;
import com.makvenis.dell.wangcangxianpolic.service.SimpleServiceMessage;
import com.makvenis.dell.wangcangxianpolic.tools.Configfile;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class BaseActivity extends AppCompatActivity {

    private int anInt;

    public LocationClient mLocationClient = null;
    private SimpleBaiDuMap myListener = new SimpleBaiDuMap();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* 在set方法之前告诉Windows使用哪一种方式跳转 */
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_base);

        /* Activity之间的跳转动画全局定义 */
        /* 调用系统内置的切换动画(或者自定仪) */
        Transition explode = TransitionInflater.from(this).inflateTransition(R.transition.explode);
        Transition slide = TransitionInflater.from(this).inflateTransition(R.transition.slide);
        Transition fade = TransitionInflater.from(this).inflateTransition(R.transition.fade);
        /* 告诉Windows在什么情况下使用上面的动画 */
        //第一次进入Activity
        getWindow().setEnterTransition(slide);
        //再次进入Activity时候
        getWindow().setReenterTransition(slide);
        //当退出时候
        getWindow().setExitTransition(slide);
        /**
         * @ 解释： 使用案例startActivity(new Intent(this,SeekActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
         *
         */


        /* 注册SimpleService 服务的消息 */
        Intent intent=new Intent(this, SimpleServiceMessage.class);
        Bundle bundle=new Bundle();
        bundle.putString("mMessage",Configfile.MESSAGE_PATH);
        intent.putExtras(bundle);
        startService(intent);


        /* 绑定广播事件 */
        EventBus.getDefault().register(this);

        /* 网络判断 */
        anInt = boolenNet();

        if(anInt == 0){
            Configfile.Log(this,"当前无网络连接,请检查网络！");
        }else if(anInt == 3){
            Configfile.Log(this,"当前4G网络连接,请注意流量使用！");
        }

        //-------------------------------百度地图的SDK配置------------------------------//
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();

        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，设置定位模式，默认高精度
        //LocationMode.Hight_Accuracy：高精度；
        //LocationMode. Battery_Saving：低功耗；
        //LocationMode. Device_Sensors：仅使用设备；

        option.setCoorType("bd09ll");
        //可选，设置返回经纬度坐标类型，默认gcj02
        //gcj02：国测局坐标；
        //bd09ll：百度经纬度坐标；
        //bd09：百度墨卡托坐标；
        //海外地区定位，无需设置坐标类型，统一返回wgs84类型坐标

        option.setScanSpan(1000*60*5);
        //可选，设置发起定位请求的间隔，int类型，单位ms
        //如果设置为0，则代表单次定位，即仅定位一次，默认为0
        //如果设置非0，需设置1000ms以上才有效

        option.setOpenGps(true);
        //可选，设置是否使用gps，默认false
        //使用高精度和仅用设备两种定位模式的，参数必须设置为true

        option.setLocationNotify(true);
        //可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false

        option.setIgnoreKillProcess(true);
        //可选，定位SDK内部是一个service，并放到了独立进程。
        //设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)

        option.SetIgnoreCacheException(false);
        //可选，设置是否收集Crash信息，默认收集，即参数为false

        option.setWifiCacheTimeOut(5*60*1000);
        //可选，7.2版本新增能力
        //如果设置了该接口，首次启动定位时，会先判断当前WiFi是否超出有效期，若超出有效期，会先重新扫描WiFi，然后定位

        option.setEnableSimulateGps(false);
        //可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false


        option.setIsNeedAddress(true);
        //可选，是否需要地址信息，默认为不需要，即参数为false
        //如果开发者需要获得当前点的地址信息，此处必须为true


        option.setIsNeedLocationDescribe(true);
        //可选，是否需要位置描述信息，默认为不需要，即参数为false
        //如果开发者需要获得当前点的位置信息，此处必须为true

        mLocationClient.setLocOption(option);
        //mLocationClient为第二步初始化过的LocationClient对象
        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        //更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明

        // TODO: 2018/4/23 是否允许定位
        mLocationClient.start();
        //mLocationClient为第二步初始化过的LocationClient对象
        //调用LocationClient的start()方法，便可发起定位请求
        //-------------------------------百度地图的SDK配置 结束----------------------------//

        /* 接口回调 */
        myListener.GetOnClinkMapListener(new SimpleBaiDuMap.GetOnClinkMapListener() {
            @Override
            public void showMap(double x, double y) { //回调的经纬度

            }

            @Override
            public void showPath(String path) { //回调具体地址
                //Configfile.Log(BaseActivity.this,"当前地址 "+path);
                Log.e("TAG","当前地址 "+path);
                //  TODO: 2018/4/13 当获取到了地址以后需要做的事情
                getLocalPath(path);
            }

            @Override
            public void showErrorCode(int error) { //回调错误码
                //Configfile.Log(BaseActivity.this,"定位失败！["+error+"]");
                Log.e("TAG","定位失败！["+error+"]");
            }
        });

        /* 定义全局变量 */
        getSqliteName();


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /* 注册处理广播事件 */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void executeMessage(MessageEvent msg){
        Log.e("TAG",new Date() + " BaseActivity 全局获取的消息对象 >>> " + msg.getMessage());
        getLocalMessage(msg.getMessage());
        // 解析Message消息队列
        List<Map<String, String>> maps = setMessageJson(msg.getMessage());
        // 存储消息队列



        // 判断是否需要显示NotifyCation
        boolean flags=true;
        if(maps.size() != 0){
            if(flags){
                AppMothedHelper helper=new AppMothedHelper(this);
                boolean dismisData = helper.isDismisData(this, "msg");
                Log.e("html",maps.size()+" >>>> "+ "是否存在"+dismisData);
                if(dismisData == true){
                    helper.update("msg",maps.toString());
                }else {
                    helper.dbInsert(new String[]{"msg",maps.toString()});
                }
                //设置显示通知
                setNotifyCation(maps);
                flags=false;
            }


        }
    }

    /* 获取当前地理位置数据 父类方法 */
    public void getLocalPath(String str){}

    /* 获取当前Message推送 父类方法 */
    public void getLocalMessage(String str){}

    /* 获取用户信息列表 */
    public void getSqliteName(){
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

    }
    /* 传递登陆当前用户 */
    public void getUserName(String s){}



    /* 当前全局NotifyCation */
    private void setNotifyCation(List<Map<String, String>> maps){
        Map<String, String> map = maps.get(0);
        String s = maps.toString();

        Intent intent=new Intent(this, NotiflyActivity.class);

        EventBus.getDefault().post(new MessageEvent(s));

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationManager manager = (NotificationManager) this.getSystemService(this.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(this);
        builder.setTicker("新的通知");
        builder.setSmallIcon(R.drawable.icon);
        builder.setContentTitle(map.get("title"));
        builder.setContentText(map.get("remark"));
        builder.setContentIntent(pendingIntent);
        builder.setOnlyAlertOnce(true);
        Notification notification = builder.build();
        //notification.flags = Notification.FLAG_AUTO_CANCEL;
        manager.notify(0, notification);


    }

    /* 处理Message 消息队列 */
    private List<Map<String, String>> setMessageJson(String message){
        try {

            JSONObject object=new JSONObject(message);
            JSONArray array = object.getJSONArray("newsList");
            List<Map<String, String>> maps = JSON.GetJson(array.toString(), new String[]{"title", "addtime", "laiyuan", "content","remark"});
            return maps;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /* 网络判断 */
    private int boolenNet(){

            int netType = 0;//没有网络
            ConnectivityManager connMgr = (ConnectivityManager)
                    this.getSystemService(this.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            int nType = networkInfo.getType();
            if (nType == ConnectivityManager.TYPE_WIFI) {
                netType = 1;// wifi
            } else if (nType == ConnectivityManager.TYPE_MOBILE) {
                int nSubType = networkInfo.getSubtype();
                TelephonyManager mTelephony = (TelephonyManager) this
                        .getSystemService(this.TELEPHONY_SERVICE);
                if (nSubType == TelephonyManager.NETWORK_TYPE_UMTS
                        && !mTelephony.isNetworkRoaming()) {
                    netType = 2;// 3G网络
                } else {
                    netType = 3;// 2G网络
                }
            }
            return netType;
    }









}
