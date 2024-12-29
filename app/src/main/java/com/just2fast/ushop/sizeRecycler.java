package com.just2fast.ushop;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;



public class sizeRecycler extends RecyclerView.Adapter<sizeRecycler.ViewHolder> {

    Context context;
    ArrayList<Integer>  countTaps = new ArrayList<>();
    RecyclerView recyclerView;
    ArrayList<String> sizes;
    Fragment parentFrag;
    ArrayList<String> sizePrice;
    //TextView sizeText;


    public sizeRecycler(Context context, ArrayList<String> sizes, ArrayList<String> sizePrice, RecyclerView recyclerView, Fragment parentFrag){
        this.context = context;
        this.sizes = sizes;
        this.sizePrice = sizePrice;
        this.parentFrag = parentFrag;
        this.recyclerView = recyclerView;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView sizeText;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sizeText =  itemView.findViewById(R.id.sizeText);
            cardView = itemView.findViewById(R.id.sizeCardView);
        }
    }

    @NonNull
    @Override
    public sizeRecycler.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.sizelayout,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull sizeRecycler.ViewHolder holder, int position) {


        holder.sizeText.setText(sizes.get(position));

        int beforeColorValue = ContextCompat.getColor(context, R.color.grey);
        int afterColorValue = context.getResources().getColor(R.color.reddish);



        //holder.cardView.setCardBackgroundColor(beforeColorValue);

        holder.cardView.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.grey));

        SharedPreferences sp = context.getSharedPreferences("BuyValues", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        int pos = position;
        if(pos==0){
            holder.cardView.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.reddish));
            countTaps.add(0);

            editor.putString("ChooseSize",sizes.get(pos));
            editor.putString("ChosenSizePrice",sizePrice.get(pos));
            editor.apply();
            if(!sizePrice.get(0).equals("null")){
                ((BuyPage)parentFrag).currentProduct.sellingPrice = sizePrice.get(pos)+((BuyPage)parentFrag).currentProduct.productId;
                Log.d("sizeChosenPrice",((BuyPage)parentFrag).currentProduct.sellingPrice+" ");
                ((BuyPage)parentFrag).sellingPriceTextView.setText(("₹"+((BuyPage)parentFrag).currentProduct.sellingPrice));
            }
            Log.d("sizeChosen",sizes.get(pos)+" ");

        }
        if(pos!=0){
            holder.cardView.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.grey));

        }



        holder.sizeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                holder.cardView.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.reddish));

                for(int i=0;i<countTaps.size();i++){
                    if(pos!=countTaps.get(i)){
                        CardView previousCard =(CardView) recyclerView.findViewHolderForAdapterPosition(countTaps.get(i)).itemView.findViewById(R.id.sizeCardView);
                        previousCard.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.grey));

                    }
                }
                countTaps.clear();
                countTaps.add(pos);

                editor.putString("ChooseSize",sizes.get(pos));
                editor.putString("ChosenSizePrice",sizePrice.get(pos));
                editor.apply();
                if(!sizePrice.get(0).equals("null")){
                    ((BuyPage)parentFrag).currentProduct.sellingPrice = sizePrice.get(pos);
                    Log.d("sizeChosenPrice",((BuyPage)parentFrag).currentProduct.sellingPrice+" ");
                    ((BuyPage)parentFrag).sellingPriceTextView.setText(("₹"+((BuyPage)parentFrag).currentProduct.sellingPrice));
                }
                Log.d("sizeChosen",sizes.get(pos)+" ");
            }
        });
    }

    @Override
    public int getItemCount() {
        return sizes.size();
    }


}
