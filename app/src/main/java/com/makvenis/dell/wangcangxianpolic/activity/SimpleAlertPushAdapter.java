package com.makvenis.dell.wangcangxianpolic.activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.makvenis.dell.wangcangxianpolic.R;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;


public class SimpleAlertPushAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    List<Map<String,String>> mData;
    Context mContext;
    /* 当前RecycleView */
    private RecyclerView mRecyclerView;

    /* item回调接口 */
    public OnclinkItemView onclinkItemView;

    public void setOnclinkItemView(OnclinkItemView onclinkItemView) {
        this.onclinkItemView = onclinkItemView;
    }

    public SimpleAlertPushAdapter(List<Map<String,String>> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_alertpush_item_activity, parent, false);
        /* Item的点击事件 */
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mRecyclerView != null && onclinkItemView != null){
                    int position = mRecyclerView.getChildAdapterPosition(v);
                    onclinkItemView.showItem(mRecyclerView,v,position);
                }
            }
        });

        return new MyAlertPushViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyAlertPushViewHolder){
            Map<String, String> map = mData.get(position);
            ((MyAlertPushViewHolder) holder).mTextView_title_min.setText(map.get("title"));
            ImageView mImageView_poto = ((MyAlertPushViewHolder) holder).mImageView_poto;
            Picasso.with(mContext).load(map.get("img")).into(mImageView_poto);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyAlertPushViewHolder extends RecyclerView.ViewHolder{
        @ViewInject(R.id.adapter_item_push_title_poto)
        ImageView mImageView_poto;

        @ViewInject(R.id.adapter_item_push_title_max)
        TextView mTextView_title_max;

        @ViewInject(R.id.adapter_item_push_title_min)
        TextView mTextView_title_min;
        public MyAlertPushViewHolder(View itemView) {
            super(itemView);
            ViewUtils.inject(this,itemView);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.mRecyclerView=recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.mRecyclerView=null;
    }

    public interface OnclinkItemView{
        void showItem(RecyclerView recyclerView,View view,int position);
    }
}
