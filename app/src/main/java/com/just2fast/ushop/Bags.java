package com.just2fast.ushop;

public class Bags {

    String title,productType,material,gender,mrp,sellingPrice,brandName,packOf,description,noOfPockets;
    String []size;
    String []colors;
    String []otherColorImages;

    public Bags(){}

    public Bags(String title, String productType,String material,String noOfPockets,String gender,String mrp,String sellingPrice,
                String brandName,String packOf,String description,String []size,String []colors, String []otherColorImages ){

        this.title = title;
        this.productType = productType;
        this.material = material;
        this.gender = gender;
        this.noOfPockets = noOfPockets;
        this.mrp = mrp;
        this.sellingPrice = sellingPrice;
        this.brandName = brandName;
        this.packOf = packOf;
        this.description = description;
        this.size = size;
        this.colors = colors;
        this.otherColorImages = otherColorImages;
    }
}
