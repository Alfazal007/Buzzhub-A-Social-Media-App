package com.example.buzzhub;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buzzhub.apiInterfaces.UserInterface;
import com.example.buzzhub.model.Profile;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class searchuser_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchuser);
        
        getProfile();
    }

    private void getProfile() {
        String username = getIntent().getStringExtra("username");
        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("User Profile");
        progressDialog.setMessage("Getting User Info");
        progressDialog.show();
        TextView viewUsername = findViewById(R.id.username_inside_layout);
        TextView bio = findViewById(R.id.user_bio);
        ImageView img = findViewById(R.id.profile_pic);
        TextView followerCount = findViewById(R.id.follower_count);
        TextView followingCount = findViewById(R.id.following_count);
        TextView PostCount = findViewById(R.id.posts_count);

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
                .baseUrl(URL+"/api/users/username/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        UserInterface userInterface = retrofit.create(UserInterface.class);
        Call<Profile> call = userInterface.getUser(username);

        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, retrofit2.Response<Profile> response) {
                progressDialog.dismiss();
                if(!response.isSuccessful())
                {
                    Toast.makeText(searchuser_Activity.this, "Error", Toast.LENGTH_SHORT).show();
                    return;
                }

                Profile user = response.body();
                Bitmap bitmap = BitmapFactory.decodeByteArray(user.img.data,0,user.img.data.length);
                img.setImageBitmap(bitmap);
                viewUsername.setText(user.username);
                bio.setText(user.bio);
                followerCount.setText(String.valueOf(user.followers));
                followingCount.setText(String.valueOf(user.following));
                PostCount.setText(String.valueOf(user.posts));

            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(searchuser_Activity.this, "Err", Toast.LENGTH_SHORT).show();
            }
        });

    }
}