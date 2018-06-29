package sc.gys.wcx.and.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * Created by dell on 2018/5/28.
 */

public class SimpleUpdateUserImagePhoto extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public SimpleUpdateUserImagePhoto(String name) {
        super(name);
    }

    public SimpleUpdateUserImagePhoto() {
        super("mName");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Bundle extras = intent.getExtras();
        String id = extras.getString("id");     //请求头 id
        String name = extras.getString("name"); //请求头 url
        String url = extras.getString("url");   //服务器地址

        RequestParams params=new RequestParams();
        params.addBodyParameter("id",id);
        params.addBodyParameter("url",name);

        new HttpUtils(10000).send(HttpRequest.HttpMethod.POST,
                url,
                params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {

                    }

                    @Override
                    public void onFailure(HttpException e, String s) {

                    }
                });

    }
}
