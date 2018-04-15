package com.hkminibus.minibusdriver;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class driver_data implements Parcelable {
    private String id;
    private String password;
    private String name;

    public driver_data(String id, String password, String name){
        this.id = id;
        this.password = password;
        this.name = name;
    }

    public driver_data(){}
    public String getName(){return name;}
    public String getId() {return id;}
    public String getPassword() {return password;}
    public boolean validatePassword(String tmp){
        boolean correct = false;
            if (password.matches(tmp)){
            correct = true;
        }
        return correct;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(id);
        parcel.writeString(password);
    }

    public driver_data(Parcel in){
        name = in.readString();
        id = in.readString();
        password = in.readString();
    }

    public static final Parcelable.Creator<driver_data> CREATOR = new Parcelable.Creator<driver_data>()
    {
        public driver_data createFromParcel(Parcel in)
        {
            return new driver_data(in);
        }
        public driver_data[] newArray(int size) {return new driver_data[size];}
    };
}