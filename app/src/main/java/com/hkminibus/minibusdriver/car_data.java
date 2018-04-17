package com.hkminibus.minibusdriver;

import android.os.Parcel;
import android.os.Parcelable;

public class car_data implements Parcelable {
    private String mPlateNo;
    private String type;
    private String carSize;

    public car_data(String mPlateNo, String type, String carSize){
        this.mPlateNo = mPlateNo;
        this.type = type;
        this.carSize = carSize;
    }

    public car_data(){}
    public String getmPlateNo(){return mPlateNo;}
    public String getType() {return type;}
    public String getCarSize() {return carSize;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mPlateNo);
        parcel.writeString(type);
        parcel.writeString(carSize);
    }

    public car_data(Parcel in){
        mPlateNo = in.readString();
        type = in.readString();
        carSize = in.readString();
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