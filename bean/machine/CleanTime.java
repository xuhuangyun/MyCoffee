package com.fancoff.coffeemaker.bean.machine;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by apple on 2018/4/11.
 */

public class CleanTime {
    String time;

    public String getTime() {
        Date date = null;
        try {
            date = new SimpleDateFormat("HH:mm").parse(time);
            String now = new SimpleDateFormat("HH:mm").format(date);
            return now;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "CleanTime{" +
                "time='" + time + '\'' +
                '}';
    }
}
