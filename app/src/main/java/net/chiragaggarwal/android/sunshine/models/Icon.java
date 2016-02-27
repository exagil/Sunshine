package net.chiragaggarwal.android.sunshine.models;

// Icon represents a forecast's visual representation
// which is mapped to a forecast description using a code

public enum Icon {
    CLEAR_SKY, LIGHT_CLOUDS, CLOUDY, RAIN, LIGHT_RAIN, STORM;

    public static Icon parse(String iconCode) {
        Icon icon = null;
        switch (iconCode) {
            case "01d":
                icon = Icon.CLEAR_SKY;
                break;
            case "01n":
                icon = Icon.CLEAR_SKY;
                break;
            case "02d":
                icon = Icon.LIGHT_CLOUDS;
                break;
            case "02n":
                icon = Icon.LIGHT_CLOUDS;
                break;
            case "03d":
                icon = Icon.CLOUDY;
                break;
            case "03n":
                icon = Icon.CLOUDY;
                break;
            case "04d":
                icon = Icon.CLOUDY;
                break;
            case "04n":
                icon = Icon.CLOUDY;
                break;
            case "09d":
                icon = Icon.RAIN;
                break;
            case "09n":
                icon = Icon.RAIN;
                break;
            case "10d":
                icon = Icon.LIGHT_RAIN;
                break;
            case "10n":
                icon = Icon.LIGHT_RAIN;
                break;
            case "11d":
                icon = Icon.STORM;
                break;
            case "11n":
                icon = Icon.STORM;
                break;
        }
        return icon;
    }
}
