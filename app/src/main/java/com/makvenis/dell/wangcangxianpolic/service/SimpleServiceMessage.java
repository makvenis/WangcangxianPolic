package com.makvenis.dell.wangcangxianpolic.service;

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
import com.makvenis.dell.wangcangxianpolic.help.MessageEvent;

import org.greenrobot.eventbus.EventBus;

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

    private void execute(Intent intent){

        while (isMessage) try {
            Thread.sleep(10000);
            Bundle extras = intent.getExtras();
            // 消息为一条可以访问的地址
            String mPath = extras.getString("mMessage");
            new HttpUtils(10000).send(HttpRequest.HttpMethod.GET,
                    mPath,
                    new RequestCallBack<String>() {
                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            String result = responseInfo.result;
                            if (result != null) {
                                if(isMessage){
                                    // 发送一条广播
                                    EventBus.getDefault().post(new MessageEvent(result));
                                    isMessage = false;
                                }
                            } else {
                                EventBus.getDefault().post(new MessageEvent("null_message"));
                            }
                        }

                        @Override
                        public void onFailure(HttpException e, String s) {
                            Log.e("TAG",new Date() + " 获取服务器消息失败 >>> 连接失败 ");
                        }
                    });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
