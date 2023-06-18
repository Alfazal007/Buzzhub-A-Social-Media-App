package com.example.buzzhub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class profile_password_changeActivity extends AppCompatActivity {

    ImageView back;
    EditText old,newpas;
    Button changepass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_password_change);

        back = findViewById(R.id.backtoprofile_pass);
        old = findViewById(R.id.old_passwordchange);
        newpas= findViewById(R.id.new_passwordchange);
        changepass=findViewById(R.id.changepass_button);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bl = new Intent(profile_password_changeActivity.this, landingActivity.class);
                startActivity(bl);
                finish();
            }
        });
    }
}