package com.prashanth.hastrix;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class CustInfoActivity extends AppCompatActivity {

    EditText etCName,etCNumber,etCEmail;
    Boolean validation = true;
    CheckBox cbdis;

    private PdfPCell cell;
    private Image bgImage;
    MainActivity mainActivity;
    LoginActivity loginActivity;
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;
    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 10;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    BaseColor myColor = WebColors.getRGBColor("#9E9E9E");
    BaseColor myColor1 = WebColors.getRGBColor("#757575");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cust_info);
        etCName = (EditText) findViewById(R.id.etCName);
        etCNumber = (EditText) findViewById(R.id.etCNumber);
        etCEmail = (EditText) findViewById(R.id.etCEmail);
        cbdis = (CheckBox) findViewById(R.id.dis);
        permissionStatus = getSharedPreferences("permissionStatus",MODE_PRIVATE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



    }
    private boolean isValidName(String name){
        if(name.equals("")){
            return false;
        }
        else
            return true;
    }
    private boolean isValidMobile(String phone) {
        if(phone.length()!=10)
            return false;
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }
    private boolean isValidMail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public void permissions(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etCEmail.getWindowToken(), 0);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(CustInfoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(CustInfoActivity.this);
                builder.setTitle("Need Storage Permission");
                builder.setMessage("This app needs storage permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(CustInfoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE,false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(CustInfoActivity.this);
                builder.setTitle("Need Storage Permission");
                builder.setMessage("This app needs storage permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(getBaseContext(), "Go to Permissions to Grant Storage", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(CustInfoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
            }

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE,true);
            editor.commit();


        } else {
            //You already have the permission, just go ahead.
            proceedAfterPermission();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void proceedAfterPermission() {
        //We've got the permission, now we can proceed further
        runGmailIntent();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //The External Storage Write Permission is granted to you... Continue your left job...
                proceedAfterPermission();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(CustInfoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //Show Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(CustInfoActivity.this);
                    builder.setTitle("Need Storage Permission");
                    builder.setMessage("This app needs storage permission");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();


                            ActivityCompat.requestPermissions(CustInfoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10);


                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {
                    Toast.makeText(getBaseContext(),"Unable to get Permission",Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    public void runGmailIntent(){
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

        if(validation == true)
        {
            // TODO: 09-04-2017  here add placeOrder methods implementation

            Toast.makeText(getApplicationContext(), "Just send through Gmail.. ", Toast.LENGTH_SHORT).show();


            //TODO: 15/04/2017  here is the new code for pdf , plz see and modify to get selected device and quantity


            float total = 0f;
            String CustomerName = etCName.getText().toString();
            String CustomerContact = etCNumber.getText().toString();
            String CustomerEmail =etCEmail.getText().toString();
            String BDAUserName =loginActivity.username;
            String address = "IInd Floor,B-C Junction," +
                    "Business Incubation Center,SMVDU Jammu,J&K-182320\n" +
                    "Website: www.hastrix.com\nE-mail: contact@hastrix.com";

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String date = sdf.format(Calendar.getInstance().getTime());
            Document doc = new Document();
            float dis = 0f;
            float install_cost = 0f;


            try {

                //Tried creating particular folder for hastrix and save pdf name as CustomerName+Date
                //But didn't work ,see if you can

                String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Hastrix/PDFs";
                File hastrixFolder = new File(path);
                if(!hastrixFolder.exists()){
                    hastrixFolder.mkdirs();
                }

                String pdfName = CustomerName+date;
                File myPdf = new File(pdfName);
                String outPath = Environment.getExternalStorageDirectory()+"/Hastrix/PDFs/newpdf.pdf";
                //String outPath2 = outPath+".pdf";


                PdfWriter.getInstance(doc,new FileOutputStream(outPath));
                doc.open();



                Font whitefont = new Font();
                whitefont.setColor(BaseColor.WHITE);

                PdfPTable headTable = new PdfPTable(3);
                float[] columnWidth = new float[]{15,80,15};
                headTable.setWidths(columnWidth);
                headTable.setWidthPercentage(100);
                headTable.setHorizontalAlignment(Element.ALIGN_CENTER);

                BaseColor mygreen = new BaseColor(0,153,76);
                PdfPCell headCell = new PdfPCell();
                headCell.setColspan(3);
                headCell.setMinimumHeight(80f);
                headCell.setBackgroundColor(mygreen);


                Paragraph addressPara = new Paragraph(address,whitefont);
                addressPara.setAlignment(Element.ALIGN_CENTER);

                Drawable image = this.getResources().getDrawable(R.drawable.hastrix2);
                Bitmap bitmap = ((BitmapDrawable) image).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] bitmapdata = stream.toByteArray();
                Image logo = Image.getInstance(bitmapdata);
                logo.setAlignment(Element.ALIGN_CENTER);
                logo.setWidthPercentage(50);


                headCell.addElement(logo);
                headCell.addElement(addressPara);
                headCell.setPadding(20);
                headTable.addCell(headCell);

                PdfPCell dateCell = new PdfPCell(new Paragraph("Date: "+date));
                dateCell.setColspan(3);
                dateCell.setPadding(5);
                headTable.addCell(dateCell);

                PdfPCell CustomerNameCell = new PdfPCell(new Paragraph("Customer Name: "+CustomerName));
                CustomerNameCell.setPadding(5);
                CustomerNameCell.setColspan(3);
                headTable.addCell(CustomerNameCell);

                PdfPCell CustomerContactCell = new PdfPCell(new Paragraph("Customer Contact:"+CustomerContact));
                CustomerContactCell.setColspan(3);
                CustomerContactCell.setPadding(5);
                headTable.addCell(CustomerContactCell);

                PdfPCell CustomerEmailCell = new PdfPCell(new Paragraph("Customer Email: "+CustomerEmail));
                CustomerEmailCell.setColspan(3);
                CustomerEmailCell.setPadding(5);
                headTable.addCell(CustomerEmailCell);

                PdfPCell BDACell = new PdfPCell(new Paragraph("BDA Username: "+BDAUserName));
                BDACell.setColspan(3);
                BDACell.setPadding(5);
                headTable.addCell(BDACell);

                Font redboldfont = new Font();
                redboldfont.setSize(10);
                redboldfont.setColor(BaseColor.RED);
                redboldfont.setStyle(Font.BOLD);

                Paragraph paragraph = new Paragraph("S.No", redboldfont);
                PdfPCell cell = new PdfPCell(paragraph);
                cell.setPadding(5);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headTable.addCell(cell);

                paragraph = new Paragraph("Device", redboldfont);
                cell = new PdfPCell(paragraph);
                cell.setPadding(5);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headTable.addCell(cell);

                paragraph = new Paragraph("Quantity", redboldfont);
                cell = new PdfPCell(paragraph);
                cell.setPadding(5);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headTable.addCell(cell);

                //start

            int sn=1;

            for(int i=0;i<mainActivity.customListViewAdapter.productNames.length;i++) {
                if(mainActivity.customListViewAdapter.list.get(i).getQuantity()!=0) {
                    headTable.addCell(String.valueOf(sn));
                    headTable.addCell(mainActivity.customListViewAdapter.list.get(i).getProductName());
                    headTable.addCell(Integer.toString(mainActivity.customListViewAdapter.list.get(i).getQuantity()));
                    total=total+mainActivity.total_costs[i];
                    sn+=1;
                }

            }
            install_cost = total*20/100;

            if(cbdis.isChecked()) {
                dis = total * 10/100;
            }

                Font blueboldfont = new Font();
                blueboldfont.setSize(12);
                blueboldfont.setColor(BaseColor.BLUE);
                blueboldfont.setStyle(Font.BOLD);

                paragraph = new Paragraph("Installation cost:(20% of total)");
                PdfPCell installationCost = new PdfPCell(paragraph);
                installationCost.setColspan(2);
                installationCost.setPadding(5);
                installationCost.setHorizontalAlignment(Element.ALIGN_RIGHT);
                headTable.addCell(installationCost);

                paragraph = new Paragraph(String.valueOf(install_cost));
                installationCost.setBackgroundColor(BaseColor.GREEN);
                installationCost = new PdfPCell(paragraph);
                installationCost.setPadding(5);
                installationCost.setHorizontalAlignment(Element.ALIGN_CENTER);
                headTable.addCell(installationCost);

                paragraph = new Paragraph("Discount:(10% of total)");
                PdfPCell Discount = new PdfPCell(paragraph);
                Discount.setColspan(2);
                Discount.setPadding(5);
                Discount.setHorizontalAlignment(Element.ALIGN_RIGHT);
                headTable.addCell(Discount);

                paragraph = new Paragraph(String.valueOf(dis));
                Discount.setBackgroundColor(BaseColor.GREEN);
                Discount = new PdfPCell(paragraph);
                Discount.setPadding(5);
                Discount.setHorizontalAlignment(Element.ALIGN_CENTER);
                headTable.addCell(Discount);



                paragraph = new Paragraph("Total Cost:");
                PdfPCell totalCostCell = new PdfPCell(paragraph);
                totalCostCell.setColspan(2);
                totalCostCell.setPadding(5);
                totalCostCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                headTable.addCell(totalCostCell);

                paragraph = new Paragraph(String.valueOf(total + install_cost -dis),blueboldfont);
                totalCostCell.setBackgroundColor(BaseColor.GREEN);
                totalCostCell = new PdfPCell(paragraph);
                totalCostCell.setPadding(5);
                totalCostCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headTable.addCell(totalCostCell);

                Paragraph tandcmsg = new Paragraph("* approximate cost is not inclusive of" +
                        "any VAT or any other charges");
                PdfPCell tandcCell = new PdfPCell(tandcmsg);
                tandcCell.setColspan(3);
                tandcCell.setPadding(3);
                headTable.addCell(tandcCell);

                tandcmsg = new Paragraph("Terms of Payment:- 1) 80% of project cost as advance payment");
                tandcCell = new PdfPCell(tandcmsg);
                tandcCell.setColspan(3);
                tandcCell.setPadding(3);
                headTable.addCell(tandcCell);

                tandcmsg = new Paragraph("                   2) The above quote is an estimate may vary on actual requirements");
                tandcCell = new PdfPCell(tandcmsg);
                tandcCell.setColspan(3);
                tandcCell.setPadding(3);
                headTable.addCell(tandcCell);

                tandcmsg = new Paragraph("Services:-         1) 12 months warranty against manufacturing defects");
                tandcCell = new PdfPCell(tandcmsg);
                tandcCell.setColspan(3);
                tandcCell.setPadding(3);
                headTable.addCell(tandcCell);

                tandcmsg = new Paragraph("                   2) Site supervision,customer training and re-programming for a week after installation");
                tandcCell = new PdfPCell(tandcmsg);
                tandcCell.setColspan(3);
                tandcCell.setPadding(3);
                headTable.addCell(tandcCell);

                doc.add(headTable);
                doc.close();

                File f = new File(outPath);
                Uri URI = Uri.fromFile(f);

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");

                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"hastrixautomation123@gmail.com",etCEmail.getText().toString()});
                intent.putExtra(Intent.EXTRA_SUBJECT, "order");
                intent.putExtra(Intent.EXTRA_STREAM ,URI );


                startActivity(Intent.createChooser(intent, "Send Email"));

            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }


    }

}
