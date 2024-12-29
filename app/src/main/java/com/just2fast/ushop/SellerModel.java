package com.just2fast.ushop;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class SellerModel implements Parcelable {

    public String Name;
    public String GovDocNo;
    public String GSTIN;
    public String Lat;
    public String Long;
    public int verifySeller;
    public String ShopAddress;
    public String AccountNumber,AccountHolderName,IFSC;
    public int OrderAlert;
    public String phone,alterPhone;
    public String ShopName;
    public List<ProductModel> Products;
    public String SellerId;
    public List<OrderDetails> Orders;

    public SellerModel(String Name, String GovDocNo,String SellerId ,String ShopName, List<ProductModel> Products,List<OrderDetails> Orders,int OrderAlert,String GSTIN,String Lat,String Long,int verifySeller,String ShopAddress,String phone,String alterPhone,String AccountNumber,String AccountHolderName,String IFSC) {
        this.GovDocNo = GovDocNo;
        this.Name = Name;
        this.OrderAlert = OrderAlert;
        this.verifySeller = verifySeller;
        this.SellerId = SellerId;
        this.ShopName = ShopName;
        this.Orders = Orders;
        this.AccountNumber = AccountNumber;
        this.AccountHolderName = AccountHolderName;
        this.IFSC = IFSC;
        this.phone = phone;
        this.alterPhone = alterPhone;
        this.ShopAddress = ShopAddress;
        this.Lat = Lat;
        this.Long = Long;
        this.GSTIN = GSTIN;
        this.Products = Products;
    }

    public SellerModel() {
    }

    protected SellerModel(Parcel in) {
        Name = in.readString();
        GovDocNo = in.readString();
        OrderAlert= in.readInt();
        SellerId = in.readString();
        alterPhone = in.readString();
        phone = in.readString();
        ShopName = in.readString();
        GSTIN = in.readString();
        ShopAddress = in.readString();
        verifySeller = in.readInt();
        AccountNumber = in.readString();
        AccountHolderName = in.readString();
        IFSC = in.readString();
        Lat = in.readString();
        Long = in.readString();
        Products = in.createTypedArrayList(ProductModel.CREATOR);
        Orders = in.createTypedArrayList(OrderDetails.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Name);
        dest.writeString(GovDocNo);
        dest.writeString(ShopName);
        dest.writeInt(OrderAlert);
        dest.writeString(ShopAddress);
        dest.writeTypedList(Products);
        dest.writeInt(verifySeller);
        dest.writeString(GSTIN);
        dest.writeString(Lat);
        dest.writeString(phone);
        dest.writeString(alterPhone);
        dest.writeString(Long);
        dest.writeString(AccountHolderName);
        dest.writeString(AccountNumber);
        dest.writeString(IFSC);
        dest.writeString(SellerId);
        dest.writeTypedList(Orders);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SellerModel> CREATOR = new Creator<SellerModel>() {
        @Override
        public SellerModel createFromParcel(Parcel in) {
            return new SellerModel(in);
        }

        @Override
        public SellerModel[] newArray(int size) {
            return new SellerModel[size];
        }
    };
}
