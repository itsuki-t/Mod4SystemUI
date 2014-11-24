package jp.rmitkt.xposed.m4s;

import jp.rmitkt.xposed.m4s.R.id;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.widget.FrameLayout;
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
}