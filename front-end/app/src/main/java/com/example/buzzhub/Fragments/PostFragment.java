package com.example.buzzhub.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.Toast;

import com.example.buzzhub.R;
import com.example.buzzhub.apiInterfaces.PostInterface;
import com.example.buzzhub.editprofileActivity;
import com.example.buzzhub.homepageActivity;

import java.io.File;
import java.io.IOException;
import java.net.URI;

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

public class PostFragment extends Fragment {

    ImageView previewimg;
    Button addstory,addpost;
    TextView choosefile;
    EditText description;
    String filePath = "";
    Bitmap img;
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

          addpost.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  SharedPreferences preferences = getActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
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
                          .baseUrl(URL+"/api/posts/")
                          .addConverterFactory(GsonConverterFactory.create())
                          .client(httpClient.build())
                          .build();

                  PostInterface postInterface = retrofit.create(PostInterface.class);
                  File file = new File(filePath);
                  RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"),file);
                  MultipartBody.Part body = MultipartBody.Part.createFormData("img",file.getName(),requestFile);
                  RequestBody Description = RequestBody.create(MediaType.parse("multipart/form-data"),description.getText().toString());

                  Call<ResponseBody> call = postInterface.CreatePost(body,Description);
                  call.enqueue(new Callback<ResponseBody>() {
                      @Override
                      public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                          if(!response.isSuccessful())
                          {
                              Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                              return;
                          }
                          Intent intent = new Intent(getActivity(), homepageActivity.class);
                          startActivity(intent);

                      }

                      @Override
                      public void onFailure(Call<ResponseBody> call, Throwable t) {
                          description.setText(t.getMessage().toString());
                      }
                  });
              }
          });
        return view;
    }

    public void filechoose(){
        final Dialog chosee = new Dialog(this.requireContext());
        chosee.requestWindowFeature(Window.FEATURE_NO_TITLE);
        chosee.setContentView(R.layout.post_choose_file);

        TextView gallery = chosee.findViewById(R.id.choose_gallery);


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

            if (requestCode==galreq) {
                //for gallery
                Uri uri = data.getData();
                previewimg.setImageURI(uri);
                filePath = getImagePathFromUri(getActivity(),uri);
                img = BitmapFactory.decodeFile(filePath);
                Toast.makeText(getActivity(),filePath,Toast.LENGTH_SHORT).show();
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