package com.makvenis.dell.wangcangxianpolic.otherActivity;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.makvenis.dell.wangcangxianpolic.help.JSON;
import com.makvenis.dell.wangcangxianpolic.newdbhelp.AppMothedHelper;
import com.makvenis.dell.wangcangxianpolic.tools.Configfile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 使用本类来注解当前操作的目的
 */

public class HistoryUtils {

    /* 获取当前的单位名称及其ID */
    public static void getServiceAddrs(){

    }

    /* 数据库操作 获取当前用户名称 */
    public static String getUserName(Context context){
        AppMothedHelper helper=new AppMothedHelper(context);
        Map<Object, Object> map = helper.queryByKey(Configfile.USER_DATA_KEY);
        String data = (String) map.get("data");
        Map<String, String> map1 = JSON.GetJsonRegiste(data);
        String s = map1.get("username");
        Log.e("TAG",new Date() + " >>> 当前用户名称 "+s);
        if( s != null ){
            return s;
        }
        return "ssdai";
    }

    /* 获取系统当前的时间 */
    public static String showLocahostTime() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return String.valueOf(sdf.format(d));
    }


    /* 数据库操作 */
    /**
     * @param id 单位ID
     * @param log 检查的不揍及其事项
     * @param zhuangtai 当前检查的状态
     */
    public static void executeAddrs(Context context,String id,String log,String zhuangtai){

        Log.e("TAG",new Date()+" >>> 在执行用户操作的日志时候 传递的参数"+" id:"+id+"=====log:"+log);
        if(id == null || log == null || zhuangtai == null){
            return;
        }

        String userName = getUserName(context);
        String time = showLocahostTime();

        AppMothedHelper motheh=new AppMothedHelper(context);
        String sql = "insert into addrs(className,zhuangtai,danwei,user,time)" +
                " values('"+log+"','"+zhuangtai+"','"+id+"','"+userName+"','"+time+"')";
        Log.e("TAG",new Date()+" >>> 预备执行的SQLite语句 "+sql);
        motheh.executeSql(sql);

    }

    /* 用到的网络框架（表示当需要单位等信息的是偶来区获取并且解析） */
    public static void DownDataStrong(String path, final Handler handler){
        new HttpUtils(10000).send(HttpRequest.HttpMethod.GET,
                path,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        if(result != null){
                            Message msg=new Message();
                            msg.what=0x00017; //单独的id
                            msg.obj=result;
                            handler.sendMessage(msg);
                        }
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                    }
                });
    }



}
