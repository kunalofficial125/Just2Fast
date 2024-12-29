package com.just2fast.ushop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;


import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import java.util.Collections;

import java.util.List;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class home extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment home.
     */
    // TODO: Rename and change types and number of parameters
    public static home newInstance(String param1, String param2) {
        home fragment = new home();
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
    Context applicationContext;
    /*StorageReference storageReference;

    ImageView promotionBanner1;
    ImageView promotionBanner2;
    ImageView promotionBanner3;
    ImageView promotionBanner4;
    ImageView promotionBanner5;
    ImageView promotionBanner6;*/
    ImageView searchImage,poster;
    CardView searchCard;
    TextView searchText;


    int PAGE_SIZE = 20; // Number of items per page
    int currentPage = 0;
    boolean isLoading = false;

    String JsonBody;
    Activity parent;
    static String storageConnectionString = "DefaultEndpointsProtocol=https;AccountName=tykun;AccountKey=0DPLRf2Sm3gEkXRcPLQYPT1cPjnpSuHTZY0QpQjtXPRieRAg/+" +
            "vaCycyI3otZj3tu/hPmwKp6Le5+AStW89EtQ==;EndpointSuffix=core.windows.net";
    ProgressBar progressBar8;
    ImageView cat1,cat2,cat3,cat4,cat5;
    RecyclerView bannerRecycle,groceryRecRecycler;
    ImageView promoImg1,promoImg2,promoImg3,promoImg4,bannerPromo;
    TextView promoPrice1,promoPrice2,promoPrice3,promoPrice4,promoDis1,promoDis2,promoDis3,promoDis4;

    LinearLayoutManager linearLayoutManager;
    String City;
    int ii = 0;
    TextView noMoreItemsText;
    NonScrollableRecyclerView recRecyclerView;
    int sizeOfUi=50;
    ArrayList<ProductModel> list = new ArrayList<>();
    ProductModel[]products;
    DoubleListModel products1;
    int Update_Req;
    int completedQueries;
    ScrollView ss;
    Timer time;
    FirebaseDatabase database;
    Gson gson = new Gson();
    ProgressBar loadBar;
    String responseData;
    ArrayList<ProductModel> recList = new ArrayList<>();
    SharedPreferences op ;
    TextView reLocateText;
    ImageView reLocateImg;
    ArrayList<ProductModel> selectedItems = new ArrayList<>();
    SharedPreferences.Editor ed ;
    SharedPreferences preferences1;
    boolean checkShow;
    String responseDataTemp ="";
    String LocationText;
    boolean isEndReached = false; // Flag to prevent multiple actions
    searchResultsRecycle searchResultsRecycle;
    GridLayoutManager gridLayoutManager;
    ExecutorService executorService = Executors.newSingleThreadExecutor();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        applicationContext = getActivity().getApplicationContext();
        View v =inflater.inflate(R.layout.fragment_home, container, false);
        bannerRecycle = v.findViewById(R.id.bannerRecycle);
        searchImage = v.findViewById(R.id.searchImage1);
        cat1 = v.findViewById(R.id.cat1);
        poster = v.findViewById(R.id.poster);
        loadBar = v.findViewById(R.id.loadBar);
        ss = v.findViewById(R.id.ss);
        cat2 = v.findViewById(R.id.cat2);
        progressBar8 = v.findViewById(R.id.progressBar8);
        noMoreItemsText = v.findViewById(R.id.noMoreItemsText);
        database = FirebaseDatabase.getInstance();
        cat3 = v.findViewById(R.id.cat3);
        reLocateImg = v.findViewById(R.id.reLocateImg);
        reLocateText = v.findViewById(R.id.reLocateText);
        groceryRecRecycler = v.findViewById(R.id.groceryRecRecycler);
        cat4 = v.findViewById(R.id.cat4);
        bannerPromo = v.findViewById(R.id.bannerPromo);
        recRecyclerView = v.findViewById(R.id.recRecycler);
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

        cat5 = v.findViewById(R.id.cat5);
        searchText = v.findViewById(R.id.searchText1);
        parent = getActivity();

        if(time!=null){
            time.cancel();
        }



        preferences1 = parent.getSharedPreferences("Grocery",Context.MODE_PRIVATE);
        int y = preferences1.getInt("ss",0);
//         if(y!=0){
//             ss.scrollTo(0, ss.getChildAt(0).getHeight());
//         }
        int noOfGroceryProducts = preferences1.getInt("groceryNoOfProducts",0);
        if(noOfGroceryProducts!=0){
            if(parent instanceof MainActivity){
                ((MainActivity)parent).groceryCard.setVisibility(View.VISIBLE);
                ((MainActivity)parent).noOfProductsText.setText(noOfGroceryProducts+" Items Added");
            }
        }
        else {
            if(parent instanceof MainActivity){
                ((MainActivity)parent).groceryCard.setVisibility(View.INVISIBLE);

            }
        }
        searchCard = v.findViewById(R.id.searchCard);
        linearLayoutManager = new LinearLayoutManager(parent,RecyclerView.HORIZONTAL,false);
        bannerRecycle.setLayoutManager(linearLayoutManager);

        SharedPreferences preferences = parent.getSharedPreferences("UpdateReqData",Context.MODE_PRIVATE);
        SharedPreferences.Editor editorPre = preferences.edit();
        op = parent.getSharedPreferences("Update Request",Context.MODE_PRIVATE);
        ed =op.edit();
        Update_Req = op.getInt("Update Req",0);

        Calendar calendar = Calendar.getInstance();


        SharedPreferences sp23 = parent.getSharedPreferences("loginCheck",Context.MODE_PRIVATE);
        City = sp23.getString("City","Agra");

        // Get the current date and time as a Date object
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("banners",Context.MODE_PRIVATE);
        //int updateReq = sharedPreferences.getInt("updateReq",0);
        int lastUpdateDate = sharedPreferences.getInt("lastUpdateDate",0);

        recRecyclerView.setLayoutManager(new GridLayoutManager(getActivity().getApplicationContext(),2));

        SharedPreferences sp233 = parent.getSharedPreferences("loginCheck",Context.MODE_PRIVATE);
        LocationText = sp233.getString("Location","null");
        reLocateText.setText(LocationText);

        View.OnClickListener onClickListener1 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)parent).runFragment(new Bundle(),new SelectLocation(),"Select Location");
            }
        };

        reLocateImg.setOnClickListener(onClickListener1);
        reLocateText.setOnClickListener(onClickListener1);


        progressBar8.setVisibility(View.VISIBLE);
        //((MainActivity)parent).bnv.setVisibility(View.INVISIBLE);
        //if(lastUpdateDate!=dayOfMonth || updateReq ==0){
        DatabaseReference reference = database.getReference().child("Category").child("products").child("bannerProducts");

        //if(Update_Req==1){
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                products1 = snapshot.getValue(DoubleListModel.class);
                JsonBody = gson.toJson(products1);
                Log.d("JsonBodyOfGrocery",JsonBody+"");
                SharedPreferences sharedPreferences = parent.getSharedPreferences("banners",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("updateReq",1);
                editor.putInt("lastUpdateDate",dayOfMonth);
                editor.putString("bannersData",JsonBody);
                ed.putString("bannersData",JsonBody);
                ed.apply();
                Log.d("updatedDate",lastUpdateDate+" "+dayOfMonth);
                editor.apply();

                //ArrayList<ProductModel> tempList = new ArrayList<>(new ArrayList<>(products1.bannerList));
                list.clear();

                for(int i=0; i<products1.bannerList.size(); i++){
                    if(products1.bannerList.get(i).productCity.equalsIgnoreCase(City)){
                        Log.d("BannerListSize",products1.bannerList.size()+"");
                        list.add(products1.bannerList.get(i));
                        Log.d("listTitle",products1.bannerList.get(i).title);
                    }
                }

                parent.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        ArrayList<String> abc = new ArrayList<>();
                        abc.add(products1.getBannerLink1());
                        abc.add(products1.getBannerLink2());
                        Collections.shuffle(abc);
                        Log.d("bannerPromo",abc.get(0)+"");
                        Picasso.get().load(abc.get(0)).into(bannerPromo);

                        BannerRecycler bannerRecycler = new BannerRecycler(parent,list,getActivity());
                        bannerRecycle.setAdapter(bannerRecycler);

                        LinearSnapHelper linearSnapHelper = new LinearSnapHelper();

                        if (bannerRecycle.getOnFlingListener() == null) {
                            linearSnapHelper.attachToRecyclerView(bannerRecycle);
                        }

                        ((MainActivity)parent).bnv.setVisibility(View.VISIBLE);
                        time = new Timer();
                        time.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                if(linearLayoutManager.findLastCompletelyVisibleItemPosition()<bannerRecycler.getItemCount()-1){
                                    linearLayoutManager.smoothScrollToPosition(bannerRecycle,new RecyclerView.State(),linearLayoutManager.findLastCompletelyVisibleItemPosition()+1);
                                }else if (linearLayoutManager.findLastCompletelyVisibleItemPosition()<bannerRecycler.getItemCount()){
                                    linearLayoutManager.smoothScrollToPosition(bannerRecycle,new RecyclerView.State(),0);
                                }else if (bannerRecycle.getAdapter() != null && bannerRecycle.getAdapter().getItemCount() ==list.size()) {
                                    bannerRecycle.smoothScrollToPosition(0);
                                }
                            }
                        },0,5000);


                        ArrayList<ProductModel> tempProductPromoList = new ArrayList<>();
                        ArrayList<ProductModel> promoList = new ArrayList<>(new ArrayList<>(products1.productPromoList));

                        for(int i=0; i<promoList.size(); i++){
                            if(promoList.get(i).productCity.equalsIgnoreCase(City)){
                                tempProductPromoList.add(promoList.get(i));
                            }
                        }


                        Picasso.get().load(tempProductPromoList.get(0).Images.get(0)).into(promoImg1);
                        Picasso.get().load(tempProductPromoList.get(1).Images.get(0)).into(promoImg2);
                        Picasso.get().load(tempProductPromoList.get(2).Images.get(0)).into(promoImg3);
                        Picasso.get().load(tempProductPromoList.get(3).Images.get(0)).into(promoImg4);

                        promoPrice1.setText("₹"+tempProductPromoList.get(0).sellingPrice.replace(tempProductPromoList.get(0).productId,""));
                        promoPrice2.setText("₹"+tempProductPromoList.get(1).sellingPrice.replace(tempProductPromoList.get(1).productId,""));
                        promoPrice3.setText("₹"+tempProductPromoList.get(2).sellingPrice.replace(tempProductPromoList.get(2).productId,""));
                        promoPrice4.setText("₹"+tempProductPromoList.get(3).sellingPrice.replace(tempProductPromoList.get(3).productId,""));


                        groceryRecRecycler.setLayoutManager(new LinearLayoutManager(parent,RecyclerView.HORIZONTAL,false));
                        GroceryRecommendAdapter groceryRecommendAdapter = new GroceryRecommendAdapter(parent,new ArrayList<>(products1.groceryList));
                        groceryRecRecycler.setAdapter(groceryRecommendAdapter);

                        double dis = (Double.parseDouble(tempProductPromoList.get(0).sellingPrice.replace(tempProductPromoList.get(0).productId,""))/Double.parseDouble(tempProductPromoList.get(0).mrp.replace(tempProductPromoList.get(0).productId,"")))*100;
                        promoDis1.setText(((100.000000-dis+"").substring(0,4))+"%off");
                        double dis2 = (Double.parseDouble(tempProductPromoList.get(1).sellingPrice.replace(tempProductPromoList.get(1).productId,""))/Double.parseDouble(tempProductPromoList.get(1).mrp.replace(tempProductPromoList.get(1).productId,"")))*100;
                        promoDis2.setText(((100.000000-dis2+"").substring(0,4))+"%off");
                        double dis3 = (Double.parseDouble(tempProductPromoList.get(2).sellingPrice.replace(tempProductPromoList.get(2).productId,""))/Double.parseDouble(tempProductPromoList.get(2).mrp.replace(tempProductPromoList.get(2).productId,"")))*100;
                        promoDis3.setText(((100.000000-dis3+"").substring(0,4))+"%off");
                        double dis4 = (Double.parseDouble(tempProductPromoList.get(3).sellingPrice.replace(tempProductPromoList.get(3).productId,""))/Double.parseDouble(tempProductPromoList.get(3).mrp.replace(tempProductPromoList.get(3).productId,"")))*100;
                        promoDis4.setText(((100.000000-dis4+"").substring(0,4))+"%off");

                        promoImg1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Bundle bundle = new Bundle();
                                ArrayList<ProductModel> selectedItems = new ArrayList<>();
                                selectedItems.add(tempProductPromoList.get(0));
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
                                selectedItems.add(tempProductPromoList.get(1));
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
                                selectedItems.add(tempProductPromoList.get(2));
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
                                selectedItems.add(tempProductPromoList.get(3));
                                ((MainActivity)parent).bnv.setVisibility(View.INVISIBLE);
                                bundle.putParcelableArrayList("list",selectedItems);
                                ((MainActivity)parent).runFragment(bundle,new BuyPage(),"Buy Page");
                            }
                        });



                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        String Url = "https://tykun.blob.core.windows.net/categories/BannerProducts.txt";
        // ArrayList<clothes_model> Products = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();


                /*OkHttpClient.Builder builder = new OkHttpClient.Builder();
                builder.connectTimeout(5, TimeUnit.MINUTES) // connect timeout
                        .writeTimeout(5, TimeUnit.MINUTES) // write timeout
                        .readTimeout(5, TimeUnit.MINUTES); // read timeout

                client = builder.build();


                Request request = new Request.Builder()
                        .url(Url)
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Log.d("OkHttp","Failure to get");
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                         JsonBody = response.body().string();
                        Gson gson = new Gson();
                        products1 = gson.fromJson(JsonBody,DoubleListModel.class);
                        Log.d("JsonBodyOfGrocery",JsonBody+"");
                        SharedPreferences sharedPreferences = parent.getSharedPreferences("banners",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("updateReq",1);
                        editor.putInt("lastUpdateDate",dayOfMonth);
                        editor.putString("bannersData",JsonBody);
                        ed.putString("bannersData",JsonBody);
                        ed.apply();
                        Log.d("updatedDate",lastUpdateDate+" "+dayOfMonth);
                        editor.apply();
                        list.addAll(new ArrayList<>(products1.bannerList));
                        parent.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                ArrayList<String> abc = new ArrayList<>();
                                abc.add(products1.bannerLink1);
                                abc.add(products1.bannerLink2);
                                Collections.shuffle(abc);
                                Log.d("bannerPromo",abc.get(0)+"");
                                Picasso.get().load(abc.get(0)).into(bannerPromo);

                                BannerRecycler bannerRecycler = new BannerRecycler(parent,list,getActivity());
                                bannerRecycle.setAdapter(bannerRecycler);

                                LinearSnapHelper linearSnapHelper = new LinearSnapHelper();
                                linearSnapHelper.attachToRecyclerView(bannerRecycle);
                                ((MainActivity)parent).bnv.setVisibility(View.VISIBLE);
                                Timer time = new Timer();
                                time.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        if(linearLayoutManager.findLastCompletelyVisibleItemPosition()<bannerRecycler.getItemCount()-1){
                                            linearLayoutManager.smoothScrollToPosition(bannerRecycle,new RecyclerView.State(),linearLayoutManager.findLastCompletelyVisibleItemPosition()+1);
                                        }else if (linearLayoutManager.findLastCompletelyVisibleItemPosition()<bannerRecycler.getItemCount()-1){
                                            linearLayoutManager.smoothScrollToPosition(bannerRecycle,new RecyclerView.State(),0);
                                        }else if (bannerRecycle.getAdapter() != null && bannerRecycle.getAdapter().getItemCount() ==list.size()) {
                                            bannerRecycle.smoothScrollToPosition(0);
                                        }
                                    }
                                },0,5000);

                                Picasso.get().load(tempProductPromoList.get(0).Images.get(0)).into(promoImg1);
                                Picasso.get().load(tempProductPromoList.get(1).Images.get(0)).into(promoImg2);
                                Picasso.get().load(tempProductPromoList.get(2).Images.get(0)).into(promoImg3);
                                Picasso.get().load(tempProductPromoList.get(3).Images.get(0)).into(promoImg4);

                                promoPrice1.setText("₹"+tempProductPromoList.get(0).sellingPrice.replace(tempProductPromoList.get(0).productId,""));
                                promoPrice2.setText("₹"+tempProductPromoList.get(1).sellingPrice.replace(tempProductPromoList.get(1).productId,""));
                                promoPrice3.setText("₹"+tempProductPromoList.get(2).sellingPrice.replace(tempProductPromoList.get(2).productId,""));
                                promoPrice4.setText("₹"+tempProductPromoList.get(3).sellingPrice.replace(tempProductPromoList.get(3).productId,""));


                                groceryRecRecycler.setLayoutManager(new LinearLayoutManager(parent,RecyclerView.HORIZONTAL,false));
                                GroceryRecommendAdapter groceryRecommendAdapter = new GroceryRecommendAdapter(parent,new ArrayList<>(products1.groceryList));
                                groceryRecRecycler.setAdapter(groceryRecommendAdapter);

                                double dis = (Double.parseDouble(tempProductPromoList.get(0).sellingPrice.replace(tempProductPromoList.get(0).productId,""))/Double.parseDouble(tempProductPromoList.get(0).mrp.replace(tempProductPromoList.get(0).productId,"")))*100;
                                promoDis1.setText(((100.000000-dis+"").substring(0,4))+"%off");
                                double dis2 = (Double.parseDouble(tempProductPromoList.get(1).sellingPrice.replace(tempProductPromoList.get(1).productId,""))/Double.parseDouble(tempProductPromoList.get(1).mrp.replace(tempProductPromoList.get(1).productId,"")))*100;
                                promoDis2.setText(((100.000000-dis2+"").substring(0,4))+"%off");
                                double dis3 = (Double.parseDouble(tempProductPromoList.get(2).sellingPrice.replace(tempProductPromoList.get(2).productId,""))/Double.parseDouble(tempProductPromoList.get(2).mrp.replace(tempProductPromoList.get(2).productId,"")))*100;
                                promoDis3.setText(((100.000000-dis3+"").substring(0,4))+"%off");
                                double dis4 = (Double.parseDouble(tempProductPromoList.get(3).sellingPrice.replace(tempProductPromoList.get(3).productId,""))/Double.parseDouble(tempProductPromoList.get(3).mrp.replace(tempProductPromoList.get(3).productId,"")))*100;
                                promoDis4.setText(((100.000000-dis4+"").substring(0,4))+"%off");

                                promoImg1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Bundle bundle = new Bundle();
                                        ArrayList<ProductModel> selectedItems = new ArrayList<>();
                                        selectedItems.add(tempProductPromoList.get(0));
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
                                        selectedItems.add(tempProductPromoList.get(1));
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
                                        selectedItems.add(tempProductPromoList.get(2));
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
                                        selectedItems.add(tempProductPromoList.get(3));
                                        ((MainActivity)parent).bnv.setVisibility(View.INVISIBLE);
                                        bundle.putParcelableArrayList("list",selectedItems);
                                        ((MainActivity)parent).runFragment(bundle,new BuyPage(),"Buy Page");
                                    }
                                });



                            }
                        });
                    }
                });*/




        List<String> blobNames = new ArrayList<>();
