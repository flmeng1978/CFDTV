package com.towin.cfdtv;

import java.util.HashMap;

/*==========================================================
  类： IP_Station_MAP
  描述：大屏电视ip和工位映射
  备注：因为udp组播信息发给所有大屏，所以通过读取udp组播信息中的工位
        信息，映射为ip信息，比对本大屏IP，过滤出本大屏要显示的信息
===========================================================*/
public class IP_Station_MAP {
    private static HashMap<String,String> map;

    public static String getIP(String station){
        map = new HashMap<String,String>();
        map.put("射蜡"+'\0',"10.10.2.10");
        map.put("修模"+'\0',"10.10.3.10");
        map.put("組树"+'\0',"10.10.5.10");
        map.put("清洗"+'\0',"10.10.7.10");
        return map.get(station);
    }
}
