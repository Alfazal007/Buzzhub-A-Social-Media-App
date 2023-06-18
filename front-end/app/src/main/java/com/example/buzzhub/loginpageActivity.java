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

public class loginpageActivity extends AppCompatActivity {

    ImageButton bktolanding;
    EditText email,password;
    TextView forgotpd,gtsignin;
    Button signin;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);

        bktolanding = findViewById(R.id.back_arrow1);
        email = findViewById(R.id.signin_email);
        password = findViewById(R.id.signin_password);
        forgotpd = findViewById(R.id.forgot_password);
        gtsignin = findViewById(R.id.go_to_signup);
        signin = findViewById(R.id.login_button);

        bktolanding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bkl = new Intent(loginpageActivity.this, landingActivity.class);
                startActivity(bkl);
                finish();
            }
        });

        gtsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent gtsi = new Intent(loginpageActivity.this, signupActivity.class);
                startActivity(gtsi);
                finish();
            }
        });

        progressDialog = new ProgressDialog(loginpageActivity.this);
        progressDialog.setTitle("Login");
        progressDialog.setMessage("Logging In into your account");

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String useremail = email.getText().toString();
                String userpass = password.getText().toString();

                //Function Code Here

            }
        });
    }
}