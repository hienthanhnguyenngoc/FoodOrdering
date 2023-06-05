package com.project.foodordering_firebase.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.foodordering_firebase.R;
import com.project.foodordering_firebase.model.User;

public class RegisterActivity extends AppCompatActivity {

    EditText username, pwd, fullname, phone;
    Button register;
    TextView login;

    FirebaseDatabase db;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        anhXa();

        //nhan nut dang ky
        register.setOnClickListener(view -> {
            //lay thong tin tu Edit Text
            String getUsername = username.getText().toString().trim();
            String getPwd = pwd.getText().toString().trim();
            String getFullname = fullname.getText().toString().trim();
            String getPhone = phone.getText().toString().trim();

            //rang buoc do dai
            int l_username = getUsername.length();
            int l_phone = getPhone.length();
            int l_pass = getPwd.length();

            if (getUsername.isEmpty() || getPhone.isEmpty() || getFullname.isEmpty() || getPwd.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else if (l_username <= 4) {
                Toast.makeText(getApplicationContext(), "Vui lòng đặt tên người dùng dài hơn 4 ký tự", Toast.LENGTH_SHORT).show();
            } else if (l_phone != 10) {
                Toast.makeText(getApplicationContext(), "Vui lòng điền đúng định dạng số điện thoại", Toast.LENGTH_SHORT).show();
            } else if (l_pass <= 4) {
                Toast.makeText(getApplicationContext(), "Mật khẩu quá ngắn! Vui lòng đặt mật khẩu dài hơn 4 ký tự", Toast.LENGTH_SHORT).show();
            }

            else {
                db = FirebaseDatabase.getInstance();
                reference = db.getReference("User");

                User user = new User(getUsername, getFullname, getPhone, getPwd);

                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(getUsername)) {
                            Toast.makeText(getApplicationContext(), "Tên người dùng đã được đăng ký", Toast.LENGTH_SHORT).show();
                        }

                        else if (snapshot.hasChild(getPhone)) {
                            Toast.makeText(getApplicationContext(), "Số điện thoại đã được đăng ký", Toast.LENGTH_SHORT).show();
                        }

                        else {
                            reference.child(getUsername).setValue(user).addOnCompleteListener(task -> {
                                username.setText("");
                                pwd.setText("");
                                phone.setText("");
                                fullname.setText("");
                                Toast.makeText(getApplicationContext(), "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(i);
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        //nhan text view dang nhap
        login.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
        });

    }

    public void anhXa() {
        username = findViewById(R.id.ed_username);
        pwd = findViewById(R.id.ed_pwd);
        fullname = findViewById(R.id.ed_fullname);
        phone = findViewById(R.id.ed_phone);
        register = findViewById(R.id.btnRegister);
        login = findViewById(R.id.tvLogin);
    }
}