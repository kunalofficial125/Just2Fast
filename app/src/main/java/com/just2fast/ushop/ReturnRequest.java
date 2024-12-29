package com.just2fast.ushop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReturnRequest#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReturnRequest extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ReturnRequest() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReturnRequest.
     */
    // TODO: Rename and change types and number of parameters
    public static ReturnRequest newInstance(String param1, String param2) {
        ReturnRequest fragment = new ReturnRequest();
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

    RadioGroup radioGroup ;
    String SelectedIssue = "null";
    EditText moreProblem;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser user;
    Button continueBtn,continueBtn2;
    ConstraintLayout constraintLayout;
    ImageView tickImage;
    TextView tickMessage;
    Activity parent;
    OrderDetails currentOrder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_return_request, container, false);
        radioGroup = v.findViewById(R.id.radioGroup);
        moreProblem = v.findViewById(R.id.moreProblem);
        constraintLayout = v.findViewById(R.id.constraintLayout);
        continueBtn2 = v.findViewById(R.id.continueBtn2);
        tickImage = v.findViewById(R.id.tickImage);
        tickMessage = v.findViewById(R.id.tickMessage);
        continueBtn = v.findViewById(R.id.continueBtn);
        parent = getActivity();
        firebaseDatabase = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        ArrayList<OrderDetails> list = getArguments().getParcelableArrayList("list");
        if(list!=null){
            currentOrder = list.get(0);
        }



        if(currentOrder.ORDER_STATUS==5){

            constraintLayout.setVisibility(View.GONE);
            tickImage.setVisibility(View.VISIBLE);
            tickMessage.setVisibility(View.VISIBLE);
        }





        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                RadioButton currentRadioButton = radioGroup.findViewById(i);

                if (currentRadioButton != null) {
                    SelectedIssue = currentRadioButton.getText().toString();
                }

            }
        });



        continueBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(parent,MainActivity.class);
                intent.putExtra("NavFlag",1);
                startActivity(intent);
                parent.finish();
            }
        });


        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String moreProblemText = moreProblem.getText().toString();

                if(moreProblemText.isEmpty()){
                    Toast.makeText(parent, "Please Enter More About Your Problem", Toast.LENGTH_SHORT).show();
                }
                else if(SelectedIssue.equals("null")){
                    Toast.makeText(parent, "Please Select Your Issue", Toast.LENGTH_SHORT).show();
                }
                else{

                    continueBtn.setVisibility(View.INVISIBLE);

                    //Updating User
                    firebaseDatabase.getReference().child("Category").child("UsersData").
                            child(user.getEmail().replace(".","")).child("myOrders").orderByChild("ID").equalTo(currentOrder.ID).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    firebaseDatabase.getReference().child("Category").child("SellerData").child(currentOrder.product.get(0).sellerId).child("OrderAlert").setValue(3);

                                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                                        dataSnapshot.getRef().child("ORDER_STATUS").setValue(5).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                dataSnapshot.getRef().child("product").child("0").child("productVerified").setValue(SelectedIssue+": "+moreProblemText).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {







                                                        //Updating Seller
                                                        firebaseDatabase.getReference().child("Category").child("SellerData").
                                                                child(currentOrder.product.get(0).sellerId).child("Orders").orderByChild("ID").equalTo(currentOrder.ID).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                        for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                                                                            dataSnapshot.getRef().child("ORDER_STATUS").setValue(5).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void unused) {
                                                                                    dataSnapshot.getRef().child("product").child("0").child("productVerified").setValue(SelectedIssue+": "+moreProblemText).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                        @Override
                                                                                        public void onSuccess(Void unused) {

                                                                                            tickImage.setVisibility(View.VISIBLE);
                                                                                            tickMessage.setVisibility(View.VISIBLE);
                                                                                            continueBtn2.setVisibility(View.VISIBLE);
                                                                                            constraintLayout.setVisibility(View.INVISIBLE);
                                                                                            SharedPreferences op = parent.getSharedPreferences("Update Request", Context.MODE_PRIVATE);
                                                                                            SharedPreferences.Editor ed = op.edit();
                                                                                            ed.putInt("OrdersUpdate",1);
                                                                                            ed.apply();


                                                                                            //Update Delivery Partner

                                                                                            firebaseDatabase.getReference().child("Category").child("DeliveryPartnersList").child("0").child("orders").orderByChild("ID").equalTo(currentOrder.ID).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                @Override
                                                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                                                                    for(DataSnapshot dataSnapshot1: snapshot.getChildren()){

                                                                                                        dataSnapshot1.getRef().child("ORDER_STATUS").setValue(5).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                            @Override
                                                                                                            public void onSuccess(Void unused) {

                                                                                                                firebaseDatabase.getReference().child("Category").child("DeliveryPartnersList").child("0").child("OrderAlert").setValue(3).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                    @Override
                                                                                                                    public void onSuccess(Void unused) {
                                                                                                                        Log.d("DeliveryPartnerAlert","Order Returned");
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
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                }


            }
        });




        return v;
    }
}