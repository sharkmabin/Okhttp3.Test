package com.ma.okhttp3project;

import android.app.Application;

/**
 * Created by binbin.ma on 2017/3/31.
 */

public class MyApplication extends Application {
    public static MyApplication sContext;//全局的Context对象

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
    }
}
