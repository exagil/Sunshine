package net.chiragaggarwal.android.sunshine;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import net.chiragaggarwal.android.sunshine.models.LocationPreferences;

public class SettingsActivity extends PreferenceActivity implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String BLANK = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        setPreferenceValueAsSummary(zipCodePreference(), sharedPreferences);
        setPreferenceValueAsSummary(countryCodePreference(), sharedPreferences);
        setPreferenceValueAsSummary(temperatureUnitPreference(), sharedPreferences);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(LocationPreferences.HAS_CHANGED)) return;
        else if (hasNotificationPreferenceChanged(key)) return;

        String changedValue = sharedPreferences.getString(key, BLANK);

        if (hasChangedZipCode(key) || hasChangedCountryCode(key)) {
            Preference preference = findPreference(key);
            preference.setSummary(changedValue);
            LocationPreferences.getInstance(sharedPreferences).setLocationAsChanged();

        } else if (hasChangedTemperatureUnit(key)) {
            Preference temperaturePreference = findPreference(key);
            temperaturePreference.setSummary(changedValue);
        }
    }

    private boolean hasNotificationPreferenceChanged(String key) {
        return key.equals(this.getString(R.string.preference_show_notifications_key));
    }

    private boolean hasChangedTemperatureUnit(String key) {
        return key.equals(getString(R.string.preference_temperature_unit_key));
    }

    private boolean hasChangedCountryCode(String key) {
        return key.equals(getString(R.string.preference_country_code_key));
    }

    private boolean hasChangedZipCode(String key) {
        return key.equals(getString(R.string.preference_zip_code_key));
    }

    private void setPreferenceValueAsSummary(Preference preference, SharedPreferences sharedPreferences) {
        String preferenceValue = sharedPreferences.getString(preference.getKey(), "");
        preference.setSummary(preferenceValue);
    }

    private Preference zipCodePreference() {
        String zipCodePreferenceKey = getString(R.string.preference_zip_code_key);
        return findPreference(zipCodePreferenceKey);
    }

    private Preference countryCodePreference() {
        String countryCodePreferenceKey = getString(R.string.preference_country_code_key);
        return findPreference(countryCodePreferenceKey);
    }

    private Preference temperatureUnitPreference() {
        String temperatureUnitPreferenceKey = getString(R.string.preference_temperature_unit_key);
        return findPreference(temperatureUnitPreferenceKey);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Nullable
    @Override
    public Intent getParentActivityIntent() {
        return super.getParentActivityIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }
}
