package com.just2fast.ushop;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;


import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;


public class Splash_Screen extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 300;
    FirebaseFirestore firebaseFirestore;
    FirebaseDatabase database;
    Boolean check;
    private static final int REQUEST_CHECK_SETTINGS = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        firebaseFirestore = FirebaseFirestore.getInstance();
        SharedPreferences checker = getSharedPreferences("loginCheck", MODE_PRIVATE);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        check = checker.getBoolean("checkLogin", false);
        database = FirebaseDatabase.getInstance();
        try {
            database.setPersistenceEnabled(true);
        }
        catch (Exception e){
            Log.d("Persistence",e+"");
        }

        //String userPh = checker.getString("UserPhone","null");

        builder.setTitle("GPS Required");
        builder.setMessage("Location services are disabled. Enable GPS to use this feature.");

        builder.setPositiveButton("Enable GPS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (check) {
                            Intent toHome = new Intent(Splash_Screen.this, MainActivity.class);
                            startActivity(toHome);
                            finish();
                        } else {
                            Intent toLogin = new Intent(Splash_Screen.this, Login_page.class);
                            startActivity(toLogin);
                            finish();
                        }

                    }
                }, 6000);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (check) {
                            Intent toHome = new Intent(Splash_Screen.this, MainActivity.class);
                            startActivity(toHome);
                            finish();
                        } else {
                            Intent toLogin = new Intent(Splash_Screen.this, Login_page.class);
                            startActivity(toLogin);
                            finish();
                        }

                    }
                }, 2000);
            }
        });

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (check) {
                        Intent toHome = new Intent(Splash_Screen.this, MainActivity.class);
                        startActivity(toHome);
                        finish();
                    } else {
                        Intent toLogin = new Intent(Splash_Screen.this, Login_page.class);
                        startActivity(toLogin);
                        finish();
                    }

                }
            }, 3000);

        } else {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (check) {
                        Intent toHome = new Intent(Splash_Screen.this, MainActivity.class);
                        startActivity(toHome);
                        finish();
                    } else {
                        Intent toLogin = new Intent(Splash_Screen.this, Login_page.class);
                        startActivity(toLogin);
                        finish();
                    }

                }
            }, 2000);

        }


//        if(isGPSEnabled()){
//
//        }
//        else {
//            checkLocationSettings();
//        }


    }



}