//                  blobNames.add("tshirt.txt");
        blobNames.add("milk");
        blobNames.add("tshirt");
        blobNames.add("jean");
        blobNames.add("shoe");
        blobNames.add("top");




        Log.d("namesCheck",blobNames.get(0)+"");

        completedQueries = 0;



        recList.clear();
        selectedItems.clear();
        for ( ii=0;ii<blobNames.size();ii++) {
            // Construct the URL of the blob
            int c = ii;
            ArrayList<ProductModel> l1 = new ArrayList<>();
            DatabaseReference df = database.getReference().child("Category").child("products").child(blobNames.get(ii));

            df.limitToFirst(50).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    completedQueries++;

                    if(completedQueries<=blobNames.size()){
                        Log.d("EnterInQuery",completedQueries+"");
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            ProductModel p1 = childSnapshot.getValue(ProductModel.class);
                            Log.d("checkOutElement",p1.title);



                            if(City.equalsIgnoreCase(p1.productCity)){
                                recList.add(p1);
                            }


                        }
                        //recList.addAll(l1);
                        if (c == blobNames.size() - 1) {
                            // All responses have been received, handle the overall response data here
                            parent.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d("RecListSize",recList.size()+"");
                                    Collections.shuffle(recList);
                                    for(int i =0 ;i<recList.size();i++){
                                        Log.d("RecList",i+"");
                                    }
                                    // Process overallResponseData as needed
                                    // For example, you can log it or store it in SharedPreferences
                                    responseDataTemp = gson.toJson(recList);
                                    Log.d("RecListSize",recList.size()+"");
                                    Log.d("OverallResponseData", responseDataTemp.toString());
                                    // You can also use it to update your UI
                                    Log.d("responseData",responseDataTemp);
                                    editorPre.putString("recData",responseDataTemp);
                                    editorPre.apply();
                                    searchResultsRecycle = new searchResultsRecycle(getActivity(), selectedItems,home.this);
                                    gridLayoutManager = new GridLayoutManager(parent, 2);
                                    recRecyclerView.setLayoutManager(gridLayoutManager);
                                    recRecyclerView.setAdapter(searchResultsRecycle);
                                    if(recList.size()<15){
                                        selectedItems.addAll(recList);
                                        currentPage = recList.size()-1;
                                    }
                                    else{
                                        selectedItems.addAll(recList.subList(0,15));
                                        currentPage = 15;
                                    }

                                    ss.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                                        @Override
                                        public void onScrollChanged() {
                                            View view = ss.getChildAt(ss.getChildCount() - 1);

                                            int diff = (view.getBottom() - (ss.getHeight() + ss.getScrollY()));

                                            Log.d("lastItemOfGrid",gridLayoutManager.findLastCompletelyVisibleItemPosition()+"");
                                            Log.d("RecListSize",recList.size()-1+"");
                                            Log.d("currentPageBefore",currentPage+"");

                                            if(currentPage==recList.size()-1){
                                                loadBar.setVisibility(View.INVISIBLE);
                                                noMoreItemsText.setVisibility(View.VISIBLE);
                                            }

                                            if (diff == 0) {
                                                // End of the ScrollView is reached
                                                if (!isEndReached) {

                                                    isEndReached = true;
                                                    // Toast.makeText(applicationContext, "End", Toast.LENGTH_SHORT).show();

                                                    Log.d("itemPos",gridLayoutManager.findLastCompletelyVisibleItemPosition()+"");
                                                    Log.d("currentPage",currentPage+"");
                                                    if(currentPage==gridLayoutManager.findLastCompletelyVisibleItemPosition()+1){
                                                        checkShow = true;
                                                        Log.d("checkShow","true");
                                                    }

                                                    if(checkShow){

                                                        if(currentPage!=recList.size()-1){
                                                            if(recList.size()-currentPage < 15){
                                                                selectedItems.addAll(recList.subList(currentPage,recList.size()));
                                                                searchResultsRecycle.notifyItemInserted(currentPage-1);
                                                                currentPage = recList.size()-1;
                                                            }
                                                            else{
                                                                selectedItems.addAll(recList.subList(currentPage+1,currentPage+15));
                                                                searchResultsRecycle.notifyItemInserted(currentPage-1);
                                                                currentPage = currentPage+15;
                                                            }
                                                        }
                                                        else{
                                                            isEndReached = false;
                                                        }

                                                    }
                                                    else {
                                                        Log.d("checkShow","false");
                                                    }





                                                    // Perform your action here
                                                }

                                            } else {
                                                // Reset the flag when scrolling up
                                                isEndReached = false;
                                            }
                                        }

                                    });

                                    progressBar8.setVisibility(View.INVISIBLE);

                                }
                            });
                        }
                    }
                    else {
                        Log.d("My HardStick","home584");

                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });






            // Create a request object













                /*OkHttpClient client1 = new OkHttpClient();

                OkHttpClient.Builder builder1 = new OkHttpClient.Builder();
                builder1.connectTimeout(5, TimeUnit.MINUTES) // connect timeout
                        .writeTimeout(5, TimeUnit.MINUTES) // write timeout
                        .readTimeout(5, TimeUnit.MINUTES); // read timeout

                client1 = builder1.build();

                // Construct the base URL of the blob container
                String containerUrl = "https://tykun.blob.core.windows.net/categories";

                // List blobs in the container (this should be implemented using Azure Storage SDK or REST API)
                List<String> blobNames = new ArrayList<>();
                blobNames.add("tshirt.txt");
                blobNames.add("shoe.txt");


                // Iterate through the list of blob names
                for (int i=0;i<blobNames.size();i++) {
                    // Construct the URL of the blob
                    String blobUrl = containerUrl + "/" + blobNames.get(i);

                    // Create a request object
                    Request request1 = new Request.Builder()
                            .url(blobUrl)
                            .build();

                    // Perform the GET request asynchronously
                    int finalI = i;
                    client1.newCall(request1).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, IOException e) {
                            // Handle failure
                            e.printStackTrace();
                        }



                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            if (response.isSuccessful()) {
                                // Handle successful response
                                 responseData = response.body().string();
                                 responseDataTemp = responseDataTemp+responseData.substring(1,responseData.length()-1)+",";
                                ProductModel[] products =  gson.fromJson(responseData, ProductModel[].class);
                                recList.addAll(Arrays.asList(products));
                                // Process responseData as needed

                            } else {
                                // Handle unsuccessful response
                                Log.d("responseData",""+response.code());
                            }
                            Collections.shuffle(recList);


                            if (finalI == blobNames.size() - 1) {
                                // All responses have been received, handle the overall response data here
                                parent.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Process overallResponseData as needed
                                        // For example, you can log it or store it in SharedPreferences
                                        Log.d("OverallResponseData", responseDataTemp.toString());
                                        // You can also use it to update your UI
                                        responseDataTemp = "["+responseDataTemp.substring(0,responseDataTemp.length()-1)+"]";
                                        Log.d("responseData",responseDataTemp);
                                        editorPre.putString("recData",responseDataTemp);
                                        editorPre.apply();
                                        searchResultsRecycle searchResultsRecycle = new searchResultsRecycle(getActivity(), recList);
                                        recRecyclerView.setLayoutManager(new GridLayoutManager(parent, 2));
                                        recRecyclerView.setAdapter(searchResultsRecycle);
                                        progressBar8.setVisibility(View.INVISIBLE);
                                    }
                                });
                            }



                        }
                    });*/
        }
        //String recData = gson.toJson(recList,new TypeToken<ArrayList<clothes_model>>(){}.getType());



        database.getReference().child("Category").child("AvailableCategory").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                FetchCategoryModel fetchCategoryModel = snapshot.getValue(FetchCategoryModel.class);

                if(fetchCategoryModel!=null){
                    Log.d("FetchCategoryModel",fetchCategoryModel.fashionCat.get(0));
                    Log.d("FetchCategoryModel",fetchCategoryModel.fashionCat.get(1));
                    Log.d("FetchCategoryModelGro",fetchCategoryModel.groceryCat.get(0));
                    Log.d("FetchCategoryModelGro",fetchCategoryModel.groceryCat.get(1));

                    ArrayList<String> fashionCatTemp = new ArrayList<>();
                    ArrayList<String> groceryCatTemp = new ArrayList<>();

                    for(int i =0;i<fetchCategoryModel.fashionCat.size();i++){
                        fashionCatTemp.add(fetchCategoryModel.fashionCat.get(i).toLowerCase().replace(" ",""));
                    }

                    for(int i = 0; i<fetchCategoryModel.groceryCat.size();i++){
                        groceryCatTemp.add(fetchCategoryModel.groceryCat.get(i).toLowerCase().replace(" ",""));
                    }

                    fetchCategoryModel.fashionCat = fashionCatTemp;
                    fetchCategoryModel.groceryCat = groceryCatTemp;

                }

                SharedPreferences sp1225 = parent.getSharedPreferences("Category List",Context.MODE_PRIVATE);
                SharedPreferences.Editor ed1225 = sp1225.edit();
                ed1225.putString("Category List",gson.toJson(fetchCategoryModel));
                ed1225.apply();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        ed.putInt("Update Req",0);
        ed.apply();
        //}
            /*else {



                JsonBody = op.getString("bannersData","null");
                Gson gson = new Gson();
                products1 = gson.fromJson(JsonBody,DoubleListModel.class);
                Log.d("jsonBodyOfHome",JsonBody+"");
                list.clear();
                list.addAll(products1.bannerList);
                BannerRecycler bannerRecycler = new BannerRecycler(parent,list,getActivity());
                bannerRecycle.setAdapter(bannerRecycler);

                ArrayList<ProductModel> groceryList = new ArrayList<>();
                groceryList.addAll(products1.groceryList);

                groceryRecRecycler.setLayoutManager(new LinearLayoutManager(parent,RecyclerView.HORIZONTAL,false));
                GroceryRecommendAdapter groceryRecommendAdapter = new GroceryRecommendAdapter(parent,new ArrayList<>(groceryList));
                groceryRecRecycler.setAdapter(groceryRecommendAdapter);

                LinearSnapHelper linearSnapHelper = new LinearSnapHelper();
                linearSnapHelper.attachToRecyclerView(bannerRecycle);
                ((MainActivity)parent).bnv.setVisibility(View.VISIBLE);
                Timer time = new Timer();
                time.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if(linearLayoutManager.findLastCompletelyVisibleItemPosition()<bannerRecycler.getItemCount()){
                            linearLayoutManager.smoothScrollToPosition(bannerRecycle,new RecyclerView.State(),linearLayoutManager.findLastCompletelyVisibleItemPosition()+1);
                        }else if (linearLayoutManager.findLastCompletelyVisibleItemPosition()<bannerRecycler.getItemCount()){
                            linearLayoutManager.smoothScrollToPosition(bannerRecycle,new RecyclerView.State(),0);
                        }else if (bannerRecycle.getAdapter() != null && bannerRecycle.getAdapter().getItemCount() ==list.size()) {
                            bannerRecycle.smoothScrollToPosition(0);
                        }
                    }
                },5000);

                ArrayList<String> abc = new ArrayList<>();
                abc.add(products1.bannerLink1);
                abc.add(products1.bannerLink2);
                Collections.shuffle(abc);
                Log.d("bannerPromo",abc.get(0)+"");
                Picasso.get().load(abc.get(0)).into(bannerPromo);


                Picasso.get().load(tempProductPromoList.get(0).Images.get(0)).into(promoImg1);
                Picasso.get().load(tempProductPromoList.get(1).Images.get(0)).into(promoImg2);
                Picasso.get().load(tempProductPromoList.get(2).Images.get(0)).into(promoImg3);
                Picasso.get().load(tempProductPromoList.get(3).Images.get(0)).into(promoImg4);

                promoPrice1.setText("₹"+tempProductPromoList.get(0).sellingPrice.replace(tempProductPromoList.get(0).productId,""));
                promoPrice2.setText("₹"+tempProductPromoList.get(1).sellingPrice.replace(tempProductPromoList.get(1).productId,""));
                promoPrice3.setText("₹"+tempProductPromoList.get(2).sellingPrice.replace(tempProductPromoList.get(2).productId,""));
                promoPrice4.setText("₹"+tempProductPromoList.get(3).sellingPrice.replace(tempProductPromoList.get(3).productId,""));


                double dis = (Double.parseDouble(tempProductPromoList.get(0).sellingPrice.replace(tempProductPromoList.get(0).productId,""))/Double.parseDouble(tempProductPromoList.get(0).mrp.replace(tempProductPromoList.get(0).productId,"")))*100;
                promoDis1.setText(((100.000000-dis+"").substring(0,4))+"%off");
                double dis2 = (Double.parseDouble(tempProductPromoList.get(1).sellingPrice.replace(tempProductPromoList.get(1).productId,""))/Double.parseDouble(tempProductPromoList.get(1).mrp.replace(tempProductPromoList.get(1).productId,"")))*100;
                promoDis2.setText(((100.000000-dis2+"").substring(0,4))+"%off");
                double dis3 = (Double.parseDouble(tempProductPromoList.get(2).sellingPrice.replace(tempProductPromoList.get(2).productId,""))/Double.parseDouble(tempProductPromoList.get(2).mrp.replace(tempProductPromoList.get(2).productId,"")))*100;
                promoDis3.setText(((100.000000-dis3+"").substring(0,4))+"%off");
                double dis4 = (Double.parseDouble(tempProductPromoList.get(3).sellingPrice.replace(tempProductPromoList.get(3).productId,""))/Double.parseDouble(tempProductPromoList.get(3).mrp.replace(tempProductPromoList.get(3).productId,"")))*100;
                promoDis4.setText(((100.000000-dis4+"").substring(0,4))+"%off");

                promoImg1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        ArrayList<ProductModel> selectedItems = new ArrayList<>();
                        selectedItems.add(tempProductPromoList.get(0));
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
                        selectedItems.add(tempProductPromoList.get(1));
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
                        selectedItems.add(tempProductPromoList.get(2));
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
                        selectedItems.add(tempProductPromoList.get(3));
                        ((MainActivity)parent).bnv.setVisibility(View.INVISIBLE);
                        bundle.putParcelableArrayList("list",selectedItems);
                        ((MainActivity)parent).runFragment(bundle,new BuyPage(),"Buy Page");
                    }
                });



                String responseData1= preferences.getString("recData","null");
                Log.d("responseDataElse",responseData1);
                 products =  gson.fromJson(responseData1, ProductModel[].class);
                 recList.clear();
                recList.addAll(Arrays.asList(products));
                if(y==0){
                    Collections.shuffle(recList);
                }
                searchResultsRecycle searchResultsRecycle = new searchResultsRecycle(getActivity(), selectedItems,home.this);
                recRecyclerView.setLayoutManager(new GridLayoutManager(parent,2));
                recRecyclerView.setAdapter(searchResultsRecycle);
                progressBar8.setVisibility(View.INVISIBLE);

            }*/

        //}
        /*else{
            String JsonBody = sharedPreferences.getString("bannersData","null");
            Gson gson = new Gson();
            products = gson.fromJson(JsonBody,clothes_model[].class);
            list.addAll(Arrays.asList(products));
            BannerRecycler bannerRecycler  = new BannerRecycler(getActivity().getApplicationContext(),list,getActivity());
            bannerRecycle.setAdapter(bannerRecycler);

            LinearSnapHelper linearSnapHelper = new LinearSnapHelper();
            linearSnapHelper.attachToRecyclerView(bannerRecycle);

            Timer time = new Timer();
            time.schedule(new TimerTask() {
                @Override
                public void run() {
                    if(linearLayoutManager.findLastCompletelyVisibleItemPosition()<bannerRecycler.getItemCount()-1){
                        linearLayoutManager.smoothScrollToPosition(bannerRecycle,new RecyclerView.State(),linearLayoutManager.findLastCompletelyVisibleItemPosition()+1);
                    }else if (linearLayoutManager.findLastCompletelyVisibleItemPosition()<bannerRecycler.getItemCount()-1){
                        linearLayoutManager.smoothScrollToPosition(bannerRecycle,new RecyclerView.State(),0);
                    }else if (bannerRecycle.getAdapter() != null && bannerRecycle.getAdapter().getItemCount() ==list.size()) {
                        bannerRecycle.smoothScrollToPosition(0);
                    }
                }
            },0,5000);

        }*/





        searchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(parent,search_instance.class));
                parent.finish();
            }
        });
        searchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(parent,search_instance.class));
                parent.finish();
            }
        });
        searchCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(parent,search_instance.class));
                parent.finish();
            }
        });

        poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(parent,search_instance.class);
                intent.putExtra("query","tshirt");
                startActivity(intent);
                parent.finish();
            }
        });

        SharedPreferences sp = getActivity().getSharedPreferences("DirectQuery", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        cat1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(parent,search_instance.class);
                intent.putExtra("query","tshirt");
                editor.putInt("directQueryRun2",1);
                editor.apply();
                startActivity(intent);
                parent.finish();
            }
        });
        cat2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(parent,search_instance.class);
                intent.putExtra("query","shoes");
                editor.putInt("directQueryRun2",1);
                editor.apply();
                startActivity(intent);
                parent.finish();
            }
        });
        cat3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(parent,search_instance.class);
                intent.putExtra("query","jeans");
                editor.putInt("directQueryRun2",1);
                editor.apply();
                startActivity(intent);
                parent.finish();
            }
        });
        cat4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(parent,search_instance.class);
                intent.putExtra("query","watches");
                editor.putInt("directQueryRun2",1);
                editor.apply();
                startActivity(intent);
                parent.finish();
            }
        });
        cat5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((MainActivity)parent).runFragment(new Bundle(),new cart(),"Category");

            }
        });

