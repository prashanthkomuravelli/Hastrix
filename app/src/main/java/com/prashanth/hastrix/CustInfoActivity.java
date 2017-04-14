package com.prashanth.hastrix;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

public class CustInfoActivity extends AppCompatActivity {

    EditText etCName,etCNumber,etCEmail;
    Boolean validation = true;

    private PdfPCell cell;
    private Image bgImage;
    MainActivity mainActivity;
    BaseColor myColor = WebColors.getRGBColor("#9E9E9E");
    BaseColor myColor1 = WebColors.getRGBColor("#757575");
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
                Drawable myImage = CustInfoActivity.this.getResources().getDrawable(R.drawable.logo2);
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
                int sn=1;

           /* for (int i = 1; i <= 10; i++) {
                table.addCell(String.valueOf(i));
                table.addCell("Header 1 row " + i);
                table.addCell("Header 2 row " + i);
                table.addCell("Header 3 row " + i);
                table.addCell("Header 4 row " + i);
                table.addCell("Header 5 row " + i);

            } */

                for(int i=0;i<mainActivity.customListViewAdapter.productNames.length;i++) {
                    if(mainActivity.customListViewAdapter.list.get(i).getQuantity()!=0) {
                        table.addCell(String.valueOf(sn));
                        table.addCell(mainActivity.customListViewAdapter.list.get(i).getProductName());
                        table.addCell(Integer.toString(mainActivity.customListViewAdapter.list.get(i).getQuantity()));
                        table.addCell(Integer.toString(mainActivity.costs[i]));
                        table.addCell(Integer.toString(mainActivity.total_costs[i]));
                        total=total+mainActivity.total_costs[i];
                        sn+=1;
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

            /*for(int i=0;i<customListViewAdapter1.productNames.length;i++) {
                final int quantity;

                String str = customListViewAdapter1.list.get(i).getProductName() + "    " +customListViewAdapter1.list.get(i).getQuantity();

                quantity = customListViewAdapter1.list.get(i).getQuantity();

                text=text +"\n" + str+ " " +mainActivity.total_costs[i];


            } */
            File f = new File(outPath);
            Uri URI = Uri.fromFile(f);

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("message/rfc822");

            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"hastrixautomation123@gmail.com",etCEmail.getText().toString()});
            intent.putExtra(Intent.EXTRA_SUBJECT, "order");
            intent.putExtra(Intent.EXTRA_STREAM ,URI );


            startActivity(Intent.createChooser(intent, "Send Email"));


        }


    }

}
