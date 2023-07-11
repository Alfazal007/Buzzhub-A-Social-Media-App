package com.example.buzzhub.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buzzhub.R;
import com.example.buzzhub.apiInterfaces.UserInterface;
import com.example.buzzhub.model.Profile;
import com.example.buzzhub.model.User;
import com.example.buzzhub.searchuser_Activity;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SearchFragment extends Fragment {


    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        ImageView searchBtn = view.findViewById(R.id.search_profilebtn);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText username;
                ProgressDialog progressDialog;
                username = view.findViewById(R.id.searchprofile);
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setTitle("User Profile");
                progressDialog.setMessage("Getting User Info");
                progressDialog.show();

                // Searching username if exists

                SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
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
                Call<User> call = userInterface.getUser(username.getText().toString());

                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, retrofit2.Response<User> response) {
                        progressDialog.dismiss();

                        if(!response.isSuccessful())
                        {
                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Toast.makeText(getContext(), "Found", Toast.LENGTH_SHORT).show();
                        User user = response.body();
                        ImageView img = view.findViewById(R.id.addr_user);
                        TextView responseUsername = view.findViewById(R.id.add_username);

                        Bitmap bitmap = BitmapFactory.decodeByteArray(user.img.data,0,user.img.data.length);
                        img.setImageBitmap(bitmap);
                        responseUsername.setText(user.username);

                        responseUsername.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getContext(), searchuser_Activity.class);
                                intent.putExtra("userObject",user.username);
                                startActivity(intent);
                            }
                        });



                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Err", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        // Inflate the layout for this fragment
        return view;


    }




}