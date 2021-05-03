package com.example.ipcalc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class IpCalculation extends AppCompatActivity {
    TextView ip_addr, netmask, ip_class, ipNetmask, numberHosts;
    Button next;
    public  static String ipAddress, ipAddr;
    public static Double subNet;
    public static int newNetMask, numberOfHosts, numberOfHosts1, newNetMask1;
    public static int numberofHostBits;
    public static String ipClass;

    /* function to get hostbits */
    public static int getHostsBits() { return numberOfHosts1; }
    public static int getNetMask () { return newNetMask1;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip_calculation);
        ip_addr = findViewById(R.id.ipaddr1);
        netmask = findViewById(R.id.netmask);
        ip_class = findViewById(R.id.classip);
        ipNetmask = findViewById(R.id.ipnetmask);
        next = findViewById(R.id.next);
        numberHosts = findViewById(R.id.hosts);

        ip_addr.setText(MainActivity.getIp());
        ipAddress = ip_addr.getText().toString();

        ipClass = getClass(ipAddress);
        ip_class.setText(ipClass);
        //getting subnet
        subNet = MainActivity.getSubnet();
        numberofHostBits = getNetmaskBits(subNet);
        System.out.println("total no of bits"+numberofHostBits);
        /* getting new netmask */
        newNetMask = getNewNetMask(numberofHostBits);
        System.out.println("new netmask"+newNetMask);
        netmask.setText(Double.toString(newNetMask));
        ipNetmask.setText(ipAddress + "/" + Double.toString(newNetMask));
        numberOfHosts = getNumberOfHosts(numberofHostBits, ipClass);
        numberHosts.setText(Double.toString(numberOfHosts));
        System.out.println("Number of hosts"+numberOfHosts);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(IpCalculation.this, ipRange.class);
                numberOfHosts1 = numberOfHosts;
                newNetMask1 = newNetMask;
                startActivity(i);
            }
        });
    }
/* function to new netmask */
    private int getNewNetMask(int temp) {
        int newMask = 0;
        if(ipClass == "A") {
           newMask = 8 + temp;
        } else if (ipClass == "B") {
            newMask =  16 + temp;
        } else if(ipClass == "C") {
            newMask = 24 + temp;
        } else if(ipClass == "D") {
            newMask = 32 + temp;
        }
        return  newMask;
    }

/* function to netmask bits */
    private int getNetmaskBits(Double subNet) {
        int val;
        for(int i = 1; i<= 10; i++) {
            val = (int) Math.pow(2, i);
            if(val == subNet) {
                return i;
            }
            else if(val > subNet) {
                return i;
            }
        }

        return 0;
    }

/* function to find ip class */
    private String getClass(String ipAddress) {
        int index = ipAddress.indexOf('.');
        String ipsub = ipAddress.substring(0,index);
        int ip = Integer.parseInt(ipsub);
        if (ip>=1 && ip<=126)
            return "A";
        else if (ip>=128 && ip<=191)
            return "B";
        else if (ip>=192 && ip<223)
            return "C";
        else if (ip >=224 && ip<=239)
            return "D";
        else
            return "E";
    }

/* to find number of host bits */
    private  int getNumberOfHosts(int newNetMask, String ipClass) {
        double ans = 0;
        int val;
        if(ipClass == "A") {
            val = 8 - newNetMask;
            ans = (Math.pow(2, val)) - 2;
        } else if(ipClass == "B") {
            val = 8 - newNetMask;
            ans = (Math.pow(2, val)) - 2;
        } else if(ipClass == "C") {
            val = 8 - newNetMask;
            ans = (Math.pow(2, val)) - 2;
        }
        return (int) ans;
    }
}

