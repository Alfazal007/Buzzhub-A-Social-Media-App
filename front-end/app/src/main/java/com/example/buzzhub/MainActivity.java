package com.example.buzzhub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences preferences = getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
                String retrievedToken  = preferences.getString("TOKEN","");//second parameter default value.

                Intent ilanding;

                if(retrievedToken.equals(""))
                {
                    ilanding = new Intent(MainActivity.this, landingActivity.class);
                }
                else{
                    ilanding = new Intent(MainActivity.this, homepageActivity.class);
                }
                startActivity(ilanding);
                finish();
            }
        }, 5000);
    }
}