package com.example.prilogulka.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
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
    public static String parseServerTime(String serverTime) {
        // yyyy-mm-ddThh:mm:ss.ZONE
        String dateTime[] = serverTime.split(" ");
        if (dateTime.length == 1)
            dateTime = serverTime.split("T");

        String date[] = dateTime[0].split("-");
        String time[] = dateTime[1].split("\\.");

        return String.format("%s-%s-%s %s", date[2], date[1], date[0], time[0]);
    }

    public static boolean isAdult(String dateS) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        try {
            Date date = sdf.parse(dateS);
            int day = date.getDate();
            int month = date.getMonth();
            int year = date.getYear() + 1900;
            System.out.println("b_date = " + date.getDate() + "." + date.getMonth() + "." + (date.getYear() + 1900));

            Date dateNow = new Date();
            int nDay = dateNow.getDate();
            int nMonth = dateNow.getMonth();
            int nYear = dateNow.getYear() + 1900;
            System.out.println("now = " + nDay + "." + nMonth + "." + nYear);

            if (year + 18 < nYear)
                return true;
            else if (year + 18 == nYear) {
                System.out.println("year");
                if (month < nMonth)
                    return true;
                else if (month == nMonth) {
                    System.out.println("month");
                    if (day + 1 <= nDay) {
                        System.out.println("day");
                        return true;
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }
}
