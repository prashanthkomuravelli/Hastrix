package com.prashanth.hastrix;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Splashscreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        Thread t=new Thread(){
            @Override
            public void run()
            {
                try {
                    sleep(3500);
                    Intent in=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(in);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }
}
