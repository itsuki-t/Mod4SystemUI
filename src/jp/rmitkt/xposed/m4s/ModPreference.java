package jp.rmitkt.xposed.m4s;

import com.afollestad.materialdialogs.*;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

public class ModPreference extends ActionBarActivity {
	public static final String PACKAGE_NAME = ModPreference.class.getPackage().getName();
	public static TextView text1;
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.prefslayout);
		if (savedInstanceState == null) {
		  getFragmentManager().beginTransaction().add(R.id.container, new PrefFragment()).commit();
		 }
	}

	public void showQSNumOfRowDialog(final SharedPreferences sp, final Preference pref) {
        new MaterialDialog.Builder(this)
        .title(R.string.pref_qs_number_of_row_title)
        .items(R.array.number_of_row)
        .itemsCallbackSingleChoice(Integer.parseInt(sp.getString(getString(R.string.pref_qs_number_of_row_key_index),"0")), new MaterialDialog.ListCallback() {
        	@Override
        	public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
        		sp.edit().putString(getString(R.string.pref_qs_number_of_row_key), text.toString()).commit();
        		sp.edit().putString(getString(R.string.pref_qs_number_of_row_key_index), Integer.toString(which)).commit();
				pref.setTitle(String.format(getString(R.string.pref_qs_number_of_row_summary), text.toString()));
        	}
        })
        .positiveText(getString(R.string.btn_choose))
        .build()
        .show();
	}	

	public void showQSRowHeightDialog(final SharedPreferences sp, final Preference pref) {
        new MaterialDialog.Builder(this)
        .title(R.string.pref_qs_row_height_title)
        .items(R.array.row_height)
        .itemsCallbackSingleChoice(Integer.parseInt(sp.getString(getString(R.string.pref_qs_row_height_key_index),"0")), new MaterialDialog.ListCallback() {
        	@Override
        	public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
        		sp.edit().putString(getString(R.string.pref_qs_row_height_key), text.toString()).commit();
        		sp.edit().putString(getString(R.string.pref_qs_row_height_key_index), Integer.toString(which)).commit();
				pref.setTitle(String.format(getString(R.string.pref_qs_row_height_summary), text.toString()));
        	}
        })
        .positiveText(getString(R.string.btn_choose))
        .build()
        .show();
	}	
}