package com.project.foodordering_firebase.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.project.foodordering_firebase.model.Bill;
import com.project.foodordering_firebase.model.Cart;
import com.project.foodordering_firebase.model.Store;

import java.text.DecimalFormat;

public class DetailOrderActivity extends AppCompatActivity {
    TextView bOrderID, bFullName, bPhone, bAddress, bTotalPrice, bDateTime;
    ImageView back;
    private String formattedTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order);

        anhXa();
        showBill();

        Bundle b = getIntent().getExtras();
        String userName = b.getString("userName");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
//                Intent i = new Intent(getApplicationContext(), HistoryBookingActivity.class);
//                i.putExtra("userName", userName);
//                startActivity(i);
            }
        });

    }

    public void showBill() {
        Intent i = getIntent();

        String fullName = i.getStringExtra("fullName");
        String orderID = i.getStringExtra("billID");
        String datetime = i.getStringExtra("datetime");
        String phone = i.getStringExtra("phoneNumber");
        String address = i.getStringExtra("address");
        Double totalprice = i.getDoubleExtra("totalPrice",0);

        DecimalFormat formatter = new DecimalFormat("#,###");
        formattedTotal = formatter.format(totalprice);

        bFullName.setText(fullName);
        bOrderID.setText(orderID);
        bDateTime.setText(datetime);
        bPhone.setText(phone);
        bAddress.setText(address);
        bTotalPrice.setText(formattedTotal);
    }


    public void anhXa() {
        bOrderID = findViewById(R.id.tvOrderID);
        bFullName = findViewById(R.id.tvFullName);
        bPhone = findViewById(R.id.tvPhone);
        bAddress = findViewById(R.id.tvAddress);
        bTotalPrice = findViewById(R.id.totalPrice);
        bDateTime = findViewById(R.id.tvDateTime);
        back = findViewById(R.id.ivBackDetail);
    }


}



