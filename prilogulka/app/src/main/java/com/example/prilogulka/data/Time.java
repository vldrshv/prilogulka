package com.example.prilogulka.data;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Time {

    public static String getTodayTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(new Date());
    }

    public static String getToday(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        return sdf.format(new Date());//toString(getDay()) + "." + toString(getMonth()) + "." + toString(getYear());
    }

    private static String toString(int value) { return value < 10 ? "0" + value : "" + value;}
    private static String toString(Date d) {
        return toString(d.getDay()) + "." + toString(d.getMonth()) + "." + toString(d.getYear() + 1900);
    }

    public static String getTimeWithDifference(long days) {
        Date date = new Date();
        long time = date.getTime();

        time -= days * 24 * 60 * 60 * 1000;
        date.setTime(time);

        return toString(date);
    }
}
