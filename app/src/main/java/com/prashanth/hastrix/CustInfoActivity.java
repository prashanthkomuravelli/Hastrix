package com.prashanth.hastrix;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CustInfoActivity extends AppCompatActivity {

    EditText etCName,etCNumber,etCEmail;
    Boolean validation = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cust_info);

        etCName = (EditText) findViewById(R.id.etCName);
        etCNumber = (EditText) findViewById(R.id.etCNumber);
        etCEmail = (EditText) findViewById(R.id.etCEmail);



    }
    private boolean isValidName(String name){
        if(name.equals("")){
            return false;
        }
        else
            return true;
    }
    private boolean isValidMobile(String phone) {
        if(phone.length()>10)
            return false;
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }
    private boolean isValidMail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    public void runGmailIntent(View view){
        if (isValidName(etCName.getText().toString()) == false) {
            Toast.makeText(getApplicationContext(), "Enter Valid Name", Toast.LENGTH_SHORT).show();
            validation = false;
        }
        else {
            if (isValidMobile(etCNumber.getText().toString()) == false) {
                Toast.makeText(getApplicationContext(), "Enter Valid Contact", Toast.LENGTH_SHORT).show();
                validation = false;
            }
            else {
                if (isValidMail(etCEmail.getText().toString()) == false) {
                    Toast.makeText(getApplicationContext(), "Enter Valid Email", Toast.LENGTH_SHORT).show();
                    validation = false;
                }
                else
                    validation = true;
            }
        }

        if(validation == true){
            // TODO: 09-04-2017  here add placeOrder methods implementation
            Toast.makeText(getApplicationContext(), "All OK", Toast.LENGTH_SHORT).show();
        }

    }

}
