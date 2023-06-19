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
import android.widget.Toast;

import com.example.buzzhub.apiInterfaces.AuthInterface;
import com.example.buzzhub.model.RegisterUser;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
        progressDialog.setMessage("Your account is created");

        sign_up.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                progressDialog.show();

                RegisterUser user = new RegisterUser(name.getText().toString(), email.getText().toString(), password.getText().toString());

                //Login Function Should be Written
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://192.168.1.15:8800/api/auth/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                AuthInterface authInterface = retrofit.create(AuthInterface.class);

                Call<RegisterUser> call = authInterface.createUser(user);
                call.enqueue(new Callback<RegisterUser>() {
                    @Override
                    public void onResponse(Call<RegisterUser> call, Response<RegisterUser> response) {
                        progressDialog.dismiss();
                        if(!response.isSuccessful())
                        {
                            Toast.makeText(signupActivity.this, "Account already exists, try to log in!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Intent success = new Intent(signupActivity.this, loginpageActivity.class);
                        startActivity(success);
                    }

                    @Override
                    public void onFailure(Call<RegisterUser> call, Throwable t) {
                        Toast.makeText(signupActivity.this, "Error " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}