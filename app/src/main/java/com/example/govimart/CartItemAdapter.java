package com.example.govimart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;

public class CartItemAdapter extends FirestoreRecyclerAdapter<CartItem, CartItemAdapter.CartItemHolder> {


    //
    private CartItemAdapter.OnItemClickListener listener;
    private CartItemAdapter.OnAdapterCountListener onAdapterCountListener;
    private int itmCount = 0;


    public CartItemAdapter(@NonNull FirestoreRecyclerOptions<CartItem> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull CartItemAdapter.CartItemHolder holder, int position, @NonNull CartItem model) {

        System.out.println("123 Title is" + model.getCartItemPostTitle());

        holder.tvCartItemTitle.setText(model.getCartItemPostTitle());
        holder.tvCartItemCategory.append(" " + model.getCartItemCategory());
        holder.tvCartItemLocation.append(" "+ model.getCartItemLocation());
        holder.tvCartItemDistrict.append(" "+ model.getCartItemDistrict());
        holder.tvCartItemUnitPrice.append(" "+ model.getCartItemUnitPrice());
        holder.tvCartItemQuantity.append(" "+ NumberFormat.getNumberInstance().format(model.getCartItemQuantity()));
        holder.tvCartItemTotalPrice.append(" "+ NumberFormat.getNumberInstance().format(model.getCartItemTotalPrice()));

        //FirebaseStorage storage = FirebaseStorage.getInstance();
        // StorageReference gsReference = storage.getReferenceFromUrl(model.getPImageUrl());
        // String imageUrl = String.valueOf(gsReference.getDownloadUrl());

        //System.out.println("LaLa downURL is" + gsReference.getDownloadUrl());

        /*
        Picasso.get()
                .load(model.getPImageUrl())
                .placeholder(R.mipmap.ic_launcher)
                .resize(800, 500)
                .centerInside()
                //.fit()
                //.centerCrop()
                .into(holder.imageViewPhoto);*/


    }

    @NonNull
    @Override
    public CartItemAdapter.CartItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_card,
                parent, false);
        //
        //onAdapterCountListener.onAdapterCountListener(getItemCount());
        //
        return new CartItemAdapter.CartItemHolder(v);
    }

    // If we want to delete
    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class CartItemHolder extends RecyclerView.ViewHolder {
        TextView tvCartItemTitle;
        TextView tvCartItemCategory;
        TextView tvCartItemLocation;
        TextView tvCartItemDistrict;
        TextView tvCartItemUnitPrice;
        TextView tvCartItemQuantity;
        TextView tvCartItemTotalPrice;

        //
        public CartItemHolder(View itemView) {
            super(itemView);
            tvCartItemTitle = itemView.findViewById(R.id.tv_cart_item_title);
            tvCartItemCategory = itemView.findViewById(R.id.tv_cart_item_category);
            tvCartItemLocation = itemView.findViewById(R.id.tv_cart_item_location);
            tvCartItemDistrict = itemView.findViewById(R.id.tv_cart_item_district);
            tvCartItemUnitPrice = itemView.findViewById(R.id.tv_cart_item_unit_price);
            tvCartItemQuantity = itemView.findViewById(R.id.tv_cart_item_quantity);
            tvCartItemTotalPrice = itemView.findViewById(R.id.tv_cart_item_total_price);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }
    public void setOnItemClickListener(CartItemAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
    //

    public interface OnAdapterCountListener {
        void onAdapterCountListener(int count);
    }

    //private OnAdapterCountListener onAdapterCountListener;
    public void setOnAdapterCountListener(OnAdapterCountListener l) {
        onAdapterCountListener = l;
    }

    //

    /*
    public int getItmCount() {
        return this.itmCount;
    }

     */

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        //itmCount = this.getItemCount();
        onAdapterCountListener.onAdapterCountListener(getItemCount());
    }

}
