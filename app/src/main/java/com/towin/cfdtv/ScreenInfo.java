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

    public int PlanCapacity;    //计划产能
    public int CurrCapacity;    //当前产能
    public String CurrType;     //当前型号
}
