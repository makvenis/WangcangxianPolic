package sc.gys.wcx.and.details;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import sc.gys.wcx.and.company.CompanyActivity;
import sc.gys.wcx.and.R;
import sc.gys.wcx.and.help.PermissionsUtils;

/* 单位信息第二版采用ReadioButton设计 */

@ContentView(R.layout.activity_select_details)
public class SelectDetailsActivity extends AppCompatActivity {


    @ViewInject(R.id.fl)
    FrameLayout fl;

    @ViewInject(R.id.rg)
    RadioGroup mRg;

    @ViewInject(R.id.Main_button_a)
    RadioButton mRadioButton_a;

    @ViewInject(R.id.Main_button_b)
    RadioButton mRadioButton_b;

    @ViewInject(R.id.Main_button_c)
    RadioButton mRadioButton_c;

    private FragmentManager manager;

    /* 处理toolbar 开始 version=2  */
    /* include 里面的点击事件 */
    @ViewInject(R.id.toolbar_callbank)
    ImageView mImageView_bank;
    @ViewInject(R.id.toolbar_callbank_text)
    TextView mBankTextView;
    @ViewInject(R.id.mToolbar_text)
    TextView mTextView;
    /* 处理toolbar 结束 */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);

        PermissionsUtils permissionsUtils=new PermissionsUtils();
        permissionsUtils.SetPermissionForNormal(this);

        mTextView.setText("单位信息及其更改");

        /* 默认加载第一个碎片 */
        manager=getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fl,new ToViewFragment());
        transaction.commit();
        /* 第一个碎片加载完毕 */

        /* 获取固定xmlId 存储的单位id值 */
        String id = getIntent().getStringExtra("id"); //单位ID
        insertXmlId(id);

        /* 处理button事件 */
        SetOnlinkRadioButton();

    }

    /* 存储单位ID  */
    private void insertXmlId(String id) {
        SharedPreferences spf = getSharedPreferences("xmlId",MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putString("id",id);
        edit.apply();
    }

    /* 处理Button事件机制 */
    private void SetOnlinkRadioButton() {
        //RadioButton点击
        mRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int i) {
                if(i==R.id.Main_button_a){
                    manager=getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.fl,new ToViewFragment());
                    transaction.commit();
                }else if(i==R.id.Main_button_b){
                    manager=getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.fl,new UpdateViewFragment());
                    transaction.commit();
                }else if(i==R.id.Main_button_c){
                    manager=getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.fl,new AddViewFragment());
                    transaction.commit();
                }
            }
        });


    }

    /* 返回 */
    @OnClick({R.id.toolbar_callbank})
    public void oncklinkViewImage(View v){
        Intent intent = new Intent(this, CompanyActivity.class);
        startActivityForResult(intent,200, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    /* 返回 */
    @OnClick({R.id.toolbar_callbank_text})
    public void oncklinkViewTextView(View v){
        Intent intent = new Intent(this, CompanyActivity.class);
        startActivityForResult(intent,200, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

}
