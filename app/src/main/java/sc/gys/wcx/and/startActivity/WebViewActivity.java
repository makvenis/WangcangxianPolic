package sc.gys.wcx.and.startActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import sc.gys.wcx.and.company.CompanyActivity;
import sc.gys.wcx.and.correctActivity.CorrectCommandActivity;
import sc.gys.wcx.and.help.MessageEvent;
import sc.gys.wcx.and.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;

/* 作者  王从文 */
/* 全局采用注解模式 */
/* 旺苍县公安局巡防系统 WebView的基本属性 */

/**
 * @// STOPSHIP: 2018/4/1
 * @ Content: 加载几张表格（type=1,2,3,4）
 * @ 解释: 加载Toolbar
 * @ 外链: 整改，返回首页，笔录
 * @
 */

@ContentView(R.layout.activity_web_view)
public class WebViewActivity extends BaseActivity {

    @ViewInject(R.id.webView)
    private WebView mWebView;

    @ViewInject(R.id.toolbar)
    Toolbar mToolbar;

    @ViewInject(R.id.toolbar_name)
    TextView mTextView;

    /* bundle 中获取的标题 */
    private String mTitle_intent;

    /* bundle 中获取的Url */
    private String mUrl_intent;
    private String mCid;
    /* 单位id */
    private String id;


    /* 图片上传 */
    @ViewInject(R.id.mUploadImage)
    Button mSubmit;

    /* mUploadImageShow */
    @ViewInject(R.id.mUploadImageShow)
    Button mUploadImageShow;



    @OnClick({R.id.mUploadImageShow})
    public void showImage(View v){
        Log.e("mDataBase_id","上传完毕之后查看图片的集合id >>> \n"+message);
        Intent intent=new Intent(this,ShowUploadImageActivity.class);
        /**
         *mTitle_intent = bundle.getString("mTitle");
         mUrl_intent = bundle.getString("mUrl");
         mCid = bundle.getString("mCid");
         id = bundle.getString("id");
         */
        intent.putExtra("mTitle",mTitle_intent);
        intent.putExtra("mUrl",mUrl_intent );
        intent.putExtra("mCid", mCid);
        intent.putExtra("id",id);
        startActivity(intent);

    }

    /* 接收广播 */
    String message;
    @Deprecated
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getUrl(MessageEvent msg){
        message = msg.getMessage();
        Log.e("mDataBase_id","上传完毕之后查看图片的集合id >>> \n"+message);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);

        /* 获取父类传递过来的参数 */
        getParmentData();

        /* 设置Toolbar */
        setToolbar();

        /* 设置标题 在Toolbar里面就已经设置了 */
        //mTextView.setText(mTitle_intent);

        /* 设置WebView的基本属性 */
        setWebView();

        EventBus.getDefault().register(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /* 获取参数（上一级页面传递过来的必要参数） */
    public void getParmentData() {
        //页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        mTitle_intent = bundle.getString("mTitle");
        mUrl_intent = bundle.getString("mUrl");
        mCid = bundle.getString("mCid");
        id = bundle.getString("id");

    }

    /* 设置webView的基本属性 */
    private void setWebView(){
        WebSettings webSettings = mWebView.getSettings();
        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);

        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        mWebView.loadUrl(mUrl_intent+"&bianhao="+mCid);
    }

    //设置标题栏Toolbar
    private void setToolbar() {
        mToolbar.setTitle(mTitle_intent);   //设置标题
        mToolbar.setSubtitle("查看基本");    //设置副标题
        mToolbar.setSubtitleTextColor(Color.RED);  //设置副标题字体颜色
        setSupportActionBar(mToolbar);   //必须使用
        //添加左边图标点击事件
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WebViewActivity.this,CompanyActivity.class));
                finish();
            }
        });

        // TODO: 2018/4/7   menu项点击事件

        //添加menu项点击事件
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.toolbar_r_1: //返回首页
                        startActivity(new Intent(WebViewActivity.this,HomeActivity.class));
                        break;
                    case R.id.toolbar_r_2: //限期整改
                        /* 构建显示的数据 */
                        ArrayList<String> mGoneData1 = new ArrayList<>();
                        mGoneData1.add("mCardView_9");
                        mGoneData1.add("mCardView_20");
                        mGoneData1.add("mCardView_23");
                        mGoneData1.add("mCardView_17");
                        mGoneData1.add("mCardView_16");
                        jumpActivity("旺苍县公安局责令限期整改治安隐患通知书",mGoneData1);
                        break;
                    case R.id.toolbar_r_3: //责令改正

                        /* 构建显示的数据 */
                        ArrayList<String> mGoneData = new ArrayList<>();
                        mGoneData.add("mCardView_9");
                        mGoneData.add("mCardView_11");
                        mGoneData.add("mCardView_13");
                        mGoneData.add("mCardView_15");
                        mGoneData.add("mCardView_16");
                        mGoneData.add("mCardView_18");
                        jumpActivity("旺苍县公安局责令改正通知书",mGoneData);

                        break;
                    case R.id.toolbar_r_4: //当场处罚
                        /* 构建显示的数据 */
                        ArrayList<String> mGoneData2 = new ArrayList<>();
                        mGoneData2.add("mCardView_9");
                        mGoneData2.add("mCardView_10");
                        mGoneData2.add("mCardView_11");
                        mGoneData2.add("mCardView_12");
                        mGoneData2.add("mCardView_21");
                        mGoneData2.add("mCardView_14");
                        mGoneData2.add("mCardView_15");
                        mGoneData2.add("mCardView_22");
                        mGoneData2.add("mCardView_16");
                        mGoneData2.add("mCardView_17");
                        jumpActivity("旺苍县公安局当场处罚决定书",mGoneData2);
                        break;
                }
                return true;    //返回为true
            }
        });
    }

    //设置menu（右边图标）
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu); //解析menu布局文件到menu
        return true;
    }

    //当数据适配时候的参数方法

    /**
     * @param mTitle AppToolbar 的标题
     * @param mGoneData AppView 当前文书需要显示的布局控件
     * {@link=into} 参考枚举
     */
    public void jumpActivity(String mTitle,ArrayList<String> mGoneData){
        //参数构建
        Bundle bundle=new Bundle();
        bundle.putString("mTitle",mTitle);
        bundle.putStringArrayList("mGoneData",mGoneData);
        bundle.putString("id",id); //单位id
        Log.e("TAG",new Date()+" >>> 单位ID(WebViewActivity) " + id);

        //跳转
        Intent intent=new Intent(WebViewActivity.this, CorrectCommandActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    /**
     *
     * @11 单位
     * @12 性别
     * @13 年龄
     * @14 出生日期 （调用）
     * @15 身份证种类
     * @16 证件号码
     * @17 法定代表人
     * @18 单位地址
     * @19 查明
     * @10 证据证实
     * @111 条款名称
     * @112 给予的处罚
     * @113 执行措施
     * @114 处罚地点
     * @115 办案警察
     * @116 对你单位进行检查时间
     * @117 整改完毕时间
     * @118 旺公责通字
     * @119 旺公字(限期整改)
     * @120 条例，措施（限期整改）
     * @121 执行方式（当场处罚）
     * @122 被检查人签名
     * @123 被检查人签名(中文汉字)
     */

    @OnClick({R.id.mUploadImage})
    public void jumActivity(View v){
        Intent intent=new Intent(this,UploadCheckActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("mTitle",mTitle_intent);
        bundle.putString("mUrl",mUrl_intent);
        bundle.putString("mCid",mCid);
        bundle.putString("id",id);
        intent.putExtras(bundle);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }


}
