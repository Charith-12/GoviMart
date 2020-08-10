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


public class CategoriesTabFragment extends Fragment {
    private static final String TAG = "HomeFragment";

    //private Button btnTEST;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //
        View view = inflater.inflate(R.layout.categories_fragment_layout,container,false);
        //
        ArrayList<MainCategoryCard> mainCategoriesList = new ArrayList<>();
        mainCategoriesList.add(new MainCategoryCard(R.drawable.img_vegetables, "Vegetables"));
        mainCategoriesList.add(new MainCategoryCard(R.drawable.img_fruits, "Fruits"));
        mainCategoriesList.add(new MainCategoryCard(R.drawable.img_rice, "Rice"));
        mainCategoriesList.add(new MainCategoryCard(R.drawable.img_pulses_and_grains, "Pulses and Grains"));
        mainCategoriesList.add(new MainCategoryCard(R.drawable.img_spices, "Spices"));
        mainCategoriesList.add(new MainCategoryCard(R.drawable.img_leaf_vegitables, "Leaf Vegetables"));
        mainCategoriesList.add(new MainCategoryCard(R.drawable.img_tubers, "Tubers"));
        mainCategoriesList.add(new MainCategoryCard(R.drawable.img_plantation_crops, "Plantation Crops"));
        mainCategoriesList.add(new MainCategoryCard(R.drawable.img_dairy, "Dairy Products"));
        mainCategoriesList.add(new MainCategoryCard(R.drawable.img_herbs, "Herbs"));
        mainCategoriesList.add(new MainCategoryCard(R.drawable.img_oils, "Oils"));
        mainCategoriesList.add(new MainCategoryCard(R.drawable.img_farm_products, "Farm Products"));
        mainCategoriesList.add(new MainCategoryCard(R.drawable.img_other, "Other Products"));
        mainCategoriesList.add(new MainCategoryCard(R.drawable.img_farming_eqip, "Farming Equipments"));

        // original -> // mRecyclerView = findViewById(R.id.homeCategoryRecyclerView);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.homeCategoryRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        // original -> // mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager = new LinearLayoutManager(getActivity());
        // Alternate to above -> // mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new MainCategoryCardAdapter(mainCategoriesList, getActivity()); //mAdapter = new MainCategoryCardAdapter(getActivity(), mainCategoriesList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        //

        // prev -> // return inflater.inflate(R.layout.categories_fragment_layout,container,false);
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