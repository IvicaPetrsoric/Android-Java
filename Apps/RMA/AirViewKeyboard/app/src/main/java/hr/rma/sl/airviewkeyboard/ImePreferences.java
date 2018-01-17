package hr.rma.sl.airviewkeyboard;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;


/**
 * Displays the IME preferences inside the input method setting.
 */
public class ImePreferences extends PreferenceActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.settings_name);

        addPreferencesFromResource(R.xml.ime_preferences);

        setEditTextListener("keyboard_height_percentage");
        setEditTextListener("enlarge_key_percentage");
        setEditTextListener("enlarge_row_percentage");
     }


    protected void setEditTextListener(String preferenceKey){
        EditTextPreference etPref = (EditTextPreference)getPreferenceManager().findPreference(preferenceKey);
        etPref.setSummary(etPref.getText());

        etPref.setOnPreferenceChangeListener(new EditTextPreference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                EditTextPreference pref = (EditTextPreference) preference;
                pref.setSummary(newValue.toString());
                return true;
            }
        });
    }


}
