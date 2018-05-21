package com.makvenis.dell.wangcangxianpolic.activity;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.util.Log;


public abstract class SimpleLayoutManagerOnScrollListener extends OnScrollListener {

    //声明一个LinearLayoutManager
    /*通过管理器来获取是否到底部*/
    private LinearLayoutManager manager;

    //在屏幕上可见的item数量
    private int visibleItemCount;

    //已经加载出来的Item的数量
    private int totalItemCount;

    //在屏幕可见的Item中的第一个
    private int firstVisibleItem;

    //是否正在上拉数据
    private boolean loading = true;

    //主要用来存储上一个totalItemCount
    private int historyTotal = 0;

    //当前页，从0开始
    private int currentPage = 0;
    public SimpleLayoutManagerOnScrollListener(LinearLayoutManager manager) {
        this.manager = manager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        //获取屏幕上面能看得到的Item个数
        visibleItemCount=recyclerView.getChildCount();
        //通过管理器获取已经加载出来的Item个数
        totalItemCount=manager.getItemCount();
        //通过管理器获取屏幕上面第一个可以看见的Item
        firstVisibleItem=manager.findFirstVisibleItemPosition();

        if(loading){
            Log.d("TAG","firstVisibleItem: " +firstVisibleItem); //第一个可以看见Item
            Log.d("TAG","totalPageCount:" +totalItemCount);      //管理器加载出来的Item个数
            Log.d("TAG", "visibleItemCount:" + visibleItemCount);//在屏幕上面可以看见的Item个数
            //如果管理器加载出来的数据大于上一次管理器加载出来的数据 说明加载数据了
            if(totalItemCount > historyTotal){
                //说明数据已经加载结束
                loading = false;
                historyTotal = totalItemCount;
            }
        }

        //这里需要好好理解
        //屏幕中第一个Item的序号（postion）大于 管理器加载出来的数据减去屏幕可见的Item个数说明到达了底部
        if (!loading && totalItemCount-visibleItemCount <= firstVisibleItem){
            currentPage ++;
            onLoadMore(currentPage);
            loading = true;
        }
    }
    /**
     * 提供一个抽闲方法，在Activity中监听到这个SimpleEndOnScrollListener
     * 并且实现这个方法
     * */
    public abstract void onLoadMore(int currentPage);


}
