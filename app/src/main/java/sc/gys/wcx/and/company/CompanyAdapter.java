package sc.gys.wcx.and.company;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import sc.gys.wcx.and.R;
import sc.gys.wcx.and.details.SelectDetailsActivity;
import sc.gys.wcx.and.tools.Configfile;
import sc.gys.wcx.and.view.SimpleDialogSureView;
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
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        /* 赋值操作 */
        //拿到数组
        final Map<String, String> map = mList.get(position);
        holder.item_TextView_Name.setText(map.get("name"));
        holder.item_TextView_Style.setText(map.get("attr"));
        holder.item_TextView_Path.setText(map.get("address"));
        String url = map.get("photoUrl");
        Log.e("TAG","适配器集合取值（photoUrl）"+url);
        /* 拆分 */ //str.indexOf("ABC")!=-1 包含字符串
        if(url != null && url.indexOf("../../") != -1){
            String[] split = url.split(",");
            String path = split[0].replace("../../",Configfile.SERVICE_WEB_IMG);
            Picasso.with(mContext)
                    .load(path)
                    .resize(100,100)
                    .centerCrop()
                    .placeholder(R.drawable.icon_normal_no_photo)
                    .error(R.drawable.icon_normal_no_photo)
                    .into(holder.item_ImageView);
            Log.e("TAG","适配器图片加载网络地址（url）"+path);
        }else {
            //加载本地默认的图片 //暂未图片
            holder.item_ImageView.setImageResource(R.drawable.icon_normal_no_photo);
        }

        /* 处理侧滑事件按钮 */
        holder.deleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent=new Intent(mContext, MoreDetailsActivity.class);
                Intent intent=new Intent(mContext, SelectDetailsActivity.class);
                intent.putExtra("id",map.get("id"));//单位ID
                mContext.startActivity(intent);
            }
        });

        /* 处理单位删除 */
        holder.addTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String id = map.get("id");
                final SimpleDialogSureView dialog=new SimpleDialogSureView(mContext);
                dialog.setMessage("确认删除当前单位? 不恰当删除将造成数据丢失");
                dialog.show();

                dialog.setOnclinkDialogSureListener(new SimpleDialogSureView.setOnclinkDialogSureListener() {
                    @Override
                    public Void OnClinkSureListener() {
                        mCallBankItemCheck.show(id,position);
                        dialog.dismiss();
                        return null;
                    }


                });

                dialog.setOnclinkDialogCancelListener(new SimpleDialogSureView.setOnclinkDialogCancelListener() {
                    @Override
                    public Void OnClinkCancelListener() {
                        // TODO: 2018/6/2  取消当前操作
                        dialog.dismiss();
                        return null;
                    }
                });
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

    CallBankItemCheck mCallBankItemCheck;

    public void SetCallBankItemCheck(CallBankItemCheck mCallBankItemCheck) {
        this.mCallBankItemCheck = mCallBankItemCheck;
    }

    public interface CallBankItemCheck{
        /**
         * @param id 删除的单位ID
         */
        void show(String id,int postion);
    }

}
