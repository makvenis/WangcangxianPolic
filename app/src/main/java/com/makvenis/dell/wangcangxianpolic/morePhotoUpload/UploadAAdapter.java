package com.makvenis.dell.wangcangxianpolic.morePhotoUpload;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.makvenis.dell.wangcangxianpolic.R;

import java.io.IOException;
import java.util.List;


public class UploadAAdapter extends RecyclerView.Adapter<UploadAAdapter.MyViewHolder>{

    Context mContext;
    List<Uri> data;
    AddItemOnClink mAddItemOnClink;

    public void setAddItemOnClink(AddItemOnClink mAddItemOnClink) {
        this.mAddItemOnClink = mAddItemOnClink;
    }

    public UploadAAdapter(Context mContext, List<Uri> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_upload_image_item_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        if(holder instanceof MyViewHolder){
            Uri uri = data.get(position);

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);
                holder.mImageItem.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }


            if(position == data.size()-1){
                View itemView = ((MyViewHolder) holder).itemView;
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext,"我是最后一个",Toast.LENGTH_SHORT).show();
                        /**
                         * 点击最后一个触发回调事件
                         */
                        mAddItemOnClink.show();

                    }
                });
            }

        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        @ViewInject(R.id.mImageItem)
        ImageView mImageItem;
        public MyViewHolder(View itemView) {
            super(itemView);
            ViewUtils.inject(this,itemView);
        }
    }

    public interface AddItemOnClink{
        void show();
    }

}
