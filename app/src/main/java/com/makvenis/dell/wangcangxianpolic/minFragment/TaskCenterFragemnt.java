package com.makvenis.dell.wangcangxianpolic.minFragment;

import android.content.Context;
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
        map1.put("mTop","new");
        List<Integer> mList=new ArrayList<>();
        mList.add(R.drawable.icon_min_task_hostory);
        data.add(0,map1);
        data.add(1,mList);


        /* 构建数据 我的设置 */
        List<Object> data3=new ArrayList<>();
        Map<String,String> map3=new HashMap<>();
        map3.put("mTitle","我的设置");
        map3.put("mType","NEW_SET");
        map3.put("mTop","set");
        List<Integer> mList_3=new ArrayList<>();
        mList_3.add(R.drawable.icon_min_task_set);
        data3.add(0,map3);
        data3.add(1,mList_3);


        /* *//* 构建数据 我的任务 *//*
        List<Object> data2=new ArrayList<>();
        Map<String,String> map2=new HashMap<>();
        map2.put("mTitle","我的任务");
        map2.put("mType","NEW_ACTIVITY");
        map2.put("mTop","");
        List<Integer> mList_1=new ArrayList<>();
        mList_1.add(R.drawable.icon_min_task_wancheng);
        data2.add(0,map2);
        data2.add(1,mList_1);*/

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

        mDtabase.add(data);
        mDtabase.add(data3);
        //mDtabase.add(data2);
        mDtabase.add(data4);

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
        public void onBindViewHolder(MyTaskViewHolder holder, int position) {
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
