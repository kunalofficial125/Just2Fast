package com.just2fast.ushop;

import java.util.HashMap;
import java.util.List;

public class UserModel {
    public Double name_state;
    public String name,email,City,Area;
    public List<OrderDetails> myOrders;
    public Double profile_img_state, login_state, addressCount;
    public HashMap<String,String> starsRef;

    public UserModel(String name,String email,Double name_state,Double addressCount,List<OrderDetails> myOrders,HashMap<String,String> starsRef,String Area,String City){
        this.name = name;
        this.email = email;
        this.myOrders = myOrders;
        this.addressCount = addressCount;
        this.Area = Area;
        this.City = City;
        this.starsRef = starsRef;
        this.name_state = name_state;
        //this.profile_img_url = profile_img_url;
    }
    public UserModel(){

    }

}
