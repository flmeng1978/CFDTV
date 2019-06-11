package com.towin.cfdtv;

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
