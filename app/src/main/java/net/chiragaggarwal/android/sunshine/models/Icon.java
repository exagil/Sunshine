package net.chiragaggarwal.android.sunshine.models;

// Icon represents a forecast's visual representation
// which is mapped to a forecast description using a code

public enum Icon {
    CLEAR_SKY, LIGHT_CLOUDS;

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
        }
        return icon;
    }
}
