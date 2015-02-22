package jp.rmitkt.xposed.m4s;

import jp.rmitkt.xposed.m4s.NetworkMod.WiFiBandMod;
import jp.rmitkt.xposed.m4s.RecentPanelMod.RecentPanelMod;
import jp.rmitkt.xposed.m4s.ScreenshotMod.ScreenShotMod;
import android.content.res.XModuleResources;
import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class Mod4SystemUI implements IXposedHookZygoteInit,IXposedHookInitPackageResources,IXposedHookLoadPackage {
	public static final String PACKAGE_NAME = Mod4SystemUI.class.getPackage().getName();
	public static final String CLASSNAME_SYSTEMUI = "com.android.systemui";
	static String modulePath = null;
	static XModuleResources modRes;
	static XSharedPreferences pref = null;

	@Override
	public void initZygote(StartupParam startupParam) throws Throwable {
		modulePath = startupParam.modulePath;
		pref = new XSharedPreferences(PACKAGE_NAME);
	}

    @Override
    public void handleInitPackageResources(InitPackageResourcesParam resparam) throws Throwable {
		if (resparam.packageName.equals(CLASSNAME_SYSTEMUI)) {
			if (pref.getBoolean("QucikSettings", false)) {
				NotifPanelMod.initPackageResources(pref, resparam);
			}
		}
    }

    @Override
    public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
    	// For recent panel mod
    	if (lpparam.packageName.equals(CLASSNAME_SYSTEMUI)) {
    		if (pref.getBoolean("ShowRAMBarInRecent", false)) {
    			RecentPanelMod.init(pref, lpparam.classLoader);
    		}
    	}
    	// For screenshot mod
    	if (lpparam.packageName.equals(ScreenShotMod.TARGET_PACKAGE_NAME)){
    		if (pref.getBoolean("EnableWholeScreenShot", false)) {
    			ScreenShotMod.init(pref, lpparam.classLoader);
    		}
    	}
    	// For Network mod
    	if (lpparam.packageName.equals(ScreenShotMod.TARGET_PACKAGE_NAME)){
    		if (pref.getBoolean("WifiBand", false)) {
    			WiFiBandMod.init(pref, lpparam.classLoader);
    		}
    	}
    }
}