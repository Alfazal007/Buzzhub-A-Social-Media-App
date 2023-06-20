package com.example.buzzhub.apiInterfaces;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface UserInterface {
    @DELETE("delete")
    Call<String> deleteAccount();
}
