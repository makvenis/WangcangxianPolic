package sc.gys.wcx.and.broadcastReceiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import sc.gys.wcx.and.help.JSON;
import sc.gys.wcx.and.startActivity.NotiflyActivity;
import sc.gys.wcx.and.tools.Configfile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 *  定义NotifyCation的广播接收者
 */

public class MyNotifyCationBroadcastReceiver extends BroadcastReceiver {

    public String TAG="MyNotifyCationBroadcastReceiver";

    // 复写onReceive()方法
    // 接收到广播后，则自动调用该方法
    @Override
    public void onReceive(Context context, Intent intent) {
        //写入接收广播后的操作
        if(intent.getAction().equals("NOTIFY_NEWS")){
            try {
                String message = intent.getStringExtra("notify");
                Log.e(TAG,message);
                // 解析Message消息队列
                JSONObject object = new JSONObject(message);
                JSONArray array = object.getJSONArray("newsList");
                if(array.length() > 0){
                    List<Map<String, String>> maps = JSON.GetJson(array.toString(), new String[]{"title","remark","cid","id"});
                    if(maps.size() > 0){
                        Map<String, String> stringMap = maps.get(0);
                        // 存储消息队列
                        if(stringMap != null){
                            setNotifyCation(stringMap,context);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }

    /* 打开NotifyCation */
        /* 当前推送NotifyCation */
    private void setNotifyCation(Map<String, String> map, Context mContext){

        /* 通知ID 有两处需要到这个ID的使用 所以提成全局 */
        int notiflyId=0;
        /* 转化string  因为只有传String类型 */

        Intent intent=new Intent(mContext, NotiflyActivity.class);
        intent.putExtra("id",map.get("id")); //页面参数ID
        intent.putExtra("url", Configfile.NEWS_ALL_CONTENT_PATH);//页面域名
        intent.putExtra("notiflyId",String.valueOf(notiflyId)); //需要关闭的NotiflyCation的id

        /**
         *  int FLAG_CANCEL_CURRENT：如果该PendingIntent已经存在，则在生成新的之前取消当前的。
         *  int FLAG_NO_CREATE：如果该PendingIntent不存在，直接返回null而不是创建一个PendingIntent。
         *  int FLAG_ONE_SHOT:该PendingIntent只能用一次，在send()方法执行后，自动取消。
         *  int FLAG_UPDATE_CURRENT：如果该PendingIntent已经存在，则用新传入的Intent更新当前的数据。
         *  我们需要把最后一个参数改为PendingIntent.FLAG_UPDATE_CURRENT,这样在启动的Activity里就可以用接收Intent传送数据              *  的方法正常接收。
         */

        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager manager = (NotificationManager) mContext.getSystemService(mContext.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(mContext);
        builder.setTicker("通知");
        builder.setSmallIcon(sc.gys.wcx.and.R.drawable.icon);
        builder.setContentTitle(map.get("title"));
        builder.setContentText( map.get("remark"));
        builder.setContentIntent(pendingIntent);
        builder.setOnlyAlertOnce(true);
        Notification notification = builder.build();
        //notification.flags = Notification.FLAG_AUTO_CANCEL;
        manager.notify(notiflyId, notification);
    }


}
