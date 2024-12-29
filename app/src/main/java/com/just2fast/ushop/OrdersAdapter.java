package com.just2fast.ushop;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.Viewholder> {

    Context context;
    ImageView orderImg;
    TextView title,orderDate,status;
    ArrayList<OrderDetails> orderDetails;

    public OrdersAdapter(Context context,ArrayList<OrderDetails> orderDetails){
        this.context = context;
        this.orderDetails = orderDetails;
    }


    @NonNull
    @Override
    public OrdersAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.orders_recycler,parent,false);
        return new Viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersAdapter.Viewholder holder, int position) {
        OrderDetails od = orderDetails.get(position);
        int p = od.product.get(0).title.indexOf("--");
        if(p!=-1){
            holder.title.setText(od.product.get(0).title.substring(0,p));
        }
        else {
            holder.title.setText(od.product.get(0).title);
        }
        holder.orderDate.setText("Ordered on : "+od.orderDate);

        int primaryColor = ContextCompat.getColor(context, R.color.darkYellow);
        int completeColor = ContextCompat.getColor(context, R.color.green);
        int alertColor = ContextCompat.getColor(context, R.color.red);

        if(od.ORDER_STATUS == 1){
            holder.status.setText("Order Placed");
            holder.status.setTextColor(primaryColor);
        } else if (od.ORDER_STATUS == 2) {
            holder.status.setText("Order in Processing");
            holder.status.setTextColor(primaryColor);
        } else if (od.ORDER_STATUS == 3) {
            holder.status.setText("Out For Delivery");
            holder.status.setTextColor(primaryColor);
        }
        else if(od.ORDER_STATUS == 4){
            holder.status.setText("Delivered :)");
            holder.status.setTextColor(completeColor);
        }
        else if(od.ORDER_STATUS == 5){
            holder.status.setText("Return");
            holder.status.setTextColor(alertColor);
        }
        else if (od.ORDER_STATUS==7){
            holder.status.setText("Cancelled");
            holder.status.setTextColor(alertColor);
        }

        int posOfUri = 0;
        for (int i =0;i<orderDetails.get(position).product.get(0).color.size();i++){
            Log.d("posOfUri12",orderDetails.get(position).color+"");
            Log.d("posOfUri",orderDetails.get(position).product.get(0).color.get(i)+"");
            if(orderDetails.get(position).color.equals(orderDetails.get(position).product.get(0).color.get(i)) ){
                posOfUri = i;
            }
        }
        Log.d("posOfUri",posOfUri+"");
        Picasso.get().load(orderDetails.get(position).product.get(0).colorImages.get(posOfUri)).into(holder.orderImg);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<OrderDetails> list = new ArrayList<>();
                list.add(od);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("list",list);
                bundle.putInt("buyPageNavigation",1);
                ((MainActivity)context).runFragment(bundle,new Confirm_order_details(),"Your Orders");
            }
        };

        holder.cl.setOnClickListener(onClickListener);
        holder.status.setOnClickListener(onClickListener);
        holder.title.setOnClickListener(onClickListener);
        holder.orderDate.setOnClickListener(onClickListener);
        holder.orderImg.setOnClickListener(onClickListener);

    }

    @Override
    public int getItemCount() {
        return orderDetails.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder{

        ImageView orderImg;
        TextView title,orderDate,status;
        ConstraintLayout cl;
        public Viewholder(@NonNull View v) {
            super(v);
            orderImg = v.findViewById(R.id.orderImg);
            orderDate = v.findViewById(R.id.orderDate);
            title = v.findViewById(R.id.title);
            status = v.findViewById(R.id.status);
            cl = v.findViewById(R.id.cl);
        }
    }
}
