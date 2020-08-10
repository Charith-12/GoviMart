package com.example.govimart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ProgressBar;

public class WelcomeActivity extends AppCompatActivity {

    CountDownTimer timer;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        progressBar = (ProgressBar)findViewById(R.id.pb_Welcome);

        timer = new CountDownTimer(1500,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int TimeLeft = (int)millisUntilFinished/1000;
                progressBar.setVisibility(View.VISIBLE);

            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(WelcomeActivity.this,LogInActivity.class);
                progressBar.setVisibility(View.GONE);
                startActivity(intent);

            }
        };

    }
    @Override
    protected void onResume(){
        super.onResume();
        timer.start();

    }
}