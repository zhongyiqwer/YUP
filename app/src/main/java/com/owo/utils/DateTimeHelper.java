package com.owo.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;

/**
 * 日期处理
 */
@SuppressLint("SimpleDateFormat")
public class DateTimeHelper {

    @SuppressLint("SimpleDateFormat")
    public static String FULL_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    public static String DATE_FORMAT_TILL_SECOND = "yyyy-MM-dd HH:mm:ss";
    public static SimpleDateFormat DATE_TILL_DAY_CURRENT_YEAR = new SimpleDateFormat(
            "MM-dd");
    public static SimpleDateFormat DATE_TILL_DAY_CH = new SimpleDateFormat(
            "yyyy-MM-dd");

    public  static String countdownMillis2FormatTime(long millisTime){
        long sec = millisTime % 60000 /1000;
        long min = millisTime /60000 %60;
        long h = millisTime / 3600000;
        Formatter formatter = new Formatter();
        return  formatter.format("%02d:%02d:%02d", new Object[]{Long.valueOf(h),
                Long.valueOf(min),Long.valueOf(sec)}).toString();
    }


    /**
     * 毫秒时间转格式时间
     *
     * @param timeMillis
     * @param format
     * @return
     */
    public static String timeMillis2FormatTime(String timeMillis, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar calendar = Calendar.getInstance();
        long time = Long.parseLong(timeMillis);
        calendar.setTimeInMillis(time);
        return sdf.format(calendar.getTime());
    }

    public static long FormatTime2timeMillis(String time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        long millionSeconds = 0;
        try {
            millionSeconds =
                    sdf.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return millionSeconds;
    }

    /**
     * 计算未来时间
     */
    public static String calFutureTime(int month) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        Date now = new Date();
        calendar.setTime(now);
        calendar.add(Calendar.MONTH, month);
        return sdf.format(calendar.getTime());
    }


    /**
     * 计算时间差（返回天数）
     *
     * @param timeMillis
     * @param format
     * @return
     */
    public static long caldeltaDays(String timeMillis, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar calendar = Calendar.getInstance();
        long time = Long.parseLong(timeMillis);
        calendar.setTimeInMillis(time);
        String formatTime = sdf.format(calendar.getTime());

        Date currentDate = new Date();
        String currentTime = sdf.format(currentDate);

        Date date1 = null;
        Date date2 = null;
        try {
            date1 = sdf.parse(formatTime);
            date2 = sdf.parse(currentTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date1 != null && date2 != null)
            return (date2.getTime() - date1.getTime()) / (24 * 3600 * 1000);
        else return -999999;
    }


    /**
     * 计算时间差
     *
     * @param time
     * @return
     */
    public static long deltaTime(String time) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentDate = new Date();
        String dateStr = df.format(currentDate);
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = df.parse(dateStr);
            d2 = df.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (d1 != null && d2 != null)
            return d2.getTime() - d1.getTime();
        else return -999999;
    }


    /**
     * 计算时间差
     *
     * @return
     */
    public static long deltaTime(String time1, String time2) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = df.parse(time1);
            d2 = df.parse(time2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (d1 != null && d2 != null)
            return d2.getTime() - d1.getTime();
        else return -999999;
    }


    /**
     * 日期字符串转换为Date
     *
     * @param dateStr
     * @param format
     * @return
     */
    public static Date strToDate(String dateStr, String format) {
        Date date = null;

        if (!TextUtils.isEmpty(dateStr)) {
            SimpleDateFormat df = new SimpleDateFormat(format);
            try {
                date = df.parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return date;
    }

    // 获取年份
    public static int getYear() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, 0);
        return c.get(Calendar.YEAR);
    }

    /**
     * 日期转换为字符串
     *
     * @param timeStr
     * @param format
     * @return
     */
    public static String dateToString(String timeStr, String format) {
        // 判断是否是今年
        Date date = strToDate(timeStr, format);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // 如果是今年的话，才取“xx月xx日”日期格式
        if (calendar.get(Calendar.YEAR) == getYear()) {
            return DATE_TILL_DAY_CURRENT_YEAR.format(date);
        }

        return DATE_TILL_DAY_CH.format(date);
    }

    /**
     * 日期逻辑
     *
     * @param dateStr 日期字符串
     * @return
     */
    public static String timeLogic(String dateStr, String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.get(Calendar.DAY_OF_MONTH);
        long now = calendar.getTimeInMillis();
        Date date = strToDate(dateStr, format);
        calendar.setTime(date);
        long past = calendar.getTimeInMillis();

        // 相差的秒数
        long time = (now - past) / 1000;

        StringBuffer sb = new StringBuffer();
        if (time > 0 && time < 60) { // 1小时内
            return sb.append(time + "秒前").toString();
        } else if (time > 60 && time < 3600) {
            return sb.append(time / 60 + "分钟前").toString();
        } else if (time >= 3600 && time < 3600 * 24) {
            return sb.append(time / 3600 + "小时前").toString();
        } else if (time >= 3600 * 24 && time < 3600 * 48) {
            return sb.append("昨天").toString();
        } else if (time >= 3600 * 48 && time < 3600 * 72) {
            return sb.append("前天").toString();
        } else if (time >= 3600 * 72) {
            return dateToString(dateStr, format);
        }
        return dateToString(dateStr, format);
    }

    /**
     * 获取当前日期
     *
     * @return 当前日期
     */
    public static String getCurrentDateTime(String format) {
        Date date = new Date();
        SimpleDateFormat dateFm = new SimpleDateFormat(format);
        return dateFm.format(date);
    }

    /**
     * 判断是否白天
     *
     * @return
     */
    public static boolean isDay() {
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.HOUR_OF_DAY) >= 6 && calendar.get(Calendar.HOUR_OF_DAY) <= 18) {
            return true;
        } else
            return false;

    }

    /**
     * 判断某一时间是否在一个区间内
     *
     * @param sourceTime 时间区间,半闭合,如[10:00-20:00)
     * @param curTime    需要判断的时间 如10:00
     * @return
     * @throws IllegalArgumentException
     */
    public static boolean isInTime(String sourceTime, String curTime) {
        if (sourceTime == null || !sourceTime.contains("-") || !sourceTime.contains(":")) {
            throw new IllegalArgumentException("Illegal Argument arg:" + sourceTime);
        }
        if (curTime == null || !curTime.contains(":")) {
            throw new IllegalArgumentException("Illegal Argument arg:" + curTime);
        }
        String[] args = sourceTime.split("-");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            long now = sdf.parse(curTime).getTime();
            long start = sdf.parse(args[0]).getTime();
            long end = sdf.parse(args[1]).getTime();
            if (args[1].equals("00:00")) {
                args[1] = "24:00";
            }
            if (end < start) {
                if (now >= end && now < start) {
                    return false;
                } else {
                    return true;
                }
            } else {
                if (now >= start && now < end) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Illegal Argument arg:" + sourceTime);
        }

    }
}
