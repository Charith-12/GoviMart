package com.example.govimart;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;


public class AddCategoryCardAdapter extends RecyclerView.Adapter<AddCategoryCardAdapter.AddCategoryCardViewHolder> {

    private ArrayList<MainCategoryCard> mCategoryList;
    private Context context;

    public class AddCategoryCardViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextView1;
        private Context context1;


        public AddCategoryCardViewHolder(View itemView, Context ctx) {
            super(itemView);
            this.context1 = ctx;

            mImageView = itemView.findViewById(R.id.categoryImageView);
            mTextView1 = itemView.findViewById(R.id.categoryTextView);

            // On Click Listener for card
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //
                    String cardText = mTextView1.getText().toString();
                    v.getContext().startActivity(new Intent(v.getContext(), AddNewItemActivity.class).putExtra("SELECTED_CATEGORY", cardText));

                    /*if(cardText.equalsIgnoreCase("Vegetables")){

                        // Old intent
                        //v.getContext().startActivity(new Intent(v.getContext(), ShowVegetablesActivity.class));

                        // New fragment transaction

                        // If we need to get some info from mainActivity -> the fragment when Its created.Replace with below line
                        //MessagesFragment messagesFragment1 = MessagesFragment.newInstance("this is text ", 123);

                        VegetablesFragment vegetablesFragment = new VegetablesFragment();

                        FragmentManager manager = ((AppCompatActivity)context1).getSupportFragmentManager();


                        manager.beginTransaction()
                                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                                .replace(R.id.master_fragment_container,
                                        vegetablesFragment)
                                .addToBackStack(null)
                                .commit();
                    }

                    //
                    */

                }
            });

        }
    }

    //Constructor
    public AddCategoryCardAdapter(ArrayList<com.example.govimart.MainCategoryCard> categoryList, Context context) {
        mCategoryList = categoryList;
        this.context = context;
    }

    @Override
    public AddCategoryCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_category_card, parent, false);
        AddCategoryCardViewHolder categoryViewHolder = new AddCategoryCardViewHolder(v, context);
        return categoryViewHolder;
    }
    @Override
    public void onBindViewHolder(AddCategoryCardViewHolder holder, int position) {
        com.example.govimart.MainCategoryCard currentItem = mCategoryList.get(position);
        holder.mImageView.setImageResource(currentItem.getImageResource());
        holder.mTextView1.setText(currentItem.getText1());


    }
    @Override
    public int getItemCount() {
        return mCategoryList.size();
    }

    //

}
