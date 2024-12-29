package com.just2fast.ushop;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import java.time.LocalDate;
import android.content.res.ColorStateList;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Confirm_order_details#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Confirm_order_details extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Confirm_order_details() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Confirm_order_details.
     */
    // TODO: Rename and change types and number of parameters
    public static Confirm_order_details newInstance(String param1, String param2) {
        Confirm_order_details fragment = new Confirm_order_details();
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

//    String name,ph,fullAddress,size,color,modeOfPayment;
//    int noOfProducts;
    UserModel userModel;
    TextView otp;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    ArrayList<OrderDetails>  orderDetailsList;
    OrderDetails orderDetails;
    TextView heading,sizeText,colorText,qty,phText,nameText,fullAddressText,MopText,TotalAmountText,orderId;
    ImageView productImage1,backBtn;
    LinearLayout ll121;
    TextView pp1,pl1,pp2,pl2,pp3,pl3,pp4,totalQuantityText;
    ProgressBar progressBar;
    int dayOfMonth;
    Activity parent;
    TextView ReturnBtn,pds,pdz;
    int a;
    String jsonUser;
    int NavFlag;
    Button continueShop;
    FirebaseDatabase firebaseDatabase;
    int buyPageFlag;
    String month;
    int year;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_confirm_order_details, container, false);
        heading = v.findViewById(R.id.heading);
        sizeText = v.findViewById(R.id.sizeText);
        colorText = v.findViewById(R.id.colorText);
        ReturnBtn = v.findViewById(R.id.returnBtn);
        otp = v.findViewById(R.id.otp);
        pds = v.findViewById(R.id.pds);
        pdz = v.findViewById(R.id.pdz);
        totalQuantityText = v.findViewById(R.id.totalQtyText);
        firebaseDatabase = FirebaseDatabase.getInstance();
        orderId = v.findViewById(R.id.orderId);
        qty = v.findViewById(R.id.qty);
        progressBar = v.findViewById(R.id.progressBar11);
        progressBar.setVisibility(View.INVISIBLE);
        MopText = v.findViewById(R.id.MopText);
        pp1 = v.findViewById(R.id.pp1);
        pl1 = v.findViewById(R.id.pl1);
        pp2 = v.findViewById(R.id.pp2);
        continueShop = v.findViewById(R.id.continueShop);
        pl2 = v.findViewById(R.id.pl2);
        pp3 = v.findViewById(R.id.pp3);
        pl3 = v.findViewById(R.id.pl3);
        ll121 = v.findViewById(R.id.ll121);
        pp4 = v.findViewById(R.id.pp4);
        backBtn = v.findViewById(R.id.backBtn);
        TotalAmountText = v.findViewById(R.id.TotalAmountText);
        fullAddressText = v.findViewById(R.id.fullAddress);
        nameText = v.findViewById(R.id.name);
        phText = v.findViewById(R.id.contact);
        productImage1 = v.findViewById(R.id.productImage1);

        parent = getActivity();
        totalQuantityText.setVisibility(View.INVISIBLE);



        continueShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(parent,MainActivity.class));
                parent.finish();
            }
        });


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parent.onBackPressed();
            }
        });


        //SharedPreferences spCheckBack = parent.getSharedPreferences("CheckBack",Context.MODE_PRIVATE);

        int checkBackFlag = getArguments().getInt("CheckBackFlag",0);

        if(parent instanceof MainActivity && checkBackFlag==1 ){
            ((MainActivity)parent).checkBack=123;
        }


        orderDetailsList = getArguments().getParcelableArrayList("list");
        orderDetails = orderDetailsList.get(0);





        Gson gson = new Gson();
        String jsonTemp = gson.toJson(orderDetailsList);
        Log.d("jsonKey",jsonTemp);

        int flag = getArguments().getInt("flag",0);
        buyPageFlag = getArguments().getInt("buyPageNavigation",0);
        NavFlag = getArguments().getInt("NavFlag",0);

        Log.d("ConfirmOrder_122",NavFlag+"");

        if(orderDetails.product.get(0).category.toLowerCase().contains("grocery")){
            buyPageFlag=0;
        }

        int date = Integer.parseInt(orderDetails.orderDate.substring(0,2).trim());

        LocalDate currentDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentDate = LocalDate.now();
            dayOfMonth = currentDate.getDayOfMonth();
            month = currentDate.getMonth().toString().substring(0, 3);
            year = currentDate.getYear();

        }


        if(orderDetails.orderDate.contains(month+" "+year)){

            if(orderDetails.ORDER_STATUS==4){

                if(buyPageFlag==1){
                   // ll121.setVisibility(View.VISIBLE);
                    ReturnBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Bundle bundle = new Bundle();
                            ArrayList<OrderDetails> list = new ArrayList<>();
                            list.add(orderDetails);
                            bundle.putParcelableArrayList("list",list);

                            ((MainActivity)parent).runFragment(bundle,new ReturnRequest(),"Return Request");
                        }
                    });

                }



                if(orderDetails.orderDate.toLowerCase().contains("feb")){
                    if(date==29 || date == 28){
                        if(2==dayOfMonth){
                            ReturnBtn.setVisibility(View.INVISIBLE);
                            ll121.setVisibility(View.INVISIBLE);
                        }
                        else {
                            ReturnBtn.setVisibility(View.VISIBLE);
                            ll121.setVisibility(View.VISIBLE);
                        }
                    }
                    else if(date+2==dayOfMonth){
                        ReturnBtn.setVisibility(View.INVISIBLE);
                        ll121.setVisibility(View.INVISIBLE);
                    }
                    else {
                        ReturnBtn.setVisibility(View.VISIBLE);
                        ll121.setVisibility(View.VISIBLE);
                    }
                }

                else{

                    if(date==29 ){
                        if(31==dayOfMonth){
                            ReturnBtn.setVisibility(View.INVISIBLE);
                            ll121.setVisibility(View.INVISIBLE);
                        }
                        else {
                            ReturnBtn.setVisibility(View.VISIBLE);
                            ll121.setVisibility(View.VISIBLE);
                        }
                    }
                    else if (date==30){
                        if(1==dayOfMonth){
                            ReturnBtn.setVisibility(View.INVISIBLE);
                            ll121.setVisibility(View.INVISIBLE);
                        }
                        else {
                            ReturnBtn.setVisibility(View.VISIBLE);
                            ll121.setVisibility(View.VISIBLE);
                        }
                    }
                    else if(date==31){
                        if(2==dayOfMonth){
                            ReturnBtn.setVisibility(View.INVISIBLE);
                            ll121.setVisibility(View.INVISIBLE);
                        }
                        else {
                            ReturnBtn.setVisibility(View.VISIBLE);
                            ll121.setVisibility(View.VISIBLE);
                        }
                    }
                    else if(date+2==dayOfMonth || date+2<dayOfMonth){
                        ReturnBtn.setVisibility(View.INVISIBLE);
                        ll121.setVisibility(View.INVISIBLE);
                    }
                    else {
                        ReturnBtn.setVisibility(View.VISIBLE);
                        ll121.setVisibility(View.VISIBLE);
                    }

                }

            }

            else if(orderDetails.ORDER_STATUS<4){

                if(buyPageFlag==1){
                    ll121.setVisibility(View.VISIBLE);
                    ReturnBtn.setText("Cancel");
                    ReturnBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            //TODO Cancel Order

                            Dialog confirmCancel = new Dialog(parent);
                            confirmCancel.setContentView(R.layout.order_confirm_dialog);

                            TextView msg = confirmCancel.findViewById(R.id.textView32);
                            TextView allAlert = confirmCancel.findViewById(R.id.allAlert);
                            ImageView dialog_backBtn = confirmCancel.findViewById(R.id.dialog_backBtn);
                            Button confirmBtn = confirmCancel.findViewById(R.id.confirmBtn);
                            msg.setTextSize(25);
                            msg.setText("Are you sure you want to cancel this order ?");
                            allAlert.setVisibility(View.VISIBLE);

                            dialog_backBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    confirmCancel.dismiss();
                                }
                            });


                            confirmBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    confirmBtn.setVisibility(View.INVISIBLE);

                                    firebaseDatabase.getReference().child("Category").child("UsersData").
                                            child(user.getEmail().replace(".","")).child("myOrders").orderByChild("ID").equalTo(orderDetails.ID).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                    firebaseDatabase.getReference().child("Category").child("SellerData").child(orderDetails.product.get(0).sellerId).child("OrderAlert").setValue(2);

                                                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                                                        dataSnapshot.getRef().child("ORDER_STATUS").setValue(7).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {

                                                                //Updating Seller
                                                                firebaseDatabase.getReference().child("Category").child("SellerData").
                                                                        child(orderDetails.product.get(0).sellerId).child("Orders").orderByChild("ID").equalTo(orderDetails.ID).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                                                                                    dataSnapshot.getRef().child("ORDER_STATUS").setValue(7).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                        @Override
                                                                                        public void onSuccess(Void unused) {

                                                                                            int lightGreen = parent.getResources().getColor(R.color.lightGreen);
                                                                                            int reddish = parent.getResources().getColor(R.color.reddish);

                                                                                            pl1.setBackgroundColor(lightGreen);
                                                                                            pp2.setBackgroundTintList(ColorStateList.valueOf(lightGreen));
                                                                                            pl2.setBackgroundColor(lightGreen);
                                                                                            pp3.setBackgroundTintList(ColorStateList.valueOf(lightGreen));
                                                                                            pl3.setBackgroundColor(lightGreen);
                                                                                            pp4.setBackgroundTintList(ColorStateList.valueOf(reddish));
                                                                                            pds.setText("Order Cancelled !");
                                                                                            pdz.setText("Order is Cancelled by User :(");

                                                                                            ReturnBtn.setVisibility(View.INVISIBLE);
                                                                                            ll121.setVisibility(View.INVISIBLE);
                                                                                            confirmCancel.dismiss();

                                                                                            Log.d("OrderCancelled","done");

                                                                                            SharedPreferences op = parent.getSharedPreferences("Update Request", Context.MODE_PRIVATE);
                                                                                            SharedPreferences.Editor ed = op.edit();
                                                                                            ed.putInt("OrdersUpdate",1);
                                                                                            ed.apply();


                                                                                            //Update Delivery Partner

                                                                                            firebaseDatabase.getReference().child("Category").child("DeliveryPartnersList").child("0").child("orders").orderByChild("ID").equalTo(orderDetails.ID).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                @Override
                                                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                                                                    for(DataSnapshot dataSnapshot1: snapshot.getChildren()){

                                                                                                        dataSnapshot1.getRef().child("ORDER_STATUS").setValue(7).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                            @Override
                                                                                                            public void onSuccess(Void unused) {
                                                                                                                firebaseDatabase.getReference().child("Category").child("DeliveryPartnersList").child("0").child("OrderAlert").setValue(2).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                    @Override
                                                                                                                    public void onSuccess(Void unused) {
                                                                                                                        Log.d("DeliveryPartnerAlert","Order Cancelled");
                                                                                                                    }
                                                                                                                });
                                                                                                            }
                                                                                                        });

                                                                                                    }

                                                                                                }

                                                                                                @Override
                                                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                                                }
                                                                                            });




                                                                                        }
                                                                                    });
                                                                                }
                                                                            }

                                                                            @Override
                                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                                            }
                                                                        });


                                                            }
                                                        });
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });

                                    // Confirm Button Ending >>>
                                }
                            });

                            confirmCancel.show();

                            // Main Button Ending >>>
                        }
                    });

                }

                if(orderDetails.orderDate.toLowerCase().contains("feb")){
                    if(date==29 || date == 28){
                        if(2==dayOfMonth){
                            ReturnBtn.setVisibility(View.INVISIBLE);
                            ll121.setVisibility(View.INVISIBLE);
                        }
                        else {
                            ReturnBtn.setVisibility(View.VISIBLE);
                            ll121.setVisibility(View.VISIBLE);
                        }
                    }
                    else if(date+2==dayOfMonth || date+2<dayOfMonth){
                        ReturnBtn.setVisibility(View.INVISIBLE);
                        ll121.setVisibility(View.INVISIBLE);
                    }
                    else {
                        ReturnBtn.setVisibility(View.VISIBLE);
                        ll121.setVisibility(View.VISIBLE);
                    }
                }

                else{

                    if(date==29 ){
                        if(31==dayOfMonth){
                            ReturnBtn.setVisibility(View.INVISIBLE);
                            ll121.setVisibility(View.INVISIBLE);
                        }
                        else {
                            ReturnBtn.setVisibility(View.VISIBLE);
                            ll121.setVisibility(View.VISIBLE);
                        }
                    }
                    else if (date==30){
                        if(1==dayOfMonth){
                            ReturnBtn.setVisibility(View.INVISIBLE);
                            ll121.setVisibility(View.INVISIBLE);
                        }
                        else {
                            ReturnBtn.setVisibility(View.VISIBLE);
                            ll121.setVisibility(View.VISIBLE);
                        }
                    }
                    else if(date==31){
                        if(2==dayOfMonth){
                            ReturnBtn.setVisibility(View.INVISIBLE);
                            ll121.setVisibility(View.INVISIBLE);
                        }
                        else {
                            ReturnBtn.setVisibility(View.VISIBLE);
                            ll121.setVisibility(View.VISIBLE);
                        }
                    }
                    else if(date+2==dayOfMonth || date+2<dayOfMonth){
                        Log.d("dayOfMonth",dayOfMonth+" and "+ date);
                        ReturnBtn.setVisibility(View.INVISIBLE);
                        ll121.setVisibility(View.INVISIBLE);
                    }
                    else {
                        Log.d("dayOfMonthInElse",dayOfMonth+" and "+ date);
                        ReturnBtn.setVisibility(View.VISIBLE);
                        ll121.setVisibility(View.VISIBLE);
                    }

                }

            }


        }




       /* String Url = "https://tykun.blob.core.windows.net/users/"+user.getEmail().replace(".","")+".txt";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(Url).build();*/
        if(flag == 1){

            ReturnBtn.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);





            firebaseDatabase.getReference().child("Category").child("UsersData").child(user.getEmail().replace(".","")).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    userModel = snapshot.getValue(UserModel.class);
                    UserUpdate userUpdate = new UserUpdate(parent.getApplicationContext());
                    if(NavFlag==1){

                        if(userModel.myOrders!=null){
                            for (OrderDetails order : orderDetailsList) {
                                if (!userModel.myOrders.contains(order)) {
                                    userModel.myOrders.add(order);
                                }
                            }
                        }
                        else {
                            userModel.myOrders = new ArrayList<>();
                            userModel.myOrders.addAll(orderDetailsList);
                        }

                        String JsonTemp = gson.toJson(userModel);

                        Log.d("UserJson_ConfirmOrder_119",JsonTemp);
                        Log.d("UserJson_ConfirmOrder_120",userModel.myOrders.size()+"");

                        userUpdate.userUpdate(userModel,JsonTemp);


                        SharedPreferences sharedPreferences1 = parent.getSharedPreferences("Grocery", Context.MODE_PRIVATE);
                        a = sharedPreferences1.getInt("groceryNoOfProducts",0);

                        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                        editor1.putString("groceryList","null");
                        editor1.putInt("groceryNoOfProducts",0);
                        editor1.apply();
                        Log.d("UserJson_ConfirmOrder_121",orderDetailsList.size()+"");

                    }else {
                        if(userModel.myOrders!=null && !userModel.myOrders.contains(orderDetails)){
                            userModel.myOrders.add(orderDetails);
                        }
                        else {
                            userModel.myOrders = new ArrayList<>();
                            userModel.myOrders.add(orderDetails);
                        }
                        String JsonTemp = gson.toJson(userModel);

                        Log.d("UserJson_ConfirmOrder_119",JsonTemp);
                        Log.d("UserJson_ConfirmOrder_120",userModel.myOrders.size()+"");

                        userUpdate.userUpdate(userModel,JsonTemp);
                    }


                    SharedPreferences op = parent.getSharedPreferences("Update Request",Context.MODE_PRIVATE);
                    SharedPreferences.Editor ed = op.edit();
                    ed.putInt("OrdersUpdate",1);
                    ed.apply();


                            continueShop.setVisibility(View.VISIBLE);
                            if (parent instanceof MainActivity){
                                ((MainActivity)parent).enableGroceryView();
                            }
                            if(a>1){
                                totalQuantityText.setVisibility(View.VISIBLE);
                                totalQuantityText.setText((a-1)+"+ More");
                                totalQuantityText.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(parent,MainActivity.class);
                                        intent.putExtra("NavFlag",1);
                                        startActivity(intent);
                                        parent.finish();
                                    }
                                });
                            }









                            //Stock Update
                    /*firebaseDatabase.getReference().child("Category").child("products")
                            .child(orderDetails.product.get(0).category.toLowerCase()).orderByChild("productId").equalTo(orderDetails.product.get(0).productId).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {


                                    Log.d("OrderDetailsQuantity",orderDetails.quantity+"");

                                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){

                                        dataSnapshot.getRef().child("stock").setValue(dataSnapshot.getValue(ProductModel.class).stock-Integer.parseInt(orderDetails.quantity)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                                firebaseDatabase.getReference().child("Category").child("SellerData").child(orderDetails.product.get(0).sellerId.replace(".", ""))
                                                        .child("Products").orderByChild("productId").equalTo(orderDetails.product.get(0).productId).addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                                                                    dataSnapshot.getRef().child("stock").setValue(dataSnapshot.getValue(ProductModel.class).stock-Integer.parseInt(orderDetails.quantity)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void unused) {
                                                                            Log.d("StockUpdated","done");
                                                                        }
                                                                    });
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });


                                            }
                                        });

                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });*/



                            //Stock Update End



                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



            /*client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.e("UserGet_Error",e+"");
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                    jsonUser = response.body().string();
                    Gson gson = new Gson();
                    userModel = gson.fromJson(jsonUser,UserModel.class);
                    UserUpdate userUpdate = new UserUpdate(parent.getApplicationContext());
                    if(NavFlag==1){
                        userModel.myOrders.addAll(orderDetailsList);
                        SharedPreferences sharedPreferences1 = parent.getSharedPreferences("Grocery", Context.MODE_PRIVATE);
                         a = sharedPreferences1.getInt("groceryNoOfProducts",0);

                        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                        editor1.putString("groceryList","null");
                        editor1.putInt("groceryNoOfProducts",0);
                        editor1.apply();
                        Log.d("UserJson_ConfirmOrder_121",orderDetailsList.size()+"");

                    }else {
                        userModel.myOrders.add(orderDetails);
                    }
                    String JsonTemp = gson.toJson(userModel);

                    Log.d("UserJson_ConfirmOrder_119",JsonTemp);
                    Log.d("UserJson_ConfirmOrder_120",userModel.myOrders.size()+"");

                    userUpdate.userUpdate(JsonTemp);

                    SharedPreferences op = parent.getSharedPreferences("Update Request",Context.MODE_PRIVATE);
                    SharedPreferences.Editor ed = op.edit();
                    ed.putInt("OrdersUpdate",1);
                    ed.apply();

                    parent.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            continueShop.setVisibility(View.VISIBLE);
                            if (parent instanceof MainActivity){
                                ((MainActivity)parent).enableGroceryView();
                            }
                            if(a>1){
                                totalQuantityText.setVisibility(View.VISIBLE);
                                totalQuantityText.setText((a-1)+"+ More");
                                totalQuantityText.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(parent,MainActivity.class);
                                        intent.putExtra("NavFlag",1);
                                        startActivity(intent);
                                        parent.finish();
                                    }
                                });
                            }
                        }
                });


//                    FragmentManager fm = getActivity().getSupportFragmentManager();
//                    fm.popBackStack("paymentMethods",FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                    fm.popBackStack("buyingDetails",FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                    fm.popBackStack("savedAddresses",FragmentManager.POP_BACK_STACK_INCLUSIVE);

            }
        });*/
        }
        else {

        }


