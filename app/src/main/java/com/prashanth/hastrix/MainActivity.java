package com.prashanth.hastrix;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.client.utilities.Base64;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener
        , NavigationView.OnNavigationItemSelectedListener
{

    ListView listView;
    CustomListViewAdapter customListViewAdapter;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference,mDatabaseReference1;
    private Firebase mRef;
    File myFile;
    //ArrayList<Integer> total_costs;
    public int[] total_costs ;
    /*PdfDocument document = new PdfDocument();
    PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(100,100, 1).create();
    PdfDocument.Page page = document.startPage(pageInfo); */





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.lvMain);
        customListViewAdapter = new CustomListViewAdapter(this);
        listView.setAdapter(customListViewAdapter);
        listView.setOnItemClickListener(this);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("products");
        //total_costs = new ArrayList<Integer>();
        total_costs = new int[customListViewAdapter.productNames.length];




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View v, final int index, long l) {
        final TextView tvQuant = (TextView) v.findViewById(R.id.tvQuantity);
        final TextView tvQuantMsg = (TextView) v.findViewById(R.id.tvQuantMsg);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.quantity_dialog,null);

        TextView tvPnameDialog = (TextView) view.findViewById(R.id.tvPnameDialog);
        final EditText etQuantityDialog = (EditText) view.findViewById(R.id.etQuantityDialog);
        ImageView ivPlusDialog = (ImageView) view.findViewById(R.id.ivPlus);
        ImageView ivMinusDialog = (ImageView) view.findViewById(R.id.ivMinus);

        builder.setView(view).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int num = Integer.parseInt(etQuantityDialog.getText().toString());
                if(num>0) {
                    tvQuantMsg.setVisibility(View.VISIBLE);
                    tvQuant.setVisibility(View.VISIBLE);
                    tvQuant.setText(String.valueOf(num));
                    customListViewAdapter.list.get(index).setQuantity(num);
                    Log.i("product name",customListViewAdapter.list.get(index).getProductName());
                    mRef = new Firebase("https://hastrix-14836.firebaseio.com/products/"+customListViewAdapter.list.get(index).getProductName());
                    mRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                            String value = dataSnapshot.getValue(String.class);
                            Log.i("product cost",value);
                            int total_cost;
                            total_cost = customListViewAdapter.list.get(index).getQuantity() * Integer.parseInt(value);
                            Log.i("product total cost of "+customListViewAdapter.list.get(index).getProductName(),Integer.toString(total_cost));
                            total_costs[index]=total_cost;
                            Log.i("Array value 1",Integer.toString(total_costs[index]));
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });

                }
            }
        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                /*tvQuant.setVisibility(View.INVISIBLE);
                tvQuantMsg.setVisibility(View.INVISIBLE);*/
                tvQuant.setText(String.valueOf(0));
                customListViewAdapter.list.get(index).setQuantity(0);
            }
        });
        Log.i("Array value",Integer.toString(total_costs[index]));
        tvPnameDialog.setText(customListViewAdapter.list.get(index).getProductName());
        etQuantityDialog.setText("0");
        builder.show();
        ivPlusDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int num = Integer.parseInt(etQuantityDialog.getText().toString());
                if(num>=5)
                    Toast.makeText(getApplicationContext(),"Maximum Quantity Selected ",Toast.LENGTH_SHORT).show();
                else {
                    num=num+1;
                    etQuantityDialog.setText(String.valueOf(num));
                }
            }
        });
        ivMinusDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int num = Integer.parseInt(etQuantityDialog.getText().toString());
                if(num==0)
                    Toast.makeText(getApplicationContext(),"Minimum Quantity Selected",Toast.LENGTH_SHORT).show();
                else {
                    num=num-1;
                    etQuantityDialog.setText(String.valueOf(num));
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_signout) {
            AuthUI.getInstance().signOut(this);
            Intent in = new Intent(this, LoginActivity.class);
            startActivity(in);
            MainActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void placeOrder(View view){
        String text = " ";

        for(int i=0;i<customListViewAdapter.productNames.length;i++) {
            final int quantity;

            String str = customListViewAdapter.list.get(i).getProductName() + "    " +customListViewAdapter.list.get(i).getQuantity();

            quantity = customListViewAdapter.list.get(i).getQuantity();

            text=text +"\n" + str+ " " +total_costs[i];


        }

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"hastrixautomation123@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "order");
        intent.putExtra(Intent.EXTRA_TEXT, text);

        startActivity(Intent.createChooser(intent, "Send Email"));
        //Intent in = new Intent(this , order.class);
        //startActivity(in); */

        /*com.itextpdf.text.Document document = new com.itextpdf.text.Document();

        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/vindroid";

            File dir = new File(path);
            if(!dir.exists()) {
                dir.mkdir();
            }

            Log.d("PDFCreator", "PDF Path: " + path);


            File file = new File(dir, "sample.pdf");
            FileOutputStream fOut = new FileOutputStream(file);

            PdfWriter.getInstance(document, fOut);

            //open the document
            document.open();


            Paragraph p1 = new Paragraph("Sample PDF CREATION USING IText");
            Font paraFont= new Font(Font.FontFamily.COURIER);
            p1.setAlignment(Paragraph.ALIGN_CENTER);
            p1.setFont(paraFont);

            //add paragraph to document
            document.add(p1);

            Paragraph p2 = new Paragraph("This is an example of a simple paragraph");
            Font paraFont2= new Font(Font.FontFamily.COURIER,14.0f,0, CMYKColor.GREEN);
            p2.setAlignment(Paragraph.ALIGN_CENTER);
            p2.setFont(paraFont2);

            document.add(p2);






        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        }
        finally
        {
            document.close();
        }

       /* View content = findViewById(R.id.lvMain);
        content.draw(page.getCanvas());
        document.finishPage(page); */




    }

}
