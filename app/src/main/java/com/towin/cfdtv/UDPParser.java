package com.towin.cfdtv;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class UDPParser {
    public static ScreenInfo parse(byte[] data) {
        UDPMsg udpMsg = new UDPMsg();

        //UDP头部消息
        udpMsg.StartCode = BitConverter.toInt(data, 0);
        udpMsg.Type = BitConverter.toInt(data, 4);
        udpMsg.Length = BitConverter.toInt(data, 8);
        udpMsg.Version = BitConverter.toInt(data, 12);

        //UDP消息正文
        int contentStart = 16;  //
        udpMsg.PlanCapacity = BitConverter.toInt(data,contentStart+0);
        udpMsg.CurrCapacity = BitConverter.toInt(data,contentStart+4);
        int typeMsgLen = 0;
        for(int i=contentStart+8;;i++){
            typeMsgLen++;
            if(data[i]=='\0') //当前类型信息为可变长度，以'\0'结束
                break;
        }
        try {
            byte bits[] = BitConverter.copyFrom(data,contentStart+8,typeMsgLen);
            udpMsg.CurrType = new String(bits,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        ScreenInfo info = new ScreenInfo(0,0,"-");
        info.PlanCapacity = udpMsg.PlanCapacity;
        info.CurrCapacity = udpMsg.CurrCapacity;
        info.CurrType = udpMsg.CurrType;
        return info;
    }
}
