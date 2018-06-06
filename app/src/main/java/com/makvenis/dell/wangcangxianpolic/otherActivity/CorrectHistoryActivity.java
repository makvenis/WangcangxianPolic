package com.makvenis.dell.wangcangxianpolic.otherActivity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.makvenis.dell.wangcangxianpolic.R;
import com.makvenis.dell.wangcangxianpolic.help.JSON;
import com.makvenis.dell.wangcangxianpolic.newdbhelp.AppMothedHelper;
import com.makvenis.dell.wangcangxianpolic.startActivity.BaseActivity;
import com.makvenis.dell.wangcangxianpolic.startActivity.HomeActivity;
import com.makvenis.dell.wangcangxianpolic.tools.Configfile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *@ 解释 我的的检查历史
 *@ 采用数据库的存储方式来更新
 *@ 检查单位 检查事项 是否完成检查 未完成的项目
 */
//layout_history_item Item的地址
@ContentView(R.layout.activity_correct_history)
public class CorrectHistoryActivity extends BaseActivity {

    /* 全局采用注解框架 */
    @ViewInject(R.id.mHistory_siwipe)
    SwipeRefreshLayout mSwipe;
    @ViewInject(R.id.mHistory_recycle)
    RecyclerView mRecyclerView;
    /* 当前用户用户 */
    private String mName;
    /* 全局Adapter */
    private MySimpleHistoryAdapter mAdapter;

    /* 全局上下文 */
    public final Context mContext=CorrectHistoryActivity.this;

    /* include 里面的点击事件 */
    @ViewInject(R.id.toolbar_callbank)
    ImageView mImageView_bank;
    @ViewInject(R.id.mToolbar_text)
    TextView mTextView;

