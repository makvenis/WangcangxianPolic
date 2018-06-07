package com.makvenis.dell.wangcangxianpolic.help;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;

/* 作者  王从文 */
/* 全局采用注解模式 */
/* 旺苍县公安局巡防系统 百度地图服务定位 */

/**
 * @// STOPSHIP: 2018/4/13
 * @ Content: 首页加载
 * @ 解释: 通过每个页面的加载或者使用BaseActivity来实现定位
 * @ 定位采用回调方式 回调定位结果
 * @
 */

/**
 * Android定位SDK自v7.2版本起，对外提供了Abstract类型的监听接口BDAbstractLocationListener*，用于实现定位监听。原有BDLocationListener暂时保留，推荐开发者升级到Abstract类型的新监听接口使用，
 * 该接口会异步获取定位结果，核心代* 码如下：
 */

/**
 * @ 参考地址:http://lbsyun.baidu.com/index.php?title=android-locsdk/guide/get-location/latlng
 *
 */

public class SimpleBaiDuMap extends BDAbstractLocationListener {

    /* 接口类 */
    private GetOnClinkMapListener mMapListener;
    /* 接口Set方法 */
    public void GetOnClinkMapListener(GetOnClinkMapListener mGetOnClinkMapListener) {
        this.mMapListener = mGetOnClinkMapListener;
    }

    @Override
    public void onReceiveLocation(BDLocation location) {
        //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
        //以下只列举部分获取经纬度相关（常用）的结果信息
        //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

        double x = location.getLatitude();    //获取纬度信息
        double y = location.getLongitude();    //获取经度信息
        float radius = location.getRadius();    //获取定位精度，默认值为0.0f

        String coorType = location.getCoorType();
        //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准

        String addr = location.getAddrStr();    //获取详细地址信息
        String country = location.getCountry();    //获取国家
        String province = location.getProvince();    //获取省份
        String city = location.getCity();    //获取城市
        String district = location.getDistrict();    //获取区县
        String street = location.getStreet();    //获取街道信息
        String locationDescribe = location.getLocationDescribe();    //获取位置描述信息

        int errorCode = location.getLocType();
        //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明

        /* 开始回调 */
        mMapListener.showMap(x,y);
        /* 拼接详细地址 */
        String dismisPath=addr+street+locationDescribe;
        mMapListener.showPath(dismisPath);
        mMapListener.showErrorCode(errorCode);
    }

    public interface GetOnClinkMapListener{
        /* 回调经纬度 x y */
        void showMap(double x,double y);
        /* 回调具体的地址 四川省广元市苍溪县文昌镇双庙村三组油坊下 */
        void showPath(String path);
        /* 回调获取错误的时候的错误码 */
        void showErrorCode(int error);
    }


}
