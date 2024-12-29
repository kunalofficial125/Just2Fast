package com.just2fast.ushop;

import android.os.Parcel;
import android.os.Parcelable;

public class Address_Model implements Parcelable {
    public String name, house, road, lastAddress, city, state ,ph , pinCode,area,AddressId;
    public  Address_Model(String name, String ph, String house , String road, String lastAddress, String AddressId){
        this.name = name;
        this.ph = ph;
        this.house = house;
        this.road = road;

        this.lastAddress = lastAddress;
        this.AddressId = AddressId;

    }
    public Address_Model(){}

    protected Address_Model(Parcel in) {
        name = in.readString();
        house = in.readString();
        road = in.readString();
        lastAddress = in.readString();
        ph = in.readString();
        AddressId = in.readString();
    }

    public static final Creator<Address_Model> CREATOR = new Creator<Address_Model>() {
        @Override
        public Address_Model createFromParcel(Parcel in) {
            return new Address_Model(in);
        }

        @Override
        public Address_Model[] newArray(int size) {
            return new Address_Model[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(house);
        dest.writeString(road);
        dest.writeString(lastAddress);
        dest.writeString(ph);
        dest.writeString(AddressId);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
