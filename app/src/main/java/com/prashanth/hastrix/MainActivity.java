package com.prashanth.hastrix;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import static java.lang.Integer.parseInt;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener
        , NavigationView.OnNavigationItemSelectedListener
{

    ListView listView;
    CustomListViewAdapter customListViewAdapter;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference,mDatabaseReference1;
    private Firebase mRef;
    File myFile;

    BaseColor myColor = WebColors.getRGBColor("#9E9E9E");
    BaseColor myColor1 = WebColors.getRGBColor("#757575");
    //ArrayList<Integer> total_costs;
    public int[] total_costs ;
    public int[] costs;
    /*PdfDocument document = new PdfDocument();
    PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(100,100, 1).create();
    PdfDocument.Page page = document.startPage(pageInfo); */
    private String path;
    private Image bgImage;
    private File dir;
    private File file;
    private PdfPCell cell;





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
        costs = new int[customListViewAdapter.productNames.length];




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
        CheckBox cbDialog = (CheckBox) view.findViewById(R.id.cbDialog);
        final EditText etNewPriceDialog = (EditText) view.findViewById(R.id.etNewPriceDialog);

        builder.setView(view).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int num = parseInt(etQuantityDialog.getText().toString());
                float newPrice = Float.parseFloat(etNewPriceDialog.getText().toString());
                if(num>0) {
                    tvQuantMsg.setVisibility(View.VISIBLE);
                    tvQuant.setVisibility(View.VISIBLE);
                    tvQuant.setText(String.valueOf(num));
                    customListViewAdapter.list.get(index).setQuantity(num);

                    if (newPrice > 0) {
                        customListViewAdapter.list.get(index).setNewPrice(newPrice);
                        Toast.makeText(getApplicationContext(), String.valueOf(customListViewAdapter.list.get(index).getNewPrice()), Toast.LENGTH_SHORT).show();
                    }

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
                            costs[index] = Integer.parseInt(value);
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
                customListViewAdapter.list.get(index).setNewPrice(0);
                Toast.makeText(getApplicationContext(), String.valueOf(customListViewAdapter.list.get(index).getNewPrice()), Toast.LENGTH_SHORT).show();
            }
        });
        Log.i("Array value",Integer.toString(total_costs[index]));
        tvPnameDialog.setText(customListViewAdapter.list.get(index).getProductName());
        etQuantityDialog.setText("0");
        etNewPriceDialog.setText("0");
        builder.show();
        cbDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    CheckBox i = (CheckBox)view;
                if(i.isChecked()==true){
                    etNewPriceDialog.setVisibility(View.VISIBLE);
                    etNewPriceDialog.setText("");
                }
                else{
                    etNewPriceDialog.setVisibility(View.INVISIBLE);
                }

            }
        });

        ivPlusDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int num = parseInt(etQuantityDialog.getText().toString());
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
                int num = parseInt(etQuantityDialog.getText().toString());
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

    public void placeOrder(View view) throws DocumentException {
        while (costs[0]==0) {


        }

        String text = " ";

        Document doc= new Document() ;
        String outPath = Environment.getExternalStorageDirectory() + "/mypdf.pdf" ;
        long total=0;
        try {
            PdfWriter.getInstance(doc,new FileOutputStream(outPath));
            doc.open();
            PdfPTable pt = new PdfPTable(2);
            pt.setWidthPercentage(100);
            float[] fl = new float[]{20, 80};
            pt.setWidths(fl);
            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);
            Drawable myImage = MainActivity.this.getResources().getDrawable(R.drawable.logo2);
            Bitmap bitmap = ((BitmapDrawable) myImage).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bitmapdata = stream.toByteArray();
            bgImage = Image.getInstance(bitmapdata);
            bgImage.setAbsolutePosition(330f, 642f);
            cell.addElement(bgImage);
            pt.addCell(cell);
            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);
            cell.addElement(new Paragraph("Hastrix Automation"));
            cell.addElement(new Paragraph(""));
            cell.addElement(new Paragraph(""));
            pt.addCell(cell);
            /*cell = new PdfPCell(new Paragraph(""));
            cell.setBorder(Rectangle.NO_BORDER);
            pt.addCell(cell); */

            PdfPTable pTable = new PdfPTable(1);
            pTable.setWidthPercentage(100);
            cell = new PdfPCell();
            cell.setColspan(1);
            cell.addElement(pt);
            pTable.addCell(cell);
            PdfPTable table = new PdfPTable(5);

            float[] columnWidth = new float[]{15,40,15,15,15};
            table.setWidths(columnWidth);


            cell = new PdfPCell();


            cell.setBackgroundColor(myColor);
            cell.setColspan(4);
            cell.addElement(pTable);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" "));
            cell.setColspan(4);
            table.addCell(cell);
            cell = new PdfPCell();
            cell.setColspan(4);

            cell.setBackgroundColor(myColor1);

            cell = new PdfPCell(new Phrase("#"));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Product Name"));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Quantity"));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Price per item"));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Price"));
            cell.setBackgroundColor(myColor1);
            table.addCell(cell);

            //table.setHeaderRows(3);
            cell = new PdfPCell();
            cell.setColspan(5);

           /* for (int i = 1; i <= 10; i++) {
                table.addCell(String.valueOf(i));
                table.addCell("Header 1 row " + i);
                table.addCell("Header 2 row " + i);
                table.addCell("Header 3 row " + i);
                table.addCell("Header 4 row " + i);
                table.addCell("Header 5 row " + i);

            } */
            for(int i=0;i<customListViewAdapter.productNames.length;i++) {
                if(customListViewAdapter.list.get(i).getQuantity()!=0) {
                    table.addCell(String.valueOf(i));
                    table.addCell(customListViewAdapter.list.get(i).getProductName());
                    table.addCell(Integer.toString(customListViewAdapter.list.get(i).getQuantity()));
                    table.addCell(Integer.toString(costs[i]));
                    table.addCell(Integer.toString(total_costs[i]));
                    total=total+total_costs[i];
                }
                /*String str = customListViewAdapter.list.get(i).getProductName() + "  " +customListViewAdapter.list.get(i).getQuantity();
                doc.add(new Paragraph(str+Integer.toString(total_costs[i]))); */
            }

            PdfPTable ftable = new PdfPTable(5);
            ftable.setWidthPercentage(100);
            float[] columnWidthaa = new float[]{15,40,15,15,15};
            ftable.setWidths(columnWidthaa);
            cell = new PdfPCell();
            cell.setColspan(5);
            cell.setBackgroundColor(myColor1);
            cell = new PdfPCell(new Phrase("Total cost" ));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
            cell = new PdfPCell(new Phrase( String.valueOf(total)));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell);
            /*cell = new PdfPCell(new Phrase(""));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(myColor1);
            ftable.addCell(cell); */
            cell = new PdfPCell(new Paragraph("Footer"));
            cell.setColspan(5);
            ftable.addCell(cell);
            cell = new PdfPCell();
            cell.setColspan(5);
            cell.addElement(ftable);
            table.addCell(cell);
            doc.add(table);



            doc.close();
            Toast.makeText(this, "pdf created", Toast.LENGTH_SHORT).show();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(int i=0;i<customListViewAdapter.productNames.length;i++) {
            final int quantity;

            String str = customListViewAdapter.list.get(i).getProductName() + "    " +customListViewAdapter.list.get(i).getQuantity();

            quantity = customListViewAdapter.list.get(i).getQuantity();

            text=text +"\n" + str+ " " +total_costs[i];


        }
        File f = new File(outPath);
        Uri URI = Uri.fromFile(f);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");

        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"hastrixautomation123@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "order");
        intent.putExtra(Intent.EXTRA_STREAM ,URI );


        startActivity(Intent.createChooser(intent, "Send Email"));




    }

    public void goToCustActivity(View view){
        Intent custInfoIntent = new Intent(getApplicationContext(),CustInfoActivity.class);
        startActivity(custInfoIntent);
    }

}
