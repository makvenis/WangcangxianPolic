package sc.gys.wcx.and.newsnotescheck;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import sc.gys.wcx.and.R;


public class ChineseMedicineReportAdapter extends BaseAdapter {

    Context context;
    Question quesions;

    private OnCallBackCheck mOnCallBackCheck;

    public void setmOnCallBackCheck(OnCallBackCheck mOnCallBackCheck) {
        this.mOnCallBackCheck = mOnCallBackCheck;
    }

    public ChineseMedicineReportAdapter(Context context, Question quesions) {
        this.context = context;
        this.quesions = quesions;
    }

    @Override
    public int getCount() {
        return quesions.getAnswer().size();
    }

    @Override
    public Object getItem(int position) {
        return quesions.getAnswer().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_answer_list, null);
            RadioButton check_yes = (RadioButton) convertView.findViewById(R.id.check_yes);
            RadioButton check_no = (RadioButton) convertView.findViewById(R.id.check_no);
            holder = new ViewHolder();
            //holder.tvText=tvText;
            holder.check_no = check_no;
            holder.check_yes= check_yes;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Question.Answer answer = quesions.getAnswer().get(position);
        holder.check_yes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Configfile.Log(context,"选中---是");
                mOnCallBackCheck.OnClinkCheckBool("是");
            }
        });

        holder.check_no.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Configfile.Log(context,"选中---否");
                mOnCallBackCheck.OnClinkCheckBool("否");
            }
        });

        return convertView;
    }

    static class ViewHolder {
        //TextView tvText;
        RadioButton check_yes,check_no;
    }

    /* 回调数据结果 */
    public interface OnCallBackCheck{
        void OnClinkCheckBool(String s);
    }


}
