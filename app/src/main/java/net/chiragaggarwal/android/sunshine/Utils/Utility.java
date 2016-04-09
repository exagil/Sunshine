package net.chiragaggarwal.android.sunshine.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import net.chiragaggarwal.android.sunshine.R;

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

    public static String savedZipCode(Context context, SharedPreferences sharedPreferences) {
        String preferenceZipCodeKey = context.getString(R.string.preference_zip_code_key);
        return sharedPreferences.getString(preferenceZipCodeKey, "");
    }

    public static String savedTemperatureUnit(Context context, SharedPreferences sharedPreferences) {
        String preferenceTemperatureUnitKey = context.getString(R.string.preference_temperature_unit_key);
        return sharedPreferences.getString(preferenceTemperatureUnitKey, "");
    }

    public static String savedCountryCode(Context context, SharedPreferences sharedPreferences) {
        String preferenceCountryCodeKey = context.getString(R.string.preference_country_code_key);
        return sharedPreferences.getString(preferenceCountryCodeKey, "");
    }
}
