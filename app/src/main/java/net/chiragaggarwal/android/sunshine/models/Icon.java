package net.chiragaggarwal.android.sunshine.models;

// Icon represents a forecast's visual representation
// which is mapped to a forecast description using a code

public enum Icon {
    CLEAR_SKY {
        public String code() {
            return "01d";
        }
    }, LIGHT_CLOUDS {
        public String code() {
            return "02d";
        }
    }, CLOUDY {
        public String code() {
            return "03d";
        }
    }, RAIN {
        public String code() {
            return "09d";
        }
    }, LIGHT_RAIN {
        public String code() {
            return "10d";
        }
    }, STORM, SNOW, FOG, CLEAR_SKY_NIGHT {
        public String code() {
            return "01n";
        }
    },
    LIGHT_CLOUDS_NIGHT {
        public String code() {
            return "02n";
        }
    }, CLOUDY_NIGHT {
        public String code() {
            return "03n";
        }
    }, RAIN_NIGHT {
        public String code() {
            return "09n";
        }
    }, LIGHT_RAIN_NIGHT {
        public String code() {
            return "10n";
        }
    }, STORM_NIGHT, SNOW_NIGHT,
    FOG_NIGHT, SCATTERED_CLOUDS {
        public String code() {
            return "03d";
        }
    }, SCATTERED_CLOUDS_NIGHT {
        public String code() {
            return "03n";
        }
    }, BROKEN_CLOUDS_NIGHT {
        public String code() {
            return "04n";
        }
    }, BROKEN_CLOUDS {
        public String code() {
            return "04d";
        }
    };

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
                icon = Icon.SCATTERED_CLOUDS;
                break;
            case "03n":
                icon = Icon.SCATTERED_CLOUDS_NIGHT;
                break;
            case "04d":
                icon = Icon.BROKEN_CLOUDS;
                break;
            case "04n":
                icon = Icon.BROKEN_CLOUDS_NIGHT;
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

    public String code() {
        return this.code();
    }
}
