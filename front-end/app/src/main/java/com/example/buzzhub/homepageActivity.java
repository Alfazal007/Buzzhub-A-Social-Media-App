package com.example.buzzhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class homepageActivity extends AppCompatActivity {

    Fragment fragment = null;
    BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        navigationView = findViewById(R.id.bottom_navi);
        //navigationView.setItemIconTintList(null);
        getSupportFragmentManager().beginTransaction().replace(R.id.body_container,new HomeFragment()).commit();
        navigationView.setSelectedItemId(R.id.home);

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(item.getItemId() == R.id.home){
                    fragment =new HomeFragment();
                }
                else if(item.getItemId() == R.id.search) {
                    fragment =new SearchFragment();

                }
                else if(item.getItemId() == R.id.post) {
                    fragment =new PostFragment();

                }
                else if(item.getItemId() == R.id.chat) {
                    fragment =new ChatFragment();

                }
                else if(item.getItemId() == R.id.profile) {
                    fragment =new ProfileFragment();

                }
                getSupportFragmentManager().beginTransaction().replace(R.id.body_container,fragment).commit();
                return true;
            }
        });
    }
}