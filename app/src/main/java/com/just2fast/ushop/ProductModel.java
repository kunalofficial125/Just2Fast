package com.just2fast.ushop;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;

public class ProductModel implements Parcelable {

    public String title,category,fabric,occasion,productVerified,promoVerified,sellerId,mrp,sellingPrice,brandName,packOf,
            categoryCode,description,star5,star4,star3,star2,star1,productCity;
    public List<String> color;
    public List<String> Images;
    public  String productId;
    public List<String> size;
    public List<String> sizePrice;
    public int stock;
    public List<String> colorImages;

    public ProductModel(){}

    public ProductModel(String title, List<String> color, List<String> colorImages, List<String> Images, String category, List<String> size,List<String> sizePrice ,String fabric,int stock, String gender, String occasion, String mrp, String sellingPrice, String brandName, String packOf, String description, String productId, String star5, String star4, String star3, String star2, String star1, String productVerified, String promoVerified, String sellerId,String productCity){

        this.title = title;
        this.Images = Images;
        this.color = color;
        this.category = category;
        this.productCity=productCity;
        this.promoVerified = promoVerified;
        this.sellerId = sellerId;
        this.size = size;
        this.fabric = fabric;
        this.productId = productId;
        this.sizePrice = sizePrice;
        this.stock = stock;
        this.occasion = occasion;
        this.mrp = mrp;
        this.colorImages = colorImages;
        this.sellingPrice = sellingPrice;
        this.brandName = brandName;
        this.packOf = packOf;
        this.star1 = star1;
        this.star2 = star2;
        this.star3 = star3;
        this.star4 = star4;
        this.star5 = star5;
        this.description = description;
        this.productVerified = productVerified;
    }

    protected ProductModel(Parcel in) {
        title = in.readString();
        category = in.readString();
        fabric = in.readString();
        occasion = in.readString();
        stock = in.readInt();
        category = in.readString();
        mrp = in.readString();
        sellingPrice = in.readString();
        brandName = in.readString();
        packOf = in.readString();
        categoryCode = in.readString();
        star1 = in.readString();
        star2 = in.readString();
        sellerId = in.readString();
        star3 = in.readString();
        star4 = in.readString();
        sizePrice = in.createStringArrayList();
        star5 = in.readString();
        productCity = in.readString();
        colorImages = in.createStringArrayList();
        description = in.readString();
        productVerified = in.readString();
        promoVerified = in.readString();
        color = in.createStringArrayList();
        Images = in.createStringArrayList();
        size = in.createStringArrayList();
    }



    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(category);
        parcel.writeList(Images);
        parcel.writeList(size);
        parcel.writeString(fabric);
        parcel.writeString(productCity);
        parcel.writeInt(stock);
        parcel.writeString(sellerId);
        parcel.writeString(occasion);
        parcel.writeString(mrp);
        parcel.writeString(sellingPrice);
        parcel.writeList(sizePrice);
        parcel.writeString(promoVerified);
        parcel.writeString(productVerified);
        parcel.writeList(colorImages);
        parcel.writeString(brandName);
        parcel.writeString(productId);
        parcel.writeList(color);
        parcel.writeString(star1);
        parcel.writeString(star2);
        parcel.writeString(star3);
        parcel.writeString(star4);
        parcel.writeString(star5);
        parcel.writeString(packOf);
        parcel.writeString(description);
    }

    public static final Creator<ProductModel> CREATOR = new Creator<ProductModel>() {
        @Override
        public ProductModel createFromParcel(Parcel in) {
            return new ProductModel(in);
        }

        @Override
        public ProductModel[] newArray(int size) {
            return new ProductModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }


}
