package com.example.buzzhub;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.buzzhub.Fragments.ProfileFragment;
import com.example.buzzhub.apiInterfaces.UserInterface;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class editprofileActivity extends AppCompatActivity {
    String filePath = "";

    TextView choosefile;
    ImageView preview,back;
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
//                progressDialog.show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        //Updating the profile by sending the data to the database from here on the click
//                        Intent intent = new Intent(editprofileActivity.this, homepageActivity.class);
//                        startActivity(intent);
//                        finish();

                        SharedPreferences preferences = getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
                        String retrievedToken  = preferences.getString("TOKEN","");
                        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
                        httpClient.addInterceptor(new Interceptor() {
                            @NonNull
                            @Override
                            public Response intercept(@NonNull Chain chain) throws IOException {
                                Request original = chain.request();
                                Request.Builder requestBuilder = original.newBuilder()
                                        .header("Authorization", "Bearer " + retrievedToken);
                                Request request = requestBuilder.build();
                                return chain.proceed(request);
                            }
                        });

                        String URL  = preferences.getString("URL","");

                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(URL+"/api/users/")
                                .addConverterFactory(GsonConverterFactory.create())
                                .client(httpClient.build())
                                .build();

                        UserInterface userInterface = retrofit.create(UserInterface.class);
                        File file = new File(filePath);
                        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"),file);

                        EditText username = findViewById(R.id.pr_username);
                        EditText bio = findViewById(R.id.pr_bio);

                        MultipartBody.Part body = MultipartBody.Part.createFormData("img",file.getName(),requestFile);
                        RequestBody userName = RequestBody.create(MediaType.parse("multipart/form-data"),username.getText().toString());
                        RequestBody Bio = RequestBody.create(MediaType.parse("multipart/form-data"),bio.getText().toString());


                        Call<ResponseBody> call = userInterface.updateUser(body,userName,Bio);
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                                if(!response.isSuccessful())
                                {
                                    Toast.makeText(editprofileActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                }

                                Toast.makeText(editprofileActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                                Intent success = new Intent(editprofileActivity.this, homepageActivity.class);
                                startActivity(success);
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                bio.setText(t.getMessage());
                            }
                        });
                    }
                },2500);

            }
        });
    }

    public void fileselect(){
        final Dialog filech = new Dialog(this);
        filech.requestWindowFeature(Window.FEATURE_NO_TITLE);
        filech.setContentView(R.layout.post_choose_file);

        TextView gallery = filech.findViewById(R.id.choose_gallery);


        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 // Permission not granted, request it
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, galreq);
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
        Bitmap img;

        if(resultCode== Activity.RESULT_OK){

            if (requestCode==galreq) {
                //for gallery

                Uri uri = data.getData();
                preview.setImageURI(uri);
                filePath = getImagePathFromUri(this,uri);
                img = BitmapFactory.decodeFile(filePath);
            }


        }
    }

    private String getImagePathFromUri(Context context, Uri uri) {
        String imagePath = null;
        if (uri.getScheme().equals("content")) {
            ContentResolver contentResolver = context.getContentResolver();
            Cursor cursor = contentResolver.query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                if (columnIndex != -1) {
                    imagePath = cursor.getString(columnIndex);
                }
                cursor.close();
            }
        } else if (uri.getScheme().equals("file")) {
            imagePath = uri.getPath();
        }
        return imagePath;
    }


}