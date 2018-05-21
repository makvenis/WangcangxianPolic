package com.makvenis.dell.wangcangxianpolic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.makvenis.dell.wangcangxianpolic.R;
import com.makvenis.dell.wangcangxianpolic.startActivity.HomeActivity;

import java.util.ArrayList;
import java.util.List;




/* 公安警情的推送 */

@ContentView(R.layout.activity_alert_push_police)
public class AlertPushPoliceActivity extends AppCompatActivity {

    @ViewInject(R.id.mAlertPush_police_swipe)
    SwipeRefreshLayout mSwipe;
    @ViewInject(R.id.mAlertPush_police_recycle)
    RecyclerView mRecyclerView;
    @ViewInject(R.id.toolbar_callbank)
    ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);

        /* 获取绑定事件以及获取数据 */
        setDataAdapter();
        /* 处理刷新事件 */
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setDataAdapter();
                mSwipe.setRefreshing(false);
            }
        });

    }

    @OnClick({R.id.toolbar_callbank})
    public void callBank(View view){
        startActivity(new Intent(this, HomeActivity.class));
    }

    /* 获取绑定事件以及获取数据 */
    private void setDataAdapter() {
        List<Object> data = creatData();
        LinearLayoutManager manager=new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,
                false);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(new SimpleAlertPushPoliceAdapter(this,data));

    }

    private List<Object> creatData() {

        List<Object> mData=new ArrayList<>();

        List<String> mobj1=new ArrayList<>();
        mobj1.add(0,"http://p2.so.qhmsg.com/bdr/_240_/t0141c909410dffe997.jpg");
        mobj1.add(1,"我是标题");
        mobj1.add(2,"3"); //图片的个数

        List<String> mobj2=new ArrayList<>();
        mobj2.add(0,"我是重要新闻....,我喜欢滚动的播放");
        mobj2.add(1,"我是重要新闻....,我喜欢滚动的播放");

        List<List<String>> mobj3=new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            List<String> minObj3=new ArrayList<>();
            minObj3.add(0,"我是历史通知---标题");
            minObj3.add(1,"我是历史通知---小标题，颜色也较为浅显，字体也比较小，而且字还比较多.....");
            minObj3.add(2,"http://b1.hucdn.com/upload/item/1804/16/59603443964511_800x800.jpg");
            mobj3.add(minObj3);
        }


        mData.add(0,mobj1);
        mData.add(1,mobj2);
        mData.add(2,mobj3);

        return mData;


    }
}
