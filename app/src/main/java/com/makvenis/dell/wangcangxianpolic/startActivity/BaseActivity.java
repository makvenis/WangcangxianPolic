package com.makvenis.dell.wangcangxianpolic.startActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.makvenis.dell.wangcangxianpolic.R;
import com.makvenis.dell.wangcangxianpolic.help.JSON;
import com.makvenis.dell.wangcangxianpolic.help.SimpleBaiDuMap;
import com.makvenis.dell.wangcangxianpolic.newdbhelp.AppMothedHelper;
import com.makvenis.dell.wangcangxianpolic.tools.Configfile;
import com.makvenis.dell.wangcangxianpolic.tools.NetworkTools;
import com.makvenis.dell.wangcangxianpolic.utils.NetUtil;

import java.util.Date;
import java.util.Map;

public class BaseActivity extends AppCompatActivity{

    private int anInt;

    NetBroadcastReceiver mReceiver;

    public LocationClient mLocationClient = null;
    private SimpleBaiDuMap myListener = new SimpleBaiDuMap();

    public Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 2){
                Log.e("TAG", new Date() + " >>>>当前回调地址结果 " + ((String) msg.obj)+"");
            }
        }
    };

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
        getWindow().setEnterTransition(explode);
        //再次进入Activity时候
        getWindow().setReenterTransition(explode);
        //当退出时候
        getWindow().setExitTransition(explode);
        /**
         * @ 解释： 使用案例startActivity(new Intent(this,SeekActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
         *
         */
        /* 广播注册 */
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mReceiver = new NetBroadcastReceiver();
        this.registerReceiver(mReceiver, filter);


        /* 网络判断 */
        anInt = boolenNet();

        if(anInt == 0){
            //startActivity(new Intent(this,NoActivity.class));
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

            SharedPreferences pref = getSharedPreferences("set",MODE_PRIVATE);

            @Override
            public void showMap(final double x, final double y) { //回调的经纬度
                boolean mPathBoolean = pref.getBoolean("dingwei", false);
                if(mPathBoolean == true){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                        /* 实例地址 http://ssdaixiner.oicp.net:26168/wcjw/mobile/doSaveCoordinates?longitude=106.302964&latitude=32.229278 */
                            try {
                                Thread.sleep(3000);
                                String mX = String.valueOf(x);
                                String mY = String.valueOf(y);
                                String path = Configfile.UPDATE_MAP_PATH+"?longitude="+mX+"&latitude="+ mY+
                                        "&username="+getSqliteName();
                                NetworkTools.getHttpTools(path,mHandler,2);

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }else {
                    Configfile.Log(BaseActivity.this,"请在设置中开启定位");
                }

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
        unregisterReceiver(mReceiver);
    }

    /* 传递获取当前地理位置数据 父类方法 */
    public void getLocalPath(String str){}

    /* 传递获取当前Message推送 父类方法 */
    public void getLocalMessage(String str){}

    /* 传递登陆当前用户 */
    public void getUserName(String s){}

    /* 获取用户信息列表 */
    public String getSqliteName(){
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
            getUserName("");
        }
        return s;
    }

    /* 网络判断 */
    @Deprecated
    private int boolenNet(){

        int netType = 0;//没有网络
        ConnectivityManager connMgr = (ConnectivityManager)
                this.getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        /* 如果没有网络 这里经过调试发现返回的是null */
        if (networkInfo == null) {
            return netType;
        }

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


    /**
     * 判断网络连接的广播 {@link NetBroadcastReceiver}
     */

    public class NetBroadcastReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            // 如果相等的话就说明网络状态发生了变化
            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                int netId = NetUtil.boolenNet(context);
                Log.e("NetBroadcastReceiver"," 网络当前广播值: >>> " + netId+"");
                if(netId == 0){
                    startActivity(new Intent(BaseActivity.this,NoActivity.class));
                }

            }
        }


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
                System.exit(0);
                // TODO: 2018/4/2 退出APP的事件
            }
        }
        return true;
    }


}
