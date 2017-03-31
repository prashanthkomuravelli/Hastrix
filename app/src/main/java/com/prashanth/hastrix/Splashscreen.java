package com.prashanth.hastrix;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

public class Splashscreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        Thread t=new Thread(){
            @Override
            public void run()
            {
                try {
                    sleep(3000);
                    if(networkConnectivity()==true) {
                        Intent in = new Intent(getApplicationContext(), MainActivity.class);//to do
                        startActivity(in);
                        finish();
                    }
                    else
                    {
                        Intent notConnectedIntent = new Intent(getApplicationContext(),NotConnectedActivity.class);
                        startActivity(notConnectedIntent);
                        finish();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }
    private boolean networkConnectivity()
    {
        ConnectivityManager cm =
                (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
}
