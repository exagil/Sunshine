package net.chiragaggarwal.android.sunshine;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class SettingsActivity extends PreferenceActivity implements
        Preference.OnPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
        setPreferenceValueAsSummary();
    }

    private void setPreferenceValueAsSummary() {
        Preference preference = findPreference(preferenceZipCodeKey());
        String preferenceValue = PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getString(preference.getKey(), "");
        onPreferenceChange(preference, preferenceValue);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference instanceof EditTextPreference) {
            preference.setSummary(newValue.toString());
        }
        return true;
    }

    private String preferenceZipCodeKey() {
        return getString(R.string.preference_zip_code);
    }
}
