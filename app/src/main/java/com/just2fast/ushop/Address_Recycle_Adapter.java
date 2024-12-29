package com.just2fast.ushop;

import android.app.Activity;
import android.content.Context;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;


import com.google.gson.Gson;


import java.util.ArrayList;

public class Address_Recycle_Adapter extends RecyclerView.Adapter<Address_Recycle_Adapter.Viewholder>{

    Context context;
    TextView name,address,switching;
    int flag,NavFlag;
    TextView phone;
    Button edit;
    ArrayList<Address_Model> addresses;
    ArrayList<ProductModel> product;
    Activity parent;
    ArrayList<OrderDetails> orderDetails;
    Activity activity;

//    public Address_Recycle_Adapter(ArrayList<Address_Model> addresses, Context context, Activity activity,int flag){
//        this.addresses = addresses;
//        this.context = context;
//        this.flag = flag;
//        this.activity = activity;
//    }
    public Address_Recycle_Adapter(ArrayList<Address_Model> addresses, Context context, Activity activity, ArrayList<OrderDetails> orderDetails, int flag, ArrayList<ProductModel> product, Activity parent, int NavFlag){
        this.addresses = addresses;
        this.context = context;
        this.flag = flag;
        this.orderDetails = orderDetails;
        this.NavFlag = NavFlag;
        this.product = product;
        this.activity = activity;
        this.parent =parent;
    }

    public class Viewholder extends  RecyclerView.ViewHolder{

        TextView name,address;
        TextView phone,switching;
        Button edit;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.address);
            edit = itemView.findViewById(R.id.edit);
            phone = itemView.findViewById(R.id.phone);
            switching = itemView.findViewById(R.id.switching);
        }
    }

    @NonNull
    @Override
    public Address_Recycle_Adapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.savedaddresseslayout,parent,false);
        name = itemView.findViewById(R.id.name);
        address = itemView.findViewById(R.id.address);
        edit = itemView.findViewById(R.id.edit);
        phone = itemView.findViewById(R.id.phone);
        switching = itemView.findViewById(R.id.switching);
        return new Viewholder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull Address_Recycle_Adapter.Viewholder holder, int position) {
        holder.name.setText(addresses.get(position).name);
        int id = holder.getAbsoluteAdapterPosition();
        int pos = position;
        String fullAddresses = addresses.get(position).house+" "+addresses.get(position).road+" "+
                addresses.get(position).lastAddress+" ";
        holder.address.setText(fullAddresses);
        holder.phone.setText(addresses.get(position).ph);
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                if(flag==1){
                    bundle.putParcelableArrayList("list",product);
                }
                if(NavFlag == 1){
                    bundle.putParcelableArrayList("jsonTemp",orderDetails);
                    bundle.putInt("NavFlag",1);
                }

                bundle.putInt("flag",flag);
                bundle.putInt("Position",id);
                bundle.putInt("REQUEST_CODE",100);
                bundle.putString("name",addresses.get(id).name);
                bundle.putString("ph",addresses.get(id).ph);
                bundle.putString("house",addresses.get(id).house);
                bundle.putString("road",addresses.get(id).road);

                bundle.putString("AddressId",addresses.get(id).AddressId);
                Log.d("position",id+" ");
                try{
                    ((MainActivity)parent).runFragment(bundle,new AddAddress(),"Edit Address");
                }
                catch (Exception e){
                    ((search_instance)parent).runFragment(bundle,new AddAddress(),4);
                }
            }
        });



        if(flag==1){
            switching.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("list",product);
                    bundle.putString("Address",fullAddresses);
                    bundle.putString("name",addresses.get(pos).name);
                    bundle.putString("ph",addresses.get(holder.getAbsoluteAdapterPosition()).ph);
                    if(NavFlag==1){
                        if(parent instanceof MainActivity){
                            bundle.putParcelableArrayList("jsonTemp",orderDetails);
                            bundle.putInt("NavFlag",1);
                            Gson gson1 = new Gson();
                            String a = gson1.toJson(orderDetails);
                            Log.d("a",a+"");
                            ((MainActivity)parent).runFragment(bundle,new payment_method(),"Payment Method");
                        }
                    }
                    else {
                        try {
                            ((MainActivity)parent).runFragment(bundle,new BuyingDetails(),"");

                        }
                        catch (Exception e){
                            ((search_instance)parent).runFragment(bundle,new BuyingDetails(),2);
                        }
                    }
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return addresses.size();
    }
}
