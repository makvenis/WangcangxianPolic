package com.makvenis.dell.wangcangxianpolic.minFragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.makvenis.dell.wangcangxianpolic.R;
import com.makvenis.dell.wangcangxianpolic.help.JSON;
import com.makvenis.dell.wangcangxianpolic.login.RegisteActivity;
import com.makvenis.dell.wangcangxianpolic.newdbhelp.AppMothedHelper;
import com.makvenis.dell.wangcangxianpolic.otherActivity.CorrectHistoryActivity;
import com.makvenis.dell.wangcangxianpolic.otherActivity.SetActivity;
import com.makvenis.dell.wangcangxianpolic.startActivity.UpdatePassActivity;
import com.makvenis.dell.wangcangxianpolic.tools.Configfile;
import com.makvenis.dell.wangcangxianpolic.tools.DownloadAppUpdateManager;
import com.makvenis.dell.wangcangxianpolic.view.SimpleDialogSureView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 个人中心 ---- 个人设置
 */

public class TaskCenterFragemnt extends Fragment {

    @ViewInject(R.id.minTaskCenter)
    RecyclerView mRecyclerView;

    /* 数据装载 */
    List<List<Object>> mData=new ArrayList<>();

    String obj;
    boolean isUpdate;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){

                case 0X000001:
                    if(msg.obj != null){
                        obj = (String) msg.obj;
                        Log.e("version",obj);
                        isUpdate=true;

                        if(isUpdate) {

                            Map<String, Object> json = JSON.getObjectJson(obj, new String[]{"address", "verid"});
                            Integer verid = (Integer) json.get("verid");
                            String address = (String) json.get("address");
                            Log.e("version",verid+"");
                            /* 此处地址只运用于测试 不可用于发行版 */
                            //String mTestAppUpdatePath = "http://192.168.0.106/im/version.1.6.1.apk";
                            final DownloadAppUpdateManager manager = new DownloadAppUpdateManager(getActivity(), address, "旺苍公安巡防");

                            int code = manager.getAppVersionCode();
                            int newCode = verid.intValue();
                            if(newCode > code){
                                // TODO: 2018/5/23  获取服务器的最新版本
                                final AlertDialog show = new AlertDialog.Builder(getContext())
                                        .setTitle("版本更新")
                                        .setIcon(R.drawable.icon_update_app_120)
                                        .setMessage("最新版本V1.2.3." + code + " \n 1.修复适配器图片加载方法 \n 2.优化特效配置"
                                                + " \n 3.修改推送服务"
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
                            }else {
                                Configfile.Log(getActivity(),"当前为最新版本");
                            }


                        }
                    }

                    break;
            }
        }
    };
    private MySimpleTaskCenterAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.min_fragment_task_center, null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /* 数据源 */
        List<List<Object>> mDtabase = CreadData(false);
        mData.addAll(mDtabase);
        /* 绑定适配器 */
        RecyclerView.LayoutManager manager=new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new MySimpleTaskCenterAdapter(mData);
        mRecyclerView.setAdapter(mAdapter);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewUtils.inject(this,view);
    }


    /* 创建数据 */
    public List<List<Object>> CreadData(boolean bool){

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

        /* 构建数据 密码修改 */
        List<Object> data6=new ArrayList<>();
        Map<String,String> map4_pass=new HashMap<>();
        map4_pass.put("mTitle","修改密码");
        map4_pass.put("mType","NEW_PASS");
        map4_pass.put("mTop","");
        List<Integer> mList_pass=new ArrayList<>();
        mList_pass.add(R.drawable.icon_user_passworld);
        data6.add(0,map4_pass);
        data6.add(1,mList_pass);

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

        /* 构建数据 个性风格 */
        List<Object> data_style=new ArrayList<>();
        Map<String,String> map4_style=new HashMap<>();
        map4_style.put("mTitle","个性风格");
        map4_style.put("mType","NEW_STYLE");
        map4_style.put("mTop","");
        List<Integer> mList_style=new ArrayList<>();
        mList_style.add(R.drawable.icon_user_jumpactivity_style);
        data_style.add(0,map4_style);
        data_style.add(1,mList_style);

        /* 版本检查 */
        List<Object> data5=new ArrayList<>();
        Map<String,String> map_version=new HashMap<>();
        map_version.put("mTitle","版本检查");
        map_version.put("mType","NEW_VERSION");
        // TODO: 2018/5/23  获取服务器的最新版本 如果有最新版本责需要显示为 map_version.put("mTop","(当前有新版本待更新)");
        // TODO: 2018/5/23  否则则显示为 map_version.put("mTop","(当前为最新版本)");
        if(bool == false){
            map_version.put("mTop","(当前为最新版本)");
        }else {
            map_version.put("mTop","(当前有新版本待更新)");
        }
        map_version.put("mTop","");
        List<Integer> list_version=new ArrayList<>();
        list_version.add(R.drawable.icon_update_app_120);
        data5.add(0,map_version);
        data5.add(1,list_version);

        mDtabase.add(data);
        mDtabase.add(data3);
        mDtabase.add(data6);
        mDtabase.add(data4);
        mDtabase.add(data_style);
        mDtabase.add(data5);

        return mDtabase;
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

                        try {
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

                        } catch (Exception e){
                            throw new IllegalArgumentException("窗口溢出");
                        }


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
                        Configfile.Log(getActivity(), "正在检查版本...");
                        new HttpUtils(10000).send(HttpRequest.HttpMethod.GET,
                                Configfile.APP_UPDATE,
                                new RequestCallBack<String>() {
                                    @Override
                                    public void onSuccess(ResponseInfo<String> responseInfo) {
                                        String result = responseInfo.result;
                                        if(result != null){
                                            Log.e("version",result);
                                            Message msg=new Message();
                                            msg.obj=result;
                                            msg.what=0X000001;
                                            handler.sendMessage(msg);
                                        }
                                    }

                                    @Override
                                    public void onFailure(HttpException e, String s) {
                                        Configfile.Log(getActivity(),"网络连接失败！");
                                    }
                                });

                    }else if(mType.equals("NEW_PASS")){ //密码修改

                                startActivity(new Intent(getActivity(), UpdatePassActivity.class));

                    }else if(mType.equals("NEW_STYLE")){ //个性风格
                        Configfile.Log(getActivity()," 关闭 ");
                    }else {
                        Configfile.Log(getActivity(),"Task 参数错误[ error ]" + mType);
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
