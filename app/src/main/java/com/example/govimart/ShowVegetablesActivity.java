package com.example.govimart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

//
public class ShowVegetablesActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference postsRef = db.collection("Posts");
    private SalePostAdapter adapter;
    private TextView categoryTitleBarTv, noItemsTv;
    private RecyclerView showPostsRecyclerViewView;
    private String selectedCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_vegetables);

        categoryTitleBarTv = (TextView) findViewById(R.id.tv_category_title_bar);
        noItemsTv = (TextView) findViewById(R.id.tv_show_vegetables_no_items);
        showPostsRecyclerViewView = (RecyclerView) findViewById(R.id.vegetablesRecyclerView);

        Intent initialIntent = getIntent();
        selectedCategory = initialIntent.getStringExtra("CATEGORY_ON_CARD");
        categoryTitleBarTv.setText(selectedCategory);

        /*
        FloatingActionButton buttonAddNote = findViewById(R.id.button_add_note);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NewNoteActivity.class));
            }
        });

         */

        setUpRecyclerView();
    }
    private void setUpRecyclerView() {
        Query query = postsRef.whereEqualTo("pCategory", selectedCategory).orderBy("pDateAdded", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<SalePost> options = new FirestoreRecyclerOptions.Builder<SalePost>()
                .setQuery(query, SalePost.class)
                .build();
        adapter = new SalePostAdapter(options);
        adapter.startListening();
        adapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);
        RecyclerView recyclerView = findViewById(R.id.vegetablesRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        // To handle Empty RecyclerView
        adapter.setOnAdapterCountListener(new SalePostAdapter.OnAdapterCountListener() {
            @Override
            public void onAdapterCountListener(int count) {
                if (count > 0){
                    //adapterEmptyText.setVisibility(View.GONE);
                    noItemsTv.setVisibility(View.GONE);
                    showPostsRecyclerViewView.setVisibility(View.VISIBLE);
                }else {
                    noItemsTv.setVisibility(View.VISIBLE);
                    showPostsRecyclerViewView.setVisibility(View.GONE);
                }

            }
        });
        //


        // If we want to implement Swipe to delete(Only the Firestore records here)
        /*
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.deleteItem(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);

         */

        adapter.setOnItemClickListener(new SalePostAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                SalePost salePost = documentSnapshot.toObject(SalePost.class);
                String id = documentSnapshot.getId();
                String path = documentSnapshot.getReference().getPath();
                Toast.makeText(ShowVegetablesActivity.this,
                        "Position: " + position + " ID: " + id, Toast.LENGTH_SHORT).show();

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
    }

    // if intend to use a dialog
    public void updateCart(String chosenQtyString){

        //String chosenQuantityString = chosenQuantityEt.getText().toString().trim();
        //double chosenQuantityDouble = Double.parseDouble(chosenQuantityString);

        Toast.makeText(ShowVegetablesActivity.this, "Chosen Qty is " + chosenQtyString, Toast.LENGTH_SHORT).show();

    }

    /*@Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }*/
    /*@Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }*/



}

//
/*
public class ShowVegetablesActivity extends AppCompatActivity implements SalePostAdapter.OnItemClickListener {
    private RecyclerView mRecyclerView;
    private SalePostAdapter mAdapter;
    private ProgressBar mProgressCircle;
    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private List<SalePost> mUploads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_vegetables);

        mRecyclerView = findViewById(R.id.vegetablesRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mProgressCircle = findViewById(R.id.progress_circle);
        mUploads = new ArrayList<>();
        mAdapter = new SalePostAdapter(ShowVegetablesActivity.this, mUploads);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(ShowVegetablesActivity.this);
        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
        //
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference mCollectionRef = db.collection("Posts");
        //
        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUploads.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    SalePost upload = postSnapshot.getValue(SalePost.class);
                    upload.setKey(postSnapshot.getKey());
                    mUploads.add(upload);
                }
                mAdapter.notifyDataSetChanged();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ShowVegetablesActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
    }
    @Override
    public void onItemClick(int position) {
        Toast.makeText(this, "Normal click at position: " + position, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onWhatEverClick(int position) {
        Toast.makeText(this, "Whatever click at position: " + position, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onDeleteClick(int position) {
        SalePost selectedItem = mUploads.get(position);
        final String selectedKey = selectedItem.getKey();
        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getPostImageUrl());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mDatabaseRef.child(selectedKey).removeValue();
                Toast.makeText(ShowVegetablesActivity.this, "Item deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }
}

 */