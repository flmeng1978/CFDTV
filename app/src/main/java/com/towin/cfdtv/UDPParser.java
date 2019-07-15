package com.towin.cfdtv;

import android.app.AlertDialog;

import java.io.UnsupportedEncodingException;

/*==========================================================
  类： UDPParser
  描述：解析udp组播数据包
  备注：
===========================================================*/
public class UDPParser {

    //返回值null：表明无效包或错包
    public static ScreenInfo parse(byte[] data) {
        UDPMsg udpMsg = new UDPMsg();
        ScreenInfo info;

        //UDP头部消息
        udpMsg.StartCode = BitConverter.toInt(data, 0);
        udpMsg.Type = BitConverter.toInt(data, 4);
        udpMsg.Length = BitConverter.toInt(data, 8);
        udpMsg.Version = BitConverter.toInt(data, 12);

        //UDP消息正文
        int contentStart = 16;
        udpMsg.PlanCapacity = BitConverter.toInt(data,contentStart+0);  //计划产能
        udpMsg.CurrCapacity = BitConverter.toInt(data,contentStart+4);  //当前产能
        int typeMsgLen = 0;
        for(int i=contentStart+8;;i++){
            typeMsgLen++;
            if(data[i]=='\0') //当前类型信息为可变长度，以'\0'结束
                break;
        }
        try {
            byte bits[] = BitConverter.copyFrom(data,contentStart+8,typeMsgLen);
            udpMsg.CurrType = new String(bits,"UTF-8");     //当前型号
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        //
        int IPMsgLen = 0;
        for(int i=contentStart+8+typeMsgLen;;i++){
            IPMsgLen++;
            if(data[i]=='\0') //当前类型信息为可变长度，以'\0'结束
                break;
        }
        try {
            byte bits[] = BitConverter.copyFrom(data,contentStart+8+typeMsgLen,IPMsgLen);
            udpMsg.station = new String(bits,"UTF-8");         //工位
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        String ip = IP_Station_MAP.getIP(udpMsg.station);  //根据工位查询对应的大屏电视ip

        if(ip==null) return null;  //无效包

        if(ip.equals(LocalHost.getHostIP())) {      //是传给本大屏的信息吗
           info = new ScreenInfo(0,0,"-");
            info.PlanCapacity = udpMsg.PlanCapacity;
            info.CurrCapacity = udpMsg.CurrCapacity;
            info.CurrType = udpMsg.CurrType;
            return info;
        }else
            return null;    //不是传给本机的包，视为无效包
    }
}
