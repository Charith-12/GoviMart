package com.example.govimart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class NotificationActivity extends AppCompatActivity {

    private Toolbar notificationsToolBar;
    private TextView noNotificationsTv;
    private RecyclerView notificationsRecyclerViewView;


    private NotificationsAdapter adapter;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        notificationsToolBar = (Toolbar) findViewById(R.id.tb_notifications_screen);
        noNotificationsTv = (TextView) findViewById(R.id.tv_notifications_no_notifications);
        notificationsRecyclerViewView = (RecyclerView) findViewById(R.id.notifications_recyclerView);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();

        setSupportActionBar(notificationsToolBar);
        notificationsToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onBackPressed();
                Intent intent = new Intent(NotificationActivity.this, SellerMainActivityHome.class);
                startActivity(intent);
                // Todo:
            }
        });


        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        Query query = db.collection("Users").document(userId).collection("Notifications").orderBy("time", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Notification> options = new FirestoreRecyclerOptions.Builder<Notification>()
                .setQuery(query, Notification.class)
                .build();
        adapter = new NotificationsAdapter(options);
        adapter.startListening();
        adapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);
        RecyclerView recyclerView = findViewById(R.id.notifications_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        // To handle Empty RecyclerView
        adapter.setOnAdapterCountListener(new NotificationsAdapter.OnAdapterCountListener() {
            @Override
            public void onAdapterCountListener(int count) {
                if (count > 0){
                    //adapterEmptyText.setVisibility(View.GONE);
                    noNotificationsTv.setVisibility(View.GONE);
                    notificationsRecyclerViewView.setVisibility(View.VISIBLE);
                }else {
                    noNotificationsTv.setVisibility(View.VISIBLE);
                    notificationsRecyclerViewView.setVisibility(View.GONE);
                }

            }
        });
        //


        // Swipe to delete

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                final RecyclerView.ViewHolder viewHolder1 = viewHolder;

                // Alert Dialog to confirm
                AlertDialog.Builder builder = new AlertDialog.Builder(NotificationActivity.this);
                builder.setTitle("Are you sure?")
                        .setMessage("You won't be able to interact with this order")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //onBackPressed();
                                int position = viewHolder1.getAdapterPosition();
                                adapter.deleteItem(viewHolder1.getAdapterPosition());
                                Toast.makeText(NotificationActivity.this, "Deleted", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                adapter.notifyItemChanged(viewHolder1.getAdapterPosition());
                            }
                        });


                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                //

            }
        }).attachToRecyclerView(recyclerView);



        adapter.setOnItemClickListener(new NotificationsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Notification notification = documentSnapshot.toObject(Notification.class);
                String id = documentSnapshot.getId();
                String path = documentSnapshot.getReference().getPath();
                Toast.makeText(NotificationActivity.this,
                        "Position: " + position + " ID: " + id, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(NotificationActivity.this, NotificationExpanded.class);

                intent.putExtra("NE_TITLE", notification.getTitleOfPost());
                intent.putExtra("NE_QUANTITY", notification.getQuantityBought());
                String timeAsString = String.valueOf(notification.getTime());
                intent.putExtra("NE_DATE", timeAsString);
                intent.putExtra("NE_POST_ID", notification.getIdOfPost());
                intent.putExtra("NE_BUYER_NAME", notification.getBuyerName());
                //intent.putExtra("SP_POST_ID", id);
                intent.putExtra("NE_BUYER_ADDRESS", notification.getBuyerAddress());
                intent.putExtra("NE_BUYER_TP", notification.getBuyerContactNumber());
                intent.putExtra("NE_BUYER_NOTE", notification.getBuyerNote());
                intent.putExtra("NE_BUYER_BUYER_ID", notification.getBuyerId());


                startActivity(intent);


            }
        });
    }



    /*
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    /*
    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.stopListening();
    }

     */
}