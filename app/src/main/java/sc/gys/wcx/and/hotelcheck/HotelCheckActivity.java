package sc.gys.wcx.and.hotelcheck;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

import sc.gys.wcx.and.startActivity.BaseActivity;
import sc.gys.wcx.and.view.SimpleLoadingDialog;
import sc.gys.wcx.and.R;
import sc.gys.wcx.and.tools.Configfile;

import java.util.ArrayList;
import java.util.List;


@ContentView(R.layout.activity_hotel_check)
public class HotelCheckActivity extends BaseActivity {

    @ViewInject(R.id.hotelcheck_recycle)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.toolbar)
    private Toolbar toolbar;

    @ViewInject(R.id.toolbar_name)
    private TextView mTextView;

    private SimpleLoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);

        //设置标题栏Toolbar
        setToolbar();
        //获取数据
        GetHotelCheckData();
        //设置适配器
        SetAdapter();
        //设置本页LOG
        mTextView.setText("旅店业安全情况检查登记表");
        //加载Dialog
        dialog=new SimpleLoadingDialog(HotelCheckActivity.this);

    }

    //设置标题栏Toolbar
    private void setToolbar() {
        toolbar.setTitle("旅馆安全检查");   //设置标题
        toolbar.setSubtitle("检查");    //设置副标题
        toolbar.setSubtitleTextColor(Color.RED);  //设置副标题字体颜色
        setSupportActionBar(toolbar);   //必须使用
        //添加左边图标点击事件
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // TODO: 2018/4/7   menu项点击事件

        //添加menu项点击事件
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.toolbar_r_1:
                        Configfile.Log(HotelCheckActivity.this,"上传数据中....");
                        dialog.setMessage("上传数据中...").show();
                        // TODO: 2018/4/8  上传数据
                        break;
                    case R.id.toolbar_r_2:
                        Configfile.Log(HotelCheckActivity.this,"进入笔录");
                        break;
                    case R.id.toolbar_r_3:
                        Configfile.Log(HotelCheckActivity.this,"正在进入整改");
                        break;

                }
                return true;    //返回为true
            }
        });
    }

    //设置menu（右边图标）
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu); //解析menu布局文件到menu
        return true;
    }



    private void SetAdapter() {
        List<HotelChectEntry> hotelChectEntries = GetHotelCheckData();
        /* 设置布局界面 */
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,
                false);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(new HotelCheckAdapter(hotelChectEntries,this));
    }

    private List<HotelChectEntry> GetHotelCheckData() {

        List<HotelChectEntry> mData = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            HotelChectEntry entry = new HotelChectEntry();
            entry.setmTitle("这是标题"+i);
            mData.add(entry);
        }

        return mData;

    }
}
