package sc.gys.wcx.and.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import sc.gys.wcx.and.help.JSON;
import sc.gys.wcx.and.startActivity.HomeActivity;
import sc.gys.wcx.and.startActivity.NotiflyActivity;
import sc.gys.wcx.and.tools.Configfile;
import sc.gys.wcx.and.tools.NetworkTools;
import sc.gys.wcx.and.R;


@ContentView(R.layout.activity_alert_push)
public class AlertPushNewsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    @ViewInject(R.id.mAlertPush_recycle)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.mAlertPush_SwipeRefreshLayout)
    private SwipeRefreshLayout mSwipeRefresh;

    /* 存放数据 */
    private List<Map<String,String>> mData=new ArrayList<>();
    /* 布局适配器 */
    private SimpleAlertPushAdapter adapter;
    /* 翻页 */
    int page=2;
    /* 全局Dialog */
    //SimpleLoadingDialog dialog;
    /* 回调的数据 */
    private String mObject;

    /* 全局Handler */
    public Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            String obj = (String) msg.obj;
            if(obj != null){
                switch (what){
                    case 0X000006:
                        List<Map<String, String>> maps = getParment(obj);
                        for (int i = 0; i < maps.size(); i++) {
                            mData.add(maps.get(i));
                        }
                        adapter.notifyDataSetChanged();
                        //dialog.dismiss();
                        break;
                }
            }

        }
    };


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

        /* 赋值ToolbarTitle */
        mTextView.setText("法律法规");


        Bundle bundle = getIntent().getExtras();
        mObject = bundle.getString("mAdapter");

        /* 这个是下拉刷新出现的那个圈圈要显示的颜色 */
        mSwipeRefresh.setColorSchemeResources(
                R.color.colorPrimaryDark
        );

        /* 绑定刷新事件 */
        mSwipeRefresh.setOnRefreshListener(this);

        /* 初始化数据 */
        initData();

        /* 设置布局 */
        /*RecyclerView.LayoutManager manager=new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,false);*/
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        /* 设置适配器 */
        adapter=new SimpleAlertPushAdapter(mData,this);
        mRecyclerView.setAdapter(adapter);

        /* 设置RecycleView的滑动监听 */
        mRecyclerView.addOnScrollListener(new SimpleLayoutManagerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                loadMoreData();
                Snackbar.make(mRecyclerView,"正在加载中...",Snackbar.LENGTH_SHORT)
                        .setAction("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mSwipeRefresh.setRefreshing(false);
                            }
                        })
                        .show();
            }
        });

        /* Item回调事件 */
        adapter.setOnclinkItemView(new SimpleAlertPushAdapter.OnclinkItemView() {
            @Override
            public void showItem(RecyclerView recyclerView, View view, String position) {
                Intent intent = new Intent(AlertPushNewsActivity.this, NotiflyActivity.class);

                intent.putExtra("url",Configfile.NEWS_ALL_CONTENT_PATH);
                intent.putExtra("id",position);
                intent.putExtra("type","AlertPushNewsActivity"); //说明有哪一个类跳转公共类
                startActivity(intent);
            }
        });

    }

    /* 返回 */
    @OnClick({R.id.toolbar_callbank})
    public void oncklinkViewImage(View v){
        startActivity(new Intent(this, HomeActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    /* 返回 */
    @OnClick({R.id.toolbar_callbank_text})
    public void oncklinkViewTextView(View v){
        startActivity(new Intent(this, HomeActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }


    //初始化一开始加载的数据
    private void initData(){
        List<Map<String, String>> maps = getParment(mObject);
        mData.addAll(maps);
        /* 获取总的页码标签 */
    }

    //每次上拉加载的时候，就加载十条数据到RecyclerView中
    private void loadMoreData(){
        //dialog=new SimpleLoadingDialog(this);
        //dialog.setMessage("加载更多...").show();
        String mPath=Configfile.NEWS_PATH+page;
        NetworkTools.HttpUtilsGet(this,mPath,mHandler);
        page++;
    }

    //下拉刷新
    private void updateData(){
        mData.clear();
        String mPath=Configfile.NEWS_PATH+1;
        NetworkTools.HttpUtilsGet(this,mPath,mHandler);
    }

    @Override
    public void onRefresh() {
        updateData();
        //数据重新加载完成后，提示数据发生改变，并且设置现在不在刷新
        adapter.notifyDataSetChanged();
        mSwipeRefresh.setRefreshing(false);
        Snackbar.make(mRecyclerView,"刷新成功",Snackbar.LENGTH_SHORT)
                .setAction("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSwipeRefresh.setRefreshing(false);
                    }
                })
                .show();
    }

    /**
     "addtime":1521022383000,
     "cid":114,
     "cmsclass":Object{...},
     "hits":13,
     "id":319,
     "ismsg":1,
     "laiyuan":"本网",
     "msgname":"jk",
     "picdefault":"../../upload/20180313162658256.jpg",
     "pxtime":1521022383000,
     "remark":"水下婚纱照也是现代很多新人喜欢的一种婚纱照风格",
     "state":0,
     "title":"水下婚纱照如何拍摄更自然唯美",
     "titlefu":"水下婚纱照如何拍摄更自然唯美",
     "username":"ssdai"
     */

    /**
     * @解析当前的数据
     *
     */
    public List<Map<String,String>> getParment(String s){
        try {
            JSONObject object=new JSONObject(s);
            JSONArray jsonArray = object.getJSONArray("newsList");
            List<Map<String, String>> data = JSON.GetJson(jsonArray.toString(), new String[]{"addtime", "cid", "id", "laiyuan", "msgname", "picdefault", "pxtime", "remark", "title", "titlefu", "username"});
            return data;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
