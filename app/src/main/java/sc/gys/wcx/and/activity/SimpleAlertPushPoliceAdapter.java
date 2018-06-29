package sc.gys.wcx.and.activity;

import android.content.Context;
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
import sc.gys.wcx.and.R;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

/**
 * 公安推送的消息列表 适配器
 */

public class SimpleAlertPushPoliceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private int TYPE_1 = 0;
    private int TYPE_2 = 1;
    private int TYPE_3 = 2;

    Context mContext;
    List<Object> mData;

    public SimpleAlertPushPoliceAdapter(Context mContext, List<Object> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return TYPE_1;
        }else if (position == 1){
            return TYPE_2;
        }else {
            return TYPE_3;
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        if(viewType == TYPE_1){
            view= LayoutInflater.from(mContext).inflate(R.layout.alert_push_item_img,null,false);
            return new ImageViewHolder(view);
        }else if(viewType == TYPE_2){
            view= LayoutInflater.from(mContext).inflate(R.layout.alert_push_item_news,null,false);
            return new NowTimeNewsViewHolder(view);
        }else if(viewType == TYPE_3){
            view= LayoutInflater.from(mContext).inflate(R.layout.alert_push_item_history,null,false);
            return new HistoryViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof ImageViewHolder){

            Map<String, String> map = (Map<String, String>) mData.get(0);

            if(map.size() > 0){

                ImageView m_img = ((ImageViewHolder) holder).mImageView;

                Picasso.with(mContext).load(map.get("picdefault")).into(m_img);
                ((ImageViewHolder) holder).mTextView.setText(map.get("title"));
                ((ImageViewHolder) holder).mTextView_num.setText(map.get("hits"));
            }


        }else if(holder instanceof NowTimeNewsViewHolder){
            Map<String, String> map = (Map<String, String>) mData.get(1);
            if(map.size() > 0){

                ((NowTimeNewsViewHolder) holder).mTextView_max.setText(map.get("remark"));
                ((NowTimeNewsViewHolder) holder).mTextView_min.setText("来源:"+map.get("laiyuan"));
            }


        }else if(holder instanceof HistoryViewHolder){
            RecyclerView Recycle_view = ((HistoryViewHolder) holder).mRecyclerView;
            //获取数据
            List<Map<String,String>> objects = (List<Map<String,String>>) mData.get(2);

            Log.e("SimpleAlertPushPoliceAdapter",objects.size()+"=======");

            //设置布局管理器
            RecyclerView.LayoutManager manager=new LinearLayoutManager(mContext,
                    LinearLayoutManager.VERTICAL,
                    false);
            Recycle_view.setLayoutManager(manager);
            ItemHistoryAdapter adapter=new ItemHistoryAdapter(objects);
            if(objects.size() > 0){
                Recycle_view.setAdapter(adapter);
            }


        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    /* 静态内部类 (图片) */
    public class ImageViewHolder extends RecyclerView.ViewHolder{
        @ViewInject(R.id.mAlertPush_police_item_img_img)
        ImageView mImageView;
        @ViewInject(R.id.mAlertPush_police_item_img_title)
        TextView mTextView;
        @ViewInject(R.id.mAlertPush_police_item_img_num)
        TextView mTextView_num;
        public ImageViewHolder(View itemView) {
            super(itemView);
            ViewUtils.inject(this,itemView);
        }
    }

    /* 静态内部类 (今日要闻) */
    public class NowTimeNewsViewHolder extends RecyclerView.ViewHolder{
        @ViewInject(R.id.mAlertPush_police_item_now_title)
        TextView mTextView_max;
        @ViewInject(R.id.mAlertPush_police_item_now_minTitle)
        TextView mTextView_min;
        public NowTimeNewsViewHolder(View itemView) {
            super(itemView);
            ViewUtils.inject(this,itemView);
        }
    }

    /* 静态内部类 (历史推送) */
    public class HistoryViewHolder extends RecyclerView.ViewHolder{
        @ViewInject(R.id.mAlertPush_police_item_now_recycle)
        RecyclerView mRecyclerView;
        public HistoryViewHolder(View itemView) {
            super(itemView);
            ViewUtils.inject(this,itemView);
        }
    }

    /* 内部类适配器(历史推送) */
    public class ItemHistoryAdapter extends RecyclerView.Adapter<ItemHistoryAdapter.ItemHistoryViewHolder>{

        List<Map<String,String>>  mMindata;

        public ItemHistoryAdapter(List<Map<String,String>>  mMindata) {
            this.mMindata = mMindata;
        }

        @Override
        public ItemHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.alert_push_item_history_news, null, false);
            return new ItemHistoryViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ItemHistoryViewHolder holder, int position) {
            if(holder instanceof ItemHistoryViewHolder){
                Map<String,String> list = mMindata.get(position);
                holder.max_TextView.setText(list.get("title"));
                holder.min_TextView.setText(list.get("remark"));
                Picasso.with(mContext).load(list.get("picdefault")).into(holder.mImageView);
            }
        }

        @Override
        public int getItemCount() {
            return mMindata.size();
        }

        public class ItemHistoryViewHolder extends RecyclerView.ViewHolder{
            @ViewInject(R.id.mAlertPush_police_item_history_maxTitle)
            TextView max_TextView;
            @ViewInject(R.id.mAlertPush_police_item_history_minTitle)
            TextView min_TextView;
            @ViewInject(R.id.mAlertPush_police_item_history_img)
            ImageView mImageView;
            public ItemHistoryViewHolder(View itemView) {
                super(itemView);
                ViewUtils.inject(this,itemView);
            }
        }
    }
}
