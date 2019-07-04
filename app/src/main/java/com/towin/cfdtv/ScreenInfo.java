package com.towin.cfdtv;

/*==========================================================
  类： ScreenInfo
  描述：大屏显示信息内容
  备注：
===========================================================*/
public class ScreenInfo {
    public ScreenInfo(int planCapacity, int currCapacity, String currType) {
        PlanCapacity = planCapacity;
        CurrCapacity = currCapacity;
        CurrType = currType;
    }

    public int PlanCapacity;
    public int CurrCapacity;
    public String CurrType;
}
