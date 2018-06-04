package com.makvenis.dell.wangcangxianpolic.newCompanyPost;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.gesture.Gesture;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.makvenis.dell.wangcangxianpolic.R;
import com.makvenis.dell.wangcangxianpolic.help.CacheUtils;

import java.io.File;
import java.util.List;

/* 签名的适配器 */

public class CompanyGouseAdapter extends RecyclerView.Adapter<CompanyGouseAdapter.MyViewHolder> {

    private Context mContext;
    List<CompanyGouseEntry> data;

    public CompanyGouseAdapter(Context mContext, List<CompanyGouseEntry> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.public_company_iten_image, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        if(holder instanceof MyViewHolder){
            final CompanyGouseEntry e = data.get(position);

            //赋值
            holder.mCompany_Nmae.setText(e.getName());

            // 签名的点击事件
            holder.mCompany_QianMing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getGesture(holder.mCompany_SetMing,e.getKey());
                    /* 路径 */
                    File mFile = mContext.getExternalFilesDir(null);
                    String path = mFile.getAbsoluteFile() + e.getKey() + ".png"; //当前文件的地址
                    Bitmap bitmap= BitmapFactory.decodeFile(path); //将图片的长和宽缩小味原来的1/2
                    //回调图片的地址 和 Bitmap
                    mClink.saveImage(bitmap,e.getKey(),path);
                }
            });

            // 签名之后后去剪切的图片赋值UI界面
            // TODO: 2018/6/3  签名之后后去剪切的图片赋值UI界面


        }
    }

    /* AlertDialog 的点击事件 */
    /**
     * @param imageView 用户画好界面的图之后赋值到UI（imageView）
     * @param fileName 当使用Gesture来实现手势绘图的时候 当绘制完毕需要给图片命名
     */
    public void getGesture(final ImageView imageView,final String fileName) {

        View myView = LayoutInflater.from(mContext).inflate(R.layout.my_gesure_xml, null, false);
        GestureOverlayView gesure = (GestureOverlayView) myView.findViewById(R.id.dialog_gesture);
        gesure.setGestureColor(Color.GREEN);//设置手势的颜色
        gesure.setGestureStrokeWidth(2);//设置手势的画笔粗细
        gesure.setFadeOffset(3000); //手势淡出时间
        gesure.addOnGesturePerformedListener(new GestureOverlayView.OnGesturePerformedListener() {
            @Override
            public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
                //将手势转换成图片
                Bitmap bitmap = gesture.toBitmap(128,128,10,0xffff0000);
                //保存
                CacheUtils.setWLocalCache(bitmap,fileName,mContext);
            }
        });


        // 创建构建器
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        // 设置参数
        builder.setTitle("提示").setIcon(R.mipmap.icon_app)
                .setMessage("请等待签名缓存完毕并且淡出！如果签名失败请清除缓存")
                .setView(myView)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {// 积极
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bitmap localCache = CacheUtils.getLocalCache(fileName,mContext);
                        imageView.setImageBitmap(localCache);
                    }
                })
                .setNegativeButton("清除缓存", new DialogInterface.OnClickListener() {// 消极
                    @Override
                    public void onClick(DialogInterface dialog,int which) {

                    }
                })
                .setNeutralButton("取消", new DialogInterface.OnClickListener() {// 中间级
                    @Override
                    public void onClick(DialogInterface dialog,int which) {
                    }
                });
        builder.create().show();
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        @ViewInject(R.id.mCompany_SetMing)
        ImageView mCompany_SetMing;
        @ViewInject(R.id.mCompany_QianMing)
        ImageView mCompany_QianMing;
        @ViewInject(R.id.mCompany_Nmae)
        TextView mCompany_Nmae;

        public MyViewHolder(View itemView) {
            super(itemView);
            ViewUtils.inject(this,itemView);
        }
    }


    OnClinkItem mClink;
    public void setOnItemClick(OnClinkItem mClink) {
        this.mClink = mClink;
    }

    public interface OnClinkItem{
        void saveImage(Bitmap bitmap,String name,String path);
    }
}
