package com.project.foodordering_firebase.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.project.foodordering_firebase.R;
import com.project.foodordering_firebase.adapter.StoreAdapter;
import com.project.foodordering_firebase.adapter.StoreAdapter_User;
import com.project.foodordering_firebase.model.Store;

public class ListStoreActivity extends AppCompatActivity {

    ImageView back;
    TextView title;
    String username, storeCate;
    StoreAdapter_User storeAdapter_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_store);

        anhXa();
        getTitleCate();

        Intent intent = getIntent();
        username = intent.getStringExtra("userName");
        storeCate = intent.getStringExtra("storeCate");

        RecyclerView recyclerView = findViewById(R.id.rcvStore);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        FirebaseRecyclerOptions<Store> options =
                new FirebaseRecyclerOptions.Builder<Store>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Store")
                                .orderByChild("storeCate").equalTo(getTitleCate()), Store.class)
                        .build();


        storeAdapter_user = new StoreAdapter_User(getApplicationContext(), options, username);
        recyclerView.setAdapter(storeAdapter_user);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListStoreActivity.this, HomeActivity.class);
                intent.putExtra("userName", username);
                intent.putExtra("storeCate", storeCate);
                startActivity(intent);
            }
        });

        //line item
        DividerItemDecoration itemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.devider));
        recyclerView.addItemDecoration(itemDecoration);



    }

    public void anhXa(){
        title = findViewById(R.id.tvCate);
        back = findViewById(R.id.back);
    }

    public String getTitleCate(){
        Bundle extras = getIntent().getExtras();
        String category = "";
        if (extras != null) {
            category = getIntent().getStringExtra("storeCate");
            title.setText(category);
        }
        return category;
    }

    @Override
    protected void onStart() {
        super.onStart();
//        if (storeAdapter_user != null) {
//            storeAdapter_user.startListening();
//        }
        storeAdapter_user.startListening();
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
////        if (storeAdapter_user != null) {
////            storeAdapter_user.stopListening();
////        }
//        storeAdapter_user.stopListening();
//    }
}