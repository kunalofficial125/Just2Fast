package com.just2fast.ushop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import okhttp3.OkHttpClient;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.ArrayList;

import java.util.List;
import java.util.Scanner;

import okhttp3.Request;
import okhttp3.Call;
import okhttp3.Callback;

import okhttp3.Response;

import com.google.gson.Gson;


public class JsonDemo extends AppCompatActivity {
    FirebaseStorage storage;
    CloudBlobContainer container;
    String JsonBody2 ="";
    ProgressBar progressBar6;
    File localFile;
    String storageConnectionString = "DefaultEndpointsProtocol=https;AccountName=tykun;AccountKey=0DPLRf2Sm3gEkXRcPLQYPT1cPjnpSuHTZY0QpQjtXPRieRAg/+" +
            "vaCycyI3otZj3tu/hPmwKp6Le5+AStW89EtQ==;EndpointSuffix=core.windows.net";
    String Url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json_demo);
        storage = FirebaseStorage.getInstance();
        progressBar6 = findViewById(R.id.progressBar6);
        progressBar6.setVisibility(View.VISIBLE);





        Url = "https://tykun.blob.core.windows.net/categories/tshirts.txt";
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
                         Log.d("JsonBody",JsonBody+" ");

                         runOnUiThread(() -> {
                             TextView textView18 = findViewById(R.id.textView18);

                             Gson gson = new Gson();


                             Person []person = gson.fromJson(JsonBody,Person[].class);

                             if (person.length > 0) {
                                 List<String> sizes = new ArrayList<>();
                                 sizes.add("XSSS");
                                 sizes.add("XLLLLL");
                                 sizes.add("PM");

                                 //Log.d("personInfo",person[0].title);
                                // textView18.setText(sizes.get(0)+person[0].getTitle());
                                 JsonBody2 = JsonBody.replace("[\"XS\",\"XM\",\"XL\"]","[\"XSSSSS\",\"XMMMMM\",\"XLLLLL\"]");
                                // Log.d("JsonBody2",JsonBody2);


                             }


                             progressBar6.setVisibility(View.INVISIBLE);
                         });
                         try {
                             localFile = new File(getFilesDir(), "template.txt");
                             storage.getReference("template").child("template.txt").getFile(localFile).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                                 @Override
                                 public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                                     // Assuming this code is inside an appropriate method or context
                                     new Thread(new Runnable() {
                                         @Override
                                         public void run() {
                                             try {
                                                 writingJsonBody(localFile, JsonBody2,1);
                                                 InputStream inputStream = new FileInputStream(localFile);

                                                 //Upload the file to Azure Storage
                                                 CloudStorageAccount storageAccount = null;
                                                 try {
                                                     storageAccount = CloudStorageAccount.parse(storageConnectionString);
                                                 } catch (URISyntaxException | InvalidKeyException e) {
                                                     throw new RuntimeException(e);
                                                 }

                                                 CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
                                                 try {
                                                     container = blobClient.getContainerReference("categories");
                                                     CloudBlockBlob blob = container.getBlockBlobReference("tshirts.txt");

                                                     blob.uploadFromFile(localFile.getAbsolutePath());
                                                     Log.d("DataUploadToAzure","Done");


                                                 } catch (URISyntaxException | StorageException e) {
                                                     throw new RuntimeException(e);
                                                 }

                                                 // Upload the file to Firebase Storage

                                                 storage.getReference("template").child("template.txt").putStream(inputStream)
                                                         .addOnSuccessListener(taskSnapshot -> {
                                                                 Log.d("UploadTask", "Upload successful");


                                                         })
                                                         .addOnFailureListener(exception -> {
                                                             Log.e("UploadTask", "Upload failed: " + exception.getMessage());
                                                         });

                                                 // Read and log the contents of the file using a Scanner
                                                 Scanner scanner = new Scanner(localFile);
                                                 while (scanner.hasNextLine()) {
                                                     Log.d("JsonBody", scanner.nextLine() + "\n");
                                                 }

                                                 // Close the scanner
                                                 scanner.close();
                                             } catch (Exception e) {
                                                 Log.e("Exception", "An error occurred: " + e.getMessage());
                                             }
                                         }
                                     }).start();

                                 }
                             });
                         } catch (Exception e) {
                             e.printStackTrace();
                         }
                     }
                 });


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

//            'title': "Graphic Men's Tshirt",
//                    'price': 998,
//                    'size': ["S","M","L"]
}
