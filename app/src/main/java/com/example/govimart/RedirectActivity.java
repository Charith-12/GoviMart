package com.example.govimart;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class RedirectActivity extends AppCompatActivity {
    TextView Role;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private StorageReference storageReference;

    String U_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redirect);

        Role = findViewById(R.id.tv_role);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        U_ID =mAuth.getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference();

        DocumentReference documentReference = fStore.collection("Users").document(U_ID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                Role.setText(value.getString("Role"));
                RedirectHome();
            }
        });

    }
    private void RedirectHome() {
        String role = Role.getText().toString().trim();
        if(role.equals("Seller and Buyer")){
            Intent intent = new Intent(RedirectActivity.this, SellerMainActivityHome.class);
            startActivity(intent);
            finish();

        }
        else if(role.equals("Buyer")){
            Intent intent = new Intent(RedirectActivity.this, BuyerMainActivityHome.class);
            startActivity(intent);
            finish();

        }
        else if (role.equals("Seller and Transporter")){
            Intent intent = new Intent(RedirectActivity.this, Seller_and_TransporterMainActivityHome.class);
            startActivity(intent);
            finish();

        }
        else if (role.equals("Buyer and Transporter")){
            Intent intent = new Intent(RedirectActivity.this, Buyer_and_TransporterMainActivityHome.class);
            startActivity(intent);
            finish();

        }
        else{
            Intent intent = new Intent(RedirectActivity.this, SellerMainActivityHome.class);
            startActivity(intent);
            finish();

        }
    }
}