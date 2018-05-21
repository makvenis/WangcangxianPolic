package com.makvenis.dell.wangcangxianpolic.startActivity;

import android.app.NotificationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.makvenis.dell.wangcangxianpolic.R;
import com.makvenis.dell.wangcangxianpolic.help.MessageEvent;
import com.makvenis.dell.wangcangxianpolic.newdbhelp.AppMothedHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;

@ContentView(R.layout.activity_notifly)
public class NotiflyActivity extends AppCompatActivity {

    @ViewInject(R.id.mNotifyWeb)
    WebView mWebView;


    String s="<p><br/></p><p style=\"margin-top: 0px; margin-bottom: 0px;\"><span style=\"font-size: medium;\">随着现在社会的不断进步，人们生活意识和方式都发生了改变，结婚前，新人都想用拍婚纱照的方式来纪念彼此之间恋爱时候的美好，在我们大多数人的印象中，拍婚纱照肯定要带着幸福的笑容，拍出满面春风的感觉才叫拍婚纱照。</span></p><p style=\"margin-top: 0px; margin-bottom: 0px;\">&nbsp;</p><p style=\"margin-top: 0px; margin-bottom: 0px; text-align: center;\"><img src=\"../../upload/20180313162641635.jpg\" width=\"535\" height=\"766\" alt=\"\" style=\"width: 535px; height: 766px;\"/></p><p style=\"margin-top: 0px; margin-bottom: 0px;\">&nbsp;</p><p style=\"margin-top: 0px; margin-bottom: 0px;\"><span style=\"font-size: medium;\">　　但是我们别忘了，这个世界上还有很大一部分的人是不爱笑的哦，如果本身就不爱笑的人硬要逼他挤出笑容来拍婚纱照，拍出来的婚纱照可想而知是不漂亮的。也还有的是因为紧张而不会笑的新娘，有些也可能是天生就是这样的高冷范。淘拍拍婚纱摄影小编要说的就是，笑与不笑其实只是取决于你个人喜好的，做出真的自己才能在拍婚纱照的时候有更出色的表现。</span></p><p style=\"margin-top: 0px; margin-bottom: 0px;\">&nbsp;</p><p style=\"margin-top: 0px; margin-bottom: 0px;\"><span style=\"font-size: medium;\">　　如果“笑”不好，还不如不笑，拍婚纱照的时候多想一些你们之间的甜蜜往事，用您们日常相处的当时来拍婚纱照，怎么自然怎么拍，用享受的表情去替代挤不出来的笑会更加美好哦。或者嘴角上扬45°，嘴角轻轻上扬闭上眼睛，任由摄影师去捕捉你们之间的甜蜜细节，以最自然最轻松的相处方式来应对拍婚纱照，其他的事情就交给专业的摄影师去做吧。</span></p><p style=\"margin-top: 0px; margin-bottom: 0px;\">&nbsp;</p><p style=\"margin-top: 0px; margin-bottom: 0px; text-align: center;\"><br/></p><p style=\"margin-top: 0px; margin-bottom: 0px; text-align: center;\">&nbsp;</p><p style=\"margin-top: 0px; margin-bottom: 0px;\"><span style=\"font-size: medium;\">　　你也可以在拍婚纱照的时候多加一些动作，让您和爱人之间的互动更加丰富，您也可以托腮、轻抚额头或遮住嘴部，还是把捕捉美好画面的工作交给摄影师去做吧，在最后淘拍拍婚纱摄影小编要说一句题外话：在拍婚纱照之前，一定要做好皮肤保养工作，保持肌肤水润才能避免拍婚纱照时化妆干巴巴，皮肤是吃不住粉底很容易脱妆的哦。</span></p><p><br/></p><p><br/></p>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);

        EventBus.getDefault().register(this);


        /* 设置WebView */
        //开启JavaScript支持
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl("file:///android_asset/list.html");
        mWebView.addJavascriptInterface(new NotiflyActivity.JSinterface(), "JS");// 设置本地调用对象及其JS访问名称

    }

    private void setHtml() {
        mWebView.post(new Runnable() {
            @Override
            public void run() {
                /* 消息查询 */
                AppMothedHelper helper=new AppMothedHelper(NotiflyActivity.this);
                Map<Object, Object> map = helper.queryByKey("msg");
                String data = (String) map.get("data");
                Log.e("html",">>> 查询结果:"+data);
                //List<Map<String, String>> maps = JSON.GetJson(data, new String[]{"title", "addtime", "laiyuan", "content", "remark"});
                //
                //Log.e("html"," 准备赋值 "+maps.toString());
                /*if(m != null && m.size() > 0){
                    mWebView.loadUrl("javascript: setTitle('" + m.get("title") + "')");
                    mWebView.loadUrl("javascript: setName('" +  m.get("laiyuan") + "')");
                    mWebView.loadUrl("javascript: setTimes('" + m.get("addtime") + "')");
                    String s = m.get("content").replace("../../", Configfile.SERVICE_WEB_IMG);
                    mWebView.loadUrl("javascript: setContext('" + s + "')");
                }*/
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setHtmlNotiflycation(MessageEvent msg){
        if(msg.getMessage() != null){
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            //移除标记为id的通知 (只是针对当前Context下的所有Notification)
            notificationManager.cancelAll();
        }
    }


    /* 构建中间类 */
    public class JSinterface {
        @JavascriptInterface
        public void execute() {

            setHtml();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
