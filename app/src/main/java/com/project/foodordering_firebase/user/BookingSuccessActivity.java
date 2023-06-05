package com.project.foodordering_firebase.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.project.foodordering_firebase.R;

public class BookingSuccessActivity extends AppCompatActivity {

    Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_success);
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isLoggedIn = getIntent().getBooleanExtra("login_status", false);
                String userName = getIntent().getStringExtra("userName");

                Intent intent = new Intent(BookingSuccessActivity.this, HomeActivity.class);
                intent.putExtra("login_status", isLoggedIn);
                intent.putExtra("userName", userName);
                startActivity(intent);
                finish();
            }
        });

    }
}