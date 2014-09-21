package jp.rmitkt.xposed.m4s;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;

public class ModPreference extends PreferenceActivity {
	public static final String PACKAGE_NAME = ModPreference.class.getPackage().getName();
	private CheckBoxPreference mQS_MergeNotiftabAndQStab;
	private ListPreference mQS_NumberOfRowSelect;
	private ListPreference mQS_RowHeightSelect;

	@SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.HoloLightTheme);
		addPreferencesFromResource(R.xml.prefs);
		getPreferenceManager().setSharedPreferencesMode(MODE_WORLD_READABLE);

		mQS_MergeNotiftabAndQStab =  (CheckBoxPreference) findPreference(getString(R.string.pref_use_qsmod_key));
		mQS_NumberOfRowSelect = (ListPreference) findPreference(getString(R.string.pref_qs_number_of_row_key));
		mQS_RowHeightSelect = (ListPreference) findPreference(getString(R.string.pref_qs_row_height_key));

		if (mQS_MergeNotiftabAndQStab.isChecked()) {
			mQS_NumberOfRowSelect.setEnabled(true);
			mQS_RowHeightSelect.setEnabled(true);
		} else {
			mQS_NumberOfRowSelect.setEnabled(false);
			mQS_RowHeightSelect.setEnabled(false);
		}
		mQS_MergeNotiftabAndQStab.setOnPreferenceChangeListener(new CheckBoxPreference.OnPreferenceChangeListener(){
			@Override
			public boolean onPreferenceChange(Preference preference,Object newValue) {
				if (newValue.equals(true)) {
					mQS_NumberOfRowSelect.setEnabled(true);
					mQS_RowHeightSelect.setEnabled(true);
				} else {
					mQS_NumberOfRowSelect.setEnabled(false);
					mQS_RowHeightSelect.setEnabled(false);
				}
				return true;
			}
		});

		mQS_NumberOfRowSelect.setSummary(String.format(getString(R.string.pref_qs_number_of_row_summary), mQS_NumberOfRowSelect.getEntryValues()));
		mQS_NumberOfRowSelect.setOnPreferenceChangeListener(new EditTextPreference.OnPreferenceChangeListener(){
			@Override
			public boolean onPreferenceChange(Preference preference,Object newValue) {
				preference.setSummary(String.format(getString(R.string.pref_qs_number_of_row_summary), newValue.toString()));
				return true;
			}
		});

		mQS_RowHeightSelect.setSummary(String.format(getString(R.string.pref_qs_row_height_summary), mQS_RowHeightSelect.getEntryValues()));
		mQS_RowHeightSelect.setOnPreferenceChangeListener(new EditTextPreference.OnPreferenceChangeListener(){
			@Override
			public boolean onPreferenceChange(Preference preference,Object newValue) {
				preference.setSummary(String.format(getString(R.string.pref_qs_row_height_summary), newValue.toString()));
				return true;
			}
		});
		
	}	
}