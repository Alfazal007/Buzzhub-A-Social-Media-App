package com.example.buzzhub.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.buzzhub.Adapter.HomepostAdapter;
import com.example.buzzhub.Adapter.StoryAdapter;
import com.example.buzzhub.R;
import com.example.buzzhub.model.HomepostModel;
import com.example.buzzhub.model.StoryModel;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    RecyclerView storyview,homepost;
    ArrayList<StoryModel> list;
    ArrayList<HomepostModel> homepostlist;
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
        list.add(new StoryModel(R.drawable.addclose));
        list.add(new StoryModel(R.drawable.chatclose));
        list.add(new StoryModel(R.drawable.homeclose));
        list.add(new StoryModel(R.drawable.searchclose));
        list.add(new StoryModel(R.drawable.addclose));
        list.add(new StoryModel(R.drawable.addclose));

        StoryAdapter adapter = new StoryAdapter(list,getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        storyview.setLayoutManager(linearLayoutManager);
        storyview.setNestedScrollingEnabled(false);
        storyview.setAdapter(adapter);

        //from here post

        homepost = view.findViewById(R.id.postinhome);
        homepostlist = new ArrayList<>();
        // add the post inb the code only should write the code here to fetch
        homepostlist.add(new HomepostModel(R.drawable.homeopen,R.drawable.nico_robin,"Nico Robin","Archeologist","150","120","25","Straw-Hat pirates Archeologist"));
        homepostlist.add(new HomepostModel(R.drawable.homeclose,R.drawable.zoro,"Zoro","Explorer","1500","1200","200","Straw-Hat pirates Vice-Captain"));
        homepostlist.add(new HomepostModel(R.drawable.homeclose,R.drawable.chopper,"Tony-Chopper","Doctor","115","112","20","Straw-Hat pirates Ship Doctor"));
        homepostlist.add(new HomepostModel(R.drawable.homeclose,R.drawable.brook,"Brook","Soul-King","150","120","2","Straw-Hat pirates Musician"));
        HomepostAdapter homepostAdapter = new HomepostAdapter(homepostlist,getContext());
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext());
        homepost.setLayoutManager(linearLayoutManager1);
        homepost.setNestedScrollingEnabled(true);
        homepost.setAdapter(homepostAdapter);
        return view;
    }
}