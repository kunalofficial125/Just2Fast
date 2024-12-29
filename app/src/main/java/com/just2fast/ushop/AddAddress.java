package com.just2fast.ushop;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.hbb20.CountryCodePicker;

import com.microsoft.azure.storage.blob.CloudBlobContainer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddAddress#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddAddress extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddAddress() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddAddress.
     */
    // TODO: Rename and change types and number of parameters
    public static AddAddress newInstance(String param1, String param2) {
        AddAddress fragment = new AddAddress();
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

    String ph,city,postal,area,state,nominee,house,road;
    boolean STATE_CHECK;
    FirebaseFirestore firebaseFirestore;
    TextView activityTitle;
    FirebaseUser user;
    String userContact;
    FirebaseDatabase firebaseDatabase;
    File localFile;
    LottieAnimationView lnv;
    FirebaseStorage storage;
    CloudBlobContainer blobContainer;
    UserModel userData;
    String AddressRef,jsonTemp;
    ArrayList<OrderDetails> orderDetails ;
    double addressCount2;
    int NavFlag;
    ArrayList<ProductModel> list;
    Activity parent;
    String storageConnectionString = "DefaultEndpointsProtocol=https;AccountName=tykun;AccountKey=0DPLRf2Sm3gEkXRcPLQYPT1cPjnpSuHTZY0QpQjtXPRieRAg/+" +
            "vaCycyI3otZj3tu/hPmwKp6Le5+AStW89EtQ==;EndpointSuffix=core.windows.net";

    EditText newPh,newCity,newPostal,newArea,newState,newNominee,newHouse,newRoad;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_address, container, false);
        //getActivity().getSupportFragmentManager().beginTransaction().detach(getActivity().getSupportFragmentManager().findFragmentByTag("saved")).commit();
        activityTitle = v.findViewById(R.id.activityTitle);
        user = FirebaseAuth.getInstance().getCurrentUser();
        newPh = v.findViewById(R.id.newPh);
        parent = getActivity();
        SharedPreferences sp = getActivity().getSharedPreferences("profileInfo", Context.MODE_PRIVATE);

        newCity = v.findViewById(R.id.city);
        lnv = v.findViewById(R.id.progressBar9);
        newArea = v.findViewById(R.id.area);
        newPostal = v.findViewById(R.id.postal);
        newState = v.findViewById(R.id.state);
        newNominee = v.findViewById(R.id.nominee);
        newHouse = v.findViewById(R.id.house);
        newRoad = v.findViewById(R.id.road);
        lnv.setVisibility(View.INVISIBLE);
        firebaseDatabase = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        firebaseFirestore = FirebaseFirestore.getInstance();
        ArrayList<Address_Model> address_models = new ArrayList<>();
        Button save_address = v.findViewById(R.id.save_address);
        String name = getArguments().getString("name");
        String phone = getArguments().getString("ph");
        String home = getArguments().getString("house");
        String street = getArguments().getString("road");
        int flag = getArguments().getInt("flag");
        NavFlag = getArguments().getInt("NavFlag");
        Log.d("NavFlagAdd",NavFlag+"");
        if(flag==1){
           list = getArguments().getParcelableArrayList("list");
        }

        if(NavFlag == 1){
            Log.d("NavFlagEntryInAdd",NavFlag+"");
            orderDetails = getArguments().getParcelableArrayList("jsonTemp");
            Log.d("OrderDetailsInAddPart",orderDetails.get(0).product.get(0).sellerId);
        }
        String Area = getArguments().getString("area");
        String cities = getArguments().getString("city");
