package com.project.foodordering_firebase.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.location.GnssAntennaInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.project.foodordering_firebase.R;
import com.project.foodordering_firebase.adapter.CartAdapter;
import com.project.foodordering_firebase.adapter.FoodAdapter_User;
import com.project.foodordering_firebase.adapter.StoreAdapter_User;
import com.project.foodordering_firebase.model.FavStore;
import com.project.foodordering_firebase.model.Food;
import com.project.foodordering_firebase.model.Store;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;

public class ViewStoreActivity extends AppCompatActivity {

    ImageView back, storeImg, cart;
    TextView sName, address, status;
    RecyclerView recyclerView;
    FoodAdapter_User foodAdapter_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_store);
        anhXa();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Nhận biến từ ListStoreActivity
        Intent intent = getIntent();
        String storeName = intent.getStringExtra("storeName");
        String storeImage = intent.getStringExtra("storeImg");
        String sAddress = intent.getStringExtra("address");
        String userName = intent.getStringExtra("userName");
        String storeCate = intent.getStringExtra("storeCate");

        //set
        sName.setText(storeName);
        address.setText(sAddress);
        Glide.with(this)
                .load(storeImage)
                .apply(new RequestOptions()
                        .override(ViewGroup.LayoutParams.MATCH_PARENT, 200))
                .into(storeImg);

        FirebaseRecyclerOptions<Food> options =
                new FirebaseRecyclerOptions.Builder<Food>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Food")
                                .orderByChild("storeName").equalTo(storeName), Food.class)
                        .build();


        foodAdapter_user = new FoodAdapter_User(options, getApplicationContext(), userName);
        recyclerView.setAdapter(foodAdapter_user);


        //cart
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewStoreActivity.this, CartActivity.class);
                intent.putExtra("userName", userName);
                intent.putExtra("storeCate", storeCate);
                startActivity(intent);
            }
        });

        //quay lại
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();

//                Intent intent = new Intent(ViewStoreActivity.this, ListStoreActivity.class);
//                intent.putExtra("userName", userName);
//                intent.putExtra("storeCate", storeCate);
//                startActivity(intent);
            }
        });

        // yeu thich
        ToggleButton toggleButton = findViewById(R.id.toggleButton);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // heart icon is now red
                    buttonView.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.red)));
                    DatabaseReference storesRef = FirebaseDatabase.getInstance().getReference("Store");
                    Query query = storesRef.orderByChild("storeName").equalTo(storeName);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot storeSnapshot : dataSnapshot.getChildren()) {
                                String storeCategory = storeSnapshot.child("storeCate").getValue(String.class);
                                if (storeCategory.equals(storeCate)) {
                                    String storeID = storeSnapshot.child("storeID").getValue(String.class);
                                    // storeID
                                    FavStore favStore = new FavStore(userName, storeID, storeName, storeImage, sAddress);

                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("FavStore").child(userName);
                                    reference.child(storeID).setValue(favStore).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(ViewStoreActivity.this, "Đã thêm vào cửa hàng yêu thích", Toast.LENGTH_SHORT).show();
                                            SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPref.edit();
                                            editor.putBoolean("toggle_state", toggleButton.isChecked());
                                            editor.apply();
                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Handle database error
                        }
                    });


                }

                else {
                    // heart icon is now white
                    buttonView.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.white)));
                    DatabaseReference storesRef = FirebaseDatabase.getInstance().getReference("Store");
                    Query query = storesRef.orderByChild("storeName").equalTo(storeName);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot storeSnapshot : snapshot.getChildren()) {
                                String storeCategory = storeSnapshot.child("storeCate").getValue(String.class);
                                if (storeCategory.equals(storeCate)) {
                                    String storeID = storeSnapshot.child("storeID").getValue(String.class);

                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("FavStore").child(userName).child(storeID);
                                    reference.removeValue();
                                    Toast.makeText(ViewStoreActivity.this, "Đã hủy yêu thích", Toast.LENGTH_SHORT).show();
                                    SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putBoolean("toggle_state", toggleButton.isChecked());
                                    editor.apply();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

        });


        //
        Date currentDate = new Date();
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
        String timeText = timeFormat.format(currentDate);

        // Get the current time
        LocalTime time = LocalTime.now();

        // Create the start and end times for the closed period
        LocalTime closedStartTime = LocalTime.of(22, 0); // 10:00 PM
        LocalTime closedEndTime = LocalTime.of(7, 0); // 07:00 AM

        // Check if the current time is within the closed period
        if (time.isAfter(closedStartTime) || time.isBefore(closedEndTime)) {
            status.setText("Đã đóng cửa");
        } else {
            status.setText("Đang mở cửa");
        }

        //line item
        DividerItemDecoration itemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.devider));
        recyclerView.addItemDecoration(itemDecoration);

    }

    public void anhXa(){
        back = findViewById(R.id.back);
        storeImg = findViewById(R.id.storeImg);
        sName = findViewById(R.id.tvStoreName);
        address = findViewById(R.id.tvAddress);
        status = findViewById(R.id.status);
        recyclerView = findViewById(R.id.rvFood);
        cart = findViewById(R.id.iconCart);
    }

    @Override
    public void onStart() {
        super.onStart();
        foodAdapter_user.startListening();
//        if (foodAdapter_user != null) {
//            foodAdapter_user.startListening();
//        }
    }

//    @Override
//    public void onStop() {
//        super.onStop();
//        foodAdapter_user.stopListening();
////        if (foodAdapter_user != null) {
////            foodAdapter_user.stopListening();
////        }
//    }

}