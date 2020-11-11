package com.example.demo.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class DateUtil {
    private static Logger logger = LoggerFactory.getLogger(DateUtil.class);

    public static final long Second = 1000;
    public static final long Minute = 60 * Second;
    public static final long HalfHour = 30 * Minute;
    public static final long Hour = 60 * Minute;
    public static final long Day = 24 * Hour;

    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String YYYYMMDD_HHMMSS = "yyyyMMdd HHmmss";
    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static final String YYMMDD = "yyMMdd";
    public static final String HHMMSS = "HHmmss";
    public static final String YYYYMMDD = "yyyyMMdd";
    public static final String YYYYMM = "yyyyMM";
    public static final String YYYY = "yyyy";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYY__MM = "yyyy/MM";
    public static final String YYYY__MM__DD = "yyyy/MM/dd";
    public static final String YYYY__MM__DD__HH = "yyyy/MM/dd HH";
    public static final String YYYY__MM__DD__HH__MM__SS = "yyyy/MM/dd HH:mm:ss";


    public static int getTimeDifference(Date date1, Date date2) {
        long diff = date1.getTime() - date2.getTime();
        int hour = (int) (diff / (60 * 60 * 1000));
        return hour;
    }

}
