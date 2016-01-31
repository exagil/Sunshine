package net.chiragaggarwal.android.sunshine.models;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import net.chiragaggarwal.android.sunshine.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Forecast implements Parcelable {
    private static final String TEMPERATURE = "temp";
    private static final String WEATHER = "weather";
    private static final String DATE = "dt";
    private static final String MINIMUM_TEMPERATURE = "min";
    private static final String MAXIMUM_TEMPERATURE = "max";
    private static final String MAIN_DESCRIPTION = "main";
    private static final String SEPERATOR = " - ";
    private static final String DAY_KEYWORD = "E";
    private static final String MONTH_NAME_KEYWORD = "MMMM";
    private static final String COMMA = ", ";
    private static final String DATE_KEYWORD = " d";
    private static final long ONE_THOUSAND_MILLISECONDS = 1000;
    public static final String TAG = "net.chiragaggarwal.android.sunshine.models.Forecast";
    private static final String SPACE = " ";

    private final Date date;
    private final Double minimumTemperature;
    private final String mainDescription;
    private final Double maximumTemperature;

    public Forecast(Date date, Double minimumTemperature, Double maximumTemperature, String mainDescription) {
        this.date = date;
        this.minimumTemperature = minimumTemperature;
        this.maximumTemperature = maximumTemperature;
        this.mainDescription = mainDescription;
    }

    public static Forecast fromJSON(JSONObject dayForecast) throws JSONException {
        JSONObject temperature = dayForecast.getJSONObject(TEMPERATURE);
        JSONObject weather = dayForecast.getJSONArray(WEATHER).getJSONObject(0);

        Date date = new Date(Long.parseLong(dayForecast.getString(DATE)) * ONE_THOUSAND_MILLISECONDS);
        Double minimumTemperature = temperature.getDouble(MINIMUM_TEMPERATURE);
        Double maximumTemperature = temperature.getDouble(MAXIMUM_TEMPERATURE);
        String mainDescription = weather.getString(MAIN_DESCRIPTION);
        return new Forecast(date, minimumTemperature, maximumTemperature, mainDescription);
    }

    public String summary() {
        return formattedDate() + SEPERATOR + this.mainDescription + SEPERATOR + formattedTemperatures();
    }

    public String summaryWithHashtag(Context context) {
        return summary() + SPACE + context.getString(R.string.hashtag);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(date != null ? date.getTime() : -1);
        dest.writeValue(this.minimumTemperature);
        dest.writeString(this.mainDescription);
        dest.writeValue(this.maximumTemperature);
    }

    public static final Creator<Forecast> CREATOR = new Creator<Forecast>() {
        public Forecast createFromParcel(Parcel source) {
            return new Forecast(source);
        }

        public Forecast[] newArray(int size) {
            return new Forecast[size];
        }
    };

    protected Forecast(Parcel in) {
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        this.minimumTemperature = (Double) in.readValue(Double.class.getClassLoader());
        this.mainDescription = in.readString();
        this.maximumTemperature = (Double) in.readValue(Double.class.getClassLoader());
    }

    private String formattedTemperatures() {
        return this.maximumTemperature + "/" + this.minimumTemperature;
    }

    private String formattedDate() {
        return new SimpleDateFormat(DAY_KEYWORD + COMMA + MONTH_NAME_KEYWORD + DATE_KEYWORD,
                Locale.US).format(this.date);
    }
}
