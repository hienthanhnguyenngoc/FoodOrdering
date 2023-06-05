package com.project.foodordering_firebase.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.foodordering_firebase.MainActivity;
import com.project.foodordering_firebase.R;
import com.project.foodordering_firebase.model.FavStore;


public class InfoActivity extends AppCompatActivity {

    ImageView back;
    LinearLayout history, favorite;
    TextView username, more;

    String displayUsername, displayPhone, displayFullname;
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        back = findViewById(R.id.backFromInfo);
        btnLogout = findViewById(R.id.btnLogout);
        username = findViewById(R.id.tv_Info_UserName);
        history = findViewById(R.id.history);
        favorite = findViewById(R.id.favorite);
        more = findViewById(R.id.tv_more);

        Bundle b = getIntent().getExtras();
        displayUsername = b.getString("userName");
        displayPhone = b.getString("phoneNumber");
        displayFullname = b.getString("fullName");
        username.setText(displayUsername);

        //quay lại
        back.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), HomeActivity.class);
            i.putExtra("userName", displayUsername);
            startActivity(i);
        });

        //đăng xuất
        btnLogout.setOnClickListener(view -> {
            Intent i = new Intent(InfoActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        });

        //thông tin chi tiết
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("userName", displayUsername);
                bundle.putString("passWord", pushPassword());


                Intent intent = new Intent(getApplicationContext(), EditAccountActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        //quán ăn yêu thích
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), FavStoreActivity.class);
                i.putExtra("userName", displayUsername);
                startActivity(i);
            }
        });

        //lịch sử đơn hàng
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), HistoryBookingActivity.class);
                i.putExtra("userName", displayUsername);
                startActivity(i);
            }
        });

    }
    public String pushPassword(){
        //nhan username tu Login Page
        String password = "";
        Bundle b = getIntent().getExtras();
        if (b != null) {
            password = b.getString("passWord");
        }
        return password;
    }



}