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

public class emailchange_Activity extends AppCompatActivity {

    ImageView back;
    EditText newmail,password;
    Button cngmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emailchange);

        back = findViewById(R.id.backtoprofile);
        newmail = findViewById(R.id.new_emailchange);
        password = findViewById(R.id.pass_emailchange);
        cngmail = findViewById(R.id.changemail_button);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        cngmail.setOnClickListener(new View.OnClickListener() {
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

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://192.168.1.15:8800/api/users/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(httpClient.build())
                        .build();

                UserInterface userInterface = retrofit.create(UserInterface.class);
                UpdateEmailAndPassword emailChange = new UpdateEmailAndPassword(newmail.getText().toString(),password.getText().toString(),null);
                Call<String> call = userInterface.updateEmailOrPassword(emailChange);


                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        if(!response.isSuccessful())
                        {
                            Toast.makeText(emailchange_Activity.this, "Error", Toast.LENGTH_SHORT).show();
                            return;
                        }


                        Toast.makeText(emailchange_Activity.this, response.body(), Toast.LENGTH_SHORT).show();
                        SharedPreferences preferences = getSharedPreferences("MY_APP",Context.MODE_PRIVATE);
                        preferences.edit().putString("TOKEN","").apply();
                        Intent bl = new Intent(emailchange_Activity.this, landingActivity.class);
                        startActivity(bl);
                        finish();

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(emailchange_Activity.this, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}