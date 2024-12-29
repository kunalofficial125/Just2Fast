package com.just2fast.ushop;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class HistoryAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    Fragment fragment;
    ArrayList<String> arrHistory;


    public HistoryAdapter(Context context, ArrayList<String> arrHistory, Fragment fragment){
        this.context = context;
        this.arrHistory = arrHistory;
        this.fragment = fragment;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if(arrHistory.size()>4){
            return 4;
        }
        else {
            return arrHistory.size();
        }
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view  = inflater.inflate(R.layout.history_layout,null);
        TextView textView = view.findViewById(R.id.text1);
        textView.setText(arrHistory.get(i));
        ImageView imageView1 = view.findViewById(R.id.imageView1);
        imageView1.setImageResource(R.drawable.baseline_youtube_searched_for_24);
        return view;
    }
}
