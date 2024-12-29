package com.just2fast.ushop;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

public class DeliveryPartnerModel implements Parcelable {

    public String name;
    public String ph,devId;
    public String Lat;
    public int OrderAlert;
    public String Long;
    public ArrayList<OrderDetailsForDev> orders;

    public DeliveryPartnerModel(String name,String devId, String ph, String Lat, String Long, ArrayList<OrderDetailsForDev> orders,int OrderAlert) {
        this.name = name;
        this.ph = ph;
        this.Lat = Lat;
        this.devId = devId;
        this.Long = Long;
        this.OrderAlert = OrderAlert;
        this.orders = orders;
    }

    public DeliveryPartnerModel() {}

    protected DeliveryPartnerModel(Parcel in) {
        name = in.readString();
        ph = in.readString();
        OrderAlert = in.readInt();
        Lat = in.readString();
        devId = in.readString();
        Long = in.readString();
        orders = in.createTypedArrayList(OrderDetailsForDev.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(ph);
        dest.writeString(Lat);
        dest.writeString(devId);
        dest.writeInt(OrderAlert);
        dest.writeString(Long);
        dest.writeTypedList(orders);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DeliveryPartnerModel> CREATOR = new Creator<DeliveryPartnerModel>() {
        @Override
        public DeliveryPartnerModel createFromParcel(Parcel in) {
            return new DeliveryPartnerModel(in);
        }

        @Override
        public DeliveryPartnerModel[] newArray(int size) {
            return new DeliveryPartnerModel[size];
        }
    };
}
