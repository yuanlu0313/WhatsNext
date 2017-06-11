package com.yl.yuanlu.whatsnext.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by LUYUAN on 6/5/2017.
 */

public class DateUtils {

    private static SimpleDateFormat sdf_date = new SimpleDateFormat("EEE, MMM dd, yyyy");
    private static SimpleDateFormat sdf_time = new SimpleDateFormat("HH:mm");

    public static String dateToString(Date date) {
        return sdf_date.format(date);
    }

    public static String timeToString(Date time) {
        return sdf_time.format(time);
    }

}
