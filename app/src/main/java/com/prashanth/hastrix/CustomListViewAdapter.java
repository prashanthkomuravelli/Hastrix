package com.prashanth.hastrix;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Satyam on 30-03-2017.
 */

public class CustomListViewAdapter extends BaseAdapter {

    ArrayList<SingleProductDetails> list;
    String [] productNames;
    Context context;

    public CustomListViewAdapter(Context context){

        this.context = context;
        list = new ArrayList<SingleProductDetails>();

        productNames = context.getResources().getStringArray(R.array.ProductNames);
        for(int i =0;i<productNames.length;i++)
        {
            list.add(new SingleProductDetails(productNames[i],"Quantity Selected",0));
        }



    }

    @Override
    public int getCount() {
        return productNames.length; //or list.size() can also be used
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row= layoutInflater.inflate(R.layout.singlerow_listview,viewGroup,false);
        TextView tvProductName = (TextView) row.findViewById(R.id.tvPname);
        //TextView tvQuantMsg = (TextView) row.findViewById(R.id.tvQuantMsg);
        TextView tvQuantity = (TextView) row.findViewById(R.id.tvQuantity);

        SingleProductDetails temp = list.get(i);
        tvProductName.setText(temp.getProductName());
       // tvQuantMsg.setText(temp.getQuantityMsg());
        tvQuantity.setText(String.valueOf(temp.getQuantity()));

        return row;
    }
}

