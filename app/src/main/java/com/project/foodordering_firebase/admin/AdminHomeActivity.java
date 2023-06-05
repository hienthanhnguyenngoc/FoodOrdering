package com.project.foodordering_firebase.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;

import com.project.foodordering_firebase.MainActivity;
import com.project.foodordering_firebase.R;

public class AdminHomeActivity extends AppCompatActivity {
    TextView store, user, booking;
    Button dangXuat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        store = findViewById(R.id.store);
        user = findViewById(R.id.user);
        dangXuat = findViewById(R.id.btnLogOut);

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ListUserActivity.class);
                startActivity(i);
            }
        });

        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ListStoreActivity.class);
                startActivity(i);
            }
        });

        dangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });

    }
}