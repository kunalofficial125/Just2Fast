package com.just2fast.ushop;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> productImg;
    public ListAdapter(ArrayList<String> productImg, Context context){
        this.context = context;
        this.productImg = productImg;
    }
    @Override
    public int getCount() {
        return productImg.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    ImageView imageView;
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v=LayoutInflater.from(context).inflate(R.layout.productimagelayout,viewGroup,false);
        /*imageView = v.findViewById(R.id.imageView);
        Picasso.get().load(productImg.get(i)).into(imageView);*/
        return v;
    }
}
