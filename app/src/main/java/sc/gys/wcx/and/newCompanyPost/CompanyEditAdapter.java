package sc.gys.wcx.and.newCompanyPost;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import sc.gys.wcx.and.R;

import java.util.List;

/**
 * Adapter中回调用户的输入参数
 *
 * 使用方法
 *
 *  String[] mTitleName=new String[]{"编号","旺公","违法行为"};
    String[] key=new String[]{"number","readHead","document"};
    List<CompanyItemEntry> mData=new ArrayList<>();


                for (int i = 0; i < mTitleName.length; i++) {
                        CompanyItemEntry e = new CompanyItemEntry();
                        e.setType(TYPE_EDIT);
                        e.setName(mTitleName[i]); //必要参数
                        e.setKey(key[i]); //必要参数
                        e.setValue(null);
                        mData.add(e);
                        }
 */





public class CompanyEditAdapter extends RecyclerView.Adapter<CompanyEditAdapter.MyViewHolder> {

    public Context mContext;
    List<CompanyItemEntry> data;

    public CompanyEditAdapter(Context mContext, List<CompanyItemEntry> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.public_company_iten_edit, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        if(holder instanceof MyViewHolder){
            final CompanyItemEntry e = data.get(position);
            holder.mCompany_Nmae.setText(e.getName());

            final EditText mCompany_edit = holder.mCompany_Edit;
            //点击事件
            //.....
            // editText设置输入监听
            mCompany_edit.addTextChangedListener(new TextSwitcher(holder,e));
            //失去焦点的回调事件
            mCompany_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(!hasFocus){
                        String string = mCompany_edit.getText().toString();
                        mClick.overFoucesEdit(position,e.getKey(),string);
                    }

                }
            });
            // 将每个position位置和EditText相关联
            mCompany_edit.setTag(position);
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        @ViewInject(R.id.mCompany_Nmae)
        TextView mCompany_Nmae;
        @ViewInject(R.id.mCompany_Edit)
        EditText mCompany_Edit;

        public MyViewHolder(View itemView) {
            super(itemView);
            ViewUtils.inject(this,itemView);
        }
    }

    private class TextSwitcher implements TextWatcher {

        private MyViewHolder mHolder;
        private CompanyItemEntry e;

        public TextSwitcher(MyViewHolder mHolder, CompanyItemEntry e) {
            this.mHolder = mHolder;
            this.e = e;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s != null) {
                //回调用户当前的输入
                mClick.loadingEdit(((Integer) mHolder.mCompany_Edit.getTag()),e.getKey(),s.toString());
            }
        }
    }

    OnItemClick mClick;

    public interface OnItemClick {
        void onItemClick(int position);
        //接口回调，监听输入内容
        void loadingEdit(int position,String type, String string);

        //失去焦点的监听
        void overFoucesEdit(int postion,String type,String edit);

    }
    public void setOnItemClick(OnItemClick click) {
        mClick = click;
    }

}
