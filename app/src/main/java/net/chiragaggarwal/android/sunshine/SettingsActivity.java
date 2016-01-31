package net.chiragaggarwal.android.sunshine;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class SettingsActivity extends PreferenceActivity implements
        Preference.OnPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);

        setPreferenceValueAsSummary(countryCodePreference());
        setPreferenceValueAsSummary(zipCodePreference());
        setPreferenceValueAsSummary(temperatureUnitPreference());
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference instanceof EditTextPreference) {
            preference.setSummary(newValue.toString());
        } else if (preference instanceof ListPreference) {
            String selectedEntry = getSelectedEntryCorrespondingToEntryValue(preference, (String) newValue);
            preference.setSummary(selectedEntry);
        }
        return true;
    }

    private void setPreferenceValueAsSummary(Preference preference) {
        preference.setOnPreferenceChangeListener(this);
        String preferenceValue = PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getString(preference.getKey(), "");
        onPreferenceChange(preference, preferenceValue);
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

    private String getSelectedEntryCorrespondingToEntryValue(Preference preference, String newValue) {
        ListPreference listPreference = ((ListPreference) preference);
        CharSequence[] listPreferenceEntries = listPreference.getEntries();
        CharSequence[] listPreferenceEntryValues = listPreference.getEntryValues();

        for (Integer preferenceIndex = 0; preferenceIndex < listPreferenceEntries.length; preferenceIndex++) {
            if (newValue.equals(listPreferenceEntryValues[preferenceIndex]))
                return (String) listPreferenceEntries[preferenceIndex];
        }
        return "";
    }
}
