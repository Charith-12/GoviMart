package com.example.govimart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class NewUserAccountCreate1 extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText FirstName,LastName,MobileNO,Email;
    Spinner Gender;
    Button NextStep,Back;
    String GenderSelected;
/*    String EmailCheck;
    FirebaseFirestore db = FirebaseFirestore.getInstance();*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_account_create1);

        FirstName = (EditText)findViewById(R.id.et_FirstName);
        LastName = (EditText)findViewById(R.id.et_LastName);
        MobileNO = (EditText)findViewById(R.id.et_MobileNo);
        Email = (EditText)findViewById(R.id.et_Email);
        Gender = (Spinner) findViewById(R.id.spi_Gender);
        NextStep = (Button) findViewById(R.id.btn_NextStep);
        Back = (Button)findViewById(R.id.btn_Back1);


        ArrayAdapter<String> GenderAdepter = new ArrayAdapter<String>(NewUserAccountCreate1.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.Gender));
        GenderAdepter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Gender.setAdapter(GenderAdepter);
        Gender.setOnItemSelectedListener(this);


        NextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenNewAccountConectivity();

            }
        });

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

    }
    public boolean ValidateDataInput() {
        String FstName = FirstName.getText().toString();
        String LstName = LastName.getText().toString();
        String Mobile = MobileNO.getText().toString();
        String email = Email.getText().toString().trim();
        String gender = GenderSelected.toString();

        if (FstName.isEmpty()) {
            FirstName.setError("First Name is Required");
            FirstName.requestFocus();
            return false;
        }
        if (LstName.isEmpty()) {
            LastName.setError("Last Name is Required");
            LastName.requestFocus();
            return false;
        }
        if (Mobile.isEmpty()) {
            MobileNO.setError("Mobile No is Required");
            MobileNO.requestFocus();
            return false;
        }
        if (Mobile.length() != 10) {
            MobileNO.setError("Please enter a valid Mobile No");
            MobileNO.requestFocus();
            return false;
        }
        if (!Patterns.PHONE.matcher(Mobile).matches()) {
            MobileNO.setError("Please enter a valid Mobile No");
            MobileNO.requestFocus();
            return false;
        }
        if (email.isEmpty()) {
            Email.setError("Email is Required");
            Email.requestFocus();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Email.setError("Please enter a valid Email");
            Email.requestFocus();
            return false;
        } else {
            return true;
        }


    }


    public void OpenNewAccountConectivity(){
        Intent intent = new Intent(NewUserAccountCreate1.this,NewUserAccountCreate2.class);
        if(ValidateDataInput()){

            String FstName = FirstName.getText().toString();
            String LstName = LastName.getText().toString();
            String Mobile = MobileNO.getText().toString();
            String email = Email.getText().toString().trim();
            String gender = GenderSelected.toString();



            Bundle NewUserDataBundale= new Bundle();
            NewUserDataBundale.putString("FirstName",FstName);
            NewUserDataBundale.putString("LastName",LstName);
            NewUserDataBundale.putString("MobileNo",Mobile);
            NewUserDataBundale.putString("Email",email);
            NewUserDataBundale.putString("Gender",gender);

            intent.putExtras(NewUserDataBundale);
            startActivity(intent);
            finish();

        };




    }
    private  void back(){
        Intent i = new Intent( NewUserAccountCreate1.this,LogInActivity.class);
        startActivity(i);
        finish();
    }
/*    private void EmailCheckDB(){
        db.collection("Users")
    }*/


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        GenderSelected=parent.getItemAtPosition(position).toString();

//        Toast.makeText(parent.getContext(),GenderSelected,Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}