package com.makvenis.dell.wangcangxianpolic.hotelcheck;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.makvenis.dell.wangcangxianpolic.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HotelCheckAdapter extends RecyclerView.Adapter<HotelCheckAdapter.MyViewHolder> {



    private List<HotelChectEntry> mDataBase;
    private Context mContext;

    public HotelCheckAdapter(List<HotelChectEntry> mDataBase, Context mContext) {
        this.mDataBase = mDataBase;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_hotel_check, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        HotelChectEntry entry = mDataBase.get(position);
        //查找当前布局Item
        View itemView = holder.itemView;
        //获取输入的内容
        String mEditText = holder.item_hotel_beizhu_text.getText().toString();
        //赋值标题
        holder.item_hotel_title.setText(entry.getmTitle());

        /* 构建数据 */
        List<Map<String,String>> mDataBaseMap = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Map<String,String> map = new HashMap<>();
            map.put("mTitle","特行证是否上墙");
            mDataBaseMap.add(map);
        }

        SimpleAdapter mAdapter = new SimpleAdapter(mContext,mDataBaseMap,R.layout.public_item_check_xml,
                new String[]{"mTitle"},new int[]{R.id.item_public_check_title});
        //赋值适配器
        holder.item_hotel_wen_list.setAdapter(mAdapter);


    }

    @Override
    public int getItemCount() {
        if(mDataBase.size() != 0){

            return mDataBase.size();
        }else {

            return 0;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private ListView item_hotel_wen_list;
        private TextView item_hotel_title;
        private EditText item_hotel_beizhu_text;

        public MyViewHolder(View itemView) {
            super(itemView);
            item_hotel_title = ((TextView) itemView.findViewById(R.id.item_hotel_title));
            item_hotel_beizhu_text = ((EditText) itemView.findViewById(R.id.item_hotel_beizhu_text));
            item_hotel_wen_list = ((ListView) itemView.findViewById(R.id.item_hotel_wen_list));
        }
    }



}