//        String district = getArguments().getString("state");
//        String pinCode = getArguments().getString("pinCode");
        int REQUEST_CODE = getArguments().getInt("REQUEST_CODE",0);
        int Position = getArguments().getInt("Position",-1);
        String AddressId = getArguments().getString("AddressId");


        SharedPreferences sp23 = parent.getSharedPreferences("loginCheck",Context.MODE_PRIVATE);
        city = sp23.getString("Location","null");

        Log.d("locationCheckAdd",city+" ");


        if(REQUEST_CODE==100){
            activityTitle.setText("UPDATE ADDRESS");
            newNominee.setText(name);
            newHouse.setText(home);
            newRoad.setText(street);
//            newState.setText(district);
            newPh.setText(phone);
//            newPostal.setText(pinCode);

            save_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lnv.setVisibility(View.VISIBLE);
                    save_address.setVisibility(View.INVISIBLE);
                    Log.d("positionClass",Position+" ");
                    CountryCodePicker ccp  = v.findViewById(R.id.countryCodePicker);
                    house = newHouse.getText().toString();
                    road = newRoad.getText().toString();
                    nominee = newNominee.getText().toString();
                    state = newState.getText().toString();
                    postal = newPostal.getText().toString();
                    ccp.registerCarrierNumberEditText(newPh);
                    ph = ccp.getFullNumberWithPlus();
                    String [] states={"Uttar Pradesh", "West Bengal","Maharashtra", "Karnataka", "Rajasthan", "Andhra Pradesh", "Madhya Pradesh", "Gujarat", "Bihar", "Tamil Nadu", "Odisha", "Kerala", "Assam","Jharkhand", "Punjab", "Chhattisgarh", "Himachal Pradesh", "Uttarakhand", "Haryana", "Tripura", "Delhi", "Meghalaya", "Goa", "Manipur", "Mizoram", "Arunachal Pradesh", "Nagaland", "Puducherry", "Andaman and Nicobar Islands", "Sikkim", "Daman and Diu", "Lakshadweep", "Chandigarh", "Jammu and Kashmir", "Dadra and Nagar Haveli","Ladakh"};
                    for (String s : states) {
                        if (state.toLowerCase().toLowerCase().trim().equals(s.toLowerCase())) {
                            STATE_CHECK = true;
                        }
                    }
                    if(nominee.length()<5){
                        Toast.makeText(getActivity(), "ENTER NAME MORE THAN 5 LETTERS", Toast.LENGTH_SHORT).show();
                    } else if (ph.length()<10) {
                        Toast.makeText(getActivity(), "ENTER VALID MOBILE NUMBER", Toast.LENGTH_SHORT).show();
                        //} else if (!STATE_CHECK) {
                        // Toast.makeText(getActivity(), "ENTER VALID STATE", Toast.LENGTH_SHORT).show();
                    }else if (house.length()<1 && road.length()<1 && area.length()<1 && city.length()<1 && state.length()<1 && postal.length()<1) {
                        Toast.makeText(getActivity(), "Fill All The Fields Correctly", Toast.LENGTH_SHORT).show();
                    } else {
                        SharedPreferences sp = getActivity().getSharedPreferences("loginCheck", Context.MODE_PRIVATE);
                        int loginCheck = sp.getInt("loginType",0);

                        if(loginCheck==0){
                            userContact = user.getPhoneNumber();
                        }
                        else {
                            userContact = user.getEmail().replace(".","");

                        }

                        Log.d("entryInUpdating",""+userContact);



                        if(!AddressId.equals("0.0")){
                            AddressRef = "Address"+AddressId.replace(".0","");
                            Log.d("AddressRef",AddressRef+" ");
                        }
                        else{

                            AddressRef = "Address"+0;
                        }






                        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Category").child("Users").child(userContact).child("Addresses").child(AddressRef);

                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                // Address_Model address = snapshot.getValue(Address_Model.class);
                                //Address_Model updatedAddress = new Address_Model(nominee,ph,house,road,city,area,state,postal,AddressId);
                                HashMap<String,Object> updatedAddress = new HashMap<>();
                                updatedAddress.put("name",nominee);
                                updatedAddress.put("ph",ph);
                                updatedAddress.put("house",house);
                                updatedAddress.put("road",road);
//                                updatedAddress.put("state",state);
//                                updatedAddress.put("pinCode",postal);
                                databaseReference.updateChildren(updatedAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(parent, "Address Updated", Toast.LENGTH_SHORT).show();
                                            Bundle bundle = new Bundle();
                                            if(NavFlag==0){
                                                if(flag==1){
                                                    bundle.putInt("flag",1);
                                                    bundle.putParcelableArrayList("list",list);
                                                    bundle.putString("Address",house+" "+road+" "+city+" ");
                                                    bundle.putString("ph",ph);
                                                    bundle.putString("name",nominee);
                                                    if(parent instanceof  search_instance){
                                                        ((search_instance)parent).runFragment(bundle,new BuyingDetails(),0);
                                                    }
                                                    else {
                                                        ((MainActivity)parent).runFragment(bundle,new BuyingDetails(),"");
                                                    }
                                                }
                                                else{
                                                    ((MainActivity)parent).runFragment(bundle,new savedAddresses(),"Saved Addresses");
                                                }
                                            }
                                            else {
                                                bundle.putInt("NavFlag",1);
                                                bundle.putParcelableArrayList("jsonTemp",orderDetails);
                                                bundle.putString("Address",house+" "+road+" "+city+" ");
                                                bundle.putString("ph",ph);
                                                bundle.putString("name",nominee);
                                                ((MainActivity)parent).runFragment(bundle,new payment_method(),"Payment Method");
                                            }
                                        }
                                        else {
                                            Toast.makeText(getActivity(), "Filed! Please Try after some time", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        /*firebaseFirestore.collection(userContact).whereEqualTo("AddressId",AddressId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    Address_Model model = documentSnapshot.toObject(Address_Model.class);
                                    // Access other document fields here using documentSnapshot.get("field_name")
                                    firebaseFirestore.collection(userContact).document(documentSnapshot.getId()).update("name",nominee,"ph",ph,"house",house,"road",road,"city",city,"area",area,"state",state,"pinCode",postal).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d("AddressUpdated","done");
                                            Intent intent167 = new Intent(getActivity(),MainActivity.class);
                                            intent167.putExtra("FRAGMENT_REQ_CODE",1000);
                                            startActivity(intent167);
                                            finish();
                                        }
                                    });

                                }
                            }
                        });*/

                    }
                }
            });
        }
        else {
            save_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    firebaseDatabase.getReference().child("Category").child("UsersData").child(user.getEmail().replace(".","")).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            UserModel userModel = snapshot.getValue(UserModel.class);
                            addressCount2 =userModel.addressCount;

                            lnv.setVisibility(View.VISIBLE);
                            save_address.setVisibility(View.INVISIBLE);
                            CountryCodePicker ccp = v.findViewById(R.id.countryCodePicker);
                            house = newHouse.getText().toString();
                            road = newRoad.getText().toString();
                            nominee = newNominee.getText().toString();
//                    state = newState.getText().toString();
//                    postal = newPostal.getText().toString();
                            ccp.registerCarrierNumberEditText(newPh);
                            ph = ccp.getFullNumberWithPlus();
//                    String[] states = {"Uttar Pradesh", "West Bengal", "Maharashtra", "Karnataka", "Rajasthan", "Andhra Pradesh", "Madhya Pradesh", "Gujarat", "Bihar", "Tamil Nadu", "Odisha", "Kerala", "Assam", "Jharkhand", "Punjab", "Chhattisgarh", "Himachal Pradesh", "Uttarakhand", "Haryana", "Tripura", "Delhi", "Meghalaya", "Goa", "Manipur", "Mizoram", "Arunachal Pradesh", "Nagaland", "Puducherry", "Andaman and Nicobar Islands", "Sikkim", "Daman and Diu", "Lakshadweep", "Chandigarh", "Jammu and Kashmir", "Dadra and Nagar Haveli", "Ladakh"};
//                    for (String s : states) {
//                        if (state.toLowerCase().toLowerCase().trim().equals(s.toLowerCase())) {
//                            STATE_CHECK = true;
//                        }
//                    }
                            if (nominee.length() < 5) {
                                Toast.makeText(getActivity(), "ENTER NAME MORE THAN 5 LETTERS", Toast.LENGTH_SHORT).show();
                            } else if (ph.length() < 10) {
                                Toast.makeText(getActivity(), "ENTER VALID MOBILE NUMBER", Toast.LENGTH_SHORT).show();
                                //} else if (!STATE_CHECK) {
                                // Toast.makeText(getActivity(), "ENTER VALID STATE", Toast.LENGTH_SHORT).show();
                            } else if (house.length() < 1 && road.length() < 1 && area.length() < 1 && city.length() < 1 && state.length() < 1 && postal.length() < 1) {
                                Toast.makeText(getActivity(), "Fill All The Fields Correctly", Toast.LENGTH_SHORT).show();
                            } else {
                                SharedPreferences sp = getActivity().getSharedPreferences("loginCheck", Context.MODE_PRIVATE);
                                int loginCheck = sp.getInt("loginType", 0);

                                if (loginCheck == 0) {
                                    userContact = user.getPhoneNumber();
                                } else {
                                    userContact = user.getEmail();
                                }

                                SharedPreferences sp23 = parent.getSharedPreferences("loginCheck",Context.MODE_PRIVATE);
                                city = sp23.getString("Location","null");

                                Log.d("locationCheckAdd2",city+" ");


                                Address_Model address_model = new Address_Model(nominee, ph, house, road, city,addressCount2+1+"");
                                int addressCount = (int) addressCount2;

                                String documentRef = "Address" + (addressCount + 1);
                                Log.d("addressCount",documentRef+"");

                                DatabaseReference databaseReference = firebaseDatabase.getReference().child("Category").child("Users").child(user.getEmail().replace(".","")).child("Addresses");
                                databaseReference.child(documentRef).setValue(address_model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            //firebaseFirestore.collection(userContact).document("UserInfo").update("addressCount", (addressCount2 + 1.0));
                                            Log.d("addressAdded", "Done");
                                            Toast.makeText(getActivity(), "ADDRESS ADDED SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                                            SharedPreferences sp2 = getActivity().getSharedPreferences("profile",Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor2 = sp2.edit();

                                            SharedPreferences sp1 =getActivity().getSharedPreferences("profileInfo",Context.MODE_PRIVATE);

                                            SharedPreferences.Editor editor = sp1.edit();
                                            editor.putFloat("addressCount",(float)addressCount2+1);
                                            editor.apply();
                                            firebaseDatabase.getReference().child("Category").child("UsersData").child(user.getEmail().replace(".","")).child("addressCount").setValue(addressCount2+1);


                                            SharedPreferences sp = getActivity().getSharedPreferences("profile",Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor12  = sp.edit();
                                            editor12.putInt("profileUpdateReq",1);
                                            editor12.apply();


                                            Bundle bundle = new Bundle();
                                            Log.d("flagInAdd", flag + " ");
                                            if (NavFlag == 0) {
                                                if (flag == 1) {
                                                    bundle.putInt("flag", 1);
                                                    bundle.putParcelableArrayList("list", list);
                                                    bundle.putString("Address", house + " " + road  + " " + city + " ");
                                                    bundle.putString("ph", ph);
                                                    bundle.putString("name", nominee);
                                                    if (parent instanceof search_instance) {
                                                        ((search_instance) parent).runFragment(bundle, new BuyingDetails(), 0);
                                                    } else {
                                                        ((MainActivity) parent).runFragment(bundle, new BuyingDetails(), "");
                                                    }
                                                } else {
                                                    ((MainActivity) parent).runFragment(bundle, new savedAddresses(), "Saved Addresses");
                                                }

                                            } else {
                                                bundle.putInt("NavFlag", 1);
                                                bundle.putParcelableArrayList("jsonTemp", orderDetails);
                                                bundle.putString("Address", house + " " + road + " " +" " + city + " ");
                                                bundle.putString("ph", ph);
                                                bundle.putString("name", nominee);
                                                ((MainActivity) parent).runFragment(bundle, new payment_method(), "Payment Method");
                                            }




                                            /*String Url = "https://tykun.blob.core.windows.net/users/"+user.getEmail().replace(".","")+".txt";

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
                                                        jsonTemp = JsonBody.replace((userData.addressCount)+"",(userData.addressCount+1)+"");

                                                        localFile= new File(getActivity().getFilesDir(),"template.txt");
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
                                                                                SharedPreferences sp = getActivity().getSharedPreferences("profile",Context.MODE_PRIVATE);
                                                                                SharedPreferences.Editor editor  = sp.edit();
                                                                                editor.putInt("profileUpdateReq",1);
                                                                                editor.apply();

                                                                                getActivity().runOnUiThread(new Runnable() {
                                                                                    @Override
                                                                                    public void run() {
                                                                                        Bundle bundle = new Bundle();
                                                                                        Log.d("flagInAdd",flag+" ");
                                                                                        if (NavFlag==0){
                                                                                            if(flag==1){
                                                                                                bundle.putInt("flag",1);
                                                                                                bundle.putParcelableArrayList("list",list);
                                                                                                bundle.putString("Address",house+" "+road+" "+area+" "+city+" ");
                                                                                                bundle.putString("ph",ph);
                                                                                                bundle.putString("name",nominee);
                                                                                                if(parent instanceof  search_instance){
                                                                                                    ((search_instance)parent).runFragment(bundle,new BuyingDetails(),0);
                                                                                                }
                                                                                                else {
                                                                                                    ((MainActivity)parent).runFragment(bundle,new BuyingDetails(),"");
                                                                                                }
                                                                                            }
                                                                                            else{
                                                                                                ((MainActivity)parent).runFragment(bundle,new savedAddresses(),"Saved Addresses");
                                                                                            }

                                                                                        }
                                                                                        else {
                                                                                            bundle.putInt("NavFlag",1);
                                                                                            bundle.putParcelableArrayList("jsonTemp",orderDetails);
                                                                                            bundle.putString("Address",house+" "+road+" "+area+" "+city+" ");
                                                                                            bundle.putString("ph",ph);
                                                                                            bundle.putString("name",nominee);
                                                                                            ((MainActivity)parent).runFragment(bundle,new payment_method(),"Payment Method");
                                                                                        }
                                                                                    }
                                                                                });





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
                                                    }
                                                }});*/






                                        }
                                    }
                                });

                                /*firebaseFirestore.collection(userContact).document(documentRef).set(address_model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        firebaseFirestore.collection(userContact).document("UserInfo").update("addressCount", (documentSnapshot.getDouble("addressCount")) + 1.0);
                                        Log.d("addressAdded", "Done");
                                        Toast.makeText(getActivity(), "ADDRESS ADDED SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                                        Intent intent2 = new Intent(getActivity(), MainActivity.class);
                                        intent2.putExtra("FRAGMENT_REQ_CODE",1000);
                                        startActivity(intent2);
                                        finish();
                                    }
                                });*/
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });







                }
            });
        }
        return v;
    }

    public void writingJsonBody(File file,String JsonBody,int flag) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
        if(flag==1){
            writer.write("");
            writer.write(JsonBody);
            writer.flush();
            writer.close();
        }
        else{
            writer.write("");
            writer.close();
        }
        Log.d("WritingDone","Yes");
    }
}