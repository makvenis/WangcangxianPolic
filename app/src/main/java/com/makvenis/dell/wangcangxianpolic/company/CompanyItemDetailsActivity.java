package com.makvenis.dell.wangcangxianpolic.company;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.makvenis.dell.wangcangxianpolic.R;
import com.makvenis.dell.wangcangxianpolic.help.JSON;
import com.makvenis.dell.wangcangxianpolic.newsnotescheck.NotesNewActivity;
import com.makvenis.dell.wangcangxianpolic.startActivity.BaseActivity;
import com.makvenis.dell.wangcangxianpolic.tools.Configfile;
import com.makvenis.dell.wangcangxianpolic.tools.NetworkTools;
import com.makvenis.dell.wangcangxianpolic.view.SimpleLoadingDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* 作者  王从文 */
/* 全局采用注解模式 */
/* 旺苍县公安局巡防系统 预查单位详情界面 */

@ContentView(R.layout.activity_company_item_details)
public class CompanyItemDetailsActivity extends BaseActivity {

    @ViewInject(R.id.details_swipe)
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @ViewInject(R.id.details_listView)
    private ListView mListView;


    private String obj;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0X000003){
                obj = ((String) msg.obj);

                if(obj != null){
                    List<Map<String, String>> list = JSON.GetJson(obj, new String[]{"content", "id", "jcClass", "tradeId"});
                    Log.e("TAG",list.size()+"Handler添加到集合mList");
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);

        GetParmentActivityData();


        new Thread(new Runnable() {
            @Override
            public void run() {
                NetworkTools.OkHttpUtils(new String[]{"type","name"},new String[]{"1","1"}, Configfile.FORM_POST,mHandler);
            }
        }).start();


    }


    public void GetParmentActivityData(){
        //页面接收数据
        Bundle bundle = this.getIntent().getExtras();


        /* 处理适配 */
        List<Map<String,String>> mData = new ArrayList<>();
        Map<String,String> map = new HashMap<>();
        map.put("Item1",bundle.getString("Item1"));//地区;
        map.put("Item2",bundle.getString("Item2"));//警官姓名
        map.put("Item3",bundle.getString("Item3"));//警官警员证件号码
        map.put("Item4",bundle.getString("Item4"));//警官头像地址

        map.put("Item5",bundle.getString("Item5"));//单位名称
        map.put("Item6",bundle.getString("Item6"));//组织机构代码
        map.put("Item7",bundle.getString("Item7"));//法人姓名
        map.put("Item8",bundle.getString("Item8"));//法人电话
        map.put("Item9",bundle.getString("Item9"));//地址
        map.put("Item10",bundle.getString("Item10"));//公司简介
        mData.add(map);

        /* 处理适配器 */
        MySimpleAdapterOnlinkListent mAdapter=new MySimpleAdapterOnlinkListent(this,mData,R.layout.item_details_adapter_activity,
                new String[]{"Item1","Item2","Item3","Item4","Item5","Item6","Item7","Item8","Item9","Item10"},
                new int[]{R.id.details_xiaqu,
                        R.id.details_name,
                        R.id.details_num,
                        R.id.details_img,
                        R.id.details_dwmc,
                        R.id.details_zzjgdm,
                        R.id.details_faren,
                        R.id.details_tel,
                        R.id.details_path,
                        R.id.details_remark}){
            @Override
            public void setViewImage(ImageView v, String value) {
                if(value.indexOf("http://") == 0){
                    Picasso.with(CompanyItemDetailsActivity.this).load(value).into(v);
                }else {
                    super.setViewImage(v, value);
                }
            }
        };

        mListView.setAdapter(mAdapter);


    }

    //SimpleAdapter 子控件点击事件
    class MySimpleAdapterOnlinkListent extends SimpleAdapter{

        private Context mConentSimple;

        public MySimpleAdapterOnlinkListent(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
            this.mConentSimple = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = super.getView(position, convertView, parent);
            //CheckBox mCheckBox = (CheckBox) convertView.findViewById(R.id.favorites);
            Button mButton = (Button) convertView.findViewById(R.id.company_start_jiancha);
            mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SimpleLoadingDialog dialog=new SimpleLoadingDialog(CompanyItemDetailsActivity.this);
                    dialog.setMessage("正在请求数据中...").show();

                    if(obj != null){
                        Intent intent=new Intent(CompanyItemDetailsActivity.this,
                                NotesNewActivity.class);
                        Bundle bundle=new Bundle();
                        Log.e("TAG","需要传递的数据"+obj);
                        bundle.putString("data",obj);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        dialog.dismiss();
                    }

                    // TODO: 2018/4/7 传递参数包括 公司地址 公司法人的信息(3条)
                }
            });

            return convertView;
        }
    }
}
