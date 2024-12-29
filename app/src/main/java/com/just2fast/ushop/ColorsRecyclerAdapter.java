package com.just2fast.ushop;



import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class ColorsRecyclerAdapter extends RecyclerView.Adapter<ColorsRecyclerAdapter.ViewHolder> {
    Context context;
    ArrayList<MoreColorsOfProduct> list;
    CardView colorCardView;
    ImageView colorImage;
    Fragment parentFrag;
    RecyclerView recyclerView;

    ArrayList<Integer> countTaps = new ArrayList<>();

    public ColorsRecyclerAdapter(Context context, ArrayList<MoreColorsOfProduct> list, RecyclerView recyclerView, Fragment parentFrag){
        this.context = context;
        this.recyclerView = recyclerView;
        this.list = list;
        this.parentFrag = parentFrag;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CardView colorCardView;
        ImageView colorImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            colorCardView = itemView.findViewById(R.id.colorCardView);
            colorImage = itemView.findViewById(R.id.colorImage);
        }
    }

    @NonNull
    @Override
    public ColorsRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.colorsadapterlayout,parent,false);
        colorCardView = v.findViewById(R.id.colorCardView);
        colorImage = v.findViewById(R.id.colorImage);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull ColorsRecyclerAdapter.ViewHolder holder, int position) {
        String currentColor =list.get(0).colorsNameList.get(position);
        int beforeColorValue = ContextCompat.getColor(context,R.color.white);
        SharedPreferences sp = context.getSharedPreferences("BuyValues", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        int colorValue = ContextCompat.getColor(context, R.color.red);

        int pos = position;
        if(pos==0){
            ((BuyPage)parentFrag).colorShow.setText("Color: "+currentColor);
            holder.colorCardView.setCardBackgroundColor(colorValue);
            countTaps.add(0);
            editor.putString("ChooseColor",currentColor);
            editor.apply();
            Log.d("colorChosen",currentColor+" ");
        }
        if(pos!=0){
            holder.colorCardView.setCardBackgroundColor(beforeColorValue);
        }

        Picasso.get().load(list.get(0).colorsImageList.get(position)).into(colorImage);
        holder.colorImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BuyPage)parentFrag).colorShow.setText("Color: "+currentColor);

                holder.colorCardView.setCardBackgroundColor(colorValue);


                for(int i=0;i<countTaps.size();i++){
                    if(pos!=countTaps.get(i)){
                    CardView previousCard =(CardView) recyclerView.findViewHolderForAdapterPosition(countTaps.get(i)).itemView.findViewById(R.id.colorCardView);
                    previousCard.setCardBackgroundColor(beforeColorValue);
                    }
                }
                countTaps.clear();
                countTaps.add(pos);
                editor.putString("ChooseColor",currentColor);
                editor.apply();
                Log.d("colorChosen",currentColor+" ");
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.get(0).colorsImageList.size();
    }


}
