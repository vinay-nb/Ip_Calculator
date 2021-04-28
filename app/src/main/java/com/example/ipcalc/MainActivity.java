package com.example.ipcalc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText ip_addr, nosubnet;
    Button cal;
    public static String ipAddr;
    public static Double subNet;
    public static String getIp() {
        return ipAddr;
    }
    public static Double getSubnet() {
        return subNet;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ip_addr = findViewById(R.id.ipaddr);
        nosubnet = findViewById(R.id.sub);
        cal =  findViewById(R.id.cal);
        cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ip_addr.getText().toString().length() == 0) {
                    Toast.makeText(MainActivity.this, "enter the ip address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!validate(ip_addr.getText().toString())) {
                    Toast.makeText(MainActivity.this, "enter valid ip address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(nosubnet.getText().toString().length() == 0) {
                    Toast.makeText(MainActivity.this, "enter subnet", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Intent i = new Intent(MainActivity.this, IpCalculation.class);
                    ipAddr = ip_addr.getText().toString();
                    subNet = Double.parseDouble(nosubnet.getText().toString());
                    startActivity(i);
                }
            }
        });

    }
    public static boolean validate(String ipAddr) {
        String PATTERN = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";
        return ipAddr.matches(PATTERN);
    }
}