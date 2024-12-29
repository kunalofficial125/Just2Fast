package com.just2fast.ushop;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.reflect.TypeToken;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.gson.Gson;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;


public class BuyPage extends Fragment {
    RecyclerView recycler;
    RecyclerView colorsRecycler;
    Gson gson =  new Gson();
    String storageConnectionString = "DefaultEndpointsProtocol=https;AccountName=tykun;AccountKey=0DPLRf2Sm3gEkXRcPLQYPT1cPjnpSuHTZY0QpQjtXPRieRAg/+" +
            "vaCycyI3otZj3tu/hPmwKp6Le5+AStW89EtQ==;EndpointSuffix=core.windows.net";
    TextView moreDetailsText,sizeAlert,colorShow;
    int noOfProducts,rating;
    HashMap<String,String> starRef = new HashMap<>();
    CloudBlobContainer blobContainer;
    int userInfoValue=0;
    Button done,buy;
    boolean containsKey;
    FirebaseStorage storage;
    File localFile;
    ArrayList<ProductModel> list = new ArrayList<>();
    String userLat,userLong;
    ArrayList<ProductModel> listTemp = new ArrayList<>();
    ProductModel currentProductTemp;
    ScrollView scrollView2;
    String userContact,jsonTemp;
    ImageButton rateButton;
    FirebaseDatabase firebaseDatabase;
    TextView titleView,sellingPriceTextView, offText, reduceItems,countItems,increaseItems,ratedText,starAverage,description;
    RecyclerView sizeRecycler;
    String ratingStarByUser;
    FirebaseUser user;
    Activity parent;
    UserModel userData;
    TextView outOfStockText,outOfStockDate;
    ImageView backBtn;
    ProductModel currentProduct;
    UserModel userModel;
    ProgressBar progressBar1,progressBar2,progressBar3,progressBar4,progressBar5;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_buy_page, container, false);
        parent = getActivity();
        storage = FirebaseStorage.getInstance();
        titleView = v.findViewById(R.id.titleView);
        progressBar1 = v.findViewById(R.id.progressBar1);
        sizeAlert = v.findViewById(R.id.sizeAlert);
        sizeAlert.setVisibility(View.INVISIBLE);
        description = v.findViewById(R.id.description);
        sellingPriceTextView = v.findViewById(R.id.sellingPriceTextView);
        progressBar2 = v.findViewById(R.id.progressBar2);
        starAverage = v.findViewById(R.id.starAverage);
        backBtn = v.findViewById(R.id.backBtn);
        colorShow = v.findViewById(R.id.sizeShow);
        outOfStockText = v.findViewById(R.id.outOfStockText);
        outOfStockDate = v.findViewById(R.id.outOfStockDate);
        scrollView2 = v.findViewById(R.id.scrollView2);
        rateButton = v.findViewById(R.id.rate);
        ratedText = v.findViewById(R.id.ratedText);
        ratedText.setVisibility(View.INVISIBLE);
        buy = v.findViewById(R.id.buy);
        progressBar3 = v.findViewById(R.id.progressBar3);
        progressBar4 = v.findViewById(R.id.progressBar4);
        progressBar5 = v.findViewById(R.id.progressBar5);



        recycler = v.findViewById(R.id.recycler);

        offText  = v.findViewById(R.id.offText);

        reduceItems = v.findViewById(R.id.reduceItems);
        increaseItems = v.findViewById(R.id.increaseItems);
        countItems = v.findViewById(R.id.countItems);

        sizeRecycler = v.findViewById(R.id.sizeRecycler);
        moreDetailsText = v.findViewById(R.id.moreDetailsText);

        if(getActivity() instanceof MainActivity){
            ((MainActivity)parent).bnv.setVisibility(View.INVISIBLE);
            ((MainActivity)parent).groceryCard.setVisibility(View.INVISIBLE);
        }

        listTemp = getArguments().getParcelableArrayList("list");
        currentProductTemp = listTemp.get(0);
        user = FirebaseAuth.getInstance().getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        SharedPreferences userLatLong = parent.getSharedPreferences("userLatLong",Context.MODE_PRIVATE);
        userLat = userLatLong.getString("lat","null");
        userLong = userLatLong.getString("long","null");








        Dialog rateDialog = new Dialog(parent);
        rateDialog.setContentView(R.layout.ratingdialog);
        ImageView star1 = rateDialog.findViewById(R.id.star1);
        ImageView star2 = rateDialog.findViewById(R.id.star2);
        ImageView star3 = rateDialog.findViewById(R.id.star3);
        ImageView star4 = rateDialog.findViewById(R.id.star4);
        ImageView star5 = rateDialog.findViewById(R.id.star5);
        done  = rateDialog.findViewById(R.id.done);
        TextView titleDialog = rateDialog.findViewById(R.id.titleDialog);
        ImageView productImgOfDialog = rateDialog.findViewById(R.id.productImg);
        Picasso.get().load(currentProductTemp.Images.get(0)).into(productImgOfDialog);
        titleDialog.setText(currentProductTemp.title);
        star5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable drawable = getActivity().getDrawable(R.drawable.baseline_star_rate_24);
                star1.setImageDrawable(drawable);
                star2.setImageDrawable(drawable);
                star3.setImageDrawable(drawable);
                star4.setImageDrawable(drawable);
                star5.setImageDrawable(drawable);
                rating = 5;

            }
        });
        star4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable drawable = getActivity().getDrawable(R.drawable.baseline_star_rate_24);
                Drawable drawable2 = getActivity().getDrawable(R.drawable.baseline_star_outline_24);
                star1.setImageDrawable(drawable);
                star2.setImageDrawable(drawable);
                star3.setImageDrawable(drawable);
                star4.setImageDrawable(drawable);
                star5.setImageDrawable(drawable2);
                rating = 4;
            }
        });
        star3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable drawable = getActivity().getDrawable(R.drawable.baseline_star_rate_24);
                Drawable drawable2 = getActivity().getDrawable(R.drawable.baseline_star_outline_24);
                star1.setImageDrawable(drawable);
                star2.setImageDrawable(drawable);
                star3.setImageDrawable(drawable);
                star4.setImageDrawable(drawable2);
                star5.setImageDrawable(drawable2);
                rating = 3;
            }
        });
        star2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable drawable = getActivity().getDrawable(R.drawable.baseline_star_rate_24);
                Drawable drawable2 = getActivity().getDrawable(R.drawable.baseline_star_outline_24);
                star1.setImageDrawable(drawable);
                star2.setImageDrawable(drawable);
                star3.setImageDrawable(drawable2);
                star4.setImageDrawable(drawable2);
                star5.setImageDrawable(drawable2);
                rating = 2;
            }
        });
        star1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable drawable = getActivity().getDrawable(R.drawable.baseline_star_rate_24);
                Drawable drawable2 = getActivity().getDrawable(R.drawable.baseline_star_outline_24);
                star1.setImageDrawable(drawable);
                star2.setImageDrawable(drawable2);
                star3.setImageDrawable(drawable2);
                star4.setImageDrawable(drawable2);
                star5.setImageDrawable(drawable2);
                rating = 1;
            }
        });



        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // db.child(currentProduct.productId).setValue(rating+"");
                ////


                rateButton.setVisibility(View.INVISIBLE);
                ratedText.setVisibility(View.VISIBLE);
                ratedText.setText("You Rated " + rating + " Star!");
                /*DatabaseReference databaseReference = firebaseDatabase.getReference().child("Category")
                        .child("Products").child("tshirt").child("men").child(currentProduct.title);*/
                //HashMap<String ,Object> stars = new HashMap<>();
                Database database = new Database();
                database.updateProduct(parent, currentProduct.category.toLowerCase(), currentProduct, "1", rating, currentProduct.productId);

                SharedPreferences op = parent.getSharedPreferences("Update Request", Context.MODE_PRIVATE);
                String a = op.getString("bannersData", "null");
                DoubleListModel productTemp = gson.fromJson(a, DoubleListModel.class);





                for (int i = 0; i < productTemp.productPromoList.size(); i++) {
                    if (currentProduct.productId.equals(productTemp.productPromoList.get(i).productId)) {
                        database.updateProduct(parent, "BannerProducts", currentProduct, "1", rating, currentProduct.productId);
                        break;
                    }
                }

                /*databaseReference.updateChildren(stars).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(parent, "Rated Successfully!", Toast.LENGTH_SHORT).show();
                    }
                });*/


                //jsonTemp = JsonBody.replace(userData.name,editName.getText().toString());
                starRef.put(currentProduct.productId, rating + "");
                Log.d("starRefObject", starRef.get(currentProduct.productId));

                if (userModel.starsRef == null) {
                    userModel.starsRef = new HashMap<>();
                }
                userModel.starsRef.putAll(starRef);
                database.updateProduct(parent,currentProduct.category,currentProduct,"0",rating,currentProduct.productId);

                firebaseDatabase.getReference().child("Category").child("UsersData").child(user.getEmail().replace(".", "")).setValue(userModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        jsonTemp = gson.toJson(userModel);
                        SharedPreferences sharedPreferences = parent.getSharedPreferences("UserDetails",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor1 = sharedPreferences.edit();
                        editor1.putString("UserJson",jsonTemp);
                        editor1.apply();
                        SharedPreferences sp = parent.getSharedPreferences("profile",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor  = sp.edit();
                        editor.putInt("profileUpdateReq",1);
                        editor.apply();
                        rateDialog.dismiss();
                    }
                });

            }
        });








        firebaseDatabase.getReference().child("Category").child("products").child(currentProductTemp.category.toLowerCase()).orderByChild("productId").equalTo(currentProductTemp.productId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    currentProduct = snapshot.getValue(ProductModel.class);
                    list.add(currentProduct);

                    Log.d("SellingPPPPPP",currentProduct.sellingPrice);

                    SharedPreferences sp = parent.getSharedPreferences("BuyValues", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("ChooseSize", "null");
                    editor.putString("ChooseColor", "null");
                    editor.putInt("noOfProducts", 1);
                    editor.apply();

                    backBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            parent.onBackPressed();
                        }
                    });


                    try {
                        userContact = user.getEmail().replace(".", "");
                        Log.d("userInfo", userContact + " ");
                    } catch (Exception e) {
                        Log.d("gettingUserInfoErrorByEmail", e + " ");
                        try {
                            userContact = user.getPhoneNumber();
                            Log.d("phoneInfo", userContact);
                            userInfoValue = 1;
                        } catch (Exception e2) {
                            Log.d("gettingUserInfoError", e2 + " ");
                            //userContact = user.getEmail();
                        }
                    }

                    Log.d("phoneInfo", userContact);
                    DatabaseReference db = firebaseDatabase.getReference().child("Category").child("Users")
                            .child(userContact).child("RatedProducts");

