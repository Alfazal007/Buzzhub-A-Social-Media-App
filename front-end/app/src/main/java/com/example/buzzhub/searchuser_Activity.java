package com.example.buzzhub;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

    ImageView img ;
    TextView viewUsername,userNameAtTop,bio,followerCount,followingCount,PostCount ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchuser);
        Button followBtn = findViewById(R.id.follow_button);
        viewUsername = findViewById(R.id.username_inside_layout);
        userNameAtTop = findViewById(R.id.profile_username1);
        bio = findViewById(R.id.user_bio);
        img = findViewById(R.id.profile_pic);
        followerCount = findViewById(R.id.follower_count);
        followingCount = findViewById(R.id.following_count);
        PostCount = findViewById(R.id.posts_count);
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

        getProfile(followBtn,retrofit,retrievedToken);

        Retrofit retrofit1 = new Retrofit.Builder()
                .baseUrl(URL+"/api/users/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();


        followBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String id = getIntent().getStringExtra("id");

                String followOrUnFollow = followBtn.getText().toString();

                if(followOrUnFollow.equals("Follow"))
                {
                    UserInterface userInterface = retrofit1.create(UserInterface.class);
                    Call<String> call = userInterface.followUser(id);

                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                            if(!response.isSuccessful())
                            {
                                Toast.makeText(searchuser_Activity.this, "Error", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            Toast.makeText(searchuser_Activity.this, "Followed User!", Toast.LENGTH_SHORT).show();
                            followBtn.setText("Unfollow");
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(searchuser_Activity.this, "Internal Server Error", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                else{
                    UserInterface userInterface = retrofit1.create(UserInterface.class);
                    Call<String> call = userInterface.unFollowUser(id);

                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                            if(!response.isSuccessful())
                            {
                                Toast.makeText(searchuser_Activity.this, "Error", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            Toast.makeText(searchuser_Activity.this, "Unfollowed User!", Toast.LENGTH_SHORT).show();
                            followBtn.setText("Follow");
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(searchuser_Activity.this, "Internal Server Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }



    private void getProfile(Button followBtn, Retrofit retrofit , String retrievedToken ) {
        String username = getIntent().getStringExtra("username");
        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("User Profile");
        progressDialog.setMessage("Getting User Info");
        progressDialog.show();





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
                userNameAtTop.setText(user.username);
                bio.setText(user.bio);
                followerCount.setText(String.valueOf(user.followers));
                followingCount.setText(String.valueOf(user.following));
                PostCount.setText(String.valueOf(user.posts));
                if(user.followingUser)
                {
                    followBtn.setText("Unfollow");
                }
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(searchuser_Activity.this, "Err", Toast.LENGTH_SHORT).show();
            }
        });

    }
}