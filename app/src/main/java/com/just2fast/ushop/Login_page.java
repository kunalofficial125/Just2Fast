package com.just2fast.ushop;



import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.gson.Gson;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.squareup.picasso.Picasso;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class Login_page extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    FirebaseFirestore firebaseFirestore;
    SignInButton signInButton;
    File localFile;
    CloudBlobContainer container;
    Boolean checkPolicy;
    ConstraintLayout cs;
    ImageView bg;
    FirebaseStorage storage;
    LottieAnimationView shopLoad;
    FirebaseDatabase firebaseDatabase;
    String Phone;
    String storageConnectionString = "DefaultEndpointsProtocol=https;AccountName=tykun;AccountKey=0DPLRf2Sm3gEkXRcPLQYPT1cPjnpSuHTZY0QpQjtXPRieRAg/+" +
            "vaCycyI3otZj3tu/hPmwKp6Le5+AStW89EtQ==;EndpointSuffix=core.windows.net";
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth mAuth;

    private void updateUI(FirebaseUser currentUser) {
        Intent toHome = new Intent(this,MainActivity.class);
        startActivity(toHome);
        finish();
        }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);


        firebaseDatabase  = FirebaseDatabase.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        bg = findViewById(R.id.bg);
        cs = findViewById(R.id.cs);
        mAuth = FirebaseAuth.getInstance();

        signInButton = findViewById(R.id.signInButton);
        shopLoad = findViewById(R.id.shopLoad);
        shopLoad.setVisibility(View.INVISIBLE);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);

        }

        SharedPreferences op = getSharedPreferences("Update Request",MODE_PRIVATE);
        SharedPreferences.Editor ed = op.edit();
        ed.putInt("Charges Update Req",1);
        ed.apply();

        Glide.with(this).load("https://firebasestorage.googleapis.com/v0/b/ushop-3a240.appspot.com/o/FCMImages%2Flogin_bg.png?alt=media&token=42ae4031-9dda-4916-9e64-e626f93b33f7").preload();


        CheckNetwork net = new CheckNetwork();

        if(net.isNetworkAvailable(this)){
            Log.d("net","Internet Available");
        }
        else{
            net.isNetworkNotAvailableResult(this);
        }

        SharedPreferences policyPreference = getSharedPreferences("policy",MODE_PRIVATE);
        SharedPreferences.Editor policyEditor = policyPreference.edit();
        checkPolicy = policyPreference.getBoolean("checkPolicy",false);

        Intent toBrowser = new Intent(Intent.ACTION_VIEW);



       try {
            Drawable drawable = AppCompatResources.getDrawable(this,R.drawable.lets_start);
            bg.setImageDrawable(drawable);
        }
        catch (Exception e){
            Log.d("ImageError",e+"");
            //Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/ushop-3a240.appspot.com/o/FCMImages%2Flogin_bg.png?alt=media&token=42ae4031-9dda-4916-9e64-e626f93b33f7").into(bg);

            try {
                Glide.with(this).load("https://firebasestorage.googleapis.com/v0/b/ushop-3a240.appspot.com/o/FCMImages%2Flogin_bg.png?alt=media&token=42ae4031-9dda-4916-9e64-e626f93b33f7").into(bg);

            }
            catch (Exception b){
                Log.d("ImageError",b+"");
            }

       }


       //Glide.with(this).load("https://firebasestorage.googleapis.com/v0/b/ushop-3a240.appspot.com/o/FCMImages%2Flogin_bg.png?alt=media&token=42ae4031-9dda-4916-9e64-e626f93b33f7").apply(new RequestOptions().override(800, 600)).into(bg);


        //Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/ushop-3a240.appspot.com/o/FCMImages%2Flogin_bg.png?alt=media&token=42ae4031-9dda-4916-9e64-e626f93b33f7").into(bg);


        Dialog policyDialog = new Dialog(this);
        policyDialog.setContentView(R.layout.policy_dialog);
        policyDialog.setCancelable(false);

        Button decline = policyDialog.findViewById(R.id.decline);
        Button accept = policyDialog.findViewById(R.id.accept);
        TextView tc = policyDialog.findViewById(R.id.tc);
        TextView pp = policyDialog.findViewById(R.id.pp);
        CheckBox checkBox = policyDialog.findViewById(R.id.checkBox);
        TextView cp = policyDialog.findViewById(R.id.cp);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBox.isChecked()){
                    policyEditor.putBoolean("checkPolicy",true);
                    policyEditor.apply();
                    policyDialog.dismiss();
                }
                else {
                    Toast.makeText(Login_page.this, "First Tick the check box", Toast.LENGTH_SHORT).show();
                }
            }
        });

        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        tc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toBrowser.setData(Uri.parse("https://www.just2fast.com/terms-conditions"));
                startActivity(toBrowser);
            }
        });

        pp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toBrowser.setData(Uri.parse("https://www.just2fast.com/privacy-policy"));
                startActivity(toBrowser);
            }
        });

        cp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toBrowser.setData(Uri.parse("https://www.just2fast.com/cancellation-and-return-policy"));
                startActivity(toBrowser);
            }
        });



        if(!checkPolicy){
            policyDialog.show();
        }



        processGoogleRequest();
