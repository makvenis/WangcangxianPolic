package com.makvenis.dell.wangcangxianpolic.details;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.makvenis.dell.wangcangxianpolic.R;
import com.makvenis.dell.wangcangxianpolic.help.JSON;
import com.makvenis.dell.wangcangxianpolic.tools.Configfile;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* 单位详情（查看） */

public class ToViewFragment extends Fragment {

    /* 单位ID */
    private String xmlIdString;

    /* TAG */
    public final String TAG = "ToViewFragment";

    @ViewInject(R.id.mToViewSwipe)
    SwipeRefreshLayout mSwipe;

    @ViewInject(R.id.mToViewRecycle)
    RecyclerView mRecycle;

    /* Context */
    public final Context mContext = getActivity();

    /* 数据集合 */
    List<List<Map<String,String>>> mMaxData = new ArrayList<>();

    /* 全局Adapter */
    private ToViewAdapter mAdapter;

    public Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            switch (what){
                case 0X000001:
                    String obj = (String) msg.obj;
                    Log.e(TAG," mHandler >>> " + obj);
                    List<List<Map<String, String>>> lists = setTypeAdapterData(obj);
                    mMaxData.addAll(lists);
                    Log.e(TAG," addAll() 之后的集合状态 "+mMaxData.size() + " >>> " + mMaxData.toString());
                    /* 绑定适配器 */
                    RecyclerView.LayoutManager manager=new LinearLayoutManager(mContext,
                            LinearLayoutManager.VERTICAL,false);
                    mRecycle.setLayoutManager(manager);
                    mAdapter = new ToViewAdapter(getActivity(),mMaxData);
                    if( mMaxData != null ){
                        mRecycle.setAdapter(mAdapter);
                        mSwipe.setRefreshing(false);
                    }
                    break;
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_more_toview, null);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /* 获取固定xmlId 存储的单位id值 */
        SharedPreferences xmlId = getActivity().getSharedPreferences("xmlId", Context.MODE_PRIVATE);
        xmlIdString = xmlId.getString("id", "0");
        Log.e(TAG,"haredPreferences()" + xmlIdString);

        /* 设置刷新 */
        mSwipe.setRefreshing(true);

        /* 构建数据 */
        creatMoreDatils();

