package com.just2fast.ushop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link cart#newInstance} factory method to
 * create an instance of this fragment.
 */
public class cart extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public cart() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment cart.
     */
    // TODO: Rename and change types and number of parameters
    public static cart newInstance(String param1, String param2) {
        cart fragment = new cart();
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


    ImageView flourCatImg,riceCatImg,pulsesCatImg,oilCatImg,drinksCatImg,teaCatImg,creamCatImg,hairOilCatImg;
    ImageView tshirtCat,shoeCat,shirtCat,jeanCat,jacketCat,pantCat;
    ImageView topCat,kurtaCat,sareeCat,jumpsuitCat,searchMoreImg;
    Activity parent;
    CardView searchMoreCard;
    TextView searchMoreText;
    SharedPreferences sp;
    SharedPreferences.Editor editor;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_cart, container, false);
        parent  = getActivity();

        flourCatImg = v.findViewById(R.id.flourCat);
        riceCatImg = v.findViewById(R.id.riceCat);
        pulsesCatImg = v.findViewById(R.id.pulseCat);
        oilCatImg = v.findViewById(R.id.cookingOilCat);
        drinksCatImg = v.findViewById(R.id.drinkCat);
        teaCatImg = v.findViewById(R.id.teaCat);
        searchMoreImg = v.findViewById(R.id.searcMoreImg);
        searchMoreText = v.findViewById(R.id.searchMoreText);
        searchMoreCard = v.findViewById(R.id.searchMoreCard);
        creamCatImg = v.findViewById(R.id.faceCreamCat);
        hairOilCatImg = v.findViewById(R.id.hairOilCat);
        tshirtCat = v.findViewById(R.id.tshirtCat);
        shoeCat = v.findViewById(R.id.shoeCat);
        shirtCat = v.findViewById(R.id.shirtCat);
        jeanCat = v.findViewById(R.id.jeanCat);
        jacketCat = v.findViewById(R.id.jacketCat);
        pantCat = v.findViewById(R.id.pantCat);
        topCat = v.findViewById(R.id.topCat);
        kurtaCat = v.findViewById(R.id.kurtaCat);
        sareeCat = v.findViewById(R.id.sareeCat);
        jumpsuitCat = v.findViewById(R.id.jumpsuitCat);

        sp = parent.getSharedPreferences("DirectQuery", Context.MODE_PRIVATE);
        editor = sp.edit();

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = ((ImageView)view).getContentDescription().toString();
                Intent intent = new Intent(parent,search_instance.class);
                intent.putExtra("query",query);
                editor.putInt("directQueryRun2",1);
                editor.apply();
                startActivity(intent);
            }
        };

        tshirtCat.setOnClickListener(onClickListener);
        shoeCat.setOnClickListener(onClickListener);
        shirtCat.setOnClickListener(onClickListener);
        jeanCat.setOnClickListener(onClickListener);
        jacketCat.setOnClickListener(onClickListener);
        pantCat.setOnClickListener(onClickListener);
        topCat.setOnClickListener(onClickListener);
        kurtaCat.setOnClickListener(onClickListener);
        sareeCat.setOnClickListener(onClickListener);
        jumpsuitCat.setOnClickListener(onClickListener);
        flourCatImg.setOnClickListener(onClickListener);
        riceCatImg.setOnClickListener(onClickListener);
        pulsesCatImg.setOnClickListener(onClickListener);
        oilCatImg.setOnClickListener(onClickListener);
        drinksCatImg.setOnClickListener(onClickListener);
        teaCatImg.setOnClickListener(onClickListener);
        creamCatImg.setOnClickListener(onClickListener);
        hairOilCatImg.setOnClickListener(onClickListener);


        View.OnClickListener onClickListener2 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(parent,search_instance.class));
                parent.finish();
            }
        };

        searchMoreCard.setOnClickListener(onClickListener2);
        searchMoreImg.setOnClickListener(onClickListener2);
        searchMoreText.setOnClickListener(onClickListener2);



        return v;
    }



}