//        login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                CheckNetwork net = new CheckNetwork();
//                if(net.isNetworkAvailable(Login_page.this)){
//                    Log.d("net","Internet Available");
//                }
//                else{
//                    net.isNetworkNotAvailableResult(Login_page.this);
//                }
//                if(ph.getText().toString().length()!=10){
//                    Toast.makeText(Login_page.this, "Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
//                }
//                else{
//                    Intent toOtp = new Intent(Login_page.this,otp_page.class);
//                    Phone  = ccp.getFullNumberWithPlus();
//                    Log.d("ccp",Phone+" ");
//                    toOtp.putExtra("ph",Phone);
//                    startActivity(toOtp);
//                    finish();
//                }
//
//            }
//        });
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopLoad.setVisibility(View.VISIBLE);
                signIn();
            }
        });





    }

    private void signIn() {
        try {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, 101);
            Log.d("doneSignIn","done");
        }
        catch (Exception e){
            shopLoad.setVisibility(View.INVISIBLE);
            Log.d("intentEx",e+" ");
        }

    }

    private void processGoogleRequest() {

        Log.d("processGoogleRequest","start");

            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(
                            "589299052560-0ke1l9qb8pjpj5g5dmquj0bo53u4fp1v.apps.googleusercontent.com")
                    .requestEmail()
                    .build();

            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
            Log.d("processGoogleRequest","done");
        }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    Log.d("fcmToken",account.getIdToken());
                    firebaseAuthWithGoogle(account.getIdToken());
                } catch (ApiException e) {
                    // Google Sign In failed, update UI appropriately
                    Log.d("failed",e+" ");
                }
            }
    private void firebaseAuthWithGoogle(String idToken) {
        CheckNetwork net = new CheckNetwork();
        if(net.isNetworkAvailable(this)){
            Log.d("net","Internet Available");
            AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();

                                DatabaseReference databaseReference = firebaseDatabase.getReference().child("Category").child("UsersData").child(user.getEmail().replace(".",""));

                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()){
                                            Log.d("userState","Login");
                                            Gson gson = new Gson();
                                            UserModel userData = snapshot.getValue(UserModel.class);
                                            String JsonBody = gson.toJson(userData);
                                            SharedPreferences sp2 = getSharedPreferences("UserDetails",MODE_PRIVATE);
                                            SharedPreferences.Editor editor2 = sp2.edit();
                                            editor2.putString("UserJson",JsonBody);
                                            editor2.apply();
                                            //userData = gson.fromJson(JsonBody, UserModel.class);
                                            SharedPreferences sp = getSharedPreferences("loginCheck",MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sp.edit();
                                            editor.putBoolean("locationCheck",false);
                                            editor.putInt("loginType",1);
                                            editor.putBoolean("checkLogin",true);
                                            editor.apply();
                                            SharedPreferences sharedPreferences = getSharedPreferences("banners",Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor1 = sharedPreferences.edit();
                                            editor1.putInt("updateReq",0);
                                            Calendar calendar = Calendar.getInstance();
                                            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                                            editor1.putInt("lastUpdateDate",dayOfMonth);
                                            editor1.apply();
                                            updateUI(user);
                                        }
                                        else {
                                            Log.d("userState","SignUp");
                                            List<OrderDetails> myOrders = new ArrayList<>();
                                            HashMap<String,String> starsRef = new HashMap<>();
                                            UserModel userModel = new UserModel(user.getDisplayName(), user.getEmail(), 0.0, 0.0, myOrders, starsRef,"null","null");
                                            databaseReference.setValue(userModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Gson gson = new Gson();
                                                    String jsonTemp = gson.toJson(userModel);
                                                    SharedPreferences sp2 = getSharedPreferences("UserDetails",MODE_PRIVATE);
                                                    SharedPreferences.Editor editor2 = sp2.edit();
                                                    editor2.putString("UserJson",jsonTemp);
                                                    editor2.apply();
                                                    SharedPreferences sp = getSharedPreferences("loginCheck",MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = sp.edit();

                                                    editor.putInt("loginType",1);
                                                    editor.putBoolean("checkLogin",true);
                                                    editor.apply();
                                                    updateUI(user);
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(Login_page.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(Login_page.this, "Login Failed, Try Again!", Toast.LENGTH_SHORT).show();
                                    }
                                });


                                String Url = "https://tykun.blob.core.windows.net/users/"+user.getEmail().replace(".","")+".txt";

                                // Initialize a BlobServiceClient

                                /*try {
                                    OkHttpClient client = new OkHttpClient();
                                    Request request = new Request.Builder()
                                            .url(Url)
                                            .build();
                                    client.newCall(request).enqueue(new Callback() {
                                        @Override
                                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                            Log.d("OkHttp","Failure to get");
                                        }

                                        @Override
                                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                            String JsonBody = response.body().string();
                                            Log.d("JsonBody",JsonBody);
                                            if(!JsonBody.contains("BlobNotFound")){
                                                Gson gson = new Gson();
                                                UserModel userData = new UserModel();
                                                SharedPreferences sp2 = getSharedPreferences("UserDetails",MODE_PRIVATE);
                                                SharedPreferences.Editor editor2 = sp2.edit();
                                                editor2.putString("UserJson",JsonBody);
                                                editor2.apply();
                                                userData = gson.fromJson(JsonBody, UserModel.class);
                                                SharedPreferences sp = getSharedPreferences("loginCheck",MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sp.edit();
                                                editor.putInt("loginType",1);
                                                editor.putBoolean("checkLogin",true);
                                                editor.apply();
                                                SharedPreferences sharedPreferences = getSharedPreferences("banners",Context.MODE_PRIVATE);
                                                SharedPreferences.Editor editor1 = sharedPreferences.edit();
                                                editor1.putInt("updateReq",0);
                                                Calendar calendar = Calendar.getInstance();
                                                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                                                editor1.putInt("lastUpdateDate",dayOfMonth);
                                                editor1.apply();
                                                updateUI(user);
                                            }
                                            else {
                                                localFile= new File(getFilesDir(),"template.txt");
                                                storage.getReference("template").child("template.txt").getFile(localFile).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                                                        new Thread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                try {

                                                                    UserModel userModel = new UserModel(user.getDisplayName(), user.getEmail(), 0.0, 0.0, new ArrayList<>(), new HashMap<>());
                                                                    Gson gson = new Gson();
                                                                    String jsonTemp = gson.toJson(userModel);
                                                                    SharedPreferences sp2 = getSharedPreferences("UserDetails",MODE_PRIVATE);
                                                                    SharedPreferences.Editor editor2 = sp2.edit();
                                                                    editor2.putString("UserJson",jsonTemp);
                                                                    editor2.apply();
                                                                    File file = new File(getFilesDir(), user.getEmail().replace(".", ""));
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
                                                                        container = blobClient.getContainerReference("users");
                                                                        CloudBlockBlob blob = container.getBlockBlobReference(user.getEmail().replace(".", "") + ".txt");

                                                                        blob.uploadFromFile(localFile.getAbsolutePath());
                                                                        Log.d("DataUploadToAzure", "Done");

                                                                        SharedPreferences sp = getSharedPreferences("loginCheck",MODE_PRIVATE);
                                                                        SharedPreferences.Editor editor = sp.edit();

                                                                        editor.putInt("loginType",1);
                                                                        editor.putBoolean("checkLogin",true);
                                                                        editor.apply();
                                                                        updateUI(user);


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

                                        }
                                    });
                                }catch (Exception e){
                                    e.printStackTrace();

                                }*/






                                // ArrayList<clothes_model> Products = new ArrayList<>();


                                /*firebaseFirestore.collection(user.getEmail()).document("UserInfo").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        Double login_state = documentSnapshot.getDouble("login_state");
                                        try {
                                            if (login_state == 1.0) {
                                                updateUI(user);
                                            }
                                        }catch (Exception e){
                                            Log.d("LoginExceptionLine179",e+" ");
                                            UserModel userInfo = new UserModel(user.getDisplayName(),user.getEmail(),null,0.0,1.0,1.0,0.0);

                                            firebaseFirestore.collection(user.getEmail()).document("UserInfo").set(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Log.d("dataStore","done");

                                                }
                                            });
                                        }
                                    }
                                });*/
                                /*firebaseFirestore.collection(user.getEmail()).addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                        List userData = value.toObjects(UserModel.class);
                                        UserModel userDetails =(UserModel) userData.get(0);
                                        if(userDetails.login_state==0){
                                            UserModel userInfo = new UserModel(user.getDisplayName(),user.getEmail(),null,0.0,1.0,1.0,0.0);
                                            firebaseFirestore.collection(user.getEmail()).document("UserInfo").set(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Log.d("dataStore","done");
                                                }
                                            });
                                        }
                                    }
                                });*/


                            } else {
                                // If sign in fails, display a message to the user.
                                Log.d("sign in fail", "signInWithCredential:failure", task.getException());
                                Toast.makeText(Login_page.this, "Login Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else{
            net.isNetworkNotAvailableResult(this);
        }

    }




}
