package com.just2fast.ushop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.PaymentResultListener;

import java.util.ArrayList;

//import com.razorpay.Checkout;


public class search_instance extends AppCompatActivity implements PaymentResultListener {

    int count = 0;
    FragmentManager fragmentManager;
    String query;
    Activity activity;
    Fragment currentFrag;
    FirebaseDatabase firebaseDatabase;


    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_instance);
        Log.d("Entering in SearchInstance","done");
        firebaseDatabase = FirebaseDatabase.getInstance();
        fragmentManager = getSupportFragmentManager();
        activity = this;
        Intent intent = getIntent();
        query= intent.getStringExtra("query");
        runFragment(new Bundle(), new search_page(),1);

        if(fragmentManager.findFragmentById(R.id.frameLayout)==fragmentManager.findFragmentByTag("search")){
            count = 1;
        }
        /*ft.add(R.id.frameLayout,new search_page());

        ft.commit();*/


    }
    public void runFragment(Bundle bundle, Fragment fragment,int flag){
        currentFrag = fragment;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
        count  = flag;
        SharedPreferences sp = getSharedPreferences("DirectQuery",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        int a = sp.getInt("directQueryRun2",0);
        if(a!=0){
            bundle.putString("query",query);
            editor.putInt("directQueryRun",1);
            editor.putInt("directQueryRun2",0);
            editor.apply();
        }

        if(flag==1){

            ft.add(R.id.frameLayout,fragment,"search");
            getSupportFragmentManager().popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
            ft.addToBackStack(null);
            fragment.setArguments(bundle);
            ft.commit();


        }
        else if(flag==2){

            ft.replace(R.id.frameLayout,fragment,"saved");
            ft.addToBackStack("savedAddresses");
            fragment.setArguments(bundle);
            ft.commit();

        } else if (flag==3) {
            ft.replace(R.id.frameLayout,fragment);
            ft.addToBackStack(null);
            fragment.setArguments(bundle);
            ft.commit();


        } else if (flag==4) {
            ft.replace(R.id.frameLayout,fragment,"address");
            ft.addToBackStack("buyingDetails");
            fragment.setArguments(bundle);
            ft.commit();

        } else if (flag == 5) {
            ft.replace(R.id.frameLayout,fragment,"paymentMethods");
            ft.addToBackStack("paymentMethods");
            fragment.setArguments(bundle);
            ft.commit();
        } else if (flag == 6) {
            ft.replace(R.id.frameLayout,fragment,"confirm order");
            ft.addToBackStack("confirmOrder");
            fragment.setArguments(bundle);
            ft.commit();
        } else{

            ft.replace(R.id.frameLayout,fragment);
            ft.addToBackStack(null);
            fragment.setArguments(bundle);
            ft.commit();

        }

    }

    @Override
    public void onBackPressed() {
        if(fragmentManager.findFragmentById(R.id.frameLayout)==fragmentManager.findFragmentByTag("search")){
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
        else if(fragmentManager.findFragmentById(R.id.frameLayout)==fragmentManager.findFragmentByTag("confirm order")) {
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
        else{
            super.onBackPressed();
        }
    }


    @Override
    public void onPaymentSuccess(String s) {

        Fragment fragment = (payment_method) getSupportFragmentManager().findFragmentByTag("paymentMethods");

        if(fragment!=null){
            ((payment_method)fragment).updateData();
            ((payment_method)fragment).switchFragment();
        }
        Log.d("Payment",s);

    }

    @Override
    public void onPaymentError(int i, String s) {
        Log.d("Payment",s);
        Toast.makeText(this,"Payment Failed by User", Toast.LENGTH_SHORT).show();
    }
}