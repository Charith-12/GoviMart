package com.example.govimart;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;

public class SalePostDetailed extends AppCompatActivity {

    TextView titleTv, quantityTv, descriptionTv, nameTv, mobileNoTv, WaitTv;
    EditText enteredQtyEt;
    Button addToCartBtn;
    ProgressBar waitPbar;

    String pdCategory;
    String pdTitle;
    double pdQuantity;
    String pdGrade;
    String pdPrice;
    String pdAvailableDateFrom;
    String pdAvailableDateTo;
    String pdHarvestedDate;
    String pdLocation;
    String pdDistrict;
    String pdDescription;
    String pdImageUrl;
    String pdPostId;
    String pdOwnerId;
    String pdOwnerName;
    String pdOwnerMobileNo;

    private FirebaseAuth mAuth;
    private String ownerId;
    FirebaseFirestore db;

    double userEnteredQtyDouble;
    double totalCartItemPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_post_detailed);

        titleTv = (TextView) findViewById(R.id.textView_detailed_Title);
        quantityTv = (TextView) findViewById(R.id.textView_detailed_Quantity);
        descriptionTv = (TextView) findViewById(R.id.textView_detailed_Description);
        nameTv = (TextView) findViewById(R.id.textView_detailed_Name);
        mobileNoTv = (TextView) findViewById(R.id.textView_detailed_Mobile_no);
        WaitTv = (TextView) findViewById(R.id.tv_detailed_please_wait);

        waitPbar = (ProgressBar) findViewById(R.id.pb_detailed);

        enteredQtyEt = (EditText) findViewById(R.id.et_detailed_enter_quantity);
        addToCartBtn = (Button) findViewById(R.id.btn_add_item_to_cart);

        mAuth = FirebaseAuth.getInstance();        // Authentication
        ownerId = mAuth.getCurrentUser().getUid();

        db = FirebaseFirestore.getInstance();     // Database

        Intent intent = getIntent();
        pdTitle = intent.getStringExtra("SP_TITLE");
        pdQuantity =intent.getDoubleExtra("SP_QUANTITY", 0.0);
        pdDescription = intent.getStringExtra("SP_DESCRIPTION");
        pdOwnerName = intent.getStringExtra("SP_NAME");
        pdOwnerMobileNo = intent.getStringExtra("SP_MOBILE");
        pdPostId = intent.getStringExtra("SP_POST_ID");
        pdOwnerId = intent.getStringExtra("SP_OWNER_ID");
        pdCategory = intent.getStringExtra("SP_CATEGORY");
        pdDistrict = intent.getStringExtra("SP_DISTRICT");
        pdLocation = intent.getStringExtra("SP_LOCATION");
        pdPrice = intent.getStringExtra("SP_PRICE");
        pdImageUrl = intent.getStringExtra("SP_IMG_URL");
        pdAvailableDateFrom = intent.getStringExtra("SP_AVAILABLE_FROM");
        pdAvailableDateTo = intent.getStringExtra("SP_AVAILABLE_TO");
        pdHarvestedDate = intent.getStringExtra("SP_DATE_HARVESTED");
        pdGrade = intent.getStringExtra("SP_GRADE");

        // setting textViews
        titleTv.setText(pdTitle);
        quantityTv.append(" "+ pdQuantity);
        descriptionTv.setText(pdDescription);
        nameTv.setText(pdOwnerName);
        mobileNoTv.setText(pdOwnerMobileNo);

        enteredQtyEt.setHint("Max. " + pdQuantity);

        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCartTapped();
            }
        });

        mobileNoTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialer();
            }
        });

    }

    private void openDialer() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + pdOwnerMobileNo));
        startActivity(intent);

    }

    public boolean validateQuantity(){

        String userEnteredQtyStringTemp = enteredQtyEt.getText().toString();

        if(userEnteredQtyStringTemp.isEmpty()){
            enteredQtyEt.setError("Please enter Quantity");
            enteredQtyEt.requestFocus();
            return false;
        }
        else {

            double userEnteredQtyDoubleTemp = Double.parseDouble(userEnteredQtyStringTemp);
            if(userEnteredQtyDoubleTemp > pdQuantity){
                enteredQtyEt.setError("Invalid Quantity!");
                enteredQtyEt.requestFocus();
                return false;
            }
            else return true;

        }

    }

    private void addToCartTapped() {

        if(validateQuantity()){
            Toast.makeText(SalePostDetailed.this, "Qty validation Returned True", Toast.LENGTH_LONG).show();
            addItemToCart();
        }

    }

    private void addItemToCart() {

        waitPbar.setVisibility(View.VISIBLE);
        WaitTv.setVisibility(View.VISIBLE);
        addToCartBtn.setClickable(false);

        // Old
        /*
        String ownerId = mAuth.getCurrentUser().getUid();
        // Database entry
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String userEnteredQtyString = enteredQtyEt.getText().toString();
        double userEnteredQtyDouble = Double.parseDouble(userEnteredQtyString);
        double totalCartItemPrice = (userEnteredQtyDouble)*(Double.parseDouble(pdPrice));

         */

        // New
        String userEnteredQtyString = enteredQtyEt.getText().toString();
        userEnteredQtyDouble = Double.parseDouble(userEnteredQtyString);
        totalCartItemPrice = (userEnteredQtyDouble)*(Double.parseDouble(pdPrice));

        //// New

        CollectionReference cartItemsCollRef = db.collection("Users").document(ownerId).collection("CartItems");
        Query query = cartItemsCollRef.whereEqualTo("cartItemPostId", pdPostId);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                boolean cartUpdated = false;

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //Log.d(TAG, document.getId() + " => " + document.getData());

                        cartUpdated = true;
                        //Log.d(TAG, "Document exists!");
                        // Updating existing Cart Item (Merge)
                        //
                        double existingQty = (double) document.getDouble("cartItemQuantity");
                        double qtyIncrement;
                        double priceIncrement;

                        if((userEnteredQtyDouble + existingQty) > pdQuantity){      // To Avoid newly adding quantity exceeding the available quantity
                            qtyIncrement = pdQuantity - existingQty;  // Adding Maximum possible amount
                            priceIncrement = (qtyIncrement)*(Double.parseDouble(pdPrice));
                        }
                        else{
                            qtyIncrement = userEnteredQtyDouble;
                            priceIncrement = totalCartItemPrice;
                        }


                        document.getReference().update("cartItemQuantity", FieldValue.increment(qtyIncrement), "cartItemTotalPrice", FieldValue.increment(priceIncrement), "cartItemDateAdded", new Date() )
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //Log.d(TAG, "DocumentSnapshot successfully updated!");
                                        //

                                        Toast.makeText(SalePostDetailed.this, "Item Added", Toast.LENGTH_LONG).show();

                                        waitPbar.setVisibility(View.GONE);
                                        WaitTv.setVisibility(View.GONE);
                                        addToCartBtn.setClickable(true);

                                        // Alert Dialog on success
                                        AlertDialog.Builder builder = new AlertDialog.Builder(SalePostDetailed.this);
                                        builder.setTitle("Item Added!")
                                                .setMessage("Cart updated successfully")
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        finish();
                                                    }
                                                });


                                        AlertDialog alertDialog = builder.create();
                                        alertDialog.show();
                                        //


                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        //Log.w(TAG, "Error updating document", e);

                                        //
                                        Toast.makeText(SalePostDetailed.this, "Failed to add to the cart!", Toast.LENGTH_LONG).show();

                                        waitPbar.setVisibility(View.GONE);
                                        WaitTv.setVisibility(View.GONE);
                                        addToCartBtn.setClickable(true);

                                        // Alert Dialog on failure
                                        AlertDialog.Builder builder = new AlertDialog.Builder(SalePostDetailed.this);
                                        builder.setTitle("Item Adding Failed!")
                                                .setMessage("Failed to update the cart")
                                                .setPositiveButton("Go Back", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        finish();
                                                    }
                                                })
                                                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        addToCartTapped();
                                                    }
                                                });


                                        AlertDialog alertDialog = builder.create();
                                        alertDialog.show();
                                        //

                                    }
                                });
                        //


                        //


                    }

                    // Prev. location
                    //

                    if(!cartUpdated){
                        //
                        // User doesn't have this Item added to his Cart. so, instead of updating, app creates new CartItem
                        // Creating new cart item
                        CartItem cartItem = new CartItem(pdPostId, pdOwnerId, new Date(), pdTitle, pdCategory, userEnteredQtyDouble, pdPrice, totalCartItemPrice, pdDistrict, pdLocation);
                        //

                        db.collection("Users").document(ownerId).collection("CartItems")
                                .add(cartItem)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Log.d("TAG", "Add to Cart success");
                                        Toast.makeText(SalePostDetailed.this, "Item Added", Toast.LENGTH_LONG).show();

                                        waitPbar.setVisibility(View.GONE);
                                        WaitTv.setVisibility(View.GONE);
                                        addToCartBtn.setClickable(true);

                                        // Alert Dialog on success
                                        AlertDialog.Builder builder = new AlertDialog.Builder(SalePostDetailed.this);
                                        builder.setTitle("Item Added!")
                                                .setMessage("Item added to the cart successfully")
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        finish();
                                                    }
                                                });


                                        AlertDialog alertDialog = builder.create();
                                        alertDialog.show();
                                        //

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("TAG", "Details upload Failed");
                                        Toast.makeText(SalePostDetailed.this, "Failed to add to the cart!", Toast.LENGTH_LONG).show();

                                        waitPbar.setVisibility(View.GONE);
                                        WaitTv.setVisibility(View.GONE);
                                        addToCartBtn.setClickable(true);

                                        // Alert Dialog on failure
                                        AlertDialog.Builder builder = new AlertDialog.Builder(SalePostDetailed.this);
                                        builder.setTitle("Item Adding Failed!")
                                                .setMessage("Failed to add item to the cart")
                                                .setPositiveButton("Go Back", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        finish();
                                                    }
                                                })
                                                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        addToCartTapped();
                                                    }
                                                });


                                        AlertDialog alertDialog = builder.create();
                                        alertDialog.show();
                                        //

                                    }
                                });
                        //
                        //
                    }

                    //

                } else {
                    //Log.d(TAG, "Error getting documents: ", task.getException());

                }
            }
        });
        ////


        //  ORIGINAL BELOW
        /*
        CartItem cartItem = new CartItem(pdPostId, pdOwnerId, new Date(), pdTitle, pdCategory, userEnteredQtyDouble, pdPrice, totalCartItemPrice, pdDistrict, pdLocation);
        //

        //


        db.collection("Users").document(ownerId).collection("CartItems")
                .add(cartItem)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("TAG", "Add to Cart success");
                        Toast.makeText(SalePostDetailed.this, "Item Added", Toast.LENGTH_LONG).show();

                        waitPbar.setVisibility(View.GONE);
                        WaitTv.setVisibility(View.GONE);
                        addToCartBtn.setClickable(true);

                        // Alert Dialog on success
                        AlertDialog.Builder builder = new AlertDialog.Builder(SalePostDetailed.this);
                        builder.setTitle("Item Added!")
                                .setMessage("Item added to the cart successfully")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                });


                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        //

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Details upload Failed");
                        Toast.makeText(SalePostDetailed.this, "Failed to add to the cart!", Toast.LENGTH_LONG).show();

                        waitPbar.setVisibility(View.GONE);
                        WaitTv.setVisibility(View.GONE);
                        addToCartBtn.setClickable(true);

                        // Alert Dialog on failure
                        AlertDialog.Builder builder = new AlertDialog.Builder(SalePostDetailed.this);
                        builder.setTitle("Item Adding Failed!")
                                .setMessage("Failed to add item to the cart")
                                .setPositiveButton("Go Back", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                })
                                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        addToCartTapped();
                                    }
                                });


                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        //

                    }
                });

        // ORIGINAL OVER

         */

    }


    public void onBackIconTapped(View view) {
        onBackPressed();
    }
}