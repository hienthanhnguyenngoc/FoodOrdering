package com.project.foodordering_firebase.user;

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
import com.google.firebase.database.FirebaseDatabase;
import com.project.foodordering_firebase.R;
import com.project.foodordering_firebase.adapter.BillAdapter;
import com.project.foodordering_firebase.model.Bill;
import com.project.foodordering_firebase.model.Cart;

public class HistoryBookingActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    BillAdapter billAdapter;
    ImageView back;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_booking);

        anhXa();

        Bundle b = getIntent().getExtras();
        String userName = b.getString("userName");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<Bill> options =
                new FirebaseRecyclerOptions.Builder<Bill>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Bill").child(userName)
                                .orderByChild("userName").equalTo(userName), Bill.class)
                        .build();

        billAdapter = new BillAdapter(context, options,userName);
        recyclerView.setAdapter(billAdapter);

        //line item
        DividerItemDecoration itemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.devider));
        recyclerView.addItemDecoration(itemDecoration);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), InfoActivity.class);
                i.putExtra("userName", userName);
                startActivity(i);
            }
        });
    }
    public void anhXa() {
        recyclerView = findViewById(R.id.rvhistoryorder);
        back = findViewById(R.id.backFromHistoryOrder);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (billAdapter != null) {
            billAdapter.startListening();
        }
    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (billAdapter != null) {
//            billAdapter.stopListening();
//        }
//    }

}

