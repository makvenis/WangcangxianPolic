package com.makvenis.dell.wangcangxianpolic.details;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import android.widget.Toast;

import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.makvenis.dell.wangcangxianpolic.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* 单位详情（查看） */

public class ToViewFragment extends Fragment {

    /* 单位ID */
    private String id;

    /* TAG */
    public final String TAG = "ToViewFragment";

    @ViewInject(R.id.mToViewSwipe)
    SwipeRefreshLayout mSwipe;

    @ViewInject(R.id.mToViewRecycle)
    RecyclerView mRecycle;

    /* Context */
    public final Context mContext = getActivity();

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
        String xmlIdString = xmlId.getString("id", "0");
        Log.e(TAG,"haredPreferences()" + xmlIdString);

        /* 绑定适配器 */
        RecyclerView.LayoutManager manager=new LinearLayoutManager(mContext,
                LinearLayoutManager.VERTICAL,false);
        mRecycle.setLayoutManager(manager);
        mRecycle.setAdapter(new ToViewAdapter(getActivity()));

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

        public ToViewAdapter(Context mContext) {
            this.mContext = mContext;
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

                RollPagerView page = ((MyRollViewHolder) holder).mRollPagerView;
                //设置适配器
                page.setAdapter(new MyPagerAdapter());
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


            }else if(holder instanceof MyRecycleViewHolder){

                RecyclerView mDatilsRecycle = ((MyRecycleViewHolder) holder).mDatailsRecycle;

                List<Map<String,String>> data = new ArrayList<>();
                for (int i = 0; i < 20; i++) {
                    Map<String,String> map=new HashMap<>();
                    map.put("key","asd");
                    data.add(map);
                }

                LinearLayoutManager manager=new LinearLayoutManager(getActivity(),
                        LinearLayoutManager.VERTICAL,false);

                mDatilsRecycle.setLayoutManager(manager);
                mDatilsRecycle.setAdapter(new MyTestPaddingAdapter(data,getActivity()));

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


        // SetScaleType(ImageView.ScaleType.CENTER_CROP);
        // 按比例扩大图片的size居中显示，使得图片长(宽)等于或大于View的长(宽)

        @Override
        public View getView(ViewGroup container, int position) {
            ImageView imageView = new ImageView(container.getContext());
            imageView.setImageResource(image[position]);
            String path="https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=488179422,3251067872&fm=200&gp=0.jpg";
            //Picasso.with(mContext).load(path).into(imageView); //失败
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return imageView;
        }

        @Override
        public int getCount() {
            return image.length;
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

            View view = LayoutInflater.from(mContext).inflate(R.layout.test_item, parent, false);

            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
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
            public MyViewHolder(View itemView) {
                super(itemView);
                ViewUtils.inject(this,itemView);
            }
        }
    }



}
