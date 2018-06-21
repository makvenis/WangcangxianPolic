package com.makvenis.dell.wangcangxianpolic.details;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.makvenis.dell.wangcangxianpolic.R;
import com.makvenis.dell.wangcangxianpolic.tools.Configfile;

import java.io.File;
import java.io.FileOutputStream;

@ContentView(R.layout.activity_web_history)
public class WebHistoryActivity extends AppCompatActivity {

    @ViewInject(R.id.mWebViewHistory)
    WebView mWebView;
    private String mType;
    private String id;
    private String companyName;
    private String table;

    /* 处理toolbar 开始 version=2  */
    /* include 里面的点击事件 */
    @ViewInject(R.id.toolbar_callbank)
    ImageView mImageView_bank;
    @ViewInject(R.id.toolbar_callbank_text)
    TextView mBankTextView;
    @ViewInject(R.id.mToolbar_text)
    TextView mTextView;
    /* 处理toolbar 结束 */

    String[] mPermissions = new String[]{Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS};

    String url;
    private String bianhao;
    private String mUrl; //再上层适配器中已经做了全地址拼接

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_web_history);
        ViewUtils.inject(this);

        //权限检查
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
            int i = ActivityCompat.checkSelfPermission(this, mPermissions[0]);
            if(i != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,mPermissions,1);
            }else {
                Configfile.Log(this,"您之前拒绝了此权限，请到设置--权限开通系列权限");
            }
        }

        /* 获取父类参数 */
        getParment();

        mTextView.setText(table);

        // TODO: 2018/6/5  加载地址
        /* 获取当前名称表 */
        getTypeTable(mType);
        /* 判断当前的用户表 */
        url = getTypeUrl(mType);
        Log.e("DATA","WebHistoryActivity >>> "+url+" >>> "+mType);

        /* 设置网页基本参数 */
        setWebView();

        mWebView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Configfile.Log(WebHistoryActivity.this,"当前为长按保存图片和选择打印机");
                printPDF();
                return true;
            }
        });

    }


    /* 返回 */
    @OnClick({R.id.toolbar_callbank})
    public void oncklinkViewImage(View v){
        /* 返回首页需要单位ID参数 */
        /* 获取固定xmlId 存储的单位id值 */
        SharedPreferences xmlId = getSharedPreferences("xmlId", Context.MODE_PRIVATE);
        String xmlIdString = xmlId.getString("id", "0");
        Log.e("DATA","haredPreferences()" + xmlIdString);
        Intent intent = new Intent(this, SelectDetailsActivity.class);
        intent.putExtra("bank_id",3);
        intent.putExtra("id",xmlIdString);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    /* 返回 */
    @OnClick({R.id.toolbar_callbank_text})
    public void oncklinkViewTextView(View v){
        Intent intent = new Intent(this, SelectDetailsActivity.class);
        intent.putExtra("bank_id",3);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }


    private void setWebView() {

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

        // TODO: 2018/6/5  加载地址
        Log.e("DATA","WebHistoryActivity >>>适配器传递的地址 "+mUrl+" >>> 类型 "+mType);
        mWebView.loadUrl(mUrl);
    }

    public String getTypeTable(String type) {

        if(type != null){

            int i = Integer.valueOf(type).intValue();
            switch (i){

                case 0:
                    return "";
                case 1:
                    return "旅店安全检查表";
                case 2:
                    return "金融机构检查登记表";
                case 3:
                    return "寄递物流业安全检查登记表";
                case 4:
                    return "校园治安保卫工作检查登记表";
                case 5:
                    return "民爆物品安全检查情况登记表";
                case 6:
                    return "加油站";
            }
        }
        return null;
    }

    public String getTypeUrl(String url) {
        if(url != null){

            int i = Integer.valueOf(url).intValue();

            switch (i){

                case 0:
                    return "";
                case 1:
                    return Configfile.RESULT_HTML_TYPE_1;
                case 2:
                    return Configfile.RESULT_HTML_TYPE_2;
                case 3:
                    return Configfile.RESULT_HTML_TYPE_3;
                case 4:
                    return Configfile.RESULT_HTML_TYPE_4;
                case 5:
                    return Configfile.RESULT_HTML_TYPE_5;
            }
        }
        return null;
    }

    public void getParment() {
        Bundle bundle = getIntent().getExtras();
        mType = bundle.getString("type");
        id = bundle.getString("id");
        companyName = bundle.getString("companyName");
        table = bundle.getString("table");
        bianhao = bundle.getString("bianhao");
        mUrl = bundle.getString("url");

    }

    /* 屏幕长按事件 */
    public void printPDF() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // Get a PrintManager instance
            PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);

            // Get a print adapter instance
            PrintDocumentAdapter printAdapter = mWebView.createPrintDocumentAdapter();

            // Create a print job with name and adapter instance
            String jobName = getString(R.string.app_name) + " Document";
            printManager.print(jobName, printAdapter,
                    new PrintAttributes.Builder().build());
            saveImage();
        } else {
            Toast.makeText(getApplicationContext(), "当前系统不支持该功能", Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * 保存图片
     */
    public void saveImage() {
        Picture picture = mWebView.capturePicture();
        Bitmap b = Bitmap.createBitmap(
                picture.getWidth(), picture.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        picture.draw(c);
        File file = new File("/sdcard/" + "page.jpg");
        if(file.exists()){
            file.delete();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file.getAbsoluteFile());
            if (fos != null) {
                b.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                fos.close();
                Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "保存失败", Toast.LENGTH_SHORT).show();
        }
    }
}
