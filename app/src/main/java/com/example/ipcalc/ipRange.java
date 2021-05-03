package com.example.ipcalc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class ipRange extends AppCompatActivity {
    TextView ipRange;
    Button download;
    TableLayout table, table1, tablenet1, tablebrod;
    public  static  String ipAddr;
    public  int ip[] = new int[4];
    public int netIp[] = new int[4];
    public int endIp[] = new int[4];
    public int broadCastIp[] = new int[4];
    public int startIp[] = new int[4];
    public int pageHeight = 2010;
    public int pageWidth = 1200;
    public int newNetMask;
    public Double subNet;
    Bitmap bmp, scaledbmp;
    public final int PERMISSION_REQUEST_CODE = 200;

    public static int numberOfHostsBits;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip_range);
        table = findViewById(R.id.table1);
        table1 = findViewById(R.id.table2);
        tablenet1 = findViewById(R.id.tablenet);
        tablebrod = findViewById(R.id.tablebroad);
        download = findViewById(R.id.download);
//        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background);
//        scaledbmp = Bitmap.createScaledBitmap(bmp, 140, 140, false);

        /* fetching from main activity */
        ipAddr = MainActivity.getIp();
        System.out.println("Ip address in IP range page "+ipAddr);
        numberOfHostsBits = IpCalculation.getHostsBits();
        System.out.println("Hosts bits fecthed in ipRange page "+numberOfHostsBits);
        newNetMask = IpCalculation.getNetMask();
        subNet = MainActivity.getSubnet();

        /* typecasting */
        String ip1[] = ipAddr.split("\\.");
        for(int i=0; i<4; i++) {
            ip[i] = Integer.parseInt(ip1[i]);
        }
        calculateRange(ip, (numberOfHostsBits+2));

        /*generating pdf*/
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generatePdf(ipAddr, numberOfHostsBits, newNetMask, subNet);
            }
        });
    }
/* to check permission */
    private boolean checkPermission() {
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return (permission1 == PackageManager.PERMISSION_GRANTED) && (permission2 == PackageManager.PERMISSION_GRANTED);
    }
