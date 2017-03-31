package com.prashanth.hastrix;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class NotConnectedActivity extends AppCompatActivity {

    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_connected);

        button= (Button) findViewById(R.id.btnTry);

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
    public void checkConnectivity(View v)
    {
        if(networkConnectivity()==true)
        {
            Intent main = new Intent(getApplicationContext(),MainActivity.class);//to do
            startActivity(main);
            finish();
        }
        else{
            Toast.makeText(getApplicationContext(),"Sorry still no connection found",Toast.LENGTH_SHORT).show();
        }
    }
}
