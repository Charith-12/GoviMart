package com.example.govimart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogInActivity extends AppCompatActivity {
    TextView CreateNewAccount, ForgotPassword;
    EditText Email, Password;
    Button LogIn;
    CheckBox RememberMe;


    private FirebaseAuth mAuth;
    String email, password;
    Boolean remember_me,RM;

    //<!-- variables for  Shard Preferences -->
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String EMAIL = "email";
    public static final String PASSWORD  = "password";
    public static final String REMEMBER_ME  = "rememberME";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        CreateNewAccount = findViewById(R.id.tv_CreateNewAccount);
        Email = findViewById(R.id.et_EmailLogIN);
        Password = findViewById(R.id.et_PasswordLogIn);
        LogIn =  findViewById(R.id.btn_LogIn);
        ForgotPassword = findViewById(R.id.tv_ForgotPassword);
        RememberMe = findViewById(R.id.cb_RememberMe);


        mAuth = FirebaseAuth.getInstance();



        //<!-- Remember Me  DATA LOAD -->
        LoadLogInData();

        //<!-- Remember Me  DATA Update -->
        UpdateLogInData();




        LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                UserLogIn();
                IsRememberME();
                LoginUser();


            }
        });

        ForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this, PasswordRecoveryActivity.class);
                startActivity(intent);

            }
        });


        CreateNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this, NewUserAccountCreate1.class);
                startActivity(intent);

            }
        });




    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);
    }

    public boolean ValidateDataInput() {
        String password = Password.getText().toString().trim();
        String email = Email.getText().toString().trim();

        if (email.isEmpty()) {
            Email.setError("Please Enter your Email");
            Email.requestFocus();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Email.setError("Please enter a valid Email");
            Email.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            Password.setError("Password is Required");
            Password.requestFocus();
            return false;
        } else {
            return true;
        }


    }

    public void LoginUser() {
        if (ValidateDataInput()) {
            UserLogIn();
        }
    }

    public void UserLogIn() {
        password = Password.getText().toString().trim();
        email = Email.getText().toString().trim();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "signInWithEmail:success");
                    Toast.makeText(LogInActivity.this, "Authentication done.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LogInActivity.this, RedirectActivity.class);
                    startActivity(intent);

                } else {
                    Log.w("TAG", "signInWithEmail:success");
                    Toast.makeText(LogInActivity.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    //<!-- Remember Me  checked? -->
    public  void IsRememberME(){
        if (RememberMe.isChecked()){
            SaveLogInData();
        }
        else{
            DeleteLogInData();
        }
    }

    private void DeleteLogInData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(EMAIL,"");
        editor.putString(PASSWORD,"");
        editor.putBoolean(REMEMBER_ME,false);
        editor.apply();
        Toast.makeText(this,"Data deleted ",Toast.LENGTH_SHORT).show();
    }

    //<!-- Remember Me  Save data method -->
    public  void  SaveLogInData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(EMAIL,Email.getText().toString());
        editor.putString(PASSWORD,Password.getText().toString());
        editor.putBoolean(REMEMBER_ME,RememberMe.isChecked());
        editor.apply();
        Toast.makeText(this,"Data Saved ",Toast.LENGTH_SHORT).show();

    }

    //<!-- Remember Me  Load data method -->
    public void LoadLogInData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        email = sharedPreferences.getString(EMAIL,"");
        password = sharedPreferences.getString(PASSWORD,"");
        remember_me = sharedPreferences.getBoolean(REMEMBER_ME,false);

    }

    //<!-- Remember Me  Update data method -->
    public void UpdateLogInData(){
        Email.setText(email);
        Password.setText(password);
        RememberMe.setChecked(remember_me);

    }


}