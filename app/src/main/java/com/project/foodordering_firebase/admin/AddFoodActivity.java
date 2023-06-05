package com.project.foodordering_firebase.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.foodordering_firebase.R;
import com.project.foodordering_firebase.model.Food;
import com.project.foodordering_firebase.model.Store;

public class AddFoodActivity extends AppCompatActivity {

    EditText getFoodName, getFoodDes, getPrice, getFoodImg;
    Button add;

    FirebaseDatabase db;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
        anhXa();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get data from edit text
                String foodName = getFoodName.getText().toString();
                String foodDes = getFoodDes.getText().toString();
                String price = getPrice.getText().toString();
                String foodImg = getFoodImg.getText().toString();

                String storeName = getStoreName();
                String storeCate = getStoreCate();

                if (!foodName.isEmpty() && !foodDes.isEmpty() && !price.isEmpty() && !foodImg.isEmpty()){
                    db = FirebaseDatabase.getInstance();
                    reference = db.getReference("Food");

                    String foodID = reference.push().getKey();
                    Food food = new Food(foodID, foodName, price, foodDes, foodImg, storeCate, storeName);

                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            reference.child(foodID).setValue(food).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    getFoodName.setText("");
                                    getFoodDes.setText("");
                                    getPrice.setText("");
                                    getFoodImg.setText("");
                                    Toast.makeText(getApplicationContext(), "Thêm món ăn thành công", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getApplicationContext(), MenuActivity.class);
                                    startActivity(i);
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }
                else {
                    Toast.makeText(AddFoodActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    public void anhXa(){
        getFoodName = findViewById(R.id.addFName);
        getFoodDes = findViewById(R.id.addFDes);
        getPrice = findViewById(R.id.addPrice);
        getFoodImg = findViewById(R.id.addFImg);
        add = findViewById(R.id.btnAddFood);
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
}