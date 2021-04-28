package com.example.ipcalc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ipRange extends AppCompatActivity {
    public  static  String ipAddr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip_range);

        ipAddr = MainActivity.getIp();
        System.out.println(ipAddr);

        
    }
}