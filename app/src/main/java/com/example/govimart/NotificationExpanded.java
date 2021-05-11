package com.example.govimart;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;

public class NotificationExpanded extends AppCompatActivity {

    private Toolbar expandedNotificationToolBar;
    private TextView post_titleTV, boughtQtyTv, dateTv, buyerNameTv, buyerAddressTv, buyerTpTv, buyerNoteTv, viewPostTv;
    private Button callBtn;

    private String neTitle, neDate, neBuyerName, neBuyerAddress, neBuyerContactNo, neBuyerNote, nePostId, neBuyerId;
    private double neQuantity;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference postsRef = db.collection("Posts");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_expanded);

        expandedNotificationToolBar = (Toolbar) findViewById(R.id.tb_notifications_expanded_screen);
        post_titleTV = (TextView) findViewById(R.id.tv_ne_post_title);
        boughtQtyTv = (TextView) findViewById(R.id.tv_ne_bought_quantity);
        dateTv = (TextView) findViewById(R.id.tv_ne_time_bought);
        buyerNameTv = (TextView) findViewById(R.id.tv_ne_buyer_name);
        buyerAddressTv = (TextView) findViewById(R.id.tv_ne_buyer_address);
        buyerTpTv = (TextView) findViewById(R.id.tv_ne_buyer_contact_no);
        buyerNoteTv = (TextView) findViewById(R.id.tv_ne_buyer_notes);
        viewPostTv = (TextView) findViewById(R.id.tv_ne_view_post);
        callBtn = (Button) findViewById(R.id.btn_ne_make_call);

        setSupportActionBar(expandedNotificationToolBar);
        expandedNotificationToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Intent
        Intent intent = getIntent();
        neTitle = intent.getStringExtra("NE_TITLE");
        neQuantity =intent.getDoubleExtra("NE_QUANTITY", 0.0);
        neDate = intent.getStringExtra("NE_DATE");
        nePostId = intent.getStringExtra("NE_POST_ID");  // for viewing the post
        neBuyerName = intent.getStringExtra("NE_BUYER_NAME");
        neBuyerAddress = intent.getStringExtra("NE_BUYER_ADDRESS");
        neBuyerContactNo = intent.getStringExtra("NE_BUYER_TP");
        neBuyerNote = intent.getStringExtra("NE_BUYER_NOTE");
        neBuyerId = intent.getStringExtra("NE_BUYER_BUYER_ID");  // for open chat?
        //


        // Setting textViews
        post_titleTV.setText(neTitle);
        dateTv.setText(neDate);
        boughtQtyTv.setText("" + neQuantity);
        buyerNameTv.setText(neBuyerName);
        buyerAddressTv.setText(neBuyerAddress);
        buyerTpTv.setText(neBuyerContactNo);
        buyerNoteTv.setText(neBuyerNote);


        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialer();
            }
        });

        viewPostTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(NotificationExpanded.this, "View Post Happening here", Toast.LENGTH_SHORT).show();
                showPost();
            }
        });


    }

    private void openDialer() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + neBuyerContactNo));
        startActivity(intent);

    }

    private void showPost(){

        // Show Dialog on Item Tap
        Dialog mDialog = new Dialog(NotificationExpanded.this);
        mDialog.setContentView(R.layout.sale_post_card);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();

        final TextView textViewTitle = mDialog.findViewById(R.id.tv_post_title);
        final TextView textViewCategory = mDialog.findViewById(R.id.tv_post_category);
        final TextView textViewDistrict = mDialog.findViewById(R.id.tv_post_district);
        final TextView textViewLocation = mDialog.findViewById(R.id.tv_post_location);
        final TextView textViewQuantity = mDialog.findViewById(R.id.tv_post_quantity);
        final TextView textViewPrice = mDialog.findViewById(R.id.tv_post_price);
        final ImageView imageViewPhoto = mDialog.findViewById(R.id.iv_post_image);
        final TextView textViewAvailableDateFrom = mDialog.findViewById(R.id.tv_post_date_from);
        final TextView textViewAvailableDateTo = mDialog.findViewById(R.id.tv_post_date_to);
        final TextView textViewHarvestedDate = mDialog.findViewById(R.id.tv_post_date_harvested);
        final TextView textViewGrade = mDialog.findViewById(R.id.tv_post_grade);
        final TextView textViewDescription = mDialog.findViewById(R.id.tv_post_description);
        final TextView textViewFirstName = mDialog.findViewById(R.id.tv_post_owner_name);
        final TextView textViewMobileNo = mDialog.findViewById(R.id.tv_post_owner_mobile_no);

        final DocumentReference documentReference = postsRef.document(nePostId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                //
                textViewTitle.setText(value.getString("pTitle"));
                textViewCategory.append(" " + value.getString("pCategory"));
                textViewDistrict.append(" "+ value.getString("pDistrict"));
                textViewLocation.append(" "+ value.getString("pLocation"));
                textViewQuantity.append(" "+ NumberFormat.getNumberInstance().format(value.getDouble("pQuantity")));
                textViewPrice.append(" "+ value.getString("pPrice"));
                textViewAvailableDateFrom.append(" "+ value.getString("pAvailableDateFrom"));
                textViewAvailableDateTo.append(" "+ value.getString("pAvailableDateTo"));
                textViewHarvestedDate.append(" "+ value.getString("pHarvestedDate"));
                textViewGrade.append(" "+ value.getString("pGrade"));
                textViewDescription.setText(String.format("Description : %s", value.getString("pDescription")));
                textViewFirstName.append(" "+ value.getString("pOwnerName"));
                textViewMobileNo.append(" "+ value.getString("pOwnerMobileNo"));


                Picasso.get()
                        .load(value.getString("pImageUrl"))
                        .placeholder(R.mipmap.ic_launcher)
                        .resize(800, 500)
                        .centerInside()
                        //.fit()
                        //.centerCrop()
                        .into(imageViewPhoto);

                //
            }
        });

    }


}