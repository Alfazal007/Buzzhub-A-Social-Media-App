package com.example.buzzhub.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.buzzhub.R;

public class PostFragment extends Fragment {

    ImageView previewimg;
    Button addstory,addpost;
    TextView choosefile;
    EditText description;
    private final int cmreq = 69;
    private final int galreq = 77;
    public PostFragment() {
        // Required empty public constructor
    }

      @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post, container, false);
          previewimg = view.findViewById(R.id.post_img);
          addpost = view.findViewById(R.id.add_post_btn);
          addstory = view.findViewById(R.id.post_addstory_btn);
          description = view.findViewById(R.id.post_caption);
          choosefile = view.findViewById(R.id.dialog_file);

          choosefile.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  filechoose();
              }
          });
        return view;
    }

    public void filechoose(){
        final Dialog chosee = new Dialog(this.requireContext());
        chosee.requestWindowFeature(Window.FEATURE_NO_TITLE);
        chosee.setContentView(R.layout.post_choose_file);

        TextView camera = chosee.findViewById(R.id.choose_camera);
        TextView gallery = chosee.findViewById(R.id.choose_gallery);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent icam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(icam,cmreq);
                chosee.dismiss();
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gal = new Intent(Intent.ACTION_PICK);
                gal.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gal,galreq);
                chosee.dismiss();
            }
        });

        chosee.show();
        chosee.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        chosee.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        chosee.getWindow().getAttributes().windowAnimations =R.style.DialogAnimation;
        chosee.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode== Activity.RESULT_OK){
            if(requestCode==cmreq){
                //for camera
                Bitmap img =(Bitmap)(data.getExtras().get("data"));
                previewimg.setImageBitmap(img);
            } else
            if (requestCode==galreq) {
                //for gallery
                previewimg.setImageURI(data.getData());

            }
        }
    }
}