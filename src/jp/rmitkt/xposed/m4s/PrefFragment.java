package jp.rmitkt.xposed.m4s;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import de.robv.android.xposed.XposedBridge;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;

public class PrefFragment extends PreferenceFragment {

	private CheckBoxPreference mQS_MergeNotiftabAndQStab;
	private CheckBoxPreference mQS_ShowQSEditButton;
	private Preference mQS_NumberOfRowSelect;
	private Preference mQS_RowHeightSelect;
	private CheckBoxPreference mRecent_ShowRAMBar;
	private CheckBoxPreference mScreenshot_UseMod;
	protected static SharedPreferences sp = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.prefs);
		sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
		setPreference();
		setListener();
		setInitialValue();
	}

	private void setPreference(){
		mQS_MergeNotiftabAndQStab =  (CheckBoxPreference) findPreference(getString(R.string.pref_use_qsmod_key));
		mQS_ShowQSEditButton =  (CheckBoxPreference) findPreference(getString(R.string.pref_show_qs_edit_button_key));
		mQS_NumberOfRowSelect = (Preference) findPreference(getString(R.string.pref_qs_number_of_row_key));
		mQS_RowHeightSelect = (Preference) findPreference(getString(R.string.pref_qs_row_height_key));
		mRecent_ShowRAMBar =  (CheckBoxPreference) findPreference(getString(R.string.pref_use_recentmod_key));
		mScreenshot_UseMod =  (CheckBoxPreference) findPreference(getString(R.string.pref_use_wholescreenshot_key));
	}

	private void setListener(){
		mQS_MergeNotiftabAndQStab.setOnPreferenceChangeListener(new CheckBoxPreference.OnPreferenceChangeListener(){
			@Override
			public boolean onPreferenceChange(Preference preference,Object newValue) {
				if (newValue.equals(true)) {
					mQS_ShowQSEditButton.setEnabled(true);
					mQS_NumberOfRowSelect.setEnabled(true);
					mQS_RowHeightSelect.setEnabled(true);
				} else {
					mQS_ShowQSEditButton.setEnabled(false);
					mQS_NumberOfRowSelect.setEnabled(false);
					mQS_RowHeightSelect.setEnabled(false);
				}
				return true;
			}
		});

		mQS_NumberOfRowSelect.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				try {
					ModPreference activity = (ModPreference) getActivity();  
					activity.showQSNumOfRowDialog(sp, preference);
				} catch (ClassCastException e) {
					XposedBridge.log(e);
				}
				return true;
			}
		});

		mQS_RowHeightSelect.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				try {
					ModPreference activity = (ModPreference) getActivity();  
					activity.showQSRowHeightDialog(sp, preference);
				} catch (ClassCastException e) {
					XposedBridge.log(e);
				}
				return true;
			}
		});
	}

	private void setInitialValue(){
		if (mQS_MergeNotiftabAndQStab.isChecked()) {
			mQS_ShowQSEditButton.setEnabled(true);
			mQS_NumberOfRowSelect.setEnabled(true);
			mQS_RowHeightSelect.setEnabled(true);
		} else {
			mQS_ShowQSEditButton.setEnabled(false);
			mQS_NumberOfRowSelect.setEnabled(false);
			mQS_RowHeightSelect.setEnabled(false);
		}
		mQS_NumberOfRowSelect.setTitle(String.format(getString(R.string.pref_qs_number_of_row_summary), sp.getString(getString(R.string.pref_qs_number_of_row_key), "4")));
		mQS_RowHeightSelect.setTitle(String.format(getString(R.string.pref_qs_row_height_summary), sp.getString(getString(R.string.pref_qs_row_height_key), "100")));
	}
}