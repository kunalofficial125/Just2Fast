package com.just2fast.ushop;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchResults#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchResults extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchResults() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchResults.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchResults newInstance(String param1, String param2) {
        SearchResults fragment = new SearchResults();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    RecyclerView recyclerView;
    CardView searchCard;
    int currentPage=0;
    TextView searchText,notFoundText;
    LottieAnimationView lottieAnimationView;
    searchResultsRecycle searchResultsRecycle;
    ScrollView ss;
    TextView noMoreItemsText;
    ProgressBar progressBar12;
    boolean isEndReached,checkShow;
    ArrayList<ProductModel> selectedItems = new ArrayList<>();
    GridLayoutManager gridLayoutManager;
    Activity parent;
    ArrayList<ProductModel> recList = new ArrayList<>();
    ArrayList<ProductModel> list = new ArrayList<>();
    String query;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_search_results, container, false);
        selectedItems.clear();
        try {
            recList = getArguments().getParcelableArrayList("list");

            SharedPreferences sp = getActivity().getSharedPreferences("query", Context.MODE_PRIVATE);
            query = sp.getString("query","null");
            Log.d("Queries",query+"");
        }
        catch (Exception e){
            Log.d("gettingDataError",e+" ");
        }

        Log.d("listLength",recList.size()+" ");

        recyclerView = v.findViewById(R.id.recyclerView);
        searchText = v.findViewById(R.id.searchText);
        ss = v.findViewById(R.id.ss);
        progressBar12 = v.findViewById(R.id.progressBar12);
        notFoundText = v.findViewById(R.id.notFoundText);
        noMoreItemsText = v.findViewById(R.id.noMoreItemsText);
        searchCard = v.findViewById(R.id.searchCard);
        lottieAnimationView = v.findViewById(R.id.notFound);
        parent = getActivity();
        searchText.setText(query);

        SharedPreferences preferences1 = parent.getSharedPreferences("Grocery",Context.MODE_PRIVATE);
        int y = preferences1.getInt("ss",0);
        recyclerView.scrollTo(0,y);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchedText  = searchText.getText().toString();
                Bundle bundle = new Bundle();
                bundle.putString("searchedText",searchedText);
                ((search_instance)parent).runFragment(bundle,new search_page(),3);
            }
        };

        searchText.setOnClickListener(onClickListener);
        searchCard.setOnClickListener(onClickListener);



        AppCompatActivity activity = (AppCompatActivity) getActivity();

        if(!recList.isEmpty()){
            searchResultsRecycle = new searchResultsRecycle(activity,selectedItems,SearchResults.this);
            gridLayoutManager = new GridLayoutManager(parent,2);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setAdapter(searchResultsRecycle);

            if(recList.size()<15){
                selectedItems.addAll(recList);
                currentPage = recList.size()-1;
            }
            else{
                selectedItems.addAll(recList.subList(0,15));
                currentPage = 15;
            }


            ss.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                @Override
                public void onScrollChanged() {
                    View view = ss.getChildAt(ss.getChildCount() - 1);

                    int diff = (view.getBottom() - (ss.getHeight() + ss.getScrollY()));

                    if(gridLayoutManager.findLastCompletelyVisibleItemPosition()==recList.size()-1){
                        progressBar12.setVisibility(View.INVISIBLE);
                        noMoreItemsText.setVisibility(View.VISIBLE);
                    }


                    if (diff == 0) {
                        // End of the ScrollView is reached
                        if (!isEndReached) {

                            isEndReached = true;
                            // Toast.makeText(applicationContext, "End", Toast.LENGTH_SHORT).show();


                            Log.d("itemPos",gridLayoutManager.findLastCompletelyVisibleItemPosition()+"");
                            Log.d("currentPage",currentPage+"");
                            if(currentPage==gridLayoutManager.findLastCompletelyVisibleItemPosition()+1){
                                checkShow = true;
                                Log.d("checkShow","true");
                            }

                            if(checkShow){

                                if(currentPage!=recList.size()-1){

                                    if(recList.size()-currentPage < 15){
                                        selectedItems.addAll(recList.subList(currentPage,recList.size()));
                                        searchResultsRecycle.notifyItemInserted(currentPage-1);
                                        currentPage = recList.size()-1;
                                    }
                                    else{
                                        selectedItems.addAll(recList.subList(currentPage+1,currentPage+15));
                                        searchResultsRecycle.notifyItemInserted(currentPage-1);
                                        currentPage = currentPage+15;
                                    }
                                }
                                else{
                                    isEndReached = false;
                                }

                            }
                            else {
                                Log.d("checkShow","false");
                            }





                            // Perform your action here
                        }

                    } else {
                        // Reset the flag when scrolling up
                        isEndReached = false;
                    }
                }

            });


        }
        else {

            lottieAnimationView.setVisibility(View.VISIBLE);
            notFoundText.setVisibility(View.VISIBLE);
            progressBar12.setVisibility(View.INVISIBLE);

        }



        return v;
    }
}