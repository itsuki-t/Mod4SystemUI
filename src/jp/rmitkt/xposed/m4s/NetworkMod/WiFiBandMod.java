package jp.rmitkt.xposed.m4s.NetworkMod;

import android.content.res.XResources;

import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;

public class WiFiBandMod {
    private static final String TAG = "XposedWiFiBandMod";
    private static final boolean DEBUG = false;
    
    private static void log(String message) {
        XposedBridge.log(TAG + ": " + message);
    }
    
    public static void init(final XSharedPreferences prefs, ClassLoader classLoader) {
		XResources.setSystemWideReplacement("android", "bool", "config_wifi_dual_band_support", true);
    }
}
