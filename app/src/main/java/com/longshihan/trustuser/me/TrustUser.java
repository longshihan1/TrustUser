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
            findAndHookMethod("android.app.Application", lpparam.classLoader, "attach", Context.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    XposedBridge.log(TAG + " >> getDefaultBuilder:0054:" + param.args.length);
                    Context context = (Context) param.args[0];
                    processOkHttp(context.getClassLoader());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void processOkHttp(ClassLoader classLoader) {
        /* hooking OKHTTP by SQUAREUP */
        /* com/squareup/okhttp/CertificatePinner.java available online @ https://github.com/square/okhttp/blob/master/okhttp/src/main/java/com/squareup/okhttp/CertificatePinner.java */
        /* public void check(String hostname, List<Certificate> peerCertificates) throws SSLPeerUnverifiedException{}*/
        /* Either returns true or a exception so blanket return true */
        /* Tested against version 2.5 */
        Log.d(TAG, "Hooking com.squareup.okhttp.CertificatePinner.check(String,List) (2.5) for: " + currentPackageName);

        try {
            classLoader.loadClass("com.squareup.okhttp.CertificatePinner");
            findAndHookMethod("com.squareup.okhttp.CertificatePinner",
                    classLoader,
                    "check",
                    String.class,
                    List.class,
                    new XC_MethodReplacement() {
                        @Override
                        protected Object replaceHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                            return true;
                        }
                    });
        } catch (ClassNotFoundException e) {
            // pass
            Log.d(TAG, "OKHTTP 2.5 not found in " + currentPackageName + "-- not hooking");
        }

        //https://github.com/square/okhttp/blob/parent-3.0.1/okhttp/src/main/java/okhttp3/CertificatePinner.java#L144
        Log.d(TAG, "Hooking okhttp3.CertificatePinner.check(String,List) (3.x) for: " + currentPackageName);

        try {
            classLoader.loadClass("okhttp3.CertificatePinner");
            findAndHookMethod("okhttp3.CertificatePinner",
                    classLoader,
                    "check",
                    String.class,
                    List.class,
                    new XC_MethodReplacement() {
                        @Override
                        protected Object replaceHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                            return null;
                        }
                    });
        } catch (ClassNotFoundException e) {
            Log.d(TAG, "OKHTTP 3.x not found in " + currentPackageName + " -- not hooking");
            // pass
        }

        //https://github.com/square/okhttp/blob/parent-3.0.1/okhttp/src/main/java/okhttp3/internal/tls/OkHostnameVerifier.java
        try {
            classLoader.loadClass("okhttp3.internal.tls.OkHostnameVerifier");
            findAndHookMethod("okhttp3.internal.tls.OkHostnameVerifier",
                    classLoader,
                    "verify",
                    String.class,
                    javax.net.ssl.SSLSession.class,
                    new XC_MethodReplacement() {
                        @Override
                        protected Object replaceHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                            return true;
                        }
                    });
        } catch (ClassNotFoundException e) {
            Log.d(TAG, "OKHTTP 3.x not found in " + currentPackageName + " -- not hooking OkHostnameVerifier.verify(String, SSLSession)");
            // pass
        }

        //https://github.com/square/okhttp/blob/parent-3.0.1/okhttp/src/main/java/okhttp3/internal/tls/OkHostnameVerifier.java
        try {
            classLoader.loadClass("okhttp3.internal.tls.OkHostnameVerifier");
            findAndHookMethod("okhttp3.internal.tls.OkHostnameVerifier",
                    classLoader,
                    "verify",
                    String.class,
                    java.security.cert.X509Certificate.class,
                    new XC_MethodReplacement() {
                        @Override
                        protected Object replaceHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                            return true;
                        }
                    });
        } catch (ClassNotFoundException e) {
            Log.d(TAG, "OKHTTP 3.x not found in " + currentPackageName + " -- not hooking OkHostnameVerifier.verify(String, X509)(");
            // pass
        }
    }
}
