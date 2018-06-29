package sc.gys.wcx.and.startActivity;

import android.app.ActivityOptions;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import sc.gys.wcx.and.activity.AlertPushNewsActivity;
import sc.gys.wcx.and.tools.Configfile;
import sc.gys.wcx.and.R;

/**
 *
 * @解释 适用于所有的新闻页面的详情页面
 * @参数 接收的参数未id
 * @参数 粗腰传递的是预备访问的地址
 *
 */
@ContentView(R.layout.activity_notifly)
public class NotiflyActivity extends AppCompatActivity {

    public final String TAG="NotiflyActivity";

    @ViewInject(R.id.mNotifyWeb)
    WebView mWebView;

    /* 处理toolbar 开始 version=2  */
    /* include 里面的点击事件 */
    @ViewInject(R.id.toolbar_callbank)
    ImageView mImageView_bank;
    @ViewInject(R.id.toolbar_callbank_text)
    TextView mBankTextView;
    @ViewInject(R.id.mToolbar_text)
    TextView mTextView;
    /* 处理toolbar 结束 */

    /* 说明有哪一个类跳转公共类 便于返回上一级 */
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);

        /* 文本字 */
        mTextView.setText("法律法规");

        /* 获取notifyCation传递的参数值  */
        String id = getIntent().getStringExtra("id");
        String url = getIntent().getStringExtra("url");
        String colseId = getIntent().getStringExtra("notiflyId");
        type = getIntent().getStringExtra("AlertPushNewsActivity");
        Log.e(TAG,TAG+">>>>"+url+id+type);

        /* 设置WebView */
        //开启JavaScript支持
        mWebView.getSettings().setJavaScriptEnabled(true);
        /* 判断必要参数是否缺失 */
        if(id == null || url == null){
            Configfile.Log(this,"参数错误 [Error] "+id+" or "+url);
            bankJumpActivity();
        }
        mWebView.loadUrl(url+id);

        /*实例地址: http://ssdaixiner.oicp.net:26168/wcjw/mobile/news/newsDetail2?id=318 */

        /* 通知关闭 */
        if(colseId != null){
            /* String 转 Int */
            int colse = Integer.valueOf(colseId).intValue();
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            //移除标记为id的通知 (只是针对当前Context下的所有Notification)
            notificationManager.cancel(colse);
        }
    }

    /* 返回 */
    @OnClick({R.id.toolbar_callbank})
    public void oncklinkViewImage(View v){
        bankJumpActivity();
    }

    /* 返回 */
    @OnClick({R.id.toolbar_callbank_text})
    public void oncklinkViewTextView(View v){
        bankJumpActivity();
    }


    /* 返回实例对象的方法（也就是当由哪一个类跳转过来的 当需要返回的时候 按照原路径返回 并且判断参数） */
    public void bankJumpActivity(){
        if(type.equals("AlertPushNewsActivity")){
            startActivity(new Intent(this,AlertPushNewsActivity.class));
            Log.e(TAG,type);
        }else {
            startActivity(new Intent(this, HomeActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        }
    }

}
