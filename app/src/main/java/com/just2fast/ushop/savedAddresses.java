package com.just2fast.ushop;





import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.widget.ProgressBar;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;


import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link savedAddresses#newInstance} factory method to
 * create an instance of this fragment.
 */
public class savedAddresses extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public savedAddresses() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment savedAddresses.
     */
    // TODO: Rename and change types and number of parameters
    public static savedAddresses newInstance(String param1, String param2) {
        savedAddresses fragment = new savedAddresses();
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

    TextView addNewAddBtn;
    //String ph,city,postal,area,state,nominee,house,road;
    //boolean STATE_CHECK;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser user;
    String userContact;
    int accountValue = 0;
    FirebaseDatabase firebaseDatabase;
    Context parent;
    ArrayList<Address_Model> list  = new ArrayList<>();
    ArrayList<ProductModel> buyingPrd = new ArrayList<>();
    ProgressBar progressBar;
    CardView cardView2;
    int flag;
    TextView alertAddress;

    ArrayList<OrderDetails> orderDetails = new ArrayList<>();

    int NavFlag;
    Activity parentActivity;
    //int ADAPTER_REQ_CODE=0;
    RecyclerView addressesRecycler;


    //EditText newPh,newCity,newPostal,newArea,newState,newNominee,newHouse,newRoad;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_saved_addresses, container, false);
        addNewAddBtn = v.findViewById(R.id.addNewAddBtn);
        cardView2 = v.findViewById(R.id.cardView2);
        user = FirebaseAuth.getInstance().getCurrentUser();
        parentActivity = getActivity();
        parent = getActivity().getApplicationContext();
        progressBar = v.findViewById(R.id.progressBar);
        firebaseDatabase = FirebaseDatabase.getInstance();
        flag = getArguments().getInt("buttonFlag",0);
        alertAddress = v.findViewById(R.id.alertAddress);
        buyingPrd = getArguments().getParcelableArrayList("list");
        NavFlag = getArguments().getInt("NavFlag",0);
        Log.d("NavFlag",NavFlag+"");
        orderDetails = getArguments().getParcelableArrayList("jsonTemp");
        Gson gson1 = new Gson();
        String a = gson1.toJson(orderDetails);
        Log.d("a",a+"");



        if(user.getEmail()!=null){
            userContact = user.getEmail().replace(".","");
        }
        else {
            userContact = user.getPhoneNumber();
        }
        Log.d("userContact",userContact);






        SharedPreferences sp = getActivity().getSharedPreferences("profileInfo",Context.MODE_PRIVATE);
        float addressCount = sp.getFloat("addressCount",0.0f);
        //if(addressCount!=0.0) {


            DatabaseReference databaseReference = firebaseDatabase.getReference().child("Category").child("Users").child(userContact).child("Addresses");
            //progressBar.setVisibility(View.INVISIBLE);
            addressesRecycler = v.findViewById(R.id.addressesRecycleView);
            firebaseFirestore = FirebaseFirestore.getInstance();
            //ArrayList<Address_Model> address_models = new ArrayList<>();



        /*SharedPreferences sp = getActivity().getSharedPreferences("loginCheck", Context.MODE_PRIVATE);
        int loginCheck = sp.getInt("loginCheck",0);

        if(loginCheck==0){
            userContact = user.getPhoneNumber();
        }
        else {
            userContact = user.getEmail();
        }*/
            //userContact = getArguments().getString("UserContact");
            Log.d("UserContact", userContact + " ");

        /*firebaseFirestore.collection(userContact).document("UserInfo").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                progressBar.setVisibility(View.VISIBLE);
                if(documentSnapshot.exists()) {
                    int addressCount = documentSnapshot.getDouble("addressCount").intValue();
                    if (addressCount != 0.0) {
                        for ( i = 1; i <= addressCount; i++) {
                            String docsPaths = "Address" + i;

                            databaseReference.addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                }

                                @Override
                                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                }

                                @Override
                                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                                }

                                @Override
                                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                            firebaseFirestore.collection(userContact).document(docsPaths).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {

                                    Address_Model address_model = new Address_Model(documentSnapshot.getString("name"), documentSnapshot.getString("ph"),
                                            documentSnapshot.getString("house"), documentSnapshot.getString("road"), documentSnapshot.getString("city"),
                                            documentSnapshot.getString("area"), documentSnapshot.getString("state"), documentSnapshot.getString("pinCode"),documentSnapshot.getString("AddressId"));
                                    Log.d("nameOfDocs",documentSnapshot.getString("name")+" ");
                                    Log.d("nameOfAddress",address_model.name+" ");
                                    address_models.add(address_model);
                                    Log.d("modelsAdding","done");
                                    if(address_models.size()==addressCount){

                                        Log.d("EntryInAdapter","done");
                                        Address_Recycle_Adapter ard = new Address_Recycle_Adapter(address_models, parent,getActivity());
                                        addressesRecycler.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                                        addressesRecycler.setAdapter(ard);

                                    }
                                }
                            });

                        }

                        Log.d("AddressSize", address_models.size()+" ");

                    }
                }
                progressBar.setVisibility(View.INVISIBLE);
            }
        });*/
            progressBar.setVisibility(View.VISIBLE);
            list.clear();
            databaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {


                    if(snapshot.getChildrenCount()==0){
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    Address_Model model = snapshot.getValue(Address_Model.class);
                    if (!list.contains(model)) {
                        list.add(model);
                        Log.d("brand", model.AddressId + " ");
                    }

                    if(NavFlag==1){
                        flag = 1;
                    }

                    Address_Recycle_Adapter ard = new Address_Recycle_Adapter(list, parent, getActivity(),orderDetails,flag,buyingPrd,getActivity(),NavFlag);
                    addressesRecycler.setLayoutManager(new LinearLayoutManager(parent));
                    addressesRecycler.setAdapter(ard);
                    progressBar.setVisibility(View.INVISIBLE);
                    Log.d("AdapterApplied", "SuccessFul");
                    if(list.isEmpty()){
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("childrenCount", error.getMessage() + " ");

                }
            });

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(list.isEmpty()){
                        progressBar.setVisibility(View.INVISIBLE);
                        alertAddress.setVisibility(View.VISIBLE);
                    }
                }
            },4000);
        //}



            addNewAddBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("flag",flag);
                    if(NavFlag == 0){
                        bundle.putParcelableArrayList("list",buyingPrd);
                    }
                    else {
                        bundle.putParcelableArrayList("jsonTemp",orderDetails);
                        bundle.putInt("NavFlag",1);
                    }

                    try{
                        ((MainActivity)parentActivity).runFragment(bundle,new AddAddress(),"Edit Address");
                    }
                    catch (Exception e){
                        ((search_instance)parentActivity).runFragment(bundle,new AddAddress(),4);
                    }
                }
            });


        return v;
    }
}