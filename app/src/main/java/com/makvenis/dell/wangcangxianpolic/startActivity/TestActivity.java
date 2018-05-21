package com.makvenis.dell.wangcangxianpolic.startActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.makvenis.dell.wangcangxianpolic.R;

@ContentView(R.layout.activity_test)
public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
    }
}
