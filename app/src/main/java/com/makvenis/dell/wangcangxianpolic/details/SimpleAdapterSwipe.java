package com.makvenis.dell.wangcangxianpolic.details;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;

import java.util.List;

/**
 * Created by dell on 2018/6/6.
 */

public class SimpleAdapterSwipe extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    private FragmentManager fm;
    public SimpleAdapterSwipe(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fm = fm;
        this.fragments = fragments;
    }

    public void setFragments(List<Fragment> fragments) {
        if(this.fragments != null){
            FragmentTransaction ft = fm.beginTransaction();
            for(Fragment f:this.fragments){
                ft.remove(f);
            }
            ft.commit();
            ft=null;
            fm.executePendingTransactions();
        }
        this.fragments = fragments;
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int arg0) {
        return fragments.get(arg0);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }


    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){

            case 0:
                return "单位详情";
            case 1:
                return "修改信息";
            case 2:
                return "检查历史";

        }

        return super.getPageTitle(position);

    }
}