    /* 全局Handler */
    public Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String obj = (String) msg.obj;
            switch (msg.what){

                case 0X000017:
                    List<Map<String, String>> maps = JSON.GetJson(obj, new String[]{});

                    break;

            }
        }
    };

    @Override
    public void getUserName(String s) {
        this.mName = s;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);

        /* 刷新组件 */
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cractData();
                mSwipe.setRefreshing(false);
            }
        });

        mTextView.setText(mName+"检查历史");

        /* 绑定适配器 */
        setAdapterManager();
    }

    /* 返回 */
    @OnClick({R.id.toolbar_callbank})
    public void oncklinkView(View v){
        Intent intent=new Intent(this,HomeActivity.class);
        intent.putExtra("bank_id",2);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    /* 返回 */
    @OnClick({R.id.mToolbar_text})
    public void onclinkText(View v){
        Intent intent=new Intent(this,HomeActivity.class);
        intent.putExtra("bank_id",2);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    /* 创建数据 */
    public List<Map<String, Object>> cractData(){
        AppMothedHelper helper=new AppMothedHelper(this);
        if( mName != null){
            List<Map<String, Object>> maps = helper.queryByKeyAddrs(mName);
            Log.e("TAG",new Date()+" >>> 当前用户为"+mName);
            Configfile.Log(mContext,"当前用户为"+mName);
            return maps;
        }else {
            Log.e("TAG",new Date()+" >>> 当前用户为"+mName);
            Configfile.Log(mContext,"当前用户为"+mName);
            return new ArrayList<>();
        }
    }

    /* 适配器操作类 */
    private void setAdapterManager() {
        /* 创建数据 */
        List<Map<String, Object>> data = cractData();
        mAdapter = new MySimpleHistoryAdapter(data);
        RecyclerView.LayoutManager manager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);
    }


    /* 为了方便管理 此页面的适配器就写在此处 */
    public class MySimpleHistoryAdapter extends RecyclerView.Adapter<MySimpleHistoryAdapter.MySimpleHistoryViewHolder>{

        List<Map<String, Object>> mAdapterData;

        public MySimpleHistoryAdapter(List<Map<String, Object>> maps) {
            this.mAdapterData = maps;
        }

        @Override
        public MySimpleHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.layout_history_item, parent, false);
            return new MySimpleHistoryViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MySimpleHistoryViewHolder holder, int position) {
            if(holder instanceof MySimpleHistoryViewHolder){
                final Map<String, Object> map = mAdapterData.get(position);
                int c=position+1;
                holder.id.setText(c+"");
                if(position >= 3){ //说明是最新的消息
                    holder.gone.setVisibility(View.GONE);
                }
                holder.name.setText(mName);
                holder.company.setText(((String) map.get("className")));//检查的内容 当前时间检查了什么内容？
                // TODO: 2018/5/18  被检查的单位名称？ 根据单位ID去获取
                holder.title.setText("被检查单位（ID="+map.get("danwei")+"）");//被检查的单位名称

                holder.ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       Configfile.Log(mContext,"数据已上传 (完成)");
                    }
                });

                holder.looding.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //被检查的单位ID
                        String danWeiId = (String) map.get("danwei");
                        //添加时间ID（用于删除的时候的条件）
                        String addTime = (String) map.get("time");
                        /* 调用底部PopWindows */
                        bottomwindow(v,danWeiId,addTime);
                    }
                });

            }
        }

        @Override
        public int getItemCount() {
            return mAdapterData.size();
        }

        public class MySimpleHistoryViewHolder extends RecyclerView.ViewHolder{

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
            @ViewInject(R.id.mHistory_looding) //更多
            ImageView looding;
            @ViewInject(R.id.mHistory_ok)      //盾牌
            ImageView ok;
            public MySimpleHistoryViewHolder(View itemView) {
                super(itemView);
                /* Adapter注册 注解事件 */
                ViewUtils.inject(this,itemView);
            }
        }
    }

    /* PopWindows 的操作等（开始） */
    /**
     * @ 解释直接调用 bottomwindow(view)方法
     * @ 解释 详细的不揍操作在方法setButtonListeners()里面
     */
    private PopupWindow popupWindow;

    public void bottomwindow(View view,String danWeiId,String addTime) {

        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.layout_history_popwindows, null);
        popupWindow = new PopupWindow(layout,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        //点击空白处时，隐藏掉pop窗口
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //添加弹出、弹入的动画
        popupWindow.setAnimationStyle(R.style.Popupwindow);
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        popupWindow.showAtLocation(view, Gravity.LEFT | Gravity.BOTTOM, 0, -location[1]);
        //添加按键事件监听
        setButtonListeners(layout,danWeiId,addTime);
        //添加pop窗口关闭事件，主要是实现关闭时改变背景的透明度
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
        backgroundAlpha(0.5f);
    }

    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

    }

    /* PopWindows的控件全局变量 */
    private TextView danwei,faren,dizhi;

    public void setButtonListeners(LinearLayout view,String danWeiId,final String addTime) { //
        danwei = (TextView) view.findViewById(R.id.mHistory_pop_type2_danwei); //单位
        faren = (TextView) view.findViewById(R.id.mHistory_pop_type2_faren);   //法人
        dizhi = (TextView) view.findViewById(R.id.mHistory_pop_type2_dizhi);   //地址
        LinearLayout delete = (LinearLayout) view.findViewById(R.id.mHistory_pop_delete);//删除该条记录 mHistory_pop_over
        LinearLayout callBank = (LinearLayout) view.findViewById(R.id.mHistory_pop_over); //退出
        // 判断单位ID是否存在 否则导致无法查询单位的具体信息
        if(danWeiId == null){
            danwei.setText("暂未数据");
            faren.setText("暂未数据");
            dizhi.setText("暂未数据");
        }else {
            // TODO: 2018/5/18 赋值单位信息
            danwei.setText("暂未数据");
            faren.setText("暂未数据");
            dizhi.setText("暂未数据");

        }
        setOnclinkButtom(delete,1,addTime);
        setOnclinkButtom(callBank,2,addTime);
    }

    /* PopWindows 的操作等 （结束） */

    public void setOnclinkButtom(LinearLayout ll, final int type,final String addTime){
        switch (type){
            case 1: //删除该条记录
                ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(addTime == null){ //也即是当缺乏删除条件的时候责不删除
                            Configfile.Log(mContext,"参数错误[addTime] 删除失败");
                        }else {
                            String sql="delete from addrs where time = '"+addTime+"'";
                            Log.e("TAG",new Date()+" >>> 预备执行的删除语句 "+sql);
                            new AppMothedHelper(mContext).executeSql(sql);
                            Configfile.Log(mContext,"删除成功");
                            mAdapter.notifyDataSetChanged();
                        }
                        //关闭PopWindows
                        popupWindow.dismiss();
                    }
                });
                break;
            case 2: //退出
                ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });

                break;

        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() ==  KeyEvent.ACTION_DOWN){
            Intent intent=new Intent(this,HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        return true;
    }


}