/* to request permissions */
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    private void generatePdf(String ipAddr, int numberOfHostsBits, int newNetMask, double subNet) {
        if (checkPermission() == false) {
            Toast.makeText(getApplicationContext(), "Permisson denied", Toast.LENGTH_SHORT).show();
        } else {
            requestPermissions();
        }
        PdfDocument pd = new PdfDocument();
        Paint p = new Paint();

        PdfDocument.PageInfo myPage = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();
        PdfDocument.Page myPage1 = pd.startPage(myPage);
        Canvas c = myPage1.getCanvas();

        p.setTextAlign(Paint.Align.CENTER);
        p.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        p.setTextSize(70);
        c.drawText("Ip Calculator", pageWidth/2, 270, p);

//        p.setTextAlign(Paint.Align.LEFT);
        p.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        p.setTextSize(45f);
        p.setColor(Color.BLACK);
        c.drawText("Given Ip Adress", 30, 500, p);
        p.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        p.setTextSize(45f);
        p.setColor(Color.BLACK);
        c.drawText(ipAddr, 60, 600, p);

//        p.setTextAlign(Paint.Align.LEFT);
        p.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        p.setTextSize(45f);
        p.setColor(Color.BLACK);
        c.drawText("Given Subnet", 30, 600, p);
        p.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        p.setTextSize(45f);
        p.setColor(Color.BLACK);
        c.drawText(String.valueOf(subNet), 60, 600, p);

        p.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        p.setTextSize(45f);
        p.setColor(Color.BLACK);
        c.drawText("New netmask", 30, 700, p);
        p.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        p.setTextSize(45f);
        p.setColor(Color.BLACK);
        c.drawText(String.valueOf(newNetMask), 60, 700, p);

        p.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        p.setTextSize(45f);
        p.setColor(Color.BLACK);
        c.drawText("Number of host bits", 30, 800, p);
        p.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        p.setTextSize(45f);
        p.setColor(Color.BLACK);
        c.drawText(String.valueOf(numberOfHostsBits), 60, 800, p);


        pd.finishPage(myPage1);
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        try {
            pd.writeTo(new FileOutputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
        pd.close();
    }

    private void calculateRange(int[] ip, int numberOfHostsBits) {
        int n = ip.length;
        System.out.println("Ip address  after typecasting");
        for(int i=0; i<ip.length; i++) {
            System.out.println(ip[i]);
        }
        System.out.println("networkip"+" "+"startip"+" "+"endip"+" "+"broadCastIp");
        for(int i=0; i<4; i++) {
            /* netip calculation */
            System.out.println("Network ip"+i);
            Set<String> hash_net = new HashSet<String>();
            for (int j=0; j<n; j++) {
                netIp[j] = ip[j];
//                System.out.print("network ip"+netIp[i]+".");
//                TextView tv_net = new TextView(this);
//                String value = " ";
//                for(int y12=0; y12<4; y12++) {
//                    value += netIp[y12]+".";
//                }
//                if(hash_net.contains(value)) {
//                    continue;
//                } else {
//                    hash_net.add(value);
//                }
//                System.out.print(netIp[j]+".");
//                System.out.println("string value"+value);
//                tv_net.setText(value);
//                tv_net.setGravity(Gravity.CENTER);
//                tv_net.setTextSize(18);
//                tv_net.setTextColor(Color.BLACK);
//                TableRow tr = new TableRow(this);
//                tr.addView(tv_net);
//                tablenet1.addView(tr);

                TextView tv_net = new TextView(this);
                System.out.print(netIp[j]+".");
                tv_net = new TextView(getBaseContext());
                tv_net.append(String.valueOf(netIp[j]));
//                tv_net.append(String.valueOf("\n"));
                tablenet1.addView(tv_net);

            }
            /* startip calculation */
            for(int x=0; x<n; x++) {
                startIp[x] = ip[x];
            }
            if(startIp[n-1] < 255) {
                startIp[n-1] += 1;
            } else if(startIp[n-1] > 255){
                startIp[n-2] += 1;
                startIp[n-1] = 0;
            }
            System.out.print("Start ip"+i);
            Set<String>  hash_set = new HashSet<String>();
            for(int y=0; y<n; y++) {
                System.out.print(startIp[y]+".");
                TextView tv = new TextView(this);
                String value = " ";
                for(int y1=0; y1<4; y1++) {
                    value += startIp[y1]+".";
                }
                if(hash_set.contains(value)) {
                    continue;
                } else {
                    hash_set.add(value);
                }
                System.out.println("string value"+value);
                tv.setText(value);
                tv.setGravity(Gravity.CENTER);
                tv.setTextSize(18);
                tv.setTextColor(Color.BLACK);
                TableRow tr = new TableRow(this);
                tr.addView(tv);
                table.addView(tr);
            }
            System.out.println("set values"+hash_set);
            /* broadcast p calculation */
            for(int z=0; z<n; z++) {
                broadCastIp[z] = ip[z];
            }
            if(broadCastIp[n-1] < 255) {
                if(broadCastIp[n-1] == 0)
                    broadCastIp[n-1] += numberOfHostsBits - 1;
                else
                    broadCastIp[n-1] += numberOfHostsBits;
            } else if(broadCastIp[n-1] > 255){
                broadCastIp[n-2] +=1;
                broadCastIp[n-1] = 0;
            }
            if(broadCastIp[n-1] > 255) {
                int rem = broadCastIp[n-1] % 255;
                if(rem < 255) {
                    while (rem !=0 ) {
                        broadCastIp[n - 2] += 1;
                        rem -= 1;
                        broadCastIp[n-1] = rem;
                        rem -=1;
                    }
                }
            }

            System.out.println("BroadCast ip"+ broadCastIp[i]);
            Set<String> hash_brod = new HashSet<String>();
            for(int z1=0; z1<n; z1++) {
                TextView tv_brod = new TextView(this);
                String value = " ";
                for(int y12=0; y12<4; y12++) {
                    value += broadCastIp[y12]+".";
                }
                if(hash_brod.contains(value)) {
                    continue;
                } else {
                    hash_brod.add(value);
                }
                System.out.print(broadCastIp[z1]+".");
                System.out.println("string value"+value);
                tv_brod.setText(value);
                tv_brod.setGravity(Gravity.CENTER);
                tv_brod.setTextSize(18);
                tv_brod.setTextColor(Color.BLACK);
                TableRow tr = new TableRow(this);
                tr.addView(tv_brod);
                tablebrod.addView(tr);
            }
            System.out.println("end ip"+i);
            for(int z2=0; z2<n; z2++) {
                endIp[z2] = broadCastIp[z2];
            }
            endIp[n-1] -= 1;
            /* endip calculation */
//            displaying ip calculation
            Set<String>  hash_set1 = new HashSet<String>();
            for(int z3=0; z3<n; z3++) {
                System.out.println(endIp[z3]);
                TextView tv1 = new TextView(this);
                String value = " ";
                for(int y1=0; y1<4; y1++) {
                    value += endIp[y1]+".";
                }
                if(hash_set1.contains(value)) {
                    continue;
                } else {
                    hash_set1.add(value);
                }
                System.out.println("end Ip in string"+value);
                tv1.setText(value);
                tv1.setGravity(Gravity.CENTER);
                tv1.setTextSize(18);
                tv1.setTextColor(Color.BLACK);
                tv1.setPadding(0,0, 0, 1);
                TableRow tr1 = new TableRow(this);
                tr1.addView(tv1);
                table1.addView(tr1);
            }
            ip[n-1] = broadCastIp[n-1] + 1;
        }

    }
}