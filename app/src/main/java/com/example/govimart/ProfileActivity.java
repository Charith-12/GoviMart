package com.example.govimart;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    private TextView user_name,phone,email,role,address,gender;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    StorageReference storageReference;
    String U_ID;
    DocumentReference UserName;
    ImageView profile_imageView;
    Toolbar toolbar;

    private Button editUserData,logOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        user_name = findViewById(R.id.tv_ProfileUserNameText);
        email = findViewById(R.id.tv_ProfileUserEmailText);
        phone = findViewById(R.id.tv_ProfileUserMobileNoText);
        gender = findViewById(R.id.tv_ProfileUserGenderText);
        address = findViewById(R.id.tv_ProfileAddressText);
        role = findViewById(R.id.tv_ProfileRole);
        /*recoverPassword = findViewById(R.id.btn_ResetPassword);*/
        editUserData = findViewById(R.id.btn_EditUserData);
        profile_imageView = findViewById(R.id.iv_ProfileImage);





        logOut = findViewById(R.id.btn_LogOut);


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        U_ID =fAuth.getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference();

        /* UserName = fStore.collection("Users").document("UserName");*/
        displayUserData();
        LoadProfileImage();

        toolbar = findViewById(R.id.toolbarProfile);
        setSupportActionBar(toolbar);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, SellerMainActivityHome.class);
                startActivity(intent);
            }
        });



/*        recoverPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, PasswordRecoveryActivity.class);
                startActivity(intent);

            }
        });*/

/*        profile_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();

            }
        });*/

        editUserData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this,EditUserProfileActivity.class);
/*                String UserName = user_name.getText().toString().trim();
                String Mobile = phone.getText().toString().trim();
                String Email = email.getText().toString().trim();
                String Address = address.getText().toString().trim();
//                String gender = GenderSelected.toString();



                Bundle UserDataBundle= new Bundle();
                UserDataBundle.putString("UserName",UserName);
                UserDataBundle.putString("MobileNo",Mobile);
                UserDataBundle.putString("Email",Email);
                UserDataBundle.putString("Address",Address);

                intent.putExtras(UserDataBundle);*/
                startActivity(intent);

            }
        });


        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogOut();

            }
        });



    }
    private void displayUserData() {
        DocumentReference documentReference = fStore.collection("Users").document(U_ID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                user_name.setText(value.getString("UserName"));
                email.setText(value.getString("Email"));
                phone.setText(value.getString("MobileNo"));
                gender.setText(value.getString("Gender"));
                address.setText(value.getString("Address"));
                role.setText(value.getString("Role"));
            }
        });
    }

    private void LogOut() {
        fAuth.signOut();

        Intent intent = new Intent(ProfileActivity.this, LogInActivity.class);
        startActivity(intent);

        finish();
    }


    private void LoadProfileImage() {
        StorageReference ProfileRef = storageReference.child("Users/"+U_ID+"/ProfileImage");
        //        StorageReference ProfileRef = storageReference.child("Users/"+"ProfileImage/"+U_ID);
        ProfileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profile_imageView);
            }
        });
    }

/*    private void openGallery() {
        Intent OpenGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(OpenGalleryIntent,1000);
    }*/

/*    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                Uri imageProfileUri = data.getData();
//                profile_imageView.setImageURI(imageProfileUri);


                UploadImageToFirebase(imageProfileUri);

            }

        }
    }*/

/*    private void UploadImageToFirebase(Uri imageProfileUri) {
        final StorageReference fReference = storageReference.child("Users/"+U_ID+"/ProfileImage");
        //        final StorageReference fReference = storageReference.child("Users/"+"ProfileImage/"+U_ID);
        fReference.putFile(imageProfileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profile_imageView);
                    }
                });
                Toast.makeText(ProfileActivity.this,"Success",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileActivity.this,"Failed",Toast.LENGTH_SHORT).show();
            }
        });

    }*/


}