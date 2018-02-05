package com.baidu.mapapi.clusterutil;

import android.os.Bundle;

import com.baidu.mapapi.clusterutil.clustering.ClusterItem;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.model.LatLng;
import com.wao.dogcat.R;

/**
 * Created by Administrator on 2018/2/1.
 * 每个Marker点，包含Marker点坐标以及图标
 */

public class MyItem implements ClusterItem {
    private LatLng mPosition;

    private int taskType;

    Bundle bundle;

    public MyItem(LatLng latLng,int taskType,Bundle bundle) {
        this.mPosition = latLng;
        this.taskType = taskType;
        this.bundle = bundle;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    public Bundle getBundle(){
        return bundle;
    }

    @Override
    public BitmapDescriptor getBitmapDescriptor() {
        BitmapDescriptor bitmapDescriptor = null;
        // 根据类型构建mark图标
        if ( taskType==2) {
            bitmapDescriptor = (BitmapDescriptorFactory
                    .fromResource(R.drawable.map_reality));
        } else if ( taskType==1) {
            bitmapDescriptor = (BitmapDescriptorFactory
                    .fromResource(R.drawable.map_online));
        } else if ( taskType==3){
            bitmapDescriptor = (BitmapDescriptorFactory
                    .fromResource(R.drawable.map_immediately));
        }
        return bitmapDescriptor;
    }
}
