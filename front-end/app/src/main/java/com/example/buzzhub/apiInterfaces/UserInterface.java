package com.example.buzzhub.apiInterfaces;

import com.example.buzzhub.model.UpdateEmailAndPassword;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface UserInterface {
    @DELETE("delete")
    Call<String> deleteAccount();

    @Headers({
            "Content-Type: application/json",
            "Accept: application/json"
    })
    @PUT("update")
    Call<String> updateEmailOrPassword(@Body UpdateEmailAndPassword emailChange);
}
