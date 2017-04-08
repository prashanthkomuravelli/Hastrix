package com.prashanth.hastrix;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.Integer.parseInt;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener
        //implements NavigationView.OnNavigationItemSelectedListener
{

    ListView listView;
    CustomListViewAdapter customListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.lvMain);
        customListViewAdapter = new CustomListViewAdapter(this);
        listView.setAdapter(customListViewAdapter);
        listView.setOnItemClickListener(this);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);*/
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

    /*
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
    }*/
}
