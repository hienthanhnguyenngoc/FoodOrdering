package com.project.foodordering_firebase.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;
import com.project.foodordering_firebase.R;
import com.project.foodordering_firebase.adapter.UserAdapter;
import com.project.foodordering_firebase.model.User;

public class ListUserActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    UserAdapter userAdapter;
    FloatingActionButton floatingActionButton;

    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_user);

        recyclerView = findViewById(R.id.rvListUser);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<User> options =
                new FirebaseRecyclerOptions.Builder<User>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("User"),User.class)
                        .build();

        userAdapter = new UserAdapter(options);
        recyclerView.setAdapter(userAdapter);

        floatingActionButton = findViewById(R.id.fabUser);
        floatingActionButton.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), AddUserActivity.class);
            startActivity(i);

        });

        back = findViewById(R.id.backFromCus);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AdminHomeActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (userAdapter != null) {
            userAdapter.startListening();
        }
    }
}