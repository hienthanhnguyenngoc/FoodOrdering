package com.project.foodordering_firebase.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.project.foodordering_firebase.R;
import com.project.foodordering_firebase.adapter.CartAdapter;
import com.project.foodordering_firebase.adapter.FavStoreAdapter;
import com.project.foodordering_firebase.model.Cart;
import com.project.foodordering_firebase.model.FavStore;

public class FavStoreActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FavStoreAdapter favStoreAdapter;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_store);

        //ánh xạ
        back = findViewById(R.id.back);

        //lấy username từ info activity
        Intent i = getIntent();
        String username = i.getStringExtra("userName");

        recyclerView = findViewById(R.id.rvFavoriteStore);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<FavStore> options =
                new FirebaseRecyclerOptions.Builder<FavStore>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("FavStore").child(username)
                                .orderByChild("userName").equalTo(username), FavStore.class)
                        .build();

        favStoreAdapter = new FavStoreAdapter(options);
        recyclerView.setAdapter(favStoreAdapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (favStoreAdapter != null) {
            favStoreAdapter.startListening();
        }
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (favStoreAdapter != null) {
//            favStoreAdapter.stopListening();
//        }
//    }
}