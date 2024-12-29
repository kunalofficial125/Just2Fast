package com.just2fast.ushop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class product_recycler extends RecyclerView.Adapter<product_recycler.Viewholder> {
    ImageView imageView;
    ArrayList<String> uris;
    Context context;

    public product_recycler(Context context, ArrayList<String> uris){
        this.uris = uris;
        this.context = context;
    }


    @NonNull
    @Override
    public product_recycler.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.productimagelayout,parent,false);
        imageView = itemView.findViewById(R.id.imageView);
        return new Viewholder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull product_recycler.Viewholder holder, int position) {
        String imgUri = uris.get(position);

       Picasso.get().load(imgUri).into(imageView);

    }

    @Override
    public int getItemCount() {
        return uris.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder{
        ImageView imageView ;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            imageView =  itemView.findViewById(R.id.imageView);
        }
    }
}
