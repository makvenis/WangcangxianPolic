package com.makvenis.dell.wangcangxianpolic.startActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_page_link,null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        List<Fragment> dataFragment = new ArrayList<>();
        dataFragment.add(new PersonalCenterFragemnt());
        dataFragment.add(new TaskCenterFragemnt());
        mTabLayout.setupWithViewPager(mViewPage);
        mViewPage.setAdapter(new SimpleViewPage(getChildFragmentManager(),dataFragment));

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewUtils.inject(this,view);
    }
}
