package sc.gys.wcx.and.hotelcheck;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

import sc.gys.wcx.and.startActivity.BaseActivity;
import sc.gys.wcx.and.R;
import sc.gys.wcx.and.tools.Configfile;


@ContentView(R.layout.activity_notes)
public class NotesActivity extends BaseActivity {

    @ViewInject(R.id.toolbar)
    private Toolbar toolbar;

    @ViewInject(R.id.toolbar_name)
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);

        //设置标题栏Toolbar
        setToolbar();
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

        //添加menu项点击事件
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.toolbar_r_1:
                        Configfile.Log(NotesActivity.this,"上传数据中....");

                }
                return true;
            }
        });
    }
    //设置menu（右边图标）
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_notes, menu); //解析menu布局文件到menu
        return true;
    }

}
