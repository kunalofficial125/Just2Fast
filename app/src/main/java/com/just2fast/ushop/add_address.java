package com.just2fast.ushop;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.hbb20.CountryCodePicker;

import java.util.ArrayList;
import java.util.HashMap;

public class add_address extends AppCompatActivity {
    String ph,city,postal,area,state,nominee,house,road;
    boolean STATE_CHECK;
    FirebaseFirestore firebaseFirestore;
    TextView activityTitle;
    FirebaseUser user;
    String userContact;
    FirebaseDatabase firebaseDatabase;
    //UserModel userData;
    String AddressRef;

    EditText newPh,newCity,newPostal,newArea,newState,newNominee,newHouse,newRoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        activityTitle = findViewById(R.id.activityTitle);
        user = FirebaseAuth.getInstance().getCurrentUser();
        newPh = findViewById(R.id.newPh);
        SharedPreferences sp = getSharedPreferences("profileInfo",Context.MODE_PRIVATE);
        float addressCount2 = sp.getFloat("addressCount",0.0f);
        newCity = findViewById(R.id.city);
        newArea = findViewById(R.id.area);
        newPostal = findViewById(R.id.postal);
        newState = findViewById(R.id.state);
        newNominee = findViewById(R.id.nominee);
        newHouse = findViewById(R.id.house);
        newRoad = findViewById(R.id.road);

        firebaseDatabase = FirebaseDatabase.getInstance();

        firebaseFirestore = FirebaseFirestore.getInstance();
        ArrayList<Address_Model> address_models = new ArrayList<>();
        Button save_address = findViewById(R.id.save_address);
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String phone = intent.getStringExtra("ph");
        String home = intent.getStringExtra("house");
        String street = intent.getStringExtra("road");
        String Area = intent.getStringExtra("area");
        String cities = intent.getStringExtra("city");
        String district = intent.getStringExtra("state");
        String pinCode = intent.getStringExtra("pinCode");
        int REQUEST_CODE = intent.getIntExtra("REQUEST_CODE",0);
        int Position = intent.getIntExtra("Position",-1);
        String AddressId = intent.getStringExtra("AddressId");


        SharedPreferences sp23 = getSharedPreferences("loginCheck",MODE_PRIVATE);
        city = sp23.getString("Location","null");


