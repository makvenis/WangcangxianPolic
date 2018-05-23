package com.makvenis.dell.wangcangxianpolic.minFragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.makvenis.dell.wangcangxianpolic.R;
import com.makvenis.dell.wangcangxianpolic.login.RegisteActivity;
import com.makvenis.dell.wangcangxianpolic.newdbhelp.AppMothedHelper;
import com.makvenis.dell.wangcangxianpolic.otherActivity.CorrectHistoryActivity;
import com.makvenis.dell.wangcangxianpolic.otherActivity.SetActivity;
import com.makvenis.dell.wangcangxianpolic.tools.Configfile;
import com.makvenis.dell.wangcangxianpolic.tools.DownloadAppUpdateManager;
import com.makvenis.dell.wangcangxianpolic.view.SimpleDialogSureView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 个人中心 ---- 正在进行的任务
 */

public class TaskCenterFragemnt extends Fragment {

    @ViewInject(R.id.minTaskCenter)
    RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.min_fragment_task_center, null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        List<List<Object>> mDtabase=new ArrayList<>();


        /* 构建数据 我的检查历史 */
        List<Object> data=new ArrayList<>();
        Map<String,String> map1=new HashMap<>();
        map1.put("mTitle","我的检查历史");
        map1.put("mType","NEW_ACTIVITY");
        map1.put("mTop","+1");
        List<Integer> mList=new ArrayList<>();
        mList.add(R.drawable.icon_min_task_hostory);
        data.add(0,map1);
        data.add(1,mList);


        /* 构建数据 我的设置 */
        List<Object> data3=new ArrayList<>();
        Map<String,String> map3=new HashMap<>();
        map3.put("mTitle","我的设置");
        map3.put("mType","NEW_SET");
        //map3.put("mTop","set");
        map3.put("mTop","");
        List<Integer> mList_3=new ArrayList<>();
        mList_3.add(R.drawable.icon_min_task_set);
        data3.add(0,map3);
        data3.add(1,mList_3);

        /* 构建数据 我的退出 */
        List<Object> data4=new ArrayList<>();
        Map<String,String> map4=new HashMap<>();
        map4.put("mTitle","退出登陆");
        map4.put("mType","NEW_DIALOG");
        map4.put("mTop","");
        List<Integer> mList_4=new ArrayList<>();
        mList_4.add(R.drawable.icon_tuichu);
        data4.add(0,map4);
        data4.add(1,mList_4);

        /* 版本检查 */
        List<Object> data5=new ArrayList<>();
        Map<String,String> map_version=new HashMap<>();
        map_version.put("mTitle","版本检查");
        map_version.put("mType","NEW_VERSION");
        // TODO: 2018/5/23  获取服务器的最新版本 如果有最新版本责需要显示为 map_version.put("mTop","(当前有新版本待更新)");
        // TODO: 2018/5/23  否则则显示为 map_version.put("mTop","(当前为最新版本)");
        map_version.put("mTop","(当前有新版本待更新)");
        List<Integer> list_version=new ArrayList<>();
        list_version.add(R.drawable.icon_update_app_120);
        data5.add(0,map_version);
        data5.add(1,list_version);

        mDtabase.add(data);
        mDtabase.add(data3);
        mDtabase.add(data4);
        mDtabase.add(data5);

