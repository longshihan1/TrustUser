package com.longshihan.trustuser.me;

import android.app.AndroidAppHelper;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookConstructor;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class TrustUser implements IXposedHookLoadPackage {
    private static final String TAG = "TrustUserXposed";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XposedBridge.log(TAG + " >> current package:" + lpparam.packageName);
        final Class<?> clazz = XposedHelpers.findClass("android.security.net.config.ManifestConfigSource", lpparam.classLoader);
        try {
//            findAndHookMethod("android.security.net.config.NetworkSecurityConfig",
//                    lpparam.classLoader,"getDefaultBuilder",Integer.class,Integer.class, new XC_MethodHook() {
//                        @Override
//                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                            XposedBridge.log(TAG + " >> getDefaultBuilder:2:" + param.args.length);
//                        }
//                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            findAndHookMethod("android.security.net.config.ManifestConfigSource",
                    lpparam.classLoader, "getDefaultConfig", new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param)  {
                            XposedHelpers.setIntField(param.thisObject, "mTargetSdkVersion", 10);
                            XposedBridge.log(TAG + " >> getDefaultBuilder:0022:" + XposedHelpers.getIntField(param.thisObject, "mTargetSdkVersion"));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            findAndHookMethod("android.security.net.config.ManifestConfigSource$DefaultConfigSource",
                    lpparam.classLoader, "getDefaultConfig", new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            XposedBridge.log(TAG + " >> getDefaultBuilder:003:" + param.args.length);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
//         findAndHookMethod("android.security.net.config.NetworkSecurityConfig",
//                 lpparam.classLoader,"getDefaultBuilder",Integer.class, new XC_MethodHook() {
//                     @Override
//                     protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                         XposedBridge.log(TAG + " >> getDefaultBuilder:01:" + param.args.length);
//                     }
//                 });
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
//            findAndHookMethod("android.security.net.config.NetworkSecurityConfig",
//                    lpparam.classLoader,"getDefaultBuilder", ApplicationInfo.class, new XC_MethodHook() {
//                        @Override
//                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                            XposedBridge.log(TAG + " >> getDefaultBuilder:1:" + param.args.length);
//                        }
//                    });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
