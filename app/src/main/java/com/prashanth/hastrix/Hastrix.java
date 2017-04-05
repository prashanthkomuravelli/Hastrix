package com.prashanth.hastrix;

import android.app.Application;

import com.firebase.client.Firebase;


/**
 * Created by K.PRASHANTH on 04-04-2017.
 */

public class Hastrix extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);


    }
}
