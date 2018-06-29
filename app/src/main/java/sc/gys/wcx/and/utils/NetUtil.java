package sc.gys.wcx.and.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * {@link NetUtil} 动态检测网络变化 废弃使用 {@link AppJudiment #isNetworkConnected}
 *
 */

public class NetUtil {

    /* 网络判断 */
    @Deprecated
    public static int boolenNet(Context context){

        int netType = 0;//没有网络
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        /* 如果没有网络 这里经过调试发现返回的是null */
        if (networkInfo == null) {
            return netType;
        }

        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = 1;// wifi
        } else if (nType == ConnectivityManager.TYPE_MOBILE) {
            int nSubType = networkInfo.getSubtype();
            TelephonyManager mTelephony = (TelephonyManager) context
                    .getSystemService(context.TELEPHONY_SERVICE);
            if (nSubType == TelephonyManager.NETWORK_TYPE_UMTS
                    && !mTelephony.isNetworkRoaming()) {
                netType = 2;// 3G网络
            } else {
                netType = 3;// 2G网络
            }
        }
        return netType;
    }



}
