package com.just2fast.ushop;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.firestore.FirebaseFirestore;


import com.google.gson.Gson;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class profile extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment profile.
     */
    // TODO: Rename and change types and number of parameters
    public static profile newInstance(String param1, String param2) {
        profile fragment = new profile();
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
    Button log_out_btn;
    TextView editButton, savedAddressesButton,categories;
    ProgressBar progressBar;
    TextView displayName,myOrdersText;
    double name_state;
    Gson gson = new Gson();
    Context parentActivityContext;
    TextView aboutUs, contactUs, tc,pp;
    double addressCount;
    UserModel userData;
    ImageView profile_img;
    FirebaseFirestore firebaseFirestore;
    int imgUpdateCode;
    int profileUpdateReq;
    Activity parent;
    FirebaseDatabase firebaseDatabase;
    String userPh,name,email;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FirebaseUser user = FirebaseAuth. getInstance(). getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        parent = getActivity();
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        displayName = v.findViewById(R.id.name);
        myOrdersText = v.findViewById(R.id.myOrdersText);
        firebaseDatabase = FirebaseDatabase.getInstance();
        progressBar = v.findViewById(R.id.progressBar11);
        categories = v.findViewById(R.id.categories);
        pp = v.findViewById(R.id.pp);
        contactUs = v.findViewById(R.id.contactUs);
        aboutUs = v.findViewById(R.id.aboutUs);
        tc = v.findViewById(R.id.tc);
        progressBar.setVisibility(View.INVISIBLE);
        savedAddressesButton = v.findViewById(R.id.savedAddressesButton);
        parentActivityContext = getActivity().getApplicationContext();
        editButton = v.findViewById(R.id.editButton);
        profile_img  = v.findViewById(R.id.profile_img);




        Intent toBrowser = new Intent(Intent.ACTION_VIEW);

        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("EnterBrowser","aboutUs");
                toBrowser.setData(Uri.parse("https://www.just2fast.com/about-us"));
                startActivity(toBrowser);
            }
        });

        pp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("EnterBrowser","privacyPolicy");
                toBrowser.setData(Uri.parse("https://www.just2fast.com/privacy-policy"));
                startActivity(toBrowser);
            }
        });

        tc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("EnterBrowser","termsConditions");
                toBrowser.setData(Uri.parse("https://www.just2fast.com/terms-conditions"));
                startActivity(toBrowser);
            }
        });

        contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("EnterBrowser","contactUs");
                toBrowser.setData(Uri.parse("https://www.just2fast.com/contact-us"));
                startActivity(toBrowser);
            }
        });

        SharedPreferences sp1 = getActivity().getSharedPreferences("profileInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp1.edit();
        SharedPreferences sp = getActivity().getSharedPreferences("loginCheck", MODE_PRIVATE);


        //String Url = "https://tykun.blob.core.windows.net/users/"+user.getEmail().replace(".","")+".txt";
        SharedPreferences sp2 = getActivity().getSharedPreferences("profile", MODE_PRIVATE);
        imgUpdateCode = sp2.getInt("ImgUpdateCode",1);
        profileUpdateReq = sp2.getInt("profileUpdateReq",1);

        if(imgUpdateCode == 1){
            Drawable drawable = parent.getDrawable(R.drawable.pfp1);
            profile_img.setImageDrawable(drawable);
        } else if (imgUpdateCode == 2) {
            Drawable drawable = parent.getDrawable(R.drawable.pfp3);
            profile_img.setImageDrawable(drawable);
        }
        else if (imgUpdateCode == 3) {
            Drawable drawable = parent.getDrawable(R.drawable.pfp5);
            profile_img.setImageDrawable(drawable);
        }
        else if (imgUpdateCode == 4) {
            Drawable drawable = parent.getDrawable(R.drawable.pfp2);
            profile_img.setImageDrawable(drawable);
        }
        else if (imgUpdateCode == 5) {
            Drawable drawable =parent.getDrawable(R.drawable.pfp4);
            profile_img.setImageDrawable(drawable);
        }
        else {
            Drawable drawable = parent.getDrawable(R.drawable.pfp6);
            profile_img.setImageDrawable(drawable);
        }

        if(profileUpdateReq==1){
            progressBar.setVisibility(View.VISIBLE);
            firebaseDatabase.getReference().child("Category").child("UsersData").child(user.getEmail().replace(".","")).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                     userData = snapshot.getValue(UserModel.class);
                    String JsonBody = gson.toJson(userData);
                    userData = gson.fromJson(JsonBody, UserModel.class);
                    name_state = userData.name_state;
                    SharedPreferences sharedPreferences = parent.getSharedPreferences("profile", MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = sharedPreferences.edit();
                    editor1.putInt("profileUpdateReq",0);
                    editor1.putString("userDataAsJson",JsonBody);
                    editor1.apply();
                    SharedPreferences sp2 =parent.getSharedPreferences("UserDetails",MODE_PRIVATE);
                    SharedPreferences.Editor editor2 = sp2.edit();
                    editor2.putString("UserJson",JsonBody);
                    editor2.apply();
                    name = userData.name;
                    parent.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            displayName.setText(name);
                            addressCount =userData.addressCount;
                            editor.putFloat("addressCount", (float) addressCount);
                            editor.apply();
                            Log.d("nameByDocs", name + " ");
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });






            /*OkHttpClient client = new OkHttpClient();
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
                        userData = new UserModel();
                        userData = gson.fromJson(JsonBody, UserModel.class);
                        name_state = userData.name_state;
                        SharedPreferences sharedPreferences = parent.getSharedPreferences("profile", MODE_PRIVATE);
                        SharedPreferences.Editor editor1 = sharedPreferences.edit();
                        editor1.putInt("profileUpdateReq",0);
                        editor1.putString("userDataAsJson",JsonBody);
                        editor1.apply();
                        SharedPreferences sp2 =parent.getSharedPreferences("UserDetails",MODE_PRIVATE);
                        SharedPreferences.Editor editor2 = sp2.edit();
                        editor2.putString("UserJson",JsonBody);
                        editor2.apply();
                        name = userData.name;
                        parent.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                displayName.setText(name);
                                addressCount =userData.addressCount;
                                editor.putFloat("addressCount", (float) addressCount);
                                editor.apply();
                                Log.d("nameByDocs", name + " ");
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        });
//                                                    Double name_state = documentSnapshot.getDouble("name_state");
//                                                    String ph = documentSnapshot.getString("ph");
//                                                    String profile_img_url = documentSnapshot.getString("profile_img_url");
//                                                    Double profile_img_state = documentSnapshot.getDouble("profile_img_state");
//                                                    if (name_state == 1.0) {
//                                                        if (name.length() > 4) {
//                                                            displayName.setText(name);
//                                                            Log.d("name", name);
//
//                                                        }
//                                                    } else {
//                                                        Log.d("usermobile", userPh + " ");
//                                                        displayName.setText(ph);
//                                                    }
                    }
                }});*/
        }
        else{
            SharedPreferences sharedPreferences = parent.getSharedPreferences("profile", MODE_PRIVATE);
            String JsonBody = sharedPreferences.getString("userDataAsJson","null");
            Gson gson = new Gson();

            UserModel userData = gson.fromJson(JsonBody,UserModel.class);
            displayName.setText(userData.name);
            name = userData.name;
            progressBar.setVisibility(View.INVISIBLE);

        }




            /*firebaseFirestore.collection(userPh).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                    if(error==null){
                        userInfo = value.toObjects(UserModel.class);
                        UserModel userModel =(UserModel) userInfo.get(userInfo.size()-1);
                        name = userModel.name;
                        Log.d("UserPh",userModel.ph+" ");
                        if(userModel.name_state==1){
                            if(name.length()>4){
                                displayName.setText(name);
                                Log.d("name",name);

                            }
                        }
                        else {
                            Log.d("usermobile",userPh+" ");
                            displayName.setText(userModel.ph);
                        }
                        if(userModel.profile_img_state==1){
                            progressBar.setVisibility(View.VISIBLE);
                            Log.d("profileImgUrl",userModel.profile_img_url+" ");
                            Glide.with(parentActivityContext).load(userModel.profile_img_url).placeholder(R.drawable.loading).into(profile_img);
                            Log.d("glideComplete","done");
                            progressBar.setVisibility(View.INVISIBLE);

                        }

                    }
                }
            });*/



            /*firebaseFirestore.collection(user.getEmail()).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                    if(error==null){
                        userInfo = value.toObjects(UserModel.class);
                        UserModel userModel =(UserModel) userInfo.get(userInfo.size()-1);
                        name = userModel.name;
                        Log.d("UserPh",userModel.ph+" ");
                        displayName.setText(name);
                        if(userModel.profile_img_state==1){

                            Log.d("userImgUrl",userModel.profile_img_url+" ");
                            Glide.with(parentActivityContext).load(userModel.profile_img_url).placeholder(R.drawable.loading).into(profile_img);
                            Log.d("glideComplete","done");

                        }
                    }
                }
            });*/


        log_out_btn = v.findViewById(R.id.log_out_btn);
        log_out_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder logout = new AlertDialog.Builder(getContext());
                logout.setMessage("Are You Sure Want To Logout");
                logout.setIcon(R.drawable.logout_svg);
                logout.setTitle("LOGOUT?");
                logout.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        try {
                            FirebaseAuth.getInstance().signOut();
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putBoolean("checkLogin",false);
                            editor.apply();
                            Intent toLogin = new Intent(getActivity(),Login_page.class);
                            startActivity(toLogin);
                            ((MainActivity)parent).finish();
                            Log.d("sign-outdone","SignOut");
                        }catch (Exception e){
                            Log.d("signout",e+" ");
                        }
                    }
                });
                logout.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                logout.show();
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("name",name);
                bundle.putDouble("name_state",name_state);
                try {
                    ((MainActivity)parent).runFragment(bundle,new edit_profile(),"Edit Profile");
                }catch (Exception e){
                    Log.d("bundleError",e+" ");
                }
            }
        });
        savedAddressesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)parent).runFragment(new Bundle(),new savedAddresses(),"Saved Addresses");
                Log.d("fragRunSuccessfully","done");
            }
        });

        myOrdersText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)parent).runFragment(new Bundle(),new orders(),"YourOrders");
            }
        });

        categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)parent).runFragment(new Bundle(),new cart(),"Categories");
            }
        });



        return v;
    }

}