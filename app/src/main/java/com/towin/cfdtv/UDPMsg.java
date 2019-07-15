package com.towin.cfdtv;

/*==========================================================
  类： UDPMsg
  描述：UDP数据包格式
  备注：
===========================================================*/
public class UDPMsg {
    public int StartCode;
    public int Type;
    public int Length;
    public int Version;

    public int PlanCapacity;    //计划产能
    public int CurrCapacity;    //当前产能
    public String CurrType;     //当前型号  格式："xx型号"+'\0'
    public String station;      //当前工位  格式："射蜡"+'\0'、"修模"+'\0'、"組树"+'\0'、"清洗"+'\0'
}
