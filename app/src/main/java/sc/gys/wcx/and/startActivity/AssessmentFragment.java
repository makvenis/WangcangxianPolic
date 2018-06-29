package sc.gys.wcx.and.startActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import sc.gys.wcx.and.R;
import sc.gys.wcx.and.newdbhelp.AppMothedHelper;
import sc.gys.wcx.and.tools.Configfile;

import java.util.Map;

/* 作者  王从文 */
/* 全局采用注解模式 */
/* 旺苍县公安局巡防系统 首页界面 */

public class AssessmentFragment extends Fragment {

    @ViewInject(R.id.test_fz)
    private TextView test_fz; //赋值
    @ViewInject(R.id.test_xr)
    EditText mEditText;
    @ViewInject(R.id.test_cz)
    Button mButton_in;
    @ViewInject(R.id.test_cx) // test_cxtest
    Button mButton_out;

    @ViewInject(R.id.test_cxtest) //
    Button mButton_cxtest;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_page_assessment,null);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mButton_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = mEditText.getText().toString();
                AppMothedHelper helper=new AppMothedHelper(getContext());

                boolean dismisData = helper.isDismisData(getContext(),"test");

                if(dismisData == true){
                    helper.update("test",s);
                }else {
                    helper.dbInsert(new String[]{"test", s});
                }
            }
        });


        mButton_cxtest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppMothedHelper helper=new AppMothedHelper(getContext());
                Map<Object, Object> map = helper.queryByKey(Configfile.USER_DATA_KEY);
                String data = (String) map.get("data");
                test_fz.setText(data);
            }
        });

        mButton_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppMothedHelper helper=new AppMothedHelper(getContext());
                Map<Object, Object> map = helper.queryByKey(Configfile.FORM_GET_TABLE_1);
                String data = (String) map.get("data");
                test_fz.setText(data);
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewUtils.inject(this,view);
    }
}
