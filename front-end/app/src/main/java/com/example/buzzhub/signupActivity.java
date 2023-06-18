package com.example.buzzhub;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class signupActivity extends AppCompatActivity {

    Button sign_up;
    ImageButton back;
    EditText name,email,password;
    TextView signinpage;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        sign_up = findViewById(R.id.register_button);
        back = findViewById(R.id.back_arrow);
        name = findViewById(R.id.signup_username);
        email = findViewById(R.id.signup_email);
        password = findViewById(R.id.signup_password);
        signinpage =findViewById(R.id.go_to_signin);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bkpage = new Intent(signupActivity.this, landingActivity.class);
                startActivity(bkpage);
                finish();
            }
        });

        signinpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signpg = new Intent(signupActivity.this, loginpageActivity.class);
                startActivity(signpg);
                finish();
            }
        });

        progressDialog = new ProgressDialog(signupActivity.this);
        progressDialog.setTitle("Creating your Account");
        progressDialog.setMessage("Your account is creating");

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String username = name.getText().toString();
                String useremail = email.getText().toString();
                String userpass = password.getText().toString();

                //Login Function Should be Written

            }
        });
    }
}