//        db.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if(task.isSuccessful()){
//                    DataSnapshot dataSnapshot = task.getResult();
//                     ratingStarByUser = String.valueOf(dataSnapshot.child(currentProduct.productId).getValue());
                    SharedPreferences sharedPreferences = parent.getSharedPreferences("UserDetails",Context.MODE_PRIVATE);
                    String JsonBody = sharedPreferences.getString("UserJson","null");
                    Log.d("JsonBodyBuyPage",JsonBody);

                    userModel = gson.fromJson(JsonBody,UserModel.class);
                    HashMap<String,String> starsRef = userModel.starsRef;
                    if(starsRef!=null){
                        for (int i=0;i<starsRef.size();i++){
                            if(starsRef.containsKey(currentProduct.productId)){
                                containsKey = true;
                            }
                        }

                        if (containsKey) {
                            Log.d("VisiblityStart", "done");
                            rateButton.setVisibility(View.INVISIBLE);
                            ratedText.setVisibility(View.VISIBLE);
                            ratedText.setText("You Rated " + starsRef.get(currentProduct.productId) + " Star!");
                        }
                        Log.d("RatingDone", ratingStarByUser + " ");
                    }
                    // }
//                else {
//                    Log.d("RatingNotDone",ratingStarByUser+" ");
//                }
//            }
//        });



                    noOfProducts = Integer.parseInt(countItems.getText().toString());
                    increaseItems.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(noOfProducts!=50 && noOfProducts < currentProduct.stock) {
                                countItems.setText(noOfProducts + 1 + "");
                                noOfProducts = noOfProducts + 1;
                            }
                        }
                    });

                    reduceItems.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(noOfProducts!=1){
                                countItems.setText(noOfProducts-1+"");
                                noOfProducts = noOfProducts-1;
                            }
                        }
                    });


                    String sellingPriceTemp = "₹"+currentProduct.sellingPrice;
                    Log.d("stock",currentProduct.stock+"");
                    sellingPriceTextView.setText(sellingPriceTemp);
                    if(currentProduct.stock <= 0){
                        Log.d("stockInIf",currentProduct.stock+"");
                        outOfStockText.setVisibility(View.VISIBLE);
                        outOfStockDate.setVisibility(View.VISIBLE);
                        buy.setVisibility(View.INVISIBLE);
                    }





                    //Dialog Work...





                /*jsonTemp = gson.toJson(userModel);
                Log.d("userStarRef",userModel.starsRef.get(currentProduct.productId)+"");
                Log.d("starJson", jsonTemp);


                localFile= new File(parent.getFilesDir(),"template.txt");
                storage.getReference("template").child("template.txt").getFile(localFile).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {

                                    // UserModel userModel = new UserModel(user.getDisplayName(), user.getEmail(), 0.0, 0.0, new ArrayList<>());
                                    File file = new File(getActivity().getFilesDir(), user.getEmail().replace(".", ""));
                                    localFile.renameTo(file);
                                    writingJsonBody(localFile, jsonTemp, 1);

                                    //Upload the file to Azure Storage
                                    CloudStorageAccount storageAccount = null;
                                    try {
                                        storageAccount = CloudStorageAccount.parse(storageConnectionString);
                                    } catch (URISyntaxException |
                                             InvalidKeyException e) {
                                        throw new RuntimeException(e);
                                    }

                                    CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
                                    try {
                                        blobContainer = blobClient.getContainerReference("users");
                                        CloudBlockBlob blob = blobContainer.getBlockBlobReference(user.getEmail().replace(".", "") + ".txt");

                                        blob.uploadFromFile(localFile.getAbsolutePath());
                                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserDetails",Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor1 = sharedPreferences.edit();
                                        editor1.putString("UserJson",jsonTemp);
                                        editor1.apply();
                                        SharedPreferences sp = getActivity().getSharedPreferences("profile",Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor  = sp.edit();
                                        editor.putInt("profileUpdateReq",1);
                                        editor.apply();





                                    } catch (URISyntaxException |
                                             StorageException e) {
                                        throw new RuntimeException(e);
                                    }
                                } catch (Exception e) {
                                    Log.e("Exception", "An error occurred: " + e.getMessage());
                                }
                            }
                        }).start();
                    }
                });

                rateDialog.cancel();
            }*/


                    //ProgressBars...

                    int TotalStars = Integer.parseInt(currentProduct.star1) + Integer.parseInt(currentProduct.star2) +Integer.parseInt(currentProduct.star3) +
                            Integer.parseInt(currentProduct.star4) + Integer.parseInt(currentProduct.star5) ;
                    Log.d("totalStar",TotalStars+"");


                    double StarAverage =(double)  (Integer.parseInt(currentProduct.star1) + (Integer.parseInt(currentProduct.star2)*2) +(Integer.parseInt(currentProduct.star3)*3) +
                            (Integer.parseInt(currentProduct.star4)*4) + (Integer.parseInt(currentProduct.star5)*5))/TotalStars;

                    Log.d("TotalRate",StarAverage+"");

                    if(StarAverage==0 || Double.isNaN(StarAverage)){
                        starAverage.setText(("0.0"));
                    }
                    else if((StarAverage+"").length()>3){
                        starAverage.setText(((StarAverage+"").substring(0,3)));
                    }
                    else if ((StarAverage+"").length()==1){
                        starAverage.setText((StarAverage+""));
                        starAverage.setPadding(50,0,0,0);
                    }
                    else {
                        starAverage.setText((StarAverage+""));
                    }
                    progressBar1.setMax(TotalStars);
                    progressBar2.setMax(TotalStars);
                    progressBar3.setMax(TotalStars);
                    progressBar4.setMax(TotalStars);
                    progressBar5.setMax(TotalStars);
                    progressBar1.setProgress(Integer.parseInt(currentProduct.star1));
                    progressBar2.setProgress(Integer.parseInt(currentProduct.star2));
                    progressBar3.setProgress(Integer.parseInt(currentProduct.star3));
                    progressBar4.setProgress(Integer.parseInt(currentProduct.star4));
                    progressBar5.setProgress(Integer.parseInt(currentProduct.star5));

                    ArrayList<String> uris = new ArrayList<>();
                    colorsRecycler = v.findViewById(R.id.colorsRecycler);

                    rateButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            rateDialog.show();
                        }
                    });

                    if(currentProduct.description.length()>160){
                        description.setText(currentProduct.description.substring(0,160)+"...");
                    }
                    else {
                        description.setText(currentProduct.description);
                    }

                    int p = currentProduct.title.indexOf("--");

                    if(p!=-1){
                        titleView.setText(currentProduct.title.substring(0,p));
                    }
                    else{
                        titleView.setText(currentProduct.title);
                    }

                    Log.d("SellingPriceInBuyPage123",list.get(0).sellingPrice);

                    double sellPrice = Double.parseDouble(currentProduct.sellingPrice);
                    double mrp = Double.parseDouble(currentProduct.mrp);
                    Log.d("entry+", mrp+" "+sellPrice);
                    double discount = (sellPrice/mrp)*100;
                    Log.d("discount",discount+" ");
                    String offString = (100-discount+" ").substring(0,4)+"%off";
                    offText.setText(offString);

                    MoreColorsOfProduct moreColorsOfProduct = new MoreColorsOfProduct((ArrayList<String>) currentProduct.color,(ArrayList<String>)currentProduct.colorImages);
                    ArrayList<MoreColorsOfProduct> moreColorsOfProductList = new ArrayList<>();
                    moreColorsOfProductList.add(moreColorsOfProduct);
                    ColorsRecyclerAdapter colorsAdapter = new ColorsRecyclerAdapter(parent,moreColorsOfProductList,colorsRecycler,BuyPage.this);
                    String MoreDetails = "Product Type :  "+currentProduct.category+"\n"+"Brand :  "+currentProduct.brandName+"\n"+"Build Type :  "+currentProduct.fabric+"\n"
                            +"Pack Of :  "+currentProduct.packOf+"\n"+"Occasion :  "+ currentProduct.occasion +"\n"+currentProduct.description;
                    moreDetailsText.setText(MoreDetails);

                    Log.d("SellingPriceInBuyPage12345",list.get(0).sellingPrice);
