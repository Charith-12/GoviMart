package com.example.govimart;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity {

    Toolbar checkoutToolbar;
    TextView beingProcessedTv, pleaseWaitTv, orderCompleteTv, orderCompleteAddiTv;
    ProgressBar waitingPb;
    EditText buyerName, buyerTpNo, buyerAddress, buyerNote;
    LinearLayout deliveryInfoEts, orderProcessingTvs, orderCompleteTvs;

    Button orderConfirmBtn;

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    String userId;
    String deliveryName, deliveryTp, deliveryAddress, deliveryNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        checkoutToolbar = findViewById(R.id.tb_checkout_screen);
        beingProcessedTv = findViewById(R.id.tv_checkout_being_processed);
        pleaseWaitTv = findViewById(R.id.tv_checkout_please_wait);
        orderCompleteTv = findViewById(R.id.tv_checkout_order_complete);
        orderCompleteAddiTv = findViewById(R.id.tv_checkout_order_complete_additional);
        waitingPb = findViewById(R.id.pb_checkout_wait_progress_bar);

        buyerName = (EditText) findViewById(R.id.et_delivery_name);
        buyerTpNo = (EditText) findViewById(R.id.et_delivery_telephone);
        buyerAddress = (EditText) findViewById(R.id.et_delivery_address);
        buyerNote = (EditText) findViewById(R.id.et_delivery_note);

        orderConfirmBtn = (Button) findViewById(R.id.btn_check_out_confirm_order);

        deliveryInfoEts = (LinearLayout) findViewById(R.id.ll_delivery_info_ets);
        orderProcessingTvs = (LinearLayout) findViewById(R.id.ll_order_being_processed_tvs);
        orderCompleteTvs = (LinearLayout) findViewById(R.id.ll_order_complete_tvs);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        userId = mAuth.getCurrentUser().getUid();

        setSupportActionBar(checkoutToolbar);
        checkoutToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getBuyerInfo(); // Populating EditText fields

        orderConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderConfirmTapped();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        //processOrder();

    }

    private void processOrder() {

        //String userId = mAuth.getCurrentUser().getUid();
        orderProcessingTvs.setVisibility(View.VISIBLE);
        deliveryInfoEts.setVisibility(View.GONE);

        CollectionReference cartItemsReference = db.collection("Users").document(userId).collection("CartItems");
        cartItemsReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot cartItem : task.getResult()) {

                        String postId = (String) cartItem.get("cartItemPostId");
                        double boughtQty = (double) cartItem.get("cartItemQuantity");
                        String postTitle = (String) cartItem.get("cartItemPostTitle");
                        String ownerId = (String) cartItem.get("cartItemOwnerId");
                        String cartItemId = (String) cartItem.getId();

                        updateQuantities(postId, boughtQty, postTitle, ownerId, cartItemId);

                    }

                    orderCompleteTvs.setVisibility(View.VISIBLE);
                    orderProcessingTvs.setVisibility(View.GONE);
                    //orderCompleteTv.setVisibility(View.VISIBLE);
                    //orderCompleteAddiTv.setVisibility(View.VISIBLE);
                    //pleaseWaitTv.setVisibility(View.GONE);
                    //waitingPb.setVisibility(View.GONE);

                    // Alert Dialog on success
                    AlertDialog.Builder builder = new AlertDialog.Builder(CheckoutActivity.this);
                    builder.setTitle("Task Complete")
                            .setMessage("Press OK to return to home")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    onBackPressed();
                                }
                            });


                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    //

                } else {
                    Log.d("DEBUG", "Error getting documents: ", task.getException());
                    Toast.makeText(CheckoutActivity.this, "Error getting cartItems from database ", Toast.LENGTH_LONG).show();
                }


            }

        });


    }

    private void updateQuantities(String id, double qty, String title, String ownerId, String cartItemId) {

        final String pId = id;
        final double pQty = qty;
        final String posTitle = title;
        final String posOwnerId = ownerId;
        final String currentCartItemId = cartItemId;

        db.collection("Posts").document(id).update("pQuantity", FieldValue.increment(-qty)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Toast.makeText(CheckoutActivity.this, "1 Qty Updated", Toast.LENGTH_SHORT).show();

                sendNotification(posOwnerId, pId, posTitle, pQty, currentCartItemId);

            }
        });

    }

    private void sendNotification(String ownerId, String postId, String title, double boughtQuantity, String cartItemId) {

        final String currentCartItemId = cartItemId;

        Map<String, Object> notification = new HashMap<>();
        notification.put("idOfPost", postId);
        notification.put("titleOfPost", title);
        notification.put("quantityBought", boughtQuantity);
        notification.put("time", new Date());
        notification.put("message", "Your post: " + title + ", just made a sale of : " + boughtQuantity + "kg");
        notification.put("buyerName", buyerName.getText().toString().trim());
        notification.put("buyerContactNumber", buyerTpNo.getText().toString().trim());
        notification.put("buyerAddress", buyerAddress.getText().toString().trim());
        notification.put("buyerNote", buyerNote.getText().toString().trim());
        notification.put("buyerId", userId);

        // TODO: Adding isComplete

        db.collection("Users").document(ownerId).collection("Notifications").add(notification)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("TAG", "Notification upload success");
                        Toast.makeText(CheckoutActivity.this, "1 Notification was sent", Toast.LENGTH_SHORT).show();

                        deleteCartItem(currentCartItemId);

                    }
                });

    }

    private void deleteCartItem(String currentCartItemId) {

        db.collection("Users").document(userId).collection("CartItems").document(currentCartItemId).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Toast.makeText(CheckoutActivity.this, "One item processed", Toast.LENGTH_SHORT).show();
                        // After only one successful cart item


                    }
                });

    }

    private void getBuyerInfo() {
        final DocumentReference documentReference = db.collection("Users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                deliveryName = value.getString("UserName"); //Variable
                buyerName.setText(deliveryName);  // EditText
                deliveryTp = value.getString("MobileNo"); //Variable
                buyerTpNo.setText(deliveryTp); // EditText
                deliveryAddress = value.getString("Address"); //Variable
                buyerAddress.setText(deliveryAddress); // EditText
            }
        });
    }

    private void orderConfirmTapped() {

        if(validateDataInput()){

            // Alert Dialog to confirm
            AlertDialog.Builder builder = new AlertDialog.Builder(CheckoutActivity.this);
            builder.setTitle("Are you sure?")
                    .setMessage("Press Yes to process your order")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //onBackPressed();
                            processOrder();
                            Toast.makeText(CheckoutActivity.this, "Process start goes here", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("No", null);


            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            //

        }

    }


    private boolean validateDataInput() {
        String delivName = buyerName.getText().toString().trim();
        String delivTp = buyerTpNo.getText().toString().trim();
        String delivAddress = buyerAddress.getText().toString().trim();

        if (delivName.isEmpty()) {
            buyerName.setError("Please enter Name");
            buyerName.requestFocus();
            return false;
        }

        if (delivTp.isEmpty()) {
            buyerTpNo.setError("Contact No. is Required");
            buyerTpNo.requestFocus();
            return false;
        }

        if (delivTp.length() != 10) {
            buyerTpNo.setError("Please enter a valid Contact No.");
            buyerTpNo.requestFocus();
            return false;
        }
        if (!Patterns.PHONE.matcher(delivTp).matches()) {
            buyerTpNo.setError("Please enter a valid contact No.");
            buyerTpNo.requestFocus();
            return false;
        }

        if (delivAddress.isEmpty()) {
            buyerAddress.setError("Please enter Address");
            buyerAddress.requestFocus();
            return false;
        }

        else {
            return true;
        }

    }

}

