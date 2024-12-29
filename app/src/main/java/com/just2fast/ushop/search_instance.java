package com.just2fast.ushop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

//import com.razorpay.Checkout;


public class search_instance extends AppCompatActivity {

    int count = 0;
    FragmentManager fragmentManager;
    String query;
    ProgressBar progressBar;


    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_instance);
        Log.d("Entering in SearchInstance","done");
        fragmentManager = getSupportFragmentManager();
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
            ft.replace(R.id.frameLayout,fragment,"address");
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
}