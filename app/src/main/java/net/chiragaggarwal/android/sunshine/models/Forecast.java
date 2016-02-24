package net.chiragaggarwal.android.sunshine.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import net.chiragaggarwal.android.sunshine.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static net.chiragaggarwal.android.sunshine.data.ForecastContract.ForecastEntry;

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
    public static final String TAG = "net.chiragaggarwal.android.sunshine.models.Forecast";
    private static final String SPACE = " ";
    private static final String DEGREES = "deg";
    private static final String HUMIDITY = "humidity";
    private static final String PRESSURE = "pressure";
    private static final String WIND_SPEED = "speed";
    private static final String WEATHER_ID = "id";
    private static final long ONE_THOUSAND_MILLISECONDS = 1000;
    private static final String DD_MM_YYYY = "ddMMyyyy";
    private static final String DAY_PATTERN = "EEEE";
    private static final String TOMORROW = "Tomorrow";
    private static final String TODAY = "Today";
    private static final String YESTERDAY = "Yesterday";
    private static final String HECTOPASCAL_PRESSURE_UNIT = " hPa";
    private static final String KMPH_NORTH_WIND_UNIT = " km/h NW";
    private static final String PERCENT_SYMBOL = "%";
    private static final String WEATHER_ICON = "icon";
    private static final int NO_ICON_RESOURCE = 0;

    private final Date date;
    private final Double minimumTemperature;
    public final String mainDescription;
    private Double degrees;
    private Double humidity;
    private Double pressure;
    private Double windSpeed;
    private Long weatherId;
    private String icon;
    private final Double maximumTemperature;

    public Forecast(Date date, Double minimumTemperature, Double maximumTemperature,
                    String mainDescription, Double degrees, Double humidity,
                    Double pressure, Double windSpeed, Long weatherId, String icon) {

        this.date = date;
        this.minimumTemperature = minimumTemperature;
        this.maximumTemperature = maximumTemperature;
        this.mainDescription = mainDescription;
        this.degrees = degrees;
        this.humidity = humidity;
        this.pressure = pressure;
        this.windSpeed = windSpeed;
        this.weatherId = weatherId;
        this.icon = icon;
    }

    public static Forecast fromJSON(JSONObject dayForecast) throws JSONException {
        JSONObject temperature = dayForecast.getJSONObject(TEMPERATURE);
        JSONObject weather = dayForecast.getJSONArray(WEATHER).getJSONObject(0);
        long extractedDateInMillisecondsFrom1970 = extractedUnixTimestampInSeconds(dayForecast) * ONE_THOUSAND_MILLISECONDS;

        Date date = new Date(extractedDateInMillisecondsFrom1970);
        Double minimumTemperature = temperature.getDouble(MINIMUM_TEMPERATURE);
        Double maximumTemperature = temperature.getDouble(MAXIMUM_TEMPERATURE);
        String mainDescription = weather.getString(MAIN_DESCRIPTION);
        Double degrees = dayForecast.getDouble(DEGREES);
        Double humidity = dayForecast.getDouble(HUMIDITY);
        Double pressure = dayForecast.getDouble(PRESSURE);
        Double windSpeed = dayForecast.getDouble(WIND_SPEED);
        Long weatherId = weather.getLong(WEATHER_ID);
        String icon = weather.getString(WEATHER_ICON);

        return new Forecast(date, minimumTemperature, maximumTemperature, mainDescription, degrees,
                humidity, pressure, windSpeed, weatherId, icon);
    }

    public static Forecast fromCursor(Cursor forecastsCursor) throws ParseException {
        int dateIndex = forecastsCursor.getColumnIndex(ForecastEntry.COLUMN_DATE);
        int minimumTemperatureIndex = forecastsCursor.getColumnIndex(ForecastEntry.COLUMN_MIN_TEMP);
        int maximumTemperatureIndex = forecastsCursor.getColumnIndex(ForecastEntry.COLUMN_MAX_TEMP);
        int degreesIndex = forecastsCursor.getColumnIndex(ForecastEntry.COLUMN_DEGREES);
        int humidityIndex = forecastsCursor.getColumnIndex(ForecastEntry.COLUMN_HUMIDITY);
        int pressureIndex = forecastsCursor.getColumnIndex(ForecastEntry.COLUMN_PRESSURE);
        int mainDescriptionIndex = forecastsCursor.getColumnIndex(ForecastEntry.COLUMN_SHORT_DESC);
        int weatherIdIndex = forecastsCursor.getColumnIndex(ForecastEntry.COLUMN_WEATHER_ID);
        int windSpeedIndex = forecastsCursor.getColumnIndex(ForecastEntry.COLUMN_WIND_SPEED);
        int iconIndex = forecastsCursor.getColumnIndex(ForecastEntry.COLUMN_ICON);

        String dateString = forecastsCursor.getString(dateIndex);
        Date date = new SimpleDateFormat(DD_MM_YYYY).parse(dateString);
        Double minimumTemperature = forecastsCursor.getDouble(minimumTemperatureIndex);
        Double maximumTemperature = forecastsCursor.getDouble(maximumTemperatureIndex);
        String mainDescription = forecastsCursor.getString(mainDescriptionIndex);
        Double degrees = forecastsCursor.getDouble(degreesIndex);
        Double humidity = forecastsCursor.getDouble(humidityIndex);
        Double pressure = forecastsCursor.getDouble(pressureIndex);
        Double windSpeed = forecastsCursor.getDouble(windSpeedIndex);
        Long weatherId = forecastsCursor.getLong(weatherIdIndex);
        String icon = forecastsCursor.getString(iconIndex);

        return new Forecast(date, minimumTemperature, maximumTemperature, mainDescription, degrees,
                humidity, pressure, windSpeed, weatherId, icon);
    }

    public ContentValues toContentValues(Long locationRowId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ForecastEntry.COLUMN_MAX_TEMP, maximumTemperature);
        contentValues.put(ForecastEntry.COLUMN_MIN_TEMP, minimumTemperature);
        contentValues.put(ForecastEntry.COLUMN_DATE, persistableDate());
        contentValues.put(ForecastEntry.COLUMN_SHORT_DESC, mainDescription);
        contentValues.put(ForecastEntry.COLUMN_LOC_KEY, locationRowId);
        contentValues.put(ForecastEntry.COLUMN_DEGREES, degrees);
        contentValues.put(ForecastEntry.COLUMN_HUMIDITY, humidity);
        contentValues.put(ForecastEntry.COLUMN_PRESSURE, pressure);
        contentValues.put(ForecastEntry.COLUMN_WIND_SPEED, windSpeed);
        contentValues.put(ForecastEntry.COLUMN_WEATHER_ID, weatherId);
        contentValues.put(ForecastEntry.COLUMN_ICON, icon);
        return contentValues;
    }

    public String summary() {
        return formattedDate() + SEPERATOR + this.mainDescription + SEPERATOR + formattedTemperatures();
    }

    public String summaryWithHashtag(Context context) {
        return summary() + SPACE + context.getString(R.string.hashtag);
    }

    public String formattedMaximumTemperature(Context context) {
        return String.valueOf(this.maximumTemperature) + SPACE + context.getString(R.string.degrees_symbol);
    }

    public String formattedMinimumTemperature(Context context) {
        return String.valueOf(this.minimumTemperature) + SPACE + context.getString(R.string.degrees_symbol);
    }

    public String friendlyDay(Date todaysDate) {
        SimpleDateFormat dayFormat = new SimpleDateFormat(DAY_PATTERN);
        String todaysDayString = dayFormat.format(todaysDate);
        String forecastDayString = dayFormat.format(this.date);

        Day todaysDay = Day.parse(todaysDayString);
        Day forecastDay = Day.parse(forecastDayString);

        if (todaysDay == forecastDay) return TODAY;
        else if (todaysDay.next() == forecastDay) return TOMORROW;

        return forecastDayString;
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
        dest.writeValue(this.degrees);
        dest.writeValue(this.humidity);
        dest.writeValue(this.pressure);
        dest.writeValue(this.windSpeed);
        dest.writeValue(this.weatherId);
        dest.writeValue(this.maximumTemperature);
    }

    protected Forecast(Parcel in) {
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        this.minimumTemperature = (Double) in.readValue(Double.class.getClassLoader());
        this.mainDescription = in.readString();
        this.degrees = (Double) in.readValue(Double.class.getClassLoader());
        this.humidity = (Double) in.readValue(Double.class.getClassLoader());
        this.pressure = (Double) in.readValue(Double.class.getClassLoader());
        this.windSpeed = (Double) in.readValue(Double.class.getClassLoader());
        this.weatherId = (Long) in.readValue(Long.class.getClassLoader());
        this.maximumTemperature = (Double) in.readValue(Double.class.getClassLoader());
    }

    public static final Creator<Forecast> CREATOR = new Creator<Forecast>() {
        public Forecast createFromParcel(Parcel source) {
            return new Forecast(source);
        }

        public Forecast[] newArray(int size) {
            return new Forecast[size];
        }
    };

    private static long extractedUnixTimestampInSeconds(JSONObject dayForecast) throws JSONException {
        return dayForecast.getLong(DATE);
    }

    private String formattedTemperatures() {
        return this.maximumTemperature + "/" + this.minimumTemperature;
    }

    public String formattedDate() {
        return new SimpleDateFormat(DAY_KEYWORD + COMMA + MONTH_NAME_KEYWORD + DATE_KEYWORD,
                Locale.US).format(this.date);
    }

    private long persistableDate() {
        SimpleDateFormat persistableDateFormat = new SimpleDateFormat(DD_MM_YYYY);
        String formattedPersistableDate = persistableDateFormat.format(this.date);
        return Long.parseLong(formattedPersistableDate);
    }

    public String pressureInHectopascal() {
        return this.pressure + HECTOPASCAL_PRESSURE_UNIT;
    }

    public String northWindSpeedInKmph() {
        return this.windSpeed + KMPH_NORTH_WIND_UNIT;
    }

    public String humidityInPercentage() {
        return this.humidity + PERCENT_SYMBOL;
    }

    public int iconResource() {
        int iconResource;
        switch (icon) {
            case "01d":
                iconResource = R.drawable.ic_clear;
                break;
            case "02d":
                iconResource = R.drawable.ic_light_clouds;
                break;
            case "03d":
                iconResource = R.drawable.ic_cloudy;
                break;
            case "04d":
                iconResource = R.drawable.ic_cloudy;
                break;
            case "09d":
                iconResource = R.drawable.ic_rain;
                break;
            case "10d":
                iconResource = R.drawable.ic_light_rain;
                break;
            case "11d":
                iconResource = R.drawable.ic_storm;
                break;
            case "13d":
                iconResource = R.drawable.ic_snow;
                break;
            case "50d":
                iconResource = R.drawable.ic_fog;
                break;
            default:
                iconResource = NO_ICON_RESOURCE;
                break;
        }
        return iconResource;
    }

    public int iconArt() {
        int iconResource;
        switch (icon) {
            case "01d":
                iconResource = R.drawable.art_clear;
                break;
            case "02d":
                iconResource = R.drawable.art_light_clouds;
                break;
            case "03d":
                iconResource = R.drawable.art_clouds;
                break;
            case "04d":
                iconResource = R.drawable.art_clouds;
                break;
            case "09d":
                iconResource = R.drawable.art_rain;
                break;
            case "10d":
                iconResource = R.drawable.art_light_rain;
                break;
            case "11d":
                iconResource = R.drawable.art_storm;
                break;
            case "13d":
                iconResource = R.drawable.art_snow;
                break;
            case "50d":
                iconResource = R.drawable.art_fog;
                break;
            default:
                iconResource = NO_ICON_RESOURCE;
                break;
        }
        return iconResource;
    }
}
