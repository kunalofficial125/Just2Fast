package com.just2fast.ushop;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GroceryListAdapter extends RecyclerView.Adapter<GroceryListAdapter.ViewHolder> {

    Context context;
    ArrayList<OrderDetails> list;
    String jsonTemp;
    Gson gson =  new Gson();
    int noOfProducts;
    SharedPreferences sharedPreferences;
    int noOfGroceryProducts;
    int newQty;
    int totalAmount =0;
    SharedPreferences.Editor sp;
    Fragment fragment;

    public GroceryListAdapter(Context context, ArrayList<OrderDetails> list, SharedPreferences.Editor sp, SharedPreferences sharedPreferences, Fragment fragment){
        this.context = context;
        this.list = list;
        this.sp = sp;
        this.fragment = fragment;
        this.sharedPreferences = sharedPreferences;
    }


    @NonNull
    @Override
    public GroceryListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.grocery_list_layout,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GroceryListAdapter.ViewHolder holder, int position) {
        String ImgUri = "";
        String c = gson.toJson(list);
        Log.d("dataCheckOut",c+"");


        /*SharedPreferences sharedPreferences = context.getSharedPreferences("Grocery", Context.MODE_PRIVATE);
        SharedPreferences.Editor sp = sharedPreferences.edit();
        String abc = sharedPreferences.getString("groceryList", "null");
        list = gson.fromJson(abc, new TypeToken<ArrayList<OrderDetails>>() {}.getType());*/
        holder.countItems.setText(list.get(position).quantity+"");


        noOfProducts = Integer.parseInt(holder.countItems.getText().toString());
        noOfGroceryProducts = sharedPreferences.getInt("groceryNoOfProducts",0);
        totalAmount = Integer.parseInt(list.get(position).totalAmount);
         int clickedPosition = holder.getAbsoluteAdapterPosition();



        int posOfPriceSize = 0;

        for(int i = 0; i < list.get(clickedPosition).product.get(0).size.size(); i++){
            if(list.get(clickedPosition).size.equals(list.get(clickedPosition).product.get(0).size.get(i))){
                posOfPriceSize = i;
            }
        }


        String sellingPriceOverall = list.get(clickedPosition).product.get(0).sizePrice.get(posOfPriceSize);

         newQty = Integer.parseInt(holder.countItems.getText().toString());

        holder.increaseItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(noOfProducts!=50) {
                    //int noOfGroceryProducts = sharedPreferences.getInt("groceryNoOfProducts",0);
                     newQty = Integer.parseInt(list.get(clickedPosition).quantity)+1;
                    holder.countItems.setText(newQty + "");
                    noOfGroceryProducts =noOfGroceryProducts+1;
                    list.get(clickedPosition).quantity = newQty+"";
                    jsonTemp = gson.toJson(list);
                    if (fragment instanceof GroceryList) {
                        ((GroceryList) fragment).updateData(list);
                    }
                    sp.putString("groceryList",jsonTemp);
                    sp.putString("groceryListNew",jsonTemp);
                    sp.putInt("groceryNoOfProducts",noOfGroceryProducts);
                    sp.apply();
                    int a = (Integer.parseInt(sellingPriceOverall))*newQty;
                    Log.d("newPrice",a+"");
                    totalAmount  = totalAmount + Integer.parseInt(sellingPriceOverall);
                    list.get(clickedPosition).totalAmount = totalAmount+"";
                    holder.qty.setText("Price :"+a);
                    notifyDataSetChanged();
                }
            }
        });

        holder.reduceItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if(Integer.parseInt(list.get(clickedPosition).quantity)>1){

                     newQty = Integer.parseInt(list.get(clickedPosition).quantity)-1;
                    Log.d("xyzNoOf",newQty+"");
                    holder.countItems.setText(newQty+"");
                    noOfGroceryProducts =  noOfGroceryProducts-1;
                    Log.d("xyz",list.get(clickedPosition).quantity);
                    list.get(clickedPosition).quantity = newQty+"";
                    Log.d("xyz",list.get(clickedPosition).quantity);
                    jsonTemp = gson.toJson(list);
                    if (fragment instanceof GroceryList) {
                        ((GroceryList) fragment).updateData(list);
                    }
                    sp.putString("groceryList",jsonTemp);
                    sp.putString("groceryListNew",jsonTemp);
                    sp.putInt("groceryNoOfProducts",noOfGroceryProducts);
                    sp.apply();
                    int a = (Integer.parseInt(sellingPriceOverall))*newQty;
                    Log.d("newPrice",a+"");
                    holder.qty.setText("Price :"+a);
                    totalAmount  = totalAmount - Integer.parseInt(sellingPriceOverall);
                    list.get(clickedPosition).totalAmount = totalAmount+"";
                    notifyDataSetChanged();

                }
                else{
                        Log.d("xyz",list.get(clickedPosition).quantity);
                        Log.d("xyz",clickedPosition+"");
                        totalAmount  = totalAmount - Integer.parseInt(sellingPriceOverall);
                        list.get(clickedPosition).totalAmount = totalAmount+"";
                        list.remove(clickedPosition);
                        jsonTemp = gson.toJson(list);
                        if (fragment instanceof GroceryList) {
                            ((GroceryList) fragment).updateData(list);
                        }
                    //totalAmount  = totalAmount - Integer.parseInt(list.get(clickedPosition).product.get(0).sellingPrice.replace(list.get(clickedPosition).product.get(0).productId,""));

                    sp.putString("groceryList",jsonTemp);
                    sp.putString("groceryListNew",jsonTemp);
                    sp.putInt("groceryNoOfProducts",noOfGroceryProducts-1);
                    sp.apply();
                    notifyDataSetChanged();

                }

                if(list.isEmpty()){
                    if (context instanceof MainActivity) {
                        sp.putString("groceryList",jsonTemp);
                        sp.putString("groceryListNew",jsonTemp);
                        sp.putInt("groceryNoOfProducts",noOfGroceryProducts-1);
                        sp.apply();
                       // ((MainActivity)context).runFragment(new Bundle(),new home(),"home");

                        ((MainActivity)context).onBackPressed();
                        ((MainActivity)context).getFragmentManager().popBackStack();
                    }
                }

            }
        });



        int posOfUri = 0;
        for (int i =0;i<list.get(position).product.get(0).color.size();i++){
            Log.d("posOfUri12",list.get(position).color+"");
            Log.d("posOfUri",list.get(position).product.get(0).color.get(i)+"");
            if(list.get(position).color.equals(list.get(position).product.get(0).color.get(i)) ){
                posOfUri = i;
            }
        }
        Log.d("posOfUri",posOfUri+"");
        Picasso.get().load(list.get(position).product.get(0).colorImages.get(posOfUri)).into(holder.productImg);
        Log.d("ColorTag",list.get(position).product.get(0).color.get(posOfUri));
        holder.title.setText(list.get(position).product.get(0).title);
        Log.d("PriceTag",list.get(position).totalAmount);
        Log.d("TitleTag",list.get(position).product.get(0).title);
        int a = (Integer.parseInt(sellingPriceOverall))*newQty;
        holder.qty.setText("Price :"+a);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView productImg;
        TextView title,qty,reduceItems,increaseItems,countItems;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImg = itemView.findViewById(R.id.productImg);
            title = itemView.findViewById(R.id.title);
            qty = itemView.findViewById(R.id.qty);
            countItems = itemView.findViewById(R.id.countItems);
            increaseItems = itemView.findViewById(R.id.increaseItems);
            reduceItems = itemView.findViewById(R.id.reduceItems);
        }
    }

}
