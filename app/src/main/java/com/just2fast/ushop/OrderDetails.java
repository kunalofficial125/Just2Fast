package com.just2fast.ushop;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class OrderDetails implements Parcelable{
    public ArrayList<ProductModel> product;
    public int ORDER_STATUS;
    public String orderDate,OTP,ID,Lat,Long;
    public String size,color,userEmail,userPh,quantity,address,name,modeOfPayment,totalAmount;

    public OrderDetails() {
        // Initialize any default values or leave empty
    }

    public OrderDetails(String ID, ArrayList<ProductModel> product, String size, String color,
                        String userPh, String userEmail, String quantity, String address, String name, String modeOfPayment, String totalAmount, int ORDER_STATUS, String OTP, String orderDate,String Lat,String Long){

        this.address  = address;
        this.color = color;
        this.size = size;
        this.userEmail = userEmail;
        this.ID = ID;
        this.orderDate = orderDate;
        this.OTP = OTP;
        this.ORDER_STATUS  = ORDER_STATUS;
        this.userPh  = userPh;
        this.totalAmount = totalAmount;
        this.name = name;
        this.Lat = Lat;
        this.Long = Long;
        this.modeOfPayment = modeOfPayment;
        this.quantity = quantity;
        this.product = product;
    }

    protected OrderDetails(Parcel in) {
        product = in.readParcelable(ProductModel.class.getClassLoader());
        size = in.readString();
        color = in.readString();
        userPh = in.readString();
        OTP = in.readString();
        orderDate = in.readString();
        userEmail = in.readString();
        quantity = in.readString();
        Lat = in.readString();
        Long = in.readString();
        ID = in.readString();
        address = in.readString();
        name = in.readString();
        modeOfPayment = in.readString();
        totalAmount = in.readString();
        ORDER_STATUS = in.readInt();
    }

    public static final Parcelable.Creator<OrderDetails> CREATOR = new Creator<OrderDetails>() {
        @Override
        public OrderDetails createFromParcel(Parcel in) {
            return new OrderDetails(in);
        }

        @Override
        public OrderDetails[] newArray(int size) {
            return new OrderDetails[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            dest.writeParcelableList(product, flags);
        }
        dest.writeString(size);
        dest.writeString(color);
        dest.writeString(userPh);
        dest.writeString(userEmail);
        dest.writeString(ID);
        dest.writeString(Lat);
        dest.writeString(Long);
        dest.writeString(quantity);
        dest.writeString(address);
        dest.writeString(name);
        dest.writeString(OTP);
        dest.writeString(orderDate);
        dest.writeString(modeOfPayment);
        dest.writeString(totalAmount);
        dest.writeInt(ORDER_STATUS);
    }

    @Override
    public int describeContents() {
        return 0;
    }




}
