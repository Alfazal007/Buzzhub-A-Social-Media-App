package com.example.buzzhub.apiInterfaces;

import com.example.buzzhub.model.RegisterUser;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface AuthInterface {
    @Headers({
            "Content-Type: application/json",
            "Accept: application/json"
    })
    @POST("register")
    Call<RegisterUser> createUser(@Body RegisterUser user);

    @Headers({
            "Content-Type: application/json",
            "Accept: application/json"
    })
    @POST("login")
    Call<String> loginUser(@Body RegisterUser user);


}
