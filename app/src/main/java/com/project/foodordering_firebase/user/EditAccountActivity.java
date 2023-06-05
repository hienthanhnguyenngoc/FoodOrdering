package com.project.foodordering_firebase.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.foodordering_firebase.R;
import com.project.foodordering_firebase.model.User;

import java.util.HashMap;
import java.util.Map;

public class EditAccountActivity extends AppCompatActivity {

    EditText mphoneNumber, mfullName, mpassWord;
    TextView muserName;
    Button save;
    FirebaseDatabase db;
    DatabaseReference reference;
    String displayUsername, displayPhone, displayFullname, displayPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        //ánh xạ
        muserName = findViewById(R.id.tvUserName);
        mpassWord = findViewById(R.id.editPassword);
        mphoneNumber = findViewById(R.id.editPhoneNumber);
        mfullName = findViewById(R.id.editFullName);
        save = findViewById(R.id.btnSave);

        //lấy username từ info activity
        Bundle b = getIntent().getExtras();
        displayUsername = b.getString("userName");
        displayPassword = b.getString("passWord");
        displayPhone = b.getString("phoneNumber");
        displayFullname = b.getString("fullName");
        muserName.setText(displayUsername);
        mpassWord.setText(displayPassword);
        mphoneNumber.setText(displayPhone);
        mfullName.setText(displayFullname);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = muserName.getText().toString();
                String phoneNumber = mphoneNumber.getText().toString();
                String fullName = mfullName.getText().toString();
                String password = mpassWord.getText().toString();

                db = FirebaseDatabase.getInstance();
                reference = db.getReference("User");

                if (fullName.isEmpty() || phoneNumber.isEmpty()) {
                    Toast.makeText(EditAccountActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }

                else {
                    db = FirebaseDatabase.getInstance();
                    reference = db.getReference("User").child(userName);

                    Map<String, Object> map = new HashMap<>();
                    //map.put("userName", userName);
                    map.put("passWord", password);
                    map.put("phoneNumber", phoneNumber);
                    map.put("fullName", fullName);

                    reference.updateChildren(map)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getApplicationContext(), "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(EditAccountActivity.this, InfoActivity.class);
                                    i.putExtra("userName", userName);

                                    startActivity(i);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Chỉnh sửa thông tin thất bại", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });

                }
            }

        });

    }
}