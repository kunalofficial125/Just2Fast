package com.just2fast.ushop;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import android.Manifest;
import android.content.Context;
import android.content.Intent;

import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import android.view.MenuItem;

import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_NOTIFICATION_PERMISSION = 1;
    BottomNavigationView bnv;

    Context context;
    int backCount=0;
    private static final int REQUEST_CHECK_SETTINGS = 200;

    int FRAGMENT_REQ_CODE;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 300;
    FirebaseUser firebaseUser;

    //FirebaseAuth auth;
    FirebaseFirestore firebaseFirestore;
    FrameLayout frameLayout;
    CardView groceryCard;
    FirebaseDatabase firebaseDatabase ;
    Button continueBtn;
    Charges charges =  new Charges();
    public int checkBack=0;

    TextView noOfProductsText;
    FirebaseStorage storage;
    String UserContact;
    boolean locationCheck;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        noOfProductsText = findViewById(R.id.noOfProductsText);
        FirebaseApp.initializeApp(this);
        groceryCard = findViewById(R.id.groceryCard);
        firebaseDatabase = FirebaseDatabase.getInstance();
        continueBtn = findViewById(R.id.continueBtn);

        bnv = findViewById(R.id.bnv);
        SharedPreferences op = getSharedPreferences("Update Request",MODE_PRIVATE);
        SharedPreferences.Editor ed = op.edit();
        ed.putInt("Update Req",1);
        ed.putInt("OrdersUpdate",1);
        ed.apply();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_NOTIFICATION_PERMISSION);
            } else {
                // Permission already granted, proceed with your logic
            }
        }

        SharedPreferences sp23 = getSharedPreferences("loginCheck",MODE_PRIVATE);
        locationCheck = sp23.getBoolean("locationCheck",false);
        String locationText = sp23.getString("Location","null");
        Log.d("locationCheckMain",locationText+" ");


        if(locationText.equals("null")){
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_REQUEST_CODE);

            }
            else {
                if(!isGPSEnabled()){
                    checkLocationSettings();
                }
            }
        }

        groceryCard.setVisibility(View.VISIBLE);

        context = getApplicationContext();
        frameLayout = findViewById(R.id.frameLayout);
        Intent intent = getIntent();
        int NavFlag = intent.getIntExtra("NavFlag",0);
        SharedPreferences preferences = getSharedPreferences("Grocery",MODE_PRIVATE);
        int noOfGroceryProducts = preferences.getInt("groceryNoOfProducts",0);
        Log.d("noOfGroceryProducts",noOfGroceryProducts+"");
        SharedPreferences.Editor ee = preferences.edit();
        ee.putInt("ss",0);
        ee.apply();



        if(noOfGroceryProducts==0){
            groceryCard.setVisibility(View.INVISIBLE);
        }
        else {
            groceryCard.setVisibility(View.VISIBLE);
            noOfProductsText.setText(noOfGroceryProducts+" Items Added");
        }
        Log.d("NavBarFlag",NavFlag+"");


        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runFragment(new Bundle(),new GroceryList(),"grocery");
            }
        };

        noOfProductsText.setOnClickListener(onClickListener);

        continueBtn.setOnClickListener(onClickListener);

        FRAGMENT_REQ_CODE = intent.getIntExtra("FRAGMENT_REQ_CODE",0);
        //auth = FirebaseAuth.getInstance();


        firebaseFirestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        firebaseUser  = FirebaseAuth.getInstance().getCurrentUser();



        SharedPreferences sp1 = getSharedPreferences("SearchHistory",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sp1.edit();

        Timer timer = new Timer();
       // scheduleNextTuesdayTask(timer);

        String jsonHis = sp1.getString("JsonHistory","null");
        if(jsonHis.equals("null")){
            ArrayList<String> uploadHistory = new ArrayList<>();
            Gson gson = new Gson();
            String jsonHistory = gson.toJson(uploadHistory);
            editor1.putString("JsonHistory",jsonHistory);
            editor1.apply();
        }


        SharedPreferences sp = getSharedPreferences("loginCheck",MODE_PRIVATE);
        int checkLogin = sp.getInt("loginType",0);


        if(firebaseUser== null){
            startActivity(new Intent(this,Login_page.class));
            finish();
        }

//        if(checkLogin==0){
//            UserContact = firebaseUser.getPhoneNumber();
//        }
//        else{
//            UserContact = firebaseUser.getEmail();
//        }

        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Category").child("SellerData").child("codewithmax7@gmailcom");

        /*databaseReference.child("OrderAlert").setValue(1).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                firebaseDatabase.getReference().child("Category").child("SellerData").child("codewithmax7@gmailcom").child("Orders").child("Order126").setValue("catholic").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("TAG", "onSuccess: ");


                    }
                });
            }
        });*/



        SharedPreferences opp = getSharedPreferences("Update Request",Context.MODE_PRIVATE);
        int flag = opp.getInt("Charges Update Req",0);
        if(flag==1){
            SundayTask sundayTask = new SundayTask();
            sundayTask.run();
        }

        firebaseDatabase.getReference().child("Category").child("Charge").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Charges charges = snapshot.getValue(Charges.class);
                Log.d("Charges",charges.SERVICE_CHARGE + " " + charges.SHIPPING_CHARGE);
                SharedPreferences sharedPreferences =getSharedPreferences("Charges",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Log.d("TotalCharges",charges.SERVICE_CHARGE+charges.SHIPPING_CHARGE+"");
                editor.putInt("SERVICE",charges.SERVICE_CHARGE);
                editor.putInt("SHIPPING",charges.SHIPPING_CHARGE);
                editor.apply();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        /*List<String> images = new ArrayList<>();
        images.add("https://th.bing.com/th/id/OIP.Ub6YpRIAymTwuyjRcSD92QHaIL?pid=ImgDet&rs=1");
        images.add("https://th.bing.com/th/id/OIP.yRSLuEAM-xc3Lhvi6DSAbwHaHa?pid=ImgDet&rs=1");
        images.add("https://5.imimg.com/data5/SELLER/Default/2021/4/GV/JJ/DX/13100216/mens-black-plain-t-shirt.jpg");
        List<String> colors = new ArrayList<>();
        colors.add("yellow");
        colors.add("blue");
        List<String> size = new ArrayList<>();
        size.add("XL");
        List<String> colorImages = new ArrayList<>();
        colorImages.add("https://purepng.com/public/uploads/large/purepng.com-blue-t-shirtclothingt-shirtt-shirtdressfashionclothshirt-691522330467vjhb0.png");
        colorImages.add("https://th.bing.com/th/id/OIP.bD3dT2CazF4__z4TJVz8FQHaHR?pid=ImgDet&rs=1");
        size.add("L");
        clothes_model tshirt = new clothes_model("Fancy Tshirt for boy - Tshirt",colors,colorImages,images,"tshirt",size,"nylon","boy","fancy","599","399","sento","2",
                "This Fancy t-shirt is the perfect blend of comfort and style. Made from high-quality cotton, it's soft and breathable, and perfect for everyday wear. The shirt features a unique graphic design on the front, showcasing a bold and eye-catching print that's sure to turn heads.","product3421","0","0","0","0","0");
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.getReference().child("Category").child("Products").child("tshirt").child("men").child("Fancy Tshirt for boy - Tshirt").setValue(tshirt).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(MainActivity.this, "Data Added", Toast.LENGTH_SHORT).show();
                Log.d("data_added","done");
            }
        });*/


        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                Log.d("token",s);
            }
        });

        FirebaseInstallations.getInstance().getId()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String installationId = task.getResult();
                        // Use the installationId as needed
                        Log.d("Installation ID", installationId);
                    } else {
                        Exception exception = task.getException();
                        // Handle the error
                        Log.e("Installation ID Error", "Failed to retrieve installation ID", exception);
                    }
                });


        CheckNetwork net = new CheckNetwork();
        if(net.isNetworkAvailable(this)){
            Log.d("net","Internet Available");
        }
        else{
            net.isNetworkNotAvailableResult(this);
        }
        if(getSupportActionBar()!=null){
            getSupportActionBar().setTitle("Home");
            Log.d("actionBar",getSupportActionBar()+" ");
        }


        if(locationCheck){
            loadFrag(new home(),1);
        }
        else{
            loadFrag(new SelectLocation(),1);
        }

        bnv.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if(itemId==R.id.home){


                    loadFrag(new home(),0);


                } else if (itemId==R.id.orders) {

                    loadFrag(new orders(),0);

                } else if (itemId==R.id.cart) {


                    loadFrag(new cart(),0);

                }
                else {

                    loadFrag(new profile(),0);

                }
                return true;
            }
        });
        if(FRAGMENT_REQ_CODE==1000){
            loadFrag(new savedAddresses(),0);
        }

        if(NavFlag==1){
            bnv.setSelectedItemId(R.id.orders);
            loadFrag(new orders(),0);
        }

    }

    private void loadFrag(Fragment fragment,int flag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);

        if(flag==1){
            ft.add(R.id.frameLayout,fragment);

            frameLayout.setPadding(0,0,0,0);
        }  else{
            Bundle bundle = new Bundle();
            //bundle.putString("UserContact",UserContact);
            fragment.setArguments(bundle);
            ft.replace(R.id.frameLayout,fragment);
        }
        //fragment.setArguments(bundle);
        ft.commit();
    }

    public void runFragment(Bundle bundle,Fragment fragment, String Title){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in, R.anim.slide_out, R.anim.slide_in, R.anim.slide_out);

        if(Title.equals("Buy Page")){
            ft.replace(R.id.frameLayout,fragment,"Buy Page");
            fragment.setArguments(bundle);
            ft.addToBackStack("Buy Page");
            ft.commit();
        } else if (Title.equals("Saved Addresses")) {
            ft.replace(R.id.frameLayout,fragment,"Saved Addresses");
            fragment.setArguments(bundle);
            ft.addToBackStack(null);
            ft.commit();

            frameLayout.setPadding(0,0,0,0);
        } else if (Title.equals("Your Orders" )) {
            ft.replace(R.id.frameLayout,fragment,"Your Orders");
            fragment.setArguments(bundle);
            ft.addToBackStack("Your Orders");
            ft.commit();
            frameLayout.setPadding(0,0,0,0);
        } else if (Title.equals("Select Location")) {
            ft.replace(R.id.frameLayout,fragment,"Your Orders");
            fragment.setArguments(bundle);
            ft.commit();
        } else if (Title.equals("Confirm Order Details")) {
            ft.replace(R.id.frameLayout,fragment,"Saved Addresses");
            fragment.setArguments(bundle);
            ft.addToBackStack("Confirm Order Details");
            ft.commit();
        } else {
            ft.replace(R.id.frameLayout,fragment);
            fragment.setArguments(bundle);
            ft.addToBackStack(null);
            ft.commit();

        }
    }

    private  void scheduleNextTuesdayTask(Timer timer) {
        // Get the current date
        Calendar now = Calendar.getInstance();

        // Find the next Tuesday
        while (now.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            now.add(Calendar.DAY_OF_WEEK, 1);
        }

        // Set the time to 10 AM
        now.set(Calendar.HOUR_OF_DAY, 15);
        now.set(Calendar.MINUTE, 55);
        now.set(Calendar.SECOND, 0);

        // Schedule the task to run once at 10 AM on the next Tuesday
        timer.schedule(new SundayTask(), now.getTime());
    }

    private class SundayTask extends TimerTask {
        @Override
        public void run() {

        }
    }

    @Override
    public void onBackPressed() {

        if(checkBack==123){
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }

        if(getSupportFragmentManager().findFragmentById(R.id.frameLayout)==getSupportFragmentManager().findFragmentByTag("Confirm Order Details")){
            startActivity(new Intent(this,MainActivity.class));
        }
        else {
            super.onBackPressed();
        }
        
    }

    public void enableGroceryView(){
        SharedPreferences preferences = getSharedPreferences("Grocery",MODE_PRIVATE);
        int noOfGroceryProducts = preferences.getInt("groceryNoOfProducts",0);
        Log.d("noOfGroceryProducts",noOfGroceryProducts+"");

        if(noOfGroceryProducts==0){
            groceryCard.setVisibility(View.INVISIBLE);
        }
        else {
            groceryCard.setVisibility(View.VISIBLE);
            noOfProductsText.setText(noOfGroceryProducts+" Items Added");
        }
    }


    public boolean isGPSEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }




    public void checkLocationSettings() {
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10000)
                .setFastestInterval(5000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .setAlwaysShow(true);  // This will display the dialog even if the location is already enabled

        Task<LocationSettingsResponse> task = LocationServices.getSettingsClient(this)
                .checkLocationSettings(builder.build());

        task.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    // All location settings are satisfied. The client can initialize location requests here.
                    // ...
                } catch (ApiException exception) {
                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied, but this can be fixed by showing the user a dialog.
                            try {
                                // Show the dialog by calling startResolutionForResult(), and check the result in onActivityResult().
                                ResolvableApiException resolvable = (ResolvableApiException) exception;
                                resolvable.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied, and we have no way to fix the settings so we won't show the dialog.
                            break;
                    }
                }
            }
        });
    }
    
    

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {
                // The user agreed to make required location settings changes
                // You can proceed with location request
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if(check){
//                            Intent toHome = new Intent(MainActivity.this,MainActivity.class);
//                            startActivity(toHome);
//                            finish();
//                        }
//                        else {
//                            Intent toLogin = new Intent(MainActivity.this,Login_page.class);
//                            startActivity(toLogin);
//                            finish();
//                        }
//
//                    }
//                },2000);





            } else {
                // The user did not agree to make required location settings changes
                // Handle the case accordingly
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted

                requestLocationUpdates();


                // ... (Optional: Show progress indicator while waiting for location)
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show();
            }
        }

    }

    private LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationClient;
    private void requestLocationUpdates() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10000)
                .setFastestInterval(5000);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult != null) {
                    Location location = locationResult.getLastLocation();
                    if (location != null) {
                        fusedLocationClient.removeLocationUpdates(locationCallback);  // Stop updates after getting a fix
                    }
                }
            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }





}