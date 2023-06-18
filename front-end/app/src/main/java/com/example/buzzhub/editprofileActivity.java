package com.example.buzzhub;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class editprofileActivity extends AppCompatActivity {

    TextView choosefile;
    ImageView preview,back;
    EditText name,bio;
    private final int cmreq = 67;
    private final int galreq = 79;
    ProgressDialog progressDialog;
    Button update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        choosefile = findViewById(R.id.pr_choose_file);
        update = findViewById(R.id.pr_update);
        preview = findViewById(R.id.profile_pic);
        back = findViewById(R.id.bk_to_pr);
        progressDialog = new ProgressDialog(editprofileActivity.this);
        progressDialog.setTitle("Profile Updated");
        progressDialog.setMessage("wait a moment we are updating your profile");

        choosefile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileselect();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        //Updating the profile by sending the data to the database from here on the click
                        Intent intent = new Intent(editprofileActivity.this, homepageActivity.class);
                        startActivity(intent);
                        finish();
                    }
                },2500);

            }
        });
    }

    public void fileselect(){
        final Dialog filech = new Dialog(this);
        filech.requestWindowFeature(Window.FEATURE_NO_TITLE);
        filech.setContentView(R.layout.post_choose_file);

        TextView camera = filech.findViewById(R.id.choose_camera);
        TextView gallery = filech.findViewById(R.id.choose_gallery);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent icam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(icam,cmreq);
                filech.dismiss();
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gal = new Intent(Intent.ACTION_PICK);
                gal.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gal,galreq);
                filech.dismiss();
            }
        });

        filech.show();
        filech.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        filech.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        filech.getWindow().getAttributes().windowAnimations =R.style.DialogAnimation;
        filech.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode== Activity.RESULT_OK){
            if(requestCode==cmreq){
                //for camera
                Bitmap img =(Bitmap)(data.getExtras().get("data"));
                preview.setImageBitmap(img);
            } else
            if (requestCode==galreq) {
                //for gallery
                preview.setImageURI(data.getData());

            }
        }
    }
}