        if(REQUEST_CODE==100){
            activityTitle.setText("UPDATE ADDRESS");
            newNominee.setText(name);
            newHouse.setText(home);
            newRoad.setText(street);
            newPh.setText(phone);

            save_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("positionClass",Position+" ");
                    CountryCodePicker ccp  = findViewById(R.id.countryCodePicker);
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
                        Toast.makeText(add_address.this, "ENTER NAME MORE THAN 5 LETTERS", Toast.LENGTH_SHORT).show();
                    } else if (ph.length()<10) {
                        Toast.makeText(add_address.this, "ENTER VALID MOBILE NUMBER", Toast.LENGTH_SHORT).show();
                        //} else if (!STATE_CHECK) {
                        // Toast.makeText(add_address.this, "ENTER VALID STATE", Toast.LENGTH_SHORT).show();
                    }else if (house.length()<1 && road.length()<1 && area.length()<1 && city.length()<1 && state.length()<1 && postal.length()<1) {
                        Toast.makeText(add_address.this, "Fill All The Fields Correctly", Toast.LENGTH_SHORT).show();
                    } else {
                        SharedPreferences sp =getSharedPreferences("loginCheck", Context.MODE_PRIVATE);
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
                                updatedAddress.put("lastAddress",city);
                                databaseReference.updateChildren(updatedAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(add_address.this, "Address Updated", Toast.LENGTH_SHORT).show();
                                            Intent intentToHome = new Intent(add_address.this,MainActivity.class);
                                            intentToHome.putExtra("FRAGMENT_REQ_CODE",1000);
                                            startActivity(intentToHome);
                                            finish();
                                        }
                                        else {
                                            Toast.makeText(add_address.this, "Filed! Please Try after some time", Toast.LENGTH_SHORT).show();
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
                                            Intent intent167 = new Intent(add_address.this,MainActivity.class);
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
                    CountryCodePicker ccp = findViewById(R.id.countryCodePicker);
                    house = newHouse.getText().toString();
                    road = newRoad.getText().toString();
                    nominee = newNominee.getText().toString();
                    state = newState.getText().toString();
                    postal = newPostal.getText().toString();
                    ccp.registerCarrierNumberEditText(newPh);
                    ph = ccp.getFullNumberWithPlus();
                    String[] states = {"Uttar Pradesh", "West Bengal", "Maharashtra", "Karnataka", "Rajasthan", "Andhra Pradesh", "Madhya Pradesh", "Gujarat", "Bihar", "Tamil Nadu", "Odisha", "Kerala", "Assam", "Jharkhand", "Punjab", "Chhattisgarh", "Himachal Pradesh", "Uttarakhand", "Haryana", "Tripura", "Delhi", "Meghalaya", "Goa", "Manipur", "Mizoram", "Arunachal Pradesh", "Nagaland", "Puducherry", "Andaman and Nicobar Islands", "Sikkim", "Daman and Diu", "Lakshadweep", "Chandigarh", "Jammu and Kashmir", "Dadra and Nagar Haveli", "Ladakh"};
                    for (String s : states) {
                        if (state.toLowerCase().toLowerCase().trim().equals(s.toLowerCase())) {
                            STATE_CHECK = true;
                        }
                    }
                    if (nominee.length() < 5) {
                        Toast.makeText(add_address.this, "ENTER NAME MORE THAN 5 LETTERS", Toast.LENGTH_SHORT).show();
                    } else if (ph.length() < 10) {
                        Toast.makeText(add_address.this, "ENTER VALID MOBILE NUMBER", Toast.LENGTH_SHORT).show();
                        //} else if (!STATE_CHECK) {
                        // Toast.makeText(add_address.this, "ENTER VALID STATE", Toast.LENGTH_SHORT).show();
                    } else if (house.length() < 1 && road.length() < 1 && area.length() < 1 && city.length() < 1 && state.length() < 1 && postal.length() < 1) {
                        Toast.makeText(add_address.this, "Fill All The Fields Correctly", Toast.LENGTH_SHORT).show();
                    } else {
                        SharedPreferences sp = getSharedPreferences("loginCheck", Context.MODE_PRIVATE);
                        int loginCheck = sp.getInt("loginType", 0);

                        if (loginCheck == 0) {
                            userContact = user.getPhoneNumber();
                        } else {
                            userContact = user.getEmail();
                        }





                        firebaseFirestore.collection(userContact).document("UserInfo").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Log.d("fetch", "done");
                                //int addressState = userData.addressCount;
                                Log.d("UserInfoAfterReplace",userContact);
                                Double getaddressCount = documentSnapshot.getDouble("addressCount");
                                Address_Model address_model = new Address_Model(nominee, ph, house, road, city,addressCount2+1+"");
                                int addressCount = (int) addressCount2;

                                String documentRef = "Address" + (addressCount + 1);
                                Log.d("addressCount",documentRef+"");

                                DatabaseReference databaseReference = firebaseDatabase.getReference().child("Category").child("Users").child(userContact.replace(".","")).child("Addresses");
                                databaseReference.child(documentRef).setValue(address_model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            firebaseFirestore.collection(userContact).document("UserInfo").update("addressCount", (addressCount2 + 1.0));
                                            Log.d("addressAdded", "Done");
                                            Toast.makeText(add_address.this, "ADDRESS ADDED SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                                            Intent intent2 = new Intent(add_address.this, MainActivity.class);
                                            intent2.putExtra("FRAGMENT_REQ_CODE",1000);
                                            SharedPreferences sp1 = getSharedPreferences("profileInfo",Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sp1.edit();
                                            editor.putFloat("addressCount",(float)addressCount2+1);
                                            editor.apply();
                                            startActivity(intent2);
                                            finish();
                                        }
                                    }
                                });

                                /*firebaseFirestore.collection(userContact).document(documentRef).set(address_model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        firebaseFirestore.collection(userContact).document("UserInfo").update("addressCount", (documentSnapshot.getDouble("addressCount")) + 1.0);
                                        Log.d("addressAdded", "Done");
                                        Toast.makeText(add_address.this, "ADDRESS ADDED SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                                        Intent intent2 = new Intent(add_address.this, MainActivity.class);
                                        intent2.putExtra("FRAGMENT_REQ_CODE",1000);
                                        startActivity(intent2);
                                        finish();
                                    }
                                });*/
                            }
                        });

                    }

                }
            });
        }

            }

    @Override
    public void onBackPressed() {
        Intent intent167 = new Intent(add_address.this, MainActivity.class);
        intent167.putExtra("FRAGMENT_REQ_CODE", 1000);
        startActivity(intent167);
        finish();
    }
}
