package com.makvenis.dell.wangcangxianpolic.company;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makvenis.dell.wangcangxianpolic.R;
import com.makvenis.dell.wangcangxianpolic.details.MoreDetailsActivity;
import com.makvenis.dell.wangcangxianpolic.tools.Configfile;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

/* 适配单位信息 */


public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.MyViewHolder>{

    List<Map<String,String>> mList;
    Context mContext;

    /* 当前RecycleView */
    public RecyclerView mRecyclerView;
    /* 实例化当前接口 */
    OnclinkRecycleItem mOnclinkRecycleItem;
    /*set 方法暴露出去接口*/
    public void SetOnclinkRecycleItem(OnclinkRecycleItem mOnchildListener) {
        this.mOnclinkRecycleItem = mOnchildListener;
    }


    /* 构造器 */
    public CompanyAdapter(List<Map<String, String>> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_company_adapter, parent, false);

        /* 创建View的点击事件 （普通） */
        /*view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRecyclerView !=null && mOnclinkRecycleItem !=null) {
                    int position = mRecyclerView.getChildAdapterPosition(v);
                    *//* 获取当前Item的所有数据 *//*
                    //单位ID
                    Map<String, String> map = mList.get(position);
                    String id = map.get("id");
                    mOnclinkRecycleItem.OnclinkRecycleItemListent(mRecyclerView,v,position,mList.get(position),id);
                }
            }
        });*/

        /* 创建View的点击事件 （长按） */
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (mRecyclerView !=null && mOnclinkRecycleItem !=null) {
                    int position = mRecyclerView.getChildAdapterPosition(v);
                    /* 获取当前Item的所有数据 */
                    //单位ID
                    Map<String, String> map = mList.get(position);
                    String id = map.get("id");
                    mOnclinkRecycleItem.OnclinkRecycleItemListent(mRecyclerView,v,position,mList.get(position),id);
                }

                return true;
            }
        });

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        /* 赋值操作 */
        //拿到数组
        final Map<String, String> map = mList.get(position);
        holder.item_TextView_Name.setText(map.get("name"));
        holder.item_TextView_Style.setText(map.get("attr"));
        holder.item_TextView_Path.setText(map.get("address"));
        String url = map.get("photoUrl");
        String serviceWebImg = Configfile.SERVICE_WEB_IMG;
        String replace = url.replace("../../", "");
        String img=serviceWebImg+replace;
        Picasso.with(mContext)
                .load(img)
                .placeholder(R.drawable.icon_normal_no_photo)
                .error(R.drawable.icon_normal_no_photo)
                .into(holder.item_ImageView);

        /* 处理侧滑事件按钮 */
        holder.deleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, MoreDetailsActivity.class);
                intent.putExtra("id",map.get("id"));//单位ID
                mContext.startActivity(intent);
            }
        });

        /* 处理备份按钮 */
        holder.addTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, MoreDetailsActivity.class);
                intent.putExtra("id",map.get("id"));//单位ID
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mList.size() != 0){
            return mList.size();
        }else {
            return 0;
        }
    }

    /* 接口数据曝出 */
    public interface OnclinkRecycleItem{
        void OnclinkRecycleItemListent(RecyclerView recyclerView,View view,int postion,Map<String,String> map,String id);
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

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public LinearLayout layout;
        public ImageView item_ImageView;
        public TextView item_TextView_Name,item_TextView_Style,item_TextView_Path;
        public TextView deleteTextView,addTextView;
        public MyViewHolder(View itemView) {
            super(itemView);
            layout = ((LinearLayout) itemView.findViewById(R.id.layout));
            item_TextView_Name = ((TextView) itemView.findViewById(R.id.item_TextView_Name));
            item_TextView_Style = ((TextView) itemView.findViewById(R.id.item_TextView_Style));
            item_TextView_Path = ((TextView) itemView.findViewById(R.id.item_TextView_Path));
            item_ImageView = ((ImageView) itemView.findViewById(R.id.item_ImageView));
            deleteTextView = (TextView)itemView.findViewById(R.id.content_delete);
            addTextView = (TextView)itemView.findViewById(R.id.content_add);
        }
    }

}
