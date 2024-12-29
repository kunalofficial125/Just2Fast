package com.just2fast.ushop;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.microsoft.azure.storage.blob.CloudBlobContainer;

import java.io.File;

public class Database {
    ProductModel[]products;
    File localFile;
    String storageConnectionString = "DefaultEndpointsProtocol=https;AccountName=tykun;AccountKey=0DPLRf2Sm3gEkXRcPLQYPT1cPjnpSuHTZY0QpQjtXPRieRAg/+" +
            "vaCycyI3otZj3tu/hPmwKp6Le5+AStW89EtQ==;EndpointSuffix=core.windows.net";







    CloudBlobContainer container;
    String jsonTemp;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    String Url;

    public void updateProduct(Context context, String Category, ProductModel currentProduct, String star, int starNo, String ProductId){
        user = FirebaseAuth.getInstance().getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
       DatabaseReference reference =  firebaseDatabase.getReference().child("Category").child("products").child(Category.toLowerCase());

       reference.orderByChild("productId").equalTo(ProductId).addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {

               for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                   ProductModel productModel = childSnapshot.getValue(ProductModel.class);

                   Log.d("modelStar", "EntryDone2332");
                   if (productModel != null) {
                       Log.d("modelStar", "EntryDone");
                       if (starNo == 1) {
                           productModel.star1 = (Integer.parseInt(productModel.star1) + 1) + "";
                           Log.d("modelStar", productModel.star1);
                       } else if (starNo == 2) {
                           productModel.star2 = (Integer.parseInt(productModel.star1) + 1) + "";
                       } else if (starNo == 3) {
                           productModel.star3 = (Integer.parseInt(productModel.star1) + 1) + "";
                       } else if (starNo == 4) {
                           productModel.star4 = (Integer.parseInt(productModel.star1) + 1) + "";
                       } else if (starNo == 5) {
                           productModel.star5 = (Integer.parseInt(productModel.star1) + 1) + "";
                       }

                       childSnapshot.getRef().setValue(productModel);

                       firebaseDatabase.getReference().child("Category").child("SellerData").child(productModel.sellerId).child("Products").orderByChild("productId").equalTo(productModel.productId).addListenerForSingleValueEvent(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot snapshot) {
                               for (DataSnapshot childSnapshot : snapshot.getChildren()) {

                                   if (starNo == 1) {
                                       childSnapshot.getRef().child("star1").setValue(productModel.star1);
                                   } else if (starNo == 2) {
                                       childSnapshot.getRef().child("star2").setValue(productModel.star2);
                                   } else if (starNo == 3) {
                                       childSnapshot.getRef().child("star3").setValue(productModel.star3);
                                   } else if (starNo == 4) {
                                       childSnapshot.getRef().child("star4").setValue(productModel.star4);
                                   } else if (starNo == 5) {
                                       childSnapshot.getRef().child("star5").setValue(productModel.star5);
                                   }

                               }
                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError error) {

                           }
                       });

                   }

               }


           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });

        /*localFile =  new File(context.getFilesDir(),"template.txt");
        FirebaseStorage storage = FirebaseStorage.getInstance();

        Url = "https://tykun.blob.core.windows.net/categories/"+Category+".txt";

        // ArrayList<clothes_model> Products = new ArrayList<>();
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

                Log.d("currentStar",currentProduct.star1);
                if(starNo==1){
                    String a = (Integer.parseInt(currentProduct.star1.replace("E"+ProductId,""))+1)+"E"+ProductId;
                    Log.d("replacementData",a);
                    jsonTemp =JsonBody.replace(currentProduct.star1,a);
                } else if (starNo==2) {
                    jsonTemp = JsonBody.replace(currentProduct.star2,(Integer.parseInt(currentProduct.star2.replace("D"+ProductId,""))+1)+"D"+ProductId);
                }
                else if (starNo==3) {
                 jsonTemp = JsonBody.replace(currentProduct.star3,(Integer.parseInt(currentProduct.star3.replace("C"+ProductId,""))+1)+"C"+ProductId);
                }
                else if (starNo==4) {
                    jsonTemp = JsonBody.replace(currentProduct.star4,(Integer.parseInt(currentProduct.star4.replace("B"+ProductId,""))+1)+"B"+ProductId);
                }
                else if (starNo==5) {
                    jsonTemp = JsonBody.replace(currentProduct.star5,(Integer.parseInt(currentProduct.star5.replace("A"+ProductId,""))+1)+"A"+ProductId);
                }
                Log.d("JsonBody",jsonTemp);


                storage.getReference("template").child("template.txt").getFile(localFile).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    writingJsonBody(localFile, jsonTemp,1);

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
                                        CloudBlockBlob blob = container.getBlockBlobReference(Category+".txt");

                                        blob.uploadFromFile(localFile.getAbsolutePath());
                                        Log.d("DataUploadToAzure","Done");


                                    } catch (URISyntaxException | StorageException e) {
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
        });*/


    }


}
