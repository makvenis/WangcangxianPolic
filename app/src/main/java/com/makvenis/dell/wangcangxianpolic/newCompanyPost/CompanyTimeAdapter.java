package com.makvenis.dell.wangcangxianpolic.newCompanyPost;

/* 时间的添加 */

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.makvenis.dell.wangcangxianpolic.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CompanyTimeAdapter extends RecyclerView.Adapter<CompanyTimeAdapter.MyViewHolder>{

    public Context mContext;
    List<CompanyTimeEntry> data;

    public CompanyTimeAdapter(Context mContext, List<CompanyTimeEntry> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.public_company_iten_time, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if(holder instanceof MyViewHolder){
            final CompanyTimeEntry e = data.get(position);
            //设置标题
            holder.mCompany_Nmae.setText(e.getName());
            final TextView setTime = holder.mCompany_SetTime;
            holder.mCompany_QianTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //获取当前时间
                    getTimer(mContext,setTime);
                    mClink.bankTime(position,e.getKey(),setTime.getText().toString());

                    //初始化时间格式
                    String time = setTime.getText().toString();
                    //去除格式化
                    Date date = stringByDate(time + " 00:00:00");
                    mClink.bankTime(position,e.getKey(),date);
                }
            });

            if(e.getValue() != null){
                holder.mCompany_QianTime.setImageResource(e.getValue());
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @ViewInject(R.id.mCompany_QianTime)
        ImageView mCompany_QianTime;
        @ViewInject(R.id.mCompany_SetTime)
        TextView mCompany_SetTime;
        @ViewInject(R.id.mCompany_Nmae)
        TextView mCompany_Nmae;

        public MyViewHolder(View itemView) {
            super(itemView);
            ViewUtils.inject(this,itemView);
        }
    }

    /* 处理String类型的时间转化成data类型时间 */
    public Date stringByDate(String t){
        try {
            SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = fm.parse(t);
            //System.out.println(date.toString());
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new Date();
    }

    /* 触发时间事件选择器 */
    public void getTimer(final Context context,final TextView text){

        final List<String> mTime=new ArrayList<>();
        Calendar c = Calendar.getInstance();
        // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
        new DatePickerDialog(context,
                // 绑定监听器
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        text.setText(year + "-" + (monthOfYear+1) + "-" + dayOfMonth + "");
                        Log.e("TAG","获取事件"+year + "年" + monthOfYear+ "月" + dayOfMonth + "日");
                    }
                }
                // 设置初始日期
                , c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
                .get(Calendar.DAY_OF_MONTH)).show();

    }

    OnClinkItem mClink;

    public void setmClink(OnClinkItem mClink) {
        this.mClink = mClink;
    }
    public interface OnClinkItem{
        void bankTime(int postion,String key,String time);
        void bankTime(int postion,String key,Date time);
    }
}
