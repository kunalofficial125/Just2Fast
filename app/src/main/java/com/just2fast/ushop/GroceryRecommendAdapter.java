package com.just2fast.ushop;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class GroceryRecommendAdapter extends RecyclerView.Adapter<GroceryRecommendAdapter.Viewholder> {

    Context context;
    ArrayList<ProductModel> product;

    String jsonTemp;
    Gson gson =  new Gson();
    String userLat,userLong;
    int noOfProducts =1;

    int noOfGroceryProducts;
    int newQty;
    ArrayList<OrderDetails> list = new ArrayList<>();
    int totalAmount =0;


    public GroceryRecommendAdapter(Context context, ArrayList<ProductModel> product){
        this.context = context;
        this.product = product;
    }


    @NonNull
    @Override
    public GroceryRecommendAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.grocery_rec_recycler,parent,false);
        return new Viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GroceryRecommendAdapter.Viewholder holder, int position) {



        SharedPreferences userLatLong = context.getSharedPreferences("userLatLong",Context.MODE_PRIVATE);
        userLat = userLatLong.getString("lat","null");
        userLong = userLatLong.getString("long","null");

        String titleTemp = product.get(position).title;

        int p = titleTemp.indexOf("--");

        if(p!=-1){
            holder.title.setText(product.get(position).title.substring(0,p));
        }
        else{
            holder.title.setText(product.get(position).title);
        }

        holder.price.setText("₹"+product.get(position).sellingPrice);
        Picasso.get().load(product.get(position).Images.get(0)).into(holder.productImg);
        holder.size.setText(product.get(position).size.get(0));
        int clickedPosition = holder.getAbsoluteAdapterPosition();
        SharedPreferences sharedPreferences1 = context.getSharedPreferences("Grocery", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
        String resJson = sharedPreferences1.getString("groceryList", "null");
        ArrayList<OrderDetails> ad = new ArrayList<>();
        ad = gson.fromJson(resJson,new TypeToken<ArrayList<OrderDetails>>() {}.getType());
        String currentProductId = product.get(clickedPosition).productId;
        if(!resJson.equals("null")){
            for (int i=0;i<ad.size();i++){
                if(currentProductId.equals(ad.get(i).product.get(0).productId)){
                    holder.addBtn.setVisibility(View.INVISIBLE);
                    holder.linearLayout6.setVisibility(View.VISIBLE);
                    holder.countItems.setText(ad.get(i).quantity+"");
                    break;
                }
            }
        }


        holder.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                editor1.putInt("noOfProducts",1);
                editor1.apply();
                int noOfProductsTemp = sharedPreferences1.getInt("groceryNoOfProducts",0);
                String a = sharedPreferences1.getString("groceryList", "null");
                Log.d("jsonBodyBB",a+"");


                if (a.equals("null")) {
                    Random random = new Random();
                    int randomNumber = 1000 + random.nextInt(9000);
                    int orderId = 10000000 + random.nextInt(90000000);
                    Log.d("ClickedPosition",clickedPosition+"");
                    int totalAmount = noOfProducts*(Integer.parseInt(product.get(clickedPosition).sellingPrice));
                    Log.d("PriceTag",totalAmount+"");
                    ArrayList<ProductModel> productTemp = new ArrayList<>();
                    productTemp.add(product.get(clickedPosition));
                    OrderDetails orderDetails = new OrderDetails(orderId+"", productTemp, product.get(clickedPosition).size.get(0), product.get(clickedPosition).color.get(0), "", "", noOfProducts + "", "", "", "", totalAmount+"", 1, randomNumber+"", "",userLat,userLong);
                    ArrayList<OrderDetails> orderDetailsList = new ArrayList<>();
                    orderDetailsList.add(orderDetails);
                    a = gson.toJson(orderDetailsList);
                    Log.d("ValueA104",a);

                    editor1.putString("OTP", randomNumber + "");
                    editor1.putInt("groceryNoOfProducts",noOfProducts);
                    editor1.putString("Order Id", orderId + "");
                }
                else {
                    List<OrderDetails> abc = new ArrayList<>();
                    Log.d("ClickedPosition",clickedPosition+"");
                    noOfProducts = Integer.parseInt(holder.countItems.getText().toString());
                    abc = gson.fromJson(a, new TypeToken<ArrayList<OrderDetails>>() {}.getType());
                    String Id = sharedPreferences1.getString("Order Id", "null");
                    int totalAmount = noOfProducts*(Integer.parseInt(product.get(clickedPosition).sellingPrice));
                    Log.d("PriceTag",totalAmount+"");
                    editor1.putInt("groceryNoOfProducts",noOfProducts+noOfProductsTemp);
                    ArrayList<ProductModel> productTemp = new ArrayList<>();
                    productTemp.add(product.get(clickedPosition));
                    OrderDetails orderDetails = new OrderDetails(Id, productTemp, product.get(clickedPosition).size.get(0), product.get(clickedPosition).color.get(0), "", "", noOfProducts + "", "", "", "", totalAmount+"", 1, sharedPreferences1.getString("OTP", "null"), "",userLat,userLong);
                    abc.add(orderDetails);
                    a = gson.toJson(abc);
                    Log.d("ValueA122",a);
                }
                Toast.makeText(context, "Item Added Successfully!", Toast.LENGTH_SHORT).show();
                editor1.putString("groceryList", a);
                Log.d("ValueA",a);
                editor1.apply();
                ((MainActivity)context).enableGroceryView();
                holder.addBtn.setVisibility(View.INVISIBLE);
                holder.linearLayout6.setVisibility(View.VISIBLE);


            }
        });




        holder.increaseItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int realPos = 0;

                String ab = sharedPreferences1.getString("groceryList", "null");
                list = gson.fromJson(ab, new TypeToken<ArrayList<OrderDetails>>() {}.getType());

                for(int i=0;i<list.size();i++){
                    if(product.get(clickedPosition).productId.equals(list.get(i).product.get(0).productId)){

                        for(int j=0;j<product.get(clickedPosition).color.size();j++){

                            if(product.get(clickedPosition).color.get(j).equals(list.get(i).color)){

                                realPos = i;

                            }

                        }

                    }
                }

                noOfProducts = Integer.parseInt(holder.countItems.getText().toString());
                noOfGroceryProducts = sharedPreferences1.getInt("groceryNoOfProducts",0);
                totalAmount = Integer.parseInt(list.get(realPos).totalAmount);
                Log.d("ClickedPos153",realPos+"");

                newQty = Integer.parseInt(holder.countItems.getText().toString());
                Log.d("countItems",newQty+"");

                if(noOfProducts!=50 && noOfProducts<product.get(holder.getAbsoluteAdapterPosition()).stock) {
                    //int noOfGroceryProducts = sharedPreferences.getInt("groceryNoOfProducts",0);
                    newQty = newQty+1;
                    holder.countItems.setText(newQty + "");
                    noOfGroceryProducts =noOfGroceryProducts+1;
                    list.get(realPos).quantity = newQty+"";

                    totalAmount  = totalAmount + Integer.parseInt(list.get(realPos).product.get(0).sellingPrice);
                    list.get(realPos).totalAmount = totalAmount+"";
                    jsonTemp = gson.toJson(list);
                    Log.d("countItems11",list.get(realPos).quantity+"");
                    Log.d("listCheckOut",jsonTemp+"");

                    editor1.putString("groceryList",jsonTemp);
                    editor1.putString("groceryListNew",jsonTemp);
                    editor1.putInt("groceryNoOfProducts",noOfGroceryProducts);
                    editor1.apply();
                    ((MainActivity)context).enableGroceryView();

                    notifyDataSetChanged();
                }
            }
        });

        holder.reduceItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int realPos = 0;

                String ab = sharedPreferences1.getString("groceryList", "null");
                list = gson.fromJson(ab, new TypeToken<ArrayList<OrderDetails>>() {}.getType());

                for(int i=0;i<list.size();i++){
                    if(product.get(clickedPosition).productId.equals(list.get(i).product.get(0).productId)){

                        for(int j=0;j<product.get(clickedPosition).color.size();j++){

                            if(product.get(clickedPosition).color.get(j).equals(list.get(i).color)){

                                realPos = i;

                            }

                        }

                    }
                }

                noOfProducts = Integer.parseInt(holder.countItems.getText().toString());
                noOfGroceryProducts = sharedPreferences1.getInt("groceryNoOfProducts",0);
                totalAmount = Integer.parseInt(list.get(realPos).totalAmount);

                newQty = Integer.parseInt(holder.countItems.getText().toString());
                Log.d("countItems",newQty+"");

                if(Integer.parseInt(list.get(realPos).quantity)>1){

                    newQty = Integer.parseInt(list.get(realPos).quantity)-1;
                    Log.d("xyzNoOf",newQty+"");
                    holder.countItems.setText(newQty+"");
                    noOfGroceryProducts =  noOfGroceryProducts-1;
                    Log.d("xyz",list.get(realPos).quantity);
                    list.get(realPos).quantity = newQty+"";
                    Log.d("xyz",list.get(realPos).quantity);
                    totalAmount  = totalAmount - Integer.parseInt(list.get(realPos).product.get(0).sellingPrice);
                    list.get(realPos).totalAmount = totalAmount+"";
                    jsonTemp = gson.toJson(list);
                    editor1.putString("groceryList",jsonTemp);
                    editor1.putString("groceryListNew",jsonTemp);
                    editor1.putInt("groceryNoOfProducts",noOfGroceryProducts);
                    editor1.apply();

                    ((MainActivity)context).enableGroceryView();

                    notifyDataSetChanged();

                } else if (Integer.parseInt(list.get(realPos).quantity) == 1) {
                    if(list.size()>1){
                        totalAmount  = totalAmount - Integer.parseInt(list.get(realPos).product.get(0).sellingPrice);
                        list.get(realPos).totalAmount = totalAmount+"";
                        list.remove(realPos);
                        jsonTemp = gson.toJson(list);
                        editor1.putString("groceryList",jsonTemp);
                        editor1.putString("groceryListNew",jsonTemp);
                    }
                    else {
                        editor1.putString("groceryList","null");
                        editor1.putString("groceryListNew","null");
                    }

                    holder.addBtn.setVisibility(View.VISIBLE);
                    holder.linearLayout6.setVisibility(View.INVISIBLE);


                    editor1.putInt("groceryNoOfProducts",noOfGroceryProducts-1);
                    editor1.apply();
                    ((MainActivity)context).enableGroceryView();
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return product.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{

        TextView title,size,price,reduceItems,increaseItems,countItems;
        Button addBtn;
        CardView linearLayout6;
        ImageView productImg;


        public Viewholder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            addBtn = itemView.findViewById(R.id.addBtn);
            linearLayout6 = itemView.findViewById(R.id.linearLayout6);
            size = itemView.findViewById(R.id.size);
            price = itemView.findViewById(R.id.price);
            productImg = itemView.findViewById(R.id.productImg);
            countItems = itemView.findViewById(R.id.countItems);
            increaseItems = itemView.findViewById(R.id.increaseItems);
            reduceItems = itemView.findViewById(R.id.reduceItems);
        }
    }
}