//        SharedPreferences sp = getActivity().getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
//        String jsonUser = sp.getString("UserJson","null");



//        name = getArguments().getString("name");
//        fullAddress = getArguments().getString("fullAddress");
//        ph = getArguments().getString("ph");
//        size  = getArguments().getString("size");
//        color = getArguments().getString("color");
//        modeOfPayment = getArguments().getString("modeOfPayment");
//        noOfProducts = getArguments().getInt("noOfProducts");


        //OrderDetails  orderDetails = new OrderDetails(product,size,color,ph,user.getEmail(),noOfProducts+"",fullAddress,name,modeOfPayment);


        int lightGreen = parent.getResources().getColor(R.color.lightGreen);
        int reddish = parent.getResources().getColor(R.color.reddish);
        int a = orderDetails.ORDER_STATUS;
        if(a==2){
            pl1.setBackgroundColor(lightGreen);
            pp2.setBackgroundTintList(ColorStateList.valueOf(lightGreen));
        } else if (a == 3) {
            pl1.setBackgroundColor(lightGreen);
            pp2.setBackgroundTintList(ColorStateList.valueOf(lightGreen));
            pl2.setBackgroundColor(lightGreen);
            pp3.setBackgroundTintList(ColorStateList.valueOf(lightGreen));
        }else if (a==4){
            pl1.setBackgroundColor(lightGreen);
            pp2.setBackgroundTintList(ColorStateList.valueOf(lightGreen));
            pl2.setBackgroundColor(lightGreen);
            pp3.setBackgroundTintList(ColorStateList.valueOf(lightGreen));
            pl3.setBackgroundColor(lightGreen);
            pp4.setBackgroundTintList(ColorStateList.valueOf(lightGreen));
        }
        else if(a==5){

            pl1.setBackgroundColor(lightGreen);
            pp2.setBackgroundTintList(ColorStateList.valueOf(lightGreen));
            pl2.setBackgroundColor(lightGreen);
            pp3.setBackgroundTintList(ColorStateList.valueOf(lightGreen));
            pl3.setBackgroundColor(lightGreen);
            pp4.setBackgroundTintList(ColorStateList.valueOf(reddish));
            pds.setText("Order Return Requested !");
            pdz.setText("Return of this order requested by the user :(");

        } else if (a==7) {
            pl1.setBackgroundColor(lightGreen);
            pp2.setBackgroundTintList(ColorStateList.valueOf(lightGreen));
            pl2.setBackgroundColor(lightGreen);
            pp3.setBackgroundTintList(ColorStateList.valueOf(lightGreen));
            pl3.setBackgroundColor(lightGreen);
            pp4.setBackgroundTintList(ColorStateList.valueOf(reddish));
            pds.setText("Order Cancelled !");
            pdz.setText("Order is Cancelled by User :(");
        }

        int p = orderDetails.product.get(0).title.indexOf("--");

        if(p!=-1){
            heading.setText(orderDetails.product.get(0).title.substring(0,p));
        }
        else{
            heading.setText(orderDetails.product.get(0).title);
        }


        sizeText.setText("Size: "+orderDetails.size);
        colorText.setText("Color: "+orderDetails.color);
        qty.setText("Qty: "+orderDetails.quantity);
        fullAddressText.setText(orderDetails.address);
        nameText.setText(orderDetails.name);
        otp.setText(orderDetails.OTP);
        phText.setText(orderDetails.userPh);
        orderId.setText("Order ID : "+orderDetails.ID);
        MopText.setText(orderDetails.modeOfPayment);
        if(flag==1){
            SharedPreferences sharedPreferences =parent.getSharedPreferences("Charges",Context.MODE_PRIVATE);
            int serviceCharge = sharedPreferences.getInt("SERVICE",20);
            int shippingCharge = sharedPreferences.getInt("SHIPPING",10);

            int x = Integer.parseInt(orderDetails.totalAmount);
            TotalAmountText.setText("₹"+x);
        }
        else {
            TotalAmountText.setText("₹"+orderDetails.totalAmount);
        }
        int posOfUri = 0;
        for (int i =0;i<orderDetails.product.get(0).color.size();i++){
            Log.d("posOfUri12",orderDetails.color+"");
            Log.d("posOfUri",orderDetails.product.get(0).color.get(i)+"");
            if(orderDetails.color.equals(orderDetails.product.get(0).color.get(i)) ){
                posOfUri = i;
            }
        }
        Picasso.get().load(orderDetails.product.get(0).colorImages.get(posOfUri)).into(productImage1);




        productImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(buyPageFlag==1){
                    Bundle bundle = new Bundle();
                    ArrayList<ProductModel> orderItems = new ArrayList<>();
                    orderItems.add(orderDetails.product.get(0));
                    bundle.putParcelableArrayList("list",orderItems);

                    ((MainActivity)parent).runFragment(bundle,new BuyPage(),"Buy Page of Confirm Order");
                }
            }
        });
        progressBar.setVisibility(View.INVISIBLE);




        return v;

    }

}