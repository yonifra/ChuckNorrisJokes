package com.cryprocodes.chucknorrisjokes;

import android.app.Application;
import android.content.Context;

/**
 * Created by Jonathan on 18/10/2017.
 */

public class ChuckNorrisApplication extends Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        ChuckNorrisApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return ChuckNorrisApplication.context;
    }
}
