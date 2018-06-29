package sc.gys.wcx.and.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import sc.gys.wcx.and.R;


/**
 * 自定义弹出框 确认 取消两个点击事件都必须重写
 */

public class SimpleDialogSureView extends Dialog {

    private Context context;
    private String message;
    private setOnclinkDialogCancelListener onclinkDialogCancelListener;
    private setOnclinkDialogSureListener onclinkDialogSureListener;


    public void setMessage(String message) {
        this.message = message;
    }

    public SimpleDialogSureView(@NonNull Context context) {
        super(context);this.context=context;
    }

    public SimpleDialogSureView(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);this.context=context;
    }

    protected SimpleDialogSureView(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);this.context=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = LayoutInflater.from(context).inflate(R.layout.layout_user_ui_dialog_sure_style, null);
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
        dialog_message = ((TextView) view.findViewById(R.id.dialog_message));

        dialog_message.setText(message);
        submit_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclinkDialogCancelListener.OnClinkCancelListener();
            }
        });

        sumit_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclinkDialogSureListener.OnClinkSureListener();
            }
        });

    }


    public interface setOnclinkDialogSureListener{
        Void OnClinkSureListener();
    }
    public interface setOnclinkDialogCancelListener{
        Void OnClinkCancelListener();
    }
}
