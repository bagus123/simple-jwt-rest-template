package com.tbs.app.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class DateFormatUtil {

    public static String format(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        return df.format(date);

    }
    
}
