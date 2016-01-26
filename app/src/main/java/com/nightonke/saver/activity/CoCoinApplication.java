package com.nightonke.saver.activity;

import android.app.Application;
import android.content.Context;
import android.provider.Settings;

import com.github.mmin18.layoutcast.LayoutCast;
import com.nightonke.saver.BuildConfig;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by 伟平 on 2015/11/2.
 */

public class CoCoinApplication extends Application {

    public static final int VERSION = 110;

    private static Context mContext;

    public static RefWatcher getRefWatcher(Context context) {
        CoCoinApplication application = (CoCoinApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    private RefWatcher refWatcher;

    @Override public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            LayoutCast.init(this);
        }

        refWatcher = LeakCanary.install(this);
        CoCoinApplication.mContext = getApplicationContext();
    }

    public static Context getAppContext() {
        return CoCoinApplication.mContext;
    }

    public static String getAndroidId() {
        return Settings.Secure.getString(
                getAppContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

}
