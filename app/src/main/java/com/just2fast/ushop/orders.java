package com.just2fast.ushop;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link orders#newInstance} factory method to
 * create an instance of this fragment.
 */
public class orders extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public orders() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment orders.
     */
    // TODO: Rename and change types and number of parameters
    public static orders newInstance(String param1, String param2) {
        orders fragment = new orders();
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

    RecyclerView orderRecycler,recRecycler;
    Activity parent;
    FirebaseUser user;
    TextView osText,recText;
    ImageView osImg,imageView4;
    UserModel userModel;
    FirebaseDatabase firebaseDatabase;
    Gson gson = new Gson();
    String BannerJson;
    ConstraintLayout constraintLayout;
    ProgressBar progressBar ;
    ImageView promoImg1,promoImg2,promoImg3,promoImg4;
    TextView promoPrice1,promoPrice2,promoPrice3,promoPrice4,promoDis1,promoDis2,promoDis3,promoDis4;
    DoubleListModel products1;
    String jsonTemp;
    int OrdersUpdateFlag;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_orders, container, false);
        parent = getActivity();
        user = FirebaseAuth.getInstance().getCurrentUser();
        orderRecycler = v.findViewById(R.id.orderRecycler);
        osImg = v.findViewById(R.id.osImg);
        progressBar = v.findViewById(R.id.progressBar10);
        progressBar.setVisibility(View.INVISIBLE);
        osText = v.findViewById(R.id.osText);
        constraintLayout = v.findViewById(R.id.cl);
        firebaseDatabase = FirebaseDatabase.getInstance();
        promoImg1 = v.findViewById(R.id.promoImg1);
        promoImg2 = v.findViewById(R.id.promoImg2);
        promoImg3 = v.findViewById(R.id.promoImg3);
        promoImg4 = v.findViewById(R.id.promoImg4);
        promoPrice1 = v.findViewById(R.id.promoPrice1);
        promoPrice2 = v.findViewById(R.id.promoPrice2);
        promoPrice3 = v.findViewById(R.id.promoPrice3);
        promoPrice4 = v.findViewById(R.id.promoPrice4);
        promoDis1 = v.findViewById(R.id.promoDis1);
        promoDis2 = v.findViewById(R.id.promoDis2);
        promoDis3 = v.findViewById(R.id.promoDis3);
        promoDis4 = v.findViewById(R.id.promoDis4);

        SharedPreferences preferences = parent.getSharedPreferences("Update Request",Context.MODE_PRIVATE);
        BannerJson = preferences.getString("bannersData","null");
        OrdersUpdateFlag = preferences.getInt("OrdersUpdate",1);

        ((MainActivity)parent).bnv.setVisibility(View.VISIBLE);

