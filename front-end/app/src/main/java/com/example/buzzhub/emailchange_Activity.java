package com.example.buzzhub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class emailchange_Activity extends AppCompatActivity {

    ImageView back;
    EditText newmail,password;
    Button cngmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emailchange);

        back = findViewById(R.id.backtoprofile);
        newmail = findViewById(R.id.new_emailchange);
        password = findViewById(R.id.pass_emailchange);
        cngmail = findViewById(R.id.changemail_button);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        cngmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bl = new Intent(emailchange_Activity.this, landingActivity.class);
                startActivity(bl);
                finish();

                
            }
        });
    }
}