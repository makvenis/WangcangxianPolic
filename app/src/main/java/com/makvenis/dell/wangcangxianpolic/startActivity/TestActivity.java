package com.makvenis.dell.wangcangxianpolic.startActivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.makvenis.dell.wangcangxianpolic.R;
import com.makvenis.dell.wangcangxianpolic.tools.DownloadAppUpdateManager;

@ContentView(R.layout.activity_test)
public class TestActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
    }

    public void updateApk(View view) {
        //下载并且安装
        DownloadAppUpdateManager manager=new DownloadAppUpdateManager(this,"http://192.168.0.106/im/version.1.6.1.apk","更新App");
        manager.post();
    }
}
