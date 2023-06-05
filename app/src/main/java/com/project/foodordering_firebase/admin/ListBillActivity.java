package com.project.foodordering_firebase.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.project.foodordering_firebase.R;
import com.project.foodordering_firebase.adapter.BillAdapter;
import com.project.foodordering_firebase.adapter.StoreAdapter;
import com.project.foodordering_firebase.model.Bill;
import com.project.foodordering_firebase.model.Store;

public class ListBillActivity extends AppCompatActivity {

    private String userName;
    RecyclerView recyclerView;
    BillAdapter billAdapter;
    ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_bill);

        // Get the user name from the intent extras
        Intent intent = getIntent();
        userName = intent.getStringExtra("userName");

        recyclerView = findViewById(R.id.rvListBill);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Bill> options =
                new FirebaseRecyclerOptions.Builder<Bill>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Bill").child(userName)
                                .orderByChild("userName").equalTo(userName), Bill.class)
                        .build();


        billAdapter = new BillAdapter(getApplicationContext(),options,userName);
        recyclerView.setAdapter(billAdapter);

        //line item
        DividerItemDecoration itemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.devider));
        recyclerView.addItemDecoration(itemDecoration);

        back = findViewById(R.id.back);
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
        if (billAdapter != null) {
            billAdapter.startListening();
        }
    }
}