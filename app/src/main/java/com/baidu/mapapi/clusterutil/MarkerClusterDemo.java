package com.baidu.mapapi.clusterutil;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import com.baidu.mapapi.clusterutil.clustering.ClusterManager;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.owo.module_b_home.bean.BeanTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/27.
 * 实现点聚合功能，可以写在其他地方，如map3d.tools下
 * 要获得所有marker数据并进行聚合，然后把聚合后的结果显示在地图上
 * 还要为聚合后的marker添加点击事件
 */

public class MarkerClusterDemo implements BaiduMap.OnMapLoadedCallback{

    List<MyItem> items = new ArrayList<MyItem>();

    MapStatus ms;

    private static ClusterManager<MyItem> mClusterManager;

    BaiduMap mBaiduMap ;

    List<BeanTask> txtLists = null;
    Context context;

    private int size = 15;

    private volatile static MarkerClusterDemo instance;

    private MarkerClusterDemo() {}

    //单列设计模式
    public static MarkerClusterDemo getSingleton() {
        if (instance== null) {
            synchronized (MarkerClusterDemo.class) {
                if (instance == null) {
                    instance = new MarkerClusterDemo();
                }
            }
        }
        return instance;
    }

    public void init(Context context,BaiduMap baiduMap){
        this.context =  context;
        this.mBaiduMap = baiduMap;
        if (mClusterManager==null){
            mClusterManager = new ClusterManager<MyItem>(context, mBaiduMap);
        }
    }
    public void updataMap(){
        /*if (size == 15){

            size = 16;
        }else {
            ms = new MapStatus.Builder().zoom(size).build();
            size = 15;
        }*/
        ms = new MapStatus.Builder().zoom(15).build();
        mBaiduMap.setOnMapLoadedCallback(this);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
    }
    public ClusterManager<MyItem> getClusterManager(){
        return mClusterManager;
    }

    @Override
    public void onMapLoaded() {
        // TODO Auto-generated method stub
        ms = new MapStatus.Builder().zoom(16).build();
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
    }

   /* public void init(){
        //mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
        // 定义点聚合管理类ClusterManager
        if (mClusterManager==null){
            mClusterManager = new ClusterManager<MyItem>(context, mBaiduMap);
        }
        // 添加Marker点
        addMarkerCluster();
        //后面的点击事件在FragHomeNormal中展现
    }*/




}
