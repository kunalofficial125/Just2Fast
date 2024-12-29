package com.just2fast.ushop;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.gson.Gson;
import com.microsoft.azure.storage.blob.CloudBlobContainer;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link edit_profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class edit_profile extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public edit_profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment edit_profile.
     */
    // TODO: Rename and change types and number of parameters
    public static edit_profile newInstance(String param1, String param2) {
        edit_profile fragment = new edit_profile();
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

    String storageConnectionString = "DefaultEndpointsProtocol=https;AccountName=tykun;AccountKey=0DPLRf2Sm3gEkXRcPLQYPT1cPjnpSuHTZY0QpQjtXPRieRAg/+" +
            "vaCycyI3otZj3tu/hPmwKp6Le5+AStW89EtQ==;EndpointSuffix=core.windows.net";
    EditText editName;
    //Button edit_img;
   // Uri imgUri;
    CloudBlobContainer blobContainer;
    ProgressBar progressBar;
    //Uri downloadImgUri;
    //FirebaseFirestore firebaseFirestore;
    FirebaseStorage storage;
    //ImageButton camera,gallery;
    Button save;
    ImageView profile_img,pfp1,pfp2,pfp3,pfp4,pfp5,pfp6;
    //int REQ_CODE;

//    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            new ActivityResultCallback<ActivityResult>() {
//                @Override
//                public void onActivityResult(ActivityResult result) {
//                    int resultCode  = result.getResultCode();
//                    Log.d("result",result.getData()+" ");
//                    if(resultCode==RESULT_OK && result.getData()!=null){
//                        if(REQ_CODE==200){
//                            imgUri = result.getData().getData();
//                            try {
//                                profile_img.setImageURI(imgUri);
//                            }
//                            catch (Exception e){
//                                Log.d("UriError","found");
//                            }
//                        }
//                        else{
//                            try {
//                                Bundle bundle = result.getData().getExtras();
//                                Bitmap bm = (Bitmap) bundle.get("data");
//                                Log.d("capture","done");
//                                imgUri = getImageUri(getActivity(),bm);
//                               // profile_img.setImageBitmap(bm);
//                                profile_img.setImageURI(imgUri);
//                            }catch (Exception e){
//                                Log.d("bitmap failed",e+" ");
//                            }
//                        }
//                    }
//                }
//            });


    int imgUpdateCode;
    UserModel userData;
    File localFile;
    FirebaseDatabase firebaseDatabase;
    String jsonTemp;
    Bundle bundle;

    Activity parent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_edit_profile, container, false);
       // edit_img = v.findViewById(R.id.edit_img);
        save = v.findViewById(R.id.save);
        editName = v.findViewById(R.id.editName);
        progressBar = v.findViewById(R.id.progressBar);
        profile_img = v.findViewById(R.id.profile_img);
        progressBar.setVisibility(View.INVISIBLE);
        pfp1 = v.findViewById(R.id.pfp1);
        parent = getActivity();
        firebaseDatabase = FirebaseDatabase.getInstance();
        pfp2 = v.findViewById(R.id.pfp2);
        pfp3 = v.findViewById(R.id.pfp3);
        storage = FirebaseStorage.getInstance();
        pfp4 = v.findViewById(R.id.pfp4);
        pfp5 = v.findViewById(R.id.pfp5);
        pfp6 = v.findViewById(R.id.pfp6);
        String nominee = getArguments().getString("name");
        editName.setText(nominee);


        pfp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable drawable = parent.getDrawable(R.drawable.pfp1);
                profile_img.setImageDrawable(drawable);
                imgUpdateCode = 1;
            }
        });
        pfp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable drawable = parent.getDrawable(R.drawable.pfp3);
                profile_img.setImageDrawable(drawable);
                imgUpdateCode = 2;
            }
        });
        pfp3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable drawable = parent.getDrawable(R.drawable.pfp5);
                profile_img.setImageDrawable(drawable);
                imgUpdateCode = 3;
            }
        });
        pfp4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable drawable = parent.getDrawable(R.drawable.pfp2);
                profile_img.setImageDrawable(drawable);
                imgUpdateCode = 4;
            }
        });
        pfp5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable drawable = parent.getDrawable(R.drawable.pfp4);
                profile_img.setImageDrawable(drawable);
                imgUpdateCode = 5;
            }
        });
        pfp6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable drawable = parent.getDrawable(R.drawable.pfp6);
                profile_img.setImageDrawable(drawable);
                imgUpdateCode = 6;
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sp2 =parent.getSharedPreferences("UserDetails",MODE_PRIVATE);
                Gson gson = new Gson();
                String json = sp2.getString("UserJson","null");
                UserModel newUserModel = gson.fromJson(json,UserModel.class);
                newUserModel.name = editName.getText().toString();

                firebaseDatabase.getReference().child("Category").child("UsersData").child(user.getEmail().replace(".","")).setValue(newUserModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        SharedPreferences sp = parent.getSharedPreferences("profile",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor  = sp.edit();
                        editor.putInt("profileUpdateReq",1);
                        editor.putInt("ImgUpdateCode",imgUpdateCode);
                        editor.apply();

                        parent.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                ((MainActivity) parent).runFragment(new Bundle(), new profile(),"Profile");
                            }
                        });
                    }
                });


                /*String Url = "https://tykun.blob.core.windows.net/users/"+user.getEmail().replace(".","")+".txt";

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(Url)
                        .build();

                progressBar.setVisibility(View.VISIBLE);
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
                            jsonTemp = JsonBody.replace(userData.name,editName.getText().toString());

                            localFile= new File(parent.getFilesDir(),"template.txt");
                            storage.getReference("template").child("template.txt").getFile(localFile).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {

                                                // UserModel userModel = new UserModel(user.getDisplayName(), user.getEmail(), 0.0, 0.0, new ArrayList<>());
                                                File file = new File(parent.getFilesDir(), user.getEmail().replace(".", ""));
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
                                                    SharedPreferences sp = parent.getSharedPreferences("profile",Context.MODE_PRIVATE);
                                                    SharedPreferences.Editor editor  = sp.edit();
                                                    editor.putInt("profileUpdateReq",1);
                                                    editor.putInt("ImgUpdateCode",imgUpdateCode);
                                                    editor.apply();

                                                    parent.runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {

                                                            ((MainActivity) parent).runFragment(new Bundle(), new profile(),"Profile");
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

        });









        /*storage = FirebaseStorage.getInstance();
        getParentContext  = getActivity().getApplicationContext();
        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        SharedPreferences sp = getActivity().getSharedPreferences("loginCheck",Context.MODE_PRIVATE);
        loginType = sp.getInt("loginType",0);
        if(loginType==0){
            userContact =  user.getPhoneNumber();
            firebaseFirestore.collection(user.getPhoneNumber()).document("UserInfo").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    profile_img_state = documentSnapshot.getDouble("profile_img_state");
                    String profile_img_url = documentSnapshot.getString("profile_img_url");
                    if(profile_img_state==1.0) {
                        //Glide.with(getParentContext).load(profile_img_url).into(profile_img);
                        Picasso.get().load(profile_img_url).placeholder(R.drawable.loading).into(profile_img);
                    }
                }
            });
        }
        else {
            userContact = user.getEmail();
            firebaseFirestore.collection(user.getEmail()).document("UserInfo").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    profile_img_state = documentSnapshot.getDouble("profile_img_state");
                    String profile_img_url = documentSnapshot.getString("profile_img_url");
                    if(profile_img_state==1.0){
                        //Glide.with(getParentContext).load(profile_img_url).into(profile_img);
                        Picasso.get().load(profile_img_url).placeholder(R.drawable.loading).into(profile_img);
                    }
                }
            });
        }
        edit_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.take_img_dial+og);
                dialog.setCancelable(true);
                dialog.show();
                camera = dialog.findViewById(R.id.camera);
                gallery = dialog.findViewById(R.id.gallery);
                camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        REQ_CODE =100;
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        activityResultLauncher.launch(intent);
                        dialog.dismiss();
                    }
                });
                gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        REQ_CODE = 200;
                        Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        activityResultLauncher.launch(intent);
                        dialog.dismiss();
                    }
                });
            }
        });
        reference = storage.getReference(userCont+act).child("profile_img");
        String name = getArguments().getString("name");

        editName.setText(name);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String update_name = editName.getText().toString();
                if (update_name.length() < 5) {
                    Toast.makeText(getParentContext, "Enter Name More Than 5 letters", Toast.LENGTH_SHORT).show();
                } else if (update_name.length()>14) {
                    Toast.makeText(getParentContext, "Enter Name Less Than 14 Letters", Toast.LENGTH_SHORT).show();

                } else {
                    if (imgUri != null) {
                        progressBar.setVisibility(View.VISIBLE);
                        reference.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Log.d("filePut", "done");
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Log.d("DownloadSuccess", "uriDownload");
                                        //UserModel userModel = new UserModel(editName.getText().toString(),user.getEmail()+" ",user.getPhoneNumber()+" ",1,uri.toString());
                                        downloadImgUri = uri;
                                        Log.d("downloadImgUri", downloadImgUri + " ");
                                        firebaseFirestore.collection(userContact).document("UserInfo").update("name", editName.getText().toString(), "profile_img_url", downloadImgUri.toString() + " ", "profile_img_state", 1);
                                        Log.d("byImg", "done");
                                        Log.d("downloadImgUri2", downloadImgUri + " ");
                                        imgUpdateCode = 1;
                                        progressBar.setVisibility(View.INVISIBLE);
                                    }
                                });
                            }
                        });

                    }
                    if (imgUpdateCode == 0) {
                        firebaseFirestore.collection(userContact).document("UserInfo").update("name", editName.getText().toString());
                        Log.d("byInfo", "done");
                        Log.d("downloadImgUri3", downloadImgUri + " ");
                    }
                    ((MainActivity) getActivity()).runFragment(new Bundle(), new profile(),"PROFILE");


                }
            }
        });*/

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

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}