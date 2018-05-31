package com.makvenis.dell.wangcangxianpolic.details;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.makvenis.dell.wangcangxianpolic.R;

import java.util.ArrayList;
import java.util.List;

/* 单位的更多详情信息 */
/* 包括查看单位信息、修改单位信息、增加单位信息 */
/* 布局采用 全局注解 */

@ContentView(R.layout.activity_more_details)
public class MoreDetailsActivity extends AppCompatActivity {



    /* 单位照片 *//*
    @ViewInject(R.id.mMore_photo)
    ImageView mMore_photo;

    *//* 单位名称 *//*
    @ViewInject(R.id.mMore_Name)
    EditText mMore_Name;

    *//* 单位地址 *//*
    @ViewInject(R.id.mMore_Addrs)
    EditText mMore_Addrs;

    *//* 法人姓名 *//*
    @ViewInject(R.id.mMore_LevName)
    EditText mMore_LevName;

    *//* 管辖单位 *//*
    @ViewInject(R.id.mMore_GovName)
    EditText mMore_GovName;

    *//* 单位类型 *//*
    @ViewInject(R.id.mMore_AddrsType)
    EditText mMore_AddrsType;

    *//* 单位电话 *//*
    @ViewInject(R.id.mMore_Phone)
    EditText mMore_Phone;

    *//* 法人性别 *//*
    @ViewInject(R.id.mMore_Sex)
    EditText mMore_Sex;

    *//* 添加单位照片 *//*
    @ViewInject(R.id.mMore_Addrs_photo)
    ImageView mMore_Addrs_photo;*/


    @ViewInject(R.id.mTabLayout)
    TabLayout mTabLayout;

    @ViewInject(R.id.mViewPage)
    ViewPager mViewPage;

    /* 单位id */
    private String id;

    /* 上下文 */
    public final Context mContext=MoreDetailsActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);

        creadFragmentData();
        /* 获取单位ID */
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        /* 存储id */
        insertXmlId(id);

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //Toast.makeText(mContext, "选中的"+tab.getText(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //Toast.makeText(mContext, "未选中的"+tab.getText(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //Toast.makeText(mContext, "复选的"+tab.getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void insertXmlId(String num) {
        SharedPreferences spf = getSharedPreferences("xmlId",MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putString("id",id);
        edit.apply();
    }


    public void creadFragmentData(){

        List<Fragment> dataFragment = new ArrayList<>();

        ToViewFragment toViewFragment = new ToViewFragment();
        Bundle bundle=new Bundle();
        bundle.putString("id",id);
        toViewFragment.setArguments(bundle);

        dataFragment.add(toViewFragment);
        dataFragment.add(new UpdateViewFragment());
        dataFragment.add(new AddViewFragment());
        mTabLayout.setupWithViewPager(mViewPage);
        mViewPage.setAdapter(new SimpleDetailsViewPage(getSupportFragmentManager(),dataFragment));

    }

}
