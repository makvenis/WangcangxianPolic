package com.makvenis.dell.wangcangxianpolic.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.makvenis.dell.wangcangxianpolic.R;
import com.makvenis.dell.wangcangxianpolic.tools.Configfile;
import com.makvenis.dell.wangcangxianpolic.tools.NetworkTools;
import com.makvenis.dell.wangcangxianpolic.view.SimpleLoadingDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    int page=0;
    /* 全局Dialog */
    SimpleLoadingDialog dialog;
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
                        List<Map<String, String>> maps = TestData(obj);
                        for (int i = 0; i < maps.size(); i++) {
                            mData.add(maps.get(i));
                        }
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                        break;
                }
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);

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
            public void showItem(RecyclerView recyclerView, View view, int position) {
                Configfile.Log(AlertPushNewsActivity.this,String.valueOf(position));
            }
        });

    }


    //初始化一开始加载的数据
    private void initData(){
        /*mData = new ArrayList<>();
        for (int i = 0; i < 20; i++){
            mData.add("Item"+i);
        }*/
        List<Map<String, String>> maps = TestData(mObject);
        mData.addAll(maps);
    }

    //每次上拉加载的时候，就加载十条数据到RecyclerView中
    private void loadMoreData(){
        dialog=new SimpleLoadingDialog(this);
        dialog.setMessage("加载更多...").show();
        String mPath="http://sapi.beibei.com/item/mz_temai_cat/v2/1-1"+page+"-nvzhuang.html?package=mizhe";
        NetworkTools.HttpUtilsGet(this,mPath,mHandler);
        page++;
    }

    //下拉刷新
    private void updateData(){
        //我在List最前面加入一条数据
        Map<String,String> mMaps=new HashMap<>();
        mMaps.put("title","我是“下拉刷新”生出来的");
        mMaps.put("img","https://ps.ssl.qhimg.com/sdmt/87_135_100/t01c26515f619902f48.jpg");
        mData.add(0, mMaps);
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


    public List<Map<String,String>> TestData(String s){
        try {
            List<Map<String,String>> data=new ArrayList<>();
            JSONObject object=new JSONObject(s);
            JSONArray jsonArray = object.getJSONArray("home_items");
            for (int i = 0; i < jsonArray.length(); i++) {

                Map<String,String> map=new HashMap<>();

                JSONObject obj=jsonArray.getJSONObject(i);
                String title = obj.optString("title");
                String img = obj.optString("img");
                map.put("title",title);
                map.put("img",img);
                data.add(map);
            }

            return data;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
