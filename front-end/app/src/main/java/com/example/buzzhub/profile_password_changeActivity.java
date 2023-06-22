package com.example.buzzhub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.buzzhub.apiInterfaces.UserInterface;
import com.example.buzzhub.model.UpdateEmailAndPassword;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class profile_password_changeActivity extends AppCompatActivity {

    ImageView back;
    EditText old,newpas;
    Button changepass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_password_change);

        back = findViewById(R.id.backtoprofile_pass);
        old = findViewById(R.id.old_passwordchange);
        newpas= findViewById(R.id.new_passwordchange);
        changepass=findViewById(R.id.changepass_button);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SharedPreferences preferences = getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
                String retrievedToken  = preferences.getString("TOKEN","");
                OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
                httpClient.addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        Request.Builder requestBuilder = original.newBuilder()
                                .header("Authorization", "Bearer " + retrievedToken);
                        Request request = requestBuilder.build();
                        return chain.proceed(request);
                    }
                });

                String URL  = preferences.getString("URL","");

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(URL+"/api/users/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(httpClient.build())
                        .build();

                UserInterface userInterface = retrofit.create(UserInterface.class);
                UpdateEmailAndPassword changePass = new UpdateEmailAndPassword(null,old.getText().toString(),newpas.getText().toString());

                Call<String> call = userInterface.updateEmailOrPassword(changePass);

                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, retrofit2.Response<String> response) {

                        if(!response.isSuccessful())
                        {
                            Toast.makeText(profile_password_changeActivity.this, "Error, try entering password again" ,  Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Toast.makeText(profile_password_changeActivity.this, response.body(), Toast.LENGTH_SHORT).show();
                        SharedPreferences preferences = getSharedPreferences("MY_APP",Context.MODE_PRIVATE);
                        preferences.edit().putString("TOKEN","").apply();
                        Intent bl = new Intent(profile_password_changeActivity.this, landingActivity.class);
                        startActivity(bl);
                        finish();

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(profile_password_changeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}