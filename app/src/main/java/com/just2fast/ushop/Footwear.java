package com.just2fast.ushop;

public class Footwear {

    String title,productType,material,gender,mrp,sellingPrice,brandName,packOf,description;
    String []size;
    String []colors;
    String []otherColorImages;

    public Footwear(String title,String productType,String material, String gender,String mrp,String sellingPrice,String brandName,String packOf,String description,String []colors,String []otherColorImages,String []size){
        this.title = title;
        this.productType = productType;
        this.material = material;
        this.gender = gender;
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