        /* 重新请求数据 */
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipe.setRefreshing(true);
                creatMoreDatils();
            }
        });


    }

    /* 数据规范化 */
    public List<List<Map<String, String>>> setTypeAdapterData(String  result ){

        if(result != null){
            /* 数据集合 */
            List<List<Map<String,String>>> mHandlerData = new ArrayList<>();

            Map<String, Object> json = JSON
                    .getObjectJson(result,
                    new String[]{"address", "attr", "legalaName", "level", "name", "pcs", "phone","type", "zjnum","photoUrl"});

            String[] key = new String[]{"address", "attr", "legalaName", "level", "name", "pcs", "phone","type", "zjnum","photoUrl"};

            String[] name = new String[]{"单位地址", "单位属性", "法人姓名", "管理等级", "单位名称", "管辖单位", "电话","证件类型", "证件编号","图片地址"};
            if(json.size() != 0){
                /* 构建第一组 */
                List<Map<String,String>> minDataImg = new ArrayList<>();
                String s = (String) json.get("photoUrl");
                if(s != null){
                    String[] split = s.split(",");
                    for (int i = 0; i < split.length; i++) {
                        Map<String,String> imgMpa = new HashMap<>();
                        String replace = split[i].replace("../../", Configfile.SERVICE_WEB_IMG);
                        imgMpa.put("url",replace);
                        minDataImg.add(imgMpa);
                    }
                }else {
                    for (int i = 0; i < 3; i++) {
                        Map<String, String> imgMpa = new HashMap<>();
                        imgMpa.put("url", Configfile.IMAGE_NO);
                        minDataImg.add(imgMpa);
                    }
                }

                /* 构建第二组 */
                List<Map<String,String>> minData = new ArrayList<>();

                for (int i = 0; i < json.size(); i++) {
                    Map<String,String> mByMapsData = new HashMap<>();

                    String getObj = (String) json.get(key[i]); //phone
                    if(getObj.equals("")){
                        mByMapsData.put("value", "暂未数据");
                        mByMapsData.put("key",name[i]);
                    }else {
                        mByMapsData.put("value", getObj);
                        mByMapsData.put("key",name[i]);
                    }
                    minData.add(mByMapsData);
                }
                mHandlerData.add(0,minDataImg);
                mHandlerData.add(1,minData);
                Log.e(TAG,mHandlerData.size()+" >>> " + mHandlerData.toString());
                return mHandlerData;
            }
        }
        return new ArrayList<>();
    }

    /* 数据下载 */
    private void creatMoreDatils() {
        String path = Configfile.COMPANY_URL_SEARCH_ID + xmlIdString;
        Log.e("TAG",path);
        new HttpUtils(5000).send(HttpRequest.HttpMethod.GET,
                path,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        if(result != null){
                            Message msg=new Message();
                            msg.what=0X000001;
                            msg.obj=result;
                            mHandler.sendMessage(msg);
                        }else {
                            Configfile.Log(mContext,"暂未有数据！");
                        }
                    }
                    @Override
                    public void onFailure(HttpException e, String s) {
                        Configfile.Log(mContext,"网络连接失败！");
                    }
                });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewUtils.inject(this,view);
    }


    public class ToViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        public final int VIEW_TYPE_0 = 0;
        public final int VIEW_TYPE_1 = 1;
        public final int VIEW_TYPE_2 = 2;

        public Context mContext;
        public List<List<Map<String,String>>> mMaxDataAdapter;


        public ToViewAdapter(Context mContext, List<List<Map<String, String>>> mMaxDataAdapter) {
            this.mContext = mContext;
            this.mMaxDataAdapter = mMaxDataAdapter;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType == 0){
                View view = LayoutInflater.from(mContext).inflate(R.layout.layout_datails_adapter_item_type_0, parent, false);
                return new MyRollViewHolder(view);
            }else if(viewType == 1){
                View view = LayoutInflater.from(mContext).inflate(R.layout.layout_datails_adapter_item_type_1, parent, false);
                return new MyRecycleViewHolder(view);
            }else if(viewType == 2){
                View view = LayoutInflater.from(mContext).inflate(R.layout.layout_datails_adapter_item_type_2, parent, false);
                return new MyPhotoleViewHolder(view);
            }else return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(holder instanceof MyRollViewHolder){
                List<Map<String, String>> maps = mMaxDataAdapter.get(0);
                if(maps != null){
                    RollPagerView page = ((MyRollViewHolder) holder).mRollPagerView;
                    //设置适配器
                    page.setAdapter(new MyPagerAdapter(maps));
                /* 启动自动翻页 */
                    page.pause();
                    page.resume();
                    page.isPlaying();

                    page.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            Toast.makeText(mContext,"Item "+position+" clicked",Toast.LENGTH_SHORT).show();
                        }
                    });
                }



            }else if(holder instanceof MyRecycleViewHolder){

                RecyclerView mDatilsRecycle = ((MyRecycleViewHolder) holder).mDatailsRecycle;

                List<Map<String, String>> maps = mMaxDataAdapter.get(1);

                if( maps.size() != 0){

                    LinearLayoutManager manager=new LinearLayoutManager(getActivity(),
                            LinearLayoutManager.VERTICAL,false);

                    mDatilsRecycle.setLayoutManager(manager);
                    mDatilsRecycle.setAdapter(new MyTestPaddingAdapter(maps,getActivity()));
                }


            }else if(holder instanceof MyPhotoleViewHolder){
                RelativeLayout layout = ((MyPhotoleViewHolder) holder).layout;
            }
        }

        @Override
        public int getItemViewType(int position) {
            if(position == 0){
                return VIEW_TYPE_0;
            }else if(position == 1){
                return VIEW_TYPE_1;
            }else if(position == 2){
                return VIEW_TYPE_2;
            }else return 3;
        }

        @Override
        public int getItemCount() {
            return 3;
        }


        /* Roll 静态内部类 */
        public  class MyRollViewHolder extends RecyclerView.ViewHolder{
            @ViewInject(R.id.mDatailsRoll)
            RollPagerView mRollPagerView;
            public MyRollViewHolder(View itemView) {
                super(itemView);
                ViewUtils.inject(this,itemView);
            }
        }


        /* Recycle 静态内部类 */
        public class MyRecycleViewHolder extends RecyclerView.ViewHolder{
            @ViewInject(R.id.mDatailsRecycle)
            RecyclerView mDatailsRecycle;
            public MyRecycleViewHolder(View itemView) {
                super(itemView);
                ViewUtils.inject(this,itemView);
            }
        }

        /* Recycle 静态内部类 */
        public class MyPhotoleViewHolder extends RecyclerView.ViewHolder{
            @ViewInject(R.id.layout)
            RelativeLayout layout;
            public MyPhotoleViewHolder(View itemView) {
                super(itemView);
                ViewUtils.inject(this,itemView);
            }
        }


    }

    public class MyPagerAdapter extends StaticPagerAdapter {

        private int[] image = {R.drawable.icon_test, R.drawable.icon_test, R.drawable.icon_test, R.drawable.icon_test};

        List<Map<String, String>> imgData;

        public MyPagerAdapter(List<Map<String, String>> imgData) {
            this.imgData = imgData;
        }

        // SetScaleType(ImageView.ScaleType.CENTER_CROP);
        // 按比例扩大图片的size居中显示，使得图片长(宽)等于或大于View的长(宽)

        @Override
        public View getView(ViewGroup container, int position) {
            ImageView imageView = new ImageView(container.getContext());

            //imageView.setImageResource(image[position]);
            String path="https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=488179422,3251067872&fm=200&gp=0.jpg";

            Map<String, String> map = imgData.get(position);
            String url = map.get("url");

            /* http://ssdaixiner.oicp.net:26168/wcjw/resources/images/nopic2.png */
            if(url != null){
                String[] split = url.split("//");
                String xy = split[0];
                if(xy.equals("http:") || xy.equals("https:")){
                    Picasso.with(mContext)
                            .load(url)
                            .placeholder(R.drawable.icon_normal_no_photo)
                            .error(R.drawable.icon_normal_404)
                            .into(imageView); //
                }else {
                    Picasso.with(mContext)
                            .load(Configfile.SERVICE_WEB_IMG)
                            .placeholder(R.drawable.icon_normal_no_photo)
                            .error(R.drawable.icon_normal_404)
                            .into(imageView); //
                }
            }

            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return imageView;
        }

        @Override
        public int getCount() {
            return imgData.size();
        }
    }

    public class MyTestPaddingAdapter extends RecyclerView.Adapter<MyTestPaddingAdapter.MyViewHolder>{


        List<Map<String,String>> data;
        Context mContext;

        public MyTestPaddingAdapter(List<Map<String, String>> data, Context mContext) {
            this.data = data;
            this.mContext = mContext;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(mContext).inflate(R.layout.layout_datails_adapter_item_type_viewholder, parent, false);

            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {


            Map<String, String> map = data.get(position);
            holder.mKey.setText((map.get("key")));
            holder.mValue.setText((map.get("value")));
            if(position == 3 || position == 7){
                LinearLayout liner = holder.liner;
                liner.setPadding(0,30,10,0);
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder{
            @ViewInject(R.id.liner)
            LinearLayout liner;

            @ViewInject(R.id.mViewHolderKey)
            TextView mKey;

            @ViewInject(R.id.mViewHolderValue)
            TextView mValue;

            public MyViewHolder(View itemView) {
                super(itemView);
                ViewUtils.inject(this,itemView);
            }
        }
    }



}
