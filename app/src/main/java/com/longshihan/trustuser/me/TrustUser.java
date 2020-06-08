package com.longshihan.trustuser.me;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;

import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class TrustUser implements IXposedHookLoadPackage {
    private static final String TAG = "TrustUserXposed";
    private static String currentPackageName;

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XposedBridge.log(TAG + " >> current package:" + lpparam.packageName);
        currentPackageName = lpparam.packageName;
        try {//7.0  http://androidxref.com/7.1.1_r6/xref/frameworks/base/core/java/android/security/net/config/ManifestConfigSource.java
            findAndHookMethod("android.security.net.config.ManifestConfigSource",
                    lpparam.classLoader, "getDefaultConfig", new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            XposedHelpers.setIntField(param.thisObject, "mTargetSdkVersion", 23);
                            XposedHelpers.setObjectField(param.thisObject, "mConfigSource", null);
                            Object o= XposedHelpers.getObjectField(param.thisObject, "mApplicationInfo");
                            if (o instanceof ApplicationInfo){
                                //如果匹配的上说明存在这个变量，修改这个变量,9.0新增
                                // http://androidxref.com/9.0.0_r3//xref/frameworks/base/core/java/android/security/net/config/ManifestConfigSource.java
                                ApplicationInfo info= (ApplicationInfo) o;
                                info.targetSdkVersion=23;
                                XposedHelpers.setObjectField(param.thisObject, "mApplicationInfo", info);
                            }
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
