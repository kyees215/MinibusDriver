package com.hkminibus.minibusdriver;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class stop_data implements Parcelable{
    private String name;
    private double latitude;
    private double longitude;
    private double radius;
    private int rank;

    public stop_data(String name, double latitude, double longitude, double radius, int rank){
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
        this.rank = rank;
    }

    public stop_data(){}
    public String getName(){return name;}
    public double getLatitude() {return latitude;}
    public double getLongitude() {return longitude;}
    public double getRadius(){return radius;}
    public int getRank(){return rank;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeDouble(radius);
        parcel.writeInt(rank);
    }

    public stop_data(Parcel in){
        name = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        radius = in.readDouble();
        rank = in.readInt();
    }

    public static final Parcelable.Creator<stop_data> CREATOR = new Parcelable.Creator<stop_data>()
    {
        public stop_data createFromParcel(Parcel in)
        {
            return new stop_data(in);
        }
        public stop_data[] newArray(int size) {return new stop_data[size];}
    };

    public void setValues(stop_data old, int new_rank) {}
}