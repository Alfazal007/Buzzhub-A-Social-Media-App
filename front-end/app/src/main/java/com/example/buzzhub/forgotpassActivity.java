package com.example.buzzhub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class forgotpassActivity extends AppCompatActivity {

    ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpass);
        back = findViewById(R.id.go_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bk = new Intent(forgotpassActivity.this, loginpageActivity.class);
                startActivity(bk);
                finish();
            }
        });
    }
}