//        String Url = "https://tykun.blob.core.windows.net/users/"+user.getEmail().replace(".","")+".txt";
//        OkHttpClient client =  new OkHttpClient();
//        Request request = new Request.Builder().url(Url).build();
        progressBar.setVisibility(View.VISIBLE);
        SharedPreferences.Editor ed = preferences.edit();

        if(OrdersUpdateFlag == 1) {

            firebaseDatabase.getReference().child("Category").child("UsersData").child(user.getEmail().replace(".","")).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    userModel = snapshot.getValue(UserModel.class);

                    try {
                        parent.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (userModel.myOrders == null) {
                                    osImg.setVisibility(View.VISIBLE);
                                    osText.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Log.d("orderList", "Empty Null");
                                }



                                if (userModel.myOrders.isEmpty()) {

//                                constraintSet.connect(recText.getId(), ConstraintSet.TOP, osText.getId(), ConstraintSet.BOTTOM, 13); // 0 is the margin between the bottom of the ImageView and the top of the TextView
//                                constraintSet.connect(imageView4.getId(), ConstraintSet.TOP, osText.getId(), ConstraintSet.BOTTOM, 13);
//                                constraintSet.applyTo(constraintLayout);
                                    osImg.setVisibility(View.VISIBLE);
                                    osText.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Log.d("orderList", "Empty");

                                } else {
                                    Log.d("myOrdersLength", userModel.myOrders.size() + "");
//                                constraintSet.connect(recText.getId(), ConstraintSet.TOP, orderRecycler.getId(), ConstraintSet.BOTTOM, 13); // 0 is the margin between the bottom of the ImageView and the top of the TextView
//                                constraintSet.connect(imageView4.getId(), ConstraintSet.TOP, orderRecycler.getId(), ConstraintSet.BOTTOM, 13);
//                                constraintSet.applyTo(constraintLayout);
                                    ArrayList<OrderDetails> orders = new ArrayList<>(userModel.myOrders);
                                    Collections.reverse(orders);
                                    OrdersAdapter ordersAdapter = new OrdersAdapter(parent, orders);
                                    orderRecycler.setLayoutManager(new LinearLayoutManager(parent));
                                    orderRecycler.setAdapter(ordersAdapter);
                                    //orderRecycler.setBackgroundColor(parent.getResources().getColor(R.color.grey));
                                    jsonTemp = gson.toJson(userModel);
                                    ed.putString("OrdersList",jsonTemp);

                                }

                                ed.putInt("OrdersUpdate", 0);
                                ed.apply();
                                progressBar.setVisibility(View.INVISIBLE);

                            }
                        });
                    } catch (Exception e) {
                        Log.d("MyOrderException", e + "");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            /*client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.d("OkHttp", "Failure to get");
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                     jsonTemp = response.body().string();
                    Log.d("jsonTemp_orders", jsonTemp + "");
                    Gson gson = new Gson();
                    userModel = new UserModel();
                    userModel = gson.fromJson(jsonTemp, UserModel.class);
                    Log.d("myOrdersLength", userModel.myOrders.size() + "");
                    try {
                        parent.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {



                                if (userModel.myOrders.size() == 0) {

//                                constraintSet.connect(recText.getId(), ConstraintSet.TOP, osText.getId(), ConstraintSet.BOTTOM, 13); // 0 is the margin between the bottom of the ImageView and the top of the TextView
//                                constraintSet.connect(imageView4.getId(), ConstraintSet.TOP, osText.getId(), ConstraintSet.BOTTOM, 13);
//                                constraintSet.applyTo(constraintLayout);
                                    osImg.setVisibility(View.VISIBLE);
                                    osText.setVisibility(View.VISIBLE);
                                    Log.d("orderList", "Empty");

                                } else {
//                                constraintSet.connect(recText.getId(), ConstraintSet.TOP, orderRecycler.getId(), ConstraintSet.BOTTOM, 13); // 0 is the margin between the bottom of the ImageView and the top of the TextView
//                                constraintSet.connect(imageView4.getId(), ConstraintSet.TOP, orderRecycler.getId(), ConstraintSet.BOTTOM, 13);
//                                constraintSet.applyTo(constraintLayout);
                                    ArrayList<OrderDetails> orders = new ArrayList<>(userModel.myOrders);
                                    Collections.reverse(orders);
                                    OrdersAdapter ordersAdapter = new OrdersAdapter(parent, orders);
                                    orderRecycler.setLayoutManager(new LinearLayoutManager(parent));
                                    orderRecycler.setAdapter(ordersAdapter);
                                    //orderRecycler.setBackgroundColor(parent.getResources().getColor(R.color.grey));

                                    ed.putString("OrdersList",jsonTemp);

                                }

                                ed.putInt("OrdersUpdate", 0);
                                ed.apply();
                                progressBar.setVisibility(View.INVISIBLE);

                            }
                        });
                    } catch (Exception e) {
                        Log.d("MyOrderException", e + "");
                    }

                }
            });*/
        }
        else {
            jsonTemp = preferences.getString("OrdersList","null");
            Gson gson = new Gson();
            userModel = new UserModel();
            userModel = gson.fromJson(jsonTemp, UserModel.class);
            //Log.d("myOrdersLength", userModel.myOrders.size() + "");

            try {
                if(userModel.myOrders!=null){
                    if (userModel.myOrders.isEmpty()) {
                        osImg.setVisibility(View.VISIBLE);
                        osText.setVisibility(View.VISIBLE);
                        Log.d("orderList", "Empty");

                    } else {



                        ArrayList<OrderDetails> orders = new ArrayList<>(userModel.myOrders);
                        Collections.reverse(orders);
                        OrdersAdapter ordersAdapter = new OrdersAdapter(parent, orders);
                        orderRecycler.setLayoutManager(new LinearLayoutManager(parent));
                        orderRecycler.setAdapter(ordersAdapter);
                        //orderRecycler.setBackgroundColor(parent.getResources().getColor(R.color.grey));


                    }
                }
            else{
                    osImg.setVisibility(View.VISIBLE);
                    osText.setVisibility(View.VISIBLE);
                    Log.d("orderList", "null");
                }
            }
            catch (Exception e){
                e.printStackTrace();
                osImg.setVisibility(View.VISIBLE);
                osText.setVisibility(View.VISIBLE);
                Log.d("orderList", "null");
            }



            progressBar.setVisibility(View.INVISIBLE);
        }

        Gson gson = new Gson();
        products1 = gson.fromJson(BannerJson,DoubleListModel.class);


        Picasso.get().load(products1.productPromoList.get(0).Images.get(0)).into(promoImg1);
        Picasso.get().load(products1.productPromoList.get(1).Images.get(0)).into(promoImg2);
        Picasso.get().load(products1.productPromoList.get(2).Images.get(0)).into(promoImg3);
        Picasso.get().load(products1.productPromoList.get(3).Images.get(0)).into(promoImg4);

        promoPrice1.setText("₹"+products1.productPromoList.get(0).sellingPrice.replace(products1.productPromoList.get(0).productId,""));
        promoPrice2.setText("₹"+products1.productPromoList.get(1).sellingPrice.replace(products1.productPromoList.get(1).productId,""));
        promoPrice3.setText("₹"+products1.productPromoList.get(2).sellingPrice.replace(products1.productPromoList.get(2).productId,""));
        promoPrice4.setText("₹"+products1.productPromoList.get(3).sellingPrice.replace(products1.productPromoList.get(3).productId,""));


        double dis = (Double.parseDouble(products1.productPromoList.get(0).sellingPrice.replace(products1.productPromoList.get(0).productId,""))/Double.parseDouble(products1.productPromoList.get(0).mrp.replace(products1.productPromoList.get(0).productId,"")))*100;
        promoDis1.setText(((100.000000-dis+"").substring(0,4))+"%off");
        double dis2 = (Double.parseDouble(products1.productPromoList.get(1).sellingPrice.replace(products1.productPromoList.get(1).productId,""))/Double.parseDouble(products1.productPromoList.get(1).mrp.replace(products1.productPromoList.get(1).productId,"")))*100;
        promoDis2.setText(((100.000000-dis2+"").substring(0,4))+"%off");
        double dis3 = (Double.parseDouble(products1.productPromoList.get(2).sellingPrice.replace(products1.productPromoList.get(2).productId,""))/Double.parseDouble(products1.productPromoList.get(2).mrp.replace(products1.productPromoList.get(2).productId,"")))*100;
        promoDis3.setText(((100.000000-dis3+"").substring(0,4))+"%off");
        double dis4 = (Double.parseDouble(products1.productPromoList.get(3).sellingPrice.replace(products1.productPromoList.get(3).productId,""))/Double.parseDouble(products1.productPromoList.get(3).mrp.replace(products1.productPromoList.get(3).productId,"")))*100;
        promoDis4.setText(((100.000000-dis4+"").substring(0,4))+"%off");

        promoImg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                ArrayList<ProductModel> selectedItems = new ArrayList<>();
                selectedItems.add(products1.productPromoList.get(0));
                ((MainActivity)parent).bnv.setVisibility(View.INVISIBLE);
                bundle.putParcelableArrayList("list",selectedItems);
                ((MainActivity)parent).runFragment(bundle,new BuyPage(),"Buy Page");
            }
        });
        promoImg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                ArrayList<ProductModel> selectedItems = new ArrayList<>();
                selectedItems.add(products1.productPromoList.get(1));
                ((MainActivity)parent).bnv.setVisibility(View.INVISIBLE);
                bundle.putParcelableArrayList("list",selectedItems);
                ((MainActivity)parent).runFragment(bundle,new BuyPage(),"Buy Page");
            }
        });
        promoImg3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                ArrayList<ProductModel> selectedItems = new ArrayList<>();
                selectedItems.add(products1.productPromoList.get(2));
                ((MainActivity)parent).bnv.setVisibility(View.INVISIBLE);
                bundle.putParcelableArrayList("list",selectedItems);
                ((MainActivity)parent).runFragment(bundle,new BuyPage(),"Buy Page");
            }
        });
        promoImg4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                ArrayList<ProductModel> selectedItems = new ArrayList<>();
                selectedItems.add(products1.productPromoList.get(3));
                ((MainActivity)parent).bnv.setVisibility(View.INVISIBLE);
                bundle.putParcelableArrayList("list",selectedItems);
                ((MainActivity)parent).runFragment(bundle,new BuyPage(),"Buy Page");
            }
        });

        /*String responseData1= preferences.getString("recData","null");
        Log.d("responseDataElse",responseData1);
        products =  gson.fromJson(responseData1,clothes_model[].class);
        recList.addAll(Arrays.asList(products));
        Collections.shuffle(recList);
        searchResultsRecycle searchResultsRecycle = new searchResultsRecycle(getActivity(), recList);
        recRecycler.setLayoutManager(new GridLayoutManager(parent,2));
        recRecycler.setAdapter(searchResultsRecycle);*/



        return v;
    }
}