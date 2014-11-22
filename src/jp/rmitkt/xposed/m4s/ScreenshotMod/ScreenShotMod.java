package jp.rmitkt.xposed.m4s.ScreenshotMod;

import jp.rmitkt.xposed.m4s.Mod4SystemUI;
import jp.rmitkt.xposed.m4s.R;
import jp.rmitkt.xposed.m4s.ScreenshotMod.ScreenshotAction;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.widget.BaseAdapter;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class ScreenShotMod {
    private static final String CLASS = "ScreenShotMod.java";
    public static final String TARGET_PACKAGE_NAME = "android";
    public static final String CLASS_GLOBAL_ACTIONS = "com.android.internal.policy.impl.GlobalActions";
    public static final String CLASS_ACTION = "com.android.internal.policy.impl.GlobalActions.Action";
    public static final String CLASS_SILENT_TRISTATE_ACTION = "com.android.internal.policy.impl.GlobalActions$SilentModeTriStateAction";
    
    private static Context mContext;
    private static Context armContext;
    private static String mScreenshotLabel;
    private static Drawable mScreenshotIcon;
    private static XSharedPreferences xPref;
    private static int afterRebootPos = 0;

    //constants for modes of showing confirmation dialogs
    private static void log(String message) {
    	XposedBridge.log(CLASS + ": " + message);
    }
    public static void init(final XSharedPreferences pref, final ClassLoader classLoader) {
    	xPref = pref;
        try {
            final Class<?> globalActionsClass = XposedHelpers.findClass(CLASS_GLOBAL_ACTIONS, classLoader);
            final Class<?> actionClass = XposedHelpers.findClass(CLASS_ACTION, classLoader);
            

            XposedBridge.hookAllConstructors(globalActionsClass, new XC_MethodHook() {
               @Override
               protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                   mContext = (Context) param.args[0];

                   armContext = mContext.createPackageContext(
                           Mod4SystemUI.PACKAGE_NAME, Context.CONTEXT_IGNORE_SECURITY);
                   
                   Resources armRes = armContext.getResources();
                   mScreenshotLabel = armRes.getString(R.string.take_screenshot);

                   //Get user's preference for the menu icon color theme
                   xPref.reload();
                   String IconColorMode = xPref.getString("pref_icon_color", "0");
                   log("IconColorMode = " + IconColorMode);
                   int IconColorInt = Integer.parseInt(IconColorMode);
                   int[] mScreenshotIconSet = {R.drawable.ic_screenshot, R.drawable.ic_screenshot_dark, R.drawable.ic_screenshot_color, R.drawable.ic_screenshot_existenz};

                   //Set the icons appropriately
                   //1st level icons
                   mScreenshotIcon = armRes.getDrawable(mScreenshotIconSet[IconColorInt]);
                   log("GlobalActions constructed, resources set.");
               }
            });

            XposedHelpers.findAndHookMethod(globalActionsClass, "createDialog", new XC_MethodHook() {

            	@Override
                protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                    if (mContext == null) return;

                    pref.reload();
                    boolean screenshotEnabled = pref.getBoolean("pref_enable_screenshot", true);
                    @SuppressWarnings("unchecked")
                    List<Object> mItems = (List<Object>) XposedHelpers.getObjectField(param.thisObject, "mItems");

                    Object screenshotActionItem = null;
                    Resources res = mContext.getResources();

                    for (Object o : mItems) {
                    	// search for drawable
                        try {
                            Field f = XposedHelpers.findField(o.getClass(), "mIconResId");
                            String resName = res.getResourceEntryName((Integer) f.get(o)).toLowerCase(Locale.US);
                            log("Drawable resName = " + resName);
                            if (resName.contains("screenshot")) {
//                                screenshotActionItem = o;
                            }
                        } catch (NoSuchFieldError nfe) {
                            // continue
                        } catch (Resources.NotFoundException resnfe) { 
                            // continue
                        } catch (IllegalArgumentException iae) {
                            // continue
                        }
                        // search for text
                        try {
                            Field f = XposedHelpers.findField(o.getClass(), "mMessageResId");
                            String resName = res.getResourceEntryName((Integer) f.get(o)).toLowerCase(Locale.US);
                            log("Text resName = " + resName);
                            if (resName.contains("screenshot")) {
//                                screenshotActionItem = o;
                            }
                            log(o.toString());
                        } catch (NoSuchFieldError nfe) {
                        	// continue
                        } catch (Resources.NotFoundException resnfe) { 
                        	// continue
                        } catch (IllegalArgumentException iae) {
                        	// continue
                        }
                         
                    }

                    // IV. Add/replace action items and update positions accordingly
                    if(screenshotEnabled){
	                    if (screenshotActionItem != null) {
	                    	log("Existing Screenshot action item found! Nothing is done.");
	                    	//no need to do anything!
	                    	//As the original screenshot action item can be left intact
	                    } else {
	                    	log("Existing Screenshot action item NOT found! Adding new ScreenshotAction item");
	                    	Object action = Proxy.newProxyInstance(classLoader, new Class<?>[] { actionClass }, 
	                                new ScreenshotAction(mContext, mScreenshotLabel, mScreenshotIcon));
	                    	mItems.add(afterRebootPos, action);
	                        BaseAdapter mAdapter = (BaseAdapter) XposedHelpers.getObjectField(param.thisObject, "mAdapter");
	                        mAdapter.notifyDataSetChanged(); 
	                    }
                    }
                }
            });
        } catch (Exception e) {
            XposedBridge.log(e);
        }
    }
}
