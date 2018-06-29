package sc.gys.wcx.and.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/* 当用户打开手机的时候 接收推送消息 */
/* 全局采用广播形式注册和接收广播消息 */

public class SimpleServiceMessage extends IntentService{
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public SimpleServiceMessage(String name) {
        super(name);
    }

    public SimpleServiceMessage() {
        super("mName");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        execute(intent);
    }

    private boolean isMessage=true;

    private void execute(final Intent intent){

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Bundle extras = intent.getExtras();
        // 消息为一条可以访问的地址
        String mPath = extras.getString("mMessage");
        new HttpUtils(1000).send(HttpRequest.HttpMethod.GET,
                mPath,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {

                        try {
                            String result = responseInfo.result;
                            byte[] bytes = result.getBytes();
                            String str = new String(bytes, "utf-8");
                            if (str != null) {
                                isMessage=false;
                                Log.e("TAG",str);
                                Thread.sleep(2000);
                                //EventBus.getDefault().post(new MessageEventService(str,true));
                                Intent send=new Intent();
                                send.putExtra("notify",str);
                                send.setAction("NOTIFY_NEWS");
                                send.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                                getApplicationContext().sendBroadcast(send);
                                Log.e("TAG","广播发送成功！");
                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        Log.e("TAG",new Date() + " 获取服务器消息失败 >>> 连接失败 ");
                    }
                });
    }

}
