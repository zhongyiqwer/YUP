package com.owo.utils;

/**
 * Created by ppssyyy on 2017-03-23.
 * 地图计算类
 */
public class MapCalculator {
    private static double EARTH_RADIUS = 6378137;

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 根据两个位置的经纬度，来计算两地的距离（单位为M）
     * 参数为String类型
     *
     * @param lat1Str 用户A经度
     * @param lng1Str 用户A纬度
     * @param lat2Str 用户B经度
     * @param lng2Str 用户B纬度
     * @return
     */
    public static double getDistance(String lat1Str, String lng1Str, String lat2Str, String lng2Str) {
        if (lat1Str.trim().length()==0) lat1Str="0";
        if (lng1Str.trim().length()==0) lng1Str="0";
        if (lat2Str.trim().length()==0) lat2Str="0";
        if (lng2Str.trim().length()==0) lng2Str="0";
        Double lat1 = Double.parseDouble(lat1Str);
        Double lng1 = Double.parseDouble(lng1Str);
        Double lat2 = Double.parseDouble(lat2Str);
        Double lng2 = Double.parseDouble(lng2Str);

        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double difference = radLat1 - radLat2;
        double mdifference = rad(lng1) - rad(lng2);
        double distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(difference / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(mdifference / 2), 2)));
        distance = distance * EARTH_RADIUS;
        return distance;
    }

    /**
     * 判断物体是否在圈子里
     *
     * @param cLat   圈子经度
     * @param cLong  圈子纬度
     * @param radius 圈子半径
     * @param olat   物体经度
     * @param olong  物体纬度
     * @return
     */
    public static boolean isInCircle(String cLat, String cLong, int radius, String olat, String olong) {
        if (radius != 0) {
            double distance = 0;
            distance = getDistance(cLat, cLong, olat, olong);
            System.out.println("DDDDDDDDDDDDDDISTANCE=" + distance);
            return distance > (double) radius ? false : (distance == (double) radius ? true : true);
        } else return false;


    }

}
