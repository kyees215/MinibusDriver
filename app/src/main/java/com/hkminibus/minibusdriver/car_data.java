package com.hkminibus.minibusdriver;

import android.os.Parcel;
import android.os.Parcelable;

public class car_data implements Parcelable {
    private String mPlateNo;
    private String carType;
    private String mcolor;

    public car_data(String mPlateNo, String carType, String mcolor){
        this.mPlateNo = mPlateNo;
        this.carType = carType;
        this.mcolor = mcolor;
    }

    public car_data(){}
    public String getmPlateNo(){return mPlateNo;}
    public String getCarType() {return carType;}
    public String getMcolor() {return mcolor;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mPlateNo);
        parcel.writeString(carType);
        parcel.writeString(mcolor);
    }

    public car_data(Parcel in){
        mPlateNo = in.readString();
        carType = in.readString();
        mcolor = in.readString();
    }

    public static final Parcelable.Creator<car_data> CREATOR = new Parcelable.Creator<car_data>()
    {
        public car_data createFromParcel(Parcel in)
        {
            return new car_data(in);
        }
        public car_data[] newArray(int size) {return new car_data[size];}
    };
}