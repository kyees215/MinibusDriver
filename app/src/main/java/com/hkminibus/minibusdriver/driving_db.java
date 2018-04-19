package com.hkminibus.minibusdriver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jasmine on 18/4/2018.
 */

public class driving_db {
    private String carSize;
    private boolean full;
    private double lan;
    private double lng;
    private String mPlateNo;
    private String mRouteName;
    private String mRouteNo;
    private boolean nextStop;
    private List<stop_data> stopList = new ArrayList<stop_data>();
    private String type;

    public driving_db() {}

    public driving_db(String carSize, boolean full, double lan, double lng, String mPlateNo,
                      String mRouteName, String mRouteNo, boolean nextStop, List<stop_data> stopList,String type){
        this.carSize = carSize;
        this.full = full;
        this.lan = lan;
        this.lng = lng;
        this.mPlateNo = mPlateNo;
        this.mRouteName = mRouteName;
        this.mRouteNo = mRouteNo;
        this.nextStop = nextStop;
        this.stopList = stopList;
        this.type = type;
    }

    public String getCarSize() {return carSize;}
    public boolean isFull() {return full;}
    public double getLan() {return lan;}
    public double getLng() {return lng;}
    public String getmPlateNo() {return mPlateNo;}
    public String getmRouteName() {return mRouteName;}
    public String getmRouteNo() {return mRouteNo;}
    public boolean isNextStop() {return nextStop;}
    public List<stop_data> getstopList() {return stopList;}
    public String getType() {return type;}
}