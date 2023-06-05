package com.project.foodordering_firebase.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.foodordering_firebase.R;
import com.project.foodordering_firebase.model.Store;

public class AddStoreActivity extends AppCompatActivity {

    EditText addSImg, addSName, addSAddress;
    TextView storeCate;
    Button btnAddStore;
    Spinner spinner;

    FirebaseDatabase db;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_store);

        storeCate = findViewById(R.id.storeCate);
        addSAddress = findViewById(R.id.addSAddress);
        addSName = findViewById(R.id.addSName);
        addSImg = findViewById(R.id.addSImg);
        btnAddStore = findViewById(R.id.btnAddStore);
        spinner = findViewById(R.id.spinner_store);

        String[] categories = {"Mì-Bún-Phở", "Cơm", "Tráng Miệng", "Đồ Ăn Nhanh", "Thức Uống", "Ăn Vặt"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        String selectedCategory = spinner.getSelectedItem().toString();

        btnAddStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //get data from edit text
                String storeName = addSName.getText().toString();
                String address = addSAddress.getText().toString();
                String storeImg = addSImg.getText().toString();
                String storeCate = spinner.getSelectedItem().toString();

                if(!storeName.isEmpty() && !address.isEmpty() && !storeImg.isEmpty() && spinner.getSelectedItem() != null) {

                    db = FirebaseDatabase.getInstance();
                    reference = db.getReference("Store");

                   // Store store = new Store(sName, sAddress,sImg);
                    String storeID = reference.push().getKey();
                    Store store = new Store(storeID, storeCate, storeName, storeImg, address);



                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            reference.child(storeID).setValue(store).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    spinner.setSelection(0);
                                    addSAddress.setText("");
                                    addSImg.setText("");
                                    addSName.setText("");
                                    Toast.makeText(getApplicationContext(), "Thêm cửa hàng thành công", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getApplicationContext(), ListStoreActivity.class);
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
                    Toast.makeText(getApplicationContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}