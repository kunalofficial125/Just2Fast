package com.just2fast.ushop;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.azure.core.annotation.Get;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link payment_method#newInstance} factory method to
 * create an instance of this fragment.
 */
public class payment_method extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public payment_method() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment payment_method.
     */
    // TODO: Rename and change types and number of parameters
    public static payment_method newInstance(String param1, String param2) {
        payment_method fragment = new payment_method();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    LinearLayout ll1,ll2;
    RadioButton onlinePay,codPay;
    int GET_DEV_CODE =100;
    Button continueBtn;
    ImageView backBtn;
    Activity parent;
    String name,ph,fullAddress,size,color,totalAmount;
    int noOfProducts;
    ArrayList<ProductModel> ProductModels = new ArrayList<>();
    String mod;
    String userLat,userLong;
    String month;
    int NavFlag;
    int dayOfMonth;
    FirebaseDatabase firebaseDatabase;
    LocalDate today;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    ArrayList<OrderDetails> orderDetails = new ArrayList<>();
    ArrayList<OrderDetails> tempOrders = new ArrayList<>();
    int year;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_payment_method, container, false);
        parent = getActivity();
        ll1 = v.findViewById(R.id.ll1);
        ll2 = v.findViewById(R.id.ll2);
        onlinePay = v.findViewById(R.id.onlinePay);
        continueBtn = v.findViewById(R.id.continueBtn);
        codPay = v.findViewById(R.id.codPay);
        backBtn = v.findViewById(R.id.backBtn);
        name = getArguments().getString("name");
        ph = getArguments().getString("ph");
        firebaseDatabase = FirebaseDatabase.getInstance();
        size  = getArguments().getString("size");
        totalAmount = getArguments().getString("totalAmount");
        color = getArguments().getString("color");
        noOfProducts = getArguments().getInt("noOfProducts");
        fullAddress = getArguments().getString("address");
        NavFlag = getArguments().getInt("NavFlag",0);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parent.onBackPressed();
            }
        });


        if(NavFlag==0){
            ProductModels = getArguments().getParcelableArrayList("list");
            Log.d("abcd","clothes");
        }
        else {
            orderDetails = getArguments().getParcelableArrayList("jsonTemp");
            Log.d("orderDetailPart",orderDetails.get(0).product.get(0).sellerId);
            Gson gson1 = new Gson();
            String a = gson1.toJson(orderDetails);
            Log.d("abcd",a+"");
            fullAddress = getArguments().getString("Address");

        }


        SharedPreferences userLatLong = parent.getSharedPreferences("userLatLong", Context.MODE_PRIVATE);
        userLat = userLatLong.getString("lat","null");
        userLong = userLatLong.getString("long","null");

        Bundle bundle  = new Bundle();
