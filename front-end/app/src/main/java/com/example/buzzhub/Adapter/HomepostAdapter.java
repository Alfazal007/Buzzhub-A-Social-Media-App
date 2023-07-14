package com.example.buzzhub.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buzzhub.R;
import com.example.buzzhub.model.HomepostModel;
import com.example.buzzhub.R;
import com.example.buzzhub.model.HomepostModel;

import java.util.ArrayList;

public class HomepostAdapter extends RecyclerView.Adapter<HomepostAdapter.viewHolder> {

    ArrayList<HomepostModel> list;
    Context context;


    public HomepostAdapter(ArrayList<HomepostModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_post_design,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        HomepostModel model = list.get(position);

        Bitmap profile = BitmapFactory.decodeByteArray(model.getProfileimg().data, 0, model.getProfileimg().data.length);
        Bitmap post = BitmapFactory.decodeByteArray(model.getPostimg().data, 0, model.getPostimg().data.length);

        holder.proimg.setImageBitmap(profile);
        holder.postimg.setImageBitmap(post);
        holder.username.setText(model.getUsername());
//        holder.about.setText(model.getAbout());
//        holder.like.setText(model.getLike());
//        holder.share.setText(model.getShare());
//        holder.comment.setText(model.getComment());
        holder.caption.setText( model.getCaption());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        ImageView proimg,postimg;
        TextView username,about,like,comment,share,caption;
        public viewHolder(@NonNull View itemView) {
            super(itemView);


            proimg =itemView.findViewById(R.id.post_profile);
            postimg =itemView.findViewById(R.id.home_post_image);
            username =itemView.findViewById(R.id.post_username);
//            about =itemView.findViewById(R.id.about_in_home);
//            like =itemView.findViewById(R.id.like_post);
//            comment =itemView.findViewById(R.id.comment_post);
//            share =itemView.findViewById(R.id.share_post);
            caption =itemView.findViewById(R.id.caption_in_post);
        }
    }
}