//
//        uris.add("https://cdn.pixabay.com/photo/2012/08/27/14/19/mountains-55067__340.png");
//        recycler = v.findViewById(R.id.recycler);
//        uris.add("https://media.istockphoto.com/id/1297349747/photo/hot-air-balloons-flying-over-the-botan-canyon-in-turkey.jpg?s=1024x1024&w=is&k=20&c=U-1aMueiJ5vYIMY-2JTwaSLOXTnoSkAzCVLk1hE6wfE=");
//        uris.add("https://media.istockphoto.com/id/517188688/photo/mountain-landscape.jpg?s=1024x1024&w=is&k=20&c=MB1-O5fjps0hVPd97fMIiEaisPMEn4XqVvQoJFKLRrQ=");


                    // Product Images Recycler....
                    product_recycler product_recycler = new product_recycler(parent,(ArrayList<String>) currentProduct.Images);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(parent.getApplicationContext(), RecyclerView.HORIZONTAL,false);
                    recycler.setLayoutManager(linearLayoutManager);
                    LinearSnapHelper linearSnapHelper = new LinearSnapHelper();
                    linearSnapHelper.attachToRecyclerView(recycler);
                    recycler.setAdapter(product_recycler);

                    //More Colors Recycler....
                    colorsRecycler.setLayoutManager(new LinearLayoutManager(parent.getApplicationContext(), RecyclerView.HORIZONTAL,false));
                    colorsRecycler.setAdapter(colorsAdapter);

                    //More Sizes Recycler.....
                    sizeRecycler sizeAdapter = new sizeRecycler(parent,(ArrayList<String>)currentProduct.size,(ArrayList<String>)currentProduct.sizePrice,sizeRecycler,BuyPage.this);
                    sizeRecycler.setLayoutManager(new LinearLayoutManager(parent.getApplicationContext(),RecyclerView.HORIZONTAL,false));
                    sizeRecycler.setAdapter(sizeAdapter);

                    if(list.get(0).occasion.contains("grocery")){
                        buy.setText("Add Item");
                    }


                    SharedPreferences sp1 = parent.getSharedPreferences("BuyValues", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = sp1.edit();
                    String color = sp1.getString("ChooseColor","null");


                    Log.d("SellingPriceInBuyPage54321",list.get(0).sellingPrice);

                    Date date = new Date();
                    SimpleDateFormat hourFormat = new SimpleDateFormat("HH", Locale.getDefault());
                    String currentHour = hourFormat.format(date);

                    Log.d("currentHour", "Current Hour (24-hour format): " + currentHour);

                    int hour = Integer.parseInt(currentHour);

                    if(hour>19 || hour<8){
                        buy.setVisibility(View.INVISIBLE);
                        outOfStockText.setText("Shop Closed !");
                        outOfStockDate.setText("You can buy after 8am and before 7pm");
                        outOfStockText.setVisibility(View.VISIBLE);
                        outOfStockDate.setVisibility(View.VISIBLE);
                    }



                    buy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {



                            list.get(0).sellingPrice =  list.get(0).sellingPrice;



                            editor1.putInt("noOfProducts",noOfProducts);
                            editor1.apply();
                            String size = sp1.getString("ChooseSize","null");
                            String color = sp1.getString("ChooseColor","null");

                            int posOfPriceSize = 0;

                            for(int i = 0; i < currentProduct.size.size(); i++){
                                if(size.equals(currentProduct.size.get(i))){
                                    posOfPriceSize = i;
                                }
                            }


                            String sellingPriceOverall = currentProduct.sizePrice.get(posOfPriceSize);

                            list.get(0).sellingPrice = sellingPriceOverall;

                            Log.d("SellingPriceInBuyButton",currentProduct.sellingPrice);



                            if (size.equals("null")) {
                                sizeAlert.setVisibility(View.VISIBLE);
                                scrollView2.smoothScrollTo(0, sizeAlert.getTop());
                            } else {


                                if (list.get(0).occasion.toLowerCase().equals("grocery")) {

                                    Log.d("colorLog",color);
                                    SharedPreferences sharedPreferences1 = parent.getSharedPreferences("Grocery", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                                    int noOfProductsTemp = sharedPreferences1.getInt("groceryNoOfProducts",0);
                                    String a = sharedPreferences1.getString("groceryList", "null");
                                    Log.d("jsonBodyBB",a+"");


                                    if (a.equals("null")) {
                                        Random random = new Random();
                                        int randomNumber = 1000 + random.nextInt(9000);
                                        int orderId = 10000000 + random.nextInt(90000000);
                                        int totalAmount = noOfProducts*(Integer.parseInt(sellingPriceOverall));
                                        Log.d("PriceTag",totalAmount+"");
                                        OrderDetails orderDetails = new OrderDetails(orderId+"", list, size, color, "", "", noOfProducts + "", "", "", "", totalAmount+"", 1, randomNumber+"", "",userLat,userLong);
                                        ArrayList<OrderDetails> orderDetailsList = new ArrayList<>();
                                        orderDetailsList.add(orderDetails);
                                        a = gson.toJson(orderDetailsList);

                                        editor1.putString("OTP", randomNumber + "");
                                        editor1.putInt("groceryNoOfProducts",noOfProducts);
                                        editor1.putString("Order Id", orderId + "");
                                    } else {
                                        List<OrderDetails> abc = new ArrayList<>();

                                        abc = gson.fromJson(a, new TypeToken<ArrayList<OrderDetails>>() {}.getType());
                                        String Id = sharedPreferences1.getString("Order Id", "null");
                                        int totalAmount = noOfProducts*(Integer.parseInt(sellingPriceOverall));
                                        Log.d("PriceTag",totalAmount+"");
                                        editor1.putInt("groceryNoOfProducts",noOfProducts+noOfProductsTemp);
                                        OrderDetails orderDetails = new OrderDetails(Id, list, size, color, "", "", noOfProducts + "", "", "", "", totalAmount+"", 1, sharedPreferences1.getString("OTP", "null"), "",userLat,userLong);
                                        abc.add(orderDetails);
                                        a = gson.toJson(abc);
                                    }
                                    Toast.makeText(parent, "Item Added Successfully!", Toast.LENGTH_SHORT).show();
                                    editor1.putString("groceryList", a);
                                    editor1.apply();
                                } else {
                                    Bundle bundle = new Bundle();
                                    bundle.putParcelableArrayList("list", list);
                                    Log.d("SellingPriceInBuyPage",sellingPriceOverall);
                                    bundle.putInt("buttonFlag", 1);
                                    if(parent instanceof search_instance){
                                        ((search_instance) parent).runFragment(bundle, new savedAddresses(), 2);
                                    } else   {
                                        ((MainActivity) parent).runFragment(bundle, new savedAddresses(), "");
                                    }


                                }

                            }
                        }
                    });


        /*String Url = "https://tykun.blob.core.windows.net/users/"+user.getEmail().replace(".","")+".txt";
        String storageConnectionString = "DefaultEndpointsProtocol=https;AccountName=tykun;AccountKey=0DPLRf2Sm3gEkXRcPLQYPT1cPjnpSuHTZY0QpQjtXPRieRAg/+" +
                "vaCycyI3otZj3tu/hPmwKp6Le5+AStW89EtQ==;EndpointSuffix=core.windows.net";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("OkHttp", "Failure to get");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String JsonBody = response.body().string();
                Log.d("JsonBody", JsonBody);
                if (!JsonBody.contains("BlobNotFound")) {
                    Gson gson = new Gson();
                    userData= new UserModel();
                    userData = gson.fromJson(JsonBody, UserModel.class);

                    starRef = userData.starsRef;
                    for(int i=0;i<starRef.size();i++){
                        if(starRef.containsKey(currentProduct.productId)){
                            String ab = "You Rated "+(starRef.get(currentProduct.productId))+"!";
                            ratedText.setText(ab);
                            break;
                        }
                    }
                    else{

                    }



                }
            }});*/








                }



















            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });








//        if(parent.getClass()==MainActivity.class){
//            rateButton.setVisibility(View.INVISIBLE);
//            ratedText.setVisibility(View.VISIBLE);
//            ratedText.setText("You Rated 0 Star!");
//        }





        return v;
    }





}