//        bundle.putParcelableArrayList("list",clothes_models);
//        bundle.putString("name",name);
//        bundle.putString("ph",ph);
//        bundle.putString("size",size);
//
//        bundle.putString("color",color);
//        bundle.putString("totalAmount",totalAmount);
//        bundle.putInt("noOfProducts",noOfProducts);
//        bundle.putString("fullAddress",fullAddress);


        // Get the day of the month
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
             today = LocalDate.now();
             dayOfMonth = today.getDayOfMonth();
             month = today.getMonth().toString().substring(0, 3);
             year = today.getYear();
        }



        String date  = dayOfMonth+" "+month+" "+year;
        Random random = new Random();
        int randomNumber = 1000 + random.nextInt(9000);
        int orderId = 10000000 + random.nextInt(90000000);
        Log.d("total636",totalAmount+"");

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codPay.setChecked(true);
                onlinePay.setChecked(false);
                mod = "Cash On Delivery";
                if(NavFlag==0){
                    OrderDetails oD = new OrderDetails( orderId+"", ProductModels,size,color,ph, user.getEmail(),noOfProducts+"",fullAddress,name,mod,totalAmount,1,randomNumber+"",date,userLat,userLong);
                    orderDetails.add(oD);
                }
                else {
                    for(int i=0;i<orderDetails.size();i++){
                        orderDetails.get(i).modeOfPayment = mod;
                        orderDetails.get(i).address = fullAddress;
                        orderDetails.get(i).userPh = ph;
                        orderDetails.get(i).userEmail = user.getEmail();
                        orderDetails.get(i).name = name;
                        orderDetails.get(i).orderDate = date;
                    }
                    bundle.putInt("NavFlag",NavFlag);
                }
                bundle.putParcelableArrayList("list",orderDetails);
                bundle.putInt("flag",1);

                Gson gson = new Gson();
                String jsonTemp = gson.toJson(orderDetails);
                Log.d("jsonKey",jsonTemp);

                Drawable backgroundDrawable = ll2.getBackground();
                // Check if the drawable is an instance of GradientDrawable
                if (backgroundDrawable instanceof GradientDrawable) {
                    // Cast it to GradientDrawable and set the stroke color
                    GradientDrawable gradientDrawable = (GradientDrawable) backgroundDrawable;
                    gradientDrawable.setStroke(6, Color.RED); // Set the stroke color to red
                }
                Drawable backgroundDrawable2 = ll1.getBackground();
                // Check if the drawable is an instance of GradientDrawable
                if (backgroundDrawable2 instanceof GradientDrawable) {
                    // Cast it to GradientDrawable and set the stroke color
                    GradientDrawable gradientDrawable = (GradientDrawable) backgroundDrawable2;
                    gradientDrawable.setStroke(6, getResources().getColor(R.color.grey)); // Set the stroke color to red
                }
            }
        };


        View.OnClickListener onClickListener1 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onlinePay.setChecked(true);
                codPay.setChecked(false);
                mod = "Online Payment";
                if(NavFlag==0){
                    OrderDetails oD = new OrderDetails( orderId+"", ProductModels,size,color,ph, user.getEmail(),noOfProducts+"",fullAddress,name,mod,totalAmount,1,randomNumber+"",date,userLat,userLong);
                    orderDetails.add(oD);
                }
                else {
                    for(int i=0;i<orderDetails.size();i++){
                        orderDetails.get(i).modeOfPayment = mod;
                        orderDetails.get(i).address = fullAddress;
                        orderDetails.get(i).userPh = ph;
                        orderDetails.get(i).userEmail = user.getEmail();
                        orderDetails.get(i).name = name;
                        orderDetails.get(i).orderDate = date;
                    }
                    bundle.putInt("NavFlag",NavFlag);
                }
                bundle.putParcelableArrayList("list",orderDetails);
                bundle.putInt("flag",1);

                Drawable backgroundDrawable = ll1.getBackground();
                // Check if the drawable is an instance of GradientDrawable
                if (backgroundDrawable instanceof GradientDrawable) {
                    // Cast it to GradientDrawable and set the stroke color
                    GradientDrawable gradientDrawable = (GradientDrawable) backgroundDrawable;
                    gradientDrawable.setStroke(6, Color.RED); // Set the stroke color to red
                }

                Drawable backgroundDrawable2 = ll2.getBackground();
                // Check if the drawable is an instance of GradientDrawable
                if (backgroundDrawable2 instanceof GradientDrawable) {
                    // Cast it to GradientDrawable and set the stroke color
                    GradientDrawable gradientDrawable = (GradientDrawable) backgroundDrawable2;
                    gradientDrawable.setStroke(6, getResources().getColor(R.color.grey)); // Set the stroke color to red
                }
            }
        };


        ll1.setOnClickListener(onClickListener1);
        ll2.setOnClickListener(onClickListener);
        codPay.setOnClickListener(onClickListener);
        onlinePay.setOnClickListener(onClickListener1);




        Dialog dialog = new Dialog(parent);
        dialog.setContentView(R.layout.order_confirm_dialog);
        Button confirm  =  dialog.findViewById(R.id.confirmBtn);
        ImageView dialog_backBtn = dialog.findViewById(R.id.dialog_backBtn);
        dialog_backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });




        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                confirm.setVisibility(View.INVISIBLE);

                firebaseDatabase.getReference().child("Category").child("SellerData").child(orderDetails.get(0).product.get(0).sellerId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        SellerModel sellerModel = snapshot.getValue(SellerModel.class);


                        if(sellerModel!=null) {


                            if (sellerModel.Orders != null) {


                                sellerModel.Orders.addAll(orderDetails);



                                for(int i=0;i<orderDetails.size();i++){

                                    for(int j=0;j<sellerModel.Products.size();j++){

                                        if(orderDetails.get(i).product.get(0).productId.equals(sellerModel.Products.get(j).productId)){

                                            Log.d("ValueProStock",sellerModel.Products.get(j).stock+"");
                                            Log.d("ValueOrderStock",Integer.parseInt(orderDetails.get(i).quantity)+"");

                                            int a =  sellerModel.Products.get(j).stock - Integer.parseInt(orderDetails.get(i).quantity);

                                            Log.d("ValueA",a+"");

                                            sellerModel.Products.get(j).stock = a;

                                        }

                                    }

                                }




                                firebaseDatabase.getReference().child("Category").child("SellerData").child(orderDetails.get(0).product.get(0).sellerId).child("OrderAlert").setValue(1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        sellerModel.OrderAlert = 1;
                                        firebaseDatabase.getReference().child("Category").child("SellerData").child(orderDetails.get(0).product.get(0).sellerId).setValue(sellerModel);
                                    }
                                });
                            }
                            else {
                                firebaseDatabase.getReference().child("Category").child("SellerData").child(orderDetails.get(0).product.get(0).sellerId).child("OrderAlert").setValue(1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        firebaseDatabase.getReference().child("Category").child("SellerData").child(orderDetails.get(0).product.get(0).sellerId).child("Orders").setValue(orderDetails);
                                    }
                                });
                            }




