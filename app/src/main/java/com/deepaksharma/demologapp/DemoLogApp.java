package com.deepaksharma.demologapp;

import android.app.Application;

import com.deepaksharma.webaddicted.LogLevel;
import com.deepaksharma.webaddicted.TALog;

public class DemoLogApp extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        TALog.init(this,BuildConfig.DEBUG ? LogLevel.ALL : LogLevel.NONE);

        TALog.enableBorder(true);
        TALog.enableThreadInfo(true);
        TALog.setStacktraceInfo(2);
    }

}
