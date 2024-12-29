package com.just2fast.ushop;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BannerRecycler extends RecyclerView.Adapter<BannerRecycler.Viewholder> {

    Context context;
    ImageView pfp;
    TextView title;
    Activity parent;

    Button buyBtn;
    ArrayList<ProductModel> list;

    public BannerRecycler(Context context, ArrayList<ProductModel> list, Activity parent){
        this.context = context;
        this.list = list;
        this.parent = parent;

    }

    public class Viewholder extends RecyclerView.ViewHolder{
        ImageView pfp;
        TextView title;
        Button buyBtn;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            pfp = itemView.findViewById(R.id.pfp);
            title = itemView.findViewById(R.id.title);
            buyBtn = itemView.findViewById(R.id.buybtn);
        }
    }

    @NonNull
    @Override
    public BannerRecycler.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.banners,parent,false);
        pfp = v.findViewById(R.id.pfp);
        return new Viewholder(v);
    }

    String tempTitle;

    @Override
    public void onBindViewHolder(@NonNull BannerRecycler.Viewholder holder, int position) {
        int pos = position;
        int p = list.get(position).title.indexOf("--");
        if(p!=-1){
             tempTitle = list.get(position).title.substring(0,p);
        }
        else {
             tempTitle = list.get(position).title;
        }

        if(tempTitle.length()<13){
            holder.title.setText(tempTitle);
        }
        else {
            holder.title.setText(tempTitle.substring(0,12)+"...");
        }
        Picasso.get().load(list.get(position).Images.get(0)).into(pfp);

        holder.buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                ArrayList<ProductModel> selectedItems = new ArrayList<>();
                selectedItems.add(list.get(pos));
                ((MainActivity)parent).bnv.setVisibility(View.INVISIBLE);
                bundle.putParcelableArrayList("list",selectedItems);
                ((MainActivity)parent).runFragment(bundle,new BuyPage(),"Buy Page");
                list.clear();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
