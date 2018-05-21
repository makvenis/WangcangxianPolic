package com.makvenis.dell.wangcangxianpolic.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.makvenis.dell.wangcangxianpolic.R;

import java.util.Calendar;


/**
 * 自定义弹出框 确认 取消两个点击事件都必须重写
 */

public class SimpleDialogTimerView extends Dialog {

    private Context context;
    private DatePicker dmDatePicker;
    private setOnclinkDialogCancelListener onclinkDialogCancelListener;
    private setOnclinkDialogSureListener onclinkDialogSureListener;


    public void setDmDatePicker(DatePicker dmDatePicker) {
        this.dmDatePicker = dmDatePicker;
    }

    public SimpleDialogTimerView(@NonNull Context context) {
        super(context);this.context=context;
    }

    public SimpleDialogTimerView(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);this.context=context;
    }

    protected SimpleDialogTimerView(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);this.context=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = LayoutInflater.from(context).inflate(R.layout.layout_user_ui_dialog_timer_style, null);
        setContentView(view);
        innt(view);

    }
    public TextView dialog_message,sumit_sure,submit_cancel;

    public void setOnclinkDialogCancelListener(setOnclinkDialogCancelListener onclinkDialogCancelListener) {
        this.onclinkDialogCancelListener = onclinkDialogCancelListener;
    }

    public void setOnclinkDialogSureListener(setOnclinkDialogSureListener onclinkDialogSureListener) {
        this.onclinkDialogSureListener = onclinkDialogSureListener;
    }

    private void innt(View view) {
        sumit_sure = ((TextView) view.findViewById(R.id.sumit_sure));
        submit_cancel = ((TextView) view.findViewById(R.id.submit_cancel));
        dmDatePicker = ((DatePicker) view.findViewById(R.id.timer));




        submit_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclinkDialogCancelListener.OnClinkCancelListener();
            }
        });

        sumit_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                dmDatePicker.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String nowTime = year + "年"
                                + (monthOfYear + 1) + "月" + dayOfMonth + "日";
                        onclinkDialogSureListener.OnClinkSureListener(nowTime);
                    }
                });

            }
        });

    }


    public interface setOnclinkDialogSureListener{
        Void OnClinkSureListener(String s);
    }
    public interface setOnclinkDialogCancelListener{
        Void OnClinkCancelListener();
    }
}
