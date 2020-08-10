package com.example.govimart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FeedTabFragment extends Fragment {
    private static final String TAG = "FeedFragment";

    //private Button btnTEST;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //
        View view = inflater.inflate(R.layout.feed_fragment_layout,container,false);
        //
        ArrayList<FeedItemCard> feedItemsList = new ArrayList<>();
        feedItemsList.add(new FeedItemCard(R.drawable.weather_report, "Weather Report"));
        feedItemsList.add(new FeedItemCard(R.drawable.vegetables_prices, "Vegetables Market Price Today"));
        feedItemsList.add(new FeedItemCard(R.drawable.fruits_prices, "Fruits Market Price Today"));
        feedItemsList.add(new FeedItemCard(R.drawable.other_products_price, "Other Products' Market Prices"));
        feedItemsList.add(new FeedItemCard(R.drawable.fertilizing_basics, "Fertilizing Basics"));
        feedItemsList.add(new FeedItemCard(R.drawable.farm_smart, "Farm Smart! Developing Sri Lanka's Agriculture Sector in the air"));
        feedItemsList.add(new FeedItemCard(R.drawable.sl_agriculture, "Sri Lanka - Agriculture"));


        // original -> // mRecyclerView = findViewById(R.id.feedItemsRecyclerView);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.feedItemsRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        // original -> // mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager = new LinearLayoutManager(getActivity());

        mAdapter = new FeedItemCardAdapter(feedItemsList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        //

        // prev -> // return inflater.inflate(R.layout.feed_fragment_layout,container,false);
        //View view = inflater.inflate(R.layout.categories_fragment_layout,container,false);
        //btnTEST = (Button) view.findViewById(R.id.btnTEST);

        //btnTEST.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        Toast.makeText(getActivity(), "TESTING BUTTON CLICK 1",Toast.LENGTH_SHORT).show();
        //    }
        //});

        //return view;

        return view;
    }
}
