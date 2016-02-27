package net.chiragaggarwal.android.sunshine.models;

// Icon represents a forecast's visual representation
// which is mapped to a forecast description using a code

import net.chiragaggarwal.android.sunshine.R;

public enum Icon {
    CLEAR_SKY {
        public String code() {
            return "01d";
        }

        public int resource() {
            return R.drawable.ic_clear;
        }

        public int art() {
            return R.drawable.art_clear;
        }
    }, LIGHT_CLOUDS {
        public String code() {
            return "02d";
        }

        public int resource() {
            return R.drawable.ic_light_clouds;
        }

        public int art() {
            return R.drawable.art_light_clouds;
        }
    }, RAIN {
        public String code() {
            return "09d";
        }

        public int resource() {
            return R.drawable.ic_rain;
        }

        public int art() {
            return R.drawable.art_rain;
        }
    }, LIGHT_RAIN {
        public String code() {
            return "10d";
        }

        public int resource() {
            return R.drawable.ic_light_rain;
        }

        public int art() {
            return R.drawable.art_light_rain;
        }
    }, STORM {
        public String code() {
            return "11d";
        }

        public int resource() {
            return R.drawable.ic_storm;
        }
    }, SNOW {
        public String code() {
            return "13d";
        }

        public int resource() {
            return R.drawable.ic_snow;
        }
    }, FOG {
        public String code() {
            return "50d";
        }

        public int resource() {
            return R.drawable.ic_fog;
        }
    }, CLEAR_SKY_NIGHT {
        public String code() {
            return "01n";
        }

        public int resource() {
            return R.drawable.ic_clear;
        }

        public int art() {
            return R.drawable.art_clear;
        }
    }, LIGHT_CLOUDS_NIGHT {
        public String code() {
            return "02n";
        }

        public int resource() {
            return R.drawable.ic_light_clouds;
        }

        public int art() {
            return R.drawable.art_light_clouds;
        }
    }, RAIN_NIGHT {
        public String code() {
            return "09n";
        }

        public int resource() {
            return R.drawable.ic_rain;
        }

        public int art() {
            return R.drawable.art_rain;
        }
    }, LIGHT_RAIN_NIGHT {
        public String code() {
            return "10n";
        }

        public int resource() {
            return R.drawable.ic_light_rain;
        }

        public int art() {
            return R.drawable.art_light_rain;
        }
    }, STORM_NIGHT {
        public String code() {
            return "11n";
        }

        public int resource() {
            return R.drawable.ic_storm;
        }
    }, SNOW_NIGHT {
        public String code() {
            return "13n";
        }

        public int resource() {
            return R.drawable.ic_snow;
        }
    }, FOG_NIGHT {
        public String code() {
            return "50n";
        }

        public int resource() {
            return R.drawable.ic_fog;
        }
    }, SCATTERED_CLOUDS {
        public String code() {
            return "03d";
        }

        public int resource() {
            return R.drawable.ic_cloudy;
        }

        public int art() {
            return R.drawable.art_clouds;
        }
    }, SCATTERED_CLOUDS_NIGHT {
        public String code() {
            return "03n";
        }

        public int resource() {
            return R.drawable.ic_cloudy;
        }

        public int art() {
            return R.drawable.art_clouds;
        }
    }, BROKEN_CLOUDS_NIGHT {
        public String code() {
            return "04n";
        }

        public int resource() {
            return R.drawable.ic_cloudy;
        }

        public int art() {
            return R.drawable.art_clouds;
        }
    }, BROKEN_CLOUDS {
        public String code() {
            return "04d";
        }

        public int resource() {
            return R.drawable.ic_cloudy;
        }

        public int art() {
            return R.drawable.art_clouds;
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

    public int resource() {
        return this.resource();
    }

    public int art() {
        return this.art();
    }
}
