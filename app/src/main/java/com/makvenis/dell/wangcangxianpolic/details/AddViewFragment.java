package com.makvenis.dell.wangcangxianpolic.details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.makvenis.dell.wangcangxianpolic.R;
import com.makvenis.dell.wangcangxianpolic.help.JSON;
import com.makvenis.dell.wangcangxianpolic.newdbhelp.AppMothedHelper;
import com.makvenis.dell.wangcangxianpolic.tools.Configfile;
import com.makvenis.dell.wangcangxianpolic.tools.NetworkTools;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/* 检查历史（增加） */

public class AddViewFragment extends Fragment {

    @ViewInject(R.id.mDetailsHistory)
    RecyclerView mDetailRecycle;

    @ViewInject(R.id.mSwipe)
    PullRefreshLayout mSwipe;

    /* 全局集合 */
    List<Map<String,String>> data=new ArrayList<>();
    private HistoryAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_more_add, null);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /* 获取数据 */
        createDataHistory();

        RecyclerView.LayoutManager manager=new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL,
                false);
        mDetailRecycle.setLayoutManager(manager);
        /* 适配器 */
        adapter = new HistoryAdapter(data,getActivity());
        mDetailRecycle.setAdapter(adapter);

        /* 刷新数据 */
        mSwipe.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipe.setRefreshing(true);
                createDataHistory();
            }
        });

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewUtils.inject(this,view);
    }

    public Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            switch (what){
                case 1:
                    String obj = (String) msg.obj;
                    if(obj != null){

                        if(data.size() > 0){
                            data.clear();
                        }
                        // TODO: 2018/6/5 设置适配器 参数 编号（bianhao）
                        List<Map<String, String>> maps = JSON.GetJson(obj,
                                new String[]{"id", "jctime", "remark", "type", "unitid", "username","bianhao"});
                        for (int i = 0; i < maps.size(); i++) {
                            Map<String, String> p = maps.get(i);
                            data.add(p);
                        }
                        mSwipe.setRefreshing(false);
                        adapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };

    /* 根据Type获取当前的检查类型 */
    public String getTypeTable(String type) {

        if(type != null){

            int i = Integer.valueOf(type).intValue();
            switch (i){

                case 0:
                    return "";
                case 1:
                    return "旅店安全检查表";
                case 2:
                    return "金融机构检查登记表";
                case 3:
                    return "寄递物流业安全检查登记表";
                case 4:
                    return "校园治安保卫工作检查登记表";
                case 5:
                    return "民爆物品安全检查情况登记表";
                case 6:
                    return "加油站";
            }
        }
        return null;
    }

    /* 根据当前type 获取路径 */
    public String getTypeUrl(String url) {
        if(url != null){

            int i = Integer.valueOf(url).intValue();
            switch (i){

                case 0:
                    return "";
                case 1:
                    return Configfile.RESULT_HTML_TYPE_1;
                case 2:
                    return Configfile.RESULT_HTML_TYPE_2;
                case 3:
                    return Configfile.RESULT_HTML_TYPE_3;
                case 4:
                    return Configfile.RESULT_HTML_TYPE_4;
                case 5:
                    return Configfile.RESULT_HTML_TYPE_5;
                case 6:
                    return Configfile.RESULT_HTML_TYPE_7;

            }


        }

        return null;
    }




    /* 获取用户信息列表 */
    public String getSqliteName(){
        /* 数据库操作 获取当前用户名称 */
        AppMothedHelper helper=new AppMothedHelper(getActivity());
        Map<Object, Object> map = helper.queryByKey(Configfile.USER_DATA_KEY);
        String data = (String) map.get("data");
        Map<String, String> map1 = JSON.GetJsonRegiste(data);
        String s = map1.get("username");
        Log.e("TAG",new Date() + " >>> 检查历史---当前用户名称 "+s);
        return s;
    }


    /* 获取数据 */
    public void createDataHistory(){
        String path = Configfile.HISTORY_PATH;
        /* 数据库查询当前登陆用户名称 */
        String name = getSqliteName();
        final String url=path+name;
        Log.e("DATA","当前用户去获取检查历史请求地址为 >>>"+url);
        new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] bytes = NetworkTools.NetShow(url);
                String mResult = new String(bytes);
                if(mResult != null){
                    Message msg=new Message();
                    msg.what=1;
                    msg.obj=mResult;
                    mHandler.sendMessage(msg);
                }else {
                    Message msg=new Message();
                    msg.what=1;
                    msg.obj=null;
                    mHandler.sendMessage(msg);
                }
            }
        }).start();
    }

    /* 为了方便管理 此页面的适配器就写在此处 */
    public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.SimpleViewHolder>{

        List<Map<String,String>> mData;
        Context mContext;

        public HistoryAdapter(List<Map<String, String>> mData, Context mContext) {
            this.mData = mData;
            this.mContext = mContext;
        }

        @Override
        public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(mContext).inflate(R.layout.layout_history_net_item, parent, false);
            return new SimpleViewHolder(view);
        }

        @Override
        public void onBindViewHolder(SimpleViewHolder holder, int position) {
            if(holder instanceof SimpleViewHolder){
                final Map<String, String> map = mData.get(position);
                //"id", "jctime", "remark", "type", "unitid", "username"
                //赋值
                holder.name.setText(map.get("username"));

                final String type = map.get("type");
                final String table = getTypeTable(type);
                holder.company.setText(table);
                holder.id.setText(position+"");
                holder.title.setText(map.get("remark"));
                final String bianhao = map.get("bianhao");

                /* 获取价差时间 */
                String jctime = map.get("jctime");
                /* 获取被检查单位ID */
                final String unitid = map.get("unitid");

                Log.e("DATA","jctime:"+jctime+" >>> unitid"+unitid);

                String typeUrl = getTypeUrl(type);
                Log.e("DATA","当前类型 >>> "+ type);
                final String url=typeUrl+"&bianhao="+bianhao;
                Log.e("DATA","完整地址 >>> "+ url);
                /* 点击事件 */
                View view = ((SimpleViewHolder) holder).itemView;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getActivity(),WebHistoryActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putString("bianhao",bianhao); //当前编号
                        bundle.putString("type",type);
                        bundle.putString("id",unitid);      //单位ID
                        bundle.putString("companyName",map.get("remark"));//单位名称
                        bundle.putString("table",table);   //当前检查的表格（旅店安全检查表、校园、民爆...）
                        bundle.putString("url",url);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class SimpleViewHolder extends RecyclerView.ViewHolder{
            @ViewInject(R.id.mHistory_id)      //序号 mHistory_looding
            TextView id;
            @ViewInject(R.id.mHistory_title)   //单位名称
            TextView title;
            @ViewInject(R.id.mHistory_name)    //检查人姓名
            TextView name;
            @ViewInject(R.id.mHistory_company) //检查的内容（旅店安全检查...）
            TextView company;
            @ViewInject(R.id.mHistory_gone)    //是否是最新的内容选择隐藏还是显示
            ImageView gone;

            public SimpleViewHolder(View itemView) {
                super(itemView);
                ViewUtils.inject(this,itemView);
            }
        }

    }


}
