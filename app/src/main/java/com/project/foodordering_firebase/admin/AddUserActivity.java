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
import com.project.foodordering_firebase.model.User;

public class AddUserActivity extends AppCompatActivity {

    EditText addUserName, addPassWord, addPhoneNumber, addFullName;
    Button btnAddUser;

    FirebaseDatabase db;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        addUserName = findViewById(R.id.addUserName);
        addPassWord = findViewById(R.id.addPassWord);
        addPhoneNumber = findViewById(R.id.addPhoneNumber);
        addFullName = findViewById(R.id.addFullName);
        btnAddUser = findViewById(R.id.btnAddUser);

        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //get data from edit text
                String userName = addUserName.getText().toString();
                String fullName = addFullName.getText().toString();
                String phoneNumber = addPhoneNumber.getText().toString();
                String passWord = addPassWord.getText().toString();

                if(!userName.isEmpty() && !fullName.isEmpty()
                        && !phoneNumber.isEmpty() && !passWord.isEmpty()) {

                    //xếp tham số theo constructor User
                    User user = new User(userName, fullName, passWord, phoneNumber);

                    db = FirebaseDatabase.getInstance();
                    reference = db.getReference("User");

                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(userName)) {
                                Toast.makeText(getApplicationContext(), "Tên người dùng đã tồn tại", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                reference.child(userName).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        addFullName.setText("");
                                        addPassWord.setText("");
                                        addUserName.setText("");
                                        addPhoneNumber.setText("");
                                        Toast.makeText(getApplicationContext(), "Thêm tài khoản thành công", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(getApplicationContext(), ListUserActivity.class);
                                        startActivity(i);
                                    }
                                });
                            }
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