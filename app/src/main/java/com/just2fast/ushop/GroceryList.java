package com.just2fast.ushop;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GroceryList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroceryList extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GroceryList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GroceryList.
     */
    // TODO: Rename and change types and number of parameters
    public static GroceryList newInstance(String param1, String param2) {
        GroceryList fragment = new GroceryList();
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


    RecyclerView rv;
    Gson gson = new Gson();

    Activity parent;
    GroceryListAdapter groceryListAdapter;
    ImageView backBtn;
    ArrayList<OrderDetails> orderDetails = new ArrayList<>();
    CardView cd;
    TextView extraCharges,price;

    Button next,ctn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_grocery_list, container, false);
        rv = v.findViewById(R.id.rv);
        backBtn = v.findViewById(R.id.backBtn);

        next = v.findViewById(R.id.next);
        parent = getActivity();



        rv.setLayoutManager(new LinearLayoutManager(parent));

        ((MainActivity)parent).groceryCard.setVisibility(View.INVISIBLE);

        SharedPreferences sharedPreferences1 = parent.getSharedPreferences("Grocery", Context.MODE_PRIVATE);
        String a = sharedPreferences1.getString("groceryList", "null");

        orderDetails = gson.fromJson(a,new TypeToken<ArrayList<OrderDetails>>() {}.getType());
         groceryListAdapter = new GroceryListAdapter(parent,orderDetails,sharedPreferences1.edit(),sharedPreferences1,getParentFragment());
        rv.setAdapter(groceryListAdapter);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)parent).onBackPressed();
            }
        });




        SharedPreferences sharedPreferences = parent.getSharedPreferences("Charges",Context.MODE_PRIVATE);

        int serviceCharge = sharedPreferences.getInt("SERVICE",0);
        int shippingCharge = sharedPreferences.getInt("SHIPPING",10);
        Log.d("TotalChargesList",shippingCharge+serviceCharge+"");

        Dialog dialog = new Dialog(parent);
        dialog.setContentView(R.layout.total_dialog);
        extraCharges = dialog.findViewById(R.id.extraCharges);
        price = dialog.findViewById(R.id.price);
        ctn = dialog.findViewById(R.id.ctn);
        ImageView dialog_backBtn  = dialog.findViewById(R.id.dialog_backBtn);
        dialog_backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {





                String a = sharedPreferences1.getString("groceryList", "null");
                orderDetails = gson.fromJson(a,new TypeToken<ArrayList<OrderDetails>>() {}.getType());
                int totalAmount = 0;
                for (int i=0;i<orderDetails.size();i++){


                    int posOfPriceSize = 0;

                    for(int k = 0; k < orderDetails.get(i).product.get(0).size.size(); k++){
                        if(orderDetails.get(i).size.equals(orderDetails.get(i).product.get(0).size.get(k))){
                            posOfPriceSize = k;
                        }
                    }


                    String sellingPriceOverall = orderDetails.get(i).product.get(0).sizePrice.get(posOfPriceSize);

                    totalAmount = totalAmount + (Integer.parseInt(orderDetails.get(i).quantity)*Integer.parseInt(sellingPriceOverall));
                    orderDetails.get(i).totalAmount = totalAmount+"";
                }

                totalAmount = totalAmount+serviceCharge+shippingCharge;

                price.setText(totalAmount+"");
                extraCharges.setText((serviceCharge+shippingCharge)+"+ Service And Handling");

                dialog.show();
            }
        });

        ctn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("jsonTemp",orderDetails);
                Gson gson1 = new Gson();
                String a = gson1.toJson(orderDetails);
                Log.d("a",a+"");
                bundle.putInt("NavFlag",1);
                ((MainActivity)parent).runFragment(bundle,new savedAddresses(),"Saved Addresses");
                dialog.dismiss();
            }
        });


        return v;
    }




    public void updateData(ArrayList<OrderDetails> newData) {
        orderDetails.clear();
        orderDetails.addAll(newData);
        Log.d("data",orderDetails.get(0).ID+"");

        // Notify the RecyclerView adapter of the data change
        if (groceryListAdapter != null) {
            groceryListAdapter.notifyDataSetChanged();
        }
    }
}