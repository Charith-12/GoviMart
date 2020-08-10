package com.example.govimart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordRecoveryActivity extends AppCompatActivity {
    private EditText EmailPR;
    private Button PasswordReset;

    Toolbar toolbar;

    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recovery);


        EmailPR = findViewById(R.id.et_EmailPasswordRecover);
        PasswordReset = findViewById(R.id.btn_SendResetPasswordLink);

        fAuth = FirebaseAuth.getInstance();

        toolbar = findViewById(R.id.toolbarPasswordRecovery);
        setSupportActionBar(toolbar);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PasswordRecoveryActivity.this,LogInActivity.class);
                startActivity(intent);
            }
        });


        PasswordReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendRecoveryLink();
                Intent intent = new Intent(PasswordRecoveryActivity.this, LogInActivity.class);
                startActivity(intent);

            }
        });




    }

    private void SendRecoveryLink() {
        String email = EmailPR.getText().toString().trim();
        fAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(PasswordRecoveryActivity.this,"Password Reset link sent",Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PasswordRecoveryActivity.this,"Password Reset link sent Failed",Toast.LENGTH_SHORT).show();

            }
        });


    }


}