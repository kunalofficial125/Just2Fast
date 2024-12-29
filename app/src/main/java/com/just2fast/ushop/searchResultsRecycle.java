package com.just2fast.ushop;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class searchResultsRecycle extends RecyclerView.Adapter<searchResultsRecycle.Viewholder> {

    Context context;
    ImageView imageView;
    TextView title,starAverage,desc;
    TextView mrp;
    TextView sp;
    ArrayList<ProductModel> lists;
    Fragment fragment;

    public searchResultsRecycle(Context context, ArrayList<ProductModel> lists, Fragment fragment){
        this.lists  = lists;
        this.context = context;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public searchResultsRecycle.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.results,parent,false);
        imageView = v.findViewById(R.id.productImage);
        title = v.findViewById(R.id.title2);
        starAverage = v.findViewById(R.id.rating);
        mrp = v.findViewById(R.id.mrp);
        sp = v.findViewById(R.id.sp);
        desc = v.findViewById(R.id.desc);
        return new Viewholder(v);
    }
    int y = 0;

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        SharedPreferences preferences1 = context.getSharedPreferences("Grocery",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences1.edit();


        String imgUri = lists.get(position).Images.get(0);
        ProductModel selectedProduct = lists.get(position);
        ArrayList<ProductModel> selectedItems = new ArrayList<>();

        int p = lists.get(position).title.indexOf("--");
        String productTitle;
        if(p!=-1){
            productTitle = lists.get(position).title.substring(0,p);
        }
        else {
            productTitle = lists.get(position).title;
        }
        
        String mrp = lists.get(position).mrp;
        String sp = lists.get(position).sellingPrice;
        double discount = (Double.parseDouble(sp)/Double.parseDouble(mrp))*100;
        Picasso.get().load(imgUri).into(imageView);
        holder.mrp.setText("₹"+sp);
        holder.sp.setText(((100.000000-discount+"").substring(0,4))+"%off");
        holder.title.setText(productTitle);




        Log.d("stockInSearchResults",lists.get(0).sellingPrice+"");


        int TotalStars = Integer.parseInt(lists.get(position).star1.replace("E"+lists.get(position).productId,"")) + Integer.parseInt(lists.get(position).star2.replace("D"+lists.get(position).productId,"")) +Integer.parseInt(lists.get(position).star3.replace("C"+lists.get(position).productId,"")) +
                Integer.parseInt(lists.get(position).star4.replace("B"+lists.get(position).productId,"")) + Integer.parseInt(lists.get(position).star5.replace("A"+lists.get(position).productId,"")) ;
        Log.d("totalStar",TotalStars+"");

        double StarAverage =(double)  (Integer.parseInt(lists.get(position).star1.replace("E"+lists.get(position).productId,"")) + (Integer.parseInt(lists.get(position).star2.replace("D"+lists.get(position).productId,""))*2) +(Integer.parseInt(lists.get(position).star3.replace("C"+lists.get(position).productId,""))*3) +
                (Integer.parseInt(lists.get(position).star4.replace("B"+lists.get(position).productId,""))*4) + (Integer.parseInt(lists.get(position).star5.replace("A"+lists.get(position).productId,""))*5))/TotalStars;
        Log.d("totalStar",StarAverage+"");

        if(StarAverage==0 || Double.isNaN(StarAverage)){
            starAverage.setText(("0.0"));
        }
        else if((StarAverage+"").length()>3){
            starAverage.setText(((StarAverage+"").substring(0,3)));
        }
        else if ((StarAverage+"").length()==1){
            starAverage.setText((StarAverage+""));
            starAverage.setPadding(50,0,0,0);
        }
        else {
            starAverage.setText((StarAverage+""));
        }

        if(lists.get(position).description.length()>32){
            holder.desc.setText(lists.get(position).description.substring(0,32)+"...");
        }
        else{
            holder.desc.setText(lists.get(position).description);
        }

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedItems.add(selectedProduct);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("list",selectedItems);

                if(context instanceof  search_instance){
                    ((search_instance)context).runFragment(bundle,new BuyPage(),0);

                    editor.putInt("ss",((SearchResults)fragment).gridLayoutManager.findFirstVisibleItemPosition());
                    editor.apply();

                }else {
                    ((MainActivity)context).runFragment(bundle,new BuyPage(),"BuyPage");

                    editor.putInt("ss",((home)fragment).ss.getScrollY());
                    editor.apply();

                }
            }
        };

        holder.imageView.setOnClickListener(onClickListener);
        holder.desc.setOnClickListener(onClickListener);
        holder.title.setOnClickListener(onClickListener);
        holder.sp.setOnClickListener(onClickListener);
        holder.starAverage.setOnClickListener(onClickListener);
        holder.mrp.setOnClickListener(onClickListener);

    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView title;
        TextView mrp,starAverage;
        TextView sp,desc;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.productImage);
            title = itemView.findViewById(R.id.title2);
            mrp = itemView.findViewById(R.id.mrp);
            desc = itemView.findViewById(R.id.desc);
            starAverage = itemView.findViewById(R.id.rating);
            sp = itemView.findViewById(R.id.sp);
        }
    }
    public void addItems(List<ProductModel> items) {
        int start = getItemCount();
        lists.addAll(items);
        notifyItemRangeInserted(start, items.size());
    }
}
