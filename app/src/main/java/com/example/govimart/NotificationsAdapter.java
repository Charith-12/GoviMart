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

public class NotificationsAdapter extends FirestoreRecyclerAdapter<Notification, NotificationsAdapter.NotificationHolder> {

    private NotificationsAdapter.OnItemClickListener listener;
    private NotificationsAdapter.OnAdapterCountListener onAdapterCountListener;


    public NotificationsAdapter(@NonNull FirestoreRecyclerOptions<Notification> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NotificationsAdapter.NotificationHolder holder, int position, @NonNull Notification model) {

        //System.out.println("YoYoTitle is" + model.getPTitle());

        holder.textViewTime.append(" " + model.getTime());
        holder.textViewMessage.setText(model.getMessage());
        // TODO: remove message attribute and concatenate message here

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
                .into(holder.imageViewPhoto);

         */


    }

    @NonNull
    @Override
    public NotificationsAdapter.NotificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_card,
                parent, false);
        return new NotificationsAdapter.NotificationHolder(v);
    }

    // If we want to delete
    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class NotificationHolder extends RecyclerView.ViewHolder {
        TextView textViewTime;
        TextView textViewMessage;

        //
        public NotificationHolder(View itemView) {
            super(itemView);
            textViewTime = itemView.findViewById(R.id.tv_notification_date);
            textViewMessage = itemView.findViewById(R.id.tv_notification_message);


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
    public void setOnItemClickListener(NotificationsAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    //
    public interface OnAdapterCountListener {
        void onAdapterCountListener(int count);
    }

    //private OnAdapterCountListener onAdapterCountListener;
    public void setOnAdapterCountListener(NotificationsAdapter.OnAdapterCountListener l) {
        onAdapterCountListener = l;
    }
    //

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        //itmCount = this.getItemCount();
        onAdapterCountListener.onAdapterCountListener(getItemCount());
    }


}