//                            for(int i=0;i<orderDetails.size();i++){
//
//                                int finalI = i;
//                                firebaseDatabase.getReference().child("Category").child("SellerData").child(orderDetails.get(finalI).product.get(0).sellerId).child("Products").orderByChild("productId").equalTo(orderDetailsList.get(finalI).product.get(0).productId).addListenerForSingleValueEvent(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//
//                                            int currentStock = dataSnapshot.getValue(ProductModel.class).stock;
//                                            Log.d("LogStock",currentStock+"");
//                                            Log.d("LogStock2",orderDetails.get(finalI).quantity+"");
//                                            int updatedStock = currentStock - Integer.parseInt(orderDetails.get(finalI).quantity);
//                                            Log.d("LogUpdatedStock",updatedStock+"");
//                                            dataSnapshot.getRef().child("stock").setValue(dataSnapshot.getValue(ProductModel.class).stock - Integer.parseInt(orderDetails.get(finalI).quantity));
//
//                                        }
//
//                                    }
//
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError error) {
//
//                                    }
//                                });
//
//                            }





                            for(int i=0;i<orderDetails.size();i++){

                                int finalI = i;

                                firebaseDatabase.getReference().child("Category").child("products").child(orderDetails.get(i).product.get(0).category.toLowerCase()).orderByChild("productId").equalTo(orderDetails.get(i).product.get(0).productId).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                            dataSnapshot.getRef().child("stock").setValue(dataSnapshot.getValue(ProductModel.class).stock - Integer.parseInt(orderDetails.get(finalI).quantity)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {

                                                   // dataSnapshot.getRef().child("stock").

                                                }
                                            });

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });




                            }


                            firebaseDatabase.getReference().child("Category").child("DeliveryPartners").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {


                                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){

                                        if(GET_DEV_CODE !=100){
                                            break;
                                        }


                                        GetFreeDev getFreeDev = dataSnapshot.getValue(GetFreeDev.class);

                                        if(getFreeDev!=null){

                                            Log.d("GetDevCodeBefore",GET_DEV_CODE+"");

                                            if(/*getFreeDev.status!=-1 &&*/ GET_DEV_CODE == 100){

                                                dataSnapshot.getRef().child("status").setValue(1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Log.d("GetDevCode",GET_DEV_CODE+"");
                                                        GET_DEV_CODE=200;

                                                        firebaseDatabase.getReference().child("Category").child("DeliveryPartnersList").orderByChild("devId").equalTo(getFreeDev.devId).addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                                                                    ArrayList<OrderDetailsForDev> tempOrder = new ArrayList<>();

                                                                    if(dataSnapshot1!=null){

                                                                        DeliveryPartnerModel deliveryPartnerModel = dataSnapshot1.getValue(DeliveryPartnerModel.class);

                                                                        ArrayList<String> titles  =  new ArrayList<>();
                                                                        ArrayList<String> color  =  new ArrayList<>();
                                                                        ArrayList<String> size  =  new ArrayList<>();
                                                                        ArrayList<String> quantity  =  new ArrayList<>();
                                                                        ArrayList<String> images  =  new ArrayList<>();


                                                                        for(int i=0;i<orderDetails.size();i++){

                                                                            titles.add(orderDetails.get(i).product.get(0).title);
                                                                            color.add(orderDetails.get(i).color);
                                                                            size.add(orderDetails.get(i).size);
                                                                            quantity.add(orderDetails.get(i).quantity);

                                                                            for(int j=0;j<orderDetails.get(i).product.get(0).color.size();j++){

                                                                                if(orderDetails.get(i).product.get(0).color.get(j).equals(orderDetails.get(i).color)){
                                                                                    images.add(orderDetails.get(i).product.get(0).colorImages.get(j));
                                                                                }

                                                                            }

                                                                        }

                                                                        OrderDetailsForDev orderDetailsForDev = new OrderDetailsForDev(orderDetails.get(0).ID,titles,color,size,images,quantity,orderDetails.get(0).name,orderDetails.get(0).userEmail,orderDetails.get(0).Lat,orderDetails.get(0).Long,
                                                                                orderDetails.get(0).address,mod,orderDetails.get(0).totalAmount,1,orderDetails.get(0).OTP,date,orderDetails.get(0).userPh,sellerModel.ShopName,sellerModel.phone,sellerModel.alterPhone,"",sellerModel.Lat,sellerModel.Long,sellerModel.ShopAddress,sellerModel.SellerId);


                                                                        if(deliveryPartnerModel.orders!=null){

                                                                            tempOrder.addAll(deliveryPartnerModel.orders);
                                                                            tempOrder.add(orderDetailsForDev);

                                                                        }
                                                                        else{

                                                                            tempOrder.add(orderDetailsForDev);

                                                                        }

                                                                        dataSnapshot1.getRef().child("orders").setValue(tempOrder).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void unused) {
                                                                                //Toast.makeText(parent, "DeliveryUpdateDone", Toast.LENGTH_SHORT).show();
                                                                                dataSnapshot1.getRef().child("OrderAlert").setValue(1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                    @Override
                                                                                    public void onSuccess(Void unused) {
                                                                                        Log.d("DeliveryPartnerAlert","New Order");
                                                                                    }
                                                                                });
                                                                            }
                                                                        }).addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                //Toast.makeText(parent, "DeliveryUpdateFail", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        });



                                                                    }

                                                                }

                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });



                                                    }
                                                });

                                                GET_DEV_CODE = 200;


                                            }

                                        }



                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });



                        }

                        else{
                            Toast.makeText(parent, "Technical Problem !", Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                if(parent instanceof MainActivity){
                    bundle.putInt("CheckBackFlag",1);
                    ((MainActivity)parent).runFragment(bundle,new Confirm_order_details(),"Confirm Order Details");
                }
                else {
                    ((search_instance)parent).runFragment(bundle,new Confirm_order_details(),6);
                }
                dialog.cancel();
            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(codPay.isChecked()){
                    mod = "Cash On Delivery";
                    dialog.show();
                }
            }
        });


        return v;
    }
}