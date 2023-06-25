package com.example.buzzhub.apiInterfaces;

import com.example.buzzhub.Adapter.HomepostAdapter;
import com.example.buzzhub.model.HomepostModel;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public interface PostInterface {
    @Multipart
    @POST("post")
    Call<ResponseBody> CreatePost(@Part MultipartBody.Part img, @Part("description") RequestBody description);

    @Headers({
            "Content-Type: application/json",
            "Accept: application/json"
    })
    @GET("all")
    Call<ArrayList<HomepostModel>> getFeed();

}
