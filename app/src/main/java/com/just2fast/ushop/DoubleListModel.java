package com.just2fast.ushop;

import java.util.List;

public class DoubleListModel {

    public List<ProductModel> bannerList,productPromoList,groceryList;
    public String bannerLink1,bannerLink2;

    public DoubleListModel(){

    }

    public DoubleListModel(List<ProductModel> productPromoList, List<ProductModel> bannerList,String bannerLink1,String bannerLink2,List<ProductModel> groceryList) {
        this.productPromoList = productPromoList;
        this.bannerList = bannerList;
        this.bannerLink1 = bannerLink1;
        this.groceryList = groceryList;
        this.bannerLink2 = bannerLink2;
    }

    public String getBannerLink1() {
        return bannerLink1;
    }

    public void setBannerLink1(String bannerLink1) {
        this.bannerLink1 = bannerLink1;
    }

    public String getBannerLink2() {
        return bannerLink2;
    }

    public void setBannerLink2(String bannerLink2) {
        this.bannerLink2 = bannerLink2;
    }
}
