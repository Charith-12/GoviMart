package com.example.govimart;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.squareup.picasso.Picasso;

public class Seller_and_TransporterMainActivityHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //variables for navigation_menu
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ChipNavigationBar bottomChipNav, topChipNav;
    RelativeLayout SearchBar;
    TextView userName,userRole;
    ImageView userProfilePic;


    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String U_ID;
    StorageReference storageReference;

    /*TODO: DIFFERENT BOTTOM BARS FOR DIFFERENT USERS , POPULATE THE FRAMELAYOUT WITH RELEVANT FRAGMENT
     *
     * TODO:Main menu ,bottom menu and top menu need to add fragment
     *
     * TODO:design search ,profile and many more
     *
     * //TODO: DIFFERENT BOTTOM BARS FOR DIFFERENT USERS , POPULATE THE FRAMELAYOUT WITH RELEVANT FRAGMENT
     *
     * */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_and__transporter_main_home);
        //Hooks for navigation_menu
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.tb_Main);
        topChipNav = findViewById(R.id.nav_ChipTopNavigation);
 /*       userName = findViewById(R.id.tv_UserNameNavHeader);
        userRole = findViewById(R.id.tv_UserNameNavRole);
        userProfilePic = findViewById(R.id.iv_UserNavProfileImage);*/
        SearchBar = findViewById(R.id.sch_SearchBar);


        // get data for header
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        U_ID =fAuth.getCurrentUser().getUid();

/*        DocumentReference documentReference = fStore.collection("Users").document(U_ID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                userName.setText(value.getString("UserName"));
                userRole.setText(value.getString("Role"));

            }
        });*/


        SearchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent( Seller_and_TransporterMainActivityHome.this,SeachActivity.class);
                startActivity(i);
                finish();
            }
        });



        bottomChipNav = findViewById(R.id.nav_ChipBottomNavigation);
        bottomChipNav.setItemSelected(R.id.nav_home,true);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new CategoriesTabFragment()).commit();
        ChipBottomNavigation();


        ChipTopNavigation();

        //tool bar
        setSupportActionBar(toolbar);

        //drawer menu for navigation_menu
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);
        updateNavHeader();


    }

    private void updateNavHeader() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        final TextView userName = headerView.findViewById(R.id.tv_UserNameNavHeader);
        final TextView userRole = headerView.findViewById(R.id.tv_UserNameNavRole);
        final ImageView userProfilePic = headerView.findViewById(R.id.iv_UserNavProfileImage);

        DocumentReference documentReference = fStore.collection("Users").document(U_ID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                userName.setText(value.getString("UserName"));
                userRole.setText(value.getString("Role"));

            }
        });
        StorageReference ProfileRef = storageReference.child("Users/"+U_ID+"/ProfileImage");
//        StorageReference ProfileRef = storageReference.child("Users/"+"ProfileImage/"+U_ID);
        ProfileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(userProfilePic);
            }
        });

/*        navUserMail.setText(U_ID.getEmail());
        navUsername.setText(currentUser.getDisplayName());*/


    }


    //<!-- Top navigation menu -->

    private void ChipTopNavigation() {
        topChipNav.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Intent intent;
                switch (i){
                    case R.id.nav_messages:
                        intent = new Intent( Seller_and_TransporterMainActivityHome.this,MessagesActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_notifications:
                        intent = new Intent( Seller_and_TransporterMainActivityHome.this,NotificationActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_profile:
                        intent = new Intent( Seller_and_TransporterMainActivityHome.this,ProfileActivity.class);
                        startActivity(intent);
                        break;
                }

            }
        });

    }
    //<!-- bottom navigation menu -->

    private void ChipBottomNavigation() {

        bottomChipNav.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i){
                    case R.id.nav_home:
                        fragment = new CategoriesTabFragment();
                        break;
                    case R.id.nav_feed:
                        fragment = new FeedTabFragment();
                        break;
                    case R.id.nav_add:
                        fragment = new AddTabFragment();
                        break;
                    case R.id.nav_transport:
                        // Original
                        //fragment = new TransportTabFragment
                    case R.id.nav_Cart:
                        // Original
                        //fragment = new TransportTabFragment();

                        // Added for cart testing purposes
                        fragment = new CartTabFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();

            }
        });


    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_myDetails:
                //add activity or fragment
                Intent profile_intent = new Intent(Seller_and_TransporterMainActivityHome.this,ProfileActivity.class);
                startActivity(profile_intent);
                break;
            case R.id.nav_logout:
                fAuth.signOut();

                Intent logout_intent = new Intent(Seller_and_TransporterMainActivityHome.this, LogInActivity.class);
                startActivity(logout_intent);

                finish();
                break;

        }

        return true;
    }
}