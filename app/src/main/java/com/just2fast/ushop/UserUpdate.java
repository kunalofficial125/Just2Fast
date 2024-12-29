package com.just2fast.ushop;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.gson.Gson;
import com.microsoft.azure.storage.blob.CloudBlobContainer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class UserUpdate {

    File localFile;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    Context context;
    FirebaseDatabase firebaseDatabase;
    String storageConnectionString = "DefaultEndpointsProtocol=https;AccountName=tykun;AccountKey=0DPLRf2Sm3gEkXRcPLQYPT1cPjnpSuHTZY0QpQjtXPRieRAg/+" +
            "vaCycyI3otZj3tu/hPmwKp6Le5+AStW89EtQ==;EndpointSuffix=core.windows.net";
    CloudBlobContainer blobContainer;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    
    public UserUpdate(Context context){
        this.context = context;
    }
    
    public void userUpdate(UserModel userModel,String jsonTemp){

        firebaseDatabase = FirebaseDatabase.getInstance();
        Gson gson = new Gson();
//        UserModel userModel = gson.fromJson(jsonTemp,UserModel.class);
        Log.d("UserPyra",gson.toJson(userModel)+"");



        firebaseDatabase.getReference().child("Category").child("UsersData").child(user.getEmail().replace(".","")).child("myOrders").setValue(userModel.myOrders).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                SharedPreferences sharedPreferences = context.getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor1 = sharedPreferences.edit();
                editor1.putString("UserJson",jsonTemp);
                editor1.apply();
                SharedPreferences sp = context.getSharedPreferences("profile",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor  = sp.edit();
                editor.putInt("profileUpdateReq",1);
                editor.apply();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "fail", Toast.LENGTH_SHORT).show();
            }
        });



        /*localFile= new File(context.getFilesDir(),"template.txt");
        storage.getReference("template").child("template.txt").getFile(localFile).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            // UserModel userModel = new UserModel(user.getDisplayName(), user.getEmail(), 0.0, 0.0, new ArrayList<>());
                            File file = new File(context.getFilesDir(), user.getEmail().replace(".", ""));
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
                                SharedPreferences sharedPreferences = context.getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor1 = sharedPreferences.edit();
                                editor1.putString("UserJson",jsonTemp);
                                editor1.apply();
                                SharedPreferences sp = context.getSharedPreferences("profile",Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor  = sp.edit();
                                editor.putInt("profileUpdateReq",1);
                                editor.apply();





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
        });*/
    }
    
    public void getData(){


        
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
