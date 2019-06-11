package com.towin.cfdtv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import hypermedia.net.UDP;

public class MainActivity extends AppCompatActivity {
    private TextView tv_planCapacityNum;
    private TextView tv_currCapacityNum;
    private TextView tv_currTypeText;

    private UDP udp;    //接收生产状态消息的udp对象
    private int mcPort = 5000;
    private String mcAddr = "224.1.1.1";

    private WifiManager.MulticastLock multicastLock;

    private ScreenInfo info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //安卓会限制多播，这里获得多播权限
        WifiManager wifi = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        multicastLock = wifi.createMulticastLock("multicastLock");
        multicastLock.setReferenceCounted(true);
        multicastLock.acquire();

        tv_planCapacityNum = (TextView)findViewById(R.id.tv_planCapacityNum);
        tv_currCapacityNum = (TextView)findViewById(R.id.tv_currCapacityNum);
        tv_currTypeText = (TextView)findViewById(R.id.tv_currTypeText);

        info = new ScreenInfo(0,0, "-");

        udp = new UDP(this, mcPort, mcAddr);
        udp.listen(true);

        Thread t=new Thread(){
            @Override
            public void run(){
                while(!isInterrupted()){
                    try {
                        Thread.sleep(100);  //1000ms = 1 sec
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv_planCapacityNum.setText(String.valueOf(info.PlanCapacity));
                                tv_currCapacityNum.setText(String.valueOf(info.CurrCapacity));
                                tv_currTypeText.setText(info.CurrType);
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //释放多播权限
        multicastLock.release();

    }

    //注意检查本机防火墙是否允许java平台接收UDP消息！！
    public void receive( byte[] data ) {
        info = UDPParser.parse(data);
    }
}