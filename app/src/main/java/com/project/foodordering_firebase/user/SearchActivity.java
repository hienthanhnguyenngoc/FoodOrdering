package com.project.foodordering_firebase.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.RecoverySystem;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.foodordering_firebase.R;
import com.project.foodordering_firebase.adapter.FoodAdapter;
import com.project.foodordering_firebase.adapter.FoodAdapter_User;
import com.project.foodordering_firebase.adapter.SearchStoreAdapter;
import com.project.foodordering_firebase.adapter.StoreAdapter_User;
import com.project.foodordering_firebase.model.Food;
import com.project.foodordering_firebase.model.Store;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    SearchStoreAdapter adapter;
    List<Store> storeList;
    SearchView searchView;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        anhXa();
        Intent intent = getIntent();
        String username = intent.getStringExtra("userName");

        reference = FirebaseDatabase.getInstance().getReference("Store");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        storeList = new ArrayList<>();
        adapter = new SearchStoreAdapter(this, storeList,username);
        recyclerView.setAdapter(adapter);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Store s = dataSnapshot.getValue(Store.class);
                    storeList.add(s);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });
    }

    public void searchList(String text) {
        ArrayList<Store> searchList = new ArrayList<>();
        for (Store s : storeList) {
            if (s.getStoreName().toLowerCase().contains(text.toLowerCase())) {
                searchList.add(s);
            }
        }
        adapter.searchDataList(searchList);
    }

    public void anhXa(){
        recyclerView = findViewById(R.id.rvSearch);
        searchView = findViewById(R.id.svSearch);
    }

}

