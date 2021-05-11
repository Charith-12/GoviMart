package com.example.govimart;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CartTabFragment extends Fragment {
    private static final String TAG = "CartTabFragment";

    //
    //private Button btnTEST;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference postsRef = db.collection("Posts");  // to update original post's remaining quantity
    private CollectionReference usersRef = db.collection("Users");
    private CartItemAdapter adapter;
    Button getTotalBtn, checkOutBtn;
    TextView cartTotalTv, cartIsEmptyTv;
    RecyclerView cartRecyclerViewView;
    private View view;

    double shoppingCartTotalValue;
    int cartItemCount;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //
        view = inflater.inflate(R.layout.cart_fragment_layout,container,false);
        //

        mAuth = FirebaseAuth.getInstance();


        /*
        // original -> // mRecyclerView = findViewById(R.id.homeCategoryRecyclerView);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.homeCategoryRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        // original -> // mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager = new LinearLayoutManager(getActivity());
        // Alternate to above -> // mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new MainCategoryCardAdapter(mainCategoriesList, getActivity()); //mAdapter = new MainCategoryCardAdapter(getActivity(), mainCategoriesList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter); */
        //

        // prev -> // return inflater.inflate(R.layout.categories_fragment_layout,container,false);
        //View view = inflater.inflate(R.layout.categories_fragment_layout,container,false);
        cartTotalTv = (TextView) view.findViewById(R.id.tv_cart_total);
        cartIsEmptyTv = (TextView) view.findViewById(R.id.tv_cart_is_empty);
        cartRecyclerViewView = (RecyclerView) view.findViewById(R.id.cart_fragment_recyclerView);

        checkOutBtn = (Button) view.findViewById(R.id.btn_cart_check_out);
        getTotalBtn = (Button) view.findViewById(R.id.btn_cart_view_total);

        checkOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(cartItemCount == 0){
                    //Toast.makeText(getActivity(), "CHECKOUT BUTTON CLICKED",Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(), "Your Cart is Empty!",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getActivity(), "CHECKOUT BUTTON CLICKED",Toast.LENGTH_SHORT).show();
                    cartTotalTv.setVisibility(View.GONE);
                    getTotalBtn.setVisibility(View.VISIBLE);
                    cartTotalTv.setText("Calculating...");
                    Intent intent = new Intent(getContext(), CheckoutActivity.class);
                    startActivity(intent);

                }


            }
        });

        getTotalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTotalBtn.setVisibility(View.GONE);
                cartTotalTv.setVisibility(View.VISIBLE);
                cartTotalTv.setText("Calculating...");
                Toast.makeText(getActivity(), "Calculating..",Toast.LENGTH_SHORT).show();
                getCartTotal();
            }
        });




        ///
        setUpRecyclerView(view);
        //getCartTotal();
        //cartTotalTv.setText(String.format("Cart Total : %s", shoppingCartTotalValue));

        ///




        return view;

    }

    private void setUpRecyclerView(View view){

        ///
        String ownerId = mAuth.getCurrentUser().getUid();
        //
        Query query = usersRef.document(ownerId).collection("CartItems").orderBy("cartItemDateAdded", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<CartItem> options = new FirestoreRecyclerOptions.Builder<CartItem>()
                .setQuery(query, CartItem.class)
                .build();
        adapter = new CartItemAdapter(options);
        adapter.startListening();

        adapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.cart_fragment_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        //cartItemCount = adapter.getItmCount();

        // To handle Empty Cart
        adapter.setOnAdapterCountListener(new CartItemAdapter.OnAdapterCountListener() {
            @Override
            public void onAdapterCountListener(int count) {
                if (count > 0){
                    //adapterEmptyText.setVisibility(View.GONE);
                    cartItemCount = 1;
                    cartIsEmptyTv.setVisibility(View.GONE);
                    cartRecyclerViewView.setVisibility(View.VISIBLE);
                }else {
                    cartItemCount =0;
                    cartIsEmptyTv.setVisibility(View.VISIBLE);
                    cartRecyclerViewView.setVisibility(View.GONE);
                }

            }
        });
        //



        // Swipe to delete feature

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.deleteItem(viewHolder.getAdapterPosition());
                cartTotalTv.setVisibility(View.GONE);
                getTotalBtn.setVisibility(View.VISIBLE);
                cartTotalTv.setText("Calculating...");

            }
        }).attachToRecyclerView(recyclerView);


        // On Cart Item click
        adapter.setOnItemClickListener(new CartItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                CartItem cartItem = documentSnapshot.toObject(CartItem.class);
                String cartItemId = documentSnapshot.getId();
                String path = documentSnapshot.getReference().getPath();
                Toast.makeText(getContext(), "Position: " + position + " ID: " + cartItemId, Toast.LENGTH_SHORT).show();



                /*
                Intent intent = new Intent(ShowVegetablesActivity.this, SalePostDetailed.class);
                assert salePost != null;
                intent.putExtra("SP_TITLE", salePost.getPTitle());
                intent.putExtra("SP_QUANTITY", salePost.getPQuantity());
                intent.putExtra("SP_DESCRIPTION", salePost.getPDescription());
                intent.putExtra("SP_NAME", salePost.getPOwnerName());
                intent.putExtra("SP_MOBILE", salePost.getPOwnerMobileNo());
                intent.putExtra("SP_POST_ID", id);
                intent.putExtra("SP_OWNER_ID", salePost.getPOwnerId());
                intent.putExtra("SP_CATEGORY", salePost.getPCategory());
                intent.putExtra("SP_DISTRICT", salePost.getPDistrict());
                intent.putExtra("SP_LOCATION", salePost.getPLocation());
                intent.putExtra("SP_PRICE", salePost.getPPrice());
                intent.putExtra("SP_IMG_URL", salePost.getPImageUrl());
                intent.putExtra("SP_AVAILABLE_FROM", salePost.getPAvailableDateFrom());
                intent.putExtra("SP_AVAILABLE_TO", salePost.getPAvailableDateTo());
                intent.putExtra("SP_DATE_HARVESTED", salePost.getPHarvestedDate());
                intent.putExtra("SP_GRADE", salePost.getPGrade());

                startActivity(intent);

                 */


                /*
                // Show Dialog on Item Tap
                Dialog mDialog = new Dialog(ShowVegetablesActivity.this);
                mDialog.setContentView(R.layout.dialog_enter_quantity);
                mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mDialog.show();

                final EditText chosenQuantityEt = (EditText) mDialog.findViewById(R.id.et_dialog_enter_quantity);
                Button addToCartBtn = (Button) mDialog.findViewById(R.id.btn_dialog_add_to_cart);

                addToCartBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Toast.makeText(ShowVegetablesActivity.this, "Add to cart clicked", Toast.LENGTH_SHORT).show();
                        String chosenQuantityString = chosenQuantityEt.getText().toString().trim();
                        updateCart(chosenQuantityString);

                    }
                });
                */

            }
        });
        //
        ///

    }

    private void getCartTotal(){

        final double[] cartTotalValue = {0.0};
        String ownerId = mAuth.getCurrentUser().getUid();
        //
        Query queryForTotal = usersRef.document(ownerId).collection("CartItems");
        queryForTotal.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                cartTotalValue[0] = cartTotalValue[0] + (double) document.get("cartItemTotalPrice");

                            }
                            shoppingCartTotalValue = cartTotalValue[0];
                            //setUpRecyclerView(view);
                            cartTotalTv.setText(String.format("Cart Total : %s", shoppingCartTotalValue));

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                            Toast.makeText(getContext(), "Error getting Cart Total!", Toast.LENGTH_LONG).show();
                            shoppingCartTotalValue = -1.0;
                            //setUpRecyclerView(view);
                            cartTotalTv.setText(String.format("Cart Total : %s", shoppingCartTotalValue));
                        }
                    }
                });


    }



}
