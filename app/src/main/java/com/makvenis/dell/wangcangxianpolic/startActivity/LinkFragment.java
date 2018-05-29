package com.makvenis.dell.wangcangxianpolic.startActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.makvenis.dell.wangcangxianpolic.R;
import com.makvenis.dell.wangcangxianpolic.adapter.SimpleViewPage;
import com.makvenis.dell.wangcangxianpolic.minFragment.PersonalCenterFragemnt;
import com.makvenis.dell.wangcangxianpolic.minFragment.TaskCenterFragemnt;
import com.makvenis.dell.wangcangxianpolic.otherActivity.SetActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/* 作者  王从文 */
/* 全局采用注解模式 */
/* 旺苍县公安局巡防系统 首页界面 */

public class LinkFragment extends Fragment{

    @ViewInject(R.id.mViewPage)
    private ViewPager mViewPage;
    @ViewInject(R.id.mTabLayout)
    private TabLayout mTabLayout;

    /* 设置CollapsingToolbarLayout缩放时候字体颜色 */
    @ViewInject(R.id.CollapsingToolbarLayout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    /* 返回 */
    @ViewInject(R.id.mHomeLink_bank)
    ImageView mHomeBank;

    /* tool 快速进入设置页面 */
    @ViewInject(R.id.mHomeLink_set)
    ImageView mSetImage;

    /* 更新时间 */
    @ViewInject(R.id.mTime)
    TextView mTimer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_page_link,null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SimpleDateFormat fm = new SimpleDateFormat("MM-dd");
        String time = fm.format(new Date());
        mTimer.setText("最近更新:"+time);


        /**
         * 扩张时候的title颜色：
         * mCollapsingToolbarLayout.setExpandedTitleColor();
         * 收缩后在Toolbar上显示时的title的颜色：
         * mCollapsingToolbarLayout.setCollapsedTitleTextColor();
         */
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.BLUE);
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);

        List<Fragment> dataFragment = new ArrayList<>();
        dataFragment.add(new PersonalCenterFragemnt());
        dataFragment.add(new TaskCenterFragemnt());
        mTabLayout.setupWithViewPager(mViewPage);
        mViewPage.setAdapter(new SimpleViewPage(getChildFragmentManager(),dataFragment));

    }

    @OnClick({R.id.mHomeLink_bank})
    public void bank(View view){
        startActivity(new Intent(getActivity(),HomeActivity.class));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewUtils.inject(this,view);
    }

    /**
     * {@link #onResume 接收页面的回跳显示}
     */
    @Override
    public void onResume() {
        super.onResume();
        int id = getActivity().getIntent().getIntExtra("bank_id", 0);
        if (id == 2) {
            mViewPage.setCurrentItem(1);
        }else if(id==1){
            mViewPage.setCurrentItem(0);
        }
    }


    /* 快速进入设置页面 */
    @OnClick({R.id.mHomeLink_set})
    public void setOnclink(View v){
        Intent intent = new Intent(getActivity(), SetActivity.class);
        startActivity(intent,
                ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
    }
}
