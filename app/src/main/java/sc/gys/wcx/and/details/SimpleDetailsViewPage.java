package sc.gys.wcx.and.details;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;


public class SimpleDetailsViewPage extends FragmentPagerAdapter {

    private List<Fragment> titleList;

    public SimpleDetailsViewPage(FragmentManager fm, List<Fragment> titleList) {
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
                return "单位详情";
            case 1:
                return "修改信息";
            case 2:
                return "检查历史";

        }

        return super.getPageTitle(position);

    }

}
