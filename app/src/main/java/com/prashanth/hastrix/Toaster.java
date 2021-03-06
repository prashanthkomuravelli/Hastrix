package com.prashanth.hastrix;

/**
 * Created by K.PRASHANTH on 16-04-2017.
 */
import com.prashanth.hastrix.*;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;



public class Toaster {

    public static final int INDEFINITE = Snackbar.LENGTH_INDEFINITE;
    public static final int LONG = Snackbar.LENGTH_LONG;
    public static final int SHORT = Snackbar.LENGTH_SHORT;

    public static void show(View view, String text, int duration) {
        final Snackbar snackbar = Snackbar.make(view, text, duration);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id
                .snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(15);
        snackbar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    /*public static void show(View view, int res, int duration) {
        show(vi ew, ha.getContext().getResources().getString(res), duration);
    }*/

    public static void show(View view, String text) {
        show(view, text, Snackbar.LENGTH_LONG);
    }

    /*public static void show(View view, int res) {
        show(view, MifosSelfServiceApp.getContext().getResources().getString(res));
    } */
}
