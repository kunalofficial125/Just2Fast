package com.just2fast.ushop;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class OrderDetailsForDev implements Parcelable {

    public String ID;
    public ArrayList<String> title;
    public ArrayList<String> color;
    public ArrayList<String> size;
    public ArrayList<String> quantity;
    public String customerName;
    public String Lat;
    public String Long;
    public String address;
    public String modeOfPayment;
    public String totalAmount;
    public int ORDER_STATUS;
    public String OTP;
    public String sellerId;
    public String orderDate;
    public String userPh;
    public String shopName;
    public String customerId;
    public String sellerPh;
    public String sellerLat;
    public String earning;
    public ArrayList<String> colorImage ;
    public String sellerLong;
    public String sellerAddress;
    public String sellerPh2;

    public OrderDetailsForDev(String ID, ArrayList<String> title , ArrayList<String> color, ArrayList<String> size , ArrayList<String> colorImage , ArrayList<String> quantity, String customerName,
                              String customerId,String Lat, String Long, String address, String modeOfPayment, String totalAmount, int ORDER_STATUS, String OTP,
                              String orderDate, String userPh, String shopName, String sellerPh, String sellerPh2, String earning,
                              String sellerLat, String sellerLong, String sellerAddress,String sellerId) {

        this.ID = ID;
        this.title = title;
        this.color = color;
        this.size = size;
        this.quantity = quantity;
        this.customerName = customerName;
        this.colorImage = colorImage;
        this.sellerId = sellerId;
        this.Lat = Lat;
        this.earning = earning;
        this.sellerPh2 = sellerPh2;
        this.Long = Long;
        this.address = address;
        this.modeOfPayment = modeOfPayment;
        this.totalAmount = totalAmount;
        this.customerId = customerId;
        this.ORDER_STATUS = ORDER_STATUS;
        this.OTP = OTP;
        this.orderDate = orderDate;
        this.userPh = userPh;
        this.sellerPh = sellerPh;
        this.sellerLat = sellerLat;
        this.sellerLong = sellerLong;
        this.sellerAddress = sellerAddress;
        this.shopName = shopName;
    }

    public OrderDetailsForDev() {}

    protected OrderDetailsForDev(Parcel in) {
        ID = in.readString();
        title = in.createStringArrayList();
        color = in.createStringArrayList();
        size = in.createStringArrayList();
        quantity = in.createStringArrayList();
        customerName = in.readString();
        Lat = in.readString();
        Long = in.readString();
        address = in.readString();
        sellerId = in.readString();
        modeOfPayment = in.readString();
        totalAmount = in.readString();
        ORDER_STATUS = in.readInt();
        OTP = in.readString();
        orderDate = in.readString();
        customerId = in.readString();
        userPh = in.readString();
        shopName = in.readString();
        sellerPh = in.readString();
        sellerLat = in.readString();
        earning = in.readString();
        colorImage = in.createStringArrayList();
        sellerLong = in.readString();
        sellerAddress = in.readString();
        sellerPh2 = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeStringList(title);
        dest.writeStringList(color);
        dest.writeStringList(size);
        dest.writeStringList(quantity);
        dest.writeString(customerName);
        dest.writeString(Lat);
        dest.writeString(sellerId);
        dest.writeString(Long);
        dest.writeString(address);
        dest.writeString(modeOfPayment);
        dest.writeString(totalAmount);
        dest.writeInt(ORDER_STATUS);
        dest.writeString(OTP);
        dest.writeString(orderDate);
        dest.writeString(userPh);
        dest.writeString(shopName);
        dest.writeString(sellerPh);
        dest.writeString(sellerLat);
        dest.writeString(earning);
        dest.writeString(customerId);
        dest.writeStringList(colorImage);
        dest.writeString(sellerLong);
        dest.writeString(sellerAddress);
        dest.writeString(sellerPh2);
    }

    public static final Creator<OrderDetailsForDev> CREATOR = new Creator<OrderDetailsForDev>() {
        @Override
        public OrderDetailsForDev createFromParcel(Parcel in) {
            return new OrderDetailsForDev(in);
        }

        @Override
        public OrderDetailsForDev[] newArray(int size) {
            return new OrderDetailsForDev[size];
        }
    };
}
