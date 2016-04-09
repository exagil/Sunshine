package net.chiragaggarwal.android.sunshine.Utils;

import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utility {
    private static final String YYYY_MM_DD = "yyyyMMdd";

    @NonNull
    public static String parsedCurrentDateArgument() {
        SimpleDateFormat currentDateFormat = new SimpleDateFormat(YYYY_MM_DD);
        String formattedCurrentDate = currentDateFormat.format(new Date());
        return formattedCurrentDate;
    }
}
