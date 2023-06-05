package com.project.foodordering_firebase.admin;

import androidx.appcompat.app.AppCompatActivity;
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
import com.project.foodordering_firebase.adapter.FoodAdapter;
import com.project.foodordering_firebase.adapter.StoreAdapter;
import com.project.foodordering_firebase.model.Food;
import com.project.foodordering_firebase.model.Store;

public class MenuActivity extends AppCompatActivity {

    ImageView back;
    FloatingActionButton fab;
    RecyclerView recyclerView;

    FoodAdapter foodAdapter;
    String sName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        anhXa();

        RecyclerView recyclerView = findViewById(R.id.rvMenu);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Food> options =
                new FirebaseRecyclerOptions.Builder<Food>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Food").orderByChild("storeName").equalTo(getStoreName()),Food.class)
                        .build();

        foodAdapter = new FoodAdapter(options);
        recyclerView.setAdapter(foodAdapter);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddFoodActivity.class);
                intent.putExtra("sName", getStoreName());
                intent.putExtra("sCate", getStoreCate());
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public String getStoreName() {
        Bundle extras = getIntent().getExtras();
        String sName = " ";
        if (extras != null) {
            sName = extras.getString("sName");
        }
        return sName;
    }

    public String getStoreCate(){
        Bundle extras = getIntent().getExtras();
        String sCate = " ";
        if (extras != null) {
            sCate = extras.getString("sCate");
        }
        return sCate;
    }

    public void anhXa(){
        back = findViewById(R.id.back);
        fab = findViewById(R.id.fabMenu);
        recyclerView = findViewById(R.id.rvMenu);
    }

    @Override
    protected void onStart() {
        super.onStart();
        foodAdapter.startListening();
//        if (foodAdapter != null) {
//            foodAdapter.startListening();
//        }
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        foodAdapter.stopListening();
////        if (foodAdapter != null) {
////            foodAdapter.stopListening();
////        }
//    }
}