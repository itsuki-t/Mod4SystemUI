package jp.rmitkt.xposed.m4s;

import android.content.res.XModuleResources;
import android.content.res.XResources.DimensionReplacement;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;
import de.robv.android.xposed.callbacks.XC_LayoutInflated;
import de.robv.android.xposed.callbacks.XC_LayoutInflated.LayoutInflatedParam;

public class Mod4SystemUI implements IXposedHookZygoteInit,IXposedHookInitPackageResources {
	public static final String PACKAGE_NAME = Mod4SystemUI.class.getPackage().getName();
	private static final String CLASSNAME_SYSTEMUI = "com.android.systemui";
	private static String modulePath = null;
	private static XModuleResources modRes;
	private static XSharedPreferences pref = null;

	@Override
	public void initZygote(StartupParam startupParam) throws Throwable {
		modulePath = startupParam.modulePath;
		pref = new XSharedPreferences(PACKAGE_NAME);
	}

	@Override
	public void handleInitPackageResources(InitPackageResourcesParam resparam) throws Throwable {
		if (!resparam.packageName.equals(CLASSNAME_SYSTEMUI)) {
			return;
		}
		modRes = XModuleResources.createInstance(modulePath, resparam.res);

		if (pref.getBoolean("QucikSettings", false)) {
			resparam.res.hookLayout(CLASSNAME_SYSTEMUI, "layout", "super_status_bar", new XC_LayoutInflated() {
				@Override
				public void handleLayoutInflated(final LayoutInflatedParam liparam) throws Throwable {
					// hide tab select
					liparam.view.findViewById(liparam.res.getIdentifier("tabs", "id", "android")).setVisibility(View.GONE);

					// move QuickSetting Items from QS Tab to Notification
					FrameLayout QStab = (FrameLayout) liparam.view.findViewById(liparam.res.getIdentifier("quick_settings_tab","id",CLASSNAME_SYSTEMUI));
					LinearLayout QSItems = (LinearLayout) liparam.view.findViewById(liparam.res.getIdentifier("tools_rows","id",CLASSNAME_SYSTEMUI));
					LinearLayout Notificationtab = (LinearLayout) liparam.view.findViewById(liparam.res.getIdentifier("notifications_tab","id",CLASSNAME_SYSTEMUI));
					QStab.removeView(QSItems);
					Notificationtab.addView(QSItems, 0);

					// draw upper line and bottom line of QuickSettings
					ImageView upperLine = new ImageView(liparam.view.getContext());
					ImageView bottomLine = new ImageView(liparam.view.getContext());
					upperLine.setBackgroundColor(0x0ffffffff);
					bottomLine.setBackgroundColor(0x0ffffffff);
					LinearLayout.LayoutParams upperLineLP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)(dp2px(liparam,1.0f)));
					LinearLayout.LayoutParams buttomLineLP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)(dp2px(liparam,1.0f)));
					buttomLineLP.setMargins(0, (int)(dp2px(liparam,20.0f)), 0, 0);
					Notificationtab.addView(upperLine, 0, upperLineLP);
					Notificationtab.addView(bottomLine, 2, buttomLineLP);
				}
			});

			// set QuickSetting Items
			int qsRowNum = Integer.parseInt(pref.getString("QSRowNum", "4"));
			resparam.res.setReplacement(CLASSNAME_SYSTEMUI, "integer", "config_maxToolItemsInARow", qsRowNum);
			float qsRowHeight = Float.parseFloat(pref.getString("QSRowHeight", "100.0"));
			resparam.res.setReplacement(CLASSNAME_SYSTEMUI, "dimen", "notification_panel_tools_row_height", new DimensionReplacement(qsRowHeight,TypedValue.COMPLEX_UNIT_DIP));
			resparam.res.setReplacement("com.android.systemui", "integer", "config_maxToolItemsInGrid", 16);
		}
	}

	float dp2px(LayoutInflatedParam liparam, float dp){
		float density = liparam.res.getDisplayMetrics().density;
		return density * dp;
	}
}
