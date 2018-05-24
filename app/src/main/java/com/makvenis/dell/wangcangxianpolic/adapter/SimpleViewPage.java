package com.makvenis.dell.wangcangxianpolic.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by dell on 2018/4/22.
 */

public class SimpleViewPage extends FragmentPagerAdapter {

    private List<Fragment> titleList;

    public SimpleViewPage(FragmentManager fm,List<Fragment> titleList) {
        super(fm);
        this.titleList=titleList;
    }


    @Override
    public Fragment getItem(int position) {
        return titleList.get(position);
    }

    @Override
    public int getCount() {
        return titleList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){

            case 0:
                return "个人信息";
            case 1:
                return "我的设置";

        }

        return super.getPageTitle(position);

    }


}