        RecyclerView.LayoutManager manager=new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(new MySimpleTaskCenterAdapter(mDtabase));


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewUtils.inject(this,view);
    }


    /* 为了方便管理 此页面的适配器就写在此处 */
    public class MySimpleTaskCenterAdapter extends RecyclerView.Adapter<MySimpleTaskCenterAdapter.MyTaskViewHolder>{
        private RecyclerView mRecyclerView;
        List<List<Object>> mdata_adapter;

        public MySimpleTaskCenterAdapter( List<List<Object>> data) {
            this.mdata_adapter = data;
        }

        @Override
        public MyTaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.min_task_itemview_adapter, parent, false);

            return new MyTaskViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyTaskViewHolder holder, final int position) {
            List<Object> obj = mdata_adapter.get(position);
            Log.e("TAG","position == 3 >>>"+mdata_adapter.get(2).toString());
            final Map<String, String> map = (Map<String, String>) obj.get(0);
            List<Integer> reasoul = (List<Integer>) obj.get(1);
            if(map.size() != 0  && reasoul.size() != 0){
                holder.mImageView.setImageResource(reasoul.get(0));
                holder.mTextView.setText(map.get("mTitle"));
                holder.mTextView_biaoshi.setText(map.get("mTop"));
            }

            View itemView = holder.itemView;
            final String mType = map.get("mType");

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mType.equals("NEW_DIALOG")){
                        final SimpleDialogSureView minDialog=new SimpleDialogSureView(getContext());
                        minDialog.setMessage("确定退出?");
                        minDialog.show();
                        minDialog.setOnclinkDialogSureListener(new SimpleDialogSureView.setOnclinkDialogSureListener() {
                            @Override
                            public Void OnClinkSureListener() { //点击确认
                                //删除用户数据
                                AppMothedHelper helper=new AppMothedHelper(getContext());
                                helper.delete(Configfile.USER_DATA_KEY);
                                getContext().startActivity(new Intent(getActivity(), RegisteActivity.class));
                                getActivity().finish();
                                return null;
                            }
                        });

                        minDialog.setOnclinkDialogCancelListener(new SimpleDialogSureView.setOnclinkDialogCancelListener() {
                            @Override
                            public Void OnClinkCancelListener() {
                                minDialog.dismiss();
                                return null;
                            }
                        });
                    }else if(mType.equals("NEW_ACTIVITY")){
                        /* 查询XML */
                        SharedPreferences pref = getActivity().getSharedPreferences("set", Context.MODE_PRIVATE);
                        //boolean dingwei = pref.getBoolean("dingwei", false);
                        boolean history = pref.getBoolean("localHistory", false);
                        if(history){
                            startActivity(new Intent(getActivity(),CorrectHistoryActivity.class));
                        }else {
                            final SimpleDialogSureView minDialog=new SimpleDialogSureView(getContext());
                            minDialog.setMessage("请在设置里面打开任务历史！");
                            minDialog.show();
                            minDialog.setOnclinkDialogSureListener(new SimpleDialogSureView.setOnclinkDialogSureListener() {
                                @Override
                                public Void OnClinkSureListener() { //点击确认
                                    getContext().startActivity(new Intent(getActivity(), SetActivity.class));
                                    return null;
                                }
                            });

                            minDialog.setOnclinkDialogCancelListener(new SimpleDialogSureView.setOnclinkDialogCancelListener() {
                                @Override
                                public Void OnClinkCancelListener() {
                                    minDialog.dismiss();
                                    return null;
                                }
                            });
                        }

                    }else if(mType.equals("NEW_SET")){

                        startActivity(new Intent(getActivity(),SetActivity.class));

                    }else if(mType.equals("NEW_VERSION")){
                        // TODO: 2018/5/23 模拟 此处地址只运用于测试 不可用于发行版
                        /* 此处地址只运用于测试 不可用于发行版 */
                        String mTestAppUpdatePath="http://192.168.0.106/im/version.1.6.1.apk";
                        Configfile.Log(getActivity(),"正在检查版本...");
                        final DownloadAppUpdateManager manager=new DownloadAppUpdateManager(getActivity(),mTestAppUpdatePath,"旺苍公安巡防");
                        int code = manager.getAppVersionCode();

                        Log.e("TAG",position+"");
                        // TODO: 2018/5/23  获取服务器的最新版本
                        final AlertDialog show = new AlertDialog.Builder(getContext())
                                .setTitle("版本更新")
                                .setIcon(R.drawable.icon_update_app_120)
                                .setMessage("最新版本V1.2.3."+code+" \n 1.修复适配器图片加载方法 \n 2.优化特效配置"
                                +" \n 3.修改推送服务"
                                )
                                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        /* 开始后台下载 */
                                        // TODO: 2018/5/23 当具有新的版本 去执行 manager.post();
                                        manager.post();
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .show();


                    }
                }
            });

        }


        /*他为哪一个recycler提供数据 当为一个recycle提供数据的时候就会调用这个方法*/
        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            this.mRecyclerView = recyclerView;
        }
        /*解绑*/
        @Override
        public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
            super.onDetachedFromRecyclerView(recyclerView);
            this.mRecyclerView = null;
        }

        @Override
        public int getItemCount() {
            return mdata_adapter.size();
        }

        public class MyTaskViewHolder extends RecyclerView.ViewHolder{

            @ViewInject(R.id.min_task_adapter_icon)
            ImageView mImageView; // 图片（ICON）
            @ViewInject(R.id.min_task_adapter_context)
            TextView mTextView; // 标题党
            @ViewInject(R.id.min_task_adapter_biaoshi)
            TextView mTextView_biaoshi; //标识 当有新的资源或则其他事情 则显示new标签

            public MyTaskViewHolder(View itemView) {
                super(itemView);
                /* Adapter注册 注解事件 */
                ViewUtils.inject(this,itemView);
            }
        }


    }
}
