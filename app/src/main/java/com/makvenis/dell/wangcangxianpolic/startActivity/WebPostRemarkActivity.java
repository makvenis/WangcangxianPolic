package com.makvenis.dell.wangcangxianpolic.startActivity;

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
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.makvenis.dell.wangcangxianpolic.R;

import java.util.Date;

@ContentView(R.layout.activity_post_remark)
public class WebPostRemarkActivity extends BaseActivity {

    @ViewInject(R.id.mPost_WebView)
    WebView mWebView;

    @ViewInject(R.id.toolbar_name)
    TextView mtoolbat_title;

    @ViewInject(R.id.toolbar)
    Toolbar mToolbar;

    private String mBianhao;
    private String mUrl;
    private String mtitle;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);

        /* 获取上一级传递的数据 */
        getParmentData();

        /* 赋值标题 */
        mtoolbat_title.setText(mtitle);

        /* 设置webView的基本属性 */
        setWebView();

        /* 设置toolbar的基本信息 */
        setToolbar();
    }

    //设置标题栏Toolbar
    private void setToolbar() {
        mToolbar.setTitle(mtitle);   //设置标题
        mToolbar.setSubtitle("提交信息查看");    //设置副标题
        mToolbar.setSubtitleTextColor(Color.RED);  //设置副标题字体颜色
        setSupportActionBar(mToolbar);   //必须使用
        //添加左边图标点击事件
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2018/5/12 返回上一级
            }
        });

        //添加menu项点击事件
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if(mtitle.equals("旺苍县公安局责令限期整改治安隐患通知书")){
                    switch (item.getItemId()) {
                        case R.id.toolbar_r_1: //返回首页
                            startActivity(new Intent(WebPostRemarkActivity.this,HomeActivity.class));
                            break;
                        case R.id.toolbar_r_2: // 同意限期
                            Intent intent = new Intent(WebPostRemarkActivity.this, WebHtmlActivity.class);
                            Bundle bundle=new Bundle();
                            bundle.putString("l_url","yesServiceUp.html");
                            bundle.putString("l_title","同意延期整改治安隐患");
                            bundle.putString("mLocal_bianhao",mBianhao);
                            bundle.putString("id",id);
                            intent.putExtras(bundle);
                            startActivity(intent,
                                    ActivityOptions.makeSceneTransitionAnimation(WebPostRemarkActivity.this).toBundle());
                            break;
                        case R.id.toolbar_r_3: // 不同意限期
                            Intent intent1 = new Intent(WebPostRemarkActivity.this, WebHtmlActivity.class);
                            Bundle bundle1=new Bundle();
                            bundle1.putString("l_url","noSaverUp.html");
                            bundle1.putString("l_title","不同意延期整改治安隐患");
                            bundle1.putString("mLocal_bianhao",mBianhao);
                            bundle1.putString("id",id);
                            intent1.putExtras(bundle1);
                            startActivity(intent1,
                                    ActivityOptions.makeSceneTransitionAnimation(WebPostRemarkActivity.this).toBundle());
                            break;
                    }
                }else if(mtitle.equals("旺苍县公安局当场处罚决定书")) {
                    switch (item.getItemId()) {
                        case R.id.toolbar_r_1: //返回首页
                            startActivity(new Intent(WebPostRemarkActivity.this,HomeActivity.class));
                            break;
                        case R.id.toolbar_r_2: // 具有收缴的物品清单
                            Intent intent = new Intent(WebPostRemarkActivity.this, WebHtmlActivity.class);
                            Bundle bundle=new Bundle();
                            bundle.putString("l_url","yesServiceUp.html");
                            bundle.putString("l_title","收缴物品清单");
                            bundle.putString("mLocal_bianhao",mBianhao);
                            bundle.putString("id",id);
                            intent.putExtras(bundle);
                            startActivity(intent,
                                    ActivityOptions.makeSceneTransitionAnimation(WebPostRemarkActivity.this).toBundle());
                            break;
                        case R.id.toolbar_r_3:
                            Intent intent1 = new Intent(WebPostRemarkActivity.this, WebHtmlActivity.class);
                            Bundle bundle1=new Bundle();
                            bundle1.putString("l_url","jianchabilu.html");
                            bundle1.putString("l_title","检查笔录");
                            bundle1.putString("mLocal_bianhao",mBianhao);
                            bundle1.putString("id",id);
                            intent1.putExtras(bundle1);
                            startActivity(intent1,
                                    ActivityOptions.makeSceneTransitionAnimation(WebPostRemarkActivity.this).toBundle());
                            break;

                    }

                }else if(mtitle.equals("不同意延期整改治安隐患") || mtitle.equals("同意延期整改治安隐患") || mtitle.equals("旺苍县公安局责令改正通知书")){
                    switch (item.getItemId()) {
                        case R.id.toolbar_r_1: //返回首页
                            startActivity(new Intent(WebPostRemarkActivity.this,HomeActivity.class));
                            break;
                        case R.id.toolbar_r_2: // 检查笔录
                            Intent intent = new Intent(WebPostRemarkActivity.this, WebHtmlActivity.class);
                            Bundle bundle=new Bundle();
                            bundle.putString("l_url","jianchabilu.html");
                            bundle.putString("l_title","检查笔录");
                            bundle.putString("mLocal_bianhao",mBianhao);
                            bundle.putString("id",id);
                            intent.putExtras(bundle);
                            startActivity(intent,
                                    ActivityOptions.makeSceneTransitionAnimation(WebPostRemarkActivity.this).toBundle());
                            break;


                    }
                }else {
                    switch (item.getItemId()) {
                        case R.id.toolbar_r_1: //返回首页
                            startActivity(new Intent(WebPostRemarkActivity.this,HomeActivity.class));
                            break;
                    }
                }

                return true;    //返回为true
            }
        });
    }

    //设置menu（右边图标）
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(mtitle.equals("旺苍县公安局责令限期整改治安隐患通知书")){
            getMenuInflater().inflate(R.menu.toolbartongyi, menu); //解析menu布局文件到menu 限期整改
        }else if(mtitle.equals("旺苍县公安局当场处罚决定书")){
            getMenuInflater().inflate(R.menu.toolbarshoujiao, menu); //解析menu布局文件到menu 物品收缴
        }else if(mtitle.equals("不同意延期整改治安隐患") || mtitle.equals("同意延期整改治安隐患")){
            getMenuInflater().inflate(R.menu.toolbarjianchabilu, menu); //解析menu布局文件到menu 检查笔录
        }else {
            getMenuInflater().inflate(R.menu.toolbarnoall, menu); //解析menu布局文件到menu 什么都没有
        }

        return true;
    }



    /* 获取参数（上一级页面传递过来的必要参数） */
    public void getParmentData() {
        //页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        mBianhao = bundle.getString("bianhao");
        mUrl = bundle.getString("mUrl");
        mtitle = bundle.getString("mtitle");
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

        Log.e("TAG",new Date() + " WebPostReamrkActivity 预备显示的地址"+mUrl+"&bianhao="+mBianhao);

        mWebView.loadUrl(mUrl+"?bianhao="+mBianhao);
    }


}
