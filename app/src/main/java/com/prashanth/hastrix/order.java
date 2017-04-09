package com.prashanth.hastrix;

import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.prashanth.hastrix.CustomListViewAdapter;



public class order extends AppCompatActivity {
    CustomListViewAdapter ob;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        PdfDocument document = new PdfDocument();
       

    }
}
