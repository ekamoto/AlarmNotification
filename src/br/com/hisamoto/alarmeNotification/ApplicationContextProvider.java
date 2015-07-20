package br.com.hisamoto.alarmeNotification;

import android.app.Application;
import android.content.Context;

/**
 * @author Leandro Shindi
 * @version 1.0 20/07/15.
 */
public class ApplicationContextProvider extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {

        return context;
    }
}
