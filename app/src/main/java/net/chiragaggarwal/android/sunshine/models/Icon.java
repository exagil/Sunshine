package net.chiragaggarwal.android.sunshine.models;

// Icon represents a forecast's visual representation
// which is mapped to a forecast description using a code

public enum Icon {
    CLEAR_SKY, LIGHT_CLOUDS, CLOUDY, RAIN, LIGHT_RAIN, STORM, SNOW, FOG, CLEAR_SKY_NIGHT,
    LIGHT_CLOUDS_NIGHT, CLOUDY_NIGHT, RAIN_NIGHT, LIGHT_RAIN_NIGHT, STORM_NIGHT, SNOW_NIGHT,
    FOG_NIGHT;

    public static Icon parse(String iconCode) {
        Icon icon = null;
        switch (iconCode) {
            case "01d":
                icon = Icon.CLEAR_SKY;
                break;
            case "01n":
                icon = Icon.CLEAR_SKY_NIGHT;
                break;
            case "02d":
                icon = Icon.LIGHT_CLOUDS;
                break;
            case "02n":
                icon = Icon.LIGHT_CLOUDS_NIGHT;
                break;
            case "03d":
                icon = Icon.CLOUDY;
                break;
            case "03n":
                icon = Icon.CLOUDY_NIGHT;
                break;
            case "04d":
                icon = Icon.CLOUDY;
                break;
            case "04n":
                icon = Icon.CLOUDY_NIGHT;
                break;
            case "09d":
                icon = Icon.RAIN;
                break;
            case "09n":
                icon = Icon.RAIN_NIGHT;
                break;
            case "10d":
                icon = Icon.LIGHT_RAIN;
                break;
            case "10n":
                icon = Icon.LIGHT_RAIN_NIGHT;
                break;
            case "11d":
                icon = Icon.STORM;
                break;
            case "11n":
                icon = Icon.STORM_NIGHT;
                break;
            case "13d":
                icon = Icon.SNOW;
                break;
            case "13n":
                icon = Icon.SNOW_NIGHT;
                break;
            case "50d":
                icon = Icon.FOG;
                break;
            case "50n":
                icon = Icon.FOG_NIGHT;
                break;
        }
        return icon;
    }
}
