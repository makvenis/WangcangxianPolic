package sc.gys.wcx.and.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import sc.gys.wcx.and.help.JSON;
import sc.gys.wcx.and.startActivity.HomeActivity;
import sc.gys.wcx.and.tools.Configfile;
import sc.gys.wcx.and.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




/* 公安警情的推送 */

@ContentView(R.layout.activity_alert_push_police)
public class AlertPushPoliceActivity extends AppCompatActivity {

    @ViewInject(R.id.mAlertPush_police_swipe)
    SwipeRefreshLayout mSwipe;
    @ViewInject(R.id.mAlertPush_police_recycle)
    RecyclerView mRecyclerView;
    @ViewInject(R.id.toolbar_callbank)
    ImageView mImageView;

    /* 集合装载 */
    List<Object> mData=new ArrayList<>();
    private SimpleAlertPushPoliceAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);

        /* 获取绑定事件以及获取数据 */
        setDataAdapter();


        /* 处理刷新事件 */
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mData.clear();
                setDataAdapter();
                mSwipe.setRefreshing(false);
            }
        });

    }

    @OnClick({R.id.toolbar_callbank})
    public void callBank(View view){
        startActivity(new Intent(this, HomeActivity.class));
    }

    /* 获取绑定事件以及获取数据 */
    private void setDataAdapter() {
        getNetData();
        LinearLayoutManager manager=new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,
                false);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new SimpleAlertPushPoliceAdapter(this,mData);
        mRecyclerView.setAdapter(mAdapter);
    }

    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                mAdapter.notifyDataSetChanged();
            }
        }
    };

    public void getNetData(){
        /**
         * "addtime":1521022345000,
         "cid":114,
         "cmsclass":Object{...},
         "content":"PHA+d3RmPyHlk4jlk4jlk4jlk4gyMzPvvIzmlrDlqJjvvIzlqZrnurHvvIzlq4HlpoY8L3A+",
         "hits":32,
         "id":318,
         "ismsg":1,
         "laiyuan":"本网",
         "msgname":"ssdai",
         "picdefault":"../../upload/20180313162641635.jpg",
         "pxtime":1521022345000,
         "remark":"随着现在社会的不断进步，人们生活意识和方式都发生了改变",
         "state":0,
         "title":"不爱笑的新娘怎么拍婚纱照",
         "titlefu":"",
         "username":"ssdai"
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                new HttpUtils(10000).send(HttpRequest.HttpMethod.GET,
                        "http://ssdaixiner.oicp.net:26168/wcjw/mobile/news/msglist?msgname=ssdai",
                        new RequestCallBack<String>() {
                            @Override
                            public void onSuccess(ResponseInfo<String> responseInfo) {
                                String result = responseInfo.result;

                                if(result != null){
                                    try {
                                        JSONObject object=new JSONObject(result);
                                        JSONArray array = object.getJSONArray("newsList");
                                        List<Map<String, String>> maps = JSON.GetJson(array.toString(),
                                                new String[]{"title","remark","cid","id","picdefault","laiyuan"});
                                        if(maps.size() > 0){
                                            List<Object> list = creatData(maps);
                                            mData.addAll(list);
                                            Message msg=new Message();
                                            msg.what=1;
                                            mHandler.sendMessage(msg);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(HttpException e, String s) {

                            }
                        });
            }
        }).start();


    }

    private List<Object> creatData(List<Map<String, String>> maps) {

        if(maps.size() > 0){ //至少有1条数据

            /* 取出第一条数据 */
            Map<String, String> map = maps.get(0);
            /* 创建结合装载第一组 */
            Map<String,String> mobj1=new HashMap<>();

            /* 解析图片地址 */
            String picdefault = map.get("picdefault").replace("../../", Configfile.SERVICE_WEB_IMG);
            String title = map.get("title"); //标题
            String hits = map.get("hits");   //点击数
            String cid = map.get("cid");   //cid
            String id = map.get("id");   //id
            mobj1.put("picdefault",picdefault);
            mobj1.put("title",title);
            mobj1.put("hits",hits);                 //点击数
            mobj1.put("cid",cid);                   //cid
            mobj1.put("id",id);                     //id

            mData.add(0,mobj1);

            // 2------------------------------------------------
            /* 获取网络数据 */
            Map<String, String> map1 = maps.get(0);

            /* 创建结合装载第二组 */
            Map<String,String> mobj2=new HashMap<>();

            /* 获取数据 */
            String remark = map.get("remark"); //标题
            String laiyuan = map.get("laiyuan");   //点击数

            /* 装载 */
            mobj2.put("remark",remark);
            mobj2.put("laiyuan",laiyuan);

            /* 添加主集合 */
            mData.add(1,mobj2);


            //-----------------------------------------------
            List<Map<String,String>> mobj3=new ArrayList<>();
            if(maps.size() > 1)
            for (int i = 2; i < maps.size(); i++) {
                /* 获取网络数据 */
                Map<String, String> mapAll = maps.get(i);

                /* 创建结合装载第二组 */
                Map<String,String> mobjSet=new HashMap<>();

                /* 获取数据 */
                String picdefaultAll = map.get("picdefault").replace("../../", Configfile.SERVICE_WEB_IMG);
                String titleAll = mapAll.get("title"); //标题
                String remarkAll = mapAll.get("remark");    //点击数
                String cidAll = mapAll.get("cid");          //cid
                String idAll = mapAll.get("id");            //id

                /* 装载 */
                mobjSet.put("picdefault",picdefaultAll );
                mobjSet.put("title",titleAll );
                mobjSet.put("remark", remarkAll);
                mobjSet.put("cid", cidAll);
                mobjSet.put("id", idAll);

                /* 添加装载第二组 */
                mobj3.add(mapAll);

            }
             /* 添加主集合 */
            mData.add(2,mobj3);
        }
        return mData;
    }
}
