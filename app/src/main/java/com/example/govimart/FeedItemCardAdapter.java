package com.example.govimart;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FeedItemCardAdapter extends RecyclerView.Adapter<FeedItemCardAdapter.FeedItemCardViewHolder> {

    private ArrayList<FeedItemCard> mItemsList;
    public static class FeedItemCardViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextView1;

        public FeedItemCardViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.feedItemImageView);
            mTextView1 = itemView.findViewById(R.id.feedItemTextView);

            // On Click Listener for card
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //
                    String cardText = mTextView1.getText().toString();

                    if(cardText.equalsIgnoreCase("Weather Report")){
                        v.getContext().startActivity(new Intent(v.getContext(), WeatherReportActivity.class));
                    }

                    //
                    //TODO Create intents for other cards too

                }
            });

        }
    }

    //Constructor
    public FeedItemCardAdapter(ArrayList<FeedItemCard> itemsList) {
        mItemsList = itemsList;
    }

    @Override
    public FeedItemCardAdapter.FeedItemCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item_card, parent, false);
        FeedItemCardAdapter.FeedItemCardViewHolder feedItmViewHolder = new FeedItemCardAdapter.FeedItemCardViewHolder(v);
        return feedItmViewHolder;
    }
    @Override
    public void onBindViewHolder(FeedItemCardAdapter.FeedItemCardViewHolder holder, int position) {
        FeedItemCard currentItem = mItemsList.get(position);
        holder.mImageView.setImageResource(currentItem.getImageResource());
        holder.mTextView1.setText(currentItem.getText1());

    }
    @Override
    public int getItemCount() {
        return mItemsList.size();
    }

}
