package com.makvenis.dell.wangcangxianpolic.activity;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
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
import com.makvenis.dell.wangcangxianpolic.tools.Configfile;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class SimpleAlertPushAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    List<Map<String,String>> mData;
    Context mContext;
    /* 当前RecycleView */
    private RecyclerView mRecyclerView;

    /* item回调接口 */
    public OnclinkItemView onclinkItemView;
    private ImageView mImageView_poto;

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
                    Map<String, String> map = mData.get(position);
                    String id = map.get("id");
                    onclinkItemView.showItem(mRecyclerView,v,id);
                }
            }
        });

        return new MyAlertPushViewHolder(view);
    }

    public Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                String obj = (String) msg.obj;
                Log.e("TAG","mHandler >>> "+obj);
                Picasso.with(mContext).load(obj).error(R.drawable.icon).into(mImageView_poto);
            }
        }
    };

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyAlertPushViewHolder){
            Map<String, String> map = mData.get(position);
            //赋值大标题
            if(map.get("title") != null){
                ((MyAlertPushViewHolder) holder).mTextView_title_max.setText(map.get("title"));
            }else {
                ((MyAlertPushViewHolder) holder).mTextView_title_max.setText("Adapter [error] 解析错误");
            }
            //赋值小标题
            String remark = map.get("remark");
            if( remark != null){
                if(remark.length() > 15){
                    String substring = remark.substring(0, 15);
                    ((MyAlertPushViewHolder) holder).mTextView_title_min.setText(substring+"...");
                }else {
                    ((MyAlertPushViewHolder) holder).mTextView_title_min.setText(remark);
                }

            }else {
                ((MyAlertPushViewHolder) holder).mTextView_title_min.setText("Adapter [error] 解析错误");
            }

            //赋值时间函数
            String time = map.get("addtime");
            if(time != null){
                //时间转换
                long l = Long.parseLong(time);
                Date date = new Date(l);
                String format = new SimpleDateFormat("yyyy-MM-dd").format(date);
                ((MyAlertPushViewHolder) holder).mTimer.setText(format);
            }else {
                ((MyAlertPushViewHolder) holder).mTimer.setText("Adapter [error] 解析错误");
            }

            //图片赋值
            final String img = map.get("picdefault");
            mImageView_poto = ((MyAlertPushViewHolder) holder).mImageView_poto;
            if(img != null){

                final String replace = img.replace("../../", Configfile.SERVICE_WEB_IMG);
                Log.e("TAG",new Date()+" >>>  预备传递的图片地址 "+replace);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL(replace);
                            HttpURLConnection conn= (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("GET");
                            int code = conn.getResponseCode();
                            Log.e("TAG",new Date()+" >>> 请求码 "+code+"");
                            if(code == 200){
                                Message msg=new Message();
                                msg.what=200;
                                msg.obj=replace;
                                mHandler.sendMessage(msg);
                            }else {
                                Message msg=new Message();
                                msg.what=200;
                                msg.obj="http://ssdaixiner.oicp.net:26168/wcjw/resources/images/nopic2.png";
                                mHandler.sendMessage(msg);
                            }
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (ProtocolException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }else {
                String imgNoPath="http://ssdaixiner.oicp.net:26168/wcjw/resources/images/nopic2.png";
                Picasso.with(mContext).load(imgNoPath).error(R.drawable.icon).into(((MyAlertPushViewHolder) holder).mImageView_poto);
            }

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

        @ViewInject(R.id.adapter_item_push_title_time)
        TextView mTimer;
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
        void showItem(RecyclerView recyclerView,View view,String id);
    }
}
