package com.project.foodordering_firebase.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
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
import com.project.foodordering_firebase.admin.AdminHomeActivity;

public class LoginActivity extends AppCompatActivity {

    EditText username, pwd;
    Button login;
    TextView register;

    FirebaseDatabase db;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        anhXa();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //lay thong tin tu Edit Text
                String getUsername = username.getText().toString().trim();
                String getPwd = pwd.getText().toString().trim();

                db = FirebaseDatabase.getInstance();
                reference = db.getReference("User");

                if (getUsername.isEmpty() || getPwd.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }

                if (getUsername.equals("admin") && getPwd.equals("0123456")) {
                    Toast.makeText(LoginActivity.this,"Đăng nhập thành công!",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), AdminHomeActivity.class);
                    startActivity(i);
                }
                else {
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(getUsername)){
                                // username is exist in firebase database
                                // now get password of user from firebase data and match it with user entered password

                                final String confirmPw = snapshot.child(getUsername).child("passWord").getValue(String.class);

                                if(confirmPw.equals(getPwd)) {
                                    //
                                    Toast.makeText(LoginActivity.this,"Đăng nhập thành công!",Toast.LENGTH_SHORT).show();
                                    String phoneFromDB = snapshot.child(getUsername).child("phoneNumber").getValue(String.class);
                                    String fullNameFromDB = snapshot.child(getUsername).child("fullName").getValue(String.class);
                                    String passwordFromBD = snapshot.child(getUsername).child("passWord").getValue(String.class);


                                    Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                                    Bundle b = new Bundle();
                                    b.putString("userName",getUsername);
                                    b.putString("phoneNumber",phoneFromDB);
                                    b.putString("fullName",fullNameFromDB);
                                    b.putString("passWord",passwordFromBD);

                                    i.putExtra("login_status", true);
                                    i.putExtras(b);
                                    startActivity(i);
                                }
                                else {
                                    Toast.makeText(LoginActivity.this,"Sai mật khẩu",Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(LoginActivity.this,"Tài khoản không tồn tại",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
            }
        });
    }

    public void anhXa() {
        username = findViewById(R.id.ed_username);
        pwd = findViewById(R.id.ed_pwd);
        login = findViewById(R.id.btnLogin);
        register = findViewById(R.id.tvRegister);
    }

}