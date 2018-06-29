package sc.gys.wcx.and.help;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/* 作者  王从文 */
/* 全局采用注解模式 */
/* 旺苍县公安局巡防系统 授权界面 */

/**
 * @ 解释: 当系统版本大于6.0 或者 Version版本高于23.0 需要动态授权 Android再6.0之后增加了对
 *        隐私的更强管理
 * @ 注意: 当在动态授权的同时，需要再Main...xml 里面也要静态授权
 * @ 增加: 如果需要回调权限 重写onRequestPermissionsResult() 方法
 * @ 使用方法: SetPermissionForNormal(this)
 */

public class PermissionsUtils {

    /* 动态授权 */
    public void SetPermissionForNormal(final Activity mActivity) {
        //由于6.0以下不需要动态申请权限,系统会自动调用所有权限申请好的回调(这样就包括了6.0以下的版本)
        //权限申请
        //判断此权限系统是否同意
        if ( ContextCompat.checkSelfPermission(mActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(mActivity,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            //若不同意,申请权限
            ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE}, 100);
        }
        //若系统低于6.0或者高于6.0已经同意所有权限,执行此回调
        //若版本低于6.0或者版本高于6.0权限申请成功
        //需要调用initView()方法
        if (ContextCompat.checkSelfPermission(mActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(mActivity,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(mActivity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(mActivity,
                        Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            //权限申请成功执行操作
            //doSomething
        }
    }


}
