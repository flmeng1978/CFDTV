package com.towin.cfdtv;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
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
    //电视大屏显示信息
    private TextView tv_planCapacityNum;    //计划产能
    private TextView tv_currCapacityNum;    //当前产能
    private TextView tv_currTypeText;       //当前型号

    //接收udp组播信息更新大屏信息
    private UDP udp;
    private int mcPort = 6000;              //组播端口
    private String mcAddr = "224.1.1.1";    //组播地址

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

        //获取TexView控件对象
        tv_planCapacityNum = (TextView)findViewById(R.id.tv_planCapacityNum);
        tv_currCapacityNum = (TextView)findViewById(R.id.tv_currCapacityNum);
        tv_currTypeText = (TextView)findViewById(R.id.tv_currTypeText);

        //初始化大屏显示信息
        info = new ScreenInfo(0,0, "-");

        //开始监听组播信息
        udp = new UDP(this, mcPort, mcAddr);
        udp.listen(true);

        //必须开启线程异步更新Texview
        Thread t=new Thread(){
            @Override
            public void run(){
            while(!isInterrupted()){
                try {
                    Thread.sleep(100);  //1000ms = 1 sec
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        if(info!=null) {
                            tv_planCapacityNum.setText(String.valueOf(info.PlanCapacity));
                            tv_currCapacityNum.setText(String.valueOf(info.CurrCapacity));
                            tv_currTypeText.setText(info.CurrType);
                        }
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            }
        };
        t.start();

//        new AlertDialog.Builder(MainActivity.this)
//                .setTitle("本机IP")
//                .setMessage(LocalHost.getHostIP())
//                .setPositiveButton("确定", null)
//                .show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        multicastLock.release(); //释放多播权限
        udp.dispose();  //离开多播组

    }

    //注意检查本机防火墙是否允许java平台接收UDP消息！！
    public void receive( byte[] data ) {
        info = UDPParser.parse(data);
    }
}