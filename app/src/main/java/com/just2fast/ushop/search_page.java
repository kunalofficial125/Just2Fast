package com.just2fast.ushop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.reflect.TypeToken;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class search_page extends Fragment  {

    SearchView searchView;

    FirebaseDatabase firebaseDatabase;

    ImageButton backBtn;
    Activity parent;
    TextView textView19;
    ProgressBar progressBar7;
    ListView historyList;
    Gson gson = new Gson();
    TextView discover1,discover2,discover3,discover4,discover5,discover6,discover7,discover8;
    CardView cardView5,cardView3;

    ArrayList<String> uploadHistory = new ArrayList<>();
    String directQuery;
    String tempQuery;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_search_page, container, false);
        searchView = v.findViewById(R.id.searchView);
        backBtn = v.findViewById(R.id.backBtn);
        textView19 = v.findViewById(R.id.textView19);
        progressBar7 = v.findViewById(R.id.progressBar7);
        discover1 =v.findViewById(R.id.discover1);
        discover2 = v.findViewById(R.id.discover2);
        discover3 = v.findViewById(R.id.discover3);
        discover4 = v.findViewById(R.id.discover4);
        discover5 = v.findViewById(R.id.discover5);
        discover6 = v.findViewById(R.id.discover6);
        discover7 = v.findViewById(R.id.discover7);
        discover8 = v.findViewById(R.id.discover8);
        historyList = v.findViewById(R.id.historyList);
        firebaseDatabase = FirebaseDatabase.getInstance();
        //cardView3 = v.findViewById(R.id.cardView3);
        //cardView5 = v.findViewById(R.id.cardView5);
        parent  = getActivity();

        SharedPreferences sp = getActivity().getSharedPreferences("SearchHistory",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        String json = sp.getString("JsonHistory","null");
        uploadHistory  = gson.fromJson(json, new TypeToken<ArrayList<String>>(){}.getType());



         directQuery = getArguments().getString("query","null");
         SharedPreferences sharedPreferences = getActivity().getSharedPreferences("DirectQuery",Context.MODE_PRIVATE);
         SharedPreferences.Editor editor1 = sharedPreferences.edit();
         int directQueryRun = sharedPreferences.getInt("directQueryRun2",0);
        Log.d("directQuery211",directQuery);
        Log.d("directQuery210",directQueryRun+"");

        //progressBar7.setVisibility(View.VISIBLE);
        if(!directQuery.equals("null")){
            Log.d("directQuery212",directQuery);
            editor1.putInt("directQueryRun2",0);
            editor1.apply();

            SharedPreferences sp456 = parent.getSharedPreferences("query", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor21456 = sp456.edit();
            editor21456.putString("query", directQuery);
            editor21456.apply();

            searchItems(directQuery.toLowerCase());
        }

        Intent toHome = new Intent(getActivity(),MainActivity.class);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((search_instance)parent).startActivity(toHome);
                ((search_instance)parent).finish();
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                SpellChecker spellChecker = new SpellChecker(parent);
                tempQuery = query;
                // Test the spell checker and get suggestions
                 // Replace this with your word input

                SharedPreferences sp = parent.getSharedPreferences("query", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor21 = sp.edit();
                editor21.putString("query", query);
                editor21.apply();
                String [] allWords = query.split(" ");
                String CorrectedText = "";
                for (String wordToCheck : allWords) {
                    Log.d("words",wordToCheck);
                    boolean isCorrect = spellChecker.isWordCorrect(wordToCheck);
                    if (isCorrect) {
                        //Toast.makeText(getActivity(), "Correct spelling!", Toast.LENGTH_SHORT).show();
                        CorrectedText = CorrectedText +" " +wordToCheck;
                    } else {
                        List<String> suggestions = spellChecker.getSuggestions(wordToCheck);
                        if (!suggestions.isEmpty()) {
                            String suggestionText = "Did you mean: " + suggestions.get(0);
                            CorrectedText = CorrectedText +" " +suggestions.get(0);
                            //Toast.makeText(getActivity(), suggestionText, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getActivity(), "Incorrect spelling!", Toast.LENGTH_SHORT).show();
                            CorrectedText = CorrectedText +" " +wordToCheck;
                        }
                    }

                }

                //Toast.makeText(getActivity(), CorrectedText, Toast.LENGTH_SHORT).show();




                uploadHistory.add(0,CorrectedText);

//                for(int i =0;i<uploadHistory.size();i++){
//                    Log.d("History",uploadHistory.get(i)+"");
//                }



                String jsonHistory = gson.toJson(uploadHistory);
                Log.d("JsonHistory",jsonHistory);
                editor.putString("JsonHistory",jsonHistory);
                editor.apply();
                searchItems(CorrectedText.toLowerCase());

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        //String jsonHistory = sp.getString("JsonHistory","null");


        //ArrayAdapter<String> historyAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1,savedHistory);


        Set<String> hashSet = new HashSet<>(uploadHistory);
        ArrayList<String> savedHistory = new ArrayList<>(hashSet);

        HistoryAdapter historyAdapter = new HistoryAdapter(getActivity().getApplicationContext(),savedHistory,this);

        historyList.setAdapter(historyAdapter);
        historyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SharedPreferences sp = parent.getSharedPreferences("query", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("query",savedHistory.get(i));
                editor.apply();
                searchItems(savedHistory.get(i).toLowerCase());
            }
        });

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = ((TextView)v).getText().toString();
//                uploadHistory.add(0,s.toLowerCase());
//                String jsonHistory = gson.toJson(uploadHistory);
//                Log.d("JsonHistory",jsonHistory);
//                editor.putString("JsonHistory",jsonHistory);
//                editor.apply();
                SharedPreferences sp2332 = parent.getSharedPreferences("query", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor1 = sp2332.edit();
                editor1.putString("query",s);
                editor1.apply();
                searchItems(s.toLowerCase());
            }
        };

        discover1.setOnClickListener(onClickListener);
        discover2.setOnClickListener(onClickListener);
        discover3.setOnClickListener(onClickListener);
        discover4.setOnClickListener(onClickListener);
        discover5.setOnClickListener(onClickListener);
        discover6.setOnClickListener(onClickListener);
        discover7.setOnClickListener(onClickListener);
        discover8.setOnClickListener(onClickListener);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SearchView searchView = view.findViewById(R.id.searchView);
        searchView.requestFocus();
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(searchView, InputMethodManager.SHOW_IMPLICIT);
    }
    //ProductModel[] Products;
    ArrayList<ProductModel> Products = new ArrayList<>();

    String Category;
    int completeQuery = 0;


    private void searchItems(String query) {
        progressBar7.setVisibility(View.VISIBLE);
        Log.d("queryLog",query);
        Category = "null";

        SharedPreferences sp1225 = parent.getSharedPreferences("Category List",Context.MODE_PRIVATE);
        String catString = sp1225.getString("Category List","null");
        FetchCategoryModel fetchCategoryModel = gson.fromJson(catString,FetchCategoryModel.class);

        ArrayList<String> cat = new ArrayList<>();
        cat.addAll(fetchCategoryModel.fashionCat);
        cat.addAll(fetchCategoryModel.groceryCat);

        Log.d("catValue",cat.get(0));

        String queryForCat = query;


        for (int i=0;i<cat.size();i++){
            if(queryForCat.trim().replace(" ","").contains(cat.get(i).toLowerCase())){
                Category = cat.get(i);
                break;
            }
        }

        Log.d("queryForCat",queryForCat);
        Log.d("queryForNormal",query);


        ArrayList<ProductModel> queryProducts1 = new ArrayList<>();
        ArrayList<ProductModel> queryProducts2 = new ArrayList<>();
        ArrayList<ProductModel> queryProducts3 = new ArrayList<>();
        ArrayList<ProductModel> queryProducts4 = new ArrayList<>();
        String Url = "https://tykun.blob.core.windows.net/categories/"+Category+".txt";
        // ArrayList<clothes_model> Products = new ArrayList<>();

        Log.d("Category122",Category);
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Category").child("products").child(Category);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {



                if(completeQuery<1) {


                    if (!Category.equals("null")) {
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {

                            ProductModel P1 = childSnapshot.getValue(ProductModel.class);

                            SharedPreferences sp23 = parent.getSharedPreferences("loginCheck",Context.MODE_PRIVATE);
                            String City = sp23.getString("City","Agra");

                            if(City.equalsIgnoreCase(P1.productCity)){
                                Products.add(P1);
                            }


                            Log.d("brand", P1.title + " ");

                        }

                        String[] querySplit = query.split(" ");
                        int queryCount = 0;
                        if (Products != null) {
                            for (int i = 0; i < Products.size(); i++) {

                                for (int j = 1; j < querySplit.length; j++) {
                                    if (Products.get(i).title.toLowerCase().contains(querySplit[j].toLowerCase())) {
                                        queryCount++;
                                    }

                                }
                                if (queryCount == querySplit.length) {
                                    queryProducts1.add(Products.get(i));
                                    queryCount = 0;
                                } else if (queryCount == querySplit.length - 1) {
                                    queryProducts2.add(Products.get(i));
                                    queryCount = 0;
                                } else if (queryCount == querySplit.length - 2) {
                                    queryProducts3.add(Products.get(i));
                                    queryCount = 0;
                                } else {
                                    queryProducts4.add(Products.get(i));
                                    queryCount = 0;
                                }

                            }
                        }

                        queryProducts1.addAll(queryProducts2);
                        queryProducts1.addAll(queryProducts3);
                        queryProducts1.addAll(queryProducts4);




                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList("list", queryProducts1);

                        //Continue Here...
                        //Log.d("stockBeforePass", queryProducts1.get(0).sellingPrice + "");

                        completeQuery++;
                        ((search_instance) parent).runFragment(bundle, new SearchResults(), 1);
                        directQuery = null;

                        Log.d("query", query + " ");
                    } else {
                        Log.d("Category", "NotFound");
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList("list", queryProducts1);
                        ((search_instance) parent).runFragment(bundle, new SearchResults(), 1);
                    }
                }
                else {
                    Log.d("Shinobu Log", "searchPage354");
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        /*try{

            OkHttpClient client = new OkHttpClient();
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(5, TimeUnit.MINUTES) // connect timeout
                    .writeTimeout(5, TimeUnit.MINUTES) // write timeout
                    .readTimeout(5, TimeUnit.MINUTES); // read timeout

            client = builder.build();


            Request request = new Request.Builder()
                    .url(Url)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.d("OkHttp","Failure to get");
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    String JsonBody = response.body().string();
                    Gson gson = new Gson();
                    try {
                        Products = gson.fromJson(JsonBody, ProductModel[].class);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    Log.d("JsonData",JsonBody);
                    String[] querySplit = query.split(" ");
                    int queryCount =0;
                    if(Products!=null){
                        for(int i=0;i<Products.length;i++){

                            for(int j=1;j<querySplit.length;j++){
                                if(Products[i].title.toLowerCase().contains(querySplit[j])){
                                    queryCount++;
                                }

                            }
                            if(queryCount==querySplit.length){
                                queryProducts1.add(Products[i]);
                                queryCount=0;
                            }
                            else if (queryCount==querySplit.length-1)
                            {
                                queryProducts2.add(Products[i]);
                                queryCount=0;
                            }
                            else if (queryCount==querySplit.length-2)
                            {
                                queryProducts3.add(Products[i]);
                                queryCount=0;
                            }
                            else {
                                queryProducts4.add(Products[i]);
                                queryCount=0;
                            }

                        }
                    }

                    queryProducts1.addAll(queryProducts2);
                    queryProducts1.addAll(queryProducts3);
                    queryProducts1.addAll(queryProducts4);

                    SharedPreferences sp = getActivity().getSharedPreferences("query",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("query",query);
                    editor.apply();
                    directQuery = null;
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("list",queryProducts1);

                    //Continue Here...
                    ((search_instance)getActivity()).runFragment(bundle,new SearchResults(),1);

                    Log.d("query",query+" ");
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }*/


    }


}