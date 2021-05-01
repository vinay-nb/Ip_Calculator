package com.example.ipcalc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ipRange extends AppCompatActivity {
    TextView ipRange;
    LinearLayout l, l1, l2, l3;
    public  static  String ipAddr;
    public  int ip[] = new int[4];
    public int netIp[] = new int[4];
    public int endIp[] = new int[4];
    public int broadCastIp[] = new int[4];
    public int startIp[] = new int[4];

    public static int numberOfHostsBits;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip_range);
        l = findViewById(R.id.lenar);
        l1 = findViewById(R.id.l1);
        l2 = findViewById(R.id.l2);
        l3 = findViewById(R.id.l3);
        ipAddr = MainActivity.getIp();
        System.out.println("Ip address in IP range page "+ipAddr);
        numberOfHostsBits = IpCalculation.getHostsBits();
        System.out.println("Hosts bits fecthed in ipRange page "+numberOfHostsBits);

        String ip1[] = ipAddr.split("\\.");
        for(int i=0; i<4; i++) {
            ip[i] = Integer.parseInt(ip1[i]);
        }

        calculateRange(ip, (numberOfHostsBits+2));

    }

    private void calculateRange(int[] ip, int numberOfHostsBits) {
        int n = ip.length;
        System.out.println("Ip address  after typecasting");
        for(int i=0; i<ip.length; i++) {
            System.out.println(ip[i]);
        }
        System.out.println("networkip"+" "+"startip"+" "+"endip"+" "+"broadCastIp");
        for(int i=0; i<4; i++) {
            System.out.println("Network ip"+i);
            TextView[] tv2 = new TextView[n];
            for (int j=0; j<n; j++) {
                netIp[j] = ip[j];
                System.out.print(netIp[j]+".");
                tv2[j] = new TextView(getBaseContext());
                tv2[j].append(String.valueOf(netIp[j]));
                tv2[j].append(String.valueOf("\n"));
                l2.addView(tv2[j]);
            }
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
            TextView[] tv = new TextView[n];
            for(int y=0; y<n; y++) {
                System.out.print(startIp[y]+".");
                tv[y] = new TextView(getBaseContext());
                tv[y].append(String.valueOf(startIp[y]));
                tv[y].append(String.valueOf("\n"));
                l.addView(tv[y]);
            }
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

            System.out.println("BroadCast ip"+i);
            TextView[] tv3 = new TextView[n];
            for(int z1 = 0; z1<n; z1++) {
                System.out.println(broadCastIp[z1]);
                tv3[z1] = new TextView(getBaseContext());
                tv3[z1].append(String.valueOf(broadCastIp[z1]));
                tv3[z1].append(String.valueOf("\n"));
                l3.addView(tv3[z1]);
            }
            System.out.println("end ip"+i);
            for(int z2=0; z2<n; z2++) {
                endIp[z2] = broadCastIp[z2];
            }
            endIp[n-1] -= 1;
            TextView[] tv1 = new TextView[n];
            for(int z3=0; z3<n; z3++) {
                System.out.println(endIp[z3]);
                tv1[z3] = new TextView(getBaseContext());
                tv1[z3].append(String.valueOf(endIp[z3]));
                tv1[z3].append(String.valueOf("\n"));
//                tv1[z3].setTextSize(16f);
                l1.addView(tv1[z3]);
            }
            ip[n-1] = broadCastIp[n-1] + 1;


        }
    }

}