//        List<String> images = new ArrayList<>();
//        images.add("https://cdn.shopify.com/s/files/1/0603/3031/1875/products/main-square_71428963-5c8d-4658-8638-71d72f5d7b05_x480.jpg?v=1695637141");
//        images.add("https://i.ebayimg.com/images/g/WxwAAOSwjIJj~~6c/s-l1200.webp");
//        //images.add("https://5.imimg.com/data5/SELLER/Default/2021/4/GV/JJ/DX/13100216/mens-black-plain-t-shirt.jpg");
//        List<String> colors = new ArrayList<>();
//        colors.add("yellow");
//        colors.add("blue");
//        List<String> size = new ArrayList<>();
//        size.add("XL");
//        List<String> colorImages = new ArrayList<>();
//        colorImages.add("https://cdna.lystit.com/520/650/n/photos/stadiumgoods/e47829cb/nike-White-aqua-pink-Run-Swift-3-white-Aqua-Pink-Shoes.jpeg");
//        colorImages.add("https://productimages.footy.com/63c6d0f27e517bb3ec8cb1f9/0/3840.webp?q=75");
//        size.add("L");
//        clothes_model tshirt = new clothes_model("Nike Shoes For Men",colors,colorImages,images,"tshirt",size,"nylon","boy","fancy","899","599","sento","2",
//                "This Fancy shoes is the perfect blend of comfort and style. Made from high-quality cotton, it's soft and breathable, and perfect for everyday wear. The shirt features a unique graphic design on the front, showcasing a bold and eye-catching print that's sure to turn heads.","product3421","0","0","0","0","0","1","1","merchant0123");
//
//        List<String> images1 = new ArrayList<>();
//        images1.add("https://www.costco.com.au/medias/sys_master/images/h72/h6e/32831006638110.jpg");
//        images1.add("https://www.costco.co.uk/medias/sys_master/images/hec/h05/137946651590686.jpg");
//       // images.add("https://5.imimg.com/data5/SELLER/Default/2021/4/GV/JJ/DX/13100216/mens-black-plain-t-shirt.jpg");
//        List<String> colors1 = new ArrayList<>();
//        colors1.add("yellow");
//        colors1.add("blue");
//        List<String> size1 = new ArrayList<>();
//        size1.add("XL");
//        List<String> colorImages1 = new ArrayList<>();
//        colorImages1.add("https://purepng.com/public/uploads/large/purepng.com-blue-t-shirtclothingt-shirtt-shirtdressfashionclothshirt-691522330467vjhb0.png");
//        colorImages1.add("https://th.bing.com/th/id/OIP.bD3dT2CazF4__z4TJVz8FQHaHR?pid=ImgDet&rs=1");
//        size.add("L");
//        clothes_model tshirt1 = new clothes_model("Cola Pack of 6",colors1,colorImages1,images1,"tshirt",size1,"nylon","boy","fancy","899","599","sento","2",
//                "This Fancy t-shirt is the perfect blend of comfort and style. Made from high-quality cotton, it's soft and breathable, and perfect for everyday wear. The shirt features a unique graphic design on the front, showcasing a bold and eye-catching print that's sure to turn heads.","product3421","0","0","0","0","0","1","1","merchant0123");
//
//
//
//        List<String> images2 = new ArrayList<>();
//        images2.add("https://th.bing.com/th/id/OIP.Ub6YpRIAymTwuyjRcSD92QHaIL?pid=ImgDet&rs=1");
//        images2.add("https://th.bing.com/th/id/OIP.yRSLuEAM-xc3Lhvi6DSAbwHaHa?pid=ImgDet&rs=1");
//        images2.add("https://5.imimg.com/data5/SELLER/Default/2021/4/GV/JJ/DX/13100216/mens-black-plain-t-shirt.jpg");
//        List<String> colors2 = new ArrayList<>();
//        colors2.add("yellow");
//        colors2.add("blue");
//        List<String> size2 = new ArrayList<>();
//        size2.add("XL");
//        List<String> colorImages2 = new ArrayList<>();
//        colorImages2.add("https://purepng.com/public/uploads/large/purepng.com-blue-t-shirtclothingt-shirtt-shirtdressfashionclothshirt-691522330467vjhb0.png");
//        colorImages2.add("https://th.bing.com/th/id/OIP.bD3dT2CazF4__z4TJVz8FQHaHR?pid=ImgDet&rs=1");
//        size2.add("L");
//        clothes_model tshirt2 = new clothes_model("Fancy Tshirt for boy - Tshirt",colors2,colorImages2,images2,"tshirt",size2,"nylon","boy","fancy","899","599","sento","2",
//                "This Fancy t-shirt is the perfect blend of comfort and style. Made from high-quality cotton, it's soft and breathable, and perfect for everyday wear. The shirt features a unique graphic design on the front, showcasing a bold and eye-catching print that's sure to turn heads.","product3421","0","0","0","0","0","1","1","merchant0123");
//
//
//        List<String> images3 = new ArrayList<>();
//        images3.add("https://images.meesho.com/images/products/231602575/1pnld_512.jpg");
//        images3.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTm3KqG9lPz8mQubJZqPGwTeqHtK42D6IfY0oXkfMq7RvRKj66Ma8wGbiqtRL7SzFhioP0&usqp=CAU");
//        //images.add("https://5.imimg.com/data5/SELLER/Default/2021/4/GV/JJ/DX/13100216/mens-black-plain-t-shirt.jpg");
//        List<String> colors3 = new ArrayList<>();
//        colors3.add("yellow");
//        colors3.add("blue");
//        List<String> size3 = new ArrayList<>();
//        size3.add("XL");
//        List<String> colorImages3 = new ArrayList<>();
//        colorImages3.add("https://lp2.hm.com/hmgoepprod?set=quality%5B79%5D%2Csource%5B%2Fd9%2Fd5%2Fd9d5139b8bc293ac9840486c0c9e74dc06511b41.jpg%5D%2Corigin%5Bdam%5D%2Ccategory%5Bladies_jeans_flare%5D%2Ctype%5BDESCRIPTIVESTILLLIFE%5D%2Cres%5Bm%5D%2Chmver%5B1%5D&call=url[file:/product/mobilemain]");
//        //colorImages.add("https://th.bing.com/th/id/OIP.bD3dT2CazF4__z4TJVz8FQHaHR?pid=ImgDet&rs=1");
//        size3.add("L");
//        clothes_model tshirt3 = new clothes_model("Fancy Jeans for boy - Tshirt",colors3,colorImages3,images3,"tshirt",size3,"nylon","boy","fancy","899","599","sento","2",
//                "This Fancy jeans is the perfect blend of comfort and style. Made from high-quality cotton, it's soft and breathable, and perfect for everyday wear. The shirt features a unique graphic design on the front, showcasing a bold and eye-catching print that's sure to turn heads.","product3421","0","0","0","0","0","1","1","merchant0123");






        /*promotionBanner1 = v.findViewById(R.id.promotion_banner1);
        promotionBanner2 = v.findViewById(R.id.promotion_banner2);
        promotionBanner3 =v.findViewById(R.id.promotion_banner3);
        promotionBanner4 = v.findViewById(R.id.promotion_banner4);
        promotionBanner5  = v.findViewById(R.id.promotion_banner5);
        promotionBanner6 = v.findViewById(R.id.promotion_banner6);
        searchText = v.findViewById(R.id.searchText);
        searchImage = v.findViewById(R.id.searchImage);
        List<String> color = new ArrayList<>();
        color.add("blue");
        color.add("green");

        List<String> images = new ArrayList<>();
        images.add("https://cdn.pixabay.com/photo/2012/08/27/14/19/mountains-55067__340.png");

        List<String> size = new ArrayList<>();
        size.add("S");
        size.add("XL");


        clothes_model c1 = new clothes_model("Fancy Tshirt for men",color,images,"tshirt",size,"cotton","male","party","300","150","Marvel","1","mens tss");
        clothes_model c2 = new clothes_model("Cool Tshirt for men",color,images,"shirt",size,"wool","male","formal","200","120","DC","1","men tss");
        FirebaseDatabase.getInstance().getReference().child("Category").child("t-shirt").child("men").child("Fancy Tshirt for men").setValue(c1);
        FirebaseDatabase.getInstance().getReference().child("Category").child("t-shirt").child("men").child("Cool Tshirt for men").setValue(c2);


        searchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity().getApplicationContext(),search_instance.class));
            }
        });
        searchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity().getApplicationContext(),search_instance.class));
            }
        });

        FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();

        firebaseStorage.getReference("PromotionBanners/promotion_banner_1.png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                    Glide.with(applicationContext).load(uri.toString()).placeholder(R.drawable.placeholder_for_banners).into(promotionBanner1);
                    Log.d("banner_url",uri+" ");
                }
            });
        firebaseStorage.getReference("PromotionBanners/promotion_banner_2.png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(applicationContext).load(uri.toString()).placeholder(R.drawable.placeholder_for_banners).into(promotionBanner2);
                Log.d("banner_url",uri+" ");
            }
        });
        firebaseStorage.getReference("PromotionBanners/promotion_banner_3.png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(applicationContext).load(uri.toString()).placeholder(R.drawable.placeholder_for_banners).into(promotionBanner3);
                Log.d("banner_url",uri+" ");
            }
        });
        firebaseStorage.getReference("PromotionBanners/promotion_banner_4.png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(applicationContext).load(uri.toString()).placeholder(R.drawable.placeholder_for_banners).into(promotionBanner4);
                Log.d("banner_url",uri+" ");
            }
        });
        firebaseStorage.getReference("PromotionBanners/promotion_banner_5.png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(applicationContext).load(uri.toString()).placeholder(R.drawable.placeholder_for_banners).into(promotionBanner5);
                Log.d("banner_url",uri+" ");
            }
        });
        firebaseStorage.getReference("PromotionBanners/promotion_banner_6.png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(applicationContext).load(uri.toString()).placeholder(R.drawable.placeholder_for_banners).into(promotionBanner6);
                Log.d("banner_url",uri+" ");
            }
        });*/

        return v;
    }

    private void performBackgroundTask() {
        // Your background task code here
        try {
            // Simulate a background task

            Thread.sleep(2000); // Simulate delay
            Log.d("YourActivity", "Background task completed.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Handle the interrupt
            Log.e("YourActivity", "Background task interrupted.", e);
        }
    }


}