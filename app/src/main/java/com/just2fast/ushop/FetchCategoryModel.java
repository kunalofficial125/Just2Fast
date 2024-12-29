package com.just2fast.ushop;

import java.util.ArrayList;

public class FetchCategoryModel {

    public ArrayList<String> fashionCat;
    public ArrayList<String> groceryCat;

    public FetchCategoryModel(){

    }

    public FetchCategoryModel(ArrayList<String> fashionCat,ArrayList<String> groceryCat){
        this.fashionCat = fashionCat;
        this.groceryCat = groceryCat;
    }


}
