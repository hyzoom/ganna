package com.ishraq.janna;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.view.ViewConfiguration;

import java.lang.reflect.Field;
import java.util.Locale;

/**
 * Created by Ahmed on 2/16/2016.
 */
public class JannaApp extends Application{
    public static final String LOG_TAG = "Janna";
    public static boolean actionbarOverflowForced;

    private static Context context;


    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        forceArabicLocale();
        forceActionbarOverflow();
    }


    public static Context getContext() {
        return context;
    }

    protected void forceArabicLocale() {
        Locale loc = new Locale("ar");
        Locale.setDefault(loc);
        Configuration cfg = new Configuration();
        cfg.locale = loc;
        getResources().updateConfiguration(cfg, null);
    }

    protected void forceActionbarOverflow() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }

            actionbarOverflowForced = true;
        } catch (Exception ex) {
        }
    }
}
