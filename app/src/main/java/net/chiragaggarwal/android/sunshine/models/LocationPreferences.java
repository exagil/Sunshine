package net.chiragaggarwal.android.sunshine.models;

import android.content.SharedPreferences;

public class LocationPreferences {
    private static LocationPreferences instance;
    public static final String HAS_CHANGED = "net.chiragaggarwal.android.sunshine.models.Location.HAS_CHANGED";

    private final SharedPreferences sharedPreferences;

    public static LocationPreferences getInstance(SharedPreferences sharedPreferences) {
        if (instance == null) instance = new LocationPreferences(sharedPreferences);
        return instance;
    }

    private LocationPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public boolean setLocationAsNotChanged() {
        return this.sharedPreferences.edit().putBoolean(LocationPreferences.HAS_CHANGED, false).commit();
    }

    public boolean setLocationAsChanged() {
        return this.sharedPreferences.edit().putBoolean(LocationPreferences.HAS_CHANGED, true).commit();
    }

    public boolean hasChanged() {
        return this.sharedPreferences.getBoolean(LocationPreferences.HAS_CHANGED, false);
    }
}
