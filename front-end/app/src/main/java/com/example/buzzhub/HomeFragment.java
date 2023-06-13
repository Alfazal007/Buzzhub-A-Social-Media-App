package com.example.buzzhub;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.buzzhub.Adapter.StoryAdapter;
import com.example.buzzhub.model.StoryModel;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    RecyclerView storyview;
    ArrayList<StoryModel> list;
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
        return view;
    }
}