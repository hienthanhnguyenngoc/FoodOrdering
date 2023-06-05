package com.project.foodordering_firebase.admin;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;
import com.project.foodordering_firebase.R;
import com.project.foodordering_firebase.adapter.StoreAdapter;
import com.project.foodordering_firebase.model.Store;
import com.project.foodordering_firebase.user.InfoActivity;
import com.project.foodordering_firebase.user.ViewStoreActivity;

public class ListStoreActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    StoreAdapter storeAdapter;
    ImageView back;
    Context mContext;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_store2);

        anhXa();

        //Nhận biến userName từ HomeActivity
        Intent intent = getIntent();
        String mUserName = intent.getStringExtra("userName");

        //Gửi biến userName qua ViewStoreActivity
        Bundle bundle = new Bundle();
        bundle.putString("userName", mUserName);
        Intent i = new Intent(getApplicationContext(), ViewStoreActivity.class);
        i.putExtras(bundle);


        RecyclerView recyclerView = findViewById(R.id.rvListStore);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Store> options =
                new FirebaseRecyclerOptions.Builder<Store>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Store"),Store.class)
                        .setLifecycleOwner((LifecycleOwner) mContext) // add this line
                        .build();


        storeAdapter = new StoreAdapter(mContext, options);
        recyclerView.setAdapter(storeAdapter);


        floatingActionButton = findViewById(R.id.fabStore);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AddStoreActivity.class);
                startActivity(i);
            }
        });

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AdminHomeActivity.class);
                startActivity(i);
            }
        });

        //line item
        DividerItemDecoration itemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.devider));
        recyclerView.addItemDecoration(itemDecoration);

    }

    public void anhXa(){
        recyclerView = findViewById(R.id.rvListStore);
        back = findViewById(R.id.back);
        floatingActionButton = findViewById(R.id.fabStore);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (storeAdapter != null) {
            storeAdapter.startListening();
        }
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (storeAdapter != null) {
//            storeAdapter.stopListening();
//        }
//    }
}