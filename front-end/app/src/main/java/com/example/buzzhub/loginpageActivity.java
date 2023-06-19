package com.example.buzzhub;

import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buzzhub.apiInterfaces.AuthInterface;
import com.example.buzzhub.model.RegisterUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
                String userEmail = email.getText().toString();
                String userPass = password.getText().toString();

                //Function Code Here
                RegisterUser user = new RegisterUser("",userEmail,userPass);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://192.168.1.15:8800/api/auth/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                AuthInterface authInterface = retrofit.create(AuthInterface.class);
                Call<String> call = authInterface.loginUser(user);

                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        progressDialog.dismiss();
                        if(!response.isSuccessful())
                        {
                            Toast.makeText(loginpageActivity.this, "Email or Password is Incorrect ", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        SharedPreferences preferences = getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
                        preferences.edit().putString("TOKEN",response.body()).apply();

                        Intent success = new Intent(loginpageActivity.this, homepageActivity.class);
                        startActivity(success);
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(loginpageActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}