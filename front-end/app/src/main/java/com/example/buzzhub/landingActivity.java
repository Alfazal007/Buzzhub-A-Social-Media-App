package com.example.buzzhub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class landingActivity extends AppCompatActivity {

    Button signup,login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        signup = findViewById(R.id.signup_btn);
        login = findViewById(R.id.login_btn);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reg = new Intent(landingActivity.this, signupActivity.class);
                startActivity(reg);
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent log = new Intent(landingActivity.this, loginpageActivity.class);
                startActivity(log);
                finish();
            }
        });
    }
}