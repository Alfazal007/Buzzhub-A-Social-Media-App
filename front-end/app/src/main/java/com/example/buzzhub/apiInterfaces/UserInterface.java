package com.example.buzzhub.apiInterfaces;

import com.example.buzzhub.model.Profile;
import com.example.buzzhub.model.UpdateEmailAndPassword;

import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface UserInterface {

    @GET("{username}")
    Call<Profile> getUser(@Path("username") String username);
    @GET("profile")
    Call<Profile> getProfile();

    @PUT("{id}/follow")
    Call<String> followUser(@Path("id") String id);

    @PUT("{id}/unfollow")
    Call<String> unFollowUser(@Path("id") String id);
    @DELETE("delete")
    Call<String> deleteAccount();

    @Headers({
            "Content-Type: application/json",
            "Accept: application/json"
    })
    @PUT("update")
    Call<String> updateEmailOrPassword(@Body UpdateEmailAndPassword emailChange);


    @Multipart
    @PUT("update-normal")
    Call<ResponseBody> updateUser(@Part MultipartBody.Part img,@Part("username")RequestBody username,@Part("bio")RequestBody bio);

}
