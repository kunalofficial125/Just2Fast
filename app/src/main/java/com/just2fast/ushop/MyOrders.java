package com.just2fast.ushop;

import android.os.Parcel;
import android.os.Parcelable;

public class MyOrders implements Parcelable {
    Address_Model address_model;
    ProductModel ProductModel;
    String noOfOrders,selectedSize,selectedColor;

    public MyOrders(Address_Model address_model, ProductModel ProductModel, String noOfOrders, String selectedSize, String selectedColor){
        this.address_model = address_model;
        this.noOfOrders = noOfOrders;
        this.selectedColor = selectedColor;
        this.selectedSize = selectedSize;
        this.ProductModel = ProductModel;
    }

    protected MyOrders(Parcel in) {
        address_model = in.readParcelable(Address_Model.class.getClassLoader());
        ProductModel = in.readParcelable(ProductModel.class.getClassLoader());
        noOfOrders = in.readString();
        selectedSize = in.readString();
        selectedColor = in.readString();
    }

    public static final Parcelable.Creator<MyOrders> CREATOR = new Parcelable.Creator<MyOrders>() {
        @Override
        public MyOrders createFromParcel(Parcel in) {
            return new MyOrders(in);
        }

        @Override
        public MyOrders[] newArray(int size) {
            return new MyOrders[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(address_model, flags);
        dest.writeParcelable(ProductModel, flags);
        dest.writeString(noOfOrders);
        dest.writeString(selectedSize);
        dest.writeString(selectedColor);
    }
}
