package com.hkminibus.minibusdriver;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class route_data implements Parcelable {
    private String mRouteID;
    private String mRouteNo;
    private String mRouteName;
    private String type;
    private String MonToFri;
    private String Sat;
    private String Sun;
    private List<stop_data> mStopList = new ArrayList<stop_data>();

    public route_data(String mRouteID, String mRouteNo, String mRouteName, String type) {
        this.mRouteID = mRouteID;
        this.mRouteNo = mRouteNo;
        this.mRouteName = mRouteName;
        this.type = type;
    }

    public void setmStopList(stop_data s){
        mStopList.add(s);
    }

    public route_data(){}

    public String getmRouteID(){return mRouteID;}
    public String getmRouteNo() {return mRouteNo;}
    public String getmRouteName() {return mRouteName;}
    public String getMonToFri(){return MonToFri;}
    public String getSat() {return Sat;}
    public String getSun() {return Sun;}
    public List<stop_data>getmStopList(){return mStopList;}
    public String getType(){return type;}


    @Override
    public int describeContents() {
        return 0;
    }

    public route_data(Parcel in) {
        mRouteID = in.readString();
        mRouteNo = in.readString();
        mRouteName = in.readString();
        type = in.readString();
        MonToFri = in.readString();
        Sat = in.readString();
        Sun = in.readString();
        mStopList = in.readArrayList(stop_data.class.getClassLoader());
    }


    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(mRouteID);
        parcel.writeString(mRouteNo);
        parcel.writeString(mRouteName);
        parcel.writeString(type);
        parcel.writeString(MonToFri);
        parcel.writeString(Sat);
        parcel.writeString(Sun);
        parcel.writeList(mStopList);
    }

    public static final Parcelable.Creator<route_data> CREATOR = new Parcelable.Creator<route_data>()
    {
        public route_data createFromParcel(Parcel in)
        {
            return new route_data(in);
        }
        public route_data[] newArray(int size)
        {
            return new route_data[size];
        }
    };
    public void clearmStopList() {this.mStopList.clear();}


}

