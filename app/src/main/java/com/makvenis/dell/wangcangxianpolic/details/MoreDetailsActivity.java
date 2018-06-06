package com.makvenis.dell.wangcangxianpolic.details;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.makvenis.dell.wangcangxianpolic.R;
import com.makvenis.dell.wangcangxianpolic.company.CompanyActivity;
import com.makvenis.dell.wangcangxianpolic.help.PermissionsUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/* 单位的更多详情信息 */
/* 包括查看单位信息、修改单位信息、增加单位信息 */
/* 布局采用 全局注解 */

@ContentView(R.layout.activity_more_details)
public class MoreDetailsActivity extends AppCompatActivity {

    @ViewInject(R.id.mTabLayout)
    TabLayout mTabLayout;

    @ViewInject(R.id.mViewPage)
    ViewPager mViewPage;

    /* 单位id */
    private String id;

    /* 上下文 */
    public final Context mContext=MoreDetailsActivity.this;

    @ViewInject(R.id.mHomeLink_bank)
    ImageView mImageView;

    /* 时间更新 */
    @ViewInject(R.id.mTime)
    TextView mTimer;
    private SimpleAdapterSwipe mSimpleAdapterSwipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);

        PermissionsUtils permissionsUtils=new PermissionsUtils();
        permissionsUtils.SetPermissionForNormal(this);

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

        /* 显示当前更新的时间 */
        SimpleDateFormat fm = new SimpleDateFormat("MM-dd");
        String time = fm.format(new Date());
        mTimer.setText(" 最近更新:"+time);
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
        mSimpleAdapterSwipe = new SimpleAdapterSwipe(getSupportFragmentManager(),dataFragment);
        mViewPage.setAdapter(mSimpleAdapterSwipe);

    }

    @Override
    protected void onResume() {
        int id = getIntent().getIntExtra("bank_id", 0);
        if (id == 2) {//其他Activity跳转第二个碎片里面的ViewPage中第二个页面



            List<Fragment> dataFragment = new ArrayList<>();
            dataFragment.add(new ToViewFragment());
            dataFragment.add(new UpdateViewFragment());
            dataFragment.add(new AddViewFragment());
            mSimpleAdapterSwipe.setFragments(dataFragment);

/*            mViewPage.setCurrentItem(1);
            mTabLayout.getTabAt(1).select();*/
        }else if(id == 3) {
            creadFragmentData();
            mViewPage.setCurrentItem(2);
            mTabLayout.getTabAt(2).select();
        }
        super.onResume();
    }

    @OnClick({R.id.mHomeLink_bank})
    public void BankCompanyActivity(View v){
        Intent intent = new Intent(this, CompanyActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
