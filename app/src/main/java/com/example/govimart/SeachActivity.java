package com.example.govimart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class SeachActivity extends AppCompatActivity {

    private EditText SearchField;
    private ImageButton SearchIcon;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seach);


        SearchField = findViewById(R.id.et_Search);
        SearchIcon = findViewById(R.id.imageButton5);
        recyclerView = findViewById(R.id.recyclerView_search);

        SearchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseSearch();
            }
        });

    }

    private void firebaseSearch() {

        /*Firebase<users,usersViewHolder> firebaseRecycleAdapter*/

    }

    public class usersViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public usersViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

        }
    }


}