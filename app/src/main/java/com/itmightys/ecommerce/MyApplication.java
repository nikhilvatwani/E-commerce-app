package com.itmightys.ecommerce;

import android.app.Application;
import android.content.Context;

/**
 * Created by Niknom on 3/20/2017.
 */

public class MyApplication extends Application {
    private static MyApplication sInstance;
    public void onCreate(){
        super.onCreate();
        sInstance = this;
    }
    public static MyApplication getInstance(){
        return sInstance;
    }
    public static Context getAppContext(){
        return sInstance.getApplicationContext();
    }
}
