package com.example.govimart;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class EditUserProfileActivity extends AppCompatActivity {
    Button recoverPassword, saveUserData;
    //    private Spinner Gender;
    EditText firstName, lastName, email, phoneNo, address, userName;
    ImageView profileImageView;
    Toolbar toolbar;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    StorageReference storageReference;

    private String U_ID;
    private FirebaseUser UsersName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);

        recoverPassword = findViewById(R.id.btn_ResetPassword);
        saveUserData = findViewById(R.id.btn_SaveUserData);

        userName = findViewById(R.id.et_ProfileUserName_edit);
        firstName = findViewById(R.id.et_ProfileFirstName_edit);
        lastName = findViewById(R.id.et_ProfileLastName_edit);
        email = findViewById(R.id.et_ProfileEmail_edit);
        phoneNo = findViewById(R.id.et_ProfileMobileNo_edit);
        address = findViewById(R.id.et_ProfileAddress_edit);

        profileImageView = findViewById(R.id.iv_ProfileImage_edit);

        toolbar = findViewById(R.id.toolbarEditProfile);
        setSupportActionBar(toolbar);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        U_ID = fAuth.getCurrentUser().getUid();
        UsersName = fAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();



        LoadProfileImage();
        GetUserData();

/*        Bundle UserDataBundleReceived= getIntent().getExtras();
        final String UserName = UserDataBundleReceived.getString("UserName");
        final String MobileNo = UserDataBundleReceived.getString("MobileNo");
        final String Email = UserDataBundleReceived.getString("Email");
        final String Address = UserDataBundleReceived.getString("Address");

        userName.setText(UserName);
        phoneNo.setText(MobileNo);
        email.setText(Email);
        address.setText(Address);*/









        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditUserProfileActivity.this,ProfileActivity.class);
                startActivity(intent);
                finish();;
            }
        });

        saveUserData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveData();


            }
        });


        recoverPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditUserProfileActivity.this, PasswordRecoveryActivity.class);
                startActivity(intent);

            }
        });

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();

            }
        });
    }

    private void SaveData() {
        if(ValidateEditedDataInput()){

            final String Email = email.getText().toString().trim();
            final String FstName = firstName.getText().toString().trim();
            final String LstName = lastName.getText().toString().trim();
            final String UsrName = userName.getText().toString().trim();
            final String MobileNo = phoneNo.getText().toString().trim();
            final String Address = address.getText().toString().trim();

            UsersName.updateEmail(Email).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    DocumentReference docReference = fStore.collection("Users").document(U_ID);
                    Map<String,Object> editedUser = new HashMap<>();
                    editedUser.put("FirstName",FstName);
                    editedUser.put("LastName",LstName);
                    editedUser.put("UserName",UsrName);
                    editedUser.put("MobileNo",MobileNo);
                    editedUser.put("Address",Address);
                    editedUser.put("Email",Email);
                    docReference.update(editedUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(EditUserProfileActivity.this, "update change Success", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EditUserProfileActivity.this, ProfileActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });


                    Toast.makeText(EditUserProfileActivity.this, "Email change Success", Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditUserProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });

        }



    }


    private void GetUserData() {
        final DocumentReference documentReference = fStore.collection("Users").document(U_ID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                userName.setText(value.getString("UserName"));
                firstName.setText(value.getString("FirstName"));
                lastName.setText(value.getString("LastName"));
                email.setText(value.getString("Email"));
                phoneNo.setText(value.getString("MobileNo"));
                address.setText(value.getString("Address"));
            }
        });
    }

    private void openGallery() {
        Intent OpenGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(OpenGalleryIntent, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageProfileUri = data.getData();
//                profile_imageView.setImageURI(imageProfileUri);


                UploadImageToFirebase(imageProfileUri);

            }

        }
    }

    private void UploadImageToFirebase(Uri imageProfileUri) {
        final StorageReference fReference = storageReference.child("Users/" + U_ID + "/ProfileImage");
        //        final StorageReference fReference = storageReference.child("Users/"+"ProfileImage/"+U_ID);
        fReference.putFile(imageProfileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profileImageView);
                    }
                });
                Toast.makeText(EditUserProfileActivity.this, "Success", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditUserProfileActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void LoadProfileImage() {
        StorageReference ProfileRef = storageReference.child("Users/" + U_ID + "/ProfileImage");
        //        StorageReference ProfileRef = storageReference.child("Users/"+"ProfileImage/"+U_ID);
        ProfileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImageView);
            }
        });
    }

    private boolean ValidateEditedDataInput(){
        String UsrName = userName.getText().toString();
        String FstName = firstName.getText().toString();
        String LstName = lastName.getText().toString();
        String Mobile = phoneNo.getText().toString();
        String Email = email.getText().toString().trim();
        String Address = address.toString();

        if (UsrName.isEmpty()){
            userName.setError("User Name is Required");
            userName.requestFocus();
            return false;
        }
        if (FstName.isEmpty()){
            firstName.setError("First Name is Required");
            firstName.requestFocus();
            return false;
        }
        if(LstName.isEmpty()){
            lastName.setError("Last Name is Required");
            lastName.requestFocus();
            return false;
        }
        if (Email.isEmpty()) {
            email.setError("Email is Required");
            email.requestFocus();
            return false;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            email.setError("Please enter a valid Email");
            email.requestFocus();
            return false;
        }
        if(Mobile.isEmpty()){
            phoneNo.setError("Mobile No is Required");
            phoneNo.requestFocus();
            return false;
        }

        if (Mobile.length() != 10) {
            phoneNo.setError("Please enter a valid Mobile No");
            phoneNo.requestFocus();
            return false;
        }
        if(!Patterns.PHONE.matcher(Mobile).matches()) {
            phoneNo.setError("Please enter a valid Mobile No");
            phoneNo.requestFocus();
            return false;
        }

        if(Address.isEmpty()){
            address.setError("Mobile No is Required");
            address.requestFocus();
            return false;
        }
/*        if(EmailCheckDB()){

        }*/

        else {
            return true;
        }



    }



}