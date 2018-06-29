package sc.gys.wcx.and.details;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import sc.gys.wcx.and.R;
import sc.gys.wcx.and.help.JSON;
import sc.gys.wcx.and.newCompanyPost.ToBiLuActivity;
import sc.gys.wcx.and.newCompanyPost.ToNoActivity;
import sc.gys.wcx.and.newCompanyPost.ToYesActivity;
import sc.gys.wcx.and.newdbhelp.AppMothedHelper;
import sc.gys.wcx.and.tools.Configfile;
import sc.gys.wcx.and.tools.NetworkTools;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
            int i = Integer.valueOf(type.trim()).intValue();
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
                case 27:
                    return "当场处罚";  //当场处罚
                case 22:
                    return "责令改正";   //责令改正
                case 23:
                    return "限期整改";   //限期整改
                case 21:
                    return "检查笔录";   //检查笔录
                case 24:
                    return "同意延期整改";   //同意延期整改
                case 25:
                    return "不同意延期整改";   //不同意延期整改
                case 28:
                    return "收缴物品";   //收缴物品
                case 26:
                    return "隐患复查";   //隐患复查
            }
        }
        return null;
    }

    /* 根据当前type 获取路径 */
    public String getTypeUrl(String type) {
        if(type != null){

            int i = Integer.valueOf(type).intValue();
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
                    return Configfile.RESULT_HTML_TYPE_5;
                case 27:
                    return Configfile.RESULT_HTML_TYPE_10;  //当场处罚
                case 22:
                    return Configfile.RESULT_HTML_TYPE_8;   //责令改正
                case 23:
                    return Configfile.RESULT_HTML_TYPE_11;   //限期整改
                case 21:
                    return Configfile.RESULT_HTML_TYPE_15;   //检查笔录
                case 24:
                    return Configfile.RESULT_HTML_TYPE_12;   //同意延期整改
                case 25:
                    return Configfile.RESULT_HTML_TYPE_9;   //不同意延期整改
                case 28:
                    return Configfile.RESULT_HTML_TYPE_13;   //收缴物品
                case 26:
                    return Configfile.RESULT_HTML_TYPE_14;   //隐患复查
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

    /* 获取依赖本Activity的单位ID */
    public String getXmlIdString(){
        SharedPreferences xmlId = getActivity().getSharedPreferences("xmlId", Context.MODE_PRIVATE);
        String xmlIdString = xmlId.getString("id", "0");
        Log.e("TAG","haredPreferences()" + xmlIdString);
        return xmlIdString;
    }

    /* 获取数据 */
    public void createDataHistory(){
        String path = Configfile.HISTORY_PATH;
        /* 数据库查询当前登陆用户名称 */
        String name = getSqliteName();
        final String url=path+name+"&danweiId="+getXmlIdString();
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
                final String table = getTypeTable(type.trim());
                Log.e("TAG",type+" "+table);
                holder.title.setText(map.get("remark"));
                holder.id.setText(position+"");

                //new 之后的单位名称
                String remark = map.get("remark");
                if(remark.length() > 11){
                    String substring = remark.substring(0, 11);
                    holder.company.setText(substring+"...");
                }else {
                    holder.company.setText(map.get("remark"));
                }
                final String bianhao = map.get("bianhao");

                /* 获取检查时间 */
                String jctime = map.get("jctime");
                Log.e("TAG",jctime);
                /* 获取被检查单位ID */
                final String unitid = map.get("unitid");
                /* long 转 date() */
                Calendar cal = Calendar.getInstance();
                long longValue = Long.parseLong(jctime);
                cal.setTimeInMillis(longValue);
                Date date = cal.getTime();
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                String format = sdf.format(date);
                holder.mHistory_company_time.setText(format);
                Log.e("DATA","jctime:"+jctime+" >>> unitid"+unitid);


                /* 当Type类型不为基本的检查表的时候 */
                //当前文书名称
                String s = map.get("remark");
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
                        //当前文书名称
                        String s = map.get("remark");
                        bundle.putString("type",type);     //type类型
                        bundle.putString("id",unitid);     //单位ID
                        bundle.putString("companyName", s);//单位名称
                        bundle.putString("table",table);   //当前检查的表格（旅店安全检查表、校园、民爆...）
                        bundle.putString("url",url);       //拼接之后的完整地址
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });

                /* 继续检查 */
                holder.mHistory_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String mStringTableOfOne = "限期整改治安隐患通知书";
                        bottomwindow(v,unitid,map.get("remark"));
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

            @ViewInject(R.id.mHistory_company_time)
            TextView mHistory_company_time;    //检查时间
            @ViewInject(R.id.mHistory_ok)
            ImageView mHistory_ok;

            public SimpleViewHolder(View itemView) {
                super(itemView);
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

    public void bottomwindow(View view,String unitid,String remark) {

        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        LinearLayout layout;
        if(remark.indexOf("限期整改治安隐患通知书") != -1){
            //包含 表明当前的是处于限期整改治安隐患通知书
            Log.e("TAG","当前检查历史处于"+ remark + (remark.indexOf("限期整改治安隐患通知书") != -1)+"");
            layout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.layout_history_yeoorno_details, null);
        }else {
            Log.e("TAG","当前检查历史处于 else 部分");
            layout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.layout_history_start_details, null);
        }
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
        setButtonListeners(layout,unitid,remark);
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
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

    }

    /* PopWindows的控件全局变量 */
    private TextView danwei,faren,dizhi;

    public void setButtonListeners(LinearLayout view,final String unitid,String remark) { //

        if(remark.indexOf("限期整改治安隐患通知书") != -1){

            Button xqzg = (Button) view.findViewById(R.id.mHistory_start_xqzg);
            Button zlzg = (Button) view.findViewById(R.id.mHistory_start_zlzg);
            Button jcbl = (Button) view.findViewById(R.id.mHistory_start_jcbl);

            xqzg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(), ToYesActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("l_title","同意延期整改治安隐患");
                    bundle.putString("mUrl","");
                    bundle.putString("id",unitid);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            zlzg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(), ToNoActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("l_title","不同意延期整改治安隐患");
                    bundle.putString("mUrl","");
                    bundle.putString("id",unitid);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            jcbl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //页面接收数据
                    //Bundle bundle = this.getIntent().getExtras();
                    //mBianhao = bundle.getString("mLocal_bianhao");
                    //mUrl = bundle.getString("mUrl");
                    //mtitle = bundle.getString("l_title");
                    //id = bundle.getString("id");
                    Intent intent=new Intent(getActivity(), ToBiLuActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("l_title","检查笔录");
                    bundle.putString("mUrl","");
                    bundle.putString("id",unitid);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

        }else {

            Button xqzg = (Button) view.findViewById(R.id.mHistory_start_xqzg);
            Button zlzg = (Button) view.findViewById(R.id.mHistory_start_zlzg);
            Button dccf = (Button) view.findViewById(R.id.mHistory_start_dccf);
            Button jcbl = (Button) view.findViewById(R.id.mHistory_start_jcbl);
            final int TYPE_XQZG = 1;
            final int TYPE_ZLZG = 2;
            final int TYPE_DCCF = 3;
            final int TYPE_JCBL = 4;

            xqzg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DetailsUtils.getMethondDetails(TYPE_XQZG, unitid, getActivity());
                }
            });

            zlzg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DetailsUtils.getMethondDetails(TYPE_ZLZG, unitid, getActivity());
                }
            });

            dccf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DetailsUtils.getMethondDetails(TYPE_DCCF, unitid, getActivity());
                }
            });

            jcbl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //页面接收数据
                    //Bundle bundle = this.getIntent().getExtras();
                    //mBianhao = bundle.getString("mLocal_bianhao");
                    //mUrl = bundle.getString("mUrl");
                    //mtitle = bundle.getString("l_title");
                    //id = bundle.getString("id");
                    Intent intent=new Intent(getActivity(), ToBiLuActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("l_title","检查笔录");
                    bundle.putString("mUrl","");
                    bundle.putString("id",unitid);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }
    }

    /* PopWindows 的操作等 （结束） */
}
