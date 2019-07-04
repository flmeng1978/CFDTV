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

    public int PlanCapacity;
    public int CurrCapacity;
    public String CurrType;
}
