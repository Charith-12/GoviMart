package com.example.govimart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;

//
public class SalePostAdapter extends FirestoreRecyclerAdapter<SalePost, SalePostAdapter.SalePostHolder> {
    private OnItemClickListener listener;


    public SalePostAdapter(@NonNull FirestoreRecyclerOptions<SalePost> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull SalePostHolder holder, int position, @NonNull SalePost model) {

        System.out.println("YoYoTitle is" + model.getPTitle());

        holder.textViewTitle.setText(model.getPTitle());
        holder.textViewCategory.append(" " + model.getPCategory());
        holder.textViewDistrict.append(" "+ model.getPDistrict());
        holder.textViewLocation.append(" "+ model.getPLocation());
        holder.textViewQuantity.append(" "+ NumberFormat.getNumberInstance().format(model.getPQuantity()));
        holder.textViewPrice.append(" "+ model.getPPrice());
        holder.textViewAvailableDateFrom.append(" "+ model.getPAvailableDateFrom());
        holder.textViewAvailableDateTo.append(" "+ model.getPAvailableDateTo());
        holder.textViewHarvestedDate.append(" "+ model.getPHarvestedDate());
        holder.textViewGrade.append(" "+ model.getPGrade());
        holder.textViewDescription.setText(String.format("Description : %s", model.getPDescription()));
        holder.textViewFirstName.append(" "+ model.getPOwnerName());
        holder.textViewMobileNo.append(" "+ model.getPOwnerMobileNo());


        //FirebaseStorage storage = FirebaseStorage.getInstance();
        // StorageReference gsReference = storage.getReferenceFromUrl(model.getPImageUrl());
        // String imageUrl = String.valueOf(gsReference.getDownloadUrl());

        //System.out.println("LaLa downURL is" + gsReference.getDownloadUrl());

        Picasso.get()
                .load(model.getPImageUrl())
                .placeholder(R.mipmap.ic_launcher)
                .resize(800, 500)
                .centerInside()
                //.fit()
                //.centerCrop()
                .into(holder.imageViewPhoto);


    }
    @NonNull
    @Override
    public SalePostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sale_post_card,
                parent, false);
        return new SalePostHolder(v);
    }

    // If we want to delete
    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class SalePostHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewCategory;
        TextView textViewDistrict;
        TextView textViewLocation;
        TextView textViewQuantity;
        TextView textViewPrice;
        ImageView imageViewPhoto;
        TextView textViewAvailableDateFrom;
        TextView textViewAvailableDateTo;
        TextView textViewHarvestedDate;
        TextView textViewGrade;
        TextView textViewDescription;
        TextView textViewFirstName;
        TextView textViewMobileNo;
        //
        public SalePostHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.tv_post_title);
            textViewCategory = itemView.findViewById(R.id.tv_post_category);
            textViewDistrict = itemView.findViewById(R.id.tv_post_district);
            textViewLocation = itemView.findViewById(R.id.tv_post_location);
            textViewQuantity = itemView.findViewById(R.id.tv_post_quantity);
            textViewPrice = itemView.findViewById(R.id.tv_post_price);
            imageViewPhoto = itemView.findViewById(R.id.iv_post_image);
            textViewAvailableDateFrom = itemView.findViewById(R.id.tv_post_date_from);
            textViewAvailableDateTo = itemView.findViewById(R.id.tv_post_date_to);
            textViewHarvestedDate = itemView.findViewById(R.id.tv_post_date_harvested);
            textViewGrade = itemView.findViewById(R.id.tv_post_grade);
            textViewDescription = itemView.findViewById(R.id.tv_post_description);
            textViewFirstName = itemView.findViewById(R.id.tv_post_owner_name);
            textViewMobileNo = itemView.findViewById(R.id.tv_post_owner_mobile_no);

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
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
//

/*
public class SalePostAdapter extends RecyclerView.Adapter<SalePostAdapter.SalePostViewHolder> {
    private Context mContext;
    private List<SalePost> mUploads;
    private OnItemClickListener mListener;
    public SalePostAdapter(Context context, List<SalePost> uploads) {
        mContext = context;
        mUploads = uploads;
    }
    @Override
    public SalePostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.sale_post_card, parent, false);
        return new SalePostViewHolder(v);
    }
    @Override
    public void onBindViewHolder(SalePostViewHolder holder, int position) {
        SalePost uploadCurrent = mUploads.get(position);
        holder.textViewName.setText(uploadCurrent.getName());
        Picasso.get()
                .load(uploadCurrent.getPostImageUrl())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(holder.imageView);
    }
    @Override
    public int getItemCount() {
        return mUploads.size();
    }
    public class SalePostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        public TextView textViewName;
        public ImageView imageView;
        public SalePostViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_name);
            imageView = itemView.findViewById(R.id.image_view_upload);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }
        @Override
        public void onClick(View v) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);
                }
            }
        }
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select Action");
            MenuItem doWhatever = menu.add(Menu.NONE, 1, 1, "Do whatever");
            MenuItem delete = menu.add(Menu.NONE, 2, 2, "Delete");
            doWhatever.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
        }
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    switch (item.getItemId()) {
                        case 1:
                            mListener.onWhatEverClick(position);
                            return true;
                        case 2:
                            mListener.onDeleteClick(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
        void onWhatEverClick(int position);
        void onDeleteClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}

 */