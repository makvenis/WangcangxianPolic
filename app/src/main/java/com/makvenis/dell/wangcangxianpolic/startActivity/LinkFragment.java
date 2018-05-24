package com.makvenis.dell.wangcangxianpolic.startActivity;

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

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.makvenis.dell.wangcangxianpolic.R;
import com.makvenis.dell.wangcangxianpolic.adapter.SimpleViewPage;
import com.makvenis.dell.wangcangxianpolic.minFragment.PersonalCenterFragemnt;
import com.makvenis.dell.wangcangxianpolic.minFragment.TaskCenterFragemnt;

import java.util.ArrayList;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_page_link,null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
}
