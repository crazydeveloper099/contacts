package com.lazydeveloper.contacts;

import android.app.Application;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;

import java.util.List;

/**
 * Created by anubhav on 31-08-2018.
 */

public class background extends Application
{
    public static final String APPLICATION_ID = "3CD5D7A7-ED51-B10F-FF4F-98B2CC44F800";
    public static final String API_KEY = "766453B7-C9DB-BE76-FF49-6272BEDE6800";
    public static final String SERVER_URL = "https://api.backendless.com";
    static BackendlessUser user;
    public static List<contacts> contactsList;
    public static int pos;

    @Override
    public void onCreate() {
        super.onCreate();
        Backendless.setUrl( SERVER_URL );
        Backendless.initApp( getApplicationContext(),
                APPLICATION_ID,
                API_KEY );
    }
}
