package com.example.buzzhub.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.buzzhub.Adapter.HomepostAdapter;
import com.example.buzzhub.Adapter.StoryAdapter;
import com.example.buzzhub.R;
import com.example.buzzhub.apiInterfaces.PostInterface;
import com.example.buzzhub.model.HomepostModel;
import com.example.buzzhub.model.StoryModel;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    RecyclerView storyview,homepost;
    ArrayList<StoryModel> list;
    ArrayList<HomepostModel> homepostlist;
    ProgressDialog progressDialog;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        storyview = view.findViewById(R.id.story_view);
        list = new ArrayList<>(); // Pass the data profile image through list it will to Story model and story model will send it adapter and it will set the data and send back
        list.add(new StoryModel(R.drawable.ayanokoji));
        list.add(new StoryModel(R.drawable.closeup));
        list.add(new StoryModel(R.drawable.download));
        list.add(new StoryModel(R.drawable.md));
        list.add(new StoryModel(R.drawable.one));
        list.add(new StoryModel(R.drawable.rdj));
        list.add(new StoryModel(R.drawable.rohith));

        StoryAdapter adapter = new StoryAdapter(list,getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        storyview.setLayoutManager(linearLayoutManager);
        storyview.setNestedScrollingEnabled(false);
        storyview.setAdapter(adapter);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Fetching feed");
        progressDialog.show();
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
                .baseUrl(URL+"/api/posts/feeds/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        PostInterface postInterface = retrofit.create(PostInterface.class);
        Call<ArrayList<HomepostModel>> call =  postInterface.getFeed();

        call.enqueue(new Callback<ArrayList<HomepostModel>>() {

            @Override
            public void onResponse(Call<ArrayList<HomepostModel>> call, retrofit2.Response<ArrayList<HomepostModel>> response) {
                progressDialog.dismiss();
                if(!response.isSuccessful())
                {
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                homepost = view.findViewById(R.id.postinhome);
                homepostlist = new ArrayList<>();

                ArrayList<HomepostModel> posts = response.body();

                HomepostAdapter homepostAdapter = new HomepostAdapter(posts,getContext());
                LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext());
                homepost.setLayoutManager(linearLayoutManager1);
                homepost.setNestedScrollingEnabled(true);
                homepost.setAdapter(homepostAdapter);

            }

            @Override
            public void onFailure(Call<ArrayList<HomepostModel>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        //from here post


//        // add the post inb the code only should write the code here to fetch
//        homepostlist.add(new HomepostModel(R.drawable.homeclose,R.drawable.zoro,"Zoro","Explorer","1500","1200","200","Straw-Hat pirates Vice-Captain"));
//        homepostlist.add(new HomepostModel(R.drawable.homeclose,R.drawable.chopper,"Tony-Chopper","Doctor","115","112","20","Straw-Hat pirates Ship Doctor"));
//        homepostlist.add(new HomepostModel(R.drawable.homeclose,R.drawable.brook,"Brook","Soul-King","150","120","2","Straw-Hat pirates Musician"));

        return